package com.raysi.dsa.matrix.codes;

/**
 * ============================================================================
 * PATTERN: Staircase Search & Virtual 1D Binary Search in Sorted 2D Matrices
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.matrix.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Staircase Search (Matrix II):
 *    When rows are sorted left-to-right AND columns are sorted top-to-bottom,
 *    standing at Top-Right cell (0, C-1) provides a binary decision tree:
 *    - Leftward values are SMALLER (col--)
 *    - Downward values are LARGER (row++)
 *    Prunes an entire row or column in O(1) step -> Total O(R + C).
 * 
 * 2. Virtual 1D Binary Search (Matrix I):
 *    When matrix is strictly sorted end-to-end, treat as 1D array of size R*C.
 *    Mapping formulas:
 *      row = mid / cols
 *      col = mid % cols
 *    Binary search in O(log(R * C)).
 * ============================================================================
 */
public class StaircaseSearchSortedMatrix {

    /**
     * Search in 2D Matrix II: Row-wise and column-wise sorted.
     * Time Complexity : O(R + C)
     * Space Complexity: O(1)
     */
    public boolean searchMatrixII(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Start at Top-Right corner
        int r = 0;
        int c = cols - 1;

        while (r < rows && c >= 0) {
            int val = matrix[r][c];
            if (val == target) {
                return true;
            } else if (val > target) {
                // All elements below matrix[r][c] are even larger -> eliminate column c
                c--;
            } else {
                // All elements to the left of matrix[r][c] are smaller -> eliminate row r
                r++;
            }
        }

        return false;
    }

    /**
     * Search in 2D Matrix I: Strictly sorted end-to-end.
     * Time Complexity : O(log(R * C))
     * Space Complexity: O(1)
     */
    public boolean searchMatrixI(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int low = 0;
        int high = rows * cols - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int r = mid / cols;
            int c = mid % cols;

            if (matrix[r][c] == target) {
                return true;
            } else if (matrix[r][c] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return false;
    }

    /**
     * Finds the K-th smallest element in a row- and col-sorted matrix.
     * Binary Search on Value Space [min, max] + Staircase Counting.
     * Time Complexity : O((R + C) * log(max - min))
     * Space Complexity: O(1)
     */
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int low = matrix[0][0];
        int high = matrix[n - 1][n - 1];
        int ans = low;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (countLessOrEqual(matrix, mid, n) >= k) {
                ans = mid;
                high = mid - 1; // Try finding smaller candidate
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    private int countLessOrEqual(int[][] matrix, int target, int n) {
        int count = 0;
        int r = n - 1; // Start bottom-left
        int c = 0;

        while (r >= 0 && c < n) {
            if (matrix[r][c] <= target) {
                // All elements in this column from row 0 to r are <= target
                count += (r + 1);
                c++;
            } else {
                r--;
            }
        }
        return count;
    }
}
