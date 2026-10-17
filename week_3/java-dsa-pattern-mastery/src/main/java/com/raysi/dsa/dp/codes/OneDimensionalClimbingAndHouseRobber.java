package com.raysi.dsa.dp.codes;

/**
 * ============================================================================
 * PATTERN: 1D Dynamic Programming & State Space Optimization
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.dp.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. The 4-Step Dynamic Programming Framework:
 *    Step 1: Define state: What does dp[i] represent?
 *    Step 2: Derive state transition recurrence relation: dp[i] = f(dp[i-1], dp[i-2]...)
 *    Step 3: Establish base cases: dp[0], dp[1].
 *    Step 4: Space Optimization: If dp[i] depends only on the previous K states,
 *            compress the O(N) array into K scalar variables -> O(1) space!
 * 2. House Robber (Circular Array):
 *    Since house 0 and house N-1 are adjacent, you cannot rob both.
 *    Split into two independent linear subproblems:
 *      Pass A: Rob houses in range [0, N - 2]
 *      Pass B: Rob houses in range [1, N - 1]
 *      Result = max(Pass A, Pass B).
 * ============================================================================
 */
public class OneDimensionalClimbingAndHouseRobber {

    /**
     * Climbing Stairs (LC 70) in O(1) space.
     * dp[i] = dp[i - 1] + dp[i - 2]
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public int climbStairs(int n) {
        if (n <= 2) return n;

        int prev2 = 1; // 1 way to reach step 1
        int prev1 = 2; // 2 ways to reach step 2

        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }

    /**
     * House Robber I (LC 198) in O(1) space.
     * dp[i] = max(dp[i - 1], dp[i - 2] + nums[i])
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int prev2 = 0; // max money robbing up to i - 2
        int prev1 = 0; // max money robbing up to i - 1

        for (int num : nums) {
            int curr = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }

    /**
     * House Robber II (LC 213) for circular street.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public int robCircular(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        if (n == 1) return nums[0];

        // Max of robbing [0 ... n-2] or [1 ... n-1]
        return Math.max(robRange(nums, 0, n - 2), robRange(nums, 1, n - 1));
    }

    private int robRange(int[] nums, int start, int end) {
        int prev2 = 0;
        int prev1 = 0;

        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }

    /**
     * Decode Ways (LC 91) in O(1) space.
     * Time Complexity : O(N)
     * Space Complexity: O(1)
     */
    public int numDecodings(String s) {
        if (s == null || s.isEmpty() || s.charAt(0) == '0') return 0;

        int n = s.length();
        int prev2 = 1; // dp[i-2]
        int prev1 = 1; // dp[i-1]

        for (int i = 1; i < n; i++) {
            int curr = 0;
            int singleDigit = s.charAt(i) - '0';
            int twoDigits = Integer.parseInt(s.substring(i - 1, i + 1));

            if (singleDigit >= 1) {
                curr += prev1;
            }
            if (twoDigits >= 10 && twoDigits <= 26) {
                curr += prev2;
            }

            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }
}
