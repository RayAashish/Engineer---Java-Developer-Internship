package com.raysi.dsa.graphs.codes;

import java.util.*;

/**
 * ============================================================================
 * PATTERN: Dijkstra's Shortest Path & Disjoint Set Union (Union-Find)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.graphs.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Disjoint Set Union (DSU) with Path Compression & Union by Rank:
 *    - find(i): Flattens tree by pointing parent[i] directly to root -> O(alpha(N)) ~ O(1).
 *    - union(i, j): Attaches shallower tree to deeper tree root.
 *    - Detects dynamic connectivity and cycles in undirected graphs.
 * 
 * 2. Dijkstra's Algorithm:
 *    - Finds single-source shortest path on graphs with NON-NEGATIVE edge weights.
 *    - Greedy choice: Min-Heap pops node with smallest known distance.
 *    - Relaxation: if dist[u] + weight < dist[v], update dist[v] and offer to heap.
 *    - Time: O((V + E) log V).
 * ============================================================================
 */
public class DijkstraAndDisjointSetUnion {

    /**
     * Highly optimized Disjoint Set Union (DSU) with Path Compression and Rank.
     */
    public static class DSU {
        private final int[] parent;
        private final int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]); // Path compression
        }

        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI == rootJ) return false; // Already in same set -> cycle

            // Union by rank
            if (rank[rootI] < rank[rootJ]) {
                parent[rootI] = rootJ;
            } else if (rank[rootI] > rank[rootJ]) {
                parent[rootJ] = rootI;
            } else {
                parent[rootJ] = rootI;
                rank[rootI]++;
            }
            return true;
        }
    }

    /**
     * Redundant Connection (LC 684) using DSU.
     * Returns edge that creates a cycle in tree.
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        DSU dsu = new DSU(n + 1);

        for (int[] edge : edges) {
            if (!dsu.union(edge[0], edge[1])) {
                return edge; // First edge connecting already-connected vertices
            }
        }

        return new int[0];
    }

    /**
     * Dijkstra's Algorithm: Network Delay Time (LC 743).
     * Time Complexity : O(E log V)
     * Space Complexity: O(V + E)
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> adj = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());

        for (int[] time : times) {
            int u = time[0];
            int v = time[1];
            int w = time[2];
            adj.get(u).add(new int[]{v, w});
        }

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        // Min-heap storing int[] {node, distance}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{k, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];
            int d = curr[1];

            // If current popped distance is greater than best known, prune stale entry
            if (d > dist[u]) continue;

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
            if (dist[i] == Integer.MAX_VALUE) return -1; // Unreachable node
            maxTime = Math.max(maxTime, dist[i]);
        }

        return maxTime;
    }
}
