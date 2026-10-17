# Phase 2: Matrix — Coordinate Math, Direction Vectors & Memory Mechanics
> **Author:** raysi  
> **Package:** `com.raysi.dsa.matrix`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master 2D grid mechanics, in-place geometric manipulation, and cache-conscious traversal.

---

## 1. Matrix Mental Model & Memory Mechanics

### 1.1 The JVM "Array of Arrays" Memory Architecture
In languages like C/C++, a 2D array `int matrix[R][C]` is allocated as a single, contiguous block of $R \times C \times 4$ bytes in Row-Major Order. Accessing `matrix[r][c]` is direct pointer arithmetic:
$$\text{Address} = \text{Base} + (r \times C + c) \times 4$$

In **Java**, a 2D array `int[][] matrix = new int[R][C]` is NOT contiguous memory. It is an **array of references** pointing to $R$ distinct 1D array objects scattered across the JVM heap:

```
[matrix reference] ---> [ Row 0 ptr | Row 1 ptr | Row 2 ptr ]  (Array of pointers)
                              |           |           |
                              v           v           v
                          [0, 1, 2]   [3, 4, 5]   [6, 7, 8]    (Separate heap blocks)
```

#### Cache Line Impact (Row-Major vs Column-Major Traversal):
- **Row-wise traversal (`matrix[r][c++]`):** The CPU fetches an entire 64-byte cache line (16 `int`s) from row $r$. The next 15 reads hit L1 cache immediately.
- **Column-wise traversal (`matrix[r++][c]`):** Each iteration dereferences a completely different row pointer in heap memory, triggering frequent **L1/L2 cache misses** and pointer indirection overhead.
- **Rule of Thumb:** Always traverse horizontally along rows whenever algorithmically permissible.

---

### 1.2 Coordinate System & Direction Vectors
A frequent pitfall in matrix problems is mixing up Cartesian coordinates $(x, y)$ with Matrix index coordinates $(row, col)$.

$$\text{Row } r \implies \text{Vertical axis (increases downwards)}$$
$$\text{Column } c \implies \text{Horizontal axis (increases rightwards)}$$

#### Reusable Direction Vectors:
```java
// 4-Directional (Up, Down, Left, Right)
public static final int[][] DIRS_4 = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

// 8-Directional (includes Diagonals)
public static final int[][] DIRS_8 = {
    {-1, 0}, {1, 0}, {0, -1}, {0, 1},
    {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
};

// Safe Boundary Guard Idiom:
private static boolean isValid(int r, int c, int rows, int cols) {
    return r >= 0 && r < rows && c >= 0 && c < cols;
}
```

---

### 1.3 Constraints to Big-O Matrix Table

| Dimensions ($R \times C$) | Total Cells ($N = R \cdot C$) | Permissible Big-O | Typical Target Algorithm |
| :--- | :--- | :--- | :--- |
| $R, C \le 15$ | $\le 225$ | $O(2^{R \cdot C})$ or $O(R \cdot C \cdot 3^{K})$ | Backtracking, Bitmask DP, Grid Path Finding |
| $R, C \le 50$ | $\le 2500$ | $O((R \cdot C)^2)$ | Floyd-Warshall, All-Pairs shortest path |
| $R, C \le 300$ | $\le 10^5$ | $O(R \cdot C)$ | BFS, DFS Flood Fill, 2D Prefix Sums |
| $R, C \le 1000$ | $\le 10^6$ | $O(R \cdot C)$ or $O(R + C)$ | In-place rotation, Spiral, Staircase Search |

---

## 2. The 4 Core Matrix Patterns

```
                                  +---------------------------+
                                  |   2D Matrix Navigation    |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |                                     |
+-------------------+ +-------------------+                 +-------------------+
| Pattern 1: Spiral | | Pattern 2: Search |                 | Pattern 3: In-    |
| & Bound Shrinking | | Sorted Grids      |                 | Place Rotation    |
+-------------------+ +-------------------+                 +-------------------+
  4 moving walls        Top-Right / Bot-Left                  Transpose + Reverse
  top++, bottom--       Staircase Search                      Row/Col In-Place
  left++, right--       O(R + C)                              O(1) Memory
```

---

