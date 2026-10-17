package com.raysi.dsa.matrix.codes;

/**
 * ============================================================================
 * PATTERN: In-Place Geometric Matrix Transformations
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.matrix.codes
 * ============================================================================
 * 
 * CORE FORMULAS:
 * 1. Transpose: Swap matrix[i][j] <-> matrix[j][i] for all 0 <= i < j < N.
 * 2. 90-deg Clockwise        = Transpose + Horizontal Reflection (Reverse each row)
 * 3. 90-deg Counter-Clockwise= Transpose + Vertical Reflection (Reverse each col)
 * 4. 180-deg Rotation        = Vertical Reflection + Horizontal Reflection
 * ============================================================================
 */
public class RotateAndTransposeInPlace {

    /**
     * Rotates an N x N matrix 90 degrees clockwise in-place.
     * Time Complexity : O(N^2)
     * Space Complexity: O(1) in-place
     */
    public void rotateClockwise(int[][] matrix) {
        if (matrix == null || matrix.length <= 1) return;
        int n = matrix.length;

        // Step 1: Transpose matrix along the main diagonal
        transpose(matrix, n);

        // Step 2: Reverse each row horizontally
        for (int i = 0; i < n; i++) {
            reverseRow(matrix[i], 0, n - 1);
        }
    }

    /**
     * Rotates an N x N matrix 90 degrees counter-clockwise in-place.
     * Time Complexity : O(N^2)
     * Space Complexity: O(1) in-place
     */
    public void rotateCounterClockwise(int[][] matrix) {
        if (matrix == null || matrix.length <= 1) return;
        int n = matrix.length;

        // Step 1: Transpose
        transpose(matrix, n);

        // Step 2: Reverse each column vertically
        for (int col = 0; col < n; col++) {
            int top = 0, bottom = n - 1;
            while (top < bottom) {
                int temp = matrix[top][col];
                matrix[top][col] = matrix[bottom][col];
                matrix[bottom][col] = temp;
                top++;
                bottom--;
            }
        }
    }

    /**
     * In-place transpose of an N x N matrix.
     * Only iterates the upper triangle (j > i) to avoid double-swapping back.
     */
    public void transpose(int[][] matrix, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

    private void reverseRow(int[] row, int left, int right) {
        while (left < right) {
            int temp = row[left];
            row[left] = row[right];
            row[right] = temp;
            left++;
            right--;
        }
    }
}
