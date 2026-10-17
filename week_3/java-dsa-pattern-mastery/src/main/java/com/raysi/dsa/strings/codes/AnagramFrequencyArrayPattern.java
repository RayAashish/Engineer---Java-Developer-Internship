package com.raysi.dsa.strings.codes;

import java.util.*;

/**
 * ============================================================================
 * PATTERN: Anagram Frequency Array & Sliding Window Invariant
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.strings.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Two strings are anagrams iff their character frequency histograms match.
 * 2. In Java, allocating a `new int[26]` is orders of magnitude faster than
 *    `HashMap<Character, Integer>` because it lives contiguously on the stack/heap,
 *    incurs zero object boxing overhead, and compares in O(1) time (26 operations).
 * 3. Sliding Window Anagram Matching maintains a fixed-size window of size |P|:
 *    Increment count on incoming right character, decrement on outgoing left character.
 * ============================================================================
 */
public class AnagramFrequencyArrayPattern {

    /**
     * Finds all start indices of p's anagrams in s.
     * Fixed-size sliding window of length p.length().
     * Time Complexity : O(N) where N = s.length()
     * Space Complexity: O(1) auxiliary (fixed 26-element arrays)
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s == null || p == null || s.length() < p.length()) {
            return result;
        }

        int[] pCount = new int[26];
        int[] sCount = new int[26];
        int pLen = p.length();
        int sLen = s.length();

        for (int i = 0; i < pLen; i++) {
            pCount[p.charAt(i) - 'a']++;
            sCount[s.charAt(i) - 'a']++;
        }

        if (matches(pCount, sCount)) {
            result.add(0);
        }

        // Slide window of fixed length pLen
        for (int right = pLen; right < sLen; right++) {
            sCount[s.charAt(right) - 'a']++;               // Add incoming char
            sCount[s.charAt(right - pLen) - 'a']--;        // Evict outgoing char

            if (matches(pCount, sCount)) {
                result.add(right - pLen + 1);
            }
        }

        return result;
    }

    /**
     * Determines whether s1's permutation is a substring of s2.
     * Time Complexity : O(N) where N = s2.length()
     * Space Complexity: O(1) auxiliary
     */
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;

        int[] count1 = new int[26];
        int[] count2 = new int[26];
        int len1 = s1.length();

        for (int i = 0; i < len1; i++) {
            count1[s1.charAt(i) - 'a']++;
            count2[s2.charAt(i) - 'a']++;
        }

        int matches = 0;
        for (int i = 0; i < 26; i++) {
            if (count1[i] == count2[i]) matches++;
        }

        for (int right = len1; right < s2.length(); right++) {
            if (matches == 26) return true;

            int inIdx = s2.charAt(right) - 'a';
            int outIdx = s2.charAt(right - len1) - 'a';

            // Process incoming character
            count2[inIdx]++;
            if (count2[inIdx] == count1[inIdx]) {
                matches++;
            } else if (count2[inIdx] == count1[inIdx] + 1) {
                matches--;
            }

            // Process outgoing character
            count2[outIdx]--;
            if (count2[outIdx] == count1[outIdx]) {
                matches++;
            } else if (count2[outIdx] == count1[outIdx] - 1) {
                matches--;
            }
        }

        return matches == 26;
    }

    /**
     * Groups anagrams together.
     * Uses unique frequency string key format: "#count0#count1...#count25".
     * Time Complexity : O(N * K) where N = words count, K = max string length
     * Space Complexity: O(N * K)
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) return new ArrayList<>();

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) {
                count[c - 'a']++;
            }

            // Build deterministic canonical signature key
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                sb.append('#').append(count[i]);
            }
            String key = sb.toString();

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }

    private boolean matches(int[] a, int[] b) {
        for (int i = 0; i < 26; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
}
