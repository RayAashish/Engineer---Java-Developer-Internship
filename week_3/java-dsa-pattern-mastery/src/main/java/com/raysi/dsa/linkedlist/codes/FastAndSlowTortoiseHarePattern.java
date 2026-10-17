package com.raysi.dsa.linkedlist.codes;

import com.raysi.dsa.linkedlist.codes.CustomSinglyAndDoublyLL.ListNode;

/**
 * ============================================================================
 * PATTERN: Fast & Slow Pointer (Tortoise & Hare) Mechanics
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.linkedlist.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Two pointers traverse at different velocities (slow moves 1 step, fast moves 2 steps).
 * 2. Middle Node: When fast reaches end, slow is positioned exactly at mid.
 * 3. Cycle Detection: If cycle exists, relative speed is (2 - 1) = 1 node/step.
 *    The fast pointer closes distance by 1 node per iteration, guaranteeing a collision
 *    within at most C steps (where C is cycle length).
 * 4. Cycle Entry Math:
 *    Let distance from head to entry = F.
 *    Let distance from entry to collision = a.
 *    Distance traveled by slow = F + a.
 *    Distance traveled by fast = F + a + nC.
 *    Since 2 * slow = fast:
 *      2(F + a) = F + a + nC => F + a = nC => F = nC - a = (n - 1)C + (C - a).
 *    Therefore, moving one pointer from head and one from collision point at 1 step/iter
 *    causes them to meet precisely at the cycle entrance!
 * ============================================================================
 */
public class FastAndSlowTortoiseHarePattern {

    /**
     * Finds the middle node of a linked list.
     * For even number of nodes, returns the second middle node.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public ListNode findMiddle(ListNode head) {
        if (head == null) return null;
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    /**
     * Detects whether a linked list contains a cycle.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the node where the cycle begins.
     * If no cycle exists, returns null.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;

        // Phase 1: Determine collision point
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        if (!hasCycle) return null;

        // Phase 2: Locate cycle entry node
        ListNode p1 = head;
        ListNode p2 = slow;

        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p1; // Cycle entrance
    }

    /**
     * Determines if a singly linked list is a palindrome in O(1) auxiliary space.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        // Step 1: Find first half end (slow pointer)
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Step 2: Reverse second half
        ListNode secondHalfHead = reverseList(slow.next);

        // Step 3: Compare halves
        ListNode p1 = head;
        ListNode p2 = secondHalfHead;
        boolean isPalin = true;

        while (p2 != null) {
            if (p1.val != p2.val) {
                isPalin = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }

        // Step 4: Restore list structure (good practice)
        slow.next = reverseList(secondHalfHead);

        return isPalin;
    }

    private ListNode reverseList(ListNode head) {
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
