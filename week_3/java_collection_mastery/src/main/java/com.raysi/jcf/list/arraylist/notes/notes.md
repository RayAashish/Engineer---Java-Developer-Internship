---
title: "java.util.ArrayList - JDK 17/21 Internals"
author: raysi
package: com.raysi.jcf.list.arraylist
jdk: "17 / 21 (HotSpot, 64-bit, compressed oops)"
source: src/java.base/share/classes/java/util/ArrayList.java
---

# 1. Architectural Overview & Hierarchy

## 1.1 Exact signature

```java
package java.util;

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

| Interface / class | Why it matters |
|---|---|
| `AbstractList<E>` | Supplies `protected transient int modCount`, the default `Itr`/`ListItr` skeletons, `equals`/`hashCode`, and `removeRange`. |
| `List<E>` | Positional-access contract. **JDK 21:** `List` now extends `SequencedCollection<E>`, so `ArrayList` gets `getFirst/getLast/addFirst/addLast/removeFirst/removeLast/reversed()` (ArrayList overrides the first six directly). |
| `RandomAccess` | Marker. `Collections.binarySearch/shuffle/fill/reverse` pick index-based algorithms when they see it. |
| `Cloneable` | `clone()` is shallow: copies `Arrays.copyOf(elementData, size)` and resets the clone's `modCount` to 0. |
| `Serializable` | Custom `writeObject/readObject` (see 2.1) because `elementData` is `transient`. |

## 1.2 ASCII inheritance diagram

```
                    Iterable<E>
                        ^
                  Collection<E>
                        ^
              SequencedCollection<E>        <-- JDK 21+ only (JDK 17: List extends Collection)
                        ^
   RandomAccess     List<E>        Cloneable     Serializable
        ^             ^  ^             ^              ^
        |             |  |             |              |
        |   AbstractCollection<E>      |              |
        |             ^                |              |
        |       AbstractList<E> -------+--------------+     (holds modCount)
        |        ^          ^
        |        |          +-------------------+
        |        |                              |
        +---- ArrayList<E>  <== YOU ARE HERE    AbstractSequentialList<E>
                 ^                                    ^
                 |                                    |
        (Vector -> Stack are siblings               LinkedList<E>
         under AbstractList)        CopyOnWriteArrayList<E> implements List directly
```

## 1.3 Why does this class exist?

`ArrayList` is the **resizable, array-backed, unsynchronized** `List`. It solves problems its siblings cannot:

| Sibling | Where it loses to `ArrayList` |
|---|---|
| `E[]` (raw array) | Fixed length; no `add/remove/contains`, no generics-safe API, no `Collection` interop. |
| `Vector` | Every method is `synchronized` (uncontended lock cost + false safety: compound actions still race); default doubling growth wastes memory. |
| `LinkedList` | O(1) `get(i)` vs O(n); 2x the per-element footprint; pointer chasing destroys cache locality. Its O(1) "insert in middle" is only O(1) *after* you already hold the node. |
| `CopyOnWriteArrayList` | Every write copies the entire array (O(n)); only worth it for read-mostly, snapshot-iterating workloads. |

Design bet: **sequential memory + a cheap `System.arraycopy` beats pointer structures** for the overwhelming majority of real workloads.

---

# 2. Internal Data Structure & Memory Layout (JDK Source Code Level)

## 2.1 Fields (JDK 17/21, abridged)

```java
private static final long serialVersionUID = 8683452581122892189L;

private static final int DEFAULT_CAPACITY = 10;

// Shared sentinel for `new ArrayList(0)` / `new ArrayList(emptyCollection)` / after trimToSize() on empty
private static final Object[] EMPTY_ELEMENTDATA = {};

// Shared sentinel for `new ArrayList()`; a DIFFERENT instance so grow() can tell
// "never allocated, jump to 10" from "explicitly empty, grow minimally".
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

// Package-private (not private) so nested SubList/Itr avoid synthetic accessors.
// transient: serialized manually so spare capacity is not written to the stream.
transient Object[] elementData;

private int size;                       // logical size, NOT capacity

// inherited from AbstractList
protected transient int modCount = 0;   // structural-modification counter
```

`writeObject` writes `size` and then only `elementData[0..size)`, then re-checks `modCount` and throws `ConcurrentModificationException` if the list changed mid-serialization.

## 2.2 ASCII memory diagram (HotSpot, 64-bit, compressed oops: 4-byte refs)

```
 STACK                         HEAP
 +-----------+       +-----------------------------+
 | list  ----+-----> | ArrayList object   (24 B)   |
 +-----------+       |  mark word + klass   12 B   |
                     |  modCount  (int)      4 B   |
                     |  elementData (ref)    4 B --+--+
                     |  size (int)           4 B   |  |
                     +-----------------------------+  |
                                                      v
   Object[] (one contiguous block: 16 B header incl. length + 4 B x capacity)
   +------+------+------+------+------+------+------+------+
   | ref0 | ref1 | ref2 | ref3 | ref4 | null | null | null |   capacity = 8, size = 5
   +--+---+--+---+--+---+--+---+--+---+------+------+------+
      |      |      |      |      |        ^-- spare slots (always null: GC hygiene)
      v      v      v      v      v
   [E0]   [E1]   [E2]   [E3]   [E4]      <- each element is its OWN heap object
   (e.g. Integer: 12 B header + 4 B value = 16 B)  - placed wherever the allocator/GC put them
