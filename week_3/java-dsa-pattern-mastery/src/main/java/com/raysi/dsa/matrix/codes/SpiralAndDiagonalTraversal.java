package com.raysi.dsa.matrix.codes;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: 2D Matrix Spiral & Diagonal Traversal Mechanics
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.matrix.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Spiral Traversal: Bound shrinkage. Maintain 4 boundary pointers:
 *    top, bottom, left, right. Move right along top, down along right,
 *    left along bottom, up along left. Crucial guard: check (top <= bottom)
 *    and (left <= right) before bottom and left sweeps to prevent duplicate reads.
 * 
 * 2. Diagonal Traversal: In an R x C matrix, all cells (r, c) on the same
 *    anti-diagonal share the property: (r + c = k) where 0 <= k < R + C - 1.
 *    Cells on the same main diagonal share: (r - c = k).
 * ============================================================================
 */
public class SpiralAndDiagonalTraversal {

    /**
     * Traverses an R x C matrix in spiral clockwise order.
     * Time Complexity : O(R * C) - every cell visited exactly once.
     * Space Complexity: O(1) auxiliary (excluding return list).
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            // 1. Traverse Right across the top boundary
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
            }
            top++;

            // 2. Traverse Down across the right boundary
            for (int row = top; row <= bottom; row++) {
                result.add(matrix[row][right]);
            }
            right--;

            // 3. Traverse Left across bottom boundary (guard against single-row overlap)
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    result.add(matrix[bottom][col]);
                }
                bottom--;
            }

            // 4. Traverse Up across left boundary (guard against single-column overlap)
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    result.add(matrix[row][left]);
                }
                left++;
            }
        }

        return result;
    }

    /**
     * Traverses an R x C matrix diagonally in a zigzag (bouncing) pattern.
     * Up-right direction -> Down-left direction.
     * Time Complexity : O(R * C)
     * Space Complexity: O(1) auxiliary
     */
    public int[] findDiagonalOrder(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0];
        }

        int r = mat.length;
        int c = mat[0].length;
        int[] result = new int[r * c];
        int row = 0, col = 0;

        for (int i = 0; i < r * c; i++) {
            result[i] = mat[row][col];

            // If sum of coordinates is even, direction is UP-RIGHT (row--, col++)
            if ((row + col) % 2 == 0) {
                if (col == c - 1) {
                    row++; // Hit right wall -> move down
                } else if (row == 0) {
                    col++; // Hit ceiling -> move right
                } else {
                    row--;
                    col++;
                }
            } else {
                // Direction is DOWN-LEFT (row++, col--)
                if (row == r - 1) {
                    col++; // Hit bottom floor -> move right
                } else if (col == 0) {
                    row++; // Hit left wall -> move down
                } else {
                    row++;
                    col--;
                }
            }
        }

        return result;
    }
}
