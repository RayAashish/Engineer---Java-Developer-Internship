# Java Collections Framework — Mastery Guide
> Author: raysi  
> Target Level: Senior / Staff / Principal Software Engineers (L5-L7)  
> JDK Versions: JDK 17 / JDK 21 LTS  

---

## Executive Overview
The **Java Collections Framework (JCF)** is the backbone of memory management, algorithmic data processing, and concurrency coordination in enterprise Java applications. While basic developers treat collections as simple data containers, backend architects must understand their internal memory layouts, garbage collection (GC) footprints, cache line locality, concurrency barriers, and algorithmic edge cases under extreme scale.

This repository is a production-grade, deep-dive reference architecture designed for backend engineers preparing for high-stakes systems and algorithms interviews, performance optimization, and architectural reviews.

---

## 1. Architectural Hierarchy & Complete Taxonomy

Below is the complete structural hierarchy of the Java Collections Framework. Note that `java.util.Map` does **not** inherit from `java.lang.Iterable` or `java.util.Collection` because it models a key-value mapping rather than a distinct collection of individual entities.

```
                                      java.lang.Iterable<T>
                                                │
                                                ▼
                                    java.util.Collection<E>
                   ┌────────────────────────────┼────────────────────────────┐
                   │                            │                            │
                   ▼                            ▼                            ▼
           java.util.List<E>            java.util.Queue<E>            java.util.Set<E>
           ┌───────┴───────┐                    │                     ┌──────┴──────┐
           │               │                    ▼                     │             │
      (Abstract)      (Abstract)        java.util.Deque<E>       (Abstract)         ▼
   AbstractList<E> AbstractSequentialList<E>    │              AbstractSet<E> java.util.SortedSet<E>
     ┌─────┴─────┐         │                    │                     │             │
     │           │         │                    │              ┌──────┼──────┐      ▼
     ▼           ▼         ▼                    ▼              ▼      ▼      ▼ java.util.NavigableSet<E>
ArrayList<E>  Vector<E> LinkedList<E> ◄─── ArrayDeque<E>   HashSet<E> │  EnumSet<E>    │
                 │         │                                   │      │                ▼
                 ▼         │                             LinkedHashSet<E>          TreeSet<E>
              Stack<E>     │                                          │                │
             [LEGACY]      └──────────────────────────────────────────┼────────────────┤
                                                                      │                │
   [CONCURRENT IMPLEMENTATIONS]                                       ▼                ▼
   ├── CopyOnWriteArrayList<E>                             CopyOnWriteArraySet<E> ConcurrentSkipListSet<E>
   ├── ConcurrentLinkedQueue<E> / ConcurrentLinkedDeque<E>       [CONCURRENT]          [CONCURRENT]
   ├── ArrayBlockingQueue<E> / LinkedBlockingQueue<E>
   ├── PriorityBlockingQueue<E> / DelayQueue<E>
   └── SynchronousQueue<E> / LinkedTransferQueue<E>


                                    java.util.Map<K, V>
                                             │
                       ┌─────────────────────┴─────────────────────┐
                       │                                           │
                       ▼                                           ▼
             (Abstract) AbstractMap<K, V>                java.util.SortedMap<K, V>
       ┌───────────────┼───────────────┬───────────────┐           │
       ▼               ▼               ▼               ▼           ▼
  HashMap<K, V>  IdentityHashMap<K, V> WeakHashMap<K, V> EnumMap<K, V> NavigableMap<K, V>
       │                                                           │
       ▼                                                           ▼
LinkedHashMap<K, V>                                            TreeMap<K, V>
       │                                                           │
       ├─────────────────────────────────┬─────────────────────────┤
       │                                 │                         │
  Hashtable<K, V> [LEGACY]      ConcurrentHashMap<K, V>  ConcurrentSkipListMap<K, V>
       │                             [CONCURRENT]              [CONCURRENT]
       ▼
  Properties [LEGACY]
```

### Legend of Designations
- `[LEGACY]`: Synchronized on `this` monitor via method signatures; obsolete due to coarse lock contention and suboptimal memory overhead. Avoid in modern systems.
- `[CONCURRENT]`: Lock-free, lock-striped, or copy-on-write implementations optimized for multi-threaded environments under `java.util.concurrent`.

---

## 2. Exhaustive Big-O Complexity & Behavioral Cheat Sheet

The table below delineates the exact asymptotic time bounds, memory overheads, null contracts, and ordering guarantees across the entire collection taxonomy.

