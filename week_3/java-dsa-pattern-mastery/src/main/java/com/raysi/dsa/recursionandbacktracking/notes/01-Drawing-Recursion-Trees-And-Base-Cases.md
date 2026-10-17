# Phase 6: Recursion & Backtracking — Decision Trees, State Invariants & Pruning
> **Author:** raysi  
> **Package:** `com.raysi.dsa.recursionandbacktracking`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master recursion trees, combinatorial state spaces, branch-and-bound pruning, and zero-defect backtracking templates.

---

## 1. Recursion Mental Model & Memory Mechanics

### 1.1 The Decision Tree Architecture
Every backtracking algorithm explores a **State-Space Tree (Decision Tree)**:
- **Root Node:** Initial empty state (e.g., `[]`, `board` with no queens).
- **Edges / Branches:** The valid choices available at the current decision step.
- **Node Depth:** The current index or step in the sequence ($0 \dots N$).
- **Leaf Nodes:** Terminal states where base conditions trigger:
  - If valid: capture candidate solution into global results (`result.add(new ArrayList<>(current))`).
  - If invalid: dead end (prune and backtrack).

```
                             [] (Root)
                    /                   \
            Pick 1                         Non-Pick 1
             [1]                               []
          /       \                         /       \
      Pick 2     Non-Pick 2             Pick 2     Non-Pick 2
      [1, 2]        [1]                  [2]           []
     /      \      /   \                /   \         /   \
  [1,2,3]  [1,2] [1,3] [1]            [2,3] [2]      [3]   []
```

---

### 1.2 The 3-Step Backtracking Invariant (Choose -> Explore -> Un-Choose)
Backtracking is essentially Depth-First Search over an implicit state graph.
To avoid allocating a new collection on every recursive call, we reuse a single mutable state object `current`:

```java
// 1. CHOOSE: Commit choice into mutable state
current.add(candidate);

// 2. EXPLORE: Recurse to deeper decision level
backtrack(nextState, current, result);

// 3. UN-CHOOSE (BACKTRACK): Cleanly undo choice before sibling branches execute
current.remove(current.size() - 1);
```

#### The Classic JVM Reference Bug:
```java
// DISASTER: Adds the reference! At the end of algorithm, result contains [ [], [], [] ... ]
result.add(current);

// CORRECT: Deep clone / snapshot the state at this leaf node
result.add(new ArrayList<>(current));
```

---

### 1.3 The Deduplication Mathematical Rule: `i > start` vs `i > 0`
When the input contains duplicate numbers (e.g., `[1, 2, 2]`):
1. Always **sort** the array first: `Arrays.sort(nums)`.
2. To avoid duplicate subsets, skip duplicates at the **same horizontal tree level**:
```java
if (i > start && nums[i] == nums[i - 1]) {
    continue; // Prune duplicate sibling branch
}
```

#### Why `i > start` and NOT `i > 0`?
- `i == start` is the **very first candidate** considered at this recursion depth. It is legal to pick a duplicate number vertically (e.g., picking the second `2` to make `[1, 2, 2]`).
- `i > start` means we are picking an alternative candidate for the **same slot**. If it matches `nums[i - 1]`, it generates an identical subtree that was already fully explored by the first branch!
- Writing `i > 0` kills vertical duplicates, making it impossible to form combinations containing multiple identical elements!

---

### 1.4 Constraints to Big-O Table for Backtracking

| Input Size ($N$) | Permissible Big-O | Problem Type |
| :--- | :--- | :--- |
| $N \le 10$ | $O(N!)$ | Permutations, Traveling Salesperson |
| $N \le 20$ | $O(2^N)$ | Subsets, Subsequence Sum, Power Set |
| $N \le 30$ | $O(3^N)$ or $O(4^N)$ | Grid DFS, Word Search, N-Queens |
| $N \le 9 \times 9$ | Constraint Propagation | Sudoku Solver |

---

## 2. The 4 Core Backtracking Patterns

```
                                  +---------------------------+
                                  | Backtracking Decision Tree|
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: Subsets| | Pattern 2: Comb.  | | Pattern 3: Permu- | | Pattern 4: Grid   |
| (Pick / Non-Pick) | | Sum (Reusable)    | | tations (Order)   | | & Constraint Walk |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  Order irrelevant      Unbounded reuse:      Order matters         N-Queens diagonals  
  start index moves     pass index 'i'        boolean[] visited     Word search '#' mask
  ahead: i + 1          Bounded: i + 1        In-place swaps        Sudoku 3x3 boxes    
```

