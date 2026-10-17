# Phase 8: Graphs — Representation, Algorithm Selector & Shortest Paths
> **Author:** raysi  
> **Package:** `com.raysi.dsa.graphs`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master graph representations, BFS/DFS traversal invariants, topological sorting, Dijkstra, and Disjoint Set Union (DSU).

---

## 1. Graph Mental Model & The Master Algorithm Selector

### 1.1 Adjacency Matrix vs Adjacency List Memory Mechanics

| Property | Adjacency Matrix `int[V][V]` | Adjacency List `List<List<Integer>>` |
| :--- | :--- | :--- |
| **Space Complexity** | $O(V^2)$ contiguous table | $O(V + E)$ object list of lists |
| **Memory for $V = 10^5$** | $10^{10} \times 4$ bytes $\approx$ **40 GB RAM** (`OutOfMemoryError`) | $10^5$ nodes + edges $\approx$ **~10 MB RAM** |
| **Edge Lookup $(u, v)$** | $O(1)$ direct array index | $O(\text{deg}(u))$ scan of list |
| **Iterate All Neighbors** | $O(V)$ must scan entire row | $O(\text{deg}(u))$ exact neighbor count |
| **Best Used When** | Extremely dense graphs ($E \approx V^2$) | Sparse graphs ($E \ll V^2$) — **98% of interviews** |

---

### 1.2 The Master Algorithm Selector Flowchart
When given a graph problem, immediately classify it using this decision tree:

```
                            +--------------------------+
                            | What does problem ask?   |
                            +--------------------------+
                                          |
          +-----------------+-------------+-----------------+-----------------+
          |                 |                               |                 |
+-------------------+ +-------------------+       +-------------------+ +-------------------+
| Traversal /       | | Dependency Order  |       | Shortest Path     | | Dynamic           |
| Connectivity      | | or Cycle in DAG   |       | Finding           | | Connectivity / MST|
+-------------------+ +-------------------+       +-------------------+ +-------------------+
  Unweighted Grid?      Kahn's Algorithm            Unweighted: BFS       DSU (Union-Find)   
  -> BFS / Flood Fill   (In-degree Queue)           Weighted >= 0:        -> Kruskal's MST   
  Components?           DFS Post-Order              -> Dijkstra           Redundant Edge     
  -> DFS / BFS Loop                                 Negative: Bellman     Accounts Merge     
```

#### Detailed Algorithm Complexity Matrix:
1. **Unweighted Shortest Path:** BFS $\implies O(V + E)$ Time, $O(V)$ Space.
2. **Non-Negative Weighted Shortest Path:** Dijkstra $\implies O((V + E) \log V)$ Time.
3. **Negative Edge Weights (Single Source):** Bellman-Ford $\implies O(V \cdot E)$ Time.
4. **All-Pairs Shortest Path:** Floyd-Warshall $\implies O(V^3)$ Time, $O(V^2)$ Space.
5. **Topological Sort:** Kahn's (BFS) / DFS $\implies O(V + E)$ Time, $O(V)$ Space.
6. **Minimum Spanning Tree:** Kruskal's (DSU) $\implies O(E \log E)$, Prim's $\implies O(E \log V)$.

---

### 1.3 3-Color Cycle Detection in Directed Graphs
Unlike undirected graphs where parent tracking `v != parent` suffices, directed graphs require tracking whether a node is in the **active recursion call stack**:
- **State 0 (WHITE):** Unvisited.
- **State 1 (GRAY):** Visiting (actively present in current DFS recursion path).
- **State 2 (BLACK):** Visited (completely explored, cycle-free).

$$\text{If an outgoing edge points to a node in State 1 (GRAY)} \implies \text{\textbf{BACK-EDGE CYCLE DETECTED!}}$$

---

### 1.4 Constraints to Big-O Table for Graphs

| Vertices ($V$) & Edges ($E$) | Permissible Big-O | Eligible Algorithm |
| :--- | :--- | :--- |
| $V \le 400$ | $O(V^3)$ | Floyd-Warshall All-Pairs |
| $V \le 2000, E \le 5000$ | $O(V \cdot E)$ | Bellman-Ford |
| $V, E \le 10^5$ | $O(V + E)$ or $O(E \log V)$ | BFS, DFS, Kahn's TopoSort, Dijkstra, DSU |

---

## 2. The 4 Core Graph Patterns

