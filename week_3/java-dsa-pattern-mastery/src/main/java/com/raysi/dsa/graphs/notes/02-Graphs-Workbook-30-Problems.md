# Phase 8: Graphs — Self-Guided Problem Workbook (30 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.graphs`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Grid Multi-Source BFS & Flood Fill

---

### Problem #1: Number of Islands (Difficulty: Medium)
- **Pattern Category:** Pattern 1: In-Place Sinking DFS
- **Problem Statement & Constraints:** Given an $m \times n$ binary grid $grid$, return the number of islands. $1 \le m, n \le 300$.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* Grid with 3 isolated land clusters $\to$ Output: `3`.
  - *Test Case 2 (All Water):* All '0's $\to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Stack):** Outer loop over all cells: when encountering `'1'`, increment count, then recursively sink adjacent land cells to `'0'` in 4 directions.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Max Area of Island (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Component Size Accumulation DFS
- **Problem Statement & Constraints:** Return the maximum area of an island in a binary grid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Largest island has 13 connected cells $\to$ Output: `13`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Stack):** DFS helper returns component size: $1 + \sum_{4\text{ dirs}} dfs(neighbor)$. Sink cells during traversal to prevent double-counting. Maximize over all cells.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Rotting Oranges (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Multi-Source Level-Order BFS
- **Problem Statement & Constraints:** Return minimum minutes until no fresh orange remains, or -1.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Multi-minute infection $\to$ Output: `4`.
  - *Test Case 2 (Isolated Fresh):* Fresh orange surrounded by empty cells $\to$ Output: `-1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Enqueue all initial rotten oranges at $t = 0$. Count total fresh oranges. Run BFS in level-size batches, incrementing minutes. Mutate fresh to rotten upon visit and decrement fresh count.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: 01 Matrix (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Multi-Source Distance Flood Fill
- **Problem Statement & Constraints:** Given an $m \times n$ binary matrix $mat$, return the distance of the nearest 0 for each cell.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $mat = [[0,0,0],[0,1,0],[0,0,0]] \to$ Center cell distance is 1.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Enqueue ALL cells with value 0 simultaneously. Initialize cells with value 1 to -1 (unvisited). Run multi-source BFS: when moving to an unvisited cell $(nr, nc)$, set $dist[nr][nc] = dist[r][c] + 1$ and enqueue.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Surrounded Regions (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Boundary Inversion Flood Fill
- **Problem Statement & Constraints:** Capture all regions surrounded by 'X'. An 'O' is not captured if connected to the border.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Interior 'O's captured; border-connected 'O's survive.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** DFS from all 'O's on the 4 borders of the matrix, temporarily marking them as safe (e.g. `'S'`). Scan entire board: change remaining 'O's to 'X' (captured), and change 'S' back to 'O' (survived).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Pacific Atlantic Water Flow (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Dual-Ocean Reverse Inflow BFS/DFS
- **Problem Statement & Constraints:** Return grid coordinates that can flow water to both Pacific and Atlantic oceans.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* High-elevation ridge cells flowing both directions.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Reverse water flow! Flow *uphill* from Pacific borders into boolean `pacific[R][C]`, and uphill from Atlantic borders into `atlantic[R][C]`. Cells where both booleans are true can flow to both oceans.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Shortest Path in Binary Matrix (Difficulty: Medium)
- **Pattern Category:** Pattern 1: 8-Directional Unweighted BFS
- **Problem Statement & Constraints:** Return length of shortest clear path from top-left to bottom-right in binary matrix (8-directional).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Path exists through diagonal moves.
  - *Test Case 2 (Start Blocked):* $grid[0][0] == 1 \to$ Output: `-1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** BFS with 8 directions. Mark visited immediately upon enqueueing by setting `grid[r][c] = 1`. Layer-by-layer step tracking.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Connected Components & Bipartiteness

---

### Problem #8: Number of Connected Components (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Outer-Loop Component Discovery
- **Problem Statement & Constraints:** Given $n$ nodes and undirected edges, find number of connected components.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 5$, edges = [[0,1],[1,2],[3,4]] $\to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V)$ Space):** Build adjacency list. Maintain `boolean[] visited`. Loop $0 \dots n-1$: if unvisited, increment count and run DFS/BFS to mark entire component.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Is Graph Bipartite? (Difficulty: Medium)
- **Pattern Category:** Pattern 2: 2-Coloring BFS/DFS
- **Problem Statement & Constraints:** Return true if graph can be divided into two sets such that every edge connects different sets.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Even Cycle / Bipartite):* 4-cycle $\to$ `true`.
  - *Test Case 2 (Odd Cycle / Non-Bipartite):* Triangle (3-cycle) $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V)$ Space):** Color array `colors[V]` (0 = uncolored, 1 = red, -1 = blue). For each uncolored node, start DFS/BFS with color 1. Alternate color $-color$ to neighbors. If neighbor already has same color, odd-length cycle detected $\implies$ return false.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Possible Bipartition (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Dislike Graph 2-Coloring
- **Problem Statement & Constraints:** Split $n$ people into two groups such that no two people who dislike each other are in same group.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 4$, dislikes = [[1,2],[1,3],[2,4]] $\to$ Output: `true`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V + E)$ Space):** Build undirected graph from dislikes. Run 2-coloring verification algorithm.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Clone Graph (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Map-Preserving Deep Clone DFS
- **Problem Statement & Constraints:** Return deep copy of connected undirected graph.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* 4-node graph cloned with unique object references.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V)$ Space):** Maintain `Map<Node, Node> visited` mapping original node to clone node. In DFS: if node already cloned, return clone. Create new clone, put into map, recurse to clone each neighbor, add cloned neighbors to `clone.neighbors`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Keys and Rooms (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Reachability DFS
- **Problem Statement & Constraints:** Starting in room 0, can you visit all rooms given keys found in rooms?
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* All rooms unlocked $\to$ `true`.
  - *Test Case 2 (Unreachable Room):* Room 2 locked with key in room 2 $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V)$ Space):** `boolean[] visited`. Run DFS from room 0. Track count of visited rooms. Return `count == n`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #13: Reorder Routes to Make All Paths Lead to City Zero (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Artificial Bidirectional Graph with Reversal Cost