```

## 2.3 CPU cache and memory-overhead analysis

| Metric (compressed oops) | `ArrayList<Integer>` | `LinkedList<Integer>` | `int[]` |
|---|---|---|---|
| Container overhead | 24 B + 16 B array header (once) | 24 B `LinkedList` shell | 16 B header (once) |
| Per element: structure | **4 B** (one reference slot) | **24 B** `Node` (12 header + `item`/`next`/`prev` = 3 x 4 B) | 4 B (the value itself) |
| Per element: payload | 16 B `Integer` (cache -128..127 shared) | 16 B `Integer` | none |
| **Total per element** | **~20 B** (+ up to 33-50% spare capacity) | **~40 B** | **4 B** |

Cache behavior:

- **Reference array is contiguous.** A 64 B cache line holds 16 references, so scanning `elementData` is a prefetcher-friendly linear walk. `System.arraycopy` is a vectorized memmove that streams through memory at close to bandwidth speed.
- **Elements are not contiguous.** `ArrayList<Integer>` still dereferences each reference to read the value. Objects allocated back-to-back in a TLAB usually end up adjacent, but GC compaction/copying can reorder them. This is why `int[]` (or Valhalla value classes in the future) is the real cache-friendly structure; `ArrayList` is only "cache-friendly for the pointers".
- **LinkedList chases pointers on every hop**, with each `Node` and each element on potentially different cache lines/pages, so traversal is dominated by cache misses and defeats the hardware prefetcher. Its theoretical O(1) insert is irrelevant if finding the position costs O(n) misses.
- **Capacity slack:** growth by 1.5x leaves between 0% and ~33% of the array unused (vs. up to 50% for doubling), but after a grow the array is a fresh allocation and the old one is garbage; large lists land in humongous/old-gen regions with real GC cost. Presize when you know n.

---

# 3. Step-by-Step Internal Working of Core Operations

## 3.1 Instantiation (constructors)

```java
public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;      // LAZY: no 10-slot array yet
}

public ArrayList(int initialCapacity) {
    if (initialCapacity > 0)       this.elementData = new Object[initialCapacity];   // EAGER
    else if (initialCapacity == 0) this.elementData = EMPTY_ELEMENTDATA;
    else throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
}

public ArrayList(Collection<? extends E> c) {
    Object[] a = c.toArray();
    if ((size = a.length) != 0) {
        if (c.getClass() == ArrayList.class) elementData = a;                         // reuse the fresh copy
        else elementData = Arrays.copyOf(a, size, Object[].class);                    // defend against covariant toArray()
    } else elementData = EMPTY_ELEMENTDATA;
}
```

- **Lazy default:** since JDK 7u40/8, `new ArrayList<>()` allocates nothing until the first `add`. Millions of empty lists (DTO fields, DB result holders) cost only the 24 B shell.
- **No load factor.** That concept belongs to hash tables. `ArrayList` has a growth factor (1.5x) only.
- **Two sentinels** matter: `new ArrayList<>(0)` then `add` grows `1 -> 2 -> 3 -> 4 -> 6 -> 9 -> 13` (verified in section 6), *not* `10 -> 15`.
- The `c.getClass() == ArrayList.class` check exists because a subclass or `Arrays.asList(...)` could return a `String[]` from `toArray()`, and storing into that later would throw `ArrayStoreException` (JDK-6260652; `Arrays.asList` itself was fixed in JDK 9, but the defensive copy remains).

## 3.2 Insertion (`add`) and the exact grow math

```java
public boolean add(E e) {
    modCount++;                       // structural modification
    add(e, elementData, size);
    return true;
}

private void add(E e, Object[] elementData, int s) {   // small, hot, JIT-inlinable
    if (s == elementData.length)
        elementData = grow();                          // full -> reallocate
    elementData[s] = e;                                // (1) store reference
    size = s + 1;                                      // (2) publish new size  (NOT atomic with (1))
}

private Object[] grow() { return grow(size + 1); }

private Object[] grow(int minCapacity) {
    int oldCapacity = elementData.length;
    if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        int newCapacity = ArraysSupport.newLength(oldCapacity,
                minCapacity - oldCapacity,   /* minimum growth   */
                oldCapacity >> 1);           /* preferred growth */
        return elementData = Arrays.copyOf(elementData, newCapacity);
    } else {
        return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];   // first add: 10
    }
}
```

`ArraysSupport.newLength(oldLength, minGrowth, prefGrowth)`:

```java
int prefLength = oldLength + Math.max(minGrowth, prefGrowth);   // old + max(need, old>>1)
if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH)      // SOFT_MAX = Integer.MAX_VALUE - 8
    return prefLength;
