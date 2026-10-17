package com.raysi.dsa.linkedlist.codes;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

/**
 * ============================================================================
 * PATTERN: In-Place Pointer Rewiring & K-Group Reversal
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.linkedlist.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. 3-Pointer Reversal: Maintain prev, curr, nextTemp.
 *    Save nextTemp = curr.next before rewiring curr.next = prev.
 * 2. Sublist Reversal (left to right):
 *    Use dummy node. Advance to node before `left` (pre).
 *    Repeatedly pluck the next node and move it to the front of the sublist:
 *      curr = pre.next;
 *      next = curr.next;
 *      curr.next = next.next;
 *      next.next = pre.next;
 *      pre.next = next;
 * 3. K-Group Reversal:
 *    First count if k nodes exist. If not, leave intact.
 *    If k exist, reverse those k nodes, reconnect boundaries, and recurse/iterate.
 * ============================================================================
 */
public class InPlaceReversalAndKGroupReversal {

    /**
     * Reverses an entire singly linked list in-place.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode nextTemp = curr.next; // 1. Save next reference
            curr.next = prev;              // 2. Reverse link
            prev = curr;                   // 3. Advance prev
            curr = nextTemp;               // 4. Advance curr
        }

        return prev;
    }

    /**
     * Reverses the nodes of the list from position left to position right.
     * 1-indexed.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || left == right) return head;

        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;

        // Step 1: Reach node just before position 'left'
        for (int i = 1; i < left; i++) {
            pre = pre.next;
        }

        // Step 2: Head of sublist to reverse
        ListNode curr = pre.next;

        // Step 3: Pluck and insert next nodes at sublist front
        for (int i = 0; i < right - left; i++) {
            ListNode next = curr.next;
            curr.next = next.next;
            next.next = pre.next;
            pre.next = next;
        }

        return dummy.next;
    }

    /**
     * Reverses nodes of a linked list k at a time.
     * If remaining nodes < k, leave them unchanged.
     * Time Complexity : O(N)
     * Space Complexity: O(1) iterative
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k <= 1) return head;

        ListNode dummy = new ListNode(0, head);
        ListNode groupPrev = dummy;

        while (true) {
            // Check if there are at least k nodes remaining
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) break;

            ListNode groupNext = kth.next;

            // Reverse current k-group
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            while (curr != groupNext) {
                ListNode tmp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = tmp;
            }

            // Re-stitch group connections
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
