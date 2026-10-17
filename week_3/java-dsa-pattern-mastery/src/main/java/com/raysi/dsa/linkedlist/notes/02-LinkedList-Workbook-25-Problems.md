# Phase 4: Linked Lists — Self-Guided Problem Workbook (25 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.linkedlist`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Sentinel / Dummy Node Mechanics

---

### Problem #1: Merge Two Sorted Lists (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Sentinel Head Splicing
- **Problem Statement & Constraints:** Merge two sorted linked lists into one sorted list. $0 \le list1.length, list2.length \le 50$.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $l1 = [1,2,4], l2 = [1,3,4] \to$ Output: `[1,1,2,3,4,4]`.
  - *Test Case 2 (One Empty):* $l1 = [], l2 = [0] \to$ Output: `[0]`.
  - *Test Case 3 (Both Empty):* $l1 = [], l2 = [] \to$ Output: `[]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N + M)$ Time, $O(1)$ Space):** Use dummy head node. Compare current values of $l1$ and $l2$: attach the smaller node to `curr.next` and advance that list's pointer. Repeat until one list is exhausted. Attach non-null remainder to `curr.next`. Return `dummy.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Remove Duplicates from Sorted List I (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Consecutive Value Compaction
- **Problem Statement & Constraints:** Given the head of a sorted linked list, delete all duplicates such that each element appears only once.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,1,2] \to$ Output: `[1,2]`.
  - *Test Case 2 (Multiple Duplicates):* $head = [1,1,1,2,3,3] \to$ Output: `[1,2,3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** `curr = head`. While `curr != null && curr.next != null`: if `curr.val == curr.next.val`, bypass duplicate by rewiring `curr.next = curr.next.next`. Else advance `curr = curr.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Remove Duplicates from Sorted List II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Sentinel Complete Elimination of Duplicate Keys
