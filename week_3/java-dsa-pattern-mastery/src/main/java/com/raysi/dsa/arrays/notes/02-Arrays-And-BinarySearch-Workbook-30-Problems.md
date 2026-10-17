# Phase 1: Arrays & Binary Search — Self-Guided Problem Workbook (30 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.arrays`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **ZERO SOLUTION CODE PROVIDED.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Two Pointers (Opposite & Same Direction)

---

### Problem #1: Container With Most Water (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Opposite-Direction Two Pointers
- **Problem Statement & Constraints:** Given an integer array `height` of length $N$ ($2 \le N \le 10^5$, $0 \le \text{height}[i] \le 10^4$), find two lines that together with the x-axis form a container that holds the most water. Return the maximum area.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `height = [1, 8, 6, 2, 5, 4, 8, 3, 7]` $\to$ Output: `49`.  
    *Explanation:* Line at index 1 (`height=8`) and line at index 8 (`height=7`) span distance $8 - 1 = 7$. Water height is $\min(8, 7) = 7$. Area = $7 \times 7 = 49$.
  - *Test Case 2 (Tricky / Boundary):* `height = [1, 1]` $\to$ Output: `1`.  
    *Explanation:* Minimum array length. Distance is 1, minimum height is 1. Area = $1 \times 1 = 1$.
  - *Test Case 3 (Failure Trap):* `height = [1, 100, 100, 1]` $\to$ Output: `100`.  
    *Explanation:* Naive greedy width-first would stay at boundaries ($1 \times 3 = 3$). Shrinking the smaller boundary inwards is required to discover the tall interior lines ($100 \times 1 = 100$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Nested loop comparing every index pair $(i, j)$ and computing $(j - i) \times \min(height[i], height[j])$.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Initialize `left = 0` and `right = N - 1`. The area is constrained by the shorter wall. Moving the taller wall cannot increase the area because width decreases and height is still bottlenecked by the shorter wall. Therefore, strictly increment `left` if `height[left] < height[right]`, else decrement `right`, maintaining `maxArea`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: 3Sum (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Sorting + Two Pointers
- **Problem Statement & Constraints:** Given an integer array `nums` of size $N$ ($3 \le N \le 3000$, $-10^5 \le \text{nums}[i] \le 10^5$), return all unique triplets `[nums[i], nums[j], nums[k]]` such that $i \ne j \ne k$ and `nums[i] + nums[j] + nums[k] == 0`. The solution set must not contain duplicate triplets.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [-1, 0, 1, 2, -1, -4]` $\to$ Output: `[[-1, -1, 2], [-1, 0, 1]]`.  
    *Explanation:* Sorted: `[-4, -1, -1, 0, 1, 2]`. Distinct zero-sum triplets are identified without duplicate duplicates.
  - *Test Case 2 (Tricky / Boundary):* `nums = [0, 0, 0, 0]` $\to$ Output: `[[0, 0, 0]]`.  
    *Explanation:* Must return exactly one triplet `[0, 0, 0]`, skipping duplicate zero candidates.
  - *Test Case 3 (Failure Trap):* `nums = [-2, 0, 1, 1, 2]` $\to$ Output: `[[-2, 0, 2], [-2, 1, 1]]`.  
    *Explanation:* If duplicate skipping logic is applied before finding a match, valid triplets using identical values at distinct indices are prematurely missed.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^3)$ Time, $O(N)$ Space):** Three nested loops with a `Set<List<Integer>>` to eliminate duplicate triplet combinations.
  - **2. Better Approach ($O(N^2)$ Time, $O(N)$ Space):** Fix one element and use a HashSet to find Two-Sum complements, using custom sorting/set hashing to deduplicate.
  - **3. Optimal Approach ($O(N^2)$ Time, $O(1)$ Extra Space):** Sort the array first. Loop index $i$ from 0 to $N-3$. If $i > 0$ and `nums[i] == nums[i-1]`, skip to avoid duplicates. For each fixed $i$, set `left = i + 1` and `right = N - 1` and run two-pointer convergence. When a triplet is found, advance `left` past identical elements and decrement `right` past identical elements.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Trapping Rain Water (Difficulty: Hard)
- **Pattern Category:** Pattern 1: Converging Two Pointers with Prefix Max
- **Problem Statement & Constraints:** Given $N$ non-negative integers representing an elevation map where the width of each bar is 1 ($1 \le N \le 2 \times 10^4$, $0 \le \text{height}[i] \le 10^5$), compute how much water it can trap after raining.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]` $\to$ Output: `6`.  
    *Explanation:* Water trapped in hollows between peaks at indices 1, 3, and 7 totals 6 units.
  - *Test Case 2 (Tricky / Boundary):* `height = [3, 2, 1, 0]` or `[0, 1, 2, 3]` $\to$ Output: `0`.  
    *Explanation:* Monotonically decreasing or increasing slopes have no valley to retain water.
  - *Test Case 3 (Failure Trap):* `height = [4, 2, 3]` $\to$ Output: `1`.  
    *Explanation:* Water at index 1 is trapped by left wall (4) and right wall (3). Water level = $\min(4, 3) - 2 = 1$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** For every index $i$, scan left to find `maxLeft` and scan right to find `maxRight`. Trapped water at $i = \max(0, \min(maxLeft, maxRight) - height[i])$.
  - **2. Better Approach ($O(N)$ Time, $O(N)$ Space):** Precompute `leftMax[i]` array and `rightMax[i]` array using two passes, then compute water in a third pass.
  - **3. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Two pointers `left = 0`, `right = N - 1` with variables `leftMax = 0`, `rightMax = 0`. Process whichever boundary has the smaller height: if `height[left] <= height[right]`, then water at `left` is strictly bounded by `leftMax` (since `height[right]` guarantees a wall at least as tall exists to the right). Update `leftMax` and accumulate `leftMax - height[left]`, then `left++`. Symmetrically process `right`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Move Zeroes In-Place (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Same-Direction Slow / Fast Two Pointers
- **Problem Statement & Constraints:** Given an integer array `nums` of size $N$ ($1 \le N \le 10^4$, $-2^{31} \le \text{nums}[i] \le 2^{31} - 1$), move all 0's to the end of it while maintaining the relative order of the non-zero elements in-place without copying the array.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [0, 1, 0, 3, 12]` $\to$ Output: `[1, 3, 12, 0, 0]`.  
    *Explanation:* Non-zero elements retain original order (1, 3, 12), and the two zeroes shift to indices 3 and 4.
  - *Test Case 2 (Tricky / Boundary):* `nums = [0]` or `nums = [1, 2, 3]` $\to$ Output: unchanged.  
    *Explanation:* Array with all zeroes or no zeroes should exit without out-of-bounds error.
  - *Test Case 3 (Failure Trap):* `nums = [0, 0, 0, 1]` $\to$ Output: `[1, 0, 0, 0]`.  
    *Explanation:* Tests that consecutive multiple zeroes don't trap the write pointer.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N)$ Time, $O(N)$ Space):** Copy non-zero elements into a new array, then pad remaining slots with zeroes.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Maintain write pointer `slow = 0`. Loop `fast` from 0 to $N-1$. Whenever `nums[fast] != 0`, swap `nums[slow]` and `nums[fast]` (or assign and zero-out), then increment `slow`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Remove Duplicates from Sorted Array II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Same-Direction Pointers with K-Lookback
- **Problem Statement & Constraints:** Given an integer array `nums` sorted in non-decreasing order ($1 \le N \le 3 \times 10^4$), remove some duplicates in-place such that each unique element appears **at most twice**. The relative order of the elements should be kept the same. Return the new length $K$.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 1, 1, 2, 2, 3]` $\to$ Output: `5, nums = [1, 1, 2, 2, 3, _]`.  
    *Explanation:* The third 1 is dropped; 2 appears twice; 3 appears once.
  - *Test Case 2 (Tricky / Boundary):* `nums = [1, 2, 3]` $\to$ Output: `3, nums = [1, 2, 3]`.  
    *Explanation:* No element exceeds frequency 2; array remains identical.
  - *Test Case 3 (Failure Trap):* `nums = [0, 0, 1, 1, 1, 1, 2, 3, 3]` $\to$ Output: `7, nums = [0, 0, 1, 1, 2, 3, 3, _, _]`.  
    *Explanation:* Quadruple duplicate must be reduced to exact pair.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N)$ Time, $O(N)$ Space):** Use a LinkedHashMap to count frequencies up to 2 and rewrite.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** For allowing at most $K$ duplicates (here $K=2$), the first $K$ elements are always valid. Start index `slow = 2`. Iterate `fast` from 2 to $N-1$. Compare `nums[fast]` not with `nums[fast-1]`, but with `nums[slow - 2]`. If `nums[fast] != nums[slow - 2]`, write `nums[slow++] = nums[fast]`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Two Sum with Less Than K (Difficulty: Easy-Medium)
- **Pattern Category:** Pattern 1: Sorting + Two Pointers Bounded Search
- **Problem Statement & Constraints:** Given an array `nums` of integers ($1 \le N \le 10^4$) and an integer `K`, return the maximum sum `S` such that there exists $i < j$ with `nums[i] + nums[j] = S` and $S < K$. If no such $i, j$ exists, return -1.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [34, 23, 1, 24, 75, 33, 54, 8]`, `K = 60` $\to$ Output: `58`.  
    *Explanation:* Pair 34 and 24 sums to 58, which is the closest sum strictly less than 60.
  - *Test Case 2 (Tricky / Boundary):* `nums = [10, 20, 30]`, `K = 15` $\to$ Output: `-1`.  
    *Explanation:* Smallest possible sum is $10 + 20 = 30$, which is not strictly less than 15.
  - *Test Case 3 (Failure Trap):* `nums = [30, 30]`, `K = 60` $\to$ Output: `-1`.  
    *Explanation:* Sum is 60, but constraint demands strictly less than $K$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Nested pairs, tracking max sum $< K$.
  - **2. Optimal Approach ($O(N \log N)$ Time, $O(1)$ Space):** Sort array. Pointers `left = 0`, `right = N - 1`. If `nums[left] + nums[right] < K`, this is a candidate answer: update `maxSum = max(maxSum, nums[left] + nums[right])`, and increment `left` to search for an even larger sum. Otherwise (`sum >= K`), decrement `right`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Fixed & Variable Sliding Window

---

### Problem #7: Maximum Sum of Distinct Subarrays with Length K (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Fixed Window + Frequency Map / Set
- **Problem Statement & Constraints:** Given an integer array `nums` of size $N$ ($1 \le K \le N \le 10^5$, $1 \le \text{nums}[i] \le 10^5$), find the maximum subarray sum of all subarrays of `nums` that meet the following conditions: the length of the subarray is $K$, and all elements of the subarray are distinct. Return 0 if none exists.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 5, 4, 2, 9, 9, 9]`, `K = 3` $\to$ Output: `15`.  
    *Explanation:* Subarray `[4, 2, 9]` has length 3, distinct elements, and sums to 15.
  - *Test Case 2 (Tricky / Boundary):* `nums = [4, 4, 4]`, `K = 3` $\to$ Output: `0`.  
    *Explanation:* All elements are duplicate; no valid distinct window of size 3 exists.
  - *Test Case 3 (Failure Trap):* `nums = [1, 2, 2], K = 2` $\to$ Output: `3`.  
    *Explanation:* First window `[1, 2]` is valid (sum 3), second window `[2, 2]` is invalid.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N \times K)$ Time, $O(K)$ Space):** Evaluate all $N - K + 1$ windows, inserting into a HashSet to check for size $K$.
  - **2. Optimal Approach ($O(N)$ Time, $O(K)$ Space):** Maintain running `windowSum` and a `Map<Integer, Integer>` (or frequency array) of element counts inside the window. For `right` from 0 to $N-1$, add `nums[right]`. Once `right >= K`, subtract `nums[right - K]` and remove from map if frequency becomes 0. If `map.size() == K`, update `maxSum = max(maxSum, windowSum)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Minimum Size Subarray Sum (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Variable Window (Dynamic Shrink on Validity)
- **Problem Statement & Constraints:** Given an array of positive integers `nums` ($1 \le N \le 10^5$) and a positive integer `target`, return the minimal length of a contiguous subarray of which the sum is greater than or equal to `target`. If there is no such subarray, return 0.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `target = 7`, `nums = [2, 3, 1, 2, 4, 3]` $\to$ Output: `2`.  
    *Explanation:* Subarray `[4, 3]` has minimal length 2 with sum 7.
  - *Test Case 2 (Tricky / Boundary):* `target = 100`, `nums = [1, 2, 3]` $\to$ Output: `0`.  
    *Explanation:* Total array sum is 6 $< 100$; impossible.
  - *Test Case 3 (Failure Trap):* `target = 11`, `nums = [1, 2, 3, 4, 5]` $\to$ Output: `3`.  
    *Explanation:* `[3, 4, 5]` sums to 12 $\ge 11$. Shrinking from left must verify that smaller window `[4, 5]` (sum 9) is invalid before recording length 3.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Two nested loops calculating running sum until $\ge target$.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Two pointers `left = 0`, `currentSum = 0`, `minLen = infinity`. Expand `right` across the array adding `nums[right]`. Whenever `currentSum >= target`, record `minLen = min(minLen, right - left + 1)`, subtract `nums[left]`, and increment `left` inside a `while` loop until sum falls below `target`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Longest Substring with At Most K Distinct Characters (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Variable Window with Character Frequency Map
- **Problem Statement & Constraints:** Given a string `s` and an integer `k` ($1 \le s.length \le 5 \times 10^4$, $0 \le k \le 50$), return the length of the longest substring of `s` that contains at most `k` distinct characters.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `s = "eceba"`, `k = 2` $\to$ Output: `3`.  
    *Explanation:* Substring `"ece"` contains 2 distinct characters ('e', 'c') and has length 3.
  - *Test Case 2 (Tricky / Boundary):* `s = "aa"`, `k = 0` $\to$ Output: `0`.  
    *Explanation:* With $k=0$, no characters are allowed; max length is 0.
  - *Test Case 3 (Failure Trap):* `s = "abaccc"`, `k = 2` $\to$ Output: `4`.  
    *Explanation:* `"accc"` contains 2 distinct characters ('a', 'c') with length 4.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(N)$ Space):** Generate substrings and count distinct chars using HashSet.
  - **2. Optimal Approach ($O(N)$ Time, $O(K)$ Space):** Sliding window `[left, right]` and a `Map<Character, Integer>` for frequencies. As `right` advances, put `s.charAt(right)`. If `map.size() > k`, shrink `left`: decrement count of `s.charAt(left)` and remove key when count hits 0. Update `maxLen = max(maxLen, right - left + 1)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Max Consecutive Ones III (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Variable Window with Flip Budget
- **Problem Statement & Constraints:** Given a binary array `nums` and an integer `k` ($1 \le N \le 10^5$, $0 \le k \le N$), return the maximum number of consecutive 1's in the array if you can flip at most `k` 0's.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0]`, `k = 2` $\to$ Output: `6`.  
    *Explanation:* Flipping 0s at indices 5 and 10 produces `[1, 1, 1, 1, 1, 1]` from index 5 to 10.
  - *Test Case 2 (Tricky / Boundary):* `nums = [0, 0, 0]`, `k = 0` $\to$ Output: `0`.  
    *Explanation:* Cannot flip any zero; max consecutive ones is 0.
  - *Test Case 3 (Failure Trap):* `nums = [0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1]`, `k = 3` $\to$ Output: `10`.  
    *Explanation:* Must not greedily consume $K$ on initial zeroes if a longer contiguous streak follows.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Check every subarray and count if number of zeroes $\le k$.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Translate problem: *"Find the longest subarray containing at most $k$ zeroes"*. Maintain variable `zeroCount`. Loop `right` from 0 to $N-1$. If `nums[right] == 0`, increment `zeroCount`. While `zeroCount > k`, if `nums[left] == 0`, decrement `zeroCount`, and increment `left`. Record `maxLen = max(maxLen, right - left + 1)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Fruit Into Baskets (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Variable Window (At Most 2 Types)
- **Problem Statement & Constraints:** You visit a farm with trees in a row represented by `fruits[i]` ($1 \le N \le 10^5$). You have two baskets, and each basket can only hold a single type of fruit. Return the maximum total number of fruits you can pick in a contiguous slice.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `fruits = [1, 2, 1]` $\to$ Output: `3`.  
    *Explanation:* Both types 1 and 2 can be collected across all 3 trees.
  - *Test Case 2 (Tricky / Boundary):* `fruits = [0, 1, 2, 2, 2, 2]` $\to$ Output: `5`.  
    *Explanation:* Skip the first tree of type 0 to take `[1, 2, 2, 2, 2]`.
  - *Test Case 3 (Failure Trap):* `fruits = [1, 2, 3, 2, 2]` $\to$ Output: `4`.  
    *Explanation:* Dropping type 1 leaves `[2, 3, 2, 2]` of length 4.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Check every starting tree and extend until a 3rd distinct fruit type appears.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Equivalent to: *"Longest subarray with at most 2 distinct integers"*. Maintain frequency map of at most 2 keys. Expand `right`. If map size exceeds 2, decrement count of `fruits[left]` until removed from map, incrementing `left`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Subarrays with K Different Integers (Difficulty: Hard)
- **Pattern Category:** Pattern 2: Exact Window via Difference of At-Most Windows
- **Problem Statement & Constraints:** Given an integer array `nums` and an integer `k` ($1 \le N \le 2 \times 10^4$, $1 \le k \le N$), return the number of good subarrays of `nums`. A good subarray has **exactly** `k` different integers.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 2, 1, 2, 3]`, `k = 2` $\to$ Output: `7`.  
    *Explanation:* Subarrays with exactly 2 distinct integers: `[1,2]`, `[2,1]`, `[1,2]`, `[2,3]`, `[1,2,1]`, `[2,1,2]`, `[1,2,1,2]`.
  - *Test Case 2 (Tricky / Boundary):* `nums = [1, 2, 1, 3, 4]`, `k = 3` $\to$ Output: `3`.  
    *Explanation:* `[1, 2, 1, 3]`, `[2, 1, 3]`, `[1, 3, 4]`.
  - *Test Case 3 (Failure Trap):* `nums = [1, 1, 1]`, `k = 1` $\to$ Output: `6`.  
    *Explanation:* Every single contiguous slice has 1 distinct integer: $3 \times 4 / 2 = 6$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(N)$ Space):** Nested loops maintaining a distinct element counter.
  - **2. The "Aha!" Decomposition:** Sliding window directly for *exactly* $K$ is tricky because shrinking `left` can break both ways. However, counting subarrays with *at most* $K$ distinct elements is straightforward:
    $$\text{Exact}(K) = \text{AtMost}(K) - \text{AtMost}(K - 1)$$
  - **3. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Implement helper `atMostKDistinct(nums, k)`. For each `right`, expand window; while distinct count $> k$, shrink `left`. The number of valid subarrays ending at `right` is simply `right - left + 1`. Call helper twice and subtract.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Prefix Sum + HashMap Lookups & Offsets

---

### Problem #13: Longest Subarray with Sum Equals K (Positives & Negatives) (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Prefix Sum + HashMap First-Occurrence
- **Problem Statement & Constraints:** Given an integer array `nums` of size $N$ ($1 \le N \le 10^5$, $-10^4 \le \text{nums}[i] \le 10^4$) and an integer $K$, return the length of the longest contiguous subarray that sums to $K$. Return 0 if none exists.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [10, 5, 2, 7, 1, 9]`, `K = 15` $\to$ Output: `4`.  
    *Explanation:* Subarray `[5, 2, 7, 1]` sums to 15 with length 4.
  - *Test Case 2 (Tricky / Boundary):* `nums = [-1, 1, 1]`, `K = 1` $\to$ Output: `3`.  
    *Explanation:* Entire array `[-1, 1, 1]` sums to 1.
  - *Test Case 3 (Failure Trap):* `nums = [2, 0, 0, 3]`, `K = 3` $\to$ Output: `3`.  
    *Explanation:* If you overwrite the earliest index of prefix sum 2 in the map when encountering zeroes, you will mistakenly shrink the answer to `[3]` (length 1).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Double loop tracking running sum and recording longest length when sum == $K$.
  - **2. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Track `currentPrefixSum`. Maintain `Map<Integer, Integer>` storing `(prefixSum -> earliestSeenIndex)`. Base condition: `map.put(0, -1)`. If `currentPrefixSum - K` exists in map, candidate length is `i - map.get(currentPrefixSum - K)`. **Crucial:** Only store `currentPrefixSum` if it is NOT already present (`putIfAbsent`) to maximize subarray span!
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Continuous Subarray Sum (Multiple of K) (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Prefix Sum Modulo Arithmetic + Index Distance
- **Problem Statement & Constraints:** Given an integer array `nums` ($1 \le N \le 10^5$, $0 \le \text{nums}[i] \le 10^9$) and an integer `k` ($1 \le k \le 2^{31} - 1$), return `true` if `nums` has a good subarray of length at least 2 whose sum is a multiple of `k`.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [23, 2, 4, 6, 7]`, `k = 6` $\to$ Output: `true`.  
    *Explanation:* Subarray `[2, 4]` sums to 6, a multiple of 6.
  - *Test Case 2 (Tricky / Boundary):* `nums = [23, 2, 6, 4, 7]`, `k = 13` $\to$ Output: `false`.  
    *Explanation:* No contiguous subarray of length $\ge 2$ sums to a multiple of 13.
  - *Test Case 3 (Failure Trap):* `nums = [5, 0, 0, 0]`, `k = 3` $\to$ Output: `true`.  
    *Explanation:* `[0, 0]` has length 2 and sum $0 = 0 \times 3$, which is a valid multiple of 3.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Check sum of every pair $(i, j)$ where $j - i \ge 1$ and check `sum % k == 0`.
  - **2. Optimal Approach ($O(N)$ Time, $O(\min(N, K))$ Space):** By modulo arithmetic: if $\text{prefixSum}[j] \pmod K == \text{prefixSum}[i] \pmod K$, then the subarray between $i$ and $j$ has a sum divisible by $K$! Store `(remainder -> earliestIndex)` in a HashMap. Initialize `map.put(0, -1)`. If `rem` exists in map and `currentIndex - map.get(rem) >= 2`, return `true`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Contiguous Array (Equal 0s and 1s) (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Transformation to Zero-Sum Subarray
- **Problem Statement & Constraints:** Given a binary array `nums` ($1 \le N \le 10^5$), find the maximum length of a contiguous subarray with an equal number of 0 and 1.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [0, 1]` $\to$ Output: `2`.  
    *Explanation:* Entire array has one 0 and one 1.
  - *Test Case 2 (Tricky / Boundary):* `nums = [0, 1, 0]` $\to$ Output: `2`.  
    *Explanation:* `[0, 1]` or `[1, 0]` both have length 2.
  - *Test Case 3 (Failure Trap):* `nums = [0, 0, 1, 0, 0, 0, 1, 1]` $\to$ Output: `6`.  
    *Explanation:* Subarray from index 2 to 7 (`[1, 0, 0, 0, 1, 1]`) has three 0s and three 1s.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Check count of 0s and 1s for all subarrays.
  - **2. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** **Transformation Trick:** Treat every `0` as `-1` and every `1` as `+1`. The problem reduces identically to: *"Find the longest subarray with sum equal to 0"*. Use prefix sum map with `map.put(0, -1)` and `putIfAbsent`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Subarray Sums Divisible by K (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Prefix Modulo Frequency Counting
- **Problem Statement & Constraints:** Given an integer array `nums` ($1 \le N \le 3 \times 10^4$, $-10^4 \le \text{nums}[i] \le 10^4$) and an integer `k`, return the number of non-empty subarrays that have a sum divisible by `k`.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [4, 5, 0, -2, -3, 1]`, `k = 5` $\to$ Output: `7`.  
    *Explanation:* 7 distinct subarrays sum to multiples of 5.
  - *Test Case 2 (Tricky / Boundary):* `nums = [5]`, `k = 9` $\to$ Output: `0`.  
    *Explanation:* Single element 5 is not divisible by 9.
  - *Test Case 3 (Failure Trap):* `nums = [-1, 2, 9]`, `k = 2` $\to$ Output: `2`.  
    *Explanation:* Negative modulo trap: in Java `-1 % 2 = -1`. Must normalize negative remainders to positive range `(rem % k + k) % k`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Double loop with running sum modulo $k$.
  - **2. Optimal Approach ($O(N + K)$ Time, $O(K)$ Space):** Maintain frequency array `remCount[k]`. Set `remCount[0] = 1`. For each element, accumulate `prefixSum`, normalize remainder: `rem = ((prefixSum % k) + k) % k`. Add current frequency `remCount[rem]` to total answer, then increment `remCount[rem]++`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Product of Array Except Self (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Prefix & Suffix Products Without Division
- **Problem Statement & Constraints:** Given an integer array `nums` of size $N$ ($2 \le N \le 10^5$, $-30 \le \text{nums}[i] \le 30$), return an array `answer` such that `answer[i]` is equal to the product of all elements of `nums` except `nums[i]`. You must write an algorithm that runs in $O(N)$ time and **without using the division operation**.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 2, 3, 4]` $\to$ Output: `[24, 12, 8, 6]`.  
    *Explanation:* For index 0: $2 \times 3 \times 4 = 24$; for index 1: $1 \times 3 \times 4 = 12$, etc.
  - *Test Case 2 (Tricky / Boundary):* `nums = [-1, 1, 0, -3, 3]` $\to$ Output: `[0, 0, 9, 0, 0]`.  
    *Explanation:* Zero at index 2 makes all other positions 0; index 2 becomes $(-1)(1)(-3)(3) = 9$.
  - *Test Case 3 (Failure Trap):* `nums = [0, 0]` $\to$ Output: `[0, 0]`.  
    *Explanation:* Multiple zeroes yield all zeroes in result.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Nested loop computing product of all $j \ne i$.
  - **2. Better Approach ($O(N)$ Time, $O(N)$ Space):** Precompute `prefixProduct[i]` and `suffixProduct[i]` in two separate arrays.
  - **3. Optimal Approach ($O(N)$ Time, $O(1)$ Extra Space):** Fill `result[i]` with prefix product of elements to its left. Then run a backward pass maintaining scalar variable `suffix = 1`, multiplying `result[i] *= suffix` and updating `suffix *= nums[i]`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Find Pivot Index (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Total Sum - Prefix Sum Balance
- **Problem Statement & Constraints:** Given an array of integers `nums` ($1 \le N \le 10^4$), calculate the pivot index where the sum of all numbers strictly to the left equals the sum of all numbers strictly to the right. Return leftmost pivot or -1.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 7, 3, 6, 5, 6]` $\to$ Output: `3`.  
    *Explanation:* Left sum = $1 + 7 + 3 = 11$. Right sum = $5 + 6 = 11$.
  - *Test Case 2 (Tricky / Boundary):* `nums = [2, 1, -1]` $\to$ Output: `0`.  
    *Explanation:* Left sum at index 0 is 0; right sum is $1 + (-1) = 0$.
  - *Test Case 3 (Failure Trap):* `nums = [1, 2, 3]` $\to$ Output: `-1`.  
    *Explanation:* No balance point exists.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** For each index, calculate left sum and right sum using inner loops.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Precompute `totalSum`. Maintain `leftSum = 0`. For each element $x$: `rightSum = totalSum - leftSum - x`. If `leftSum == rightSum`, return index immediately. Otherwise `leftSum += x`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Single-Pass Invariants & State Tracking

---

### Problem #19: Maximum Subarray (Kadane's Core) (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Single-Pass Dynamic Accumulator
- **Problem Statement & Constraints:** Given an integer array `nums` ($1 \le N \le 10^5$, $-10^4 \le \text{nums}[i] \le 10^4$), find the subarray with the largest sum and return its sum.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]` $\to$ Output: `6`.  
    *Explanation:* Subarray `[4, -1, 2, 1]` has the largest sum 6.
  - *Test Case 2 (Tricky / Boundary):* `nums = [-1]` $\to$ Output: `-1`.  
    *Explanation:* Single negative element must return itself, not 0.
  - *Test Case 3 (Failure Trap):* `nums = [-5, -2, -8, -1]` $\to$ Output: `-1`.  
    *Explanation:* When all numbers are negative, the least negative number is the answer.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Evaluate all subarray sums.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** State variables `currSum = nums[0]`, `maxSum = nums[0]`. Iterate from index 1: `currSum = max(nums[i], currSum + nums[i])`, `maxSum = max(maxSum, currSum)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Maximum Product Subarray (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Dual Min/Max State Tracker (Negative Flipping)
- **Problem Statement & Constraints:** Given an integer array `nums` ($1 \le N \le 2 \times 10^4$, $-10 \le \text{nums}[i] \le 10$), find a contiguous non-empty subarray that has the largest product, and return the product.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [2, 3, -2, 4]` $\to$ Output: `6`.  
    *Explanation:* `[2, 3]` has the largest product 6.
  - *Test Case 2 (Tricky / Boundary):* `nums = [-2, 0, -1]` $\to$ Output: `0`.  
    *Explanation:* Subarray `[0]` yields product 0.
  - *Test Case 3 (Failure Trap):* `nums = [-2, 3, -4]` $\to$ Output: `24`.  
    *Explanation:* Multiplying negative by negative yields positive: $(-2) \times 3 \times (-4) = 24$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Two nested loops calculating products.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** A negative number turns the minimum product into the maximum product! Maintain both `maxProd` and `minProd`. When `nums[i] < 0`, swap `maxProd` and `minProd`. Update `maxProd = max(nums[i], maxProd * nums[i])` and `minProd = min(nums[i], minProd * nums[i])`. Track global maximum.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Sort Colors (Dutch National Flag) (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 3-Way In-Place Partitioning
- **Problem Statement & Constraints:** Given an array `nums` with $N$ objects colored red, white, or blue ($1 \le N \le 300$), sort them in-place so that objects of the same color are adjacent, with the colors in the order 0 (red), 1 (white), and 2 (blue). Do not use the library sort function.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [2, 0, 2, 1, 1, 0]` $\to$ Output: `[0, 0, 1, 1, 2, 2]`.
  - *Test Case 2 (Tricky / Boundary):* `nums = [2, 0, 1]` $\to$ Output: `[0, 1, 2]`.
  - *Test Case 3 (Failure Trap):* `nums = [1, 2, 0]` $\to$ Output: `[0, 1, 2]`. Tests proper pointer boundary conditions.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N)$ Time, 2-Pass):** Count frequencies of 0, 1, 2, then overwrite array.
  - **2. Optimal Approach ($O(N)$ Time, 1-Pass, $O(1)$ Space):** Pointers `low = 0`, `mid = 0`, `high = N - 1`. While `mid <= high`: if `nums[mid] == 0`, swap `mid` and `low`, `low++`, `mid++`. If `nums[mid] == 1`, `mid++`. If `nums[mid] == 2`, swap `mid` and `high`, `high--` (do NOT increment `mid`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Best Time to Buy and Sell Stock (Difficulty: Easy)
- **Pattern Category:** Pattern 4: Running Minimum Valley Tracking
- **Problem Statement & Constraints:** You are given an array `prices` where `prices[i]` is the price of a given stock on the $i$-th day ($1 \le N \le 10^5$). Choose a single day to buy and a different day in the future to sell. Return the maximum profit. If you cannot achieve any profit, return 0.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `prices = [7, 1, 5, 3, 6, 4]` $\to$ Output: `5`.  
    *Explanation:* Buy on day 2 (price 1) and sell on day 5 (price 6), profit = $6 - 1 = 5$.
  - *Test Case 2 (Tricky / Boundary):* `prices = [7, 6, 4, 3, 1]` $\to$ Output: `0`.  
    *Explanation:* Prices decrease monotonically; no transaction yields profit.
  - *Test Case 3 (Failure Trap):* `prices = [2, 4, 1]` $\to$ Output: `2`.  
    *Explanation:* Cannot buy at 1 and sell at 4 because 1 occurs after 4!
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Check all pairs $(i, j)$ with $j > i$.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Maintain `minPriceSeenSoFar = Integer.MAX_VALUE` and `maxProfit = 0`. In a single pass: `minPriceSeenSoFar = min(minPriceSeenSoFar, price)`, then `maxProfit = max(maxProfit, price - minPriceSeenSoFar)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Majority Element ($> N/2$) (Difficulty: Easy-Medium)
- **Pattern Category:** Pattern 4: Boyer-Moore Voting Algorithm
- **Problem Statement & Constraints:** Given an array `nums` of size $N$ ($1 \le N \le 5 \times 10^4$), return the majority element that appears more than $\lfloor N / 2 \rfloor$ times.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [3, 2, 3]` $\to$ Output: `3`.
  - *Test Case 2 (Tricky / Boundary):* `nums = [2, 2, 1, 1, 1, 2, 2]` $\to$ Output: `2`.
  - *Test Case 3 (Failure Trap):* `nums = [1]` $\to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time, $O(1)$ Space):** Count each element frequency.
  - **2. Better Approach ($O(N)$ Time, $O(N)$ Space):** HashMap frequencies.
  - **3. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Boyer-Moore Voting: Maintain `candidate` and `count = 0`. Iterate: if `count == 0`, set `candidate = num`. If `num == candidate`, `count++`, else `count--`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Next Permutation (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Suffix Inversion & Swap Point
- **Problem Statement & Constraints:** Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers in-place with $O(1)$ extra memory ($1 \le N \le 100$). If no greater permutation exists, rearrange to lowest order (sorted ascending).
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [1, 2, 3]` $\to$ Output: `[1, 3, 2]`.
  - *Test Case 2 (Tricky / Boundary):* `nums = [3, 2, 1]` $\to$ Output: `[1, 2, 3]`.  
    *Explanation:* Highest possible permutation wraps around to lowest.
  - *Test Case 3 (Failure Trap):* `nums = [1, 1, 5]` $\to$ Output: `[1, 5, 1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal 3-Step Scan ($O(N)$ Time, $O(1)$ Space):**
    1. Scan right to left to find first index $i$ where `nums[i] < nums[i+1]` (the pivot point).
    2. If found, scan right to left again to find first element `nums[j] > nums[i]`, and swap them.
    3. Reverse the suffix `nums[i+1 .. N-1]` in-place to make it the smallest possible sequence.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 5: Binary Search on Monotonic Space & Rotated Arrays

---

### Problem #25: Search in Rotated Sorted Array (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Modified Binary Search with Half-Sorted Invariant
- **Problem Statement & Constraints:** Given an integer array `nums` sorted in ascending order with distinct values ($1 \le N \le 5000$), rotated at an unknown pivot, and an integer `target`, return the index of `target`, or -1 if not in `nums`.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [4, 5, 6, 7, 0, 1, 2]`, `target = 0` $\to$ Output: `4`.
  - *Test Case 2 (Tricky / Boundary):* `nums = [1]`, `target = 0` $\to$ Output: `-1`.
  - *Test Case 3 (Failure Trap):* `nums = [5, 1, 3]`, `target = 5` $\to$ Output: `0`. Pivot occurs right after target.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N)$ Time, $O(1)$ Space):** Linear scan.
  - **2. Optimal Approach ($O(\log N)$ Time, $O(1)$ Space):** At any `mid`, **at least one half of the array is strictly sorted!** Compare `nums[low]` and `nums[mid]`: if `nums[low] <= nums[mid]`, left half is sorted. Check if `target` lies within `[nums[low], nums[mid]]`. If so, search left (`high = mid - 1`), else search right. If right half is sorted, apply symmetric logic.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #26: Find Minimum in Rotated Sorted Array (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Convergence to Inflection Point
- **Problem Statement & Constraints:** Given a sorted rotated array of unique elements `nums` ($1 \le N \le 5000$), return the minimum element of this array in $O(\log N)$ time.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [3, 4, 5, 1, 2]` $\to$ Output: `1`.
  - *Test Case 2 (Tricky / Boundary):* `nums = [11, 13, 15, 17]` $\to$ Output: `11` (0 rotations).
  - *Test Case 3 (Failure Trap):* `nums = [2, 1]` $\to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log N)$ Time, $O(1)$ Space):** Use `while (low < high)`. Compare `nums[mid]` with `nums[high]`. If `nums[mid] > nums[high]`, the inflection point (minimum) must lie strictly to the right of `mid`, so `low = mid + 1`. Otherwise, minimum is at `mid` or to its left, so `high = mid`. Terminate when `low == high`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #27: Find First and Last Position of Element in Sorted Array (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Boundary Binary Search (Lower & Upper Bound)
