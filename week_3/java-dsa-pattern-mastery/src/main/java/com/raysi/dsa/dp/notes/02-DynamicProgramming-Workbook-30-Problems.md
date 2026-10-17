# Phase 9: Dynamic Programming — Self-Guided Problem Workbook (30 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.dp`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: 1D Linear State Transitions

---

### Problem #1: Climbing Stairs (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Fibonacci Recurrence
- **Problem Statement & Constraints:** Reach top of staircase of $n$ steps taking 1 or 2 steps at a time.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $n = 3 \to$ Output: `3` (1+1+1, 1+2, 2+1).
  - *Test Case 2 (Base Case):* $n = 1 \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** $dp[i] = dp[i-1] + dp[i-2]$. Maintain two variables `prev1 = 2, prev2 = 1`. Update in loop.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Min Cost Climbing Stairs (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Minimum Cost 1D Transition
- **Problem Statement & Constraints:** Pay cost to step 1 or 2 steps forward. Find minimum cost to reach top of floor.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $cost = [10, 15, 20] \to$ Output: `15`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** $dp[i] = cost[i] + \min(dp[i-1], dp[i-2])$. Track last two costs with scalar variables.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: House Robber I (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Skip or Rob Transition
- **Problem Statement & Constraints:** Maximize money robbing non-adjacent houses.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,2,3,1] \to$ Output: `4` (1 + 3).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** $dp[i] = \max(dp[i-1], dp[i-2] + nums[i])$. Maintain `prev1` (rob up to $i-1$) and `prev2` (rob up to $i-2$).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: House Robber II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Circular Array Subproblem Split
- **Problem Statement & Constraints:** Houses arranged in a circle. Cannot rob first and last together.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [2,3,2] \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Split into two linear subproblems: $\max(rob(nums[0 \dots n-2]), rob(nums[1 \dots n-1]))$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Decode Ways (Difficulty: Medium)
- **Pattern Category:** Pattern 1: 1-Digit and 2-Digit Valid Range Check
- **Problem Statement & Constraints:** Number of ways to decode string of digits ('1'-'26' $\to$ 'A'-'Z').
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "226" \to$ Output: `3` (BZ, VF, BBF).
  - *Test Case 2 (Zero Trap):* $s = "06" \to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** $dp[i]$ accumulates $dp[i-1]$ if single digit $s[i] \in [1, 9]$ and $dp[i-2]$ if two digits $s[i-1...i] \in [10, 26]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Word Break (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Boolean Suffix Partitioning
- **Problem Statement & Constraints:** Determine if string $s$ can be segmented into words in $wordDict$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "leetcode", wordDict = ["leet","code"] \to$ Output: `true`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(N)$ Space):** `boolean[] dp` where $dp[i]$ is true if prefix of length $i$ is segmentable. $dp[0] = true$. For each $i \in [1, N]$: for $j \in [0, i-1]$: if $dp[j]$ is true and $s[j...i]$ in dictionary, set $dp[i] = true$ and break.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Jump Game II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Greedy Interval BFS / 1D DP
- **Problem Statement & Constraints:** Minimum number of jumps to reach index $n - 1$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [2,3,1,1,4] \to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Greedy Approach ($O(N)$ Time, $O(1)$ Space):** Track current jump end boundary `currEnd` and farthest reachable index `farthest`. When index reaches `currEnd`, increment jumps and set `currEnd = farthest`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: 0/1 & Unbounded Knapsack

---

### Problem #8: 0/1 Knapsack (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Backward Capacity Loop
- **Problem Statement & Constraints:** Maximize value with weight capacity $W$. Items used $\le 1$ time.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Maximize value within weight limit.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot W)$ Time, $O(W)$ Space):** Loop items. Loop capacity $w$ **BACKWARD** from $W$ down to $wt[i]$: $dp[w] = \max(dp[w], dp[w - wt[i]] + val[i])$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Partition Equal Subset Sum (Difficulty: Medium)
- **Pattern Category:** Pattern 2: 0/1 Knapsack Boolean Subset
- **Problem Statement & Constraints:** Can array be partitioned into two subsets with equal sum?
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,5,11,5] \to$ Output: `true` (sum 11).
  - *Test Case 2 (Odd Sum):* $nums = [1,2,3,5] \to$ Output: `false` (total sum 11 is odd).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot target)$ Time, $O(target)$ Space):** Target is $totalSum / 2$. Boolean array `dp[target + 1]`. Loop $s$ from $target$ down to $num$: $dp[s] = dp[s] \lor dp[s - num]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Target Sum (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Subset Sum Algebraic Reduction
- **Problem Statement & Constraints:** Assign '+' or '-' to integers in array to reach $target$. Return number of ways.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,1,1,1,1], target = 3 \to$ Output: `5`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot subsetTarget)$ Time, $O(subsetTarget)$ Space):** Let $P$ be positive subset and $N$ be negative subset: $P - N = target$ and $P + N = totalSum \implies 2P = target + totalSum \implies P = (target + totalSum) / 2$. Problem reduces to finding number of subsets summing to $P$ via 0/1 Knapsack backward counting.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Ones and Zeroes (Difficulty: Medium)
- **Pattern Category:** Pattern 2: 2D Capacity Knapsack
- **Problem Statement & Constraints:** Maximum subset of binary strings with at most $m$ 0s and $n$ 1s.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $strs = ["10","0001","111001","1","0"], m = 5, n = 3 \to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(K \cdot m \cdot n)$ Time, $O(m \cdot n)$ Space):** 2D array $dp[m+1][n+1]$. For each string with $z$ zeros and $o$ ones, loop $i$ from $m$ down to $z$, and $j$ from $n$ down to $o$: $dp[i][j] = \max(dp[i][j], 1 + dp[i - z][j - o])$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Coin Change I (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Unbounded Knapsack Forward Loop
- **Problem Statement & Constraints:** Fewest coins to make amount.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $coins = [1,2,5], amount = 11 \to$ Output: `3` (5 + 5 + 1).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot amount)$ Time, $O(amount)$ Space):** Initialize $dp$ with $amount + 1$. $dp[0] = 0$. Loop coins. For each coin, loop $a$ **FORWARDS** from $coin$ to $amount$: $dp[a] = \min(dp[a], dp[a - coin] + 1)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #13: Coin Change II (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Unbounded Knapsack Combinations
- **Problem Statement & Constraints:** Number of combinations that make up amount.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $amount = 5, coins = [1,2,5] \to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot amount)$ Time, $O(amount)$ Space):** Outer loop: coins. Inner loop: $a$ from $coin$ up to $amount$: $dp[a] += dp[a - coin]$. Outer coin loop prevents duplicate permutations like $[1, 2]$ and $[2, 1]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Combination Sum IV (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Permutation Counting (Outer Target Loop)
- **Problem Statement & Constraints:** Number of possible combinations (where different orders count as distinct) summing to target.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,2,3], target = 4 \to$ Output: `7`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot target)$ Time, $O(target)$ Space):** Outer loop is target $t$ from 1 to $target$. Inner loop is numbers $num \in nums$: if $t \ge num$, $dp[t] += dp[t - num]$. Outer target loop naturally generates all permutations.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: 2D Grid DP & Geometry