else
    return hugeLength(oldLength, minGrowth);   // int overflow -> OutOfMemoryError; else clamp to SOFT_MAX or exact minLength
```

**Bitwise arithmetic:** `oldCapacity >> 1` is floor(old/2), so growth is `new = old + floor(old/2)`:

```
10 -> 15 -> 22 -> 33 -> 49 -> 73 -> 109 -> 163 -> ...
15 + 7 = 22   (7 = 15>>1, floor, not 7.5)
```

- JDK 6 used `(old * 3)/2 + 1`; JDK 7/8 used `old + (old >> 1)`; JDK 9+ routes through `ArraysSupport.newLength` to centralise the overflow/VM-limit rules.
- `addAll(c)` and `ensureCapacity(n)` request an *exact* `minCapacity`; `max(minGrowth, prefGrowth)` means a big bulk add can jump past 1.5x but never below what is needed.
- **Amortized proof sketch:** with growth factor g = 1.5, the total elements copied across all resizes while reaching n is at most n/(g-1) = 2n. So each append pays <= 2 amortized copies + 1 write, which is O(1) amortized, but a single unlucky append costs O(n).
- **Insert at index:**

```java
public void add(int index, E element) {
    rangeCheckForAdd(index);
    modCount++;
    final int s;  Object[] elementData;
    if ((s = size) == (elementData = this.elementData).length) elementData = grow();
    System.arraycopy(elementData, index, elementData, index + 1, s - index);   // shift tail right by 1
    elementData[index] = element;
    size = s + 1;
}
```

Inserting at head always moves all `size` references (memmove of 4 x size bytes).

## 3.3 Retrieval and deletion

```java
public E get(int index) {
    Objects.checkIndex(index, size);      // checks against SIZE, not capacity (slots past size are null garbage)
    return elementData(index);            // (E) elementData[index]   - unchecked cast, erased at runtime
}

public E remove(int index) {
    Objects.checkIndex(index, size);
    final Object[] es = elementData;
    @SuppressWarnings("unchecked") E oldValue = (E) es[index];
    fastRemove(es, index);
    return oldValue;
}

private void fastRemove(Object[] es, int i) {
    modCount++;
    final int newSize;
    if ((newSize = size - 1) > i)
        System.arraycopy(es, i + 1, es, i, newSize - i);   // shift tail LEFT by 1
    es[size = newSize] = null;                             // <-- GC hygiene: kill the stale duplicate reference
}
```

- **Why the `null`?** After the shift, slot `newSize` still holds a reference to the old last element. Without nulling, that object stays strongly reachable through `elementData` for as long as the list lives - a "loitering" leak, worst for long-lived caches/queues.
- **`clear()`** nulls every slot in `[0, size)` but **keeps the array** (capacity retained). Use `trimToSize()` (or reassign a new list) to release memory. `trimToSize()` on an empty list swaps back to `EMPTY_ELEMENTDATA`.
- **`remove(Object)`** is a linear `equals` scan (`o == null ? es[i] == null : o.equals(es[i])`) followed by the same `fastRemove`.
- **Bulk removal:** `removeAll/retainAll` use `batchRemove` (two-pointer compaction + null tail, in a `try/finally` so the list stays consistent if `contains` throws); JDK 9+ `removeIf` evaluates the predicate for all elements first into a bitset, then compacts once. Both are O(n) total, versus O(n^2) for a loop of `remove(i)`.
- **`subList(a,b).clear()`** delegates to `removeRange`: one arraycopy + one null-out loop.

---

# 4. Complexity & Behavioral Matrix

| Operation | Average | Worst case | Amortized | Notes (what actually happens) |
|---|---|---|---|---|
| Insert at **head** `add(0,e)` | O(n) | O(n) (+ resize copy) | O(n) | Always memmoves all `size` refs. |
| Insert at **tail** `add(e)` | O(1) | **O(n)** (resize) | **O(1)** | Resize copies `capacity` refs; <= ~2 copies/element overall. |
| Insert at **middle** `add(i,e)` | O(n) | O(n) | O(n) | Shifts `size - i` refs (n/2 on average). |
| Lookup by index `get(i)` | O(1) | O(1) | O(1) | Bounds check + array load. |
| Search by value `indexOf/contains` | O(n) | O(n) | - | Linear `equals`; O(log n) only via `Collections.binarySearch` on a *sorted* list. |
| Delete at **tail** `remove(size-1)` | O(1) | O(1) | O(1) | `arraycopy` length 0, then null. |
| Delete at **head/middle** | O(n) | O(n) | O(n) | Shift left; `remove(Object)` adds an O(n) search. |
| `sort` | O(n log n) | O(n log n) | - | `Arrays.sort(elementData, 0, size, c)` -> TimSort (stable); counts as modification. |

| Property | `ArrayList` |
|---|---|
| Ordering | Insertion/positional order guaranteed (it is a `List`; not sorted) |
| `null` elements | **Allowed**, any number (`remove(null)`, `indexOf(null)` handled) |
| Duplicates | **Allowed** |
| Iterator type | **Fail-fast, best-effort** (`modCount` check). `ListIterator` supports `add/set/previous`. |
| Thread-safety | **None** |
| Max size | Soft cap `Integer.MAX_VALUE - 8`; beyond that depends on VM, else `OutOfMemoryError` |

---

# 5. Concurrency & Thread-Safety Mechanics

## 5.1 How `modCount` triggers `ConcurrentModificationException`

```java
private class Itr implements Iterator<E> {
    int cursor;                          // index of next element to return
    int lastRet = -1;                    // index of last returned element, -1 if none
    int expectedModCount = modCount;     // snapshot at iterator creation

