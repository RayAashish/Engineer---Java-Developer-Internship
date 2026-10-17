# Phase 5: Queue, Deque & Heap — FIFO State, Sliding Windows & Binary Heaps
> **Author:** raysi  
> **Package:** `com.raysi.dsa.stackandqueue.queue`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master circular queues, monotonic double-ended queues (Deques), binary heap array representations, and dual-heap stream balancing.

---

## 1. Queue, Deque & Heap Mental Model & JVM Mechanics

### 1.1 Array-Backed Circular Queue Mechanics
In a naive linear array queue, `enqueue` moves `tail` rightward and `dequeue` moves `head` rightward. When `tail == capacity - 1`, no new elements can be enqueued even if the front half of the array is empty!

#### The Circular Array Buffer:
Wrap indices around the array boundaries using modulo arithmetic:
$$\text{tail} = (\text{tail} + 1) \pmod{\text{capacity}}$$
$$\text{head} = (\text{head} + 1) \pmod{\text{capacity}}$$

#### Bitmask Performance Trick in `ArrayDeque`:
When capacity is constrained to a **power of 2** ($2^k$), modulo division can be replaced with a single-cycle bitwise AND mask:
$$\text{index} \pmod{\text{capacity}} \equiv \text{index} \ \& \ (\text{capacity} - 1)$$
`java.util.ArrayDeque` leverages this exact optimization, achieving peak CPU instruction throughput.

---

### 1.2 Binary Heap & `java.util.PriorityQueue` Mechanics
In Java, `PriorityQueue<E>` is implemented as an array-based **Complete Binary Min-Heap**:

```
           [0] 10
          /      \
      [1] 20     [2] 15
      /    \     /    \
   [3] 30 [4] 40 [5] 50 [6] 60
```

#### Array Index Mapping Formulas (0-Indexed):
- Parent of node $i$: $\lfloor (i - 1) / 2 \rfloor$ or `(i - 1) >>> 1`
- Left child of node $i$: $2i + 1$ or `(i << 1) + 1`
- Right child of node $i$: $2i + 2$ or `(i << 1) + 2`

#### Complexity:
- `offer(e)`: Pushes to bottom leaf and performs **Sift-Up** $\implies O(\log N)$
- `poll()`: Replaces root with last leaf and performs **Sift-Down** $\implies O(\log N)$
- `peek()`: Reads root index 0 $\implies O(1)$
- **Heapify Array (`new PriorityQueue<>(collection)`):** Uses bottom-up sift-down $\implies O(N)$ time (mathematically $\sum_{h=0}^{\log N} \frac{N}{2^h} h = O(N)$), NOT $O(N \log N)$!

#### Critical Java Comparator Integer Overflow Trap:
```java
// DANGEROUS: If a is Integer.MIN_VALUE and b is positive, (a - b) overflows to positive!
PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> a - b);

// SAFE: Avoids 32-bit arithmetic overflow
PriorityQueue<Integer> pq = new PriorityQueue<>(Integer::compare);
// Max-heap:
PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
```

---

### 1.3 Constraints to Big-O Table for Queue & Heap

| Constraints ($N$) | Permissible Big-O | Applicable Pattern |
| :--- | :--- | :--- |
| $N \le 10^5$ | $O(N)$ | Monotonic Deque (Sliding Window Maximum) |
| $N \le 10^5, K \le 10^5$ | $O(N \log K)$ | Top-K Min-Heap of size K |
| Stream Input | $O(\log N)$ per event | Dual Balancing Heaps (Running Median) |

---

## 2. The 4 Core Queue & Heap Patterns

```
                                  +---------------------------+
                                  |   Queue, Deque & Heaps    |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: Queue  | | Pattern 2: Mono   | | Pattern 3: Top-K  | | Pattern 4: Dual   |
| Using Stacks      | | Deque (Slid. Win) | | PriorityQueue     | | Balancing Heaps   |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  inStack & outStack    Max/Min in Window     Min-Heap of size K    MaxHeap + MinHeap   
  Amortized O(1)        Front = extremum      Discard small items   Median in O(1)      
  Circular Modulo       Rear pops losers      O(N log K) time       O(log N) insert     
```

