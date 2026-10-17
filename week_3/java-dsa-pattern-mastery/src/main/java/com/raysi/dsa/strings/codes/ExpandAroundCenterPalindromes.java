package com.raysi.dsa.strings.codes;

/**
 * ============================================================================
 * PATTERN: Expand Around Center for Palindromic Sequences
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.strings.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. A palindrome mirrors around its center.
 * 2. In a string of length N, there are (2N - 1) possible centers:
 *    - N odd-length centers: centered on character s[i]
 *    - (N - 1) even-length centers: centered between s[i] and s[i + 1]
 * 3. Expanding outwards from these centers takes O(N^2) total time but uses
 *    STRICTLY O(1) auxiliary space (unlike 2D DP which requires O(N^2) memory).
 * ============================================================================
 */
public class ExpandAroundCenterPalindromes {

    /**
     * Finds the longest palindromic substring in s.
     * Time Complexity : O(N^2)
     * Space Complexity: O(1) auxiliary
     */
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";

        int start = 0, end = 0;

        for (int i = 0; i < s.length(); i++) {
            // Odd length palindrome centered at s[i]
            int len1 = expandFromCenter(s, i, i);
            // Even length palindrome centered between s[i] and s[i + 1]
            int len2 = expandFromCenter(s, i, i + 1);

            int maxLen = Math.max(len1, len2);
            if (maxLen > end - start) {
                // For odd: (maxLen - 1) / 2 is distance to left
                // For even: maxLen / 2 is distance to right
                start = i - (maxLen - 1) / 2;
                end = i + maxLen / 2;
            }
        }

        return s.substring(start, end + 1);
    }

    /**
     * Counts the total number of palindromic substrings in s.
     * Time Complexity : O(N^2)
     * Space Complexity: O(1) auxiliary
     */
    public int countSubstrings(String s) {
        if (s == null || s.length() == 0) return 0;
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            count += countPalindromes(s, i, i);     // Odd centers
            count += countPalindromes(s, i, i + 1); // Even centers
        }

        return count;
    }

    /**
     * Valid Palindrome II: Can remove at most one character to make s palindromic.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public boolean validPalindrome(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                // Must skip either character at left OR character at right
                return isPalindromeRange(s, left + 1, right) || isPalindromeRange(s, left, right - 1);
            }
            left++;
            right--;
        }

        return true;
    }

    private int expandFromCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // Length of valid palindrome is (right - 1) - (left + 1) + 1 = right - left - 1
        return right - left - 1;
    }

    private int countPalindromes(String s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            count++;
            left--;
            right++;
        }
        return count;
    }

    private boolean isPalindromeRange(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
