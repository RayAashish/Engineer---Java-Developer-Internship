# Phase 9: Dynamic Programming — The 4-Step Framework & Space Optimization
> **Author:** raysi  
> **Package:** `com.raysi.dsa.dp`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master the systematic progression from recursion to memoization, tabulation, and O(1) space optimization.

---

## 1. Dynamic Programming Mental Model & The 4-Step Framework

### 1.1 The Two Invariants of Dynamic Programming
You should apply Dynamic Programming if and only if the problem exhibits:
1. **Overlapping Subproblems:** The naive recursion tree recalculates the exact same state multiple times (e.g. `fib(3)` computed exponentially many times).
2. **Optimal Substructure:** The optimal solution to the overall problem is composed of optimal solutions to its constituent subproblems (e.g. Shortest path $A \to C$ passing through $B$ requires shortest path $A \to B$ and $B \to C$).

---

### 1.2 The 4-Step Evolutionary Pipeline

```
[ Step 1: Plain Recursion ]       Time: O(2^N)    Space: O(N) Call Stack
            |
            v
[ Step 2: Memoization ]           Time: O(N)      Space: O(N) Cache + O(N) Call Stack
  Top-Down with cache[]
            |
            v
[ Step 3: Tabulation ]            Time: O(N)      Space: O(N) Iterative Table
  Bottom-Up (No Stack Overflow)
            |
            v
[ Step 4: Space Optimization ]    Time: O(N)      Space: O(1) or O(W) (Variables only!)
  State Compression
```

#### The Golden Space-Compression Rule:
If the transition for `dp[i]` only references `dp[i-1]` and `dp[i-2]`, **discard the array completely**! Replace with two scalar variables (`prev1`, `prev2`) to achieve strictly $O(1)$ auxiliary memory.

---

### 1.3 The 1D Knapsack Direction Rule (The #1 DP Interview Trap!)

```
+------------------------------------+------------------------------------+
| 0/1 Knapsack (At Most 1 Use)       | Unbounded Knapsack (Unlimited Use) |
+------------------------------------+------------------------------------+
| Loop capacity BACKWARDS:           | Loop capacity FORWARDS:            |
| for (int w = W; w >= wt[i]; w--)   | for (int w = wt[i]; w <= W; w++)   |
|                                    |                                    |
| WHY? dp[w - wt[i]] refers to the   | WHY? dp[w - wt[i]] refers to the   |
| PREVIOUS item's value, guaranteeing| CURRENT item's updated value,      |
| item i is not reused!              | intentionally allowing reuse!      |
+------------------------------------+------------------------------------+
```

---

### 1.4 The Sentinel Infinity Trap
When finding minimum cost or minimum coins:
```java
// DANGEROUS: dp[w - coin] + 1 will overflow to Integer.MIN_VALUE if dp[w - coin] is MAX_VALUE!
int[] dp = new int[amount + 1];
Arrays.fill(dp, Integer.MAX_VALUE);

// SAFE: Use a safe sentinel that cannot overflow when adding 1
int[] dp = new int[amount + 1];
Arrays.fill(dp, amount + 1); // Max possible coins is 'amount' (using 1-cent coins)
```

---

### 1.5 Constraints to Big-O Table for DP

| Input Range ($N$) | Permissible Big-O | Applicable DP Dimension |
| :--- | :--- | :--- |
| $N \le 20$ | $O(N^2 \cdot 2^N)$ | Bitmask DP (Traveling Salesperson) |
| $N \le 400$ | $O(N^3)$ | Interval DP (Burst Balloons, Matrix Chain) |
| $N \le 2000$ | $O(N^2)$ | 2D DP (LCS, Edit Distance, Grid Paths) |
| $N \le 10^5$ | $O(N)$ or $O(N \log N)$ | 1D DP (House Robber), LIS with Binary Search |

---

## 2. The 5 Core Dynamic Programming Patterns