---

### Pattern 1: Subsets (Combinations without Duplicates)
- **Trigger Keywords:** `"subsets"`, `"combinations"`, `"power set"`.
- **Template:**
```java
private void backtrack(int start, int[] nums, List<Integer> current, List<List<Integer>> result) {
    result.add(new ArrayList<>(current));
    for (int i = start; i < nums.length; i++) {
        if (i > start && nums[i] == nums[i - 1]) continue; // if duplicates present
        current.add(nums[i]);
        backtrack(i + 1, nums, current, result);
        current.remove(current.size() - 1);
    }
}
```

---

### Pattern 2: Combination Sum (Target Depletion & Element Reuse)
- **Trigger Keywords:** `"combination sum"`, `"target sum"`, `"reusable elements"`.
- **Mental Model:** Sort array. If `candidates[i] > remainingTarget`, `break` immediately (prune all larger candidates). To allow unlimited element reuse, pass current index $i$ to recursive call.

---

### Pattern 3: Permutations (Order-Dependent Sequences)
- **Trigger Keywords:** `"permutations"`, `"all arrangements"`.
- **Mental Model:** Since order matters, every recursion level iterates through **all indices from $0$ to $N - 1$**, using a `boolean[] visited` array to track which elements are active in `current`.

---

### Pattern 4: Grid Backtracking & Constraint Satisfaction
- **Trigger Keywords:** `"word search"`, `"N-Queens"`, `"Sudoku"`.
- **Mental Model:** In-place state masking. Mark cell as visited with sentinel character (e.g., `'#'`). Recurse into 4 directions. In the `finally`/after-step block, restore the original character.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Subsets II (LeetCode 90)
> **Problem:** Given an integer array $nums$ that may contain duplicates, return all possible subsets without duplicates.

#### 1. Brute Force Instinct
Generate all $2^N$ subsets and store in a `Set<List<Integer>>`.
*Cost:* $O(N \cdot 2^N)$ time plus heavy hashing overhead for lists.

#### 2. "Aha!" Observation
Sort $nums$ initially. Skip duplicate siblings using `if (i > start && nums[i] == nums[i - 1]) continue;`. Guarantees every generated subset is unique without any Set filtering.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.recursionandbacktracking;

import java.util.*;

public class SubsetsIIAnchor {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(0, nums, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int[] nums, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));

        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue;

            current.add(nums[i]);
            backtrack(i + 1, nums, current, result);
            current.remove(current.size() - 1);
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \cdot 2^N)$
- **Space Complexity:** $O(N)$ recursion depth.

---

### Anchor Problem #2: Combination Sum (LeetCode 39)
> **Problem:** Given an array of distinct integers $candidates$ and target integer $target$, return all unique combinations where the chosen numbers sum to $target$. An element may be chosen unlimited times.

#### 1. Brute Force Instinct
Exhaustive search with recursion depth up to $target$.

#### 2. "Aha!" Observation
Sort candidates ascending. At each step, if $candidates[i] > remain$, break (prunes all remaining branches). To permit infinite reuse, pass $i$ (instead of $i + 1$).

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.recursionandbacktracking;

import java.util.*;

public class CombinationSumAnchor {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(0, candidates, target, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int[] candidates, int remain, List<Integer> current, List<List<Integer>> result) {
        if (remain == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remain) break; // Pruning

            current.add(candidates[i]);
            backtrack(i, candidates, remain - candidates[i], current, result);
            current.remove(current.size() - 1);
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(2^{\text{target}})$
- **Space Complexity:** $O(\text{target} / \min(\text{candidate}))$.

---

### Anchor Problem #3: Permutations (LeetCode 46)
> **Problem:** Given an array $nums$ of distinct integers, return all possible permutations.

#### 1. Brute Force Instinct
Recursive generation checking `current.contains(nums[i])` $\implies O(N)$ linear lookup on every step.

#### 2. "Aha!" Observation
Use a `boolean[] visited` array for $O(1)$ availability checking. When `current.size() == nums.length`, a full permutation has been assembled.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.recursionandbacktracking;

import java.util.*;

public class PermutationsAnchor {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, visited, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] visited, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;

