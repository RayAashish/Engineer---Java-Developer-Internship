# Phase 2: Matrix — Self-Guided Problem Workbook (20 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.matrix`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Boundary Shrinkage & Traversal Geometry

---

### Problem #1: Spiral Matrix I (Difficulty: Medium)
- **Pattern Category:** Pattern 1: 4-Wall Constriction
- **Problem Statement & Constraints:** Given an $m \times n$ matrix ($1 \le m, n \le 10$), return all elements of the matrix in spiral order.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $mat = [[1,2,3],[4,5,6],[7,8,9]] \to$ Output: `[1,2,3,6,9,8,7,4,5]`.
  - *Test Case 2 (Single Row Boundary):* $mat = [[1, 2, 3, 4]] \to$ Output: `[1, 2, 3, 4]`.  
    *Explanation:* After moving right, $top$ becomes 1 which exceeds $bottom = 0$. Bottom/left sweeps must be guarded.
  - *Test Case 3 (Single Column Boundary):* $mat = [[1],[2],[3]] \to$ Output: `[1, 2, 3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Maintain a visited matrix, changing direction vectors $(0,1) \to (1,0) \to (0,-1) \to (-1,0)$ whenever encountering an already visited cell.
  - **2. Optimal Approach ($O(R \cdot C)$ Time, $O(1)$ Extra Space):** Maintain four pointers: `top = 0, bottom = m - 1, left = 0, right = n - 1`. Loop while `top <= bottom && left <= right`. Traverse: (1) left to right on `top`, then `top++`; (2) top to bottom on `right`, then `right--`; (3) if `top <= bottom`, right to left on `bottom`, then `bottom--`; (4) if `left <= right`, bottom to top on `left`, then `left++`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Spiral Matrix II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Sequential Matrix Synthesis
- **Problem Statement & Constraints:** Given a positive integer $n$ ($1 \le n \le 20$), generate an $n \times n$ matrix filled with elements from $1$ to $n^2$ in spiral order.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 3 \to$ Output: `[[1,2,3],[8,9,4],[7,6,5]]`.
  - *Test Case 2 (Boundary):* $n = 1 \to$ Output: `[[1]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(1)$ Extra Space beyond output):** Initialize an $n \times n$ grid and counter `num = 1`. Use the four-boundary pointer template (`top, bottom, left, right`). Incrementally write `matrix[r][c] = num++` along perimeter walls until `num > n * n`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Spiral Matrix III (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Expanding Spiral Walk
- **Problem Statement & Constraints:** You start at cell $(rStart, cStart)$ of a grid with $rows \times cols$. Walk in a clockwise spiral that may go outside grid boundaries, recording cells only when inside. Return coordinates in order of visitation. $1 \le rows, cols \le 100$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $rows = 1, cols = 4, rStart = 0, cStart = 0 \to$ Output: `[[0,0],[0,1],[0,2],[0,3]]`.
  - *Test Case 2 (Standard):* $rows = 5, cols = 6, rStart = 1, cStart = 4 \to$ Returns all 30 cell coordinates.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\max(R, C)^2)$ Time, $O(1)$ Extra Space):** Observe step size progression: Right $1$, Down $1$, Left $2$, Up $2$, Right $3$, Down $3$, Left $4$, Up $4$... Step length increases by 1 after every 2 turns. Move step-by-step along direction vector. If cell satisfies $0 \le r < rows$ and $0 \le c < cols$, record coordinate. Stop when total recorded coordinates reach $rows \times cols$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Diagonal Traverse (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Anti-Diagonal Parity Alternation
- **Problem Statement & Constraints:** Given an $m \times n$ matrix $mat$, return an array of all elements in diagonal order (zigzag up-right, down-left). $1 \le m, n \le 10^4$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[1,2,3],[4,5,6],[7,8,9]] \to$ Output: `[1,2,4,7,5,3,6,8,9]`.
  - *Test Case 2 (Single Element):* $mat = [[1]] \to$ Output: `[1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(1)$ Extra Space):** Maintain $(r, c)$. If $(r + c)$ is even, direction is Up-Right: try $r--, c++$; if right boundary reached, move down $r++$; if top boundary reached, move right $c++$. If $(r + c)$ is odd, direction is Down-Left: try $r++, c--$; if bottom boundary reached, move right $c++$; if left boundary reached, move down $r++$. Priority must check wall boundaries before ceiling/floor boundaries.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Diagonal Traverse II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Ragged 2D Array Diagonal Bucket Hashing
- **Problem Statement & Constraints:** Given a 2D integer array `nums` where rows may have varying lengths, return all elements in diagonal order (bottom-to-top along each anti-diagonal $r + c = k$).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `nums = [[1,2,3],[4,5,6],[7,8,9]]` $\to$ Output: `[1,4,2,7,5,3,8,6,9]`.
  - *Test Case 2 (Ragged Rows):* `nums = [[1,2,3,4,5],[6,7],[8],[9,10,11],[12,13,14,15,16]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time where $N$ is total elements, $O(N)$ Space):** Elements on the same diagonal have equal $r + c$. Create a list of lists `buckets` indexed by $r + c$. Iterate rows from bottom $r = R - 1$ down to 0, and cols $c = 0$ to $len(row)-1$. Appending `nums[r][c]` to `buckets[r + c]` naturally places bottom elements first. Flatten buckets in order of index $0 \dots \max(r + c)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Staircase Search & Virtual Binary Search