```
                                  +---------------------------+
                                  |    Master DP Patterns     |
                                  +---------------------------+
                                                |
          +-------------+-------------+---------+-------------+-------------+
          |             |             |                       |             |
+---------------+ +---------------+ +---------------+ +---------------+ +---------------+
| Pattern 1: 1D | | Pattern 2:    | | Pattern 3:    | | Pattern 4: 2D | | Pattern 5:    |
| Linear State  | | Knapsack 01/Un| | Grid Paths    | | String Match  | | LIS Patience  |
+---------------+ +---------------+ +---------------+ +---------------+ +---------------+
  Climb Stairs    0/1: Backwards    Unique Paths      LCS               Binary Search   
  House Robber    Unb: Forwards     Min Path Sum      Edit Distance     tails[] array   
  O(1) scalars    Coin Change       Max Square        prevDiagonal      O(N log N)      
```

---

### Pattern 1: 1D Linear State Transitions
- **Trigger Keywords:** `"climbing stairs"`, `"house robber"`, `"decode ways"`.
- **Mental Model:** Current state depends on a constant number of previous states: $dp[i] = f(dp[i-1], dp[i-2])$. Compress state to 2 variables.

---

### Pattern 2: 0/1 and Unbounded Knapsack
- **Trigger Keywords:** `"subset sum"`, `"partition equal subset"`, `"coin change"`, `"target sum"`.
- **Mental Model:** 
  - 0/1 Knapsack (use item $\le 1$ time): Capacity loop counts DOWN.
  - Unbounded Knapsack (unlimited reuse): Capacity loop counts UP.

---

### Pattern 3: 2D Grid DP & Row Compression
- **Trigger Keywords:** `"unique paths"`, `"minimum path sum"`, `"maximal square"`.
- **Mental Model:** $dp[r][c] = f(dp[r-1][c], dp[r][c-1])$. Compress table from $R \times C$ to a single 1D array of size $C$: `dp[c] += dp[c - 1]`.

---

### Pattern 4: 2D String Matching (LCS & Edit Distance)
- **Trigger Keywords:** `"longest common subsequence"`, `"edit distance"`, `"distinct subsequences"`.
- **Mental Model:** Compare character $s_1[i-1]$ with $s_2[j-1]$. If match, take diagonal $(i-1, j-1)$; if mismatch, take max or min of left and top. Compress 2D to 1D using a `prevDiagonal` scalar.

---

### Pattern 5: Longest Increasing Subsequence (Patience Sorting)
- **Trigger Keywords:** `"longest increasing subsequence"`, `"russian doll envelopes"`.
- **Mental Model:** Maintain array `tails` of smallest end elements. Binary search for position of incoming element in $O(\log N)$. Total time $O(N \log N)$.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: House Robber II (LeetCode 213)
> **Problem:** Houses arranged in a circle. You cannot rob adjacent houses, and the first and last houses are adjacent. Return maximum money.

#### 1. Brute Force Instinct
Enumerate all non-adjacent subsets $\implies O(2^N)$.

#### 2. "Aha!" Observation
Since House 0 and House $N-1$ cannot both be robbed:
- Subproblem A: Ignore House $N-1$ $\implies$ rob houses in range $[0, N - 2]$.
- Subproblem B: Ignore House 0 $\implies$ rob houses in range $[1, N - 1]$.
- Run standard linear House Robber in $O(1)$ space on both subproblems. Return $\max(A, B)$.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.dp;

public class HouseRobberIIAnchor {
    public int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        if (n == 1) return nums[0];

        return Math.max(robLinear(nums, 0, n - 2), robLinear(nums, 1, n - 1));
    }

    private int robLinear(int[] nums, int start, int end) {
        int prev2 = 0;
        int prev1 = 0;

        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(1)$ auxiliary.

---

### Anchor Problem #2: Coin Change (LeetCode 322)
> **Problem:** Given integer array $coins$ and integer $amount$, return fewest number of coins to make up that amount, or -1. Unlimited supply of each coin.

#### 1. Brute Force Instinct
Greedy choice (pick largest coin) fails! Counterexample: $coins = [1, 3, 4], amount = 6$. Greedy takes $4 + 1 + 1 = 3$ coins; optimal is $3 + 3 = 2$ coins.

#### 2. "Aha!" Observation
Unbounded Knapsack!
- Initialize `dp` of size $amount + 1$ with $amount + 1$.
- Base case: `dp[0] = 0`.
- Loop coins. For each coin, loop $a$ **FORWARDS** from $coin$ to $amount$:
  $$dp[a] = \min(dp[a], dp[a - coin] + 1)$$

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.dp;

import java.util.Arrays;

public class CoinChangeAnchor {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) {
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
            }
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \cdot \text{amount})$
- **Space Complexity:** $O(\text{amount})$

