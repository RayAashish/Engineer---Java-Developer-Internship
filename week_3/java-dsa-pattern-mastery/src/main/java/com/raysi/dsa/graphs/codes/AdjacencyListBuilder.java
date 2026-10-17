package com.raysi.dsa.graphs.codes;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: Graph Representation (Adjacency List vs Adjacency Matrix)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.graphs.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Adjacency Matrix (int[V][V]):
 *    - Memory: O(V^2). Unusable when V = 10^5 (10^10 ints = 40 GB RAM).
 *    - Lookup: O(1) edge existence check: matrix[u][v] != 0.
 *    - Best for dense graphs where E ~ V^2.
 * 
 * 2. Adjacency List (List<List<Integer>> or List<List<int[]>>):
 *    - Memory: O(V + E). Perfect for sparse graphs (E << V^2).
 *    - Iterating neighbors takes O(deg(u)) instead of O(V).
 *    - The default representation in 98% of technical coding interviews!
 * ============================================================================
 */
public class AdjacencyListBuilder {

    /**
     * Builds an unweighted directed/undirected adjacency list.
     * Time Complexity : O(V + E)
     * Space Complexity: O(V + E)
     */
    public List<List<Integer>> buildUnweightedGraph(int n, int[][] edges, boolean isDirected) {
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            adj.get(u).add(v);
            if (!isDirected) {
                adj.get(v).add(u);
            }
        }

        return adj;
    }

    /**
     * Builds a weighted directed/undirected adjacency list.
     * Each entry is an int[] {neighbor, weight}.
     */
    public List<List<int[]>> buildWeightedGraph(int n, int[][] edges, boolean isDirected) {
        List<List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];

            adj.get(u).add(new int[]{v, weight});
            if (!isDirected) {
                adj.get(v).add(new int[]{u, weight});
            }
        }

        return adj;
    }
}
