package com.raysi.dsa.arrays.codes;

import java.util.HashMap;
import java.util.Map;

/**
 * Pattern 2: Sliding Window (Fixed & Variable Sized)
 * Author: raysi
 * Package: com.raysi.dsa.arrays.codes
 */
public class SlidingWindowFixedAndVariable {

    /**
     * Pattern 2A: Fixed-Size Sliding Window
     * Example: Maximum sum subarray of exact size K
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public int maxSumSubarrayFixedK(int[] nums, int k) {
        if (nums == null || nums.length < k || k <= 0) return 0;

        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }

        int maxSum = windowSum;
        for (int right = k; right < nums.length; right++) {
            windowSum += nums[right] - nums[right - k]; // Slide window: include incoming, drop outgoing
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    /**
     * Pattern 2B: Variable-Size Window (Longest Subarray with Sum <= Limit)
     * Time Complexity: O(N) amortized (each element added/removed at most once)
     * Space Complexity: O(1)
     */
    public int longestSubarraySumAtMost(int[] nums, int limit) {
        int left = 0;
        int currentSum = 0;
        int maxLength = 0;

        for (int right = 0; right < nums.length; right++) {
            currentSum += nums[right]; // 1. Expand right boundary

            // 2. Shrink left boundary until condition invariant is restored
            while (currentSum > limit && left <= right) {
                currentSum -= nums[left];
                left++;
            }

            // 3. Capture valid window size
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }

    /**
     * Pattern 2C: Variable Window with Character/Element State Tracking
     * Example: Longest substring with at most K distinct elements
     * Time Complexity: O(N)
     * Space Complexity: O(K)
     */
    public int longestWithAtMostKDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k == 0) return 0;

        Map<Character, Integer> freqMap = new HashMap<>();
        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char rChar = s.charAt(right);
            freqMap.put(rChar, freqMap.getOrDefault(rChar, 0) + 1);

            while (freqMap.size() > k) {
                char lChar = s.charAt(left);
                freqMap.put(lChar, freqMap.get(lChar) - 1);
                if (freqMap.get(lChar) == 0) {
                    freqMap.remove(lChar);
                }
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}
