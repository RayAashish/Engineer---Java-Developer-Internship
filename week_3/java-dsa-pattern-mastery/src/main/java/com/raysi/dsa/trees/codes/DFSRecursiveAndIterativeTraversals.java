package com.raysi.dsa.trees.codes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ============================================================================
 * PATTERN: DFS Recursive, Iterative & Morris Tree Traversals
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.trees.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Tree Traversals defined by when the ROOT is processed:
 *    - Pre-Order : [Root] -> Left -> Right (Serialization, Prefix Expressions)
 *    - In-Order  : Left -> [Root] -> Right (Sorted order in BST)
 *    - Post-Order: Left -> Right -> [Root] (Bottom-up deletion, Tree DP, Height)
 * 2. Iterative Simulation with Deque Stack:
 *    - In-Order: Push left nodes until null. Pop top (visit root). Step right.
 * 3. Morris Traversal:
 *    - Achieves O(N) time and O(1) auxiliary space by temporarily converting null
 *      right pointers of in-order predecessors into "threads" pointing back to the root!
 * ============================================================================
 */
public class DFSRecursiveAndIterativeTraversals {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode() {}
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Iterative In-Order Traversal.
     * Time Complexity : O(N)
     * Space Complexity: O(H) where H is tree height
     */
    public List<Integer> inorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            // Traverse as far left as possible
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            curr = stack.pop();
            result.add(curr.val); // Process root

            curr = curr.right;    // Move to right subtree
        }

        return result;
    }

    /**
     * Iterative Pre-Order Traversal.
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public List<Integer> preorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);

            // Push right child first so left child is popped and processed first
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }

        return result;
    }

    /**
     * Iterative Post-Order Traversal (Single Stack with prev pointer).
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public List<Integer> postorderIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        TreeNode prev = null;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            TreeNode peekNode = stack.peek();
            // If right child exists and hasn't been processed yet, traverse right
            if (peekNode.right != null && prev != peekNode.right) {
                curr = peekNode.right;
            } else {
                result.add(peekNode.val);
                prev = stack.pop();
            }
        }

        return result;
    }

    /**
     * Morris In-Order Traversal: O(N) Time, strictly O(1) Auxiliary Space.
     * Rewires and restores tree pointers dynamically.
     */
    public List<Integer> morrisInorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                result.add(curr.val);
                curr = curr.right;
            } else {
                // Find in-order predecessor (rightmost node in left subtree)
                TreeNode pred = curr.left;
                while (pred.right != null && pred.right != curr) {
                    pred = pred.right;
                }

                if (pred.right == null) {
                    pred.right = curr; // Create temporary thread
                    curr = curr.left;
                } else {
                    pred.right = null; // Sever thread (restore tree)
                    result.add(curr.val);
                    curr = curr.right;
                }
            }
        }

        return result;
    }
}
