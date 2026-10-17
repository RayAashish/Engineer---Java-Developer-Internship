package com.raysi.dsa.dp.codes;

import java.util.Arrays;

/**
 * ============================================================================
 * PATTERN: Longest Increasing Subsequence (LIS) & Binary Search Optimization
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.dp.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. O(N^2) Dynamic Programming:
 *    dp[i] = length of LIS ending at index i.
 *    dp[i] = 1 + max_{j < i && nums[j] < nums[i]}(dp[j]).
 * 
 * 2. O(N log N) Patience Sorting / Binary Search:
 *    Maintain an array `tails` where tails[len] is the SMALLEST tail element of
 *    all increasing subsequences of length (len + 1).
 *    For each num in nums:
 *      - Binary search for num in tails.
 *      - If num is larger than all tails -> append num, extending LIS length by 1.
 *      - Else -> replace the first tail >= num with num (greedily minimizing tail value!).
 * 
 * 3. Russian Doll Envelopes:
 *    Sort envelopes: width ASCENDING, and for equal widths sort height DESCENDING.
 *    Sorting height descending prevents picking multiple envelopes with identical widths!
 *    Now, finding max nested envelopes is simply finding LIS on heights in O(N log N).
 * ============================================================================
 */
public class LongestIncreasingSubsequenceDP {

    /**
     * Longest Increasing Subsequence (LC 300) via Patience Sorting & Binary Search.
     * Time Complexity : O(N log N)
     * Space Complexity: O(N)
     */
    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int[] tails = new int[nums.length];
        int size = 0; // Length of current longest increasing subsequence

        for (int x : nums) {
            int low = 0, high = size;

            // Binary search for first element >= x in tails[0 ... size-1]
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (tails[mid] < x) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            tails[low] = x;
            if (low == size) {
                size++;
            }
        }

        return size;
    }

    /**
     * Russian Doll Envelopes (LC 354).
     * Time Complexity : O(N log N)
     * Space Complexity: O(N)
     */
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) return 0;

        // Sort width ascending; on width tie, sort height descending
        Arrays.sort(envelopes, (a, b) -> {
            if (a[0] == b[0]) return Integer.compare(b[1], a[1]);
            return Integer.compare(a[0], b[0]);
        });

        // Extract heights and run LIS in O(N log N)
        int[] tails = new int[envelopes.length];
        int size = 0;

        for (int[] env : envelopes) {
            int h = env[1];
            int low = 0, high = size;

            while (low < high) {
                int mid = low + (high - low) / 2;
                if (tails[mid] < h) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            tails[low] = h;
            if (low == size) {
                size++;
            }
        }

        return size;
    }

    /**
     * Number of Longest Increasing Subsequences (LC 673).
     * Time Complexity : O(N^2)
     * Space Complexity: O(N)
     */
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        int[] len = new int[n];
        int[] count = new int[n];
        Arrays.fill(len, 1);
        Arrays.fill(count, 1);

        int maxLen = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (len[j] + 1 > len[i]) {
                        len[i] = len[j] + 1;
                        count[i] = count[j]; // Reset count to j's count
                    } else if (len[j] + 1 == len[i]) {
                        count[i] += count[j]; // Add alternative paths
                    }
                }
            }
            maxLen = Math.max(maxLen, len[i]);
        }

        int result = 0;
        for (int i = 0; i < n; i++) {
            if (len[i] == maxLen) {
                result += count[i];
            }
        }

        return result;
    }
}
