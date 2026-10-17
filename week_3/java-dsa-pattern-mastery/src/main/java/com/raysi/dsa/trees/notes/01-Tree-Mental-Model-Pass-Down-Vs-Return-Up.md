# Phase 7: Trees — Hierarchical Recursion, Pass-Down vs Return-Up & BST Invariants
> **Author:** raysi  
> **Package:** `com.raysi.dsa.trees`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master recursive data flow (top-down vs bottom-up), BFS level snapshots, LCA split-points, and BST invariant preservation.

---

## 1. Tree Mental Model & Recursive Data Flow Mechanics

### 1.1 The Two Fundamental Recursive Modes
Every recursive tree algorithm belongs to one of two information-flow paradigms:

```
Mode 1: Pass-Down (Top-Down / Pre-Order)      Mode 2: Return-Up (Bottom-Up / Post-Order)
           Parent [Context]                               Parent [Combines Children]
              /       \                                      ^         ^
             v         v                                    /           \
       Left Child   Right Child                     Left Result       Right Result
 (e.g., Validate BST (min, max bounds),        (e.g., Tree Height, Diameter,
  Path Sum target - root.val)                   Max Path Sum, Balanced Tree Check)
```

#### When to use which?
- **Pass Down:** When a child node's decision depends on ancestral decisions (e.g., "Am I less than the root's ancestor?", "What is my current depth?").
- **Return Up:** When a parent's answer depends on subtree results (e.g., "What is the max path through me?", "Is my subtree balanced?").

---

### 1.2 The BFS Queue Snapshot Paradigm
To traverse a tree layer-by-layer (Level Order):
```java
Queue<TreeNode> queue = new ArrayDeque<>();
queue.offer(root);

while (!queue.isEmpty()) {
    int levelSize = queue.size(); // CRITICAL SNAPSHOT
    for (int i = 0; i < levelSize; i++) {
        TreeNode curr = queue.poll();
        // Process curr
        if (curr.left != null) queue.offer(curr.left);
        if (curr.right != null) queue.offer(curr.right);
    }
}
```
**Why the snapshot is mandatory:** Without `int levelSize = queue.size()`, dynamically enqueued child nodes from level $L+1$ would merge with nodes from level $L$, corrupting the layer boundary.

---

### 1.3 Constraints to Big-O Table for Trees

| Tree Shape | Height ($H$) | Time (Traversal / Search) | Call Stack Space |
| :--- | :--- | :--- | :--- |
| **Balanced / Complete** | $O(\log N)$ | $O(N)$ Traversal / $O(\log N)$ Search | $O(\log N)$ |
| **Degenerate (Skewed / Linked List)** | $O(N)$ | $O(N)$ Traversal / $O(N)$ Search | $O(N)$ (Risk of `StackOverflowError`) |

---

## 2. The 4 Core Tree Patterns

```
                                  +---------------------------+
                                  |    Tree Decision Flow     |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: Bottom-| | Pattern 2: Top-   | | Pattern 3: BFS    | | Pattern 4: LCA    |
| Up Post-Order DP  | | Down Pass-Down    | | Level Snapshot    | | & BST Search      |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  Height, Diameter      Validate BST          Level Order           Split-Point logic   
  Max Path Sum          Path Sum I & II       Zigzag & Views        p.val < root < q.val
  Balanced Check        Root-to-Leaf          O(W) Queue space      General: L && R != null
```

---

### Pattern 1: Bottom-Up Post-Order DP
- **Trigger Keywords:** `"diameter"`, `"maximum path sum"`, `"is balanced"`, `"height"`.
- **Template:**
```java
private int postOrder(TreeNode node) {
    if (node == null) return 0;
    int left = postOrder(node.left);
    int right = postOrder(node.right);
    // 1. Update global answer across the arch through 'node'
    globalMax = Math.max(globalMax, computeArch(node, left, right));
    // 2. Return single branch contribution UP to parent
    return node.val + Math.max(left, right);
}
```

---

### Pattern 2: Top-Down Pass-Down Constraint Validation
- **Trigger Keywords:** `"validate binary search tree"`, `"path sum"`.
- **Template:**
```java
private boolean validate(TreeNode node, long min, long max) {
    if (node == null) return true;
    if (node.val <= min || node.val >= max) return false;
    return validate(node.left, min, node.val) && validate(node.right, node.val, max);
}
```

---

### Pattern 3: BFS Level Snapshot
- **Trigger Keywords:** `"level order"`, `"zigzag"`, `"right side view"`, `"minimum depth"`.
- **Mental Model:** Use FIFO queue with level snapshot size loop.

---

### Pattern 4: Lowest Common Ancestor & Split Point Search
- **Trigger Keywords:** `"lowest common ancestor"`, `"distance between two nodes"`.
- **Mental Model:**
  - General Tree: If current node matches $p$ or $q$, or if both left and right returns are non-null $\implies$ return current node.
  - BST: Search left if both $< root.val$; search right if both $> root.val$; otherwise current node is split point.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Maximum Depth of Binary Tree (LeetCode 104)
