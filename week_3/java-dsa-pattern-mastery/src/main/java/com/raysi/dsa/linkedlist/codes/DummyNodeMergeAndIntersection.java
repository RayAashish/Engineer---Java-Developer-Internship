package com.raysi.dsa.linkedlist.codes;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

/**
 * ============================================================================
 * PATTERN: Dummy Node Sentinels, Merging & Two-Pointer Equalization
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.linkedlist.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Dummy Head Sentinel: Creates a stable invariant anchor node. Eliminates
 *    all special cases for inserting or deleting the head of a list.
 * 2. Two-Pointer Offset: To remove N-th node from end, advance fast pointer by
 *    (N + 1) steps from dummy node. Then advance slow and fast together until
 *    fast reaches null. Slow is now positioned immediately before target node.
 * 3. Two-Pointer Switch (Intersection):
 *    Pointer A traverses list A then jumps to head of B.
 *    Pointer B traverses list B then jumps to head of A.
 *    Total distance traversed by each is |A| + |B|. They collide at intersection node
 *    or terminate simultaneously at null if disjoint!
 * ============================================================================
 */
public class DummyNodeMergeAndIntersection {

    /**
     * Merges two sorted linked lists into one sorted list using dummy sentinel.
     * Time Complexity : O(N + M)
     * Space Complexity: O(1) in-place rewiring
     */
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

        // Attach remaining tail
        curr.next = (list1 != null) ? list1 : list2;

        return dummy.next;
    }

    /**
     * Removes the n-th node from the end of the list and returns its head.
     * Time Complexity : O(N) single pass
     * Space Complexity: O(1)
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy;
        ListNode slow = dummy;

        // Advance fast by n + 1 steps
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }

        // Advance slow and fast simultaneously
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }

        // Delete target node
        slow.next = slow.next.next;

        return dummy.next;
    }

    /**
     * Finds the node at which the intersection of two singly linked lists begins.
     * Time Complexity : O(N + M)
     * Space Complexity: O(1)
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        ListNode pA = headA;
        ListNode pB = headB;

        // Traverse A then B, and B then A
        while (pA != pB) {
            pA = (pA == null) ? headB : pA.next;
            pB = (pB == null) ? headA : pB.next;
        }

        return pA; // Either intersection node or null
    }

    /**
     * Partitions list such that all nodes < x come before nodes >= x.
     * Preserves relative original order.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public ListNode partition(ListNode head, int x) {
        ListNode beforeDummy = new ListNode(0);
        ListNode afterDummy = new ListNode(0);
        ListNode before = beforeDummy;
        ListNode after = afterDummy;

        ListNode curr = head;
        while (curr != null) {
            if (curr.val < x) {
                before.next = curr;
                before = before.next;
            } else {
                after.next = curr;
                after = after.next;
            }
            curr = curr.next;
        }

        after.next = null; // Sever potential circular cycle
        before.next = afterDummy.next;

        return beforeDummy.next;
    }
}
