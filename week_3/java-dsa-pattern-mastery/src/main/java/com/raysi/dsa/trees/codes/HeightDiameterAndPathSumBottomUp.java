package com.raysi.dsa.trees.codes;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

/**
 * ============================================================================
 * PATTERN: Bottom-Up Post-Order DP (Diameter, Path Sum & Balance)
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.trees.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Bottom-Up Recursion (Post-Order):
 *    Subtrees solve their own subproblems and RETURN results UP to the parent node.
 *    The parent synthesizes child results:
 *      leftResult = dfs(node.left)
 *      rightResult = dfs(node.right)
 *      process(node, leftResult, rightResult)
 * 2. Diameter of Binary Tree:
 *    The longest path passing THROUGH `node` is (leftHeight + rightHeight).
 *    The value RETURNED UP to the parent is max(leftHeight, rightHeight) + 1.
 * 3. Max Path Sum:
 *    Branch contribution must be non-negative: Math.max(0, leftGain).
 *    Path through node = node.val + leftGain + rightGain.
 *    Return to parent = node.val + Math.max(leftGain, rightGain).
 * ============================================================================
 */
public class HeightDiameterAndPathSumBottomUp {

    private int maxDiameter = 0;
    private int maxPathSum = Integer.MIN_VALUE;

    /**
     * Maximum Depth / Height of Binary Tree.
     * Time Complexity : O(N)
     * Space Complexity: O(H) recursion stack
     */
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /**
     * Diameter of Binary Tree (LC 543).
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0;
        calculateHeight(root);
        return maxDiameter;
    }

    private int calculateHeight(TreeNode node) {
        if (node == null) return 0;

        int leftHeight = calculateHeight(node.left);
        int rightHeight = calculateHeight(node.right);

        // Longest path through current node in edges
        maxDiameter = Math.max(maxDiameter, leftHeight + rightHeight);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Binary Tree Maximum Path Sum (LC 124).
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public int maxPathSum(TreeNode root) {
        maxPathSum = Integer.MIN_VALUE;
        maxGain(root);
        return maxPathSum;
    }

    private int maxGain(TreeNode node) {
        if (node == null) return 0;

        // Discard negative branch gains
        int leftGain = Math.max(0, maxGain(node.left));
        int rightGain = Math.max(0, maxGain(node.right));

        // Max path passing through current node
        int currentPath = node.val + leftGain + rightGain;
        maxPathSum = Math.max(maxPathSum, currentPath);

        // Return max branch contribution to parent
        return node.val + Math.max(leftGain, rightGain);
    }

    /**
     * Determines if a binary tree is height-balanced (LC 110).
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public boolean isBalanced(TreeNode root) {
        return checkBalance(root) != -1;
    }

    private int checkBalance(TreeNode node) {
        if (node == null) return 0;

        int leftHeight = checkBalance(node.left);
        if (leftHeight == -1) return -1; // Left subtree is already imbalanced

        int rightHeight = checkBalance(node.right);
        if (rightHeight == -1) return -1; // Right subtree is already imbalanced

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1; // Current node violates balance invariant
        }

        return 1 + Math.max(leftHeight, rightHeight);
    }
}
