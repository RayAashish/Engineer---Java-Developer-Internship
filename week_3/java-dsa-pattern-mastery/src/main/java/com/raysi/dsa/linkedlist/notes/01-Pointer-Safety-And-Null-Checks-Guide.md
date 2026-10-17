# Phase 4: Linked Lists — Pointer Rewiring, Sentinel Mechanics & Safety
> **Author:** raysi  
> **Package:** `com.raysi.dsa.linkedlist`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master low-level pointer rewiring, sentinel nodes, cycle mathematical invariants, and zero-defect null safety.

---

## 1. Linked List Mental Model & JVM Memory Mechanics

### 1.1 Object Layout on HotSpot 64-bit JVM
A primitive `int[] arr = new int[1000]` takes ~4 KB of contiguous RAM. Accessing `arr[i]` takes ~1 CPU cycle if cached.
In contrast, a linked list of 1000 nodes stores disjoint heap objects:

```
[ListNode Object on Heap]
+------------------------------------+
| Mark Word (Object Lock, Age, Hash) |  8 bytes
+------------------------------------+
| Klass Word (Compressed OOP)        |  4 bytes
+------------------------------------+
| int val (Payload)                  |  4 bytes
+------------------------------------+
| ListNode next (Compressed Reference)| 4 bytes
+------------------------------------+
| Padding (8-byte alignment)         |  4 bytes
+------------------------------------+
Total per node: 24 bytes (for 4 bytes of data -> 600% memory overhead!)
```

#### The Pointer Chasing Cache Miss Problem:
Because nodes are allocated at disparate times, they land scattered throughout the heap.
```
Node A (0x10A0) ----next---> Node B (0x7F40) ----next---> Node C (0x2310)
```
The CPU Hardware Prefetcher can predict contiguous array memory accesses. It **cannot** predict where the next heap reference points. Thus, traversing a linked list triggers an L1/L2 cache miss on virtually every single node!

---

### 1.2 The Sentinel (Dummy Node) Invariant
The single greatest cause of bugs and `NullPointerException`s in linked list interviews is mutating the head node:
- Deleting the head node requires updating the external reference `head = head.next`.
- Inserting before the head requires `newNode.next = head; head = newNode;`.
- Merging lists requires checking which list has the smaller head before starting.

**The Golden Solution:** Allocate an immutable dummy node before the head:
```java
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode curr = dummy;
// Now EVERY node in the list has a guaranteed non-null predecessor!
```
At the end of your algorithm, return `dummy.next`.

---

### 1.3 The Mathematical Proof of Floyd's Cycle Algorithm
Why does the fast-and-slow pointer algorithm guarantee meeting at the cycle entrance?

```
Head                     Cycle Entrance
 o ---- F steps ---------> @ --------------+
                           ^                \
                           |                 |
                           |              Collision
                           |                 *
                           \                /
                            +--------------+
                                 <- a ->
```

Let:
- $F$ = number of nodes from `Head` to the `Cycle Entrance`.
- $C$ = length of the cycle (number of nodes).
- $a$ = distance from `Cycle Entrance` to the `Collision Point` ($*$).

When the pointers collide:
- Distance traveled by Slow: $D_{\text{slow}} = F + a$
- Distance traveled by Fast: $D_{\text{fast}} = F + a + nC$ (where $n \ge 1$ is complete loops around the cycle).

Since Fast moves at twice the speed of Slow:
$$D_{\text{fast}} = 2 \cdot D_{\text{slow}}$$
$$F + a + nC = 2(F + a)$$
$$F + a = nC$$
$$F = nC - a = (n - 1)C + (C - a)$$

#### The "Aha!" Deduction:
- $(C - a)$ is the remaining distance from the `Collision Point` to the `Cycle Entrance`.
- If you place one pointer at `Head` and keep the second pointer at the `Collision Point`, and move both forward at **1 step per iteration**:
  - The head pointer travels $F$ steps.
  - The collision pointer travels $(n - 1)$ full laps plus $(C - a)$ steps.
  - **They collide precisely at the Cycle Entrance!**

---

### 1.4 Constraints to Big-O Table for Linked Lists

