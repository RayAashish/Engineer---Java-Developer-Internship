package com.raysi.dsa.dp.codes;

import java.util.Arrays;

/**
 * ============================================================================
 * PATTERN: 0/1 Knapsack vs Unbounded Knapsack (Direction of 1D Loop)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.dp.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. 0/1 Knapsack (Each item can be used AT MOST ONCE):
 *    - Recurrence: dp[w] = max(dp[w], dp[w - wt[i]] + val[i])
 *    - CRUCIAL 1D LOOP DIRECTION: Loop capacity BACKWARD from W down to wt[i]!
 *      Why? Because dp[w - wt[i]] must hold the value from the PREVIOUS item,
 *      not the newly updated value from the current item. Going forward would
 *      accidentally allow the same item to be reused multiple times.
 * 
 * 2. Unbounded Knapsack (Unlimited reuse of items / Coin Change):
 *    - Recurrence: dp[w] = min(dp[w], dp[w - coin] + 1)
 *    - CRUCIAL 1D LOOP DIRECTION: Loop capacity FORWARD from coin up to target!
 *      Why? Because we INTENTIONALLY want to build upon newly updated states from
 *      the current item, allowing unlimited reuse.
 * ============================================================================
 */
public class ZeroOneAndUnboundedKnapsackTemplate {

    /**
     * 0/1 Knapsack: Maximum value with capacity W.
     * Time Complexity : O(N * W)
     * Space Complexity: O(W) 1D array running backwards
     */
    public int knapsack01(int W, int[] wt, int[] val) {
        int[] dp = new int[W + 1];

        for (int i = 0; i < wt.length; i++) {
            // BACKWARD LOOP prevents duplicate reuse of item i
            for (int w = W; w >= wt[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - wt[i]] + val[i]);
            }
        }

        return dp[W];
    }

    /**
     * Partition Equal Subset Sum (LC 416).
     * Time Complexity : O(N * target)
     * Space Complexity: O(target)
     */
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) totalSum += num;

        // If sum is odd, cannot partition into two equal integers
        if (totalSum % 2 != 0) return false;

        int target = totalSum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // base case: 0 sum always achievable

        for (int num : nums) {
            // BACKWARD LOOP for 0/1 Knapsack boolean subset
            for (int s = target; s >= num; s--) {
                dp[s] = dp[s] || dp[s - num];
            }
        }

        return dp[target];
    }

    /**
     * Coin Change I (LC 322): Minimum coins to make amount (Unbounded Knapsack).
     * Time Complexity : O(amount * coins.length)
     * Space Complexity: O(amount) 1D array running forwards
     */
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // Sentinel for infinity
        dp[0] = 0;

        for (int coin : coins) {
            // FORWARD LOOP allows unlimited reuse of current coin
            for (int a = coin; a <= amount; a++) {
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
            }
        }

        return (dp[amount] > amount) ? -1 : dp[amount];
    }

    /**
     * Coin Change II (LC 518): Number of combinations to make amount (Unbounded Knapsack).
     * Time Complexity : O(amount * coins.length)
     * Space Complexity: O(amount)
     */
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;

        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) {
                dp[a] += dp[a - coin];
            }
        }

        return dp[amount];
    }
}
