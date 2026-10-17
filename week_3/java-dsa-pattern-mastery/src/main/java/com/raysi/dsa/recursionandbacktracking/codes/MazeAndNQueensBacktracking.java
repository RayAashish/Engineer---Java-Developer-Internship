package com.raysi.dsa.recursionandbacktracking.codes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: Grid Backtracking, N-Queens & Constraint Satisfaction
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.recursionandbacktracking.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. N-Queens Constraint Geometry:
 *    - Columns: col (range 0 to N-1)
 *    - Main Diagonals (top-left to bot-right): (row - col) is constant.
 *      Offset by (N - 1) to make non-negative: (row - col + N - 1) (range 0 to 2N-2).
 *    - Anti-Diagonals (top-right to bot-left): (row + col) is constant (range 0 to 2N-2).
 *    - Tracking these three states in boolean arrays achieves O(1) conflict validation!
 * 
 * 2. Word Search In-Place State Masking:
 *    Instead of allocating an R x C boolean visited array, temporarily mask the board cell:
 *      char temp = board[r][c];
 *      board[r][c] = '#'; // Mark visited
 *      boolean found = dfs(...);
 *      board[r][c] = temp; // Unmask (Backtrack)
 *    Zero auxiliary memory allocation!
 * ============================================================================
 */
public class MazeAndNQueensBacktracking {

    /**
     * N-Queens Solver: Returns all distinct board configurations.
     * Time Complexity : O(N!)
     * Space Complexity: O(N) recursion stack + constraint arrays
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');

        boolean[] cols = new boolean[n];
        boolean[] diag1 = new boolean[2 * n]; // row + col
        boolean[] diag2 = new boolean[2 * n]; // row - col + n

        backtrackNQueens(0, n, board, cols, diag1, diag2, result);
        return result;
    }

    private void backtrackNQueens(int row, int n, char[][] board, boolean[] cols, boolean[] diag1, boolean[] diag2, List<List<String>> result) {
        if (row == n) {
            List<String> configuration = new ArrayList<>();
            for (char[] r : board) configuration.add(new String(r));
            result.add(configuration);
            return;
        }

        for (int col = 0; col < n; col++) {
            int d1 = row + col;
            int d2 = row - col + n;

            if (cols[col] || diag1[d1] || diag2[d2]) continue;

            // Place Queen
            board[row][col] = 'Q';
            cols[col] = diag1[d1] = diag2[d2] = true;

            // Recurse to next row
            backtrackNQueens(row + 1, n, board, cols, diag1, diag2, result);

            // Backtrack
            board[row][col] = '.';
            cols[col] = diag1[d1] = diag2[d2] = false;
        }
    }

    /**
     * Word Search in 2D Board via DFS Backtracking.
     * Time Complexity : O(R * C * 3^L) where L is word length
     * Space Complexity: O(L) recursion stack
     */
    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == word.charAt(0)) {
                    if (dfsWordSearch(board, word, 0, r, c, rows, cols)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean dfsWordSearch(char[][] board, String word, int idx, int r, int c, int rows, int cols) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= rows || c < 0 || c >= cols || board[r][c] != word.charAt(idx)) {
            return false;
        }

        // Mask cell
        char temp = board[r][c];
        board[r][c] = '#';

        // Explore 4 directions
        boolean found = dfsWordSearch(board, word, idx + 1, r + 1, c, rows, cols)
                     || dfsWordSearch(board, word, idx + 1, r - 1, c, rows, cols)
                     || dfsWordSearch(board, word, idx + 1, r, c + 1, rows, cols)
                     || dfsWordSearch(board, word, idx + 1, r, c - 1, rows, cols);

        // Unmask cell (Backtrack)
        board[r][c] = temp;

        return found;
    }

    /**
     * Sudoku Solver.
     * Fills the 9x9 board in-place.
     */
    public void solveSudoku(char[][] board) {
        solve(board);
    }

    private boolean solve(char[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == '.') {
                    for (char ch = '1'; ch <= '9'; ch++) {
                        if (isValidSudokuPlacement(board, r, c, ch)) {
                            board[r][c] = ch;
                            if (solve(board)) return true;
                            board[r][c] = '.'; // Backtrack
                        }
                    }
                    return false; // No digit 1-9 works here -> prune
                }
            }
        }
        return true; // All cells filled
    }

    private boolean isValidSudokuPlacement(char[][] board, int row, int col, char ch) {
        int boxRowStart = 3 * (row / 3);
        int boxColStart = 3 * (col / 3);

        for (int i = 0; i < 9; i++) {
            if (board[row][i] == ch) return false;
            if (board[i][col] == ch) return false;
            if (board[boxRowStart + i / 3][boxColStart + i % 3] == ch) return false;
        }
        return true;
    }
}