```
                                  +---------------------------+
                                  |    Core Graph Patterns    |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: Grid   | | Pattern 2: Cycle  | | Pattern 3: Kahn's | | Pattern 4: Greedy |
| Multi-Source BFS  | | & Bipartite Check | | Topo Sort (DAG)   | | Dijkstra & DSU    |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  Enqueue all roots     Undirected: parent    inDegree[v] array     PriorityQueue dist  
  Simultaneous spread   Directed: 3 colors    Queue inDegree == 0   Relaxation step     
  Rotting Oranges       2-Color Bipartite     Prerequisite courses  Path compression    
```

---

### Pattern 1: Grid Multi-Source BFS & Flood Fill
- **Trigger Keywords:** `"rotting oranges"`, `"shortest distance from all gates"`, `"01 matrix"`.
- **Mental Model:** Multiple sources ignite at $t = 0$. Push ALL sources to queue simultaneously before starting while-loop. Run standard level-order BFS; each layer is $+1$ second.

---

### Pattern 2: Cycle Detection & Bipartite Verification
- **Trigger Keywords:** `"course schedule"`, `"is graph bipartite"`, `"detect cycle"`.
- **Mental Model:** 
  - Directed: 3-color array (`0 = unvisited, 1 = visiting, 2 = visited`).
  - Bipartite: Color with 1 and -1. If neighbor has identical color, an odd-length cycle exists $\implies$ not bipartite.

---

### Pattern 3: Topological Sorting (Kahn's Algorithm)
- **Trigger Keywords:** `"course schedule II"`, `"alien dictionary"`, `"task order"`.
- **Mental Model:** Directed dependencies. Compute `inDegree[]`. Nodes with `inDegree == 0` have no dependencies and are ready for processing. Popping node $u$ decrements `inDegree[v]--` for all its neighbors.

---

### Pattern 4: Greedy Shortest Path (Dijkstra) & DSU
- **Trigger Keywords:** `"minimum cost to reach"`, `"network delay time"`, `"redundant connection"`.
- **Mental Model:**
  - Dijkstra: Min-Heap of `(node, currentDist)`. Sift-up lowest distance first. Skip stale entries: `if (d > dist[u]) continue;`.
  - DSU: Track disjoint components. `find(i)` with path compression; `union(i, j)` with rank.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Number of Islands (LeetCode 200)
> **Problem:** Given an $m \times n$ 2D binary grid $grid$ representing a map of '1's (land) and '0's (water), return the number of islands.

#### 1. Brute Force Instinct
Allocate a boolean `visited[m][n]` array $\implies O(R \cdot C)$ extra space.

#### 2. "Aha!" Observation
In-place sinking! When encountering `'1'`, increment island count, then launch DFS to mutate all 4-directionally connected `'1'`s into `'0'`s. Eliminates visited array entirely.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.graphs;

public class NumberOfIslandsAnchor {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    sink(grid, r, c, rows, cols);
                }
            }
        }
        return count;
    }

    private void sink(char[][] grid, int r, int c, int rows, int cols) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != '1') return;

        grid[r][c] = '0'; // Sink to water

        for (int[] d : DIRS) {
            sink(grid, r + d[0], c + d[1], rows, cols);
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C)$
- **Space Complexity:** $O(R \cdot C)$ recursion stack in worst-case (all land).

---

### Anchor Problem #2: Rotting Oranges (LeetCode 994)
> **Problem:** Every minute, any fresh orange 4-directionally adjacent to a rotten orange becomes rotten. Return minimum minutes until no fresh orange remains, or -1.

#### 1. Brute Force Instinct
Repeatedly scan the entire grid minute-by-minute until no changes occur $\implies O((R \cdot C)^2)$ time.

#### 2. "Aha!" Observation
Multi-Source BFS!
- Step 1: Enqueue ALL initial rotten oranges (cells with value 2) at time 0. Count total fresh oranges.
- Step 2: Run level-by-level BFS. Each level increment represents 1 minute.
- Step 3: If `freshCount == 0`, return elapsed minutes; else -1.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

