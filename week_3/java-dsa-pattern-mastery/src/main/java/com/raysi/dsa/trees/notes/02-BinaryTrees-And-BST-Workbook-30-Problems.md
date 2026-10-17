# Phase 7: Trees & BST — Self-Guided Problem Workbook (30 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.trees`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: DFS Recursive & Iterative Traversals

---

### Problem #1: Binary Tree Inorder Traversal (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Iterative Left-Spine Stack
- **Problem Statement & Constraints:** Return inorder traversal of binary tree nodes' values.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $root = [1,null,2,3] \to$ Output: `[1,3,2]`.
  - *Test Case 2 (Empty):* $root = [] \to$ Output: `[]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** While `curr != null || !stack.isEmpty()`: while `curr != null`, push `curr` and move `curr = curr.left`. Pop stack, record value, move `curr = curr.right`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Binary Tree Preorder Traversal (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Iterative Stack with Right-First Pushes
- **Problem Statement & Constraints:** Return preorder traversal of binary tree.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [1,null,2,3] \to$ Output: `[1,2,3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Push root to stack. While non-empty: pop node, record value, push `node.right` if non-null, then push `node.left` if non-null.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Binary Tree Postorder Traversal (Difficulty: Easy / Medium)
- **Pattern Category:** Pattern 1: Single Stack with Previous Node Tracking
- **Problem Statement & Constraints:** Return postorder traversal of binary tree.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [1,null,2,3] \to$ Output: `[3,2,1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Push down left spine. Check `stack.peek().right`: if exists and not equal to `prev`, step right; else pop, record value, update `prev`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Morris Inorder Traversal (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Threaded Binary Tree Pointer Inversion
- **Problem Statement & Constraints:** Traverse binary tree in-order in $O(N)$ time and $O(1)$ auxiliary space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Arbitrary binary tree $\to$ sorted output if BST.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** If `curr.left == null`, visit `curr` and move right. Else find inorder predecessor (rightmost node in left subtree). If predecessor's right is null, make thread `pred.right = curr` and move `curr = curr.left`. Else sever thread `pred.right = null`, visit `curr`, move `curr = curr.right`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Flatten Binary Tree to Linked List (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Preorder In-Place Rewiring
- **Problem Statement & Constraints:** Flatten tree into single linked list in-place where `right` child points to next node in preorder and `left` is null.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Binary tree $\to$ all nodes wired into right spine in preorder.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Morris-Style Approach ($O(N)$ Time, $O(1)$ Space):** `curr = root`. While `curr != null`: if `curr.left != null`, find predecessor (rightmost of left subtree). Connect `pred.right = curr.right`. Rewire `curr.right = curr.left; curr.left = null`. Move `curr = curr.right`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Populating Next Right Pointers in Each Node (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Level-by-Level Horizontal Spine Linking
- **Problem Statement & Constraints:** Populate each `next` pointer to point to its next right node in perfect binary tree in $O(1)$ extra space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Perfect binary tree $\to$ next pointers linked horizontally.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Traverse level by level using already-established `next` pointers. For node `curr`: connect `curr.left.next = curr.right`. If `curr.next != null`, connect `curr.right.next = curr.next.left`. Move `curr = curr.next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: BFS Level Order & Coordinate Views

---

### Problem #7: Binary Tree Level Order Traversal (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Queue Level Snapshot
- **Problem Statement & Constraints:** Return level order traversal of nodes' values.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [3,9,20,null,null,15,7] \to$ Output: `[[3],[9,20],[15,7]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(W)$ Space):** Queue. While non-empty: snapshot `levelSize = queue.size()`. Loop `levelSize` times polling node, recording value, enqueuing non-null children.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Binary Tree Zigzag Level Order Traversal (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Alternating Deque Insertion
- **Problem Statement & Constraints:** Level order traversal alternating left-to-right and right-to-left.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [3,9,20,null,null,15,7] \to$ Output: `[[3],[20,9],[15,7]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(W)$ Space):** Maintain boolean `leftToRight`. At each level snapshot, use `LinkedList` for current level: if `leftToRight`, `addLast()`; else `addFirst()`. Toggle flag after each level.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Binary Tree Right Side View (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Level Snapshot Tail Node
- **Problem Statement & Constraints:** Return values of nodes visible from right side.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [1,2,3,null,5,null,4] \to$ Output: `[1,3,4]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(W)$ Space):** In level snapshot loop, when index `i == levelSize - 1`, record `node.val`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Vertical Order Traversal of Binary Tree (Difficulty: Hard)
- **Pattern Category:** Pattern 2: $(col, row, val)$ Coordinate Multi-Key Sort
- **Problem Statement & Constraints:** Return vertical order traversal. Nodes in same column ordered by row ascending, then by value ascending.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Coordinates $(col, row)$ assigned properly.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):** BFS with triplet `(node, row, col)`. Store in list. Sort list by: (1) `col` ascending, (2) `row` ascending, (3) `val` ascending. Group by column.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Top View of Binary Tree (Difficulty: Medium)
- **Pattern Category:** Pattern 2: First-Seen Horizontal Distance BFS
- **Problem Statement & Constraints:** Return nodes visible from top view.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Tree with overlapping columns $\to$ topmost node per column.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** BFS queue with `(node, hd)`. `Map<Integer, Integer> topMap`. If `!topMap.containsKey(hd)`, put `(hd, node.val)`. Extract in order of sorted horizontal distance.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Bottom View of Binary Tree (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Last-Seen Horizontal Distance BFS
- **Problem Statement & Constraints:** Return nodes visible from bottom view.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Bottom-most node per column.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** BFS queue with `(node, hd)`. In BFS loop, unconditionally overwrite `bottomMap.put(hd, node.val)`. Deepest node at each column naturally overwrites earlier nodes.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Bottom-Up Post-Order DP & Structural Attributes

---

### Problem #13: Maximum Depth of Binary Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Height Recursion
- **Problem Statement & Constraints:** Find maximum depth.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Binary tree $\to$ max depth.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Base case: if null return 0. Return $1 + \max(maxDepth(left), maxDepth(right))$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Minimum Depth of Binary Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Leaf Node Depth Guard
- **Problem Statement & Constraints:** Find minimum depth to nearest leaf node.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Skewed Tree):* Root has no left child, right child has depth 5 $\to$ min depth is 6, NOT 1!
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** If root null, return 0. If left null, recurse right. If right null, recurse left. If both non-null, return $1 + \min(minDepth(left), minDepth(right))$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Balanced Binary Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: -1 Imbalance Propagation
- **Problem Statement & Constraints:** Determine if binary tree is height-balanced.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Balanced):* Balanced tree $\to$ `true`.
  - *Test Case 2 (Imbalanced):* Subtree height difference $> 1 \to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Post-order helper returning height. If left returns -1 or right returns -1 or $|left - right| > 1$, return -1. Else return $1 + \max(left, right)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Diameter of Binary Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Arch Path Maximization