### Pattern 1: Boundary Shrinkage (Spiral Traversal & Generation)
- **Trigger Keywords:** `"spiral order"`, `"layer by layer"`, `"generate n x n spiral matrix"`.
- **Mental Model:** Think of an enclosed room whose four walls ($top, bottom, left, right$) constrict inwards after every sweep.
- **ASCII Visual:**
```
     left               right
top  -> [ 1   2   3   4 ] v
        [ 5  ->   ->  6 ]
        [ 7  <-   <-  8 ]
bot  ^  [ 9  10  11  12 ] <-
```
- **Java Template:**
```java
int top = 0, bottom = R - 1, left = 0, right = C - 1;
while (top <= bottom && left <= right) {
    for (int c = left; c <= right; c++) visit(top, c);
    top++;
    for (int r = top; r <= bottom; r++) visit(r, right);
    right--;
    if (top <= bottom) {
        for (int c = right; c >= left; c--) visit(bottom, c);
        bottom--;
    }
    if (left <= right) {
        for (int r = bottom; r >= top; r--) visit(r, left);
        left++;
    }
}
```

---

### Pattern 2: Staircase Search & Virtual 1D Flattening
- **Trigger Keywords:** `"row-wise and column-wise sorted"`, `"strictly sorted"`.
- **Mental Model:** 
  - If strictly sorted $\to$ Binary Search on index space $[0, R \cdot C - 1]$ with $row = mid / C$, $col = mid \% C$.
  - If independently row-sorted and column-sorted $\to$ Stand at top-right $(0, C - 1)$. Decreasing is left ($c--$), increasing is down ($r++$).
- **ASCII Visual:**
```
        [  1,  4,  7, 11, 15 ]  <-- Start at 15
        [  2,  5,  8, 12, 19 ]      Target = 5
        [  3,  6,  9, 16, 22 ]      15 > 5 -> Col-- (left)
        [ 10, 13, 14, 17, 24 ]      11 > 5 -> Col-- (left)
        [ 18, 21, 23, 26, 30 ]       7 > 5 -> Col-- (left)
                                     4 < 5 -> Row++ (down) -> Found!
```

---

### Pattern 3: In-Place Geometric Transformations
- **Trigger Keywords:** `"rotate image"`, `"in-place"`, `"O(1) extra space"`.
- **Mental Model:** Decompose 2D rotations into fundamental linear algebra operations:
  - **90° Clockwise:** Transpose ($matrix[i][j] \leftrightarrow matrix[j][i]$) + Horizontal Reflection (Reverse each row).
  - **90° Counter-Clockwise:** Transpose + Vertical Reflection (Reverse each column).
  - **180° Rotation:** Reverse rows + Reverse columns.

---

### Pattern 4: State In-Place Encoding (Multi-State Bitmasking)
- **Trigger Keywords:** `"simulate in-place"`, `"simultaneous updates"`, `"Game of Life"`, `"Set Matrix Zeroes"`.
- **Mental Model:** When cells need to transition based on simultaneous neighbor values, you cannot overwrite values eagerly without losing past state.
  - *Method A (Bitwise Encoding):* Use Bit 0 for `Old State` and Bit 1 for `New State`.
    - `old = val & 1`
    - `val |= (newState << 1)`
    - Final state: `val >>>= 1`.
  - *Method B (Border Projection):* Use Row 0 and Col 0 as scratchpads to track which rows/cols should be zeroed out.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Spiral Matrix (LeetCode 54)
> **Problem:** Given an $m \times n$ matrix, return all elements of the matrix in spiral order.

#### 1. Brute Force Instinct
Maintain an $m \times n$ boolean `visited[][]` array. Keep moving in direction `(dr, dc)` until hitting a boundary or visited cell, then turn right.
*Cost:* $O(R \cdot C)$ time and $O(R \cdot C)$ auxiliary space.

