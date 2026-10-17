package com.raysi.dsa.trees.codes;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ============================================================================
 * PATTERN: Tree Breadth-First Search (BFS) & Level Order Mechanics
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.trees.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. The Queue Snapshot Idiom:
 *    To process nodes layer-by-layer, snapshot the current queue size:
 *      int levelSize = queue.size();
 *    Iterate exactly `levelSize` times. Any child nodes enqueued during this loop
 *    belong strictly to the NEXT level!
 * 2. Zigzag / Spiral Traversal:
 *    Maintain boolean `leftToRight`. If true, append to level list;
 *    if false, prepend (`levelList.addFirst(val)` using LinkedList).
 * 3. Right / Left Side View:
 *    The last element (or first element) in each level snapshot is the visible node.
 * ============================================================================
 */
public class BFSLevelOrderAndZigZagView {

    /**
     * Level Order Traversal (LC 102).
     * Time Complexity : O(N)
     * Space Complexity: O(W) where W is maximum tree width (up to N/2)
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>(levelSize);

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            result.add(currentLevel);
        }

        return result;
    }

    /**
     * Zigzag Level Order Traversal (LC 103).
     * Time Complexity : O(N)
     * Space Complexity: O(W)
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        boolean leftToRight = true;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            LinkedList<Integer> currentLevel = new LinkedList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                if (leftToRight) {
                    currentLevel.addLast(node.val);
                } else {
                    currentLevel.addFirst(node.val);
                }

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            result.add(currentLevel);
            leftToRight = !leftToRight; // Alternate direction
        }

        return result;
    }

    /**
     * Right Side View of Binary Tree (LC 199).
     * Time Complexity : O(N)
     * Space Complexity: O(W)
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                // If last node in current level, it is visible from the right side
                if (i == levelSize - 1) {
                    result.add(node.val);
                }

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }

        return result;
    }
}