- **Problem Statement & Constraints:** Given directed tree of $n$ cities, reorient minimum edges so all cities can travel to city 0.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Directed roads pointing away from 0 $\to$ count reversals.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V)$ Time, $O(V)$ Space):** Build adjacency list with weighted directed edges: original edge $(u \to v)$ has weight 1 (needs reorienting if we move from 0 to v), artificial back-edge $(v \to u)$ has weight 0. Run DFS starting from city 0: sum edge weights along traversal.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Cycle Detection & Topological Sort

---

### Problem #14: Course Schedule I (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Directed Cycle Detection (3-Color)
- **Problem Statement & Constraints:** Determine if you can finish all courses given prerequisites.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (No Cycle):* prerequisites = [[1,0]] $\to$ `true`.
  - *Test Case 2 (Cycle):* prerequisites = [[1,0],[0,1]] $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V + E)$ Space):** State array (0=unvisited, 1=visiting, 2=visited). DFS: mark 1. If neighbor is in state 1, back-edge cycle exists $\implies$ return false. Mark 2 upon exit.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Course Schedule II (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Kahn's Algorithm (In-Degree BFS)
- **Problem Statement & Constraints:** Return valid ordering of courses to finish all courses, or empty array if cycle exists.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Valid ordering assembled.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V + E)$ Space):** Compute in-degrees. Enqueue all nodes with in-degree 0. While queue non-empty: pop node, add to ordering, decrement neighbors' in-degrees, enqueue if in-degree becomes 0. If result length $< n$, return empty array.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Alien Dictionary (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Pairwise Word Difference TopoSort
- **Problem Statement & Constraints:** Given list of words sorted lexicographically in alien language, derive character order.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* words = ["wrt","wrf","er","ett","rftt"] $\to$ Output: `"wertf"`.
  - *Test Case 2 (Prefix Trap):* ["abc", "ab"] $\to$ Invalid ordering $\implies$ `""`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(C)$ Time where $C$ is total chars):** Compare adjacent words. Find first differing character $c_1 \ne c_2$, add directed edge $c_1 \to c_2$ and increment $inDegree[c_2]$. Run Kahn's algorithm. If output length $\ne$ unique character count, return `""`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Minimum Height Trees (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Leaf-Trimming Inward Pruning (Reverse BFS)