#### 2. "Aha!" Observation
No visited array is required! The boundary of the unvisited region is always a rectangle defined by `[top, bottom]` and `[left, right]`. Shrink boundaries sequentially. Must guard the bottom and left sweeps with `if (top <= bottom)` and `if (left <= right)` to prevent single-row / single-column double counting.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.matrix;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrixAnchor {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        if (matrix == null || matrix.length == 0) return res;

        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) res.add(matrix[top][c]);
            top++;

            for (int r = top; r <= bottom; r++) res.add(matrix[r][right]);
            right--;

            if (top <= bottom) {
                for (int c = right; c >= left; c--) res.add(matrix[bottom][c]);
                bottom--;
            }

            if (left <= right) {
                for (int r = bottom; r >= top; r--) res.add(matrix[r][left]);
                left++;
            }
        }
        return res;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C)$ — Every element is visited exactly once.
- **Space Complexity:** $O(1)$ auxiliary space (excluding result container).

---

### Anchor Problem #2: Rotate Image (LeetCode 48)
> **Problem:** Rotate an $n \times n$ 2D matrix representing an image by 90 degrees clockwise in-place.

#### 1. Brute Force Instinct
Allocate a new matrix `newMat[n][n]`. For every cell $(i, j)$, assign `newMat[j][n - 1 - i] = matrix[i][j]`.
*Cost:* $O(N^2)$ time and $O(N^2)$ extra memory. Violates the strict $O(1)$ space requirement.

#### 2. "Aha!" Observation
Notice the linear transformation: cell $(i, j) \to (j, n - 1 - i)$.
This can be factored into two symmetric steps:
1. Swap coordinates $(i, j) \to (j, i)$ $\implies$ **Transpose**.
2. Swap horizontal columns $(j, i) \to (n - 1 - j, i)$ $\implies$ **Reverse each row**.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.matrix;

public class RotateImageAnchor {
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // 1. Transpose: swap matrix[i][j] with matrix[j][i] for j > i
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        // 2. Reverse each row horizontally
        for (int i = 0; i < n; i++) {
            int left = 0, right = n - 1;
            while (left < right) {
                int temp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = temp;
                left++;
                right--;
            }
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N^2)$ — Each cell swapped a constant number of times.
- **Space Complexity:** $O(1)$ strictly in-place.

---

### Anchor Problem #3: Search a 2D Matrix II (LeetCode 240)
> **Problem:** Write an efficient algorithm that searches for a target value in an $m \times n$ matrix where integers in each row are sorted ascending left-to-right, and integers in each column are sorted ascending top-to-bottom.

#### 1. Brute Force Instinct
Binary search on each row independently $\implies O(R \log C)$ time.

#### 2. "Aha!" Observation
Start from the **Top-Right corner** $(0, C - 1)$ (or bottom-left):
- If `matrix[r][c] == target`: Found.
- If `matrix[r][c] > target`: Target cannot exist in column $c$ (since all cells below are even larger) $\implies c--$.
- If `matrix[r][c] < target`: Target cannot exist in row $r$ (since all cells to the left are even smaller) $\implies r++$.
Every comparison discards an entire row or column.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.matrix;

public class SearchMatrixIIAnchor {
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;

        int r = 0;
        int c = matrix[0].length - 1;