| Collection Class | Add / Offer (Avg / Worst) | Get / Peek (Avg / Worst) | Remove / Poll (Avg / Worst) | Contains (Avg / Worst) | size() | Thread-Safe? | Null Allowed? | Ordering Guarantee |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`ArrayList`** | $O(1)$ amortized / $O(N)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(N)$ / $O(N)$ | $O(1)$ | No | Yes | Insertion Order |
| **`LinkedList`** | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(1)$ head/tail / $O(N)$ mid | $O(N)$ / $O(N)$ | $O(1)$ | No | Yes | Insertion Order |
| **`Vector`** `[LEGACY]` | $O(1)$ amortized / $O(N)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(N)$ / $O(N)$ | $O(1)$ | Yes (method sync) | Yes | Insertion Order |
| **`Stack`** `[LEGACY]` | $O(1)$ amortized / $O(N)$ | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(1)$ | Yes (method sync) | Yes | LIFO |
| **`CopyOnWriteArrayList`** | $O(N)$ / $O(N)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(N)$ / $O(N)$ | $O(1)$ | Yes (copy-on-write) | Yes | Insertion Order |
| **`ArrayDeque`** | $O(1)$ amortized / $O(N)$ | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(1)$ | No | **No** (Throws NPE) | FIFO / LIFO |
| **`PriorityQueue`** | $O(\log N)$ / $O(\log N)$ | $O(1)$ / $O(1)$ | $O(\log N)$ / $O(\log N)$ | $O(N)$ / $O(N)$ | $O(1)$ | No | **No** (Throws NPE) | Natural / Comparator Heap |
| **`ConcurrentLinkedQueue`**| $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(N)$ (traversal) | Yes (lock-free CAS) | **No** (Throws NPE) | FIFO |
| **`ArrayBlockingQueue`** | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(1)$ | Yes (ReentrantLock) | **No** (Throws NPE) | FIFO |
| **`LinkedBlockingQueue`** | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(N)$ / $O(N)$ | $O(1)$ (`AtomicInteger`) | Yes (Two-lock queue)| **No** (Throws NPE) | FIFO |
| **`HashSet`** | $O(1)$ / $O(\log N)$ tree | N/A | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ | No | Yes (1 null) | Unordered |
| **`LinkedHashSet`** | $O(1)$ / $O(\log N)$ tree | N/A | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ | No | Yes (1 null) | Insertion Order |
| **`TreeSet`** | $O(\log N)$ / $O(\log N)$ | N/A | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ / $O(\log N)$ | $O(1)$ | No | **No** (throws NPE) | Sorted (Red-Black) |
| **`EnumSet`** | $O(1)$ / $O(1)$ | N/A | $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(1)$ | No | **No** (Throws NPE) | Enum Declaration Order |
| **`CopyOnWriteArraySet`** | $O(N)$ / $O(N)$ | N/A | $O(N)$ / $O(N)$ | $O(N)$ / $O(N)$ | $O(1)$ | Yes (copy-on-write) | Yes | Insertion Order |
| **`ConcurrentSkipListSet`**| $O(\log N)$ / $O(\log N)$ | N/A | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ / $O(\log N)$ | $O(N)$ (traversal) | Yes (lock-free CAS) | **No** (Throws NPE) | Sorted (SkipList) |
| **`HashMap`** | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ key / $O(N)$ val | $O(1)$ | No | 1 Null Key, Any Null Val| Unordered |
| **`LinkedHashMap`** | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ key / $O(N)$ val | $O(1)$ | No | 1 Null Key, Any Null Val| Insertion / Access Order |
| **`TreeMap`** | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ key / $O(N)$ val | $O(1)$ | No | **No null key**, Any Null Val| Sorted (Red-Black) |
| **`WeakHashMap`** | $O(1)$ / $O(N)$ list | $O(1)$ / $O(N)$ list | $O(1)$ / $O(N)$ list | $O(1)$ key / $O(N)$ val | $O(1)$ | No | 1 Null Key, Any Null Val| Unordered |
| **`IdentityHashMap`** | $O(1)$ / $O(N)$ linear probe| $O(1)$ / $O(N)$ | $O(1)$ / $O(N)$ | $O(1)$ key / $O(N)$ val | $O(1)$ | No | 1 Null Key, Any Null Val| Unordered (Identity `==`) |
| **`EnumMap`** | $O(1)$ / $O(1)$ (array idx)| $O(1)$ / $O(1)$ | $O(1)$ / $O(1)$ | $O(1)$ key / $O(N)$ val | $O(1)$ | No | **No null key**, Any Null Val| Enum Declaration Order |
| **`Hashtable`** `[LEGACY]` | $O(1)$ / $O(N)$ list | $O(1)$ / $O(N)$ list | $O(1)$ / $O(N)$ list | $O(1)$ key / $O(N)$ val | $O(1)$ | Yes (method sync) | **No** (NPE on key/val) | Unordered |
| **`ConcurrentHashMap`** | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ / $O(\log N)$ tree | $O(1)$ key / $O(N)$ val | $O(1)$ (approx) | Yes (synchronized node)| **No** (NPE on key/val) | Unordered |
| **`ConcurrentSkipListMap`**| $O(\log N)$ / $O(\log N)$ | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ / $O(\log N)$ | $O(\log N)$ key / $O(N)$ val| $O(N)$ (traversal) | Yes (lock-free CAS) | **No** (NPE on key/val) | Sorted (SkipList) |