    public boolean hasNext() { return cursor != size; }          // NOTE: no modCount check

    public E next() {
        checkForComodification();                                // if (modCount != expectedModCount) throw CME
        int i = cursor;
        if (i >= size) throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length) throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }

    public void remove() {
        if (lastRet < 0) throw new IllegalStateException();
        checkForComodification();
        ArrayList.this.remove(lastRet);
        cursor = lastRet;  lastRet = -1;
        expectedModCount = modCount;                             // resync: the ONLY legal mutation path
    }
}
```

- Every structural change (`add`, `remove`, `clear`, `addAll`, `sort`, `trimToSize`, `ensureCapacity` that grows) bumps `modCount`; `set` does **not**.
- Detection happens in `next()`, **not** `hasNext()`. Removing the *second-to-last* element inside a for-each therefore exits silently (`cursor == size`), while any other position throws. The JavaDoc calls fail-fast "best-effort" and says not to rely on it for correctness.
- `forEach`, `removeIf`, `replaceAll`, `sort`, `writeObject`, and `SubList` also compare `modCount` against a snapshot (`forEach` checks after the loop and stops early).
- `subList` views carry the *root's* `modCount` snapshot: a structural change to the parent (not through the view) invalidates the view.

## 5.2 Why it is unsafe under threads

`add` is a compound, non-atomic sequence on shared state with **no happens-before edges**:

1. `s == elementData.length` check -> 2. maybe `grow()` publishes a *new* array -> 3. `elementData[s] = e` -> 4. `size = s + 1`.

Observable failure modes:

- **Lost updates:** two threads read the same `size`, write the same slot, both set `size = s + 1`. Final size < expected. (The demo below shows this empirically.)
- **`ArrayIndexOutOfBoundsException`:** a thread passes the capacity check on the old array while another fills the last slot / swaps arrays.
- **`null` holes / stale reads:** `size` becomes visible before the element write (or the write goes to the discarded old array after `grow`), and without `volatile` or locks readers can see stale `size`/`elementData`.
- **Non-deterministic `ConcurrentModificationException`** during iteration, and *silent* corruption with no exception at all.
- Parallel streams must never `forEach(list::add)`; use `collect(toList())` (per-thread containers, then merge).

## 5.3 Alternatives

| Need | Use | Mechanics / trade-off |
|---|---|---|
| Read-mostly list, safe iteration while writers run | **`java.util.concurrent.CopyOnWriteArrayList`** | Holds a `volatile`-published `Object[]`; every mutator takes a lock (`synchronized` on an internal lock object since JDK 9), copies the **whole** array, swaps it in. Iterators run on an immutable **snapshot**: never throw CME, never see later writes, and `Iterator.remove/set/add` throw `UnsupportedOperationException`. Writes are O(n). |
| Simple mutual exclusion | `Collections.synchronizedList(new ArrayList<>())` | Single monitor around each call; **iteration and check-then-act still require manual `synchronized (list)`**. |
| Legacy | `Vector` | Same coarse locking, 2x growth by default; avoid. |
| Producer/consumer | `ConcurrentLinkedQueue`, `LinkedBlockingQueue`, `ArrayBlockingQueue` | Purpose-built, no index access. |
| Immutability | `List.copyOf`, `List.of`, `Collections.unmodifiableList` (view) | Share freely, no locks. |

There is **no** JDK lock-free, index-addressable, growable list; `CopyOnWriteArrayList` is the only `java.util.concurrent` `List` implementation of this shape, which is why the choice is usually "immutable snapshot" vs "coarse lock".

---

# 6. Production-Grade Java Code Verification

File: `com/raysi/jcf/list/arraylist/codes/ArrayListInternalsDemo.java`. It uses reflection to read the private `elementData` and `modCount`, so JDK 16+ needs `--add-opens java.base/java.util=ALL-UNNAMED`.

```bash
javac -d out com/raysi/jcf/list/arraylist/codes/ArrayListInternalsDemo.java
java --add-opens java.base/java.util=ALL-UNNAMED -cp out com.raysi.jcf.list.arraylist.codes.ArrayListInternalsDemo
```

```java
package com.raysi.jcf.list.arraylist.codes;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: raysi
 *
 * Verifies ArrayList internals (JDK 17/21) by peeking at private state via reflection:
 *   - ArrayList.elementData  (the backing Object[])
 *   - AbstractList.modCount  (the structural-modification counter)
 *
 * Run (reflection into java.base needs --add-opens on JDK 16+):
 *   javac -d out com/raysi/jcf/list/arraylist/codes/ArrayListInternalsDemo.java
 *   java --add-opens java.base/java.util=ALL-UNNAMED \
 *        -cp out com.raysi.jcf.list.arraylist.codes.ArrayListInternalsDemo
 */
