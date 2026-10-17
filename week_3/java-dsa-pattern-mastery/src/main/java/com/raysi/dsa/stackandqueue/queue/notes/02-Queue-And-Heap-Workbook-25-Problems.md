# Phase 5: Queue, Deque & Heap — Self-Guided Problem Workbook (25 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.stackandqueue.queue`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Circular Buffers & FIFO Stack Simulation

---

### Problem #1: Design Circular Queue (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Modulo Index Ring Buffer
- **Problem Statement & Constraints:** Design circular queue with fixed capacity $k$. Support `enQueue`, `deQueue`, `Front`, `Rear`, `isEmpty`, `isFull` in $O(1)$ time.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* Enqueue 1, 2, 3 into queue of size 3 $\to$ `isFull() == true`. Dequeue 1 $\to$ Enqueue 4 $\to$ `Rear() == 4`.
  - *Test Case 2 (Empty Dequeue Trap):* Dequeue on empty queue $\to$ returns `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time per Op, $O(K)$ Space):** Maintain `head = 0`, `tail = -1`, `size = 0`. Modulo formulas: `tail = (tail + 1) % k`, `head = (head + 1) % k`. `isFull` is `size == k`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Design Circular Deque (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Bidirectional Modulo Ring Buffer
- **Problem Statement & Constraints:** Circular double-ended queue supporting `insertFront`, `insertLast`, `deleteFront`, `deleteLast`, `getFront`, `getRear`.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Insert front and rear alternately $\to$ values retrieved correctly.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time per Op, $O(K)$ Space):** `front = 0`, `rear = k - 1`. Insert front moves leftward with wrap: `front = (front - 1 + k) % k`. Insert last moves rightward: `rear = (rear + 1) % k`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Implement Queue using Stacks (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Inverted LIFO State Transfer
- **Problem Statement & Constraints:** Implement FIFO queue using only two standard stacks.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Push 1, push 2, peek() -> 1, pop() -> 1, empty() -> false.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach (Amortized $O(1)$ Time per Op, $O(N)$ Space):** Maintain `inStack` and `outStack`. Push always to `inStack`. For pop/peek: if `outStack` is empty, pop all elements from `inStack` and push to `outStack`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Implement Stack using Queues (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Single-Queue Rotation
- **Problem Statement & Constraints:** Implement LIFO stack using queues.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Push 1, push 2, top() -> 2, pop() -> 2.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Single Queue Approach ($O(N)$ Push, $O(1)$ Pop):** Use one FIFO queue. When pushing $x$, enqueue $x$, then rotate all $(size - 1)$ previous elements by dequeuing and re-enqueuing them behind $x$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Number of Recent Calls (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Time-Window Sliding Dequeue
- **Problem Statement & Constraints:** Counter class counting requests within a 3000ms sliding time frame $[t - 3000, t]$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Pings at 1, 100, 3001, 3002 $\to$ returns counts [1, 2, 3, 3].
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Amortized per Ping, $O(W)$ Space):** Push $t$ to queue. While `queue.peek() < t - 3000`, dequeue stale timestamps. Return `queue.size()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Monotonic Deque & Sliding Windows

---