- **Problem Statement & Constraints:** Given sorted list, delete all nodes that have duplicate numbers, leaving only distinct numbers from original list.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,3,4,4,5] \to$ Output: `[1,2,5]`.
  - *Test Case 2 (All Duplicates):* $head = [1,1,1,2,2] \to$ Output: `[]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Initialize `dummy.next = head; prev = dummy`. While `head != null`: if `head.next != null && head.val == head.next.val`, fast-forward `head` until `head.next == null || head.val != head.next.val`, then rewire `prev.next = head.next` (discarding entire duplicate run). If no duplicate, advance `prev = prev.next`. Move `head = head.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Partition List (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Dual Dummy List Split & Stitch
- **Problem Statement & Constraints:** Given head of linked list and value $x$, partition it such that all nodes $< x$ come before nodes $\ge x$, preserving relative order.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,4,3,2,5,2], x = 3 \to$ Output: `[1,2,2,4,3,5]`.
  - *Test Case 2 (Already Partitioned):* $head = [2,1], x = 2 \to$ Output: `[1,2]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Create two dummy chains: `beforeHead` and `afterHead`. Iterate through list: if node $< x$, append to `before`; else append to `after`. Crucial step: set `after.next = null` to avoid infinite circular references. Finally wire `before.next = afterHead.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Add Two Numbers (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Sentinel Digit Addition with Carry
- **Problem Statement & Constraints:** Two non-empty linked lists represent non-negative integers in reverse order. Add the two numbers and return as linked list.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $l1 = [2,4,3], l2 = [5,6,4] \to$ Output: `[7,0,8]` ($342 + 465 = 807$).
  - *Test Case 2 (Carry at Most Significant):* $l1 = [9,9,9], l2 = [1] \to$ Output: `[0,0,0,1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\max(N, M))$ Time, $O(\max(N, M))$ Space):** Dummy head. Maintain `carry = 0`. Loop while $l1 \ne null$ or $l2 \ne null$ or $carry \ne 0$. Sum values plus carry. Append `new ListNode(sum % 10)` to `curr.next`. Update `carry = sum / 10`. Advance lists.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Add Two Numbers II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: LIFO Stack Digit Alignment
- **Problem Statement & Constraints:** Most significant digit comes first (not reversed). Cannot modify input lists.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $l1 = [7,2,4,3], l2 = [5,6,4] \to$ Output: `[7,8,0,7]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N + M)$ Time, $O(N + M)$ Space):** Push all digits of $l1$ onto `stack1` and $l2$ onto `stack2`. Pop from both stacks, sum with carry, and prepend new nodes to the result list (`node.next = head; head = node`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Fast & Slow Pointer (Tortoise & Hare)

---

### Problem #7: Middle of the Linked List (Difficulty: Easy)
- **Pattern Category:** Pattern 2: 2x Velocity Pointer
- **Problem Statement & Constraints:** Given head of singly linked list, return middle node. If two middle nodes, return second.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Odd Length):* $head = [1,2,3,4,5] \to$ Output node with val `3`.
  - *Test Case 2 (Even Length):* $head = [1,2,3,4,5,6] \to$ Output node with val `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** `slow = head, fast = head`. While `fast != null && fast.next != null`: advance `slow = slow.next`, `fast = fast.next.next`. Return `slow`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Linked List Cycle I (Difficulty: Easy)
- **Pattern Category:** Pattern 2: Relative Velocity Collision
- **Problem Statement & Constraints:** Determine if linked list has a cycle. $O(1)$ memory.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Cycle Present):* List with cycle $\to$ Output: `true`.
  - *Test Case 2 (No Cycle):* Linear list $\to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Fast and slow pointers. If `fast == slow`, return true. If `fast == null || fast.next == null`, loop terminates $\implies$ return false.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Linked List Cycle II (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Floyd's Cycle Entrance Convergence
- **Problem Statement & Constraints:** Return node where cycle begins, or `null`. $O(1)$ space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [3,2,0,-4]$, pos = 1 $\to$ returns node with val `2`.
  - *Test Case 2 (No Cycle):* $head = [1]$, pos = -1 $\to$ returns `null`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Detect collision point using fast/slow. Reset `p1 = head` and keep `p2 = slow` at collision. Advance both at 1 step/iteration. Point of second meeting is cycle entrance.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Palindrome Linked List (Difficulty: Easy / Medium)
- **Pattern Category:** Pattern 2: Midpoint + Reverse Second Half
- **Problem Statement & Constraints:** Determine if singly linked list is a palindrome in $O(N)$ time and $O(1)$ extra space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Palindrome):* $head = [1,2,2,1] \to$ Output: `true`.
  - *Test Case 2 (Non-Palindrome):* $head = [1,2] \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** (1) Find end of first half with `fast.next != null && fast.next.next != null`. (2) In-place reverse the second half (`reverse(slow.next)`). (3) Compare values from `head` and reversed second half. (4) Re-reverse to restore list structure.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Reorder List (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Midpoint Split + Reverse + Alternating Interleave
- **Problem Statement & Constraints:** Reorder list to: $L_0 \to L_n \to L_1 \to L_{n-1} \dots$ in-place without altering node values.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4] \to$ Output: `[1,4,2,3]`.
  - *Test Case 2 (Odd):* $head = [1,2,3,4,5] \to$ Output: `[1,5,2,4,3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** (1) Find middle node using fast/slow. (2) Sever first half from second half (`mid.next = null`). (3) Reverse second half. (4) Merge/interleave the two lists alternating one node from each list.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Maximum Twin Sum of a Linked List (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Reversal + Twin Pairing
- **Problem Statement & Constraints:** In an even length list of size $n$, the $i$-th node's twin is node $(n - 1 - i)$. Return the maximum twin sum.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [5,4,2,1] \to$ Twins: (5,1)=6, (4,2)=6 $\to$ Max: `6`.
  - *Test Case 2 (Asymmetric):* $head = [4,2,2,3] \to$ Twins: (4,3)=7, (2,2)=4 $\to$ Max: `7`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Find middle with fast/slow. Reverse the second half. Traverse both halves simultaneously, computing `p1.val + p2.val` and maximizing twin sum.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: In-Place Reversal & Sublist Transformations

---

### Problem #13: Reverse Linked List I (Difficulty: Easy)
- **Pattern Category:** Pattern 3: 3-Pointer Link Inversion
- **Problem Statement & Constraints:** Reverse a singly linked list in-place.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5] \to$ Output: `[5,4,3,2,1]`.
  - *Test Case 2 (Single Node):* $head = [1] \to$ Output: `[1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Loop with `prev = null, curr = head`. Inside loop: save `next = curr.next`, point `curr.next = prev`, advance `prev = curr, curr = next`. Return `prev`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Reverse Linked List II (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Sublist Head-Insertion
- **Problem Statement & Constraints:** Reverse nodes from position $left$ to position $right$ (1-indexed). $1 \le left \le right \le n$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5], left = 2, right = 4 \to$ Output: `[1,4,3,2,5]`.
  - *Test Case 2 (Entire List):* $left = 1, right = 5 \to$ Reverse all nodes.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Dummy node before head. Advance `pre` pointer to node at $(left - 1)$. Let `curr = pre.next`. For $(right - left)$ iterations: pluck `next = curr.next`, rewire `curr.next = next.next; next.next = pre.next; pre.next = next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Reverse Nodes in k-Group (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Segmented Block Reversal
- **Problem Statement & Constraints:** Reverse nodes $k$ at a time. If remaining count $< k$, leave as-is.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5], k = 2 \to$ Output: `[2,1,4,3,5]`.
  - *Test Case 2 (Remaining Ignored):* $head = [1,2,3,4,5], k = 3 \to$ Output: `[3,2,1,4,5]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Dummy node. While True: find $k$-th node from `groupPrev`. If null, break. Reverse the $k$ nodes between `groupPrev` and `kth.next`. Update `groupPrev` to the tail of newly reversed group.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Swap Nodes in Pairs (Difficulty: Medium)
- **Pattern Category:** Pattern 3: 2-Node Local Permutation
- **Problem Statement & Constraints:** Swap every two adjacent nodes and return its head. Must not modify node values.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4] \to$ Output: `[2,1,4,3]`.
  - *Test Case 2 (Odd):* $head = [1,2,3] \to$ Output: `[2,1,3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Equivalent to $k$-group reversal with $k = 2$. Using dummy: let $first = prev.next, second = prev.next.next$. Rewire: $first.next = second.next; second.next = first; prev.next = second; prev = first$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Swapping Nodes in a Linked List (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Symmetric Index Two-Pointer Pointer Swap
- **Problem Statement & Constraints:** Swap values of the $k$-th node from beginning and $k$-th node from end.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5], k = 2 \to$ Output: `[1,4,3,2,5]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Locate $k$-th node from start ($first$). Start another pointer from head and move concurrently as a runner advances from $first$ to end of list. When runner hits end, second pointer is at $k$-th from end ($second$). Swap values: `int tmp = first.val; first.val = second.val; second.val = tmp;`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Rotate List (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Circular Ring Cut & Reconnect
- **Problem Statement & Constraints:** Given head of list, rotate list to the right by $k$ places. $0 \le k \le 2 \times 10^9$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5], k = 2 \to$ Output: `[4,5,1,2,3]`.
  - *Test Case 2 (Full Cycle):* $k = n \to$ Original list unchanged.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Find length $L$ and tail node. Connect `tail.next = head` forming a closed ring. Effective shift is $k = k \% L$. Walk $L - k$ steps from tail to reach new tail. Set `newHead = newTail.next`, then break ring: `newTail.next = null`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Two-Pointer Offset, Intersections & Sorting

---

### Problem #19: Remove Nth Node From End of List (Difficulty: Medium)
- **Pattern Category:** Pattern 4: $(N + 1)$ Step Gap Offset
- **Problem Statement & Constraints:** Remove $n$-th node from end of list in one pass.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5], n = 2 \to$ Output: `[1,2,3,5]`.
  - *Test Case 2 (Head Removal):* $head = [1], n = 1 \to$ Output: `[]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Dummy head. Advance fast pointer $n + 1$ steps. Advance slow and fast simultaneously until fast is null. Remove target via `slow.next = slow.next.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Intersection of Two Linked Lists (Difficulty: Easy)
- **Pattern Category:** Pattern 4: Distance Equalization Switch
- **Problem Statement & Constraints:** Return node where two lists intersect, or `null`. $O(1)$ memory, $O(N + M)$ time.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Intersecting):* Two lists merge at node with val 8 $\to$ returns reference to 8.
  - *Test Case 2 (Disjoint):* No intersection $\to$ returns `null`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N + M)$ Time, $O(1)$ Space):** `pA = headA, pB = headB`. While `pA != pB`: `pA = (pA == null) ? headB : pA.next`; `pB = (pB == null) ? headA : pB.next`. Both travel $|A| + |B|$ distance, guaranteeing convergence at intersection or mutual `null`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Delete Node in a Linked List (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Value Copy & Next Bypassing
- **Problem Statement & Constraints:** Given only access to the node to be deleted (not head). Guaranteed not to be the tail.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Given pointer to node `5` in `[4,5,1,9]` $\to$ becomes `[4,1,9]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time, $O(1)$ Space):** Overwrite current node's value with next node's value: `node.val = node.next.val`. Delete next node: `node.next = node.next.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Odd Even Linked List (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Alternating Index Chain Weaving
- **Problem Statement & Constraints:** Group all odd nodes together followed by even nodes (based on node index, not value). $O(1)$ space, $O(N)$ time.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3,4,5] \to$ Output: `[1,3,5,2,4]`.
  - *Test Case 2 (Even Total):* $head = [2,1,3,5,6,4,7] \to$ Output: `[2,3,6,7,1,5,4]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Maintain `odd = head, even = head.next, evenHead = even`. Loop while `even != null && even.next != null`: `odd.next = even.next; odd = odd.next; even.next = odd.next; even = even.next;`. Finally connect `odd.next = evenHead`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Split Linked List in Parts (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Quotient & Remainder Bucket Allocation
- **Problem Statement & Constraints:** Split list into $k$ consecutive parts. Length differences between parts $\le 1$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [1,2,3], k = 5 \to$ Output: `[[1],[2],[3],[],[]]`.
  - *Test Case 2 (Non-Uniform):* 10 elements, $k = 3 \to$ parts of length 4, 3, 3.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(k)$ Output Array):** Compute total length $N$. Each part gets base size $N / k$, with the first $N \% k$ parts getting $+1$ extra element. Traverse and cut list at appropriate lengths setting `prev.next = null`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Copy List with Random Pointer (Difficulty: Medium)
- **Pattern Category:** Pattern 4: In-Place Interweaving & Random Link Replication
- **Problem Statement & Constraints:** Construct deep copy of linked list where each node has a `next` and a `random` pointer. Do it in $O(1)$ extra auxiliary memory (excluding clone nodes).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Linked list with random pointer cross-references $\to$ clones intact.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal 3-Pass In-Place Approach ($O(N)$ Time, $O(1)$ Auxiliary Space):**
    - Pass 1: Duplicate each node and insert immediately next to original ($A \to A' \to B \to B'$).
    - Pass 2: Assign random pointers: `curr.next.random = (curr.random != null) ? curr.random.next : null`.
    - Pass 3: Unweave cloned nodes from original nodes into separate list.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Sort List (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Divide-and-Conquer Merge Sort on Linked List
- **Problem Statement & Constraints:** Sort a linked list in $O(N \log N)$ time and $O(1)$ auxiliary space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $head = [4,2,1,3] \to$ Output: `[1,2,3,4]`.
  - *Test Case 2 (Already Sorted):* $head = [-1,5,3,4,0] \to$ Output: `[-1,0,3,4,5]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Merge Sort Approach ($O(N \log N)$ Time, $O(\log N)$ Recursion Stack):**
    - Base case: `head == null || head.next == null` return `head`.
    - Find midpoint using fast/slow pointer with `fast.next != null && fast.next.next != null`.
    - Sever link: `midNext = slow.next; slow.next = null;`.
    - Recursively sort both halves: `left = sortList(head); right = sortList(midNext);`.
    - Merge sorted lists using dummy head template.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