public class ArrayListInternalsDemo {

    private static final Field ELEMENT_DATA = reflect(ArrayList.class, "elementData");
    private static final Field MOD_COUNT = reflect(AbstractList.class, "modCount");

    public static void main(String[] args) throws Exception {
        if (ELEMENT_DATA == null || MOD_COUNT == null) {
            System.err.println("Re-run with: --add-opens java.base/java.util=ALL-UNNAMED");
            return;
        }
        growthTrace();
        nullingOnRemove();
        foreachCmeQuirk();
        removeOverloadTrap();
        modCountSemantics();
        subListRangeDelete();
        presizingEffect();
        concurrentCorruption();
    }

    // ---------------------------------------------------------------- 1
    private static void growthTrace() {
        header("1. Lazy allocation + 1.5x growth (grow() -> ArraysSupport.newLength)");

        ArrayList<Integer> list = new ArrayList<>();
        // HEAP: ArrayList object (~24B) -> elementData points at the SHARED static
        // DEFAULTCAPACITY_EMPTY_ELEMENTDATA (length 0). No 10-slot array exists yet.
        System.out.println("new ArrayList<>() -> capacity " + capacity(list));

        int last = capacity(list);
        for (int i = 0; i < 80; i++) {
            list.add(i);
            int cap = capacity(list);
            if (cap != last) {
                // HEAP: a NEW Object[cap] was allocated, old array copied via Arrays.copyOf
                // (native memcpy), old array becomes garbage. newCap = old + max(1, old >> 1).
                System.out.printf("  add #%-3d -> capacity %3d -> %3d%n", list.size(), last, cap);
                last = cap;
            }
        }

        // TRAP: an explicit 0 selects EMPTY_ELEMENTDATA (a different sentinel), so the
        // "jump to DEFAULT_CAPACITY=10" branch is skipped and growth starts at 1.
        ArrayList<Integer> zero = new ArrayList<>(0);
        StringBuilder sb = new StringBuilder();
        int prev = -1;
        for (int i = 0; i < 12; i++) {
            zero.add(i);
            int c = capacity(zero);
            if (c != prev) { sb.append(c).append(' '); prev = c; }
        }
        System.out.println("new ArrayList<>(0) capacities seen: " + sb.toString().trim());

        // ensureCapacity on a default list is a no-op while minCapacity <= 10.
        ArrayList<Object> lazy = new ArrayList<>();
        lazy.ensureCapacity(5);
        System.out.println("ensureCapacity(5)  on default list -> capacity " + capacity(lazy));
        lazy.ensureCapacity(11);
        System.out.println("ensureCapacity(11) on default list -> capacity " + capacity(lazy)
                + " (exact: max(10, 11), no 1.5x padding)");
    }

    // ---------------------------------------------------------------- 2
    private static void nullingOnRemove() {
        header("2. GC hygiene: removed slots are nulled, capacity is NOT released");

        ArrayList<String> names = new ArrayList<>(List.of("a", "b", "c"));
        names.remove("c");
        // HEAP: arraycopy shifted nothing (tail removal); fastRemove did es[size = newSize] = null.
        // Without that null, the array would keep "c" strongly reachable (a classic leak).
        System.out.println("after remove(\"c\"): " + Arrays.toString(raw(names)) + " size=" + names.size());

        names.clear(); // loop sets every slot in [0,size) to null, keeps the Object[]
        System.out.println("after clear():     " + Arrays.toString(raw(names))
                + " capacity retained=" + capacity(names));

        names.trimToSize(); // the ONLY way to give the array back (size 0 -> EMPTY_ELEMENTDATA)
        System.out.println("after trimToSize(): capacity=" + capacity(names));
    }