| Input Length ($N$) | Permissible Big-O | Expected Approach |
| :--- | :--- | :--- |
| $N \le 10^4$ | $O(N)$ Time, $O(1)$ Space | Fast/Slow pointers, in-place reversal, two-pass scans |
| $N \le 10^5$ | $O(N \log K)$ | Merge K Sorted Lists (Min-Heap / Divide-and-Conquer) |
| $N \le 10^5$ | $O(N \log N)$ | Merge Sort on Linked List |

---

## 2. The 4 Core Linked List Patterns

```
                                  +---------------------------+
                                  |   Linked List Rewiring    |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: Dummy  | | Pattern 2: Fast & | | Pattern 3: In-    | | Pattern 4: Two-   |
| Sentinel Node     | | Slow Pointers     | | Place Reversal    | | Pointer Offset    |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  Eliminates head       Middle node           prev, curr, next      N-th from end       
  edge-case bugs        Cycle detection       Sublist reversal      Intersection of     
  Partition / Merge     Cycle entrance        K-Group reversal      two linked lists    
```

---

### Pattern 1: Sentinel / Dummy Node
- **Trigger Keywords:** `"merge lists"`, `"remove duplicates"`, `"partition list"`, `"remove elements"`.
- **Mental Model:** A dummy node anchors the list so you never have to check `if (prev == null)` or write custom branches for head replacement.

---

### Pattern 2: Fast & Slow Pointer (Tortoise & Hare)
- **Trigger Keywords:** `"has cycle"`, `"find cycle start"`, `"middle of linked list"`, `"is palindrome"`.
- **Mental Model:** Relative speed $= 2 - 1 = 1$. Slow advances by 1, fast advances by 2. Guard against null: `while (fast != null && fast.next != null)`.

---

### Pattern 3: 3-Pointer In-Place Reversal
- **Trigger Keywords:** `"reverse list"`, `"reverse sublist from left to right"`, `"reverse in k-group"`.
- **Mental Model:** Never break a pointer without first saving where it pointed:
```java
ListNode nextTemp = curr.next; // 1. Save
curr.next = prev;              // 2. Rewire
prev = curr;                   // 3. Step prev
curr = nextTemp;               // 4. Step curr
```

---

### Pattern 4: Two-Pointer Offset & Length Equalization
- **Trigger Keywords:** `"remove nth from end"`, `"intersection of two lists"`.
- **Mental Model:** 
  - Offset: Lead runner by $N$ steps, then move both together.
  - Length Equalization: When pointer A hits end, jump to head B. When pointer B hits end, jump to head A. Both travel $len(A) + len(B)$ steps and meet at intersection.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Reverse Linked List (LeetCode 206)
> **Problem:** Given the head of a singly linked list, reverse the list, and return the reversed list.

#### 1. Brute Force Instinct
Push all node values onto a stack, then pop them off to overwrite list values or create new nodes.
*Cost:* $O(N)$ time and $O(N)$ extra memory.

#### 2. "Aha!" Observation
Rewire `next` pointers in-place using three pointers (`prev = null`, `curr = head`, `nextTemp`). Zero object allocations!

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.linkedlist;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

public class ReverseListAnchor {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }

        return prev;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — Single pass through list.
- **Space Complexity:** $O(1)$ auxiliary.

---

### Anchor Problem #2: Linked List Cycle II (LeetCode 142)
> **Problem:** Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return `null`. Do not modify the linked list.

#### 1. Brute Force Instinct
Store visited `ListNode` references in a `HashSet<ListNode>`. First node already in set is cycle entrance.
*Cost:* $O(N)$ time and $O(N)$ space.

#### 2. "Aha!" Observation
Use Floyd's Cycle Finding Algorithm ($O(1)$ space). When `slow == fast`, reset one pointer to `head`. Move both at 1 step/iteration. They collide at the cycle entry.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.linkedlist;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

public class LinkedListCycleIIAnchor {
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        if (!hasCycle) return null;