- **Problem Statement & Constraints:** Find roots of trees that minimize tree height.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Tree with central centroid $\to$ 1 or 2 roots.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V)$ Time, $O(V)$ Space):** A tree can have at most 2 centroids. Repeatedly trim leaves (nodes with degree 1) layer by layer until $\le 2$ nodes remain. The remaining nodes are the roots of Minimum Height Trees.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Find Eventual Safe States (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Cycle-Free Node Identification (Reverse Topo / 3-Color)
- **Problem Statement & Constraints:** A node is safe if every possible path starting from that node leads to a terminal node (no outgoing edges).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Nodes not trapped in any directed cycle $\to$ sorted list of safe nodes.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V)$ Space):** 3-color DFS. A node is safe if and only if it does NOT lead to any cycle (reaches state 2 safely). Or reverse all edges and run Kahn's algorithm starting from terminal nodes.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Longest Increasing Path in a Matrix (Difficulty: Hard)
- **Pattern Category:** Pattern 3: DAG Memoized DFS / Topological DP
- **Problem Statement & Constraints:** Return length of longest increasing path in an $m \times n$ integers matrix.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Increasing sequence across matrix cells $\to$ max length.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(R \cdot C)$ Space):** Strictly increasing transitions $(val < neighborVal)$ guarantee the grid is a Directed Acyclic Graph (no cycles possible!). Use memoized DFS: $dp[r][c] = 1 + \max_{valid\ neighbors} dfs(nr, nc)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Shortest Paths (Dijkstra, Bellman-Ford & PriorityQueue)

---

