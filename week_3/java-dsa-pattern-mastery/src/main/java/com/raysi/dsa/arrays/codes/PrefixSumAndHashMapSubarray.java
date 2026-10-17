package com.raysi.dsa.arrays.codes;

import java.util.HashMap;
import java.util.Map;

/**
 * Pattern 3: Prefix Sum + HashMap (Subarray Sums & Offset Invariants)
 * Author: raysi
 * Package: com.raysi.dsa.arrays.codes
 */
public class PrefixSumAndHashMapSubarray {

    /**
     * Pattern 3A: Count Subarrays With Sum Equals K (Handles Negative Numbers)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    public int countSubarraySumEqualsK(int[] nums, int k) {
        int count = 0;
        int runningPrefixSum = 0;

        // Map stores: (prefixSumValue -> frequencyOfOccurrences)
        Map<Integer, Integer> prefixFrequency = new HashMap<>();
        prefixFrequency.put(0, 1); // Crucial base case: represents an empty prefix

        for (int num : nums) {
            runningPrefixSum += num;
            int neededComplement = runningPrefixSum - k;

            if (prefixFrequency.containsKey(neededComplement)) {
                count += prefixFrequency.get(neededComplement);
            }

            prefixFrequency.put(runningPrefixSum, prefixFrequency.getOrDefault(runningPrefixSum, 0) + 1);
        }
        return count;
    }

    /**
     * Pattern 3B: Longest Subarray With Sum Equals K (Positives, Negatives, Zeros)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    public int longestSubarraySumEqualsK(int[] nums, int k) {
        int maxLength = 0;
        int runningPrefixSum = 0;

        // Map stores: (prefixSumValue -> earliestIndexFirstSeen)
        Map<Integer, Integer> earliestIndex = new HashMap<>();
        earliestIndex.put(0, -1); // Prefix sum 0 exists before array start

        for (int i = 0; i < nums.length; i++) {
            runningPrefixSum += nums[i];
            int neededComplement = runningPrefixSum - k;

            if (earliestIndex.containsKey(neededComplement)) {
                maxLength = Math.max(maxLength, i - earliestIndex.get(neededComplement));
            }

            // CRITICAL: Only record the FIRST time we encounter runningPrefixSum (maximize length)
            earliestIndex.putIfAbsent(runningPrefixSum, i);
        }
        return maxLength;
    }

    /**
     * Pattern 3C: Continuous Subarray Sum (Multiple of K, Length >= 2)
     * Time Complexity: O(N)
     * Space Complexity: O(min(N, K))
     */
    public boolean checkSubarraySumDivisibleByK(int[] nums, int k) {
        // Map stores: (remainderModuloK -> earliestIndexFirstSeen)
        Map<Integer, Integer> remainderMap = new HashMap<>();
        remainderMap.put(0, -1);

        int runningSum = 0;
        for (int i = 0; i < nums.length; i++) {
            runningSum += nums[i];
            int remainder = runningSum % k;
            
            // Normalize negative remainders to positive range in Java
            if (remainder < 0) remainder += k;

            if (remainderMap.containsKey(remainder)) {
                if (i - remainderMap.get(remainder) >= 2) {
                    return true;
                }
            } else {
                remainderMap.put(remainder, i);
            }
        }
        return false;
    }
}
