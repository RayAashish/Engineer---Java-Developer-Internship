package com.raysi.dsa.stackandqueue.stack.codes;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * PATTERN: Monotonic Stack (Next Greater / Smaller Element)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.stack.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. A Monotonic Stack preserves elements in strictly increasing or decreasing order.
 * 2. Next Greater Element (NGE): Monotonic DECREASING stack.
 *    As we iterate, if incoming element > stack.peek(), the incoming element is the
 *    NGE for stack.peek()! Pop all elements smaller than the incoming element.
 * 3. Amortized O(N) Complexity: Each element is pushed onto the stack exactly once
 *    and popped at most once -> exactly 2N stack operations across the whole array.
 * 4. Circular Arrays: Simulate by iterating 2N times and mapping index: (i % N).
 * ============================================================================
 */
public class MonotonicStackNextGreaterElement {

    /**
     * Next Greater Element in circular array nums.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);

        // Monotonic decreasing stack storing array INDICES
        Deque<Integer> stack = new ArrayDeque<>();

        // Loop 2N times to handle circular wrap-around
        for (int i = 0; i < 2 * n; i++) {
            int num = nums[i % n];

            while (!stack.isEmpty() && nums[stack.peek()] < num) {
                int poppedIndex = stack.pop();
                result[poppedIndex] = num;
            }

            // Only push indices during the first full pass
            if (i < n) {
                stack.push(i);
            }
        }

        return result;
    }

    /**
     * Daily Temperatures: Days to wait until a warmer temperature.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int currDay = 0; currDay < n; currDay++) {
            int currTemp = temperatures[currDay];

            while (!stack.isEmpty() && temperatures[stack.peek()] < currTemp) {
                int prevDay = stack.pop();
                answer[prevDay] = currDay - prevDay;
            }

            stack.push(currDay);
        }

        return answer;
    }

    /**
     * Next Greater Element I (subset query).
     * Time Complexity : O(N + M)
     * Space Complexity: O(M)
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> ngeMap = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int num : nums2) {
            while (!stack.isEmpty() && stack.peek() < num) {
                ngeMap.put(stack.pop(), num);
            }
            stack.push(num);
        }

        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = ngeMap.getOrDefault(nums1[i], -1);
        }

        return result;
    }

    /**
     * Online Stock Span: returns number of consecutive days price was <= today's price.
     */
    public static class StockSpanner {
        // Pair: [price, span]
        private final Deque<int[]> stack = new ArrayDeque<>();

        public StockSpanner() {}

        public int next(int price) {
            int span = 1;
            while (!stack.isEmpty() && stack.peek()[0] <= price) {
                span += stack.pop()[1];
            }
            stack.push(new int[]{price, span});
            return span;
        }
    }
}