### Problem #6: Sliding Window Maximum (Difficulty: Hard)
- **Pattern Category:** Pattern 2: Decreasing Index Monotonic Deque
- **Problem Statement & Constraints:** Return max of each sliding window of size $k$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,3,-1,-3,5,3,6,7], k = 3 \to$ Output: `[3,3,5,5,6,7]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(K)$ Space):** Deque stores indices in descending order of value. Pop front if index $\le i - k$. Pop rear while `nums[rear] <= nums[i]`. Front is window max.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Shortest Subarray with Sum at Least K (Difficulty: Hard)
- **Pattern Category:** Pattern 2: Monotonic Deque on Prefix Sums
- **Problem Statement & Constraints:** Shortest subarray with sum $\ge k$ where numbers can be negative.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [2,-1,2], k = 3 \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Prefix sums $P$. Deque stores prefix indices. While $P[i] - P[front] \ge k$, update minLen and poll front. While $P[i] \le P[rear]$, poll rear. Push $i$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Constrained Subsequence Sum (Difficulty: Hard)
- **Pattern Category:** Pattern 2: Monotonic Deque Optimized DP
- **Problem Statement & Constraints:** Maximum sum of non-empty subsequence such that for every two consecutive elements $nums[i]$ and $nums[j]$, $|i - j| \le k$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [10,2,-10,5,20], k = 2 \to$ Output: `37` (10 + 2 + 5 + 20).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(K)$ Space):** DP transition: $dp[i] = nums[i] + \max(0, \max_{i-k \le j < i} dp[j])$. Use monotonic decreasing deque to maintain the maximum DP value in the preceding $k$ window.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Longest Continuous Subarray With Absolute Diff $\le$ Limit (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Dual Monotonic Deques (Min & Max)
- **Problem Statement & Constraints:** Return length of longest continuous subarray such that absolute difference between any two elements is $\le limit$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [8,2,4,7], limit = 4 \to$ Output: `2` ([2,4] or [4,7]).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Two deques: `maxDeque` (monotonic decreasing) and `minDeque` (monotonic increasing). Expand `right`. While `maxDeque.peekFirst() - minDeque.peekFirst() > limit`, advance `left` and evict from heads. Maximize `right - left + 1`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Jump Game VI (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Sliding Window Maximum DP
- **Problem Statement & Constraints:** Maximum score starting at index 0 and jumping at most $k$ steps forward to reach $n - 1$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,-1,-2,4,-7,3], k = 2 \to$ Output: `7`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(K)$ Space):** $score[i] = nums[i] + \max_{i-k \le j < i} score[j]$. Maintain a monotonic decreasing deque of indices storing scores in window of size $k$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Top-K PriorityQueue & Binary Heaps

---

### Problem #11: Kth Largest Element in an Array (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Min-Heap of Size K
- **Problem Statement & Constraints:** Find $k$-th largest element in array without sorting.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [3,2,1,5,6,4], k = 2 \to$ Output: `5`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log K)$ Time, $O(K)$ Space):** Min-heap of size $k$. When size exceeds $k$, poll smallest. Root is $k$-th largest.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Top K Frequent Elements (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Frequency Min-Heap
- **Problem Statement & Constraints:** Return $k$ most frequent elements.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,1,1,2,2,3], k = 2 \to$ Output: `[1, 2]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Heap Approach ($O(N \log K)$ Time, $O(N)$ Space):** Frequency map + Min-Heap comparing counts.
  - **2. Bucket Sort Alternative ($O(N)$ Time, $O(N)$ Space):** Array of lists where index is frequency count.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #13: K Closest Points to Origin (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Max-Heap of Size K