- **Problem Statement & Constraints:** Given an array of integers `nums` sorted in non-decreasing order ($0 \le N \le 10^5$), find the starting and ending position of a given `target` value. Run in $O(\log N)$ time.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [5, 7, 7, 8, 8, 10]`, `target = 8` $\to$ Output: `[3, 4]`.
  - *Test Case 2 (Tricky / Boundary):* `nums = []`, `target = 0` $\to$ Output: `[-1, -1]`.
  - *Test Case 3 (Failure Trap):* `nums = [8, 8, 8, 8]`, `target = 8` $\to$ Output: `[0, 3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log N)$ Time, $O(1)$ Space):** Two binary searches. In search 1 (First occurrence): when `nums[mid] == target`, record answer and search left (`high = mid - 1`). In search 2 (Last occurrence): when `nums[mid] == target`, record answer and search right (`low = mid + 1`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #28: Koko Eating Bananas (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Binary Search on Monotonic Rate
- **Problem Statement & Constraints:** Given piles of bananas `piles[i]` ($1 \le N \le 10^4$, $1 \le \text{piles}[i] \le 10^9$) and hours $h$ ($N \le h \le 10^9$), return the minimum integer eating speed $k$ to eat all bananas within $h$ hours.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `piles = [3, 6, 7, 11]`, `h = 8` $\to$ Output: `4`.  
    *Explanation:* Speed 4 eats piles in: $\lceil 3/4 \rceil + \lceil 6/4 \rceil + \lceil 7/4 \rceil + \lceil 11/4 \rceil = 1 + 2 + 2 + 3 = 8 \le 8$ hours.
  - *Test Case 2 (Tricky / Boundary):* `piles = [30, 11, 23, 4, 20]`, `h = 5` $\to$ Output: `30`.  
    *Explanation:* When $h == N$, speed must equal the maximum pile size.
  - *Test Case 3 (Failure Trap):* `piles = [10^9]`, `h = 2` $\to$ Output: `500000000`. Tests integer overflow in total hours.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log(\max(\text{pile})))$ Time, $O(1)$ Space):** Range is `low = 1`, `high = max(piles)`. For `mid`, compute total hours using `(p + mid - 1) / mid` (integer ceiling) using `long` to avoid overflow. If total hours $\le h$, record answer and search smaller speed (`high = mid - 1`), else `low = mid + 1`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #29: Split Array Largest Sum / Painter's Partition (Difficulty: Hard)
