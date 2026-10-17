package com.raysi.dsa.graphs.codes;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: Cycle Detection (Directed vs Undirected) & Bipartite Graph
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.graphs.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Undirected Cycle Detection (Parent Tracking):
 *    In an undirected graph, an edge (u - v) trivially goes both ways.
 *    A cycle exists iff we reach an already-visited neighbor `v` that is NOT the parent:
 *      if (visited[v] && v != parent) -> CYCLE!
 * 
 * 2. Directed Cycle Detection (3-Color / Recursion Stack):
 *    Cannot use simple parent tracking because directed paths have orientation.
 *    Use 3 states:
 *      0 = UNVISITED
 *      1 = VISITING (currently active in current DFS call stack -> back-edge!)
 *      2 = VISITED  (completely processed, cycle-free subtree)
 *    If an edge points to a node in state 1 (VISITING) -> BACK-EDGE CYCLE!
 * 
 * 3. Bipartite Graph (2-Coloring):
 *    Graph is bipartite iff it contains NO odd-length cycles.
 *    Color nodes using colors 1 and -1. If neighbor has same color -> not bipartite!
 * ============================================================================
 */
public class CycleDetectionDirectedAndUndirected {

    /**
     * Detects cycle in an undirected graph using DFS with parent pointer.
     * Time Complexity : O(V + E)
     * Space Complexity: O(V)
     */
    public boolean hasCycleUndirected(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (dfsUndirected(i, -1, adj, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsUndirected(int u, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;

        for (int v : adj.get(u)) {
            if (!visited[v]) {
                if (dfsUndirected(v, u, adj, visited)) return true;
            } else if (v != parent) {
                return true; // Visited node that is not the direct parent -> cycle
            }
        }
        return false;
    }

    /**
     * Course Schedule I (LC 207): Detects cycle in a directed graph using 3-Coloring.
     * Time Complexity : O(V + E)
     * Space Complexity: O(V + E)
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        for (int[] pre : prerequisites) {
            adj.get(pre[1]).add(pre[0]); // pre[1] -> pre[0]
        }

        // 0 = Unvisited, 1 = Visiting, 2 = Visited
        int[] state = new int[numCourses];

        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0) {
                if (hasCycleDirected(i, adj, state)) {
                    return false; // Cycle detected -> impossible to finish
                }
            }
        }

        return true;
    }

    private boolean hasCycleDirected(int u, List<List<Integer>> adj, int[] state) {
        state[u] = 1; // Mark VISITING (in recursion stack)

        for (int v : adj.get(u)) {
            if (state[v] == 1) {
                return true; // Found back-edge to ancestor in active path -> Cycle!
            }
            if (state[v] == 0 && hasCycleDirected(v, adj, state)) {
                return true;
            }
        }

        state[u] = 2; // Mark VISITED (done and safe)
        return false;
    }

    /**
     * Is Graph Bipartite (LC 785) via 2-Coloring.
     * Time Complexity : O(V + E)
     * Space Complexity: O(V)
     */
    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] colors = new int[n]; // 0 = uncolored, 1 = red, -1 = blue

        for (int i = 0; i < n; i++) {
            if (colors[i] == 0) {
                if (!colorDFS(i, 1, graph, colors)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean colorDFS(int u, int color, int[][] graph, int[] colors) {
        colors[u] = color;

        for (int v : graph[u]) {
            if (colors[v] == color) {
                return false; // Neighbor has identical color -> odd cycle!
            }
            if (colors[v] == 0 && !colorDFS(v, -color, graph, colors)) {
                return false;
            }
        }
        return true;
    }
}