- **Problem Statement & Constraints:** Longest path between any two nodes in edges.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Longest path passing through root.
  - *Test Case 2 (Subtree):* Longest path contained entirely in left subtree.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Post-order height calculation. Update `maxDiameter = max(maxDiameter, leftH + rightH)`. Return $1 + \max(leftH, rightH)$ up to parent.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Binary Tree Maximum Path Sum (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Branch Gain Pruning
- **Problem Statement & Constraints:** Maximum path sum of any non-empty path.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [-10,9,20,null,null,15,7] \to$ Output: `42` (15 + 20 + 7).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Post-order. Gain from child: $\max(0, gain)$. Arch through node: $node.val + leftGain + rightGain$. Update global max. Return to parent: $node.val + \max(leftGain, rightGain)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Subtree of Another Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Tree Isomorphism Match
- **Problem Statement & Constraints:** Check if $subRoot$ is a subtree of $root$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Subtree matches $\to$ `true`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot M)$ Time):** If root null, return false. If `isSameTree(root, subRoot)`, return true. Else recurse `isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Invert Binary Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Child Reference Swap
- **Problem Statement & Constraints:** Invert binary tree and return its root.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Left and right subtrees mirror-swapped.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Post-order: invert left, invert right. Swap: `TreeNode temp = root.left; root.left = root.right; root.right = temp;`. Return root.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Symmetric Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Dual Mirror Pointer Recursion
- **Problem Statement & Constraints:** Check whether tree is mirror of itself.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Symmetric):* Root children mirror each other $\to$ `true`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Helper `isMirror(t1, t2)`. If both null, return true; if one null, return false; if $t1.val \ne t2.val$, return false. Return `isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Top-Down Pass-Down & Root-to-Leaf Paths

---

