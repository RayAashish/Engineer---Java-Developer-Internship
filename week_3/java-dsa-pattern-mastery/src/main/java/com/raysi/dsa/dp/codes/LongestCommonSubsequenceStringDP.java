package com.raysi.dsa.dp.codes;

/**
 * ============================================================================
 * PATTERN: 2D String Dynamic Programming (LCS & Edit Distance)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.dp.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Longest Common Subsequence (LCS):
 *    - If text1[i-1] == text2[j-1]: characters match -> dp[i][j] = 1 + dp[i-1][j-1].
 *    - If mismatch: skip from text1 or text2 -> dp[i][j] = max(dp[i-1][j], dp[i][j-1]).
 *    - Space Optimization: To compress from 2D to 1D, notice dp[i-1][j-1] is the DIAGONAL
 *      value. Save `prevDiagonal` in a temporary variable before overwriting dp[j]!
 * 
 * 2. Edit Distance (Levenshtein Distance):
 *    - If s1[i-1] == s2[j-1]: dp[i][j] = dp[i-1][j-1] (no operation needed).
 *    - If mismatch: 1 + min(Insert: dp[i][j-1], Delete: dp[i-1][j], Replace: dp[i-1][j-1]).
 * ============================================================================
 */
public class LongestCommonSubsequenceStringDP {

    /**
     * Longest Common Subsequence (LC 1143) in O(N) space.
     * Time Complexity : O(M * N)
     * Space Complexity: O(N) where N = text2.length()
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[] dp = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            int prevDiagonal = 0; // Represents dp[i-1][j-1]
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // Save dp[i-1][j] before it gets overwritten
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = 1 + prevDiagonal;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                prevDiagonal = temp; // Diagonal for next column j+1
            }
        }

        return dp[n];
    }

    /**
     * Edit Distance (LC 72).
     * Time Complexity : O(M * N)
     * Space Complexity: O(N)
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[] dp = new int[n + 1];

        for (int j = 0; j <= n; j++) dp[j] = j;

        for (int i = 1; i <= m; i++) {
            int prevDiagonal = dp[0];
            dp[0] = i; // base case: converting word1 prefix to empty word2

            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prevDiagonal;
                } else {
                    dp[j] = 1 + Math.min(prevDiagonal, Math.min(dp[j], dp[j - 1]));
                }
                prevDiagonal = temp;
            }
        }

        return dp[n];
    }

    /**
     * Distinct Subsequences (LC 115) in O(N) space.
     * Time Complexity : O(M * N)
     * Space Complexity: O(N) running backward
     */
    public int numDistinct(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[] dp = new int[n + 1];
        dp[0] = 1; // Empty target can always be formed once

        for (int i = 1; i <= m; i++) {
            // BACKWARD LOOP to avoid overwriting states from same row
            for (int j = n; j >= 1; j--) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] += dp[j - 1];
                }
            }
        }

        return dp[n];
    }
}