---

### Problem #15: Unique Paths I (Difficulty: Medium)
- **Pattern Category:** Pattern 3: 1D Row Accumulation
- **Problem Statement & Constraints:** Robot moves down or right on $m \times n$ grid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $m = 3, n = 7 \to$ Output: `28`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(C)$ Space):** $dp[c]$ array filled with 1s. Loop rows: for $c \in [1, n-1]$, $dp[c] += dp[c-1]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Unique Paths II (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Obstacle Zeroing
- **Problem Statement & Constraints:** Grid with obstacles (1).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Path bypassing obstacle.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(C)$ Space):** If cell is obstacle, $dp[c] = 0$. Else if $c > 0$, $dp[c] += dp[c-1]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Minimum Path Sum (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Top/Left Min Row Compression
- **Problem Statement & Constraints:** Path from top-left to bottom-right minimizing sum of numbers along path.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $grid = [[1,3,1],[1,5,1],[4,2,1]] \to$ Output: `7`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(C)$ Space):** $dp[c] = grid[r][c] + \min(dp[c], dp[c-1])$. First row initialized with prefix sums.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Triangle (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Bottom-Up Triangular Reduction
- **Problem Statement & Constraints:** Minimum path sum from top to bottom of triangle.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $triangle = [[2],[3,4],[6,5,7],[4,1,8,3]] \to$ Output: `11`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Bottom-Up Approach ($O(N^2)$ Time, $O(N)$ Space):** Initialize $dp$ with bottom row. Loop row $r$ from $n - 2$ down to 0: for col $c \in [0, r]$, $dp[c] = triangle[r][c] + \min(dp[c], dp[c+1])$. Answer is $dp[0]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Dungeon Game (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Reverse Bottom-Right Health DP
- **Problem Statement & Constraints:** Knight must reach princess with health $> 0$ at all times. Return minimum initial health.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Negative damage rooms and healing rooms.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Reverse Approach ($O(R \cdot C)$ Time, $O(C)$ Space):** Work backwards from bottom-right $(R-1, C-1)$ to $(0, 0)$. Health required to survive moving to $(r, c)$ is $\max(1, \min(dp[r+1][c], dp[r][c+1]) - dungeon[r][c])$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Maximal Square (Difficulty: Medium)
- **Pattern Category:** Pattern 3: 3-Way Minimum Square Expansion
- **Problem Statement & Constraints:** Find largest square containing only 1s and return its area.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Binary matrix $\to$ area of largest square.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(C)$ Space):** $dp[r][c] = 1 + \min(dp[r-1][c], dp[r][c-1], dp[r-1][c-1])$ if $matrix[r][c] == '1'$. Area is $\max(side)^2$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Minimum Falling Path Sum (Difficulty: Medium)
- **Pattern Category:** Pattern 3: 3-Branch Row-Below Minimization
- **Problem Statement & Constraints:** Falling path from row 0 to row $n-1$ moving to $(r+1, c-1)$, $(r+1, c)$, or $(r+1, c+1)$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Matrix of integers $\to$ min falling sum.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(N)$ Space):** In-place or 1D array. For row $r$, $dp[c] = matrix[r][c] + \min(dp[c-1], dp[c], dp[c+1])$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: 2D String Matching & Subsequences

