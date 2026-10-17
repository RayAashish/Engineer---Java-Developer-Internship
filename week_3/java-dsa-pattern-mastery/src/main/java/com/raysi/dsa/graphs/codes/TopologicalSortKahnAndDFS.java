package com.raysi.dsa.graphs.codes;

import java.util.*;

/**
 * ============================================================================
 * PATTERN: Topological Sort (Kahn's BFS vs DFS Post-Order)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.graphs.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Topological Sort exists IF AND ONLY IF the graph is a Directed Acyclic Graph (DAG).
 * 2. Kahn's Algorithm (BFS In-Degree Reduction):
 *    - Compute in-degree for all vertices.
 *    - Enqueue all vertices with in-degree == 0 (no prerequisites).
 *    - Poll vertex u, append to result. For every neighbor v, decrement inDegree[v]--.
 *      If inDegree[v] reaches 0, enqueue v.
 *    - If processed node count != V, a cycle exists!
 * 3. DFS Post-Order:
 *    - Nodes with no outgoing edges complete first.
 *    - Push to stack upon recursion completion. Reverse/pop stack yields valid topological order.
 * ============================================================================
 */
public class TopologicalSortKahnAndDFS {

    /**
     * Course Schedule II (LC 210) via Kahn's Algorithm.
     * Returns valid ordering, or empty array if cycle exists.
     * Time Complexity : O(V + E)
     * Space Complexity: O(V + E)
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        int[] inDegree = new int[numCourses];
        for (int[] pre : prerequisites) {
            int dest = pre[0];
            int src = pre[1];
            adj.get(src).add(dest);
            inDegree[dest]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        int[] order = new int[numCourses];
        int index = 0;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            order[index++] = u;

            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // If cycle exists, some nodes never reach in-degree 0
        return (index == numCourses) ? order : new int[0];
    }

    /**
     * Alien Dictionary (LC 269): Derives character alphabet ordering.
     * Time Complexity : O(C) where C is total characters across all words
     * Space Complexity: O(1) auxiliary (at most 26 lowercase English letters)
     */
    public String alienOrder(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        // Initialize unique letters
        for (String word : words) {
            for (char c : word.toCharArray()) {
                inDegree.putIfAbsent(c, 0);
                adj.putIfAbsent(c, new HashSet<>());
            }
        }

        // Build directed edges by comparing adjacent words
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            // Edge case: invalid prefix ordering (e.g. "abc" before "ab")
            if (w1.length() > w2.length() && w1.startsWith(w2)) {
                return "";
            }

            for (int j = 0; j < Math.min(w1.length(), w2.length()); j++) {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);

                if (c1 != c2) {
                    if (!adj.get(c1).contains(c2)) {
                        adj.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    break; // First differing character defines order; subsequent chars ignored
                }
            }
        }

        // Kahn's BFS
        Queue<Character> queue = new ArrayDeque<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            char u = queue.poll();
            sb.append(u);

            for (char v : adj.get(u)) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    queue.offer(v);
                }
            }
        }

        return (sb.length() == inDegree.size()) ? sb.toString() : "";
    }
}