---

### Anchor Problem #3: Unique Paths II (LeetCode 63)
> **Problem:** Robot in $m \times n$ grid with obstacles ('1'). Robot can only move down or right. Return unique paths to bottom-right.

#### 1. Brute Force Instinct
Full 2D DP array of size $m \times n \implies O(R \cdot C)$ auxiliary space.

#### 2. "Aha!" Observation
Row-Space Compression:
- Cell $dp[r][c]$ only depends on current row left cell ($c - 1$) and previous row same column ($c$).
- Use a single 1D array of size $cols$:
  - If cell is obstacle: `dp[c] = 0`.
  - Else if $c > 0$: `dp[c] += dp[c - 1]`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.dp;

public class UniquePathsIIAnchor {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int cols = obstacleGrid[0].length;
        int[] dp = new int[cols];
        dp[0] = (obstacleGrid[0][0] == 0) ? 1 : 0;

        for (int[] row : obstacleGrid) {
            for (int c = 0; c < cols; c++) {
                if (row[c] == 1) {
                    dp[c] = 0;
                } else if (c > 0) {
                    dp[c] += dp[c - 1];
                }
            }
        }

        return dp[cols - 1];
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C)$
- **Space Complexity:** $O(C)$ where $C$ is number of columns.

---

### Anchor Problem #4: Longest Common Subsequence (LeetCode 1143)
> **Problem:** Given two strings $text1$ and $text2$, return the length of their longest common subsequence.

#### 1. Brute Force Instinct
Generate all $2^M$ subsequences of $text1$ and check if present in $text2 \implies O(2^M \cdot N)$.

#### 2. "Aha!" Observation
2D DP:
- If $text1[i-1] == text2[j-1]$: $dp[i][j] = 1 + dp[i-1][j-1]$.
- Else: $dp[i][j] = \max(dp[i-1][j], dp[i][j-1])$.
- Compress to 1D: Save diagonal value `prevDiagonal` before overwriting `dp[j]`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.dp;

public class LongestCommonSubsequenceAnchor {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[] dp = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            int prevDiagonal = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = 1 + prevDiagonal;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                prevDiagonal = temp;
            }
        }

        return dp[n];
    }
}
```
*Complexity:*
- **Time Complexity:** $O(M \cdot N)$
- **Space Complexity:** $O(N)$

---

### Anchor Problem #5: Longest Increasing Subsequence (LeetCode 300)
> **Problem:** Given an integer array $nums$, return the length of the longest strictly increasing subsequence in $O(N \log N)$ time.

#### 1. Brute Force Instinct
Standard 1D DP: $dp[i] = 1 + \max_{j < i}(dp[j]) \implies O(N^2)$ time.

#### 2. "Aha!" Observation
Patience Sorting / Binary Search ($O(N \log N)$):
- Maintain an array `tails` where `tails[i]` stores the smallest tail of all increasing subsequences of length $i + 1$.
- For each number $x$:
  - Binary search for $x$ in `tails`.
  - If $x$ is larger than all elements in `tails`, append $x$ (extends LIS).
  - Else, overwrite the first element in `tails` that is $\ge x$ with $x$ (greedily lowers the ceiling for future elements!).

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.dp;

public class LongestIncreasingSubsequenceAnchor {
    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int[] tails = new int[nums.length];
        int size = 0;

        for (int x : nums) {
            int low = 0, high = size;

            while (low < high) {
                int mid = low + (high - low) / 2;
                if (tails[mid] < x) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            tails[low] = x;
            if (low == size) size++;
        }

        return size;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \log N)$
- **Space Complexity:** $O(N)$ for `tails` array.

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Direction of Knapsack Capacity Loops:**
   - Writing `w++` in a 0/1 Knapsack allows the same item to be picked repeatedly! Always write `w--` for 0/1 Knapsack.
2. **Empty Prefix Offsetting in 2D DP:**
   - In string DP, dimension $0$ represents the empty string `""`.
   - String index at row $i$ is $s_1.charAt(i - 1)$, NOT $s_1.charAt(i)$!
3. **Loss of Diagonal Value in 1D Space Optimization:**
   - Overwriting `dp[j]` destroys $dp[i-1][j-1]$ needed for column $j+1$. Always cache in `prevDiagonal`.