public class RottingOrangesAnchor {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public int orangesRotting(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        Queue<int[]> queue = new ArrayDeque<>();
        int fresh = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(new int[]{r, c});
                else if (grid[r][c] == 1) fresh++;
            }
        }

        if (fresh == 0) return 0;
        int minutes = 0;

        while (!queue.isEmpty() && fresh > 0) {
            int levelSize = queue.size();
            minutes++;

            for (int i = 0; i < levelSize; i++) {
                int[] curr = queue.poll();
                for (int[] d : DIRS) {
                    int nr = curr[0] + d[0];
                    int nc = curr[1] + d[1];

                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2;
                        fresh--;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        return fresh == 0 ? minutes : -1;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(R \cdot C)$
- **Space Complexity:** $O(R \cdot C)$

---

### Anchor Problem #3: Course Schedule II (LeetCode 210)
> **Problem:** Given $numCourses$ and $prerequisites$, return the ordering of courses you should take to finish all courses. If impossible due to cycle, return empty array.

#### 1. Brute Force Instinct
Try all permutations of courses $\implies O(N!)$.

#### 2. "Aha!" Observation
Kahn's Algorithm (BFS Topological Sort):
- Build graph and `inDegree[]`.
- Enqueue all courses with `inDegree == 0`.
- Process queue: for each course $u$, decrement neighbors' in-degree. When neighbor reaches 0, enqueue.
- If total processed courses $== numCourses$, return ordering; else graph has cycle $\implies$ return `new int[0]`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.graphs;

import java.util.*;

public class CourseScheduleIIAnchor {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        int[] inDegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int[] order = new int[numCourses];
        int idx = 0;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            order[idx++] = u;

            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        return (idx == numCourses) ? order : new int[0];
    }
}
```
*Complexity:*
- **Time Complexity:** $O(V + E)$
- **Space Complexity:** $O(V + E)$

---

### Anchor Problem #4: Network Delay Time (LeetCode 743)
> **Problem:** Directed weighted graph of $n$ nodes. Return minimum time for signal sent from node $k$ to reach all nodes. If impossible, return -1.

#### 1. Brute Force Instinct
DFS along all paths keeping minimum distances $\implies$ exponential worst-case time.

#### 2. "Aha!" Observation
Dijkstra's Algorithm:
- Min-Heap storing `(node, distance)`.
- Initialize `dist[]` array with `Integer.MAX_VALUE`, `dist[k] = 0`.
- If `d > dist[u]`, continue (stale heap entry).
- Relax edges: if `dist[u] + w < dist[v]`, update `dist[v]` and push to heap.
- Answer is $\max_{i=1 \dots n}(dist[i])$.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.graphs;

import java.util.*;

public class NetworkDelayTimeAnchor {
    public int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> adj = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());

        for (int[] time : times) {
            adj.get(time[0]).add(new int[]{time[1], time[2]});
        }

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{k, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];

            if (d > dist[u]) continue; // Stale entry

            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int weight = edge[1];

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }

        int maxTime = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1;
            maxTime = Math.max(maxTime, dist[i]);
        }
        return maxTime;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(E \log V)$
- **Space Complexity:** $O(V + E)$

---

### Anchor Problem #5: Redundant Connection (LeetCode 684)
> **Problem:** Given an undirected graph that started as a tree with $n$ nodes and an additional edge added. Return the edge that can be removed so the graph becomes a tree.

#### 1. Brute Force Instinct
For each edge, remove it and run DFS from node 1 to check if all $n$ nodes remain connected $\implies O(E \cdot (V + E)) = O(N^2)$.

#### 2. "Aha!" Observation
Disjoint Set Union (Union-Find):
- Start with $n$ disjoint sets.
- Iterate edges one-by-one:
  - If `find(u) == find(v)`, $u$ and $v$ are ALREADY connected in the tree! Adding this edge creates a cycle $\implies$ this is the redundant edge!
  - Otherwise, `union(u, v)`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.graphs;

public class RedundantConnectionAnchor {
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1];
        for (int i = 1; i <= n; i++) parent[i] = i;

        for (int[] edge : edges) {
            int rootU = find(parent, edge[0]);
            int rootV = find(parent, edge[1]);

            if (rootU == rootV) {
                return edge; // Cycle detected!
            }
            parent[rootU] = rootV;
        }

        return new int[0];
    }

    private int find(int[] parent, int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent, parent[i]); // Path compression
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \cdot \alpha(N)) \approx O(N)$
- **Space Complexity:** $O(N)$ for parent array.

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Stale PriorityQueue Entries in Dijkstra:**
   - Java's `PriorityQueue` does not support $O(\log N)$ `decreaseKey()`. We push updated distance tuples `(v, newDist)` into the queue.
   - Without `if (d > dist[u]) continue;`, outdated entries with worse distances will be fully expanded, ballooning runtime to exponential in dense graphs!
2. **Disconnected Components in BFS/DFS:**
   - In graph connectivity problems, launching a single BFS/DFS from node 0 fails if the graph has multiple disconnected components. Always wrap with an outer loop from $0$ to $V - 1$.
3. **Directed vs Undirected Cycle Confusion:**
   - Checking `v != parent` only works on **undirected** graphs. On directed graphs, it fails completely because a directed path can reach a visited node without forming a cycle (e.g., diamond DAG $A \to B \to D, A \to C \to D$). Use 3-color states on directed graphs.