- **Pattern Category:** Pattern 5: Binary Search on Answer Space (Partition Feasibility)
- **Problem Statement & Constraints:** Given an integer array `nums` ($1 \le N \le 1000$) and an integer `k` ($1 \le k \le \min(50, N)$), split `nums` into `k` non-empty subarrays such that the largest sum among these subarrays is **minimized**. Return the minimized largest sum.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums = [7, 2, 5, 10, 8]`, `k = 2` $\to$ Output: `18`.  
    *Explanation:* Split into `[7, 2, 5]` (sum 14) and `[10, 8]` (sum 18). Largest sum is 18.
  - *Test Case 2 (Tricky / Boundary):* `nums = [1, 2, 3, 4, 5]`, `k = 1` $\to$ Output: `15`.  
    *Explanation:* Total sum of all elements.
  - *Test Case 3 (Failure Trap):* `nums = [1, 4, 4]`, `k = 3` $\to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Better Approach ($O(k \times N^2)$ Time):** 2D Dynamic Programming.
  - **2. Optimal Approach ($O(N \log(\sum \text{nums}))$ Time, $O(1)$ Space):** Monotonic predicate: `low = max(nums)` (a partition must hold at least the largest single element), `high = sum(nums)`. For candidate sum `mid`: greedy check if array can be split into $\le k$ partitions without any partition exceeding `mid`. If feasible, `high = mid - 1`, else `low = mid + 1`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #30: Median of Two Sorted Arrays (Difficulty: Hard)
- **Pattern Category:** Pattern 5: Dual-Array Partition Binary Search
- **Problem Statement & Constraints:** Given two sorted arrays `nums1` and `nums2` of size $m$ and $n$ ($0 \le m, n \le 1000$, $m+n \ge 1$), return the median of the two sorted arrays in **$O(\log(\min(m, n)))$** runtime complexity.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* `nums1 = [1, 3]`, `nums2 = [2]` $\to$ Output: `2.0`.
  - *Test Case 2 (Tricky / Boundary):* `nums1 = [1, 2]`, `nums2 = [3, 4]` $\to$ Output: `2.5`.  
    *Explanation:* Merged: `[1, 2, 3, 4]`, median is $(2 + 3) / 2 = 2.5$.
  - *Test Case 3 (Failure Trap):* `nums1 = []`, `nums2 = [1]` $\to$ Output: `1.0`. Handles empty array partition.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(m + n)$ Time, $O(m + n)$ Space):** Merge both sorted arrays into one array and pick middle.
  - **2. Better Approach ($O(m + n)$ Time, $O(1)$ Space):** Two pointers counting until reaching median index without creating merged array.
  - **3. Optimal Approach ($O(\log(\min(m, n)))$ Time, $O(1)$ Space):** Always binary search on the shorter array (say `nums1` of size $m$). We want to partition both arrays such that the left partition contains $(m + n + 1) / 2$ elements and every element in the left partition $\le$ every element in the right partition:
    `partitionX = (low + high) / 2`, `partitionY = (m + n + 1) / 2 - partitionX`.
    Define `maxLeftX`, `minRightX`, `maxLeftY`, `minRightY` (using $\pm \infty$ for boundary edges).
    If `maxLeftX <= minRightY` and `maxLeftY <= minRightX`: Correct partition found! Return max of lefts (if odd total length) or average of max-left and min-right (if even).
    If `maxLeftX > minRightY`: Shift left (`high = partitionX - 1`). Otherwise shift right.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
