package com.raysi.dsa.stackandqueue.queue.codes;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * PATTERN: Circular Queue Design & Queue Using Stacks
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.queue.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Circular Queue: Fixes the memory exhaustion problem of standard linear arrays
 *    by wrapping pointers around using modulo arithmetic:
 *      tail = (tail + 1) % capacity
 *      head = (head + 1) % capacity
 * 2. Queue Using Two Stacks:
 *    - `inStack` receives all incoming elements via enqueue().
 *    - `outStack` dispenses elements via dequeue()/peek().
 *    - When `outStack` is empty, dump ALL elements from `inStack` to `outStack`.
 *      This inverts LIFO order into FIFO order.
 *    - Amortized complexity is O(1) because each element moves into outStack once.
 * ============================================================================
 */
public class CircularQueueImplementation {

    /**
     * Circular Queue implementation with fixed array.
     */
    public static class MyCircularQueue {
        private final int[] data;
        private int head;
        private int tail;
        private int size;
        private final int capacity;

        public MyCircularQueue(int k) {
            this.capacity = k;
            this.data = new int[k];
            this.head = 0;
            this.tail = -1;
            this.size = 0;
        }

        public boolean enQueue(int value) {
            if (isFull()) return false;
            tail = (tail + 1) % capacity;
            data[tail] = value;
            size++;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) return false;
            head = (head + 1) % capacity;
            size--;
            return true;
        }

        public int Front() {
            return isEmpty() ? -1 : data[head];
        }

        public int Rear() {
            return isEmpty() ? -1 : data[tail];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }
    }

    /**
     * FIFO Queue implemented using two LIFO Stacks.
     */
    public static class MyQueue {
        private final Deque<Integer> inStack = new ArrayDeque<>();
        private final Deque<Integer> outStack = new ArrayDeque<>();

        public MyQueue() {}

        public void push(int x) {
            inStack.push(x);
        }

        public int pop() {
            shiftStacks();
            return outStack.pop();
        }

        public int peek() {
            shiftStacks();
            return outStack.peek();
        }

        public boolean empty() {
            return inStack.isEmpty() && outStack.isEmpty();
        }

        private void shiftStacks() {
            if (outStack.isEmpty()) {
                while (!inStack.isEmpty()) {
                    outStack.push(inStack.pop());
                }
            }
        }
    }
}
