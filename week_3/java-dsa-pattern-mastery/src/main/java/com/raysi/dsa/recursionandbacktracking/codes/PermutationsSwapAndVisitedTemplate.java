package com.raysi.dsa.recursionandbacktracking.codes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: Permutations (Swap vs Visited Tracking)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.recursionandbacktracking.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Subsets vs Permutations:
 *    - Subsets: Order does NOT matter ([1,2] == [2,1]). Advance index `start` forward.
 *    - Permutations: Order MATTERS ([1,2] != [2,1]). Loop over ALL indices [0, N-1],
 *      skipping already visited elements using `boolean[] visited`.
 * 2. In-Place Swap Template:
 *    Swaps elements in-place: swap(nums, first, i), recurse(first + 1), swap(nums, first, i).
 *    Avoids extra memory allocations.
 * 3. Permutations with Duplicates Invariant:
 *    Sort array. If (i > 0 && nums[i] == nums[i-1] && !visited[i-1]), skip!
 *    Crucial insight: checking `!visited[i-1]` ensures that duplicate elements are
 *    only picked in left-to-right relative order, eliminating identical branches.
 * ============================================================================
 */
public class PermutationsSwapAndVisitedTemplate {

    /**
     * Permutations I (Unique elements) using visited tracking.
     * Time Complexity : O(N * N!)
     * Space Complexity: O(N) recursion stack + visited array
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrackPermute(nums, visited, new ArrayList<>(), result);
        return result;
    }

    private void backtrackPermute(int[] nums, boolean[] visited, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;

            visited[i] = true;
            current.add(nums[i]);

            backtrackPermute(nums, visited, current, result);

            current.remove(current.size() - 1);
            visited[i] = false;
        }
    }

    /**
     * Permutations II (May contain duplicates).
     * Time Complexity : O(N * N!)
     * Space Complexity: O(N)
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        boolean[] visited = new boolean[nums.length];
        backtrackPermuteUnique(nums, visited, new ArrayList<>(), result);
        return result;
    }

    private void backtrackPermuteUnique(int[] nums, boolean[] visited, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;
            // Skip duplicate: only pick duplicate if previous identical element was chosen
            if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) continue;

            visited[i] = true;
            current.add(nums[i]);

            backtrackPermuteUnique(nums, visited, current, result);

            current.remove(current.size() - 1);
            visited[i] = false;
        }
    }

    /**
     * Letter Combinations of a Phone Number.
     * Time Complexity : O(4^N)
     * Space Complexity: O(N)
     */
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return result;

        String[] phoneMap = {
            "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
        };

        backtrackPhone(0, digits, phoneMap, new StringBuilder(), result);
        return result;
    }

    private void backtrackPhone(int index, String digits, String[] phoneMap, StringBuilder current, List<String> result) {
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }

        String letters = phoneMap[digits.charAt(index) - '0'];
        for (int i = 0; i < letters.length(); i++) {
            current.append(letters.charAt(i));
            backtrackPhone(index + 1, digits, phoneMap, current, result);
            current.deleteCharAt(current.length() - 1);
        }
    }
}