### Problem #20: Network Delay Time (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Standard Dijkstra's Algorithm
- **Problem Statement & Constraints:** Minimum time for signal sent from node $k$ to reach all $n$ nodes.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Weighted graph $\to$ returns max of shortest distances.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(E \log V)$ Time, $O(V + E)$ Space):** Min-heap initialized with $(k, 0)$. Relax edges: if `dist[u] + w < dist[v]`, update and push. Discard stale entries `d > dist[u]`. Max distance is answer.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Path with Minimum Effort (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Min-Max Weight Dijkstra on Grid
- **Problem Statement & Constraints:** Find path from $(0, 0)$ to $(R-1, C-1)$ that minimizes the maximum absolute height difference between consecutive cells.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Grid path with minimal max jump.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Dijkstra Approach ($O(R \cdot C \log(R \cdot C))$ Time):** Min-heap stores `(effort, r, c)`. When expanding to neighbor, transition effort is $\max(effort, |height[r][c] - height[nr][nc]|)$. Relax cell efforts. Stop when target cell popped.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Cheapest Flights Within K Stops (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Bellman-Ford / BFS with Stop Constraint
- **Problem Statement & Constraints:** Find cheapest price from $src$ to $dst$ with at most $k$ stops.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Direct route more expensive than 1-stop route $\to$ cheaper picked.
  - *Test Case 2 (Stops Exceeded):* Cheapest route takes $k + 2$ stops $\to$ must use alternative.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Bellman-Ford Approach ($O(K \cdot E)$ Time, $O(V)$ Space):** Run $k + 1$ relaxations. In each round, use a clone of previous distances `prevDist` to prevent chaining more than 1 flight per iteration.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Swim in Rising Water (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Min-Max Dijkstra / Binary Search + BFS
- **Problem Statement & Constraints:** Least time until you can reach bottom-right where at time $t$ water depth is $t$ and you can only swim to cells with height $\le t$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Elevation grid $\to$ min water elevation to reach end.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Dijkstra Approach ($O(N^2 \log N)$ Time):** Min-heap of `(time, r, c)`. Time to enter cell $(nr, nc)$ is $\max(currTime, grid[nr][nc])$. First time reaching $(N-1, N-1)$ is optimal answer.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Shortest Path to Get All Keys (Difficulty: Hard)
- **Pattern Category:** Pattern 4: BFS with Bitmask State `(r, c, keyMask)`
- **Problem Statement & Constraints:** Minimum steps to collect all keys ('a'-'f') on grid with locks ('A'-'F').
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Maze with keys and locks $\to$ min steps.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal BFS Approach ($O(R \cdot C \cdot 2^K)$ Time and Space):** State in queue is `(r, c, keyMask)`. 3D visited boolean array `visited[R][C][1 << K]`. Can move to lock cell only if `(keyMask & (1 << (lock - 'A'))) != 0`. When reaching key, update `keyMask |= (1 << (key - 'a'))`. Target reached when `keyMask == (1 << K) - 1`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 5: Disjoint Set Union (DSU) & Minimum Spanning Tree

---

### Problem #25: Redundant Connection (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Undirected Cycle Detection via DSU
- **Problem Statement & Constraints:** Return edge that creates cycle in undirected graph.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* edges = [[1,2],[1,3],[2,3]] $\to$ Output: `[2,3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot \alpha(N))$ Time, $O(N)$ Space):** DSU with path compression and rank. For each edge $(u, v)$: if `!union(u, v)` (roots match), edge creates cycle $\implies$ return edge.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #26: Accounts Merge (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Email Disjoint Set Union
- **Problem Statement & Constraints:** Merge accounts belonging to same person if they share an email.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Overlapping email accounts merged and sorted.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** Map each email to an account index. If an email was already seen in an earlier account, `union(prevAccountIdx, currAccountIdx)`. Group emails by root parent account index. Sort emails in each merged account.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #27: Number of Operations to Make Network Connected (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Redundant Cable Counting DSU
- **Problem Statement & Constraints:** Can you connect all computers with given cables? Return minimum cable moves.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Redundant cables moved to connect isolated nodes $\to$ count moves.
  - *Test Case 2 (Insufficient Cables):* Total cables $< n - 1 \to$ Output: `-1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(V + E)$ Time, $O(V)$ Space):** Need at least $n - 1$ total edges. Run DSU: count redundant edges (edges where `rootU == rootV`). Count disconnected components $C$. If redundant edges $\ge C - 1$, answer is $C - 1$; else -1.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #28: Min Cost to Connect All Points (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Kruskal's / Prim's Minimum Spanning Tree
- **Problem Statement & Constraints:** Minimum cost to connect all 2D points using Manhattan distance.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* points = [[0,0],[2,2],[3,10],[5,2],[7,0]] $\to$ Output: `20`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Prim's Approach ($O(N^2)$ Time, $O(N)$ Space):** Dense graph with $N(N-1)/2$ edges. Maintain `minDist[N]` array. Iteratively select unvisited node with smallest distance, mark visited, add to total cost, and update distances to all remaining unvisited nodes.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #29: Satisfiability of Equality Equations (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Two-Pass Equality DSU
- **Problem Statement & Constraints:** Given array of equations like `"a==b"` and `"b!=c"`, return true if valid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Valid):* ["a==b","b==c","a==c"] $\to$ `true`.
  - *Test Case 2 (Contradiction):* ["a==b","b!=a"] $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** DSU of 26 letters. Pass 1: For all equations with `"=="`, perform `union(u, v)`. Pass 2: For all equations with `"!="`, check if `find(u) == find(v)`. If yes, contradiction found $\implies$ return false.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #30: Connecting Cities With Minimum Cost (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Kruskal's MST on Edge List
- **Problem Statement & Constraints:** Minimum cost to connect all $n$ cities. If impossible, return -1.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 3$, connections = [[1,2,5],[1,3,6],[2,3,1]] $\to$ Output: `6`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Kruskal's Approach ($O(E \log E)$ Time, $O(V)$ Space):** Sort edges ascending by cost. DSU over $n$ cities. For each edge, if `union(u, v)` succeeds, add cost and decrement component count. If components reduce to 1, return total cost; else -1.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