---

### Pattern 1: Circular Array & Two-Stack Queue
- **Trigger Keywords:** `"design circular queue"`, `"implement queue using stacks"`.
- **Mental Model:** Two stacks invert LIFO into FIFO. Push to `inStack`. Only when `outStack` is empty, flush all elements from `inStack` to `outStack`.

---

### Pattern 2: Monotonic Deque (Sliding Window Extremum)
- **Trigger Keywords:** `"sliding window maximum"`, `"minimum in window"`, `"subarray sum at least K"`.
- **Mental Model:** A double-ended queue where values decrease from front to back.
  1. Evict front if its index has slipped out of the moving window.
  2. Evict back if incoming value is greater than or equal to `deque.peekLast()`.
  3. Window extremum is always at `deque.peekFirst()`.

---

### Pattern 3: Top-K Min-Heap
- **Trigger Keywords:** `"kth largest element"`, `"top k frequent"`, `"k closest points"`.
- **Mental Model:** To find the $K$ largest elements, maintain a **MIN-HEAP** of size $K$. When size exceeds $K$, pop the root (which is the smallest of the top $K$). At the end, the heap contains the $K$ largest items, and the root is the $K$-th largest!

---

### Pattern 4: Dual Balancing Heaps
- **Trigger Keywords:** `"find median from data stream"`, `"sliding window median"`.
- **Mental Model:** Split numbers into two halves:
  - `maxHeap` stores the lower half.
  - `minHeap` stores the upper half.
  - Balance condition: `maxHeap.size() == minHeap.size()` or `maxHeap.size() == minHeap.size() + 1`.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Sliding Window Maximum (LeetCode 239)
> **Problem:** Given an array $nums$ and sliding window size $k$, return the max sliding window.

#### 1. Brute Force Instinct
Scan all $k$ elements in each window $\implies O(N \cdot k)$ time.

#### 2. "Aha!" Observation
Maintain a Monotonic Deque of indices. As window slides:
- Pop elements from rear that are $\le nums[i]$ (they can never be the max).
- Pop element from front if index $\le i - k$.
- `nums[deque.peekFirst()]` is always the window max.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.queue;

import java.util.ArrayDeque;
import java.util.Deque;

public class SlidingWindowMaximumAnchor {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0) return new int[0];

        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resIdx = 0;
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // Evict indices out of window
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            // Evict smaller elements from rear
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            // Record max
            if (i >= k - 1) {
                result[resIdx++] = nums[deque.peekFirst()];
            }
        }
        return result;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — Every index added and removed at most once.
- **Space Complexity:** $O(K)$ auxiliary deque.

---

### Anchor Problem #2: Kth Largest Element in an Array (LeetCode 215)
> **Problem:** Given an integer array $nums$ and an integer $k$, return the $k$-th largest element in the array.

#### 1. Brute Force Instinct
`Arrays.sort(nums)` and return `nums[nums.length - k]` $\implies O(N \log N)$.

#### 2. "Aha!" Observation
Maintain a `PriorityQueue` (Min-Heap) bounded at size $k$.
For each element, add to heap; if `heap.size() > k`, pop the smallest.
At termination, the smallest of the top $k$ (the heap root) is the $k$-th largest!

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.queue;

import java.util.PriorityQueue;

public class KthLargestElementAnchor {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \log K)$
- **Space Complexity:** $O(K)$

---

### Anchor Problem #3: Top K Frequent Elements (LeetCode 347)
> **Problem:** Given an integer array $nums$ and an integer $k$, return the $k$ most frequent elements.

#### 1. Brute Force Instinct
Count frequencies in HashMap, sort entries by frequency descending $\implies O(U \log U)$ where $U$ is unique elements.

#### 2. "Aha!" Observation
Min-Heap of size $k$ ordered by frequency. Compare entries: `PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.comparingInt(freqMap::get));`. Only keep the $k$ most frequent.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.queue;

