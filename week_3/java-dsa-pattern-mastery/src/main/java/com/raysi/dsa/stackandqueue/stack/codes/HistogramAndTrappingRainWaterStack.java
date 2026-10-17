package com.raysi.dsa.stackandqueue.stack.codes;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * PATTERN: Monotonic Stack for Histogram Geometry & Water Trapping
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.stack.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Largest Rectangle in Histogram:
 *    For each bar of height H, what is the widest rectangle with height H?
 *    It extends left until the first bar strictly shorter than H (Previous Less Element),
 *    and right until the first bar strictly shorter than H (Next Less Element).
 *    A monotonic INCREASING stack computes both boundaries in single-pass O(N).
 * 
 * 2. Trapping Rain Water:
 *    A monotonic DECREASING stack tracks dips in elevation.
 *    When a taller right boundary appears:
 *      water = (min(leftHeight, rightHeight) - bottomHeight) * width.
 * ============================================================================
 */
public class HistogramAndTrappingRainWaterStack {

    /**
     * Largest Rectangle in Histogram.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) return 0;

        int n = heights.length;
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;

        // Iterate through all bars plus one virtual sentinel bar of height 0 at index n
        for (int i = 0; i <= n; i++) {
            int currHeight = (i == n) ? 0 : heights[i];

            while (!stack.isEmpty() && currHeight < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                // If stack is empty, width extends all the way to index 0: width = i
                // Otherwise, width is bounded by element below on stack: width = i - stack.peek() - 1
                int width = stack.isEmpty() ? i : (i - stack.peek() - 1);
                maxArea = Math.max(maxArea, height * width);
            }

            stack.push(i);
        }

        return maxArea;
    }

    /**
     * Maximal Rectangle in binary matrix.
     * Reduces 2D matrix into cumulative 1D histogram per row.
     * Time Complexity : O(R * C)
     * Space Complexity: O(C)
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;

        int cols = matrix[0].length;
        int[] heights = new int[cols];
        int maxArea = 0;

        for (char[] row : matrix) {
            for (int c = 0; c < cols; c++) {
                if (row[c] == '1') {
                    heights[c]++;
                } else {
                    heights[c] = 0; // Reset height to 0 on obstacle
                }
            }
            maxArea = Math.max(maxArea, largestRectangleArea(heights));
        }

        return maxArea;
    }

    /**
     * Trapping Rain Water via Monotonic Decreasing Stack.
     * Computes water volume layer by horizontal layer.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public int trap(int[] height) {
        if (height == null || height.length < 3) return 0;

        Deque<Integer> stack = new ArrayDeque<>();
        int totalWater = 0;

        for (int right = 0; right < height.length; right++) {
            while (!stack.isEmpty() && height[right] > height[stack.peek()]) {
                int bottomIdx = stack.pop();
                if (stack.isEmpty()) break; // No left boundary to trap water

                int left = stack.peek();
                int boundedHeight = Math.min(height[left], height[right]) - height[bottomIdx];
                int width = right - left - 1;

                totalWater += (boundedHeight * width);
            }
            stack.push(right);
        }

        return totalWater;
    }
}