            visited[i] = true;
            current.add(nums[i]);

            backtrack(nums, visited, current, result);

            current.remove(current.size() - 1);
            visited[i] = false;
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \cdot N!)$
- **Space Complexity:** $O(N)$ for recursion stack and visited array.

---

### Anchor Problem #4: Word Search (LeetCode 79)
> **Problem:** Given an $m \times n$ grid of characters $board$ and a string $word$, return `true` if $word$ exists in the grid. Adjacent cells horizontally or vertically; no cell used more than once.

#### 1. Brute Force Instinct
Allocate a new $m \times n$ boolean visited array for each starting cell.
*Cost:* Frequent memory allocations and garbage collection pressure.

#### 2. "Aha!" Observation
In-place masking! Temporarily overwrite `board[r][c] = '#'` to mark visited. Recurse into 4 neighbors. Restore original character upon return (`board[r][c] = temp`). Zero auxiliary memory!

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.recursionandbacktracking;

public class WordSearchAnchor {
    public boolean exist(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == word.charAt(0)) {
                    if (dfs(board, word, 0, r, c, rows, cols)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int idx, int r, int c, int rows, int cols) {
        if (idx == word.length()) return true;
        if (r < 0 || r >= rows || c < 0 || c >= cols || board[r][c] != word.charAt(idx)) {
            return false;
        }

        char temp = board[r][c];
        board[r][c] = '#'; // Mark visited

        boolean found = dfs(board, word, idx + 1, r + 1, c, rows, cols)
                     || dfs(board, word, idx + 1, r - 1, c, rows, cols)
                     || dfs(board, word, idx + 1, r, c + 1, rows, cols)
                     || dfs(board, word, idx + 1, r, c - 1, rows, cols);

        board[r][c] = temp; // Un-choose (Backtrack)
        return found;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C \cdot 3^L)$ where $L$ is word length (3 branching choices after initial step).
- **Space Complexity:** $O(L)$ recursion stack.

---

### Anchor Problem #5: N-Queens (LeetCode 51)
> **Problem:** Place $n$ queens on an $n \times n$ chessboard such that no two queens attack each other. Return all distinct solutions.

#### 1. Brute Force Instinct
For every candidate placement $(r, c)$, scan all 8 directions across the entire board to check for attacking queens $\implies O(N)$ conflict check per placement.

#### 2. "Aha!" Observation
Mathematical $O(1)$ collision checks using 3 boolean arrays:
- Column occupied: `cols[c]`
- Anti-diagonal occupied: `diag1[r + c]`
- Main diagonal occupied: `diag2[r - c + n]`

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.recursionandbacktracking;

import java.util.*;

public class NQueensAnchor {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');

        boolean[] cols = new boolean[n];
        boolean[] diag1 = new boolean[2 * n];
        boolean[] diag2 = new boolean[2 * n];

        backtrack(0, n, board, cols, diag1, diag2, result);
        return result;
    }

    private void backtrack(int r, int n, char[][] board, boolean[] cols, boolean[] d1, boolean[] d2, List<List<String>> result) {
        if (r == n) {
            List<String> config = new ArrayList<>();
            for (char[] row : board) config.add(new String(row));
            result.add(config);
            return;
        }

        for (int c = 0; c < n; c++) {
            int id1 = r + c;
            int id2 = r - c + n;

            if (cols[c] || d1[id1] || d2[id2]) continue;

            board[r][c] = 'Q';
            cols[c] = d1[id1] = d2[id2] = true;

            backtrack(r + 1, n, board, cols, d1, d2, result);

            board[r][c] = '.';
            cols[c] = d1[id1] = d2[id2] = false;
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N!)$
- **Space Complexity:** $O(N)$ recursion depth and constraint tracking arrays.

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Forgetting to Clone State:**
   - Writing `result.add(current);` stores the live reference. When backtracking pops elements, all subsets in `result` become empty `[]`!
   - Always write: `result.add(new ArrayList<>(current));`.
2. **Un-Choosing Order in Backtracking:**
   - Any state change performed before the recursive call MUST be reversed in exact reverse order afterwards.
   - Forgetting to reset `visited[i] = false` permanently locks that path, preventing sibling branches from exploring that element.
3. **Infinite Recursion on Target Depletion:**
   - When elements are reusable (like Combination Sum), failing to advance or prune when `remain < 0` triggers `StackOverflowError`.