    // ---------------------------------------------------------------- 3
    private static void foreachCmeQuirk() {
        header("3. fail-fast is best-effort: Itr.hasNext() is `cursor != size`, not a modCount check");

        List<String> a = new ArrayList<>(List.of("A", "B", "C"));
        for (String s : a) {
            // Removing the SECOND-TO-LAST element: size drops to 2, cursor is already 2,
            // hasNext() returns false, so next() (the only place that checks modCount) never runs.
            if (s.equals("B")) a.remove(s);
        }
        System.out.println("remove second-to-last in for-each: no CME, result=" + a);

        List<String> b = new ArrayList<>(List.of("A", "B", "C"));
        try {
            for (String s : b) {
                if (s.equals("A")) b.remove(s); // cursor=1, size=2 -> hasNext true -> next() -> CME
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("remove first in for-each: CME (expectedModCount != modCount)");
        }

        List<String> c = new ArrayList<>(List.of("A", "B", "C"));
        Iterator<String> it = c.iterator();
        while (it.hasNext()) {
            if (it.next().equals("A")) it.remove(); // resyncs expectedModCount = modCount
        }
        c.removeIf(s -> s.equals("C")); // JDK 9+: bitset pass, ONE compaction, ONE modCount bump
        System.out.println("Iterator.remove / removeIf are safe: " + c);
    }

    // ---------------------------------------------------------------- 4
    private static void removeOverloadTrap() {
        header("4. remove(int index) vs remove(Object o) with List<Integer>");

        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 1));
        nums.remove(1);                    // overload resolution picks remove(int) -> index 1
        System.out.println("remove(1)                 -> " + nums + "   (removed 20 by INDEX)");
        nums.remove(Integer.valueOf(1));   // boxed -> remove(Object) -> equals() scan
        System.out.println("remove(Integer.valueOf(1)) -> " + nums + "       (removed value 1)");
    }

    // ---------------------------------------------------------------- 5
    private static void modCountSemantics() {
        header("5. What bumps modCount (and what does not)");

        ArrayList<Integer> list = new ArrayList<>(List.of(3, 1, 2));
        int m = modCount(list);
        System.out.println("start                 modCount=" + m);

        list.set(0, 99);                               // overwrite in place: NOT structural
        System.out.println("set(0, 99)            modCount=" + modCount(list) + " (unchanged)");

        list.add(7);                                   // structural
        System.out.println("add(7)                modCount=" + modCount(list));

        list.sort(null);                               // TimSort on elementData[0,size); sort() does modCount++
        System.out.println("sort(null)            modCount=" + modCount(list) + " (sort counts as a modification)");

        list.addAll(List.of());                        // empty input: modCount++ happens BEFORE the early return
        System.out.println("addAll(empty)         modCount=" + modCount(list));

        ArrayList<Integer> empty = new ArrayList<>();
        int before = modCount(empty);
        empty.clear();
        System.out.println("clear() on empty list modCount " + before + " -> " + modCount(empty));
    }

    // ---------------------------------------------------------------- 6
    private static void subListRangeDelete() {
        header("6. subList(a,b).clear() = one O(n) range delete; copy to detach the view");

        ArrayList<Integer> parent = new ArrayList<>();
        for (int i = 0; i < 10; i++) parent.add(i);

        // SubList.removeRange -> root.removeRange: ONE System.arraycopy for the tail and one
        // null-out loop, instead of 5 separate remove(i) calls (5 arraycopys, 5 modCount bumps).
        int before = modCount(parent);
        parent.subList(2, 7).clear();
        System.out.println("parent=" + parent + " modCount delta=" + (modCount(parent) - before));

        // A detached copy owns its own Object[]; the view pins the WHOLE parent array.
        List<Integer> view = parent.subList(0, 2);
        List<Integer> detached = new ArrayList<>(view);
        parent.add(100); // structural change on parent invalidates the view only
        try {
            view.size();
        } catch (ConcurrentModificationException e) {
            System.out.println("view   -> CME after parent.add(); detached=" + detached + " still fine");
        }
    }

    // ---------------------------------------------------------------- 7
    private static void presizingEffect() {
        header("7. Cost of not presizing: count reallocations while adding 100_000 elements");

        int n = 100_000;
        ArrayList<Integer> grown = new ArrayList<>();
        int resizes = 0, last = capacity(grown);
        for (int i = 0; i < n; i++) {
            grown.add(i);
            int c = capacity(grown);
            if (c != last) { resizes++; last = c; }
        }
        System.out.println("default ctor : " + resizes + " reallocations, final capacity " + capacity(grown)
                + " (" + (capacity(grown) - n) + " spare slots ~ " + (capacity(grown) - n) * 4L + " B at 4B/ref)");

        ArrayList<Integer> presized = new ArrayList<>(n);
        int before = capacity(presized);
        for (int i = 0; i < n; i++) presized.add(i);
        System.out.println("presized ctor: " + (capacity(presized) == before ? 0 : 1) + " reallocations, capacity " + capacity(presized));
    }

    // ---------------------------------------------------------------- 8
    private static void concurrentCorruption() throws Exception {
        header("8. Unsynchronized ArrayList under contention (non-deterministic: 40 trials each)");
        race("ArrayList        ", ArrayList::new, 40, 4, 50_000);
        race("synchronizedList ", () -> Collections.synchronizedList(new ArrayList<>()), 40, 4, 50_000);
    }

    private static void race(String label, Supplier<List<Integer>> factory,
                             int trials, int threads, int perThread) throws Exception {
        int lostUpdates = 0, exceptions = 0;
        String firstError = "none";
        for (int trial = 0; trial < trials; trial++) {
            List<Integer> target = factory.get();
            ExecutorService pool = Executors.newFixedThreadPool(threads);
            CountDownLatch start = new CountDownLatch(1);
            AtomicInteger failures = new AtomicInteger();
            AtomicReference<String> err = new AtomicReference<>("none");
            for (int t = 0; t < threads; t++) {
                pool.submit(() -> {
                    try {
                        start.await();
                        for (int i = 0; i < perThread; i++) {
                            // Race window: `elementData[s] = e; size = s + 1;` is NOT atomic, and
                            // grow() may publish a new array while another thread writes the old one.
                            target.add(i);
                        }
                    } catch (Throwable ex) {
                        failures.incrementAndGet();
                        err.compareAndSet("none", ex.getClass().getSimpleName());
                    }
                });
            }
            start.countDown();
            pool.shutdown();
            pool.awaitTermination(30, TimeUnit.SECONDS);
            if (failures.get() > 0) { exceptions++; if (firstError.equals("none")) firstError = err.get(); }
            else if (target.size() != threads * perThread) lostUpdates++;
        }
        System.out.printf("%s trials=%d lostUpdates=%d trialsWithException=%d firstError=%s%n",
                label, trials, lostUpdates, exceptions, firstError);
    }

    // ------------------------------------------------------------ helpers
    private static void header(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    private static Field reflect(Class<?> owner, String name) {
        try {
            Field f = owner.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
    }

    private static Object[] raw(ArrayList<?> list) {
        try {
            return (Object[]) ELEMENT_DATA.get(list);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int capacity(ArrayList<?> list) {
        return raw(list).length;
    }

    private static int modCount(ArrayList<?> list) {
        try {
            return (int) MOD_COUNT.get(list);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
```

### Sample output (OpenJDK 21.0.10; section 8 numbers vary run to run)

```text

=== 1. Lazy allocation + 1.5x growth (grow() -> ArraysSupport.newLength) ===
new ArrayList<>() -> capacity 0
  add #1   -> capacity   0 ->  10
  add #11  -> capacity  10 ->  15
  add #16  -> capacity  15 ->  22
  add #23  -> capacity  22 ->  33
  add #34  -> capacity  33 ->  49
  add #50  -> capacity  49 ->  73
  add #74  -> capacity  73 -> 109
new ArrayList<>(0) capacities seen: 1 2 3 4 6 9 13
ensureCapacity(5)  on default list -> capacity 0
ensureCapacity(11) on default list -> capacity 11 (exact: max(10, 11), no 1.5x padding)

=== 2. GC hygiene: removed slots are nulled, capacity is NOT released ===
after remove("c"): [a, b, null] size=2
after clear():     [null, null, null] capacity retained=3
after trimToSize(): capacity=0

=== 3. fail-fast is best-effort: Itr.hasNext() is `cursor != size`, not a modCount check ===
remove second-to-last in for-each: no CME, result=[A, C]
remove first in for-each: CME (expectedModCount != modCount)
Iterator.remove / removeIf are safe: [B]

=== 4. remove(int index) vs remove(Object o) with List<Integer> ===
remove(1)                 -> [10, 30, 1]   (removed 20 by INDEX)
remove(Integer.valueOf(1)) -> [10, 30]       (removed value 1)

=== 5. What bumps modCount (and what does not) ===
start                 modCount=0
set(0, 99)            modCount=0 (unchanged)
add(7)                modCount=1
sort(null)            modCount=2 (sort counts as a modification)
addAll(empty)         modCount=3
clear() on empty list modCount 0 -> 1

=== 6. subList(a,b).clear() = one O(n) range delete; copy to detach the view ===
parent=[0, 1, 7, 8, 9] modCount delta=1
view   -> CME after parent.add(); detached=[0, 1] still fine

=== 7. Cost of not presizing: count reallocations while adding 100_000 elements ===
default ctor : 24 reallocations, final capacity 106710 (6710 spare slots ~ 26840 B at 4B/ref)
presized ctor: 0 reallocations, capacity 100000

=== 8. Unsynchronized ArrayList under contention (non-deterministic: 40 trials each) ===
ArrayList         trials=40 lostUpdates=12 trialsWithException=0 firstError=none
synchronizedList  trials=40 lostUpdates=0 trialsWithException=0 firstError=none
```

> Companion example (your `SubListMemoryLeakDemo`): `subList` returns a **view** holding `root`, `parent`, `offset`, `size`. Writes through the view mutate the parent; a structural change on the parent bumps `root.modCount` and the next view access throws CME. A tiny `subList` of a huge list also pins the *entire* parent array - copy with `new ArrayList<>(view)` to detach.

---

# 7. Top 7 High-Signal Interview Questions & Deep Answers

**Q1. Why does removing an element inside a for-each sometimes throw `ConcurrentModificationException` and sometimes not?**
`hasNext()` is `cursor != size`; only `next()` calls `checkForComodification()`. If you remove the *second-to-last* element, `size` shrinks to equal `cursor`, `hasNext()` returns false, and the loop ends without ever re-checking `modCount`. Removing any other element leaves `cursor != size`, so `next()` sees `modCount != expectedModCount` and throws. (Note the test is `!=`, not `<`: removing the only element of a 1-element list leaves `cursor=1`, `size=0`, so `hasNext()` is true and `next()` throws.) Fail-fast is a best-effort bug detector, not a guarantee. Correct fixes: `Iterator.remove()`, `removeIf`, or iterate a copy.

**Q2. What exactly happens on the 1st and 11th `add` of `new ArrayList<>()`?**
The constructor stores the shared `DEFAULTCAPACITY_EMPTY_ELEMENTDATA` (length 0). The first `add`: `modCount++`, `s == 0 == elementData.length`, so `grow(1)` sees the default sentinel and allocates `new Object[max(10, 1)]`. The 11th `add` finds `s == 10 == length`, so `grow(11)` computes `newLength(10, 1, 5) = 10 + max(1, 5) = 15` and `Arrays.copyOf` allocates a new `Object[15]`, copies 10 refs, and the old array becomes garbage. Lazy allocation exists so the huge number of never-populated lists in real systems cost only the 24 B shell.

**Q3. Why 1.5x and not 2x? Why not a constant increment?**
A constant increment makes n appends cost O(n^2) total copying; any geometric factor gives O(1) amortized. 2x copies fewer times but can leave up to 50% unused; 1.5x caps waste near 33% and still averages <= 2 copies per element. (Folklore adds that factors below the golden ratio let freed blocks be reused by later allocations; that argument is weak on a compacting, generational JVM heap, so treat it as trivia, not a reason.) It is a tuned trade-off, not a law: the real remedy for hot paths is `new ArrayList<>(expectedSize)` or `ensureCapacity`, which turns ~24 reallocations for 100k elements into zero.

**Q4. When is `ArrayList` slower than `LinkedList`, and why is the textbook answer usually wrong?**
The textbook says "LinkedList inserts in the middle in O(1)". But positioning requires an O(n) walk across scattered nodes (cache misses on every hop), then 24 B of node allocation per insert. `ArrayList` does an O(n) *sequential* `memmove` that runs at memory bandwidth and allocates nothing. Break-even favors `LinkedList` essentially only for huge lists with many insertions/removals at an *already-held* `ListIterator` position, or as a `Deque` (and even there `ArrayDeque` wins). Measure with JMH, not with `System.nanoTime` loops.

**Q5. Why is `elementData` `transient`, and why is it package-private rather than `private`?**
`transient` because default serialization would write the entire array including unused capacity and nulls; custom `writeObject` writes `size` then exactly `size` elements, and `readObject` reallocates a tight array (and uses `SharedSecrets` array-length checking). It is non-private so nested classes (`SubList`, `Itr`) and `java.util` peers can read it directly; the JDK source comment says this is to simplify nested-class access. Side effect: `clone()`/deserialization produce right-sized arrays, which is also a cheap way to "trim".

**Q6. What actually goes wrong if two threads `add` to the same `ArrayList`, and why can't `volatile` fix it?**
`add` is check-grow-store-publish; none of it is atomic. Outcomes are lost updates (same `size` read twice), `ArrayIndexOutOfBoundsException` (two threads race into the last slot or across a `grow`), `null` holes (size published before/without the element store), or perfectly silent corruption. Making `elementData`/`size` `volatile` fixes visibility only; `size = s + 1` and the slot write remain separate non-atomic steps, so you need mutual exclusion (`synchronizedList`, an explicit lock) or a different design (`CopyOnWriteArrayList` for read-mostly, immutable snapshots, or confine the list to one thread and hand off results).

**Q7. `list.remove(1)` on a `List<Integer>` - what is removed, and how do you stay safe from this and from `subList` surprises?**
Overload resolution prefers `remove(int index)` (exact primitive match, no boxing), so it removes the element at index 1; `remove(Integer.valueOf(1))` or `remove((Object) 1)` removes by value via `equals`. For ranges, `list.subList(a, b).clear()` is the idiomatic O(n) single-shift deletion, but the returned view shares the parent's array and `modCount`: structurally modifying the parent afterwards makes every view operation throw CME, and keeping a small view alive pins the whole parent array in memory. Detach with `new ArrayList<>(view)` (or `List.copyOf(view)`) when the view must outlive the parent's next mutation.