        while (r < matrix.length && c >= 0) {
            int val = matrix[r][c];
            if (val == target) {
                return true;
            } else if (val > target) {
                c--; // eliminate column
            } else {
                r++; // eliminate row
            }
        }
        return false;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R + C)$ — In the worst case, we make at most $R + C$ moves.
- **Space Complexity:** $O(1)$ auxiliary.

---

### Anchor Problem #4: Set Matrix Zeroes (LeetCode 73)
> **Problem:** Given an $m \times n$ integer matrix, if an element is 0, set its entire row and column to 0's in-place without using $O(R \times C)$ or $O(R + C)$ extra memory.

#### 1. Brute Force Instinct
Allocate boolean arrays `zeroRows[m]` and `zeroCols[n]`. Mark rows/cols containing zeros, then overwrite.
*Cost:* $O(R + C)$ auxiliary space.

#### 2. "Aha!" Observation
Use the first row `matrix[0][..]` and first column `matrix[..][0]` as the marking arrays!
- Since `matrix[0][0]` overlaps both Row 0 and Col 0, use a separate boolean flag `firstColZero` for column 0.
- Iterate through matrix cells $(r \ge 1, c \ge 1)$: if $matrix[r][c] == 0$, set $matrix[r][0] = 0$ and $matrix[0][c] = 0$.
- Zero out cells based on markers, and finally zero out Row 0 and Col 0 if flagged.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.matrix;

public class SetMatrixZeroesAnchor {
    public void setZeroes(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean firstColZero = false;

        for (int r = 0; r < rows; r++) {
            if (matrix[r][0] == 0) firstColZero = true;
            for (int c = 1; c < cols; c++) {
                if (matrix[r][c] == 0) {
                    matrix[r][0] = 0;
                    matrix[0][c] = 0;
                }
            }
        }

        // Set zeroes from bottom-right upwards to avoid corrupting markers prematurely
        for (int r = rows - 1; r >= 0; r--) {
            for (int c = cols - 1; c >= 1; c--) {
                if (matrix[r][0] == 0 || matrix[0][c] == 0) {
                    matrix[r][c] = 0;
                }
            }
            if (firstColZero) {
                matrix[r][0] = 0;
            }
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C)$
- **Space Complexity:** $O(1)$ strictly in-place.

---

### Anchor Problem #5: Game of Life (LeetCode 289)
> **Problem:** Conway's Game of Life on an $m \times n$ grid. Compute the next state simultaneously in-place using $O(1)$ extra space.

#### 1. Brute Force Instinct
Clone matrix to `clone[][]`, count live neighbors in `clone`, write results into `matrix`.
*Cost:* $O(R \cdot C)$ auxiliary space.

#### 2. "Aha!" Observation
Encode both past state and next state into a 2-bit integer:
- Bit 0 = Old state (0 = Dead, 1 = Live)
- Bit 1 = New state (0 = Will die, 1 = Will live)

State transitions:
- $00_2 = 0$: Dead $\to$ Dead
- $01_2 = 1$: Live $\to$ Dead
- $10_2 = 2$: Dead $\to$ Live
- $11_2 = 3$: Live $\to$ Live

To read old neighbor state: `neighbor & 1`.
To apply new state: `board[r][c] >>= 1`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.matrix;

public class GameOfLifeAnchor {
    private static final int[][] DIRS = {
        {-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1}
    };

    public void gameOfLife(int[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int liveNeighbors = countLiveNeighbors(board, r, c, rows, cols);

                // Rule 1 & 3: Under-population or over-population -> stays dead (bit 1 = 0)
                // Rule 2: Live cell with 2 or 3 live neighbors lives on
                if ((board[r][c] & 1) == 1 && (liveNeighbors == 2 || liveNeighbors == 3)) {
                    board[r][c] |= 2; // set bit 1 to 1
                }
                // Rule 4: Dead cell with exactly 3 live neighbors becomes live
                if ((board[r][c] & 1) == 0 && liveNeighbors == 3) {
                    board[r][c] |= 2; // set bit 1 to 1
                }
            }
        }

        // Shift out past state to reveal final state
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] >>= 1;
            }
        }
    }

    private int countLiveNeighbors(int[][] board, int r, int c, int rows, int cols) {
        int count = 0;
        for (int[] d : DIRS) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                count += (board[nr][nc] & 1); // Read only bit 0 (past state)
            }
        }
        return count;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C)$
- **Space Complexity:** $O(1)$ in-place.

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Non-Square Rectangles ($R \ne C$):**
   - Transposition formulas like swapping `mat[i][j]` with `mat[j][i]` in-place *only work on square matrices* ($N \times N$). Attempting in-place transpose on $R \ne C$ yields `ArrayIndexOutOfBoundsException`.
2. **Single Row / Single Column Matrices:**
   - In Spiral Traversal, without `if (top <= bottom)` before the bottom sweep and `if (left <= right)` before the left sweep, single-row matrices will print row 0 forward, and then immediately print row 0 backward again!
3. **Array Flattening Coordinates:**
   - In row-major flattening:
     $$\text{Index to Coordinates:} \quad row = mid / C, \quad col = mid \% C$$
     *Trap:* Dividing or moduloing by $R$ instead of $C$ (number of columns)!
4. **Coordinate Flip in DFS/BFS:**
   - Writing `visited[c][r]` instead of `visited[r][c]`. Always standardize variable naming: `r` for row (bounded by $R$), `c` for column (bounded by $C$).
