package com.raysi.dsa.arrays.codes;

/**
 * Pattern 4: Single-Pass Invariants & State Tracking
 * Author: raysi
 * Package: com.raysi.dsa.arrays.codes
 */
public class KadanesAndDutchNationalFlag {

    /**
     * Pattern 4A: Kadane's Algorithm for Maximum Subarray Sum
     * Handles all negative arrays properly.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int currentMax = nums[0];
        int globalMax = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // Local decision: extend running sum or start anew at nums[i]
            currentMax = Math.max(nums[i], currentMax + nums[i]);
            globalMax = Math.max(globalMax, currentMax);
        }
        return globalMax;
    }

    /**
     * Pattern 4B: Dutch National Flag Algorithm (3-Way In-Place Partitioning)
     * Groups 0s (Red), 1s (White), 2s (Blue) in a single pass.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public void sortColors(int[] nums) {
        if (nums == null || nums.length <= 1) return;

        int low = 0;
        int mid = 0;
        int high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else { // nums[mid] == 2
                swap(nums, mid, high);
                high--;
                // Note: Do NOT increment mid here! The swapped element from high must be evaluated.
            }
        }
    }

    /**
     * Pattern 4C: Boyer-Moore Majority Vote (> N / 2 frequency)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public int majorityElement(int[] nums) {
        int candidate = nums[0];
        int count = 0;

        for (int num : nums) {
            if (count == 0) {
                candidate = num;
            }
            count += (num == candidate) ? 1 : -1;
        }
        return candidate;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