> **Problem:** Find the maximum depth (number of nodes along longest path from root down to farthest leaf).

#### 1. Brute Force Instinct
BFS level order counting total levels $\implies O(N)$ time and $O(W)$ space.

#### 2. "Aha!" Observation
Pure bottom-up recursion: depth of current node is $1 + \max(\text{depth}(left), \text{depth}(right))$.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.trees;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

public class MaxDepthAnchor {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(H)$ recursion stack.

---

### Anchor Problem #2: Diameter of Binary Tree (LeetCode 543)
> **Problem:** Return length of diameter of binary tree (longest path between any two nodes in edges).

#### 1. Brute Force Instinct
For each node, compute `maxDepth(node.left) + maxDepth(node.right)` $\implies O(N^2)$ due to repeated height calculations.

#### 2. "Aha!" Observation
Compute height bottom-up in $O(N)$. At each node, diameter through this node is $leftHeight + rightHeight$. Update global diameter tracker during the height calculation.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.trees;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

public class DiameterOfBinaryTreeAnchor {
    private int maxDiameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0;
        height(root);
        return maxDiameter;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;
        int lh = height(node.left);
        int rh = height(node.right);
        maxDiameter = Math.max(maxDiameter, lh + rh);
        return 1 + Math.max(lh, rh);
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — Single post-order pass.
- **Space Complexity:** $O(H)$ recursion stack.

---

### Anchor Problem #3: Binary Tree Maximum Path Sum (LeetCode 124)
> **Problem:** A path is any sequence of nodes with edges. Return maximum path sum of any non-empty path.

#### 1. Brute Force Instinct
Compute all paths between all pairs of nodes $\implies O(N^2)$ or higher.

#### 2. "Aha!" Observation
Bottom-up post-order:
- For child branches, if gain $< 0$, prune to 0 (`Math.max(0, gain)`).
- Path arching through current node: `node.val + leftGain + rightGain`. Update global maximum.
- Return to parent: `node.val + Math.max(leftGain, rightGain)` (can only extend ONE branch upwards).

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.trees;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

public class MaxPathSumAnchor {
    private int maxSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        gain(root);
        return maxSum;
    }

    private int gain(TreeNode node) {
        if (node == null) return 0;

        int left = Math.max(0, gain(node.left));
        int right = Math.max(0, gain(node.right));

        maxSum = Math.max(maxSum, node.val + left + right);

        return node.val + Math.max(left, right);
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(H)$

---

### Anchor Problem #4: Validate Binary Search Tree (LeetCode 98)
> **Problem:** Determine if a binary tree is a valid Binary Search Tree (BST).

#### 1. Brute Force Instinct
Check only immediate children: `node.left.val < node.val && node.right.val > node.val`.
*Fatal Flaw:* Fails when a right subtree contains a value smaller than the root ancestor!

#### 2. "Aha!" Observation
Top-down pass-down: maintain valid interval $(min, max)$ for every node.
- Left child interval: $(min, node.val)$.
- Right child interval: $(node.val, max)$.
- Use 64-bit `long` bounds `Long.MIN_VALUE` and `Long.MAX_VALUE` to handle 32-bit `Integer.MIN_VALUE` inputs.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.trees;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

public class ValidateBSTAnchor {
    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(H)$

---

### Anchor Problem #5: Lowest Common Ancestor of a Binary Tree (LeetCode 236)
> **Problem:** Find the lowest common ancestor (LCA) of two given nodes $p$ and $q$ in a binary tree.

#### 1. Brute Force Instinct
Find path from root to $p$ and path from root to $q$, store in lists, compare last matching node.
*Cost:* $O(N)$ time and $O(N)$ extra memory for path lists.

#### 2. "Aha!" Observation
Bottom-up search:
- If current node is null, $p$, or $q$, return current node.
- Recurse left and right.
- If both left and right return non-null, $p$ and $q$ are in separate subtrees $\implies$ current node is LCA!
- If only one returns non-null, propagate that non-null node upwards.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.trees;

import com.raysi.dsa.trees.codes.DFSRecursiveAndIterativeTraversals.TreeNode;

public class LCAAnchor {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) {
            return root;
        }

        return (left != null) ? left : right;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(H)$

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Integer Overflow in BST Validation:**
   - If a leaf node has `val = Integer.MAX_VALUE`, checking `node.val < max` where `max = Integer.MAX_VALUE` will incorrectly return `false`. Always use `long min = Long.MIN_VALUE, long max = Long.MAX_VALUE`.
2. **Diameter in Edges vs Nodes:**
   - Diameter in edges: $leftHeight + rightHeight$.
   - Diameter in nodes: $leftHeight + rightHeight + 1$. Double check problem statement.
3. **Negative Branch Contribution in Max Path Sum:**
   - Forgetting `Math.max(0, gain)` will cause negative subtrees to degrade the total path sum instead of being omitted.
