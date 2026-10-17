package com.raysi.dsa.trees.codes;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

import java.util.*;

/**
 * ============================================================================
 * PATTERN: Lowest Common Ancestor (LCA) & Coordinate-Based Tree Views
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.trees.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. LCA in General Binary Tree:
 *    - Base case: if root is null, p, or q, return root.
 *    - Recurse left and right.
 *    - If BOTH left and right returns are non-null -> current root is the LCA!
 *    - If only one return is non-null -> propagate that non-null node upwards.
 * 2. LCA in BST:
 *    - If both p.val and q.val < root.val -> LCA is in left subtree.
 *    - If both p.val and q.val > root.val -> LCA is in right subtree.
 *    - Else -> split point reached; current root is LCA!
 * 3. Top / Bottom View (Horizontal Distance HD):
 *    - Root has HD = 0. Left child = HD - 1. Right child = HD + 1.
 *    - Top View: First node visited at each HD in BFS.
 *    - Bottom View: Last node visited at each HD in BFS.
 * ============================================================================
 */
public class LowestCommonAncestorAndViews {

    /**
     * Lowest Common Ancestor of a Binary Tree (LC 236).
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) {
            return root; // p and q are in separate subtrees -> root is split point
        }

        return (left != null) ? left : right;
    }

    /**
     * Lowest Common Ancestor of a Binary Search Tree (LC 235).
     * Time Complexity : O(H)
     * Space Complexity: O(1) iterative
     */
    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode curr = root;

        while (curr != null) {
            if (p.val < curr.val && q.val < curr.val) {
                curr = curr.left;
            } else if (p.val > curr.val && q.val > curr.val) {
                curr = curr.right;
            } else {
                return curr; // Split point or matching node
            }
        }

        return null;
    }

    private static class QueueNode {
        TreeNode node;
        int hd; // horizontal distance
        QueueNode(TreeNode node, int hd) {
            this.node = node;
            this.hd = hd;
        }
    }

    /**
     * Top View of Binary Tree.
     * Time Complexity : O(N log N) with TreeMap (or O(N) with min/max HD tracking)
     * Space Complexity: O(N)
     */
    public List<Integer> topView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Map<Integer, Integer> topViewMap = new TreeMap<>();
        Queue<QueueNode> queue = new ArrayDeque<>();
        queue.offer(new QueueNode(root, 0));

        while (!queue.isEmpty()) {
            QueueNode curr = queue.poll();

            // First node at this horizontal distance is the top view
            topViewMap.putIfAbsent(curr.hd, curr.node.val);

            if (curr.node.left != null) {
                queue.offer(new QueueNode(curr.node.left, curr.hd - 1));
            }
            if (curr.node.right != null) {
                queue.offer(new QueueNode(curr.node.right, curr.hd + 1));
            }
        }

        result.addAll(topViewMap.values());
        return result;
    }

    /**
     * Bottom View of Binary Tree.
     * Time Complexity : O(N log N)
     * Space Complexity: O(N)
     */
    public List<Integer> bottomView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Map<Integer, Integer> bottomViewMap = new TreeMap<>();
        Queue<QueueNode> queue = new ArrayDeque<>();
        queue.offer(new QueueNode(root, 0));

        while (!queue.isEmpty()) {
            QueueNode curr = queue.poll();

            // Overwrite value so deepest/last node at each HD persists
            bottomViewMap.put(curr.hd, curr.node.val);

            if (curr.node.left != null) {
                queue.offer(new QueueNode(curr.node.left, curr.hd - 1));
            }
            if (curr.node.right != null) {
                queue.offer(new QueueNode(curr.node.right, curr.hd + 1));
            }
        }

        result.addAll(bottomViewMap.values());
        return result;
    }
}