---

### Problem #6: Search a 2D Matrix I (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Virtual 1D Flattening
- **Problem Statement & Constraints:** You are given an $m \times n$ integer matrix with two properties: (1) each row is sorted ascending, (2) the first integer of each row is greater than the last integer of the previous row. Search for $target$. $O(\log(m \cdot n))$ runtime.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3 \to$ Output: `true`.
  - *Test Case 2 (Not Found):* $mat = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13 \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log(R \cdot C))$ Time, $O(1)$ Space):** Treat matrix as a flat virtual array of size $R \times C$. Perform binary search with $low = 0, high = R \times C - 1$. Calculate $mid = low + (high - low) / 2$, then map back to 2D: $row = mid / C$, $col = mid \% C$. Standard binary search branching.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Search a 2D Matrix II (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Top-Right Staircase Pruning
- **Problem Statement & Constraints:** Search an $m \times n$ matrix where each row and column is sorted ascending. $1 \le m, n \le 300$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* 5x5 sorted matrix, $target = 5 \to$ Output: `true`.
  - *Test Case 2 (Not Found):* 5x5 sorted matrix, $target = 20 \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R + C)$ Time, $O(1)$ Space):** Position cursor at top-right corner $(r = 0, c = C - 1)$. While $r < R$ and $c \ge 0$: if `mat[r][c] == target`, return true; if `mat[r][c] > target`, decrement $c$ (eliminate column); if `mat[r][c] < target`, increment $r$ (eliminate row).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Kth Smallest Element in a Sorted Matrix (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Binary Search on Answer + Staircase Counting
- **Problem Statement & Constraints:** Given an $n \times n$ matrix where each row and column is sorted ascending, return the $k$-th smallest element. $1 \le k \le n^2$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[1,5,9],[10,11,13],[12,13,15]], k = 8 \to$ Output: `13`.
  - *Test Case 2 (Single Element):* $mat = [[-5]], k = 1 \to$ Output: `-5`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Better Approach ($O(K \log N)$ Time, $O(N)$ Space):** Min-heap initialized with first element of every row. Pop minimum and insert next element in that row $k-1$ times.
  - **2. Optimal Approach ($O(N \log(\max - \min))$ Time, $O(1)$ Space):** Binary search on value domain $[mat[0][0], mat[n-1][n-1]]$. For each candidate $mid$, count elements $\le mid$ in $O(N)$ time starting from bottom-left corner $(n - 1, 0)$. If count $\ge k$, search lower half ($high = mid - 1$); else search upper half ($low = mid + 1$).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Count Negative Numbers in a Sorted Matrix (Difficulty: Easy)
