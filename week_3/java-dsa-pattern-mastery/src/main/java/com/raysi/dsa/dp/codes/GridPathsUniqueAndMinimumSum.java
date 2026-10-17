package com.raysi.dsa.dp.codes;

import java.util.Arrays;

/**
 * ============================================================================
 * PATTERN: 2D Grid DP & Row-Compression Optimization
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.dp.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. 2D State Space Reduction:
 *    When a cell dp[r][c] only depends on the cell ABOVE it (dp[r-1][c]) and
 *    the cell to the LEFT of it (dp[r][c-1]), we only need a single 1D array:
 *      dp[c] = dp[c] (value from above) + dp[c-1] (new value from left)
 *    Reduces space from O(R * C) down to O(C)!
 * 2. Maximal Square:
 *    dp[r][c] represents the side length of the maximum all-1 square with
 *    bottom-right corner at (r, c):
 *      dp[r][c] = 1 + min(dp[r-1][c], dp[r][c-1], dp[r-1][c-1])
 * ============================================================================
 */
public class GridPathsUniqueAndMinimumSum {

    /**
     * Unique Paths I (LC 62) in O(C) space.
     * Time Complexity : O(R * C)
     * Space Complexity: O(C)
     */
    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // Row 0 base case: 1 path to reach any cell in row 0

        for (int r = 1; r < m; r++) {
            for (int c = 1; c < n; c++) {
                dp[c] += dp[c - 1]; // dp[c] is from top; dp[c-1] is from left
            }
        }

        return dp[n - 1];
    }

    /**
     * Unique Paths II (LC 63) with obstacles in O(C) space.
     * Time Complexity : O(R * C)
     * Space Complexity: O(C)
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int cols = obstacleGrid[0].length;
        int[] dp = new int[cols];
        dp[0] = (obstacleGrid[0][0] == 0) ? 1 : 0;

        for (int[] row : obstacleGrid) {
            for (int c = 0; c < cols; c++) {
                if (row[c] == 1) {
                    dp[c] = 0; // Obstacle has 0 paths
                } else if (c > 0) {
                    dp[c] += dp[c - 1];
                }
            }
        }

        return dp[cols - 1];
    }

    /**
     * Minimum Path Sum (LC 64) in O(C) space.
     * Time Complexity : O(R * C)
     * Space Complexity: O(C)
     */
    public int minPathSum(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] dp = new int[cols];

        dp[0] = grid[0][0];
        for (int c = 1; c < cols; c++) {
            dp[c] = dp[c - 1] + grid[0][c];
        }

        for (int r = 1; r < rows; r++) {
            dp[0] += grid[r][0]; // First col can only come from above
            for (int c = 1; c < cols; c++) {
                dp[c] = Math.min(dp[c], dp[c - 1]) + grid[r][c];
            }
        }

        return dp[cols - 1];
    }

    /**
     * Maximal Square (LC 221).
     * Time Complexity : O(R * C)
     * Space Complexity: O(R * C) (or O(C))
     */
    public int maximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0) return 0;

        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] dp = new int[rows + 1][cols + 1];
        int maxSide = 0;

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                if (matrix[r - 1][c - 1] == '1') {
                    dp[r][c] = 1 + Math.min(dp[r - 1][c], Math.min(dp[r][c - 1], dp[r - 1][c - 1]));
                    maxSide = Math.max(maxSide, dp[r][c]);
                }
            }
        }

        return maxSide * maxSide;
    }
}