---

### Problem #22: Longest Common Subsequence (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 2D String Match with Diagonal
- **Problem Statement & Constraints:** Return length of longest common subsequence.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s1 = "abcde", s2 = "ace" \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(M \cdot N)$ Time, $O(N)$ Space):** If $s1[i-1] == s2[j-1]$, $dp[j] = 1 + prevDiagonal$. Else $dp[j] = \max(dp[j], dp[j-1])$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Edit Distance (Difficulty: Medium / Hard)
- **Pattern Category:** Pattern 4: 3-Operation Levenshtein Distance
- **Problem Statement & Constraints:** Minimum operations (insert, delete, replace) to convert word1 to word2.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $word1 = "horse", word2 = "ros" \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(M \cdot N)$ Time, $O(N)$ Space):** If characters match, $dp[j] = prevDiagonal$. Else $dp[j] = 1 + \min(prevDiagonal, dp[j], dp[j-1])$ representing replace, delete, insert.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Distinct Subsequences (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Backward Subsequence Matching
- **Problem Statement & Constraints:** Number of distinct subsequences of $s$ which equal $t$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "rabbbit", t = "rabbit" \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(M \cdot N)$ Time, $O(N)$ Space):** $dp[0] = 1$. Loop $i \in [1, m]$. Loop $j$ **BACKWARD** from $n$ down to 1: if $s[i-1] == t[j-1]$, $dp[j] += dp[j-1]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Delete Operation for Two Strings (Difficulty: Medium)
- **Pattern Category:** Pattern 4: LCS Algebraic Complement
- **Problem Statement & Constraints:** Minimum steps to make two strings identical by deleting characters.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $word1 = "sea", word2 = "eat" \to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(M \cdot N)$ Time, $O(N)$ Space):** The characters to keep are the Longest Common Subsequence! Minimum deletions $= |s1| + |s2| - 2 \times LCS(s1, s2)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #26: Minimum ASCII Delete Sum for Two Strings (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Weighted LCS Complement
- **Problem Statement & Constraints:** Minimum ASCII sum of deleted characters to make two strings equal.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s1 = "sea", s2 = "eat" \to$ Output: `231`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(M \cdot N)$ Time, $O(N)$ Space):** Maximize ASCII sum of common subsequence $W$. If $s1[i-1] == s2[j-1]$, add $ascii(c)$. Total delete sum is $sum(s1) + sum(s2) - 2 \times W$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #27: Interleaving String (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Dual-String 2D Grid Interleaving
- **Problem Statement & Constraints:** Check if $s3$ is formed by an interleaving of $s1$ and $s2$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac" \to$ `true`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(M \cdot N)$ Time, $O(N)$ Space):** Check $|s1| + |s2| == |s3|$. Boolean $dp[j]$ is true if $s3[0...i+j-1]$ can be formed: $dp[j] = (dp[j] \land s1[i-1] == s3[i+j-1]) \lor (dp[j-1] \land s2[j-1] == s3[i+j-1])$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 5: Longest Increasing Subsequence & Interval DP

---

### Problem #28: Longest Increasing Subsequence (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Patience Sorting Binary Search
- **Problem Statement & Constraints:** Return length of longest strictly increasing subsequence in $O(N \log N)$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [10,9,2,5,3,7,101,18] \to$ Output: `4` ([2,3,7,101]).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** `tails` array. Binary search for first element $\ge x$. If found, overwrite; else append and increase size.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #29: Russian Doll Envelopes (Difficulty: Hard)
- **Pattern Category:** Pattern 5: Custom Multi-Key Sort + LIS
- **Problem Statement & Constraints:** Maximum number of envelopes you can Russian doll (fit inside one another).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $envelopes = [[5,4],[6,4],[6,7],[2,3]] \to$ Output: `3` ([2,3] -> [5,4] -> [6,7]).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** Sort envelopes: width ascending, and for equal width sort height descending (prevents picking identical widths). Run $O(N \log N)$ LIS on heights.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #30: Burst Balloons (Difficulty: Hard)
- **Pattern Category:** Pattern 5: Interval DP (Last Balloon Burst Inversion)
- **Problem Statement & Constraints:** Burst balloons to maximize coins: burst balloon $i$ yields $nums[i-1] \times nums[i] \times nums[i+1]$ coins.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [3,1,5,8] \to$ Output: `167`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Interval DP Approach ($O(N^3)$ Time, $O(N^2)$ Space):** Pad array with boundary 1s. Think in reverse: which balloon $k \in [i, j]$ is burst **LAST** in the interval $[i, j]$? Because balloon $k$ is burst last, its left boundary is $nums[i-1]$ and right boundary is $nums[j+1]$. Recurrence:
    $$dp[i][j] = \max_{i \le k \le j}(dp[i][k-1] + dp[k+1][j] + nums[i-1] \cdot nums[k] \cdot nums[j+1])$$
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