- **Pattern Category:** Pattern 2: Bottom-Left Staircase Boundary
- **Problem Statement & Constraints:** Given an $m \times n$ matrix $grid$ which is sorted in non-increasing order both row-wise and column-wise, count the number of negative numbers.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $grid = [[4,3,2,-1],[3,2,1,-1],[1,1,-1,-2],[-1,-1,-2,-3]] \to$ Output: `8`.
  - *Test Case 2 (All Non-Negative):* $grid = [[3,2],[1,0]] \to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R + C)$ Time, $O(1)$ Space):** Start at bottom-left $(r = R - 1, c = 0)$. If $grid[r][c] < 0$, then every element to the right of $(r, c)$ in row $r$ is also negative $\implies$ add $(C - c)$ to answer and decrement row $r--$. If $grid[r][c] \ge 0$, increment column $c++$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Leftmost Column with at Least a One (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Greedy Staircase Tracking
- **Problem Statement & Constraints:** A row-sorted binary matrix has each row sorted (all 0s before 1s). Using a `BinaryMatrix` API, find the index of the leftmost column with at least a 1. If none exists, return -1. Limit API calls to $\le 1000$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[0,0],[1,1]] \to$ Output: `0`.
  - *Test Case 2 (None):* $mat = [[0,0],[0,0]] \to$ Output: `-1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R + C)$ API Calls):** Start top-right $(0, C - 1)$. If cell is 1, update `ans = c` and move left $c--$ (looking for an even earlier 1). If cell is 0, no 1 can exist further left in this row, so move down $r++$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: In-Place Geometric Transformations

---

### Problem #11: Rotate Image (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Transpose + Horizontal Reflection
- **Problem Statement & Constraints:** Rotate an $n \times n$ matrix 90 degrees clockwise in-place. $1 \le n \le 20$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[1,2,3],[4,5,6],[7,8,9]] \to$ Output: `[[7,4,1],[8,5,2],[9,6,3]]`.
  - *Test Case 2 (Boundary):* $mat = [[1]] \to$ Output: `[[1]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(1)$ Space):** Step 1: Transpose matrix along main diagonal (swap $matrix[i][j] \leftrightarrow matrix[j][i]$ for $j > i$). Step 2: Reverse each row horizontally using two pointers ($left = 0, right = n - 1$).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Transpose Matrix (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Non-Square Dimension Inversion
- **Problem Statement & Constraints:** Given a 2D integer array $matrix$, return the transpose. Dimensions may be rectangular ($m \ne n$). $1 \le m, n \le 1000$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Rectangular):* $matrix = [[1,2,3],[4,5,6]] \to$ Output: `[[1,4],[2,5],[3,6]]` (dimension $2 \times 3 \to 3 \times 2$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Allocate a new result matrix of dimensions $C \times R$. For each $(r, c)$, assign $result[c][r] = matrix[r][c]$. Note: In-place transposition is impossible when $R \ne C$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #13: Reshape the Matrix (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Linear Index Mapping
- **Problem Statement & Constraints:** Reshape an $m \times n$ matrix into a $r \times c$ matrix preserving row-traversing order. If impossible ($m \cdot n \ne r \cdot c$), return original matrix.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[1,2],[3,4]], r = 1, c = 4 \to$ Output: `[[1,2,3,4]]`.
  - *Test Case 2 (Invalid):* $mat = [[1,2],[3,4]], r = 2, c = 4 \to$ Output: `[[1,2],[3,4]]` ($2 \times 2 \ne 2 \times 4$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Check if $m \cdot n == r \cdot c$. For each 1D linear index $k$ from $0$ to $m \cdot n - 1$: original cell is $(k / n, k \% n)$ and target cell is $(k / c, k \% c)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Determine Whether Matrix Can Be Obtained By Rotation (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Exhaustive 4-State Cycle Check
- **Problem Statement & Constraints:** Given two $n \times n$ binary matrices $mat$ and $target$, return `true` if you can make $mat$ equal to $target$ by rotating $mat$ in 90-degree increments (0°, 90°, 180°, 270°).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[0,1],[1,0]], target = [[1,0],[0,1]] \to$ Output: `true`.
  - *Test Case 2 (Failure Trap):* Matrices that are reflections but not rotations $\to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(1)$ Space):** Perform rotation in-place up to 4 times. After each 90° rotation (transpose + reverse rows), check for equality against $target$. If identical, return true.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Shift 2D Grid (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Modulo Index Offset
- **Problem Statement & Constraints:** Given an $m \times n$ grid and integer $k$, shift the grid $k$ times: element at $(r, c)$ moves to $(r, c + 1)$, element at $(r, n - 1)$ moves to $(r + 1, 0)$, element at $(m - 1, n - 1)$ moves to $(0, 0)$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $grid = [[1,2,3],[4,5,6],[7,8,9]], k = 1 \to$ Output: `[[9,1,2],[3,4,5],[6,7,8]]`.
  - *Test Case 2 (Full Cycle):* $k = m \times n \to$ Original grid unchanged.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Normalize shift $k = k \% (m \cdot n)$. For 1D index $i$, its original position in 1D was $(i - k + m \cdot n) \% (m \cdot n)$. Convert back to 2D coordinates.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: State In-Place Encoding & Multi-Condition Scans

---

### Problem #16: Set Matrix Zeroes (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Border Projection State Tracking
- **Problem Statement & Constraints:** Given an $m \times n$ integer matrix, if an element is 0, set its entire row and column to 0 in-place. Must use $O(1)$ auxiliary space. $1 \le m, n \le 200$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[1,1,1],[1,0,1],[1,1,1]] \to$ Output: `[[1,0,1],[0,0,0],[1,0,1]]`.
  - *Test Case 2 (First Row/Col Contains Zero):* $mat = [[0,1,2,0],[3,4,5,2],[1,3,1,5]] \to$ First row and col must be properly zeroed.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(1)$ Space):** Use row 0 and col 0 as scratch flags. Use boolean `col0` for column 0. Scan matrix: if `mat[r][c] == 0`, set `mat[r][0] = 0` and `mat[0][c] = 0`. Iterate backward from $(R-1, C-1)$ up to $(0, 1)$ applying zeros. Lastly zero column 0 if `col0` is true.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Game of Life (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 2-Bit State Encoding
- **Problem Statement & Constraints:** Compute the next generation of Conway's Game of Life on an $m \times n$ board simultaneously in-place.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $board = [[0,1,0],[0,0,1],[1,1,1],[0,0,0]] \to$ Output: `[[0,0,0],[1,0,1],[0,1,1],[0,1,0]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(1)$ Space):** Encode past and future states in 2 bits. Read past state using `cell & 1`. If cell transitions to alive in next round, set bit 1 (`cell |= 2`). After updating all cells, shift each cell rightward `cell >>= 1` to commit the future state.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Valid Sudoku (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Bitmask / Set Row-Col-Box Partitioning
- **Problem Statement & Constraints:** Determine if a $9 \times 9$ Sudoku board is valid. Only filled cells must be validated.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Valid):* Valid standard initial 9x9 board $\to$ `true`.
  - *Test Case 2 (Duplicate in 3x3 Subbox):* Same number appears twice in the same $3 \times 3$ block $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(81)$ Time, $O(1)$ Space):** Maintain three bitmask arrays of length 9: `rows[9]`, `cols[9]`, `boxes[9]`. For cell $(r, c)$ with digit $d$, its subbox index is $(r / 3) \times 3 + (c / 3)$. Check if $(1 \ll d)$ already exists in `rows[r]`, `cols[c]`, or `boxes[boxIdx]`. If so, invalid. Else set bit.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Number of Closed Islands (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Boundary Flood Fill Elimination
- **Problem Statement & Constraints:** An $m \times n$ grid consists of 0s (land) and 1s (water). A closed island is surrounded entirely by 1s (cannot touch boundary). Return number of closed islands.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Closed island in center surrounded by water $\to$ counted.
  - *Test Case 2 (Touches Boundary):* Island touches edge of grid $\to$ not closed.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Recursion Stack):** First, run DFS/flood-fill on all land cells $(0)$ connected to the 4 matrix perimeter borders, converting them to water $(1)$. All remaining land cells in the interior are guaranteed to be closed islands. Iterate through interior and count/flood-fill remaining islands.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Range Sum Query 2D - Immutable (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 2D Prefix Sum Geometry
- **Problem Statement & Constraints:** Calculate the sum of elements inside a rectangle defined by $(row1, col1)$ to $(row2, col2)$ in $O(1)$ query time after preprocessing.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Matrix of positive numbers, query submatrix $\to$ returns correct sum.
  - *Test Case 2 (Single Cell):* $row1 = row2, col1 = col2 \to$ returns exact cell value.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Preprocessing, $O(1)$ Query Time, $O(R \cdot C)$ Space):**
    - Build 2D prefix sum array $P$ of size $(R+1) \times (C+1)$:
      $$P[r+1][c+1] = mat[r][c] + P[r][c+1] + P[r+1][c] - P[r][c]$$
    - Any submatrix sum query $[r_1, c_1]$ to $[r_2, c_2]$ is computed in $O(1)$:
      $$\text{Sum} = P[r_2+1][c_2+1] - P[r_1][c_2+1] - P[r_2+1][c_1] + P[r_1][c_1]$$
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
