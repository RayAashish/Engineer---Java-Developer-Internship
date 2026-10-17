package com.raysi.dsa.recursionandbacktracking.codes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: Subsets & Combinations (Pick vs Non-Pick / Inclusion-Exclusion)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.recursionandbacktracking.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Combinatorial Decision Tree:
 *    At index i, we make a binary choice:
 *      Choice A: Pick nums[i] -> recurse to i + 1 (or i if elements are reusable)
 *      Choice B: Backtrack (remove nums[i]) -> Non-Pick -> recurse without nums[i]
 * 2. Deduplication Invariant:
 *    When the input contains duplicate numbers:
 *      Step 1: Sort the array so duplicates are adjacent.
 *      Step 2: In the loop over candidates, if (i > start && nums[i] == nums[i-1]),
 *              skip it! This ensures only the FIRST occurrence of a duplicate
 *              can initiate a branch at the current recursion depth.
 * ============================================================================
 */
public class SubsetsPickAndNonPickTemplate {

    /**
     * Subsets I (Unique elements).
     * Time Complexity : O(N * 2^N)
     * Space Complexity: O(N) recursion depth
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubsets(0, nums, new ArrayList<>(), result);
        return result;
    }

    private void backtrackSubsets(int start, int[] nums, List<Integer> current, List<List<Integer>> result) {
        // Every node in the decision tree represents a valid subset
        result.add(new ArrayList<>(current));

        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);                                   // 1. Choose
            backtrackSubsets(i + 1, nums, current, result);         // 2. Explore
            current.remove(current.size() - 1);                     // 3. Un-choose (Backtrack)
        }
    }

    /**
     * Subsets II (May contain duplicate elements).
     * Time Complexity : O(N * 2^N)
     * Space Complexity: O(N) recursion depth
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // Sort to cluster duplicate numbers together
        backtrackSubsetsWithDup(0, nums, new ArrayList<>(), result);
        return result;
    }

    private void backtrackSubsetsWithDup(int start, int[] nums, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));

        for (int i = start; i < nums.length; i++) {
            // Deduplication rule: skip duplicate candidate at current branch level
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            current.add(nums[i]);
            backtrackSubsetsWithDup(i + 1, nums, current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Combination Sum I: Unlimited element reuse.
     * Time Complexity : O(2^target)
     * Space Complexity: O(target / min_candidate)
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrackCombinationSum(0, candidates, target, new ArrayList<>(), result);
        return result;
    }

    private void backtrackCombinationSum(int start, int[] candidates, int remain, List<Integer> current, List<List<Integer>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remain) break; // Prune branch (sorted)

            current.add(candidates[i]);
            // Pass 'i' (not i + 1) because same element can be reused
            backtrackCombinationSum(i, candidates, remain - candidates[i], current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Combination Sum II: Each number may only be used once, handles duplicates.
     * Time Complexity : O(2^N)
     * Space Complexity: O(N)
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrackCombinationSum2(0, candidates, target, new ArrayList<>(), result);
        return result;
    }

    private void backtrackCombinationSum2(int start, int[] candidates, int remain, List<Integer> current, List<List<Integer>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remain) break;
            if (i > start && candidates[i] == candidates[i - 1]) continue; // Skip duplicates

            current.add(candidates[i]);
            backtrackCombinationSum2(i + 1, candidates, remain - candidates[i], current, result);
            current.remove(current.size() - 1);
        }
    }
}