        ListNode p1 = head;
        ListNode p2 = slow;
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p1;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(1)$ auxiliary.

---

### Anchor Problem #3: Reverse Nodes in k-Group (LeetCode 25)
> **Problem:** Given the head of a linked list, reverse the nodes of the list $k$ at a time, and return the modified list. Nodes left over at the end should remain as they are.

#### 1. Brute Force Instinct
Extract nodes into a list of chunks, reverse chunks of size $k$, re-link.
*Cost:* $O(N)$ auxiliary space.

#### 2. "Aha!" Observation
Iterative in-place reversal:
1. Advance a pointer $k$ steps to verify if a complete group exists.
2. If yes, reverse those $k$ nodes in-place.
3. Stitch previous group's tail to new group's head, and new group's tail to next group's head.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.linkedlist;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

public class ReverseKGroupAnchor {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k <= 1) return head;

        ListNode dummy = new ListNode(0, head);
        ListNode groupPrev = dummy;

        while (true) {
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) break;

            ListNode groupNext = kth.next;

            // Reverse current k nodes
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;
            while (curr != groupNext) {
                ListNode tmp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = tmp;
            }

            ListNode nextGroupPrev = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = nextGroupPrev;
        }

        return dummy.next;
    }

    private ListNode getKth(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }
        return curr;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — Each node visited at most twice.
- **Space Complexity:** $O(1)$ in-place.

---

### Anchor Problem #4: Merge Two Sorted Lists (LeetCode 21)
> **Problem:** Merge two sorted linked lists and return it as a sorted list. The list should be made by splicing together the nodes of the first two lists.

#### 1. Brute Force Instinct
Check `if (l1 == null) return l2; if (l2 == null) return l1;`, then compare `l1.val` and `l2.val` to set head, then loop with manual head tracking. Cluttered and error-prone.

#### 2. "Aha!" Observation
Use a single `dummy = new ListNode(0)`. While both lists non-null, wire `curr.next` to smaller node. After loop, attach whichever list is non-null. Return `dummy.next`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.linkedlist;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

public class MergeTwoSortedListsAnchor {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                curr.next = list1;
                list1 = list1.next;
            } else {
                curr.next = list2;
                list2 = list2.next;
            }
            curr = curr.next;
        }

        curr.next = (list1 != null) ? list1 : list2;
        return dummy.next;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N + M)$
- **Space Complexity:** $O(1)$ in-place rewiring.

---

### Anchor Problem #5: Remove Nth Node From End of List (LeetCode 19)
> **Problem:** Given the head of a linked list, remove the $n$-th node from the end of the list and return its head in a single pass.

#### 1. Brute Force Instinct
Two passes: First pass counts total length $L$. Second pass walks to node $L - n$ and deletes it.

#### 2. "Aha!" Observation
Single pass using two pointers with a fixed offset of $n + 1$ nodes!
- Attach `dummy.next = head`.
- Advance `fast` pointer $n + 1$ steps ahead from `dummy`.
- Now advance `slow` and `fast` together until `fast == null`.
- `slow` is guaranteed to be at node $(L - n)$, which is the node immediately preceding the target! `slow.next = slow.next.next;`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.linkedlist;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

public class RemoveNthFromEndAnchor {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy;
        ListNode slow = dummy;

        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }

        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }

        slow.next = slow.next.next;
        return dummy.next;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ single pass.
- **Space Complexity:** $O(1)$ auxiliary.

---

## 4. Common Edge Cases & Off-By-One Traps

1. **The Fast Pointer Null-Check Guard:**
   - Writing `while (fast.next != null)` will immediately throw `NullPointerException` if `fast` itself is `null`!
   - Always write: `while (fast != null && fast.next != null)`.
2. **Accidental Circular References in Partitioning:**
   - When splitting a list into two sublists (e.g., in `partition(ListNode head, int x)`), the tail of the second list might still point to a previous node in the list!
   - **Fix:** Always explicitly sever: `after.next = null;`.
3. **Losing the Head Pointer:**
   - Advancing `head = head.next` directly inside a helper method destroys the reference to the head of the list. Always create a runner: `ListNode curr = head;`.
4. **Deleting the Only Node in a List:**
   - Single-node list where $n = 1$: Dummy node ensures `slow` starts at dummy, `fast` reaches null, and `dummy.next = dummy.next.next = null`. Clean and bug-free.
