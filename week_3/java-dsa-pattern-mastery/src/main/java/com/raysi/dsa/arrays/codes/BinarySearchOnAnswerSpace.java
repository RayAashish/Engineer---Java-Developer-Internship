package com.raysi.dsa.arrays.codes;

/**
 * Pattern 5: Binary Search on Monotonic Space & Answer Space
 * Author: raysi
 * Package: com.raysi.dsa.arrays.codes
 */
public class BinarySearchOnAnswerSpace {

    /**
     * Pattern 5A: Classic Binary Search on Answer Space
     * Example: Capacity To Ship Packages Within D Days
     * Time Complexity: O(N * log(sum(weights)))
     * Space Complexity: O(1)
     */
    public int shipWithinDays(int[] weights, int days) {
        int low = 0;
        int high = 0;

        for (int w : weights) {
            low = Math.max(low, w); // Must be at least the weight of the heaviest package
            high += w;              // Maximum possible capacity carries all in 1 day
        }

        int optimalCapacity = high;
        while (low <= high) {
            int mid = low + (high - low) / 2; // Guard against integer overflow

            if (canShipWithinDays(weights, days, mid)) {
                optimalCapacity = mid;      // Feasible: record answer
                high = mid - 1;            // Try to find a smaller feasible capacity
            } else {
                low = mid + 1;             // Infeasible: must increase capacity
            }
        }
        return optimalCapacity;
    }

    private boolean canShipWithinDays(int[] weights, int maxDays, int capacity) {
        int daysUsed = 1;
        int currentDayLoad = 0;

        for (int w : weights) {
            if (currentDayLoad + w > capacity) {
                daysUsed++;
                currentDayLoad = 0;
            }
            currentDayLoad += w;
        }
        return daysUsed <= maxDays;
    }

    /**
     * Pattern 5B: Search in Rotated Sorted Array (Half-Sorted Invariant)
     * Time Complexity: O(log N)
     * Space Complexity: O(1)
     */
    public int searchRotatedSorted(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;

        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) return mid;

            // Invariant: At least one half MUST be strictly sorted
            if (nums[low] <= nums[mid]) { // Left half is sorted
                if (target >= nums[low] && target < nums[mid]) {
                    high = mid - 1; // Target lies within sorted left half
                } else {
                    low = mid + 1;  // Target lies in unsorted right half
                }
            } else { // Right half is sorted
                if (target > nums[mid] && target <= nums[high]) {
                    low = mid + 1;  // Target lies within sorted right half
                } else {
                    high = mid - 1; // Target lies in unsorted left half
                }
            }
        }
        return -1;
    }

    /**
     * Pattern 5C: Find Minimum in Rotated Sorted Array (Inflection Point Convergence)
     * Time Complexity: O(log N)
     * Space Complexity: O(1)
     */
    public int findMinRotatedSorted(int[] nums) {
        int low = 0;
        int high = nums.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] > nums[high]) {
                // Minimum must lie strictly to the right of mid
                low = mid + 1;
            } else {
                // nums[mid] <= nums[high]: Minimum is at mid or to its left
                high = mid;
            }
        }
        return nums[low];
    }
}
