# Heap Data Structure: Detailed Notes

A **heap** is a specialized, **complete binary tree**-based data structure that satisfies the **heap property**: every parent node is ordered relative to its children in a consistent way (always ≥ in a max-heap, or always ≤ in a min-heap). Heaps are the engine behind **priority queues**, and show up constantly in scheduling, graph algorithms (Dijkstra, Prim's), and the "top-K" / "kth element" family of interview problems.

```text
MIN-HEAP example (array form):      MAX-HEAP example (array form):
        1                                    9
       / \                                  / \
      3   2                                7   8
     / \ / \                              / \ / \
    5  4 8  7                            3  4 6  5

Array: [1,3,2,5,4,8,7]               Array: [9,7,8,3,4,6,5]
```

---

## Table of Contents

1. [Introduction: What Problem Does a Heap Solve?](#1-introduction-what-problem-does-a-heap-solve)
2. [The Heap Property](#2-the-heap-property)
3. [Complete Binary Tree: The Shape Constraint](#3-complete-binary-tree-the-shape-constraint)
4. [Why Array Representation (The "Brain" Behind It)](#4-why-array-representation-the-brain-behind-it)
5. [Types of Heaps](#5-types-of-heaps)
6. [Core Operations — The Theory](#6-core-operations--the-theory)
7. [Sift-Up (Bubble-Up / Heapify-Up)](#7-sift-up-bubble-up--heapify-up)
8. [Sift-Down (Bubble-Down / Heapify-Down)](#8-sift-down-bubble-down--heapify-down)
9. [Insert Operation](#9-insert-operation)
10. [Extract-Min/Max Operation](#10-extract-minmax-operation)
11. [Peek Operation](#11-peek-operation)
12. [Build-Heap: Why It's O(n), Not O(n log n)](#12-build-heap-why-its-on-not-on-log-n)
13. [Complexity Summary Table](#13-complexity-summary-table)
14. [Implementing a Heap From Scratch in Java](#14-implementing-a-heap-from-scratch-in-java)
15. [Java's Built-in `PriorityQueue`](#15-javas-built-in-priorityqueue)
16. [`PriorityQueue` with Custom Comparators](#16-priorityqueue-with-custom-comparators)
17. [`PriorityQueue` of Objects, Pairs, and Arrays](#17-priorityqueue-of-objects-pairs-and-arrays)
18. [Types of Problems Heaps Solve](#18-types-of-problems-heaps-solve)
19. [Recognizing When to Reach for a Heap](#19-recognizing-when-to-reach-for-a-heap)
20. [Common Heap Patterns](#20-common-heap-patterns)
21. [Top 20 Heap Problems (By Pattern)](#21-top-20-heap-problems-by-pattern)
22. [Sample Solutions (Worked)](#22-sample-solutions-worked)
23. [Common Pitfalls](#23-common-pitfalls)
24. [Quick Cheat Sheet](#24-quick-cheat-sheet)

---

## 1. Introduction: What Problem Does a Heap Solve?

A heap exists to answer one question **extremely efficiently, over and over**:

> "What is the smallest (or largest) element in this collection, right now?"

A naive approach — scanning the whole collection every time you need the min/max — costs **O(n)** per query. Sorting the whole collection up front costs **O(n log n)**, but then inserting or removing elements becomes expensive again, since you have to maintain sorted order.

A heap strikes a deliberate, powerful balance:

| Operation | Unsorted array | Sorted array | Heap |
|-----------|----------------|----------------|------|
| Find min/max | O(n) | O(1) | **O(1)** |
| Insert | O(1) | O(n) | **O(log n)** |
| Remove min/max | O(n) | O(n) (shift) | **O(log n)** |

This is exactly the combination needed for a **priority queue**: a collection where elements are processed in order of priority (not insertion order), and both insertion and "give me the next-highest-priority element" need to be fast.

---

## 2. The Heap Property

A **max-heap** requires, for every node:

```text
parent's value >= child's value   (for every parent-child pair, at every level)
```

A **min-heap** requires the opposite:

```text
parent's value <= child's value   (for every parent-child pair, at every level)
```

### 2.1 Crucial misconception to avoid: a heap is NOT fully sorted

The heap property only constrains **parent vs child** — it says **nothing** about the relative order of sibling nodes or of nodes in different subtrees.

```text
Valid min-heap:
        1
       / \
      5   2        <- 5 and 2 are siblings; 5 > 2, and that's perfectly fine!
     / \
    8   9
```

This is the entire reason heaps are **cheaper** to maintain than a fully sorted structure: they only enforce the **minimum necessary ordering** to answer "what's on top?" in O(1), while allowing everything else underneath to stay in whatever order is convenient.

---

## 3. Complete Binary Tree: The Shape Constraint

A heap is always a **complete binary tree**: every level is fully filled **except possibly the last**, and the last level is filled **strictly left to right**, with no gaps.

```text
VALID (complete):              INVALID (not complete):
        A                              A
       / \                            / \
      B   C                         B    C
     / \  /                        / \     \
    D  E F                        D   E     F    <- gap: no left child before this right child
```

This shape constraint is what makes the **array representation** (next section) possible and efficient — a complete binary tree can be stored without any wasted space and without needing explicit pointers at all.

---

## 4. Why Array Representation (The "Brain" Behind It)

This is the single most important implementation insight behind heaps: **because the tree is always complete, it can be stored in a plain array, with parent-child relationships computed by simple index arithmetic** — no `Node` objects, no `left`/`right` pointers needed at all.

For a node at index `i` (0-based array indexing):

```text
Left child index   =  2*i + 1
Right child index  =  2*i + 2
Parent index        =  (i - 1) / 2      (integer division)
```

```text
Index:   0   1   2   3   4   5   6
Value:   1   3   2   5   4   8   7

         1  (index 0)
        / \
   (1) 3   2 (2)
      / \  / \
 (3) 5  4 8  7 (6)
    (4)   (5)
```

```java
int i = 3;              // node holding value 5
int parent = (i - 1) / 2;   // (3-1)/2 = 1  -> parent is index 1 (value 3)      correct!
int left = 2 * i + 1;        // 2*3+1 = 7  -> out of bounds, 5 has no left child  correct!
int right = 2 * i + 2;       // 2*3+2 = 8  -> out of bounds, 5 has no right child correct!
```

### 4.1 Why this is "the brain" of the whole structure

- **No pointer overhead.** A linked tree needs extra memory per node for `left`/`right`/`parent` references; an array-backed heap needs none of that.
- **Cache-friendly.** Array elements sit contiguously in memory, which is far more cache-efficient than chasing pointers across the heap (the memory heap, not the data structure!) during traversal.
- **O(1) navigation.** Moving between a node and its parent/children is simple arithmetic, not a dereference chain.
- **Trivial resizing.** Since the shape is always "complete," adding a new node is always just appending to the end of the array (or the dynamic array/`ArrayList` backing it) — there's never a question of *where* in the tree shape the new node goes.

### 4.2 1-based indexing variant (seen in some textbooks/implementations)

Some implementations (and classic CLRS-style pseudocode) use **1-based** array indices instead, which gives slightly cleaner arithmetic:

```text
Left child   =  2*i
Right child  =  2*i + 1
Parent        =  i / 2
```

Both conventions are equally valid — Java's own `PriorityQueue` internally uses 0-based indexing, matching the formulas in section 4 above.

---

## 5. Types of Heaps

### 5.1 By ordering: Min-Heap vs Max-Heap

| | Min-Heap | Max-Heap |
|---|-----------|------------|
| Root holds | The **smallest** element | The **largest** element |
| Parent vs child | Parent ≤ children | Parent ≥ children |
| Typical use | "Give me the smallest item next" (Dijkstra, merging sorted lists, k-smallest problems) | "Give me the largest item next" (scheduling by highest priority, k-largest problems) |

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();                                // min-heap by default
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());       // max-heap
```

### 5.2 By underlying structure/implementation

| Type | Key idea | Notes |
|------|----------|-------|
| **Binary heap** | Each node has at most 2 children; stored as a flat array | The standard, most commonly used heap — what Java's `PriorityQueue` uses internally |
| **D-ary heap** | Each node has up to `d` children instead of 2 | Shallower tree (fewer levels) → faster decrease-key/insert in some cases, at the cost of slightly more comparisons per sift-down |
| **Binomial heap** | A collection ("forest") of binomial trees of different orders | Supports an efficient **merge** of two heaps in O(log n), unlike a plain binary heap's O(n) merge |
| **Fibonacci heap** | A more relaxed, lazily-consolidated structure | Achieves amortized O(1) insert and decrease-key, making it the theoretical choice for algorithms like Dijkstra's at very large scale — rarely implemented by hand in interviews due to complexity |
| **Leftist heap / Skew heap** | Self-adjusting binary trees optimized for fast merging | Mostly of academic/specialized interest |

> For almost all interview and real-world application purposes, **the binary heap (via `PriorityQueue`) is what you need.** The other variants exist to shave complexity in specific, less common scenarios (frequent merging, very large graphs with many decrease-key operations) — know they exist, but don't over-invest in implementing them by hand unless specifically asked.

---

## 6. Core Operations — The Theory

Every heap operation is built from exactly **two** fundamental repair procedures:

```text
sift-up (bubble-up)     — fix a violation where a node might be "too small" relative to its PARENT (in a max-heap)
                           or "too large" relative to its parent (in a min-heap); moves a node UP the tree
sift-down (bubble-down) — fix a violation where a node might be "too large" relative to its CHILDREN (in a max-heap)
                           or "too small" relative to its children (in a min-heap); moves a node DOWN the tree
```

Every other operation (insert, extract, build-heap, even deletion of an arbitrary element) is just a specific combination of these two procedures.

---

## 7. Sift-Up (Bubble-Up / Heapify-Up)

Used after **inserting** a new element at the end of the array — the new element may violate the heap property with respect to its **ancestors**, so it's repeatedly swapped upward until the property is restored.

```java
void siftUp(int[] heap, int i) {              // max-heap version
    while (i > 0) {
        int parent = (i - 1) / 2;
        if (heap[i] <= heap[parent]) break;    // property satisfied, stop
        swap(heap, i, parent);
        i = parent;                             // continue checking from the new position
    }
}
```

```text
Insert 10 into:            Step 1: 10 > parent(7) -> swap       Step 2: 10 > parent(9) -> swap
      9                          9                                   10
     / \                       /   \                                /  \
    7   8          ->         10    8              ->               9    8
   / \                       / \                                   / \
  3  4                      3   4                                 3   4
 (10 appended               (10 at index 3,                       (10 reaches the root,
  at the end)                 compared to parent 7)                 no further parent to check)
```

**Time complexity:** O(log n) — in the worst case, the new element travels from a leaf all the way up to the root, a height of `log n` for `n` elements in a complete binary tree.

---

## 8. Sift-Down (Bubble-Down / Heapify-Down)

Used after **removing the root** (replacing it with the last element) or during **build-heap** — the node at the top of a subtree may violate the heap property with respect to its **descendants**, so it's repeatedly swapped downward, always toward the **larger** child (in a max-heap) or **smaller** child (in a min-heap), until the property is restored.

```java
void siftDown(int[] heap, int i, int size) {    // max-heap version
    while (true) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int largest = i;

        if (left < size && heap[left] > heap[largest]) largest = left;
        if (right < size && heap[right] > heap[largest]) largest = right;

        if (largest == i) break;                // both children are smaller or absent — property restored
        swap(heap, i, largest);
        i = largest;                              // continue checking from the new position
    }
}
```

```text
Remove root 9:          Move last element (3) to root:     Sift down: 3 < max(child 8,7) -> swap with 8
      9                        3                                   8
     / \                      / \                                 / \
    7   8          ->        7   8              ->                7   3
   / \                                                            /
  3  4                                                           4
```

**Time complexity:** O(log n) — same reasoning as sift-up, bounded by the tree's height.

### 8.1 Why sift-down always picks the LARGER (or smaller) child, never either arbitrarily

If you swapped with the wrong child, you could end up fixing the violation with the node you just moved down, but simultaneously introducing a **new** violation against the child you didn't compare against. Always moving toward the extreme (larger for max-heap, smaller for min-heap) guarantees the subtree being sifted into still satisfies the heap property once the swap completes.

---

## 9. Insert Operation

```text
1. Append the new element at the END of the array (the next available "complete tree" position).
2. Sift it UP until the heap property is restored.
```

```java
void insert(int[] heap, int size, int value) {
    heap[size] = value;       // append
    siftUp(heap, size);        // restore property
}
```

**Time complexity:** O(log n) — dominated by the sift-up step.

---

## 10. Extract-Min/Max Operation

```text
1. Save the ROOT value (this is the min or max — the whole point of the structure).
2. Move the LAST element in the array to the root position.
3. Shrink the heap's logical size by one (the old last slot is now unused).
4. Sift DOWN the new root until the heap property is restored.
5. Return the saved value.
```

```java
int extractMax(int[] heap, int size) {    // "size" is passed by reference conceptually (e.g., via a wrapper/field)
    int max = heap[0];
    heap[0] = heap[size - 1];
    size--;
    siftDown(heap, 0, size);
    return max;
}
```

**Time complexity:** O(log n) — dominated by the sift-down step.

### 10.1 Why move the LAST element to the root, rather than, say, the left child?

Moving the last element preserves the **complete binary tree shape** automatically — removing the very last position never creates a "gap" anywhere else in the tree. Promoting a child directly instead would require recursively patching the resulting hole, which is exactly what sift-down already does more simply by starting fresh from the root.

---

## 11. Peek Operation

```java
int peek(int[] heap) {
    return heap[0];     // the root is ALWAYS the min (or max) — no search needed
}
```

**Time complexity:** O(1) — this is the entire payoff of maintaining the heap property at all.

---

## 12. Build-Heap: Why It's O(n), Not O(n log n)

Given an arbitrary, unordered array, you can convert it into a valid heap **in place**, without needing n separate inserts.

```java
void buildHeap(int[] heap, int size) {
    for (int i = size / 2 - 1; i >= 0; i--) {    // start from the LAST non-leaf node, work backward to the root
        siftDown(heap, i, size);
    }
}
```

### 12.1 Why start from `size/2 - 1`?

In a complete binary tree, all indices from `size/2` onward are **leaves** (they have no children) — a leaf trivially already satisfies the heap property on its own, so there's nothing to sift. Only the **internal (non-leaf) nodes** ever need fixing, and the last one is at index `size/2 - 1`.

### 12.2 The surprising complexity result: O(n), not O(n log n)

Although each individual `siftDown` call costs up to O(log n), it would seem like calling it O(n) times gives O(n log n) overall — but this overestimates the cost. **Most nodes are near the bottom of the tree**, where a sift-down has very little distance to travel.

```text
Level (from bottom, 0-indexed)   Number of nodes (roughly)    Max sift-down distance
0 (leaves)                       n/2                            0
1                                  n/4                            1
2                                  n/8                            2
...                                ...                              ...
log(n) (root)                     1                               log(n)
```

Summing `(number of nodes at level) × (sift-down distance at that level)` across all levels forms a series that converges to **O(n)** overall, not O(n log n) — a classic, somewhat famous result worth knowing, since it's a common interview/theory question in its own right ("why is heapify linear, not linearithmic?").

### 12.3 Practical consequence

This is exactly why `new PriorityQueue<>(collection)` (constructing a heap from an existing collection all at once) is more efficient than inserting every element one at a time via repeated `add()` calls — the former uses this O(n) build-heap approach internally, while the latter costs O(n log n) overall (n inserts, each O(log n)).

---

## 13. Complexity Summary Table

| Operation | Time Complexity | Notes |
|-----------|-------------------|--------|
| Peek (get min/max) | **O(1)** | Always the root |
| Insert | **O(log n)** | Append + sift-up |
| Extract-min/max | **O(log n)** | Move last to root + sift-down |
| Build-heap (from an existing array) | **O(n)** | Bottom-up sift-down from the last internal node |
| Search for an arbitrary value | **O(n)** | No ordering guarantee beyond parent/child — must scan |
| Delete an arbitrary element (given its index) | **O(log n)** | Replace with last element, then sift in whichever direction restores the property |
| Space | **O(n)** | Just the backing array — no extra per-node overhead |

---

## 14. Implementing a Heap From Scratch in Java

A complete, minimal max-heap implementation, tying together every operation above.

```java
import java.util.Arrays;

class MaxHeap {
    private int[] heap;
    private int size;

    MaxHeap(int capacity) {
        heap = new int[capacity];
        size = 0;
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i)   { return 2 * i + 1; }
    private int right(int i)  { return 2 * i + 2; }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    void insert(int value) {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);   // grow, like an ArrayList would
        }
        heap[size] = value;
        siftUp(size);
        size++;
    }

    private void siftUp(int i) {
        while (i > 0 && heap[i] > heap[parent(i)]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    int extractMax() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        siftDown(0);
        return max;
    }

    private void siftDown(int i) {
        while (true) {
            int largest = i;
            int l = left(i), r = right(i);

            if (l < size && heap[l] > heap[largest]) largest = l;
            if (r < size && heap[r] > heap[largest]) largest = r;

            if (largest == i) break;
            swap(i, largest);
            i = largest;
        }
    }

    int peek() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    boolean isEmpty() { return size == 0; }
    int size() { return size; }
}
```

```java
MaxHeap heap = new MaxHeap(10);
heap.insert(5);
heap.insert(9);
heap.insert(3);
heap.insert(7);

while (!heap.isEmpty()) {
    System.out.print(heap.extractMax() + " ");   // 9 7 5 3  — descending order
}
```

> This from-scratch version is worth building **once** to internalize the mechanics — in actual interviews and real code, you should almost always use Java's built-in `PriorityQueue` instead (section 15), unless the problem is specifically asking you to implement a heap yourself.

---

## 15. Java's Built-in `PriorityQueue`

`java.util.PriorityQueue<E>` is Java's ready-made **binary heap** implementation, found in `java.util`.

```java
import java.util.PriorityQueue;

PriorityQueue<Integer> minHeap = new PriorityQueue<>();   // MIN-heap by default — natural ordering
```

### 15.1 Core methods

| Method | Purpose | Complexity |
|--------|---------|------------|
| `add(e)` / `offer(e)` | Insert an element | O(log n) |
| `peek()` | View the smallest (or highest-priority) element, without removing it; returns `null` if empty | O(1) |
| `poll()` | Remove and return the smallest/highest-priority element; returns `null` if empty | O(log n) |
| `remove(e)` | Remove a **specific** element (not necessarily the root) | O(n) — must search first |
| `size()` | Number of elements currently in the heap | O(1) |
| `isEmpty()` | Whether the heap has zero elements | O(1) |

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.add(5);
pq.add(1);
pq.add(9);
pq.add(3);

System.out.println(pq.peek());    // 1 — smallest, without removing

while (!pq.isEmpty()) {
    System.out.print(pq.poll() + " ");   // 1 3 5 9 — ascending order
}
```

### 15.2 `PriorityQueue` is NOT fully sorted internally — don't iterate it expecting order

```java
PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 9, 3));
System.out.println(pq);        // something like [1, 3, 9, 5] — valid heap order, NOT sorted order!
```

Iterating a `PriorityQueue` directly (via `for-each`, `toString()`, or its iterator) gives elements in **internal array/heap order**, not priority order — section 2.1's "not fully sorted" point applies directly here. **Only repeated `poll()` calls** guarantee elements come out in priority order.

### 15.3 Building from an existing collection — the O(n) build-heap path

```java
List<Integer> nums = List.of(9, 3, 7, 1, 5);
PriorityQueue<Integer> pq = new PriorityQueue<>(nums);   // uses the O(n) build-heap approach internally
```

---

## 16. `PriorityQueue` with Custom Comparators

### 16.1 Max-heap via `Collections.reverseOrder()`

```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
maxHeap.add(5);
maxHeap.add(1);
maxHeap.add(9);
System.out.println(maxHeap.poll());   // 9 — largest first
```

### 16.2 Custom comparator via lambda

```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);   // reversed natural order
```

> Avoid `(a, b) -> a - b` style comparators for large integers — subtraction can silently **overflow**. Prefer `Integer.compare(a, b)` (see the operators notes on this exact overflow trap).

```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
```

### 16.3 Comparator for custom objects

```java
class Task {
    String name;
    int priority;
    Task(String name, int priority) { this.name = name; this.priority = priority; }
}

PriorityQueue<Task> taskQueue = new PriorityQueue<>((a, b) -> a.priority - b.priority);  // lower priority number = served first
taskQueue.add(new Task("Low", 3));
taskQueue.add(new Task("High", 1));
taskQueue.add(new Task("Medium", 2));

System.out.println(taskQueue.poll().name);   // "High"
```

### 16.4 Implementing `Comparable` instead of passing a comparator

```java
class Task implements Comparable<Task> {
    int priority;
    Task(int priority) { this.priority = priority; }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
}

PriorityQueue<Task> pq = new PriorityQueue<>();    // uses Task's natural ordering (compareTo) automatically
```

---

## 17. `PriorityQueue` of Objects, Pairs, and Arrays

A very common interview pattern: use a `PriorityQueue<int[]>` to bundle multiple pieces of information (a value plus its index, or a value plus a count) as a single "pair" without defining a custom class.

```java
// Each element: {value, originalIndex}
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);   // min-heap ordered by the value at index 0

pq.add(new int[]{5, 0});
pq.add(new int[]{1, 1});
pq.add(new int[]{9, 2});

int[] top = pq.poll();
System.out.println(top[0] + " at index " + top[1]);   // "1 at index 1"
```

```java
// Common for "merge k sorted lists": store {value, listIndex, elementIndex}
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
```

---

## 18. Types of Problems Heaps Solve

Heaps excel whenever a problem needs **repeated, efficient access to a current min or max**, especially when the underlying data set is changing (elements being added/removed) or is too large to fully sort up front.

```text
- Top-K / K-th largest/smallest element problems
- Merging multiple sorted sequences efficiently
- Scheduling and task/event ordering (CPU scheduling, meeting rooms)
- Running median / median-of-stream problems
- Graph algorithms: Dijkstra's shortest path, Prim's minimum spanning tree
- Huffman encoding (greedy, frequency-based merging)
- Sliding-window maximum/minimum variants (heap-based alternative to a deque)
- "Closest K points/elements" problems
- Rearranging/reorganizing data by frequency (e.g., no two adjacent same characters)
```

---

## 19. Recognizing When to Reach for a Heap

```text
Trigger phrases in a problem statement:

"kth largest" / "kth smallest"                    -> heap of size k, often the OPPOSITE-ordered heap (see pattern below)
"top k" / "k most frequent" / "k closest"          -> heap of size k
"merge k sorted ..."                               -> min-heap holding one "head" element per list
"median of a running stream"                       -> TWO heaps (a max-heap + a min-heap)
"schedule"/"meeting rooms"/"minimum number of ..."  -> heap tracking end times / resource availability
"rearrange so no two adjacent are the same"         -> max-heap by frequency
"shortest path" / "minimum cost to connect"         -> priority queue-driven greedy graph traversal (Dijkstra/Prim)
```

If a problem needs the **current smallest or largest** element repeatedly, while the data set itself keeps changing, that's the single strongest signal to reach for a heap.

---

## 20. Common Heap Patterns

### 20.1 Pattern: "Top K" / "Kth Largest" — maintain a heap of size K

The counterintuitive trick: for the **Kth LARGEST**, use a **MIN**-heap of size K (not a max-heap!). Keep only the K largest elements seen so far; the smallest of those K (sitting at the root of the min-heap) is exactly the Kth largest overall.

```text
Why a MIN-heap for "Kth largest"?
- The heap holds the K largest candidates seen SO FAR.
- Its root (minimum of these K) is the WEAKEST of the current top-K — the one to discard
  if an even bigger element comes along.
- This lets you compare each new element against just the root in O(1), and only pay
  O(log K) when a replacement actually happens.
```

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();   // size capped at K

for (int num : nums) {
    minHeap.add(num);
    if (minHeap.size() > k) {
        minHeap.poll();     // discard the current smallest, keeping only the K largest so far
    }
}
return minHeap.peek();       // the Kth largest overall
```

(Symmetrically: for the **Kth SMALLEST**, use a **MAX**-heap of size K.)

### 20.2 Pattern: Merge K Sorted Lists/Arrays

Keep one "current head" element per list in a min-heap; repeatedly pop the smallest, then push that list's next element.

```java
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
// seed with the first element of each list: {value, listIndex, elementIndex}
```

### 20.3 Pattern: Two Heaps for Running Median

Maintain a **max-heap** for the lower half of the numbers seen so far, and a **min-heap** for the upper half, keeping their sizes balanced (within 1 of each other). The median is then always derivable from the two roots in O(1).

```text
maxHeap (lower half)     minHeap (upper half)
      5                         6
     / \                        / \
    3   4                      7    8

If sizes are equal: median = average of both roots (5 and 6) = 5.5
If maxHeap has one more: median = maxHeap's root alone
```

### 20.4 Pattern: Greedy Frequency-Based Rearrangement

Count frequencies, put them in a max-heap, and repeatedly take the most frequent remaining character/item, placing a "cooldown" buffer (often another small heap or queue) before it can be reused.

### 20.5 Pattern: Priority-Queue-Driven Graph Traversal (Dijkstra / Prim)

Instead of a plain queue (BFS) or stack (DFS), use a min-heap keyed by current known distance/cost, always expanding the **cheapest** known frontier node next — this is exactly what makes Dijkstra's and Prim's algorithms "greedy, heap-driven" variants of graph traversal.

### 20.6 Pattern: Interval/Scheduling via End-Time Heap

For problems like "minimum number of meeting rooms needed," sort intervals by start time, then use a min-heap of **end times** to track which rooms are currently occupied and free up the earliest-ending one when possible.

---

## 21. Top 20 Heap Problems (By Pattern)

A curated list spanning the patterns above, roughly ordered from foundational to more advanced. (★ = worked in section 22; the rest are for you to solve.)

### Top-K / Kth Element family

1. ★ Kth Largest Element in an Array
2. Kth Smallest Element in a Sorted Matrix
3. Top K Frequent Elements
4. K Closest Points to Origin
5. Find K Pairs with Smallest Sums

### Merging family

6. ★ Merge K Sorted Lists
7. Smallest Range Covering Elements from K Lists

### Running Median / Two-Heap family

8. ★ Find Median from Data Stream
9. Sliding Window Median

### Greedy / Frequency-Rearrangement family

10. Reorganize String (no two adjacent characters the same)
11. Task Scheduler (with cooldown period)
12. Rearrange String K Distance Apart

### Scheduling / Interval family

13. Meeting Rooms II (minimum rooms required)
14. Minimum Number of Platforms (classic train-platform scheduling variant)
15. Car Pooling (capacity-constrained interval scheduling)

### Graph / Dijkstra-style family

16. Network Delay Time (Dijkstra's algorithm, directly)
17. Cheapest Flights Within K Stops
18. Path with Minimum Effort (grid-based Dijkstra variant)
19. Swim in Rising Water

### Design / Miscellaneous

20. Design Twitter (merge K users' tweet feeds, each a "sorted-by-time" stream)

---

## 22. Sample Solutions (Worked)

Three fully worked solutions, one from each of the three biggest patterns, so the mechanics are concrete before you tackle the remaining 17 yourself.

### 22.1 Kth Largest Element in an Array

**Problem:** Given an integer array `nums` and an integer `k`, return the `k`th largest element.

```java
import java.util.PriorityQueue;

class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();   // min-heap, capped at size k

        for (int num : nums) {
            minHeap.add(num);
            if (minHeap.size() > k) {
                minHeap.poll();      // discard the weakest of the current top-k
            }
        }

        return minHeap.peek();       // root = the smallest of the k largest = the kth largest overall
    }
}
```

```text
nums = [3,2,1,5,6,4], k = 2

Processing: 3 -> heap:[3]
            2 -> heap:[2,3]
            1 -> heap:[1,2,3] -> size>2 -> poll 1 -> heap:[2,3]
            5 -> heap:[2,3,5] -> size>2 -> poll 2 -> heap:[3,5]
            6 -> heap:[3,5,6] -> size>2 -> poll 3 -> heap:[5,6]
            4 -> heap:[4,5,6] -> size>2 -> poll 4 -> heap:[5,6]

peek() = 5   <- the 2nd largest element, correct!
```

**Complexity:** O(n log k) time (n insertions/removals, each O(log k) since the heap never exceeds size k), O(k) space.

---

### 22.2 Merge K Sorted Lists

**Problem:** You are given an array of `k` linked lists, each sorted in ascending order. Merge them into one sorted list.

```java
import java.util.PriorityQueue;

class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        // Min-heap ordered by each node's value
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);

        // Seed the heap with the head of every non-empty list
        for (ListNode head : lists) {
            if (head != null) pq.add(head);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (!pq.isEmpty()) {
            ListNode smallest = pq.poll();           // always the globally smallest "current head" across all lists
            tail.next = smallest;
            tail = tail.next;

            if (smallest.next != null) {
                pq.add(smallest.next);                // push that list's NEXT element in to replace it
            }
        }

        return dummy.next;
    }
}
```

```text
Lists: [1->4->5], [1->3->4], [2->6]

Seed heap with heads: {1(list1), 1(list2), 2(list3)}
Poll 1(list1) -> append to result -> push 4(list1) -> heap: {1(list2), 2(list3), 4(list1)}
Poll 1(list2) -> append -> push 3(list2) -> heap: {2(list3), 3(list2), 4(list1)}
Poll 2(list3) -> append -> push 6(list3) -> heap: {3(list2), 4(list1), 6(list3)}
Poll 3(list2) -> append -> push 4(list2) -> heap: {4(list1), 4(list2), 6(list3)}
... continues until heap is empty

Result: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
```

**Complexity:** O(N log k) time, where N is the total number of nodes across all lists and k is the number of lists (each poll/add is O(log k), since the heap never holds more than k elements at once). O(k) space for the heap.

---

### 22.3 Find Median from Data Stream

**Problem:** Design a data structure that supports adding integers one at a time and efficiently finding the median of all elements added so far.

```java
import java.util.PriorityQueue;
import java.util.Collections;

class MedianFinder {
    private PriorityQueue<Integer> maxHeap;   // holds the SMALLER half of numbers
    private PriorityQueue<Integer> minHeap;   // holds the LARGER half of numbers

    public MedianFinder() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        maxHeap.add(num);                 // always insert into maxHeap first...
        minHeap.add(maxHeap.poll());       // ...then move its largest element over to minHeap, keeping order correct

        if (minHeap.size() > maxHeap.size()) {
            maxHeap.add(minHeap.poll());    // rebalance: maxHeap should never be smaller than minHeap
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();          // odd total count: maxHeap holds the extra middle element
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;   // even total count: average the two middle elements
    }
}
```

```text
addNum(5):  maxHeap:[5]           minHeap:[]            (balance check: minHeap not bigger, no rebalance needed)
addNum(2):  maxHeap:[5]->poll 5    minHeap:[5]           maxHeap:[2] after insert+poll... 

Let's trace more carefully:
addNum(5): maxHeap.add(5) -> maxHeap:[5]
           minHeap.add(maxHeap.poll()=5) -> maxHeap:[], minHeap:[5]
           minHeap.size()(1) > maxHeap.size()(0) -> maxHeap.add(minHeap.poll()=5) -> maxHeap:[5], minHeap:[]
           median = 5.0

addNum(2): maxHeap.add(2) -> maxHeap:[5,2] (heap order: 5 on top since max-heap)
           minHeap.add(maxHeap.poll()=5) -> maxHeap:[2], minHeap:[5]
           sizes equal, no rebalance
           median = (2 + 5) / 2.0 = 3.5

addNum(3): maxHeap.add(3) -> maxHeap:[3,2]
           minHeap.add(maxHeap.poll()=3) -> maxHeap:[2], minHeap:[3,5]
           minHeap.size()(2) > maxHeap.size()(1) -> maxHeap.add(minHeap.poll()=3) -> maxHeap:[3,2], minHeap:[5]
           median = 3.0  (maxHeap bigger by one -> its root is the median)
```

**Complexity:** O(log n) per `addNum` call (heap insert/poll), O(1) for `findMedian`. O(n) space.

---

## 23. Common Pitfalls

| # | Pitfall | Fix |
|---|---------|-----|
| 1 | Assuming a heap is fully sorted | Only the root is guaranteed to be the min/max; siblings and other subtrees can be in any order |
| 2 | Iterating a `PriorityQueue` directly expecting sorted output | Only repeated `poll()` calls guarantee priority order; `toString()`/for-each give internal array order |
| 3 | Using `(a, b) -> a - b` as a comparator for large integers | Risk of overflow — use `Integer.compare(a, b)` instead |
| 4 | Using a max-heap for "Kth largest" problems | Counterintuitively, cap a MIN-heap at size K instead — its root is the Kth largest |
| 5 | Forgetting to push a list's next element after polling, in merge-K-lists style problems | Every poll from a "multi-source" heap must be paired with pushing that source's next item, or sources run dry early |
| 6 | Expecting `remove(Object)` on `PriorityQueue` to be fast | It's O(n), since the element must first be located — `PriorityQueue` only guarantees fast access to the root |
| 7 | Forgetting the empty-heap null checks | `peek()`/`poll()` return `null` on an empty queue rather than throwing — check `isEmpty()` or handle `null` explicitly |
| 8 | Using a single heap for running-median problems | A single heap can't give O(1) median access — the two-heap (max-heap + min-heap) pattern is required |
| 9 | Forgetting to re-balance the two heaps in the running-median pattern | Without rebalancing after every insert, the size invariant breaks and the median calculation becomes wrong |
| 10 | Assuming build-heap from a collection costs the same as repeated single inserts | `new PriorityQueue<>(collection)` is O(n); n individual `add()` calls would cost O(n log n) overall |

---

## 24. Quick Cheat Sheet

```text
HEAP PROPERTY         Max-heap: parent >= children (every pair)    Min-heap: parent <= children
                      NOT fully sorted — only parent-vs-child order is guaranteed

SHAPE                 Always a COMPLETE binary tree — enables flat ARRAY storage, no pointers needed

ARRAY INDEXING (0-based)   left(i)=2i+1    right(i)=2i+2    parent(i)=(i-1)/2

CORE REPAIR OPS        sift-up (bubble-up)     — after INSERT; new element may violate vs ancestors, moves UP
                       sift-down (bubble-down) — after EXTRACT/build; node may violate vs descendants, moves DOWN

OPERATIONS & COST       peek          O(1)
                       insert         O(log n)   append + sift-up
                       extract-min/max O(log n)   swap-with-last + sift-down
                       build-heap      O(n)        bottom-up sift-down from last internal node (NOT n log n!)
                       arbitrary search O(n)       no ordering guarantee beyond parent/child

JAVA PriorityQueue      new PriorityQueue<>()                         min-heap, natural order
                       new PriorityQueue<>(Collections.reverseOrder()) max-heap
                       new PriorityQueue<>((a,b) -> ...)               custom comparator
                       new PriorityQueue<>(existingCollection)         O(n) build-heap, not O(n log n)
                       peek()/poll() return null on empty — no exception thrown
                       iterating directly != sorted order; only repeated poll() guarantees priority order

KEY PATTERNS            Top-K / Kth largest   -> MIN-heap capped at size K (opposite of intuition!)
                       Kth smallest          -> MAX-heap capped at size K
                       Merge K sorted        -> heap of "current heads," push next element after each poll
                       Running median         -> TWO heaps: max-heap (lower half) + min-heap (upper half)
                       Frequency rearrange     -> max-heap keyed by frequency count
                       Dijkstra / Prim         -> min-heap keyed by current known distance/cost
```

**Remember:**

1. A heap only guarantees parent-vs-child ordering, never full sorting — that's exactly what makes it cheaper than a sorted array to maintain.
2. The array representation (with `2i+1`/`2i+2`/`(i-1)/2` index math) is the core trick that eliminates pointer overhead entirely.
3. Every heap operation reduces to sift-up or sift-down; build-heap is O(n), not O(n log n), because most nodes sit near the bottom with little distance to travel.
4. In Java, prefer `PriorityQueue` over hand-rolled heaps for real problem-solving — reserve a from-scratch implementation for understanding the mechanics or when explicitly asked.
5. The single biggest "aha" pattern to internalize: for "Kth largest," use a capped MIN-heap, not a max-heap — the root of that capped min-heap is exactly the answer.
