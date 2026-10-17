package com.raysi.dsa.trees.codes;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

/**
 * ============================================================================
 * PATTERN: Binary Search Tree (BST) Invariants & Mutations
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.trees.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. BST Invariant:
 *    For every node: ALL nodes in left subtree < node.val < ALL nodes in right subtree.
 *    Validating locally (node.left.val < node.val) is INSUFFICIENT.
 *    Must pass down allowable bounds: (min < node.val < max).
 * 2. In-Order Property:
 *    An in-order traversal of a valid BST produces a STRICTLY ASCENDING sorted sequence.
 * 3. Deletion in BST (3 Cases):
 *    - Leaf node: return null to parent.
 *    - Single child: return child to parent.
 *    - Two children: find in-order successor (smallest node in right subtree),
 *      copy successor's value to current node, then delete successor from right subtree.
 * ============================================================================
 */
public class BSTValidateInsertAndKthSmallest {

    private int kthCount = 0;
    private int kthResult = -1;

    /**
     * Validate Binary Search Tree (LC 98).
     * Time Complexity : O(N)
     * Space Complexity: O(H)
     */
    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;

        if (node.val <= min || node.val >= max) {
            return false;
        }

        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }

    /**
     * Insert into a Binary Search Tree (LC 701).
     * Time Complexity : O(H)
     * Space Complexity: O(1) iterative
     */
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);

        TreeNode curr = root;
        while (true) {
            if (val < curr.val) {
                if (curr.left == null) {
                    curr.left = new TreeNode(val);
                    break;
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    curr.right = new TreeNode(val);
                    break;
                }
                curr = curr.right;
            }
        }

        return root;
    }

    /**
     * Delete Node in a BST (LC 450).
     * Time Complexity : O(H)
     * Space Complexity: O(H)
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;

        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right, key);
        } else {
            // Case 1 & 2: 0 or 1 child
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            // Case 3: 2 children -> replace with inorder successor (min in right subtree)
            TreeNode successor = findMin(root.right);
            root.val = successor.val;
            root.right = deleteNode(root.right, successor.val);
        }

        return root;
    }

    private TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Kth Smallest Element in a BST (LC 230).
     * Time Complexity : O(H + K)
     * Space Complexity: O(H)
     */
    public int kthSmallest(TreeNode root, int k) {
        kthCount = k;
        kthResult = -1;
        inorderKth(root);
        return kthResult;
    }

    private void inorderKth(TreeNode node) {
        if (node == null || kthResult != -1) return;

        inorderKth(node.left);

        kthCount--;
        if (kthCount == 0) {
            kthResult = node.val;
            return;
        }

        inorderKth(node.right);
    }
}