import java.util.*;

public class TopKFrequentElementsAnchor {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);

        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            Comparator.comparingInt(freqMap::get)
        );

        for (int num : freqMap.keySet()) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        int[] res = new int[k];
        for (int i = 0; i < k; i++) res[i] = minHeap.poll();
        return res;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \log K)$
- **Space Complexity:** $O(N)$ for frequency map.

---

### Anchor Problem #4: Find Median from Data Stream (LeetCode 295)
> **Problem:** Design a data structure that supports adding integers from a stream and finding the median of all elements.

#### 1. Brute Force Instinct
Insert into sorted list in $O(N)$ via binary search insertion $\implies O(N)$ per `addNum`.

#### 2. "Aha!" Observation
Two balancing heaps: `maxHeap` for lower half, `minHeap` for upper half.
- Step 1: Push to `maxHeap`.
- Step 2: Sift highest lower-half element into `minHeap`: `minHeap.offer(maxHeap.poll())`.
- Step 3: Rebalance size: if `maxHeap.size() < minHeap.size()`, pop from `minHeap` back into `maxHeap`.
- Invariant: `maxHeap` has equal or 1 more element than `minHeap`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.queue;

import java.util.Collections;
import java.util.PriorityQueue;

public class MedianFinderAnchor {
    private final PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        } else {
            return (maxHeap.peek() + (double) minHeap.peek()) / 2.0;
        }
    }
}
```
*Complexity:*
- **Time Complexity:** `addNum`: $O(\log N)$, `findMedian`: $O(1)$.
- **Space Complexity:** $O(N)$.

---

### Anchor Problem #5: Shortest Subarray with Sum at Least K (LeetCode 862)
> **Problem:** Given integer array $nums$ (can contain negative values) and integer $k$, return length of shortest non-empty subarray with sum $\ge k$.

#### 1. Brute Force Instinct
Standard two-pointer sliding window fails because array contains negative numbers (window sum is not monotonic) $\implies O(N^2)$.

#### 2. "Aha!" Observation
Compute prefix sum array $P[i]$. A subarray from $j$ to $i$ has sum $P[i] - P[j] \ge k$.
Use a Monotonic Deque of prefix indices:
1. While $P[i] - P[deque.peekFirst()] \ge k$, record `i - deque.pollFirst()`. (Once a starting index $j$ yields a valid subarray for current $i$, any future subarray starting at $j$ will be strictly longer, so $j$ can be safely discarded!).
2. While $P[i] \le P[deque.peekLast()]$, poll rear. (If $P[i]$ is smaller than an earlier prefix, $P[i]$ is both smaller AND closer to future elements $\implies$ earlier prefix is dominated and useless).

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.queue;

import java.util.ArrayDeque;
import java.util.Deque;

public class ShortestSubarrayAnchor {
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + nums[i];

        int minLen = n + 1;
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i <= n; i++) {
            while (!deque.isEmpty() && prefix[i] - prefix[deque.peekFirst()] >= k) {
                minLen = Math.min(minLen, i - deque.pollFirst());
            }
            while (!deque.isEmpty() && prefix[i] <= prefix[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }

        return (minLen <= n) ? minLen : -1;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(N)$

---

## 4. Common Edge Cases & Traps

1. **Circular Modulo Wrap Trap:**
   - Modulo index calculation `(head + 1) % capacity` works, but when decrementing: `(head - 1 + capacity) % capacity` is needed to prevent negative indices!
2. **Double-Ended Eviction in Monotonic Deque:**
   - Remember that a monotonic deque evicts from BOTH ends:
     - Front: when element falls out of moving window (`index <= i - k`).
     - Back: when incoming element breaks monotonic decreasing order (`nums[back] <= nums[i]`).
3. **Integer Division in Median:**
   - When both heaps have equal size, writing `(maxHeap.peek() + minHeap.peek()) / 2` performs integer division! Must cast to `double`: `((double) maxHeap.peek() + minHeap.peek()) / 2.0`.
