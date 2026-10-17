# Phase 6: Recursion & Backtracking — Self-Guided Problem Workbook (25 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.recursionandbacktracking`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Subsets & Combinations (Inclusion / Exclusion)

---

### Problem #1: Subsets I (Difficulty: Medium)
- **Pattern Category:** Pattern 1: $2^N$ Binary Decision Tree
- **Problem Statement & Constraints:** Given an integer array $nums$ of unique elements, return all possible subsets. $1 \le nums.length \le 10$.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $nums = [1,2,3] \to$ Returns all 8 subsets.
  - *Test Case 2 (Single Element):* $nums = [0] \to$ Returns `[[], [0]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot 2^N)$ Time, $O(N)$ Space):** At each step, snapshot current list into results. Loop from `start` to $N-1$: choose `nums[i]`, recurse with `start = i + 1`, un-choose `nums[i]`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Subsets II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Duplicate Pruning via Sorting
- **Problem Statement & Constraints:** Given array $nums$ that may contain duplicates, return all unique subsets.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,2,2] \to$ Returns 6 unique subsets.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot 2^N)$ Time, $O(N)$ Space):** Sort $nums$. Inside candidate loop, if `i > start && nums[i] == nums[i - 1]`, continue (skip duplicate branch).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Combination Sum I (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Unbounded Candidate Reuse
- **Problem Statement & Constraints:** Find combinations summing to $target$. Numbers can be used unlimited times.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $candidates = [2,3,6,7], target = 7 \to$ Output: `[[2,2,3],[7]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(2^{target})$ Time, $O(target)$ Space):** Sort candidates. If `candidates[i] > remain`, break. Recurse passing index `i` (not $i + 1$) to allow reuse.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Combination Sum II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Bounded Candidate Use with Duplicates
- **Problem Statement & Constraints:** Each number in $candidates$ may only be used once in combination. May contain duplicates.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $candidates = [10,1,2,7,6,1,5], target = 8 \to$ Unique combinations.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(2^N)$ Time, $O(N)$ Space):** Sort candidates. Combine duplicate check (`i > start && candidates[i] == candidates[i-1] continue`) with single use rule (recurse with `i + 1`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Combination Sum III (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Fixed Size $K$ and Range $[1, 9]$
- **Problem Statement & Constraints:** Find all valid combinations of $k$ numbers that sum up to $n$ using digits from 1 to 9.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $k = 3, n = 7 \to$ Output: `[[1,2,4]]`.
  - *Test Case 2 (Impossible):* $k = 3, n = 2 \to$ Output: `[]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\binom{9}{k})$ Time, $O(k)$ Space):** Recurse from digit $d = start \dots 9$. If `current.size() == k`, check `remain == 0`. Prune if `remain < 0` or remaining available digits insufficient to reach $k$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Combinations (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Fixed Length $K$ Selection
- **Problem Statement & Constraints:** Given two integers $n$ and $k$, return all combinations of $k$ numbers chosen from the range $[1, n]$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 4, k = 2 \to$ Output: `[[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach with Math Pruning ($O(\binom{n}{k})$ Time):** If `current.size() == k`, add to result. Loop $i$ from $start$ up to $n - (k - current.size()) + 1$ (prunes branches with too few remaining numbers).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Permutations & State Arrangements

---

### Problem #7: Permutations I (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Visited Array Permutation
- **Problem Statement & Constraints:** Return all possible permutations of distinct integers.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,2,3] \to$ Returns 6 permutations.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot N!)$ Time, $O(N)$ Space):** `boolean[] visited`. Loop $i \in [0, N-1]$: if `!visited[i]`, mark true, add to path, recurse, pop, unmark.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Permutations II (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Permutations with Duplicates
- **Problem Statement & Constraints:** Permutations of array that may contain duplicates.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,1,2] \to$ Output: `[[1,1,2],[1,2,1],[2,1,1]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot N!)$ Time, $O(N)$ Space):** Sort array. Skip duplicate if `i > 0 && nums[i] == nums[i-1] && !visited[i-1]` (ensures duplicates are only processed in left-to-right relative order).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Next Permutation (Difficulty: Medium)
- **Pattern Category:** Pattern 2: In-Place Lexicographical Transition
- **Problem Statement & Constraints:** Rearrange numbers into lexicographically next greater permutation in-place with $O(1)$ extra memory.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,2,3] \to [1,3,2]$.
  - *Test Case 2 (Descending):* $nums = [3,2,1] \to [1,2,3]$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):**
    - Step 1: Scan from right to find first decreasing element $i$ (`nums[i] < nums[i + 1]`).
    - Step 2: If found, scan from right to find first element $j$ where `nums[j] > nums[i]`, and swap `nums[i]` with `nums[j]`.
    - Step 3: Reverse the suffix from $i + 1$ to $N - 1$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Letter Combinations of a Phone Number (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Multi-Branch String Mapping
- **Problem Statement & Constraints:** Given digit string 2-9, return all possible letter combinations.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $digits = "23" \to$ Output: `["ad","ae","af","bd","be","bf","cd","ce","cf"]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(4^N)$ Time, $O(N)$ Space):** Phone keypad map. Recurse from digit index 0 to $N$. Loop over all letters mapped to current digit, append char, recurse, delete char.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Generate Parentheses (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Open/Close Counting Constraint
- **Problem Statement & Constraints:** Given $n$ pairs of parentheses, generate all combinations of well-formed parentheses.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 3 \to$ 5 combinations.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\frac{4^n}{\sqrt{n}})$ Time Catalan Number):** Maintain counts of `open` and `close`. Can add `'('` if `open < n`. Can add `')'` if `close < open`. If `current.length() == 2 * n`, record valid string.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Palindrome Partitioning (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Substring Palindrome Slicing
- **Problem Statement & Constraints:** Partition string $s$ such that every substring of partition is a palindrome. Return all possible partitions.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "aab" \to$ Output: `[["a","a","b"],["aa","b"]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot 2^N)$ Time, $O(N)$ Space):** Recurse on `start` index. Loop `end` from `start` to $N-1$: if substring `s[start...end]` is a palindrome, add to current path, recurse with `start = end + 1`, and backtrack.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Grid Backtracking & Mazes

---

### Problem #13: Word Search I (Difficulty: Medium)
- **Pattern Category:** Pattern 3: In-Place Cell Masking
- **Problem Statement & Constraints:** Check if word exists in 2D character grid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Word exists $\to$ `true`.
  - *Test Case 2 (Revisit Invalid):* Word requires reusing cell $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C \cdot 3^L)$ Time, $O(L)$ Space):** Find matching first character. DFS with in-place masking `board[r][c] = '#'`. Explore 4 directions. Backtrack `board[r][c] = originalChar`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Word Search II (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Prefix Trie + Grid DFS
- **Problem Statement & Constraints:** Find all words from a dictionary that exist in the grid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Dictionary of words matched on grid simultaneously.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C \cdot 3^L)$ Time, $O(\sum L)$ Trie Space):** Insert all words into a Prefix Trie. Run DFS from every grid cell: follow matching Trie edges. When reaching a word end, record word, and prune leaf node from Trie to avoid duplicate discoveries.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Unique Paths III (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Hamiltonian Path on Grid
- **Problem Statement & Constraints:** Return number of 4-directional walks from start to end that walk over every non-obstacle square exactly once.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Grid with obstacles and start/end cells.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(3^K)$ Time where $K$ is empty cells):** Count total walkable empty cells `emptyCount`. DFS from start: decrement `emptyCount`, mark cell as obstacle `-1`. When reaching end, if `emptyCount == -1`, valid path found! Backtrack cell and increment count.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Rat in a Maze (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Direction Path String Construction
- **Problem Statement & Constraints:** Find all paths from $(0, 0)$ to $(N-1, N-1)$ in binary maze in lexicographical order (`'D', 'L', 'R', 'U'`).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Open paths in maze.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(4^{N^2})$ Time, $O(N^2)$ Space):** Explore in alphabetical order: 'D', 'L', 'R', 'U'. Mark cell visited. Backtrack on return.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Flood Fill (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Connected Component Color Fill
- **Problem Statement & Constraints:** Perform flood fill from $(sr, sc)$ with new color.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Grid color update.
  - *Test Case 2 (Already New Color):* Input color equals target color $\to$ immediate return to avoid infinite recursion.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Stack):** Guard `image[sr][sc] == color`. Record original color. DFS 4 directions updating color.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Knight's Tour (Difficulty: Medium)
- **Pattern Category:** Pattern 3: 8-Move Backtracking / Warnsdorff's Heuristic
- **Problem Statement & Constraints:** Visit every square of an $N \times N$ chessboard exactly once using knight moves.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $N = 8 \to$ full path.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ with Warnsdorff):** 8 knight moves $(r \pm 2, c \pm 1)$ and $(r \pm 1, c \pm 2)$. Prioritize moves leading to cells with the fewest onward legal moves.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Constraint Satisfaction & Partitioning

---

### Problem #19: N-Queens I (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Diagonal Constraint Arrays
- **Problem Statement & Constraints:** Place $n$ non-attacking queens on $n \times n$ board. Return all configurations.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 4 \to$ 2 solutions.
  - *Test Case 2 (Impossible):* $n = 2 \to$ 0 solutions.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N!)$ Time, $O(N)$ Space):** Recurse row-by-row. Maintain boolean arrays: `cols[c]`, `diag1[r + c]`, `diag2[r - c + n]`. Place queen if all 3 are false, mark true, recurse, unmark.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: N-Queens II (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Bitmask N-Queens Counter
- **Problem Statement & Constraints:** Return total number of distinct solutions for $N$-Queens.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 4 \to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Bitmask Approach ($O(N!)$ Time, $O(N)$ Stack):** Track column, diagonal, and anti-diagonal occupations as 32-bit bitmasks `cols`, `d1`, `d2`. Available positions in row: `avail = ~(cols | d1 | d2) & ((1 << n) - 1)`. Extract lowest bit with `avail & (-avail)`, shift diagonals for next row (`(d1 | bit) << 1`, `(d2 | bit) >> 1`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Sudoku Solver (Difficulty: Hard)
- **Pattern Category:** Pattern 4: 9x9 Constraint Propagation
- **Problem Statement & Constraints:** Solve 9x9 Sudoku board in-place.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Valid solvable Sudoku grid.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(9^{81})$ Worst-Case, fast in practice):** Find next empty cell `'.'`. Try digits `'1'` to `'9'`: check validity in row, column, and $3 \times 3$ subgrid. If valid, set digit and recurse. If recursive call returns true, stop. Else backtrack cell to `'.'`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Partition to K Equal Sum Subsets (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Bucket-Filling Backtracking
- **Problem Statement & Constraints:** Partition array into $k$ non-empty subsets whose sums are all equal.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [4,3,2,3,5,2,1], k = 4 \to$ Output: `true` (target sum 5).
  - *Test Case 2 (Impossible):* Sum not divisible by $k \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(k \cdot 2^N)$ Time, $O(N)$ Space):** Check if `totalSum % k == 0`. Target is `totalSum / k`. Sort descending (larger numbers fail faster). Fill buckets one by one: when a bucket reaches `target`, recurse to fill next bucket from index 0. If current number fails to fill empty bucket, break immediately.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Matchsticks to Square (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 4-Bucket Equal Partition
- **Problem Statement & Constraints:** Can you form a square using all given matchsticks without breaking them?
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $matchsticks = [1,1,2,2,2] \to$ Output: `true` (perimeter 8, side 2).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(4^N)$ Time, $O(N)$ Space):** Equivalent to Partition to $K$ Equal Subsets with $k = 4$. Sort descending. Try placing each matchstick into one of the 4 side buckets.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Word Break II (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Backtracking with Memoization
- **Problem Statement & Constraints:** Add spaces in string $s$ to construct sentences where each word is in dictionary. Return all possible sentences.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"] \to$ 2 sentences.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2 + 2^N)$ Time, $O(2^N)$ Space):** Memoized DFS returning `List<String>` valid sentences for suffix `s[start...]`. For each prefix in dictionary: recurse on suffix, combine prefix with all suffix sentences, and cache result in `Map<Integer, List<String>>`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Restore IP Addresses (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 4-Segment Valid IP Slicing
- **Problem Statement & Constraints:** Return all possible valid IP addresses formed by inserting dots into $s$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "25525511135" \to$ `["255.255.11.135","255.255.111.35"]`.
  - *Test Case 2 (Leading Zero Trap):* $s = "0000" \to$ `["0.0.0.0"]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(3^4) = O(1)$ Time, $O(1)$ Space):** Recurse on `segment = 0..3` and string index. At each segment, take slices of length 1, 2, and 3. Reject slice if: (1) length $> 1$ and starts with `'0'` (no leading zeros), or (2) parsed integer $> 255$. When 4 valid segments consume entire string, assemble IP with dots.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