### Problem #21: Path Sum I (Difficulty: Easy)
- **Pattern Category:** Pattern 4: Target Subtraction Leaf Check
- **Problem Statement & Constraints:** Return true if tree has root-to-leaf path summing to $targetSum$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Path exists $\to$ `true`.
  - *Test Case 2 (Not Leaf):* Sum matches at internal node $\to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** If root null, return false. If leaf (`left == null && right == null`), return `targetSum == root.val`. Return `hasPathSum(root.left, targetSum - root.val) || hasPathSum(root.right, targetSum - root.val)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Path Sum II (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Backtracking Path Collector
- **Problem Statement & Constraints:** Return all root-to-leaf paths where sum equals $targetSum$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Multiple matching paths $\to$ returns list of lists.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Add `root.val` to path. If leaf and `targetSum == root.val`, add copy of path to result. Recurse left and right with remaining sum. Backtrack: remove last element from path.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Path Sum III (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Prefix Sum HashMap on Tree DFS
- **Problem Statement & Constraints:** Number of paths that sum to $targetSum$ (paths do not need to start at root or end at leaf).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8 \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Maintain `Map<Long, Integer> prefixMap` seeded with `{0: 1}`. Running sum `currSum`. Count paths ending at current node: `count += prefixMap.getOrDefault(currSum - target, 0)`. Add `currSum` to map, recurse left and right, and backtrack by decrementing count of `currSum`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Sum Root to Leaf Numbers (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Base-10 Accumulator Pass-Down
- **Problem Statement & Constraints:** Sum all root-to-leaf numbers formed by digit paths.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [1,2,3] \to$ Output: `25` ($12 + 13$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Pass down `currentNum = currentNum * 10 + node.val`. If leaf, return `currentNum`. Else return `dfs(left, currentNum) + dfs(right, currentNum)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Count Good Nodes in Binary Tree (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Max-on-Path Pass-Down
- **Problem Statement & Constraints:** A node is "good" if no node on path from root has a greater value.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [3,1,4,3,null,1,5] \to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Pass down `maxSoFar`. If `node.val >= maxSoFar`, increment good count. Recurse left and right with `newMax = max(maxSoFar, node.val)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 5: Binary Search Tree (BST) Mechanics

---

### Problem #26: Validate Binary Search Tree (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Pass-Down Range Bounds $(min, max)$
- **Problem Statement & Constraints:** Determine if tree is valid BST.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Valid BST $\to$ `true`.
  - *Test Case 2 (Subtree Invariant Violation):* Right child's left child $< root \to$ `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(H)$ Space):** Recurse with `(node, min, max)`. If node is null, return true. If `node.val <= min || node.val >= max`, return false. Recurse left with `(node.left, min, node.val)` and right with `(node.right, node.val, max)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #27: Lowest Common Ancestor of a BST (Difficulty: Medium)
- **Pattern Category:** Pattern 5: BST Split-Point Navigation
- **Problem Statement & Constraints:** Find LCA of $p$ and $q$ in BST.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $p$ in left subtree, $q$ in right subtree $\to$ root is LCA.
  - *Test Case 2 (Ancestor is One of Nodes):* $p$ is root, $q$ is descendant $\to$ $p$ is LCA.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Iterative Approach ($O(H)$ Time, $O(1)$ Space):** While `curr != null`: if both $p, q < curr.val$, `curr = curr.left`; if both $p, q > curr.val$, `curr = curr.right`; else return `curr` (split point).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #28: Kth Smallest Element in a BST (Difficulty: Medium)
- **Pattern Category:** Pattern 5: In-Order Counter Short-Circuit
- **Problem Statement & Constraints:** Find $k$-th smallest element in BST.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $root = [3,1,4,null,2], k = 1 \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(H + K)$ Time, $O(H)$ Space):** Iterative or recursive in-order traversal. Decrement $k$ on visiting root. When $k == 0$, return `node.val`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #29: Delete Node in a BST (Difficulty: Medium)
- **Pattern Category:** Pattern 5: In-Order Successor Replacement
- **Problem Statement & Constraints:** Delete node with key in BST and return root.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (2 Children):* Delete root with 2 subtrees $\to$ replaced by in-order successor.
  - *Test Case 2 (Leaf):* Delete leaf $\to$ replaced by null.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(H)$ Time, $O(H)$ Space):** Search key. When found: if left null, return right; if right null, return left. If 2 children: find successor (min in right subtree), copy value to current node, delete successor from right subtree.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #30: Convert Sorted Array to Binary Search Tree (Difficulty: Easy)
- **Pattern Category:** Pattern 5: Midpoint Divide-and-Conquer
- **Problem Statement & Constraints:** Convert sorted integer array to height-balanced BST.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [-10,-3,0,5,9] \to$ Height-balanced BST.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(\log N)$ Space):** Helper `build(left, right)`. Base case: `left > right` return null. Pick `mid = left + (right - left) / 2` as root. `root.left = build(left, mid - 1); root.right = build(mid + 1, right);`. Return root.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
