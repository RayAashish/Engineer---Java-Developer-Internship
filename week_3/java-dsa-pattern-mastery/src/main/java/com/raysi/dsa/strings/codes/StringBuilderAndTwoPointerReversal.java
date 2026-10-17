package com.raysi.dsa.strings.codes;

/**
 * ============================================================================
 * PATTERN: In-Place Char Array Two-Pointer Transformations & StringBuilder
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.strings.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. String Immutability: In Java, String is immutable. Direct modifications
 *    require converting to `char[]` or using `StringBuilder`.
 * 2. Reverse Words:
 *    Step 1: Clean excess whitespace (leading, trailing, multiple internal).
 *    Step 2: Reverse the entire cleaned sequence.
 *    Step 3: Reverse each individual word within the sequence.
 * 3. In-Place String Compression: Two pointers `read` and `write` to encode
 *    runs into character + count in O(1) extra space.
 * ============================================================================
 */
public class StringBuilderAndTwoPointerReversal {

    /**
     * Reverses the words in a string, stripping multiple and leading/trailing spaces.
     * Time Complexity : O(N)
     * Space Complexity: O(N) for char array
     */
    public String reverseWords(String s) {
        if (s == null) return null;
        char[] a = s.toCharArray();
        int n = a.length;

        // Step 1: Clean spaces in-place using two pointers
        int len = cleanSpaces(a, n);

        // Step 2: Reverse entire string
        reverse(a, 0, len - 1);

        // Step 3: Reverse each word
        int start = 0;
        for (int end = 0; end < len; end++) {
            if (a[end] == ' ') {
                reverse(a, start, end - 1);
                start = end + 1;
            }
        }
        // Reverse final word
        reverse(a, start, len - 1);

        return new String(a, 0, len);
    }

    /**
     * Reverses only the English letters in a string, keeping other characters in place.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public String reverseOnlyLetters(String s) {
        char[] chars = s.toCharArray();
        int left = 0, right = chars.length - 1;

        while (left < right) {
            if (!Character.isLetter(chars[left])) {
                left++;
            } else if (!Character.isLetter(chars[right])) {
                right--;
            } else {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            }
        }

        return new String(chars);
    }

    /**
     * Compresses consecutive repeating characters in-place.
     * Time Complexity : O(N)
     * Space Complexity: O(1) in-place modification of chars[]
     */
    public int compress(char[] chars) {
        int write = 0;
        int read = 0;
        int n = chars.length;

        while (read < n) {
            char currChar = chars[read];
            int count = 0;

            // Count run length of currChar
            while (read < n && chars[read] == currChar) {
                read++;
                count++;
            }

            // Write character
            chars[write++] = currChar;

            // Write count digits if > 1
            if (count > 1) {
                String countStr = String.valueOf(count);
                for (int i = 0; i < countStr.length(); i++) {
                    chars[write++] = countStr.charAt(i);
                }
            }
        }

        return write;
    }

    private int cleanSpaces(char[] a, int n) {
        int read = 0, write = 0;
        while (read < n) {
            // Skip leading or redundant spaces
            while (read < n && a[read] == ' ') read++;

            // Copy word
            while (read < n && a[read] != ' ') {
                a[write++] = a[read++];
            }

            // Skip spaces after word
            while (read < n && a[read] == ' ') read++;

            // If there are more words, add exactly one single space
            if (read < n) {
                a[write++] = ' ';
            }
        }
        return write;
    }

    private void reverse(char[] a, int left, int right) {
        while (left < right) {
            char temp = a[left];
            a[left] = a[right];
            a[right] = temp;
            left++;
            right--;
        }
    }
}
