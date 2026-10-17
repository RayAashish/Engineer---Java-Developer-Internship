package com.raysi.dsa.stackandqueue.stack.codes;

import java.util.EmptyStackException;

/**
 * ============================================================================
 * PATTERN: Custom Stack Implementations & MinStack Design
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.stack.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Array-Based Stack: Contiguous memory, excellent L1 cache locality, amortized
 *    O(1) push via dynamic doubling capacity, O(1) pop.
 * 2. Linked List Stack: O(1) worst-case push/pop without resizing spikes, but incurs
 *    heap object allocation and pointer dereference cache misses.
 * 3. MinStack: Tracks the running minimum element in O(1) time alongside regular
 *    stack operations using paired state or differential encoding.
 * ============================================================================
 */
public class CustomStackUsingArrayAndLL {

    /**
     * Resizable Array-backed LIFO Stack.
     */
    public static class ArrayStack {
        private int[] data;
        private int top;

        public ArrayStack(int initialCapacity) {
            this.data = new int[Math.max(initialCapacity, 16)];
            this.top = -1;
        }

        public void push(int val) {
            if (top == data.length - 1) {
                resize(data.length * 2);
            }
            data[++top] = val;
        }

        public int pop() {
            if (isEmpty()) throw new EmptyStackException();
            int val = data[top--];
            if (top > 0 && top == data.length / 4) {
                resize(data.length / 2);
            }
            return val;
        }

        public int peek() {
            if (isEmpty()) throw new EmptyStackException();
            return data[top];
        }

        public boolean isEmpty() {
            return top == -1;
        }

        public int size() {
            return top + 1;
        }

        private void resize(int newCapacity) {
            int[] newArr = new int[newCapacity];
            System.arraycopy(data, 0, newArr, 0, top + 1);
            data = newArr;
        }
    }

    /**
     * Node-backed Singly Linked List Stack.
     */
    public static class LinkedListStack {
        private static class Node {
            int val;
            Node next;
            Node(int val, Node next) {
                this.val = val;
                this.next = next;
            }
        }

        private Node head;
        private int size;

        public void push(int val) {
            head = new Node(val, head);
            size++;
        }

        public int pop() {
            if (isEmpty()) throw new EmptyStackException();
            int val = head.val;
            head = head.next;
            size--;
            return val;
        }

        public int peek() {
            if (isEmpty()) throw new EmptyStackException();
            return head.val;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public int size() {
            return size;
        }
    }

    /**
     * MinStack with O(1) push, pop, top, and getMin.
     * Uses value-delta encoding to achieve O(1) extra space.
     */
    public static class MinStack {
        private final java.util.ArrayDeque<Long> stack = new java.util.ArrayDeque<>();
        private long min;

        public MinStack() {}

        public void push(int val) {
            if (stack.isEmpty()) {
                stack.push(0L);
                min = val;
            } else {
                stack.push((long) val - min);
                if (val < min) {
                    min = val;
                }
            }
        }

        public void pop() {
            if (stack.isEmpty()) return;
            long diff = stack.pop();
            if (diff < 0) {
                min = min - diff; // Restore previous min
            }
        }

        public int top() {
            long diff = stack.peek();
            if (diff >= 0) {
                return (int) (min + diff);
            } else {
                return (int) min;
            }
        }

        public int getMin() {
            return (int) min;
        }
    }
}
