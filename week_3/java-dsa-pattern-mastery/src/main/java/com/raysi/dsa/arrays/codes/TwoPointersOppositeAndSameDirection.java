package com.raysi.dsa.arrays.codes;

/**
 * Pattern 1: Two Pointers (Opposite Direction & Same Direction)
 * Author: raysi
 * Package: com.raysi.dsa.arrays.codes
 */
public class TwoPointersOppositeAndSameDirection {

    /**
     * Pattern 1A: Opposite-Direction Two Pointers (Converging from boundaries)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public int[] twoSumSorted(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1}; // 1-indexed
            } else if (sum < target) {
                left++; // Monotonic increase: need larger sum
            } else {
                right--; // Monotonic decrease: need smaller sum
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * Pattern 1B: Same-Direction Two Pointers (Slow & Fast Runner / Deduplication)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public int removeDuplicatesSorted(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int slow = 0;
        for (int fast = 1; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow]) {
                slow++;
                nums[slow] = nums[fast]; // Overwrite next unique slot
            }
        }
        return slow + 1; // Length of unique subarray
    }

    /**
     * Pattern 1C: Container With Most Water
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int width = right - left;
            int h = Math.min(height[left], height[right]);
            maxWater = Math.max(maxWater, width * h);

            // Invariant: The shorter boundary bottlenecks capacity; advance it inwards
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxWater;
    }
}
