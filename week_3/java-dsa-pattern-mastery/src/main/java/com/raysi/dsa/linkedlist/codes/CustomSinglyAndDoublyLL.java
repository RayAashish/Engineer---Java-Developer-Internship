package com.raysi.dsa.linkedlist.codes;

/**
 * ============================================================================
 * PATTERN: Singly & Doubly Linked List Pointer Architecture
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.linkedlist.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Linked lists consist of disjoint heap objects connected via object references.
 * 2. Singly Linked List: Unidirectional traversal. Deletion requires access to the
 *    PREVIOUS node (or dummy node sentinel).
 * 3. Doubly Linked List: Bidirectional traversal. Deletion of any given node is O(1)
 *    because node.prev and node.next are immediately reachable.
 * 4. Sentinel Nodes (dummy head and dummy tail) eliminate all boundary edge cases
 *    for empty list, single element list, and head/tail insertions.
 * ============================================================================
 */
public class CustomSinglyAndDoublyLL {

    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode() {}
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public static class DoublyListNode {
        public int val;
        public DoublyListNode prev;
        public DoublyListNode next;
        public DoublyListNode(int val) { this.val = val; }
    }

    /**
     * Doubly Linked List with Sentinel Head & Tail.
     * All boundary checks are completely eliminated.
     */
    public static class SentinelDoublyLinkedList {
        private final DoublyListNode head;
        private final DoublyListNode tail;
        private int size;

        public SentinelDoublyLinkedList() {
            head = new DoublyListNode(0);
            tail = new DoublyListNode(0);
            head.next = tail;
            tail.prev = head;
            size = 0;
        }

        public void addFirst(int val) {
            DoublyListNode node = new DoublyListNode(val);
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
            size++;
        }

        public void addLast(int val) {
            DoublyListNode node = new DoublyListNode(val);
            node.prev = tail.prev;
            node.next = tail;
            tail.prev.next = node;
            tail.prev = node;
            size++;
        }

        public void remove(DoublyListNode node) {
            if (node == null || node == head || node == tail) return;
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
            size--;
        }

        public int removeFirst() {
            if (isEmpty()) throw new IllegalStateException("List is empty");
            DoublyListNode first = head.next;
            remove(first);
            return first.val;
        }

        public int removeLast() {
            if (isEmpty()) throw new IllegalStateException("List is empty");
            DoublyListNode last = tail.prev;
            remove(last);
            return last.val;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }
    }
}