---

## 3. Systematic Study Order & Learning Roadmap

To build deep, architectural intuition rather than memorizing APIs, master the framework sequentially through five designated phases:

```
┌──────────────────────────────────────────────────────────────────────────────┐
│ Phase 0: Foundations, Traversal Protocols & Abstraction Contracts            │
│ ├── Iterable, Iterator, ListIterator, Spliterator                            │
│ ├── Collection root interface, AbstractCollection, Template Method Pattern   │
│ ├── Comparable vs Comparator contracts, Total Ordering vs Equivalence        │
│ └── Structural modifications, modCount internals, Fail-Fast vs Fail-Safe     │
└──────────────────────────────────────┬───────────────────────────────────────┘
                                       │
                                       ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│ Phase 1: Sequential & Indexed Collections                                    │
│ ├── ArrayList: Dynamic growth, arraycopy, capacity amortizations             │
│ ├── LinkedList: Doubly linked list, node pointer overhead, cache thrashing   │
│ ├── ArrayDeque: Circular ring buffer, power-of-two bitwise indexing          │
│ └── PriorityQueue: Binary min-heap, siftUp/siftDown, array compaction        │
└──────────────────────────────────────┬───────────────────────────────────────┘
                                       │
                                       ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│ Phase 2: Hashing & Tree-Based Data Structures                                │
│ ├── HashMap: Bucket hashing, XOR bit spread, TreeBin Red-Black split         │
│ ├── LinkedHashMap: Doubly linked bucket overlay, LRU cache eviction hooks    │
│ ├── TreeMap: Left/Right Red-Black tree balancing, successor traversal        │
│ └── Specialized Sets & Maps: EnumMap/EnumSet (Bit vectors), WeakHashMap      │
└──────────────────────────────────────┬───────────────────────────────────────┘
                                       │
                                       ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│ Phase 3: High-Throughput & Concurrent Collections                            │
│ ├── ConcurrentHashMap: Table init CAS, Synchronized bin head, TreeBin locking│
│ ├── CopyOnWriteArrayList: Array snapshotting, volatile array references      │
│ ├── Blocking Queues: ArrayBlockingQueue (1 lock) vs LinkedBlockingQueue (2)  │
│ └── ConcurrentSkipListMap: Lock-free probabilistic multi-level indexing      │
└──────────────────────────────────────┬───────────────────────────────────────┘
                                       │
                                       ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│ Phase 4: Production Tuning, Memory Layout & Interview Gauntlet               │
│ ├── JVM Memory Layout: Compressed OOPs, Object Headers (Mark/Klass words)    │
│ ├── Cache Line Optimization: 64-byte padding, false sharing avoidance       │
│ ├── JMH Benchmarking: Zero-allocation patterns, escape analysis verification │
│ └── Hardcore Interview Scenarios: Distributed locks, rate limiters, LRU/LFU  │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Phase Breakdown & Curriculum Scope

### Phase 0: Foundations & Abstractions
- **Focus**: Core protocols governing traversal, contracts, and failure mechanisms.
- **Key Concepts**:
  - `Iterable<T>` contract, lazy vs eager traversal, bytecode lowering of enhanced for-loops.
  - `Iterator<E>` vs `ListIterator<E>` vs `Spliterator<T>` (work-stealing, characteristics bitmask).
  - Abstract base classes (`AbstractCollection`, `AbstractList`) utilizing the Template Method pattern.
  - Mathematical laws of `Comparable` and `Comparator`: Anti-symmetry, Transitivity, Congruence, and Consistency with `equals()`.
  - Concurrency failure boundaries: `modCount`, transient tracking, single-threaded fail-fast pitfalls, and reflection-based internal verification.

### Phase 1: Sequential & Indexed Collections
- **Focus**: Contiguous vs non-contiguous memory allocations, CPU L1/L2 cache locality, and ring buffers.
- **Key Concepts**:
  - `ArrayList`: Geometric scaling ($1.5\times$), `System.arraycopy` native memory move, amortized cost proof.
  - `LinkedList`: Per-node 24-byte pointer overhead on 64-bit JVM, memory fragmentation, and spatial cache-miss latency.
  - `ArrayDeque`: Power-of-two circular ring buffer utilizing bitwise `(head - 1) & (elements.length - 1)` masking for blazing performance over `Stack` and `LinkedList`.
  - `PriorityQueue`: Contiguous array representation of balanced binary heaps, `siftUp` and `siftDown` heapify operations.

### Phase 2: Hashing & Tree-Based Structures
- **Focus**: Hash dispersion, collision resolution, balanced trees, and memory reclamation.
- **Key Concepts**:
  - `HashMap`: `(n - 1) & (h ^ (h >>> 16))` hash spread, bucket collision chaining, and $O(N) \to O(\log N)$ treeification via Red-Black TreeBin (`TREEIFY_THRESHOLD = 8`, `UNTREEIFY_THRESHOLD = 6`, `MIN_TREEIFY_CAPACITY = 64`).
  - `LinkedHashMap`: Maintaining deterministic iteration via doubly linked nodes, building an $O(1)$ LRU cache using `removeEldestEntry`.
  - `TreeMap`: Red-Black tree properties (root black, no consecutive red nodes, equal black depth), tree rotations, and in-order traversal.
  - Specialized structures: `IdentityHashMap` (reference equality `==`, linear probing), `EnumMap`/`EnumSet` (bit-vector operations), and `WeakHashMap` (relying on `java.lang.ref.ReferenceQueue` and `WeakReference` for automatic key scavenging).

### Phase 3: High-Performance & Concurrent Collections
- **Focus**: Non-blocking algorithms, CAS primitives, memory fences, and lock striping.
- **Key Concepts**:
  - `ConcurrentHashMap`: Modern Java 8+ re-architecture removing Segment locking; fine-grained bucket synchronization (`synchronized(node)`), lock-free read path (`volatile Node.val`), and parallel resizing via `transfer()`.
  - `CopyOnWriteArrayList`: ReentrantLock write-path snapshotting with zero read-lock contention.
  - `BlockingQueue` patterns: Producer-consumer decoupling, condition signaling (`notEmpty`, `notFull`), and `LinkedTransferQueue` dual-data-structure efficiency.
  - `ConcurrentSkipListMap`: Lock-free probabilistic multi-level indexing using CAS marker nodes.

### Phase 4: Production Tuning, Memory Layout & Interview Gauntlet
- **Focus**: Hardware mechanical sympathy, memory footprints, and interview mastery.
- **Key Concepts**:
  - Memory footprints under 64-bit HotSpot: Compressed OOPs (`-XX:+UseCompressedOops`), 12-byte object headers (8-byte Mark Word + 4-byte Klass Word), 4-byte padding alignments.
  - Cache Line Bouncing and False Sharing: `@jdk.internal.vm.annotation.Contended`, 64-byte hardware cache lines.
  - Non-obvious edge cases, fail-fast anomalies, and senior interview system-design implementations (Thread-safe TTL caches, bounded priority queues, high-frequency order books).

---

## 5. Architectural Directory Layout

```
java-collections-mastery/
├── README.md                                          <-- [This File: Global Architectural Blueprint]
└── src/main/java/com/raysi/jcf/
    ├── foundations/                                   <-- Phase 0: Core Contracts & Traversal Protocols
    │   ├── notes/
    │   │   ├── 01-Iterable-And-Iterator-Internals.md
    │   │   ├── 02-Collection-Root-Interface.md
    │   │   └── 03-Comparable-Vs-Comparator-Contract.md
    │   └── codes/
    │       ├── CustomIterableDemo.java
    │       ├── FailFastVsFailSafeDemo.java
    │       └── ComparableVsComparatorDemo.java
    ├── lists/                                         <-- Phase 1: Indexed & Sequential Internals
    ├── queues/                                        <-- Phase 1: Ring Buffers, Heaps & Deques
    ├── maps/                                          <-- Phase 2: Hash Tables, Trees & Specialized Maps
    ├── sets/                                          <-- Phase 2: Set Implementations & Bit Vectors
    ├── concurrent/                                    <-- Phase 3: Lock-Free & Fine-Grained Concurrency
    └── tuning/                                        <-- Phase 4: JMH Benchmarks, Memory & Layout
```