- **Problem Statement & Constraints:** Find $k$ closest points to $(0, 0)$ by Euclidean distance.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $points = [[1,3],[-2,2]], k = 1 \to$ Output: `[[-2,2]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log K)$ Time, $O(K)$ Space):** Max-Heap comparing $x^2 + y^2$. Maintain size $k$. Discard farthest points.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Sort Characters By Frequency (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Frequency Max-Heap / Bucket
- **Problem Statement & Constraints:** Sort string descending by frequency of characters.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "tree" \to$ Output: `"eert"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space for alphabet):** Count character frequencies. Insert into Max-Heap or frequency buckets. Reconstruct string.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Reorganize String (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Greedy Max-Heap Alternating Insertion
- **Problem Statement & Constraints:** Rearrange characters so no two adjacent characters are identical. Return `""` if impossible.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "aab" \to$ Output: `"aba"`.
  - *Test Case 2 (Impossible):* $s = "aaab" \to$ Output: `""` (max freq $> (N + 1) / 2$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log |\Sigma|)$ Time, $O(|\Sigma|)$ Space):** Max-Heap of character frequencies. Pop top two most frequent characters, append one of each to string, decrement counts, and push back if $> 0$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Last Stone Weight (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Max-Heap Simulation
- **Problem Statement & Constraints:** Smash heaviest two stones $x \le y$. If equal, both destroyed; else new weight is $y - x$. Return remaining weight.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $stones = [2,7,4,1,8,1] \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** Insert all into Max-Heap. While `heap.size() > 1`, pop $y$ and $x$. If $y > x$, push $y - x$. Return `heap.isEmpty() ? 0 : heap.peek()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Multi-Way Heap Merge & Event Scheduling

---

### Problem #17: Merge k Sorted Lists (Difficulty: Hard)
- **Pattern Category:** Pattern 4: K-Way Min-Heap Merge
- **Problem Statement & Constraints:** Merge $k$ sorted linked lists into one sorted linked list.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* 3 sorted lists $\to$ single merged sorted list.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log K)$ Time, $O(K)$ Space):** Min-heap initialized with head node of each list. Dummy head. Poll smallest node, attach to `curr.next`, and if `node.next != null`, push `node.next` into heap.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Find Median from Data Stream (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Dual Balancing Heaps
- **Problem Statement & Constraints:** Design data structure supporting running median from stream.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Stream [1, 2], median is 1.5; add 3, median is 2.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log N)$ Add, $O(1)$ Median):** `maxHeap` for lower half, `minHeap` for upper half. Push to `maxHeap`, sift top to `minHeap`. Balance sizes so `maxHeap.size() == minHeap.size()` or $+1$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Task Scheduler (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Max-Heap with Cooldown Deque
- **Problem Statement & Constraints:** CPU task scheduler with idle cooldown period $n$ between identical tasks. Return minimum intervals needed.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $tasks = ["A","A","A","B","B","B"], n = 2 \to$ Output: `8` (A -> B -> idle -> A -> B -> idle -> A -> B).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Max-Heap of frequencies + Cooldown queue storing pairs `[remainingCount, readyTime]`. Each clock tick: if cooldown queue has task ready at current time, re-insert to heap. Pop highest frequency from heap, execute, and push to cooldown queue if count $> 0$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Meeting Rooms II (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Min-Heap of Active End Times
- **Problem Statement & Constraints:** Given meeting time intervals, find minimum conference rooms required.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $intervals = [[0,30],[5,10],[15,20]] \to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** Sort intervals by start time. Min-Heap stores end times of active meetings. For each meeting: if `start >= heap.peek()`, existing room freed $\implies$ `heap.poll()`. Push current meeting's end time. Result is `heap.size()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Minimum Cost to Hire K Workers (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Wage-to-Quality Ratio Sorting + Max-Heap
- **Problem Statement & Constraints:** Hire $k$ workers satisfying minimum wage ratio constraints while minimizing total wage bill.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $quality = [10,20,5], wage = [70,50,30], k = 2 \to$ Output: `105.00000`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(K)$ Space):** Sort workers by $wage / quality$ ratio. Maintain a Max-Heap of quality values bounded at size $k$, tracking sum of qualities. For each ratio, if heap exceeds $k$, evict largest quality worker. Total wage $= ratio \times qualitySum$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: IPO (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Dual Heaps (Capital Min-Heap & Profit Max-Heap)
- **Problem Statement & Constraints:** Maximize capital by selecting at most $k$ projects given initial capital $w$, profits, and minimum capital requirements.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $k = 2, w = 0, profits = [1,2,3], capital = [0,1,1] \to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** Min-heap of projects sorted by capital requirement. Max-heap of profits. While projects can be started with current capital $w$, pop from min-heap and push profit to max-heap. Pop highest profit, add to $w$, repeat $k$ times.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Find K Pairs with Smallest Sums (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Min-Heap Index Boundary Advancement
- **Problem Statement & Constraints:** Given two sorted arrays $nums1$ and $nums2$, find $k$ pairs $(u, v)$ with smallest sums.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums1 = [1,7,11], nums2 = [2,4,6], k = 3 \to$ Output: `[[1,2],[1,4],[1,6]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(K \log \min(N, K))$ Time, $O(\min(N, K))$ Space):** Min-Heap initialized with pairs $(i, 0)$ for $i \in [0, \min(nums1.length, k) - 1]$. Pop pair $(i, j)$: add to result, and if $j + 1 < nums2.length$, push $(i, j + 1)$ into heap.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Sliding Window Median (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Dual Balancing Heaps with Lazy Deletion
- **Problem Statement & Constraints:** Return array of medians for each sliding window of size $k$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Sliding window of size 3 $\to$ sequence of medians.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log K)$ Time, $O(K)$ Space):** Dual balancing heaps (`maxHeap`, `minHeap`). To avoid $O(K)$ linear scan during `heap.remove(outNum)`, use **Lazy Deletion** with a hash map of pending deleted numbers. Prune invalidated top elements lazily when they surface at heap roots.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Seat Reservation Manager (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Min-Heap of Available Seats
- **Problem Statement & Constraints:** System managing $n$ seats numbered 1 to $n$. `reserve()` fetches smallest available seat number; `unreserve(seatNumber)` frees seat.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* reserve() -> 1, reserve() -> 2, unreserve(2), reserve() -> 2.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log N)$ Time per Op, $O(N)$ Space):** Min-Heap initialized with numbers $1 \dots n$ (or maintain a water-mark counter + min-heap of recycled seats to achieve $O(1)$ initial allocation). `reserve()` polls root. `unreserve(seat)` pushes seat back into heap.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
