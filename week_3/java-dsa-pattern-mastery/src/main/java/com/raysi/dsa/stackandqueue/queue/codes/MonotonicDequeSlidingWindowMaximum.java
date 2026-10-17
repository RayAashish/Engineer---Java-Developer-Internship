package com.raysi.dsa.stackandqueue.queue.codes;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * PATTERN: Monotonic Double-Ended Queue (Deque) Mechanics
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.queue.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Monotonic Deque maintains elements in strictly decreasing order of values.
 * 2. Window Sliding Maximum:
 *    - FRONT of deque always contains the MAXIMUM element of the current window.
 *    - When window shifts right, evict front if its index fell outside window:
 *        `if (deque.peekFirst() <= i - k) deque.pollFirst();`
 *    - Before inserting incoming element nums[i], pop all smaller elements from BACK:
 *        `while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) deque.pollLast();`
 *    - Amortized O(N) because each index is added and removed at most once!
 * ============================================================================
 */
public class MonotonicDequeSlidingWindowMaximum {

    /**
     * Sliding Window Maximum.
     * Time Complexity : O(N)
     * Space Complexity: O(K) for deque
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || k <= 0) return new int[0];

        int n = nums.length;
        int[] result = new int[n - k + 1];
        int resIdx = 0;

        // Stores INDICES of candidate maximums in decreasing order of nums[i]
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            // 1. Remove elements whose indices are outside current window [i - k + 1, i]
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            // 2. Maintain monotonic decreasing order: pop smaller elements from rear
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }

            // 3. Add current index
            deque.offerLast(i);

            // 4. Record maximum once the first full window of size k is formed
            if (i >= k - 1) {
                result[resIdx++] = nums[deque.peekFirst()];
            }
        }

        return result;
    }

    /**
     * Shortest Subarray with Sum at Least K.
     * Uses Prefix Sums + Monotonic Deque.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int minLen = n + 1;
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i <= n; i++) {
            // Check if window condition prefix[i] - prefix[opt] >= k is satisfied
            while (!deque.isEmpty() && prefix[i] - prefix[deque.peekFirst()] >= k) {
                minLen = Math.min(minLen, i - deque.pollFirst());
            }

            // Maintain monotonic increasing prefix sums
            while (!deque.isEmpty() && prefix[i] <= prefix[deque.peekLast()]) {
                deque.pollLast();
            }

            deque.offerLast(i);
        }

        return (minLen <= n) ? minLen : -1;
    }
}
