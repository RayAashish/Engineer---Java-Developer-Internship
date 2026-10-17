package com.raysi.dsa.graphs.codes;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * ============================================================================
 * PATTERN: Connected Components, Flood Fill & Multi-Source BFS
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.graphs.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Connected Components: An outer loop iterates through all nodes 0 to V-1.
 *    If node is not visited, increment component count and launch DFS/BFS to
 *    visit all reachable vertices in that component.
 * 2. Grid Flood Fill (In-Place Sinking):
 *    Sink visited land ('1' -> '0') to avoid maintaining a separate visited grid.
 * 3. Multi-Source BFS:
 *    When events happen simultaneously across multiple origins (e.g. rotting oranges,
 *    nearest gate, zombie virus):
 *    - Enqueue ALL initial sources at minute 0.
 *    - Run level-order BFS. Each level represents 1 elapsed unit of time.
 * ============================================================================
 */
public class ConnectedComponentsAndGridFloodFill {

    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Number of Islands (LC 200).
     * Time Complexity : O(R * C)
     * Space Complexity: O(R * C) recursion stack in worst case
     */
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;
        int islandCount = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '1') {
                    islandCount++;
                    sinkIslandDFS(grid, r, c, rows, cols);
                }
            }
        }

        return islandCount;
    }

    private void sinkIslandDFS(char[][] grid, int r, int c, int rows, int cols) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != '1') {
            return;
        }

        grid[r][c] = '0'; // Sink land cell

        for (int[] d : DIRS) {
            sinkIslandDFS(grid, r + d[0], c + d[1], rows, cols);
        }
    }

    /**
     * Rotting Oranges (LC 994) via Multi-Source BFS.
     * Time Complexity : O(R * C)
     * Space Complexity: O(R * C)
     */
    public int orangesRotting(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        Queue<int[]> queue = new ArrayDeque<>();
        int freshCount = 0;

        // Step 1: Collect all rotten oranges into queue and count fresh oranges
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) {
                    queue.offer(new int[]{r, c});
                } else if (grid[r][c] == 1) {
                    freshCount++;
                }
            }
        }

        if (freshCount == 0) return 0;

        int minutes = 0;

        // Step 2: Multi-source BFS layer-by-layer
        while (!queue.isEmpty() && freshCount > 0) {
            int levelSize = queue.size();
            minutes++;

            for (int i = 0; i < levelSize; i++) {
                int[] curr = queue.poll();
                int r = curr[0];
                int c = curr[1];

                for (int[] d : DIRS) {
                    int nr = r + d[0];
                    int nc = c + d[1];

                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2; // Infect to rotten
                        freshCount--;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        return freshCount == 0 ? minutes : -1;
    }

    /**
     * Number of Connected Components in an Undirected Graph (LC 323).
     * Time Complexity : O(V + E)
     * Space Complexity: O(V + E)
     */
    public int countComponents(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfsComponent(i, adj, visited);
            }
        }

        return count;
    }

    private void dfsComponent(int u, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfsComponent(v, adj, visited);
            }
        }
    }
}
