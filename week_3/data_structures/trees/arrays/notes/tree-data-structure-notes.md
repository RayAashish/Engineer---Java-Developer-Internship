# Tree Data Structure: Detailed Notes

A **tree** is a hierarchical, **non-linear** data structure made of **nodes** connected by **edges**, where every node (except one special **root**) has exactly **one parent**, and there are **no cycles**. Trees model anything with a natural hierarchy — file systems, org charts, decision processes, parsed expressions — and specialized trees (BSTs, heaps, tries, segment trees) each exploit that hierarchy to make a specific operation fast.

```text
            1              <- root (no parent)
           / \
          2   3            <- 2 and 3 are children of 1, and siblings of each other
         / \   \
        4   5   6          <- leaves (no children): 4, 5, 6
```

---

## Table of Contents

1. [Introduction: What Problem Does a Tree Solve?](#1-introduction-what-problem-does-a-tree-solve)
2. [Core Terminology](#2-core-terminology)
3. [Types of Trees](#3-types-of-trees)
4. [The Binary Tree Node: The "Brain" Behind It](#4-the-binary-tree-node-the-brain-behind-it)
5. [Binary Tree vs Binary Search Tree (BST)](#5-binary-tree-vs-binary-search-tree-bst)
6. [Why BSTs Work: The Ordering Invariant](#6-why-bsts-work-the-ordering-invariant)
7. [Balanced vs Unbalanced Trees](#7-balanced-vs-unbalanced-trees)
8. [Self-Balancing Trees: AVL and Red-Black](#8-self-balancing-trees-avl-and-red-black)
9. [Tree Traversals — The Theory](#9-tree-traversals--the-theory)
10. [DFS Traversals: Preorder, Inorder, Postorder](#10-dfs-traversals-preorder-inorder-postorder)
11. [BFS Traversal: Level Order](#11-bfs-traversal-level-order)
12. [Recursive vs Iterative Traversal](#12-recursive-vs-iterative-traversal)
13. [Core Operations on a BST](#13-core-operations-on-a-bst)
14. [Complexity Summary Table](#14-complexity-summary-table)
15. [Implementing a Binary Tree and BST in Java](#15-implementing-a-binary-tree-and-bst-in-java)
16. [Other Specialized Trees (Brief Tour)](#16-other-specialized-trees-brief-tour)
17. [Types of Problems Trees Solve](#17-types-of-problems-trees-solve)
18. [Recognizing When (and Which) Tree Technique to Use](#18-recognizing-when-and-which-tree-technique-to-use)
19. [Common Tree Patterns](#19-common-tree-patterns)
20. [Top 20 Tree Problems (By Pattern)](#20-top-20-tree-problems-by-pattern)
21. [Sample Solutions (Worked)](#21-sample-solutions-worked)
22. [Common Pitfalls](#22-common-pitfalls)
23. [Quick Cheat Sheet](#23-quick-cheat-sheet)

---

## 1. Introduction: What Problem Does a Tree Solve?

A tree exists to represent **hierarchy** and, in many specialized forms, to make a particular operation efficient by exploiting that hierarchy:

| Need | Linear structure cost | Tree-based solution | Tree cost |
|------|--------------------------|------------------------|------------|
| Search for a value | O(n) in an unsorted array/list | **Binary Search Tree** | O(log n) average |
| Find min/max repeatedly | O(n) scan, or O(n log n) to sort | **Heap** (a specialized tree) | O(1) peek |
| Represent nested/hierarchical data | Awkward — needs manual indices or parent-pointers | **Any tree** | Natural fit |
| Prefix-based string search | O(n·m) comparing every string | **Trie** | O(m), m = word length |
| Range sum/min/max queries with updates | O(n) per query, or O(n) per update | **Segment Tree / Fenwick Tree** | O(log n) per query/update |

A tree is fundamentally a generalization of a **linked list** — instead of each node pointing to exactly one "next" node, each node can point to **multiple** children, creating branching structure instead of a straight line. This single change in connectivity is what unlocks logarithmic-time operations that a linear structure can never achieve.

---

## 2. Core Terminology

```text
Root            The single node with no parent — the top of the tree
Node            Any single element in the tree, holding data + references to children
Edge            The connection between a parent and a child
Parent / Child  A node directly above / below another, connected by one edge
Sibling         Nodes that share the same parent
Leaf            A node with no children
Internal node   Any node that is NOT a leaf (has at least one child)
Subtree         Any node, together with all of its descendants, forms a smaller tree
Height of a node    The number of edges on the LONGEST path from that node down to a leaf
Depth of a node     The number of edges from the ROOT down to that node
Height of the tree   The height of the root (equivalently, depth of the deepest leaf)
Degree of a node     The number of children that node has
```

```text
            1              depth 0, height 2
           / \
          2   3            depth 1
         / \   \
        4   5   6          depth 2, height 0 (all leaves)

Height of the whole tree = 2
Node 2's subtree = {2, 4, 5}
```

---

## 3. Types of Trees

| Type | Key constraint | Typical use |
|------|-------------------|---------------|
| **General tree** | Any node can have any number of children | File systems, org charts, DOM |
| **Binary tree** | Every node has **at most 2** children (left, right) | The foundation for most tree algorithms/interview problems |
| **Binary Search Tree (BST)** | Binary tree + **ordering invariant**: left subtree < node < right subtree | Fast search/insert/delete while keeping data sorted |
| **Balanced BST (AVL, Red-Black)** | BST + a self-enforced height-balance guarantee | Guaranteed O(log n) operations, used inside `TreeMap`/`TreeSet` |
| **Heap** | Complete binary tree + parent-child ordering (not full sort) | Priority queues — covered fully in the heap notes |
| **Trie (prefix tree)** | Each node represents one character; paths from root spell words | Autocomplete, prefix search, dictionary lookups |
| **Segment Tree** | Each node represents a range/interval of an array | Range queries (sum/min/max) with point/range updates |
| **Fenwick Tree (Binary Indexed Tree)** | A compact array-based structure for prefix-sum-style queries | Similar to segment trees, less flexible but less memory/simpler |
| **N-ary tree** | Each node has up to N children, explicitly | Hierarchical category structures, game trees |

> This document focuses mainly on **binary trees and BSTs**, since they're the foundation every other tree builds on. Section 16 gives a brief tour of the specialized ones (heap, trie, segment tree) with pointers for deeper study.

---

## 4. The Binary Tree Node: The "Brain" Behind It

Unlike a heap (which is secretly just a flat array — see the heap notes), a general tree is genuinely a **linked, pointer-based** structure: there's no fixed "complete" shape to exploit with index arithmetic, so each node must explicitly hold references to its children.

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}
```

```text
         TreeNode(1)
         /         \
   TreeNode(2)   TreeNode(3)
   /       \
 null    TreeNode(5)
```

### 4.1 Why pointers, not arrays, for general trees

A heap can use array indexing **only** because it's always a *complete* tree with no gaps. A general binary tree (and especially a BST, which is shaped entirely by *insertion order*, not by any completeness rule) has **no predictable shape** — nodes can be missing anywhere, so there's no reliable index arithmetic that could locate a child. Each node must carry **explicit references**, exactly like a linked list carries a `next` reference, just now with **two** such references (`left` and `right`) instead of one.

### 4.2 This is precisely why a tree is a generalized linked list

```java
class ListNode { int val; ListNode next; }       // ONE link per node — a straight line
class TreeNode { int val; TreeNode left, right; } // TWO links per node — branching structure
```

Every algorithmic pattern you already know for linked lists (traversal, recursion, two-pointer-style techniques) extends naturally to trees — you just now have a **choice** of which child to follow at each step, which is exactly what makes recursion ("solve the left subtree, solve the right subtree, combine") the dominant technique for tree problems.

---

## 5. Binary Tree vs Binary Search Tree (BST)

A **binary tree** only constrains the **shape** — at most 2 children per node — and says nothing about the values stored.

```java
TreeNode root = new TreeNode(5);
root.left = new TreeNode(9);      // perfectly valid binary tree — no ordering required
root.right = new TreeNode(2);
```

A **Binary Search Tree (BST)** adds a crucial **ordering invariant** on top of that shape constraint (section 6), which is what unlocks fast search.

```java
// A valid BST with the same shape:
TreeNode root = new TreeNode(5);
root.left = new TreeNode(2);       // left < root
root.right = new TreeNode(9);      // right > root
```

Every BST is a binary tree, but not every binary tree is a BST — the distinction matters because algorithms that **assume** BST ordering (like simple binary search traversal) will silently give wrong answers on a plain, unordered binary tree.

---

## 6. Why BSTs Work: The Ordering Invariant

A BST enforces, for **every single node** in the tree (not just immediate children):

```text
Every value in the LEFT subtree  <  this node's value
Every value in the RIGHT subtree >  this node's value
```

```text
         8
        / \
       3   10
      / \    \
     1   6    14
        / \   /
       4   7 13

Check node 3: left subtree {1} all < 3    right subtree {6,4,7} all > 3    OK
Check node 8 (root): left subtree {3,1,6,4,7} all < 8   right subtree {10,14,13} all > 8   OK
```

### 6.1 Why this invariant gives O(log n) search

At every node, the ordering invariant lets you **eliminate an entire subtree** with a single comparison — exactly the same logic as binary search on a sorted array, just expressed as a tree walk instead of index halving.

```java
boolean search(TreeNode node, int target) {
    if (node == null) return false;
    if (node.val == target) return true;
    return target < node.val ? search(node.left, target) : search(node.right, target);
}
```

```text
Search for 7 in the tree above:
8 -> 7 < 8, go left
3 -> 7 > 3, go right
6 -> 7 > 6, go right
7 -> found!

4 comparisons, not 11 (the total node count) — each step halves (roughly) the remaining search space.
```

### 6.2 The critical subtlety: "left < node < right" applies to the WHOLE subtree, not just direct children

```text
         5
        / \
       3   8
            \
             4     <- INVALID! 4 is in the right subtree of 5, so it must be > 5, but 4 < 5

(4 being < 8, its direct parent, is not enough — it also violates the invariant relative to the ROOT, 5)
```

This is a classic trap in "validate BST" problems (section 21.1): checking only immediate parent-child relationships is **not sufficient** — the comparison must hold against the entire ancestor chain's bounds.

---

## 7. Balanced vs Unbalanced Trees

The O(log n) promise of a BST **only holds if the tree's height stays close to log n**. If elements are inserted in an already-sorted (or adversarial) order, a plain BST can **degenerate into a straight line** — effectively becoming a linked list, with O(n) operations.

```text
Inserting 1, 2, 3, 4, 5 in order into a plain BST:

1
 \
  2
   \
    3
     \
      4
       \
        5

Height = 4 (= n-1), NOT log(5) ≈ 2.3 — every operation degrades to O(n)!
```

```text
A BALANCED tree holding the same 5 values:

        3
       / \
      2   4
     /     \
    1       5

Height = 2 ≈ log2(5) — operations stay O(log n)
```

| | Balanced tree | Unbalanced (degenerate) tree |
|---|-----------------|---------------------------------|
| Height | O(log n) | Up to O(n) in the worst case |
| Search/insert/delete | O(log n) | O(n) in the worst case |
| Guaranteed by | Self-balancing logic (rotations) | Nothing — a plain BST never rebalances itself |

---

## 8. Self-Balancing Trees: AVL and Red-Black

Both are BSTs that **automatically restructure themselves** (via rotations) after insertions/deletions to keep the height close to `log n`, guaranteeing worst-case O(log n) operations instead of a plain BST's worst-case O(n).

### 8.1 AVL Tree

Enforces a strict **balance factor** at every node: the heights of the left and right subtrees can differ by **at most 1**. If an insertion/deletion violates this, one or more **rotations** (single or double) restore it immediately.

```text
Balance factor = height(left subtree) - height(right subtree)    must be in {-1, 0, 1} at EVERY node
```

### 8.2 Red-Black Tree

Enforces a looser set of coloring rules (each node is "red" or "black", with constraints like "no red node has a red child" and "every root-to-leaf path has the same number of black nodes") that together guarantee the tree's height never exceeds roughly `2 * log(n)`.

| | AVL Tree | Red-Black Tree |
|---|-----------|-------------------|
| Balance strictness | Stricter (height difference ≤ 1) | Looser (height ≤ ~2·log n) |
| Lookups | Slightly faster (more tightly balanced) | Slightly slower |
| Insertions/deletions | More rotations needed, slightly slower | Fewer rotations needed, slightly faster |
| Common real-world use | Database indexing where reads dominate | `java.util.TreeMap`/`TreeSet` internally, Linux kernel schedulers |

> You are very unlikely to need to implement AVL/Red-Black rotation logic by hand in an interview — the practical takeaway is **knowing they exist, why they're needed (section 7's degenerate-tree problem), and that Java's `TreeMap`/`TreeSet` already give you a self-balancing BST for free** (section 16.4).

---

## 9. Tree Traversals — The Theory

**Traversal** means visiting every node in the tree exactly once, in some defined order. Unlike a linear structure (only one sensible order: front-to-back), a tree has **multiple meaningfully different traversal orders**, because at every node you must decide **when** to process that node relative to its children.

```text
Two fundamental traversal families:

DFS (Depth-First)   — go as deep as possible down one branch before backtracking
                       three orderings: preorder, inorder, postorder (differ in WHEN the current node is processed)

BFS (Breadth-First)  — visit all nodes at the current depth before moving to the next depth
                       exactly one ordering: level order
```

---

## 10. DFS Traversals: Preorder, Inorder, Postorder

All three share the exact same recursive shape — the only difference is **where** the line `process(node)` sits relative to the two recursive calls.

```text
            1
           / \
          2   3
         / \
        4   5
```

### 10.1 Preorder: Node → Left → Right

```java
void preorder(TreeNode node) {
    if (node == null) return;
    System.out.print(node.val + " ");   // process FIRST
    preorder(node.left);
    preorder(node.right);
}
```

```text
Output: 1 2 4 5 3
```

**Use case:** copying/serializing a tree (you need the root's value before you can even construct its children), prefix-style expression representations.

### 10.2 Inorder: Left → Node → Right

```java
void inorder(TreeNode node) {
    if (node == null) return;
    inorder(node.left);
    System.out.print(node.val + " ");   // process BETWEEN the two recursive calls
    inorder(node.right);
}
```

```text
Output: 4 2 5 1 3
```

**Use case:** **this is the single most important traversal for BSTs** — inorder traversal of a BST visits nodes in **sorted ascending order**, always. This single fact underlies an enormous number of BST problems (section 19.1).

### 10.3 Postorder: Left → Right → Node

```java
void postorder(TreeNode node) {
    if (node == null) return;
    postorder(node.left);
    postorder(node.right);
    System.out.print(node.val + " ");   // process LAST
}
```

```text
Output: 4 5 2 3 1
```

**Use case:** anything where you need **children fully processed/resolved before the parent** — deleting a tree node-by-node (children must be freed first), computing subtree-dependent values (height, diameter, sum), postfix-style expression evaluation.

### 10.4 Side-by-side memory aid

```text
PRE-order:   process node BEFORE its children   ->  Node, Left, Right
IN-order:    process node IN BETWEEN its children -> Left, Node, Right
POST-order:  process node AFTER its children     ->  Left, Right, Node

The "pre/in/post" refers to WHEN the current node is visited relative to recursing into left/right.
```

---

## 11. BFS Traversal: Level Order

Visits nodes **level by level**, left to right within each level — this requires a **queue**, not recursion, since DFS's natural call-stack recursion has no notion of "finish this whole level before starting the next."

```java
import java.util.LinkedList;
import java.util.Queue;

void levelOrder(TreeNode root) {
    if (root == null) return;
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);

    while (!queue.isEmpty()) {
        TreeNode current = queue.poll();
        System.out.print(current.val + " ");

        if (current.left != null) queue.add(current.left);
        if (current.right != null) queue.add(current.right);
    }
}
```

```text
            1
           / \
          2   3
         / \
        4   5

Output: 1 2 3 4 5   (level 0: 1, level 1: 2,3, level 2: 4,5)
```

### 11.1 Processing level-by-level (not just all nodes in order)

A very common variant tracks each level as its **own group**, by snapshotting the queue's size at the start of each level iteration.

```java
List<List<Integer>> levelOrderGrouped(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;

    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);

    while (!queue.isEmpty()) {
        int levelSize = queue.size();          // snapshot: exactly how many nodes are at THIS level
        List<Integer> currentLevel = new ArrayList<>();

        for (int i = 0; i < levelSize; i++) {    // process exactly that many before moving to the next level
            TreeNode node = queue.poll();
            currentLevel.add(node.val);
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }

        result.add(currentLevel);
    }
    return result;
}
```

This "snapshot the size, then loop exactly that many times" trick is the single most important technique for any level-by-level tree BFS problem (zigzag traversal, right-side view, level averages, and more).

---

## 12. Recursive vs Iterative Traversal

DFS traversals are **most naturally** written recursively (the call stack does the bookkeeping for you), but can also be written iteratively using an **explicit stack** — useful when recursion depth could cause a `StackOverflowError` on a very deep/unbalanced tree, or when a problem specifically asks for an iterative solution.

```java
// Iterative preorder, using an explicit Stack instead of the call stack
void preorderIterative(TreeNode root) {
    if (root == null) return;
    Deque<TreeNode> stack = new ArrayDeque<>();
    stack.push(root);

    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        System.out.print(node.val + " ");

        if (node.right != null) stack.push(node.right);   // push RIGHT first...
        if (node.left != null) stack.push(node.left);      // ...so LEFT is popped and processed first
    }
}
```

| | Recursive | Iterative |
|---|------------|-------------|
| Code simplicity | Usually much simpler, mirrors the traversal definition directly | More verbose, needs an explicit stack/queue |
| Stack overflow risk | Yes, on very deep/unbalanced trees | No — uses heap-allocated structures, not the call stack |
| BFS (level order) | Not natural — needs a queue regardless | Always written iteratively, with a `Queue` |

---

## 13. Core Operations on a BST

### 13.1 Search

```java
TreeNode search(TreeNode node, int target) {
    if (node == null || node.val == target) return node;
    return target < node.val ? search(node.left, target) : search(node.right, target);
}
```

### 13.2 Insert

```java
TreeNode insert(TreeNode node, int val) {
    if (node == null) return new TreeNode(val);     // found the correct empty spot
    if (val < node.val) node.left = insert(node.left, val);
    else if (val > node.val) node.right = insert(node.right, val);
    return node;                                       // duplicates: silently ignored here (val == node.val)
}
```

### 13.3 Delete — the trickiest BST operation

Deletion has **three cases**, depending on how many children the node being deleted has.

```text
Case 1: node has NO children        -> simply remove it (set the parent's link to null)
Case 2: node has ONE child           -> replace the node with its single child
Case 3: node has TWO children        -> replace the node's value with its INORDER SUCCESSOR
                                         (the smallest value in its right subtree), then delete
                                         that successor node from the right subtree instead
```

```java
TreeNode delete(TreeNode node, int val) {
    if (node == null) return null;

    if (val < node.val) {
        node.left = delete(node.left, val);
    } else if (val > node.val) {
        node.right = delete(node.right, val);
    } else {
        // found the node to delete
        if (node.left == null) return node.right;     // Case 1 or Case 2 (missing left)
        if (node.right == null) return node.left;       // Case 2 (missing right)

        // Case 3: two children — find inorder successor (smallest in right subtree)
        TreeNode successor = node.right;
        while (successor.left != null) successor = successor.left;

        node.val = successor.val;                        // copy successor's value up
        node.right = delete(node.right, successor.val);   // remove the successor from its original spot
    }
    return node;
}
```

### 13.4 Why the inorder successor (or predecessor) specifically?

The inorder successor is the **smallest value greater than** the node being deleted. Replacing the deleted node's value with it preserves the BST invariant perfectly: everything in the left subtree is still smaller, and everything remaining in the right subtree is still larger (since the successor, by definition, was the smallest thing there). The **inorder predecessor** (largest value in the left subtree) works equally well as an alternative choice.

---

## 14. Complexity Summary Table

| Operation | Balanced BST / Self-Balancing Tree | Plain BST (worst case, degenerate) |
|-----------|---------------------------------------|----------------------------------------|
| Search | O(log n) | O(n) |
| Insert | O(log n) | O(n) |
| Delete | O(log n) | O(n) |
| Traversal (any order) | O(n) — must visit every node | O(n) |
| Height | O(log n) | O(n) |
| Space | O(n) | O(n) |
| Recursion stack depth (any DFS) | O(log n) | O(n) |

---

## 15. Implementing a Binary Tree and BST in Java

```java
class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class BST {
    TreeNode root;

    void insert(int val) {
        root = insertHelper(root, val);
    }

    private TreeNode insertHelper(TreeNode node, int val) {
        if (node == null) return new TreeNode(val);
        if (val < node.val) node.left = insertHelper(node.left, val);
        else if (val > node.val) node.right = insertHelper(node.right, val);
        return node;
    }

    boolean search(int target) {
        return searchHelper(root, target);
    }

    private boolean searchHelper(TreeNode node, int target) {
        if (node == null) return false;
        if (node.val == target) return true;
        return target < node.val ? searchHelper(node.left, target) : searchHelper(node.right, target);
    }

    void inorder() {
        inorderHelper(root);
        System.out.println();
    }

    private void inorderHelper(TreeNode node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.print(node.val + " ");
        inorderHelper(node.right);
    }

    int height() {
        return heightHelper(root);
    }

    private int heightHelper(TreeNode node) {
        if (node == null) return -1;      // convention: an empty tree has height -1, a single node has height 0
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }
}
```

```java
BST tree = new BST();
int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};
for (int v : values) tree.insert(v);

tree.inorder();                      // 1 3 4 6 7 8 10 13 14  — sorted, as guaranteed by inorder traversal
System.out.println(tree.search(7));  // true
System.out.println(tree.height());    // 3
```

---

## 16. Other Specialized Trees (Brief Tour)

### 16.1 Heap

A complete binary tree with a weaker ordering invariant (parent vs child only, not full BST ordering), stored as a flat array rather than with pointers. Covered in complete depth in the dedicated heap notes — the array-based implementation there is a direct contrast to this document's pointer-based `TreeNode` approach.

### 16.2 Trie (Prefix Tree)

Each node represents a single character; a path from the root spells out a word or prefix. Enables prefix search, autocomplete, and spell-checking in time proportional to the **word's length**, not the number of words stored.

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];   // one slot per lowercase letter
    boolean isEndOfWord;
}
```

```text
Inserting "cat" and "car":
        c
        |
        a
       / \
      t   r
      *   *     (* marks isEndOfWord)
```

### 16.3 Segment Tree

Each node represents an **aggregate** (sum, min, max, etc.) over a **range** of an underlying array. Enables both range queries and point/range updates in O(log n), versus O(n) for a naive re-scan on every query.

```text
Array: [1, 3, 5, 7, 9, 11]

Segment tree (sum example):
                 36
               /    \
             9       27
            / \      /  \
           4   5    16   11
          / \       / \
         1   3     7   9
```

### 16.4 Java's Built-in Self-Balancing Trees: `TreeMap` / `TreeSet`

Java's standard library provides a production-grade, self-balancing (Red-Black tree) BST implementation you rarely need to build by hand.

```java
import java.util.TreeMap;
import java.util.TreeSet;

TreeSet<Integer> set = new TreeSet<>();
set.add(5); set.add(1); set.add(9); set.add(3);

System.out.println(set.first());           // 1   — smallest element, O(log n)
System.out.println(set.last());             // 9   — largest element, O(log n)
System.out.println(set.floor(4));           // 3   — largest element <= 4
System.out.println(set.ceiling(4));         // 5   — smallest element >= 4
System.out.println(set);                    // [1, 3, 5, 9] — always sorted (inorder!) when printed
```

```java
TreeMap<String, Integer> map = new TreeMap<>();
map.put("banana", 2);
map.put("apple", 5);
map.put("cherry", 1);
System.out.println(map);    // {apple=5, banana=2, cherry=1} — keys always in sorted order
```

`TreeMap`/`TreeSet` give you guaranteed O(log n) insert/search/delete **and** always-sorted iteration — whenever a problem needs both of those together, reach for these before hand-rolling a BST.

---

## 17. Types of Problems Trees Solve

```text
- Hierarchical data representation and traversal (file systems, category trees, org charts)
- Fast sorted search/insert/delete (BST, TreeMap/TreeSet)
- Determining structural properties: height, diameter, balance, symmetry
- Path-based problems: root-to-leaf paths, path sums, longest/shortest path between nodes
- Lowest Common Ancestor (LCA) queries
- Tree construction from traversal sequences (rebuild a tree given preorder+inorder, etc.)
- Serialization/deserialization (converting a tree to/from a string or array for storage/transfer)
- Validating structural invariants (is this a valid BST? is it balanced? is it symmetric?)
- Range queries with updates (segment trees, Fenwick trees)
- Prefix-based string problems (tries)
```

---

## 18. Recognizing When (and Which) Tree Technique to Use

```text
Trigger phrases in a problem statement:

"binary tree" (no ordering mentioned)      -> plain DFS/BFS traversal, often recursion-based
"binary search tree" / "BST"                -> exploit the ordering invariant; inorder = sorted!
"level by level" / "level order" / "zigzag" -> BFS with the "snapshot queue size per level" trick
"path sum" / "root to leaf"                  -> DFS, carrying a running sum/path down the recursion
"lowest common ancestor" / "LCA"             -> recursive DFS that returns "found in this subtree?" signals
"diameter" / "height" / "balanced?"          -> postorder DFS (children's results needed before the parent's)
"serialize" / "deserialize" / "rebuild from traversal"  -> preorder (or preorder+inorder) reconstruction
"kth smallest/largest in a BST"              -> inorder traversal (gives sorted order for free)
"validate BST"                               -> DFS carrying a valid (min, max) RANGE down each recursive call
"prefix" / "autocomplete" / "starts with"    -> Trie
"range sum/min/max query" + "updates"        -> Segment Tree or Fenwick Tree
```

---

## 19. Common Tree Patterns

### 19.1 Pattern: Inorder Traversal = Sorted Order (BST only)

The single highest-leverage fact about BSTs: an inorder traversal visits nodes in **strictly ascending sorted order**, with zero extra sorting work. Kth smallest, "closest value," and "convert BST to sorted list" problems almost always reduce to this one fact.

```java
void inorder(TreeNode node, List<Integer> result) {
    if (node == null) return;
    inorder(node.left, result);
    result.add(node.val);
    inorder(node.right, result);
}
// result is now the BST's values in ascending sorted order — always
```

### 19.2 Pattern: Postorder for "Bottom-Up" / Children-Dependent Computations

Whenever a node's answer **depends on its children's answers already being computed** (height, diameter, subtree sum, "is this subtree balanced?"), postorder recursion is the natural fit — each recursive call returns its subtree's answer, which the parent then combines.

```java
int height(TreeNode node) {
    if (node == null) return -1;
    int leftHeight = height(node.left);     // fully resolve LEFT first
    int rightHeight = height(node.right);    // fully resolve RIGHT first
    return 1 + Math.max(leftHeight, rightHeight);   // THEN combine — classic postorder shape
}
```

### 19.3 Pattern: Carrying State Down the Recursion (Top-Down / Preorder-Style)

For problems like "root-to-leaf path sum" or "validate BST with bounds," information flows **downward** — each recursive call receives context from its parent (a running sum, a valid value range) and passes an updated version further down.

```java
boolean hasPathSum(TreeNode node, int remaining) {
    if (node == null) return false;
    if (node.left == null && node.right == null) return remaining == node.val;   // leaf: final check
    remaining -= node.val;
    return hasPathSum(node.left, remaining) || hasPathSum(node.right, remaining);
}
```

### 19.4 Pattern: "Found in Subtree?" Return Signals (LCA-Style)

A recursive function can return **more than just a value** — returning the node itself (or `null`) as a signal of "the target was found somewhere in this subtree" is the backbone of Lowest Common Ancestor and similar "does X exist below here" problems.

```java
TreeNode lowestCommonAncestor(TreeNode node, TreeNode p, TreeNode q) {
    if (node == null || node == p || node == q) return node;

    TreeNode left = lowestCommonAncestor(node.left, p, q);
    TreeNode right = lowestCommonAncestor(node.right, p, q);

    if (left != null && right != null) return node;   // p and q found in DIFFERENT subtrees -> this IS the LCA
    return left != null ? left : right;                  // otherwise, propagate whichever side found something
}
```

### 19.5 Pattern: Level-Order BFS with Size-Snapshotting

Covered fully in section 11.1 — whenever a problem needs level-by-level grouping (zigzag order, right-side view, level averages/max), snapshot `queue.size()` at the start of each outer loop iteration.

### 19.6 Pattern: Reconstructing a Tree from Traversal Sequences

Given two traversal orders (commonly preorder + inorder, or postorder + inorder), a unique binary tree can always be rebuilt. The key insight: **preorder's first element (or postorder's last) is always the root**, and that root's position **within the inorder sequence** splits the remaining elements into the left and right subtrees.

```text
preorder: [3, 9, 20, 15, 7]        inorder: [9, 3, 15, 20, 7]

preorder[0] = 3 is the root.
Find 3 in inorder -> splits inorder into LEFT=[9] and RIGHT=[15,20,7].
Left subtree has 1 node -> next 1 element of preorder ([9]) builds it.
Right subtree has 3 nodes -> next 3 elements of preorder ([20,15,7]) build it, recursively.
```

### 19.7 Pattern: Validate BST with a Running (min, max) Range

As flagged in section 6.2, checking only immediate parent-child relationships is insufficient — the correct approach threads a valid `(min, max)` bound down through the recursion, narrowing it at every step.

```java
boolean isValidBST(TreeNode node, Long min, Long max) {
    if (node == null) return true;
    if ((min != null && node.val <= min) || (max != null && node.val >= max)) return false;
    return isValidBST(node.left, min, (long) node.val) && isValidBST(node.right, (long) node.val, max);
}
```

---

## 20. Top 20 Tree Problems (By Pattern)

★ = worked in section 21; the rest are for you to solve.

### Traversal & Basic Structure family

1. ★ Binary Tree Inorder Traversal (recursive + iterative)
2. Binary Tree Level Order Traversal
3. Binary Tree Zigzag Level Order Traversal
4. Binary Tree Right Side View

### Height / Balance / Diameter family

5. ★ Maximum Depth of Binary Tree
6. Balanced Binary Tree (check height-balance)
7. Diameter of Binary Tree
8. Symmetric Tree

### Path-Sum family

9. Path Sum (root-to-leaf equals target)
10. Path Sum II (return all such paths)
11. Binary Tree Maximum Path Sum (path need not pass through root)

### BST-Specific family

12. ★ Validate Binary Search Tree
13. Kth Smallest Element in a BST
14. Convert Sorted Array to Balanced BST
15. Lowest Common Ancestor of a BST

### LCA / Ancestor family (general binary tree)

16. Lowest Common Ancestor of a Binary Tree (not assuming BST order)

### Construction / Serialization family

17. Construct Binary Tree from Preorder and Inorder Traversal
18. Serialize and Deserialize Binary Tree

### Advanced / Mixed family

19. Invert/Mirror a Binary Tree
20. Flatten Binary Tree to Linked List

---

## 21. Sample Solutions (Worked)

### 21.1 Binary Tree Inorder Traversal (Recursive and Iterative)

**Problem:** Given the root of a binary tree, return the inorder traversal of its nodes' values.

```java
import java.util.*;

class Solution {
    // Recursive version
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.val);
        inorderHelper(node.right, result);
    }

    // Iterative version, using an explicit stack
    public List<Integer> inorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {           // go as far LEFT as possible, remembering the path
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();                // backtrack to the most recent unvisited node
            result.add(current.val);               // process it (the "in-between" step)
            current = current.right;                // then move to its right subtree
        }
        return result;
    }
}
```

```text
Tree:        1
               \
                2
               /
              3

Recursive: inorder(1) -> inorder(null,left) -> [] -> add 1 -> inorder(2)
                       -> inorder(3,left) -> add 3 -> inorder(null) -> add 2 -> inorder(null)
Result: [1, 3, 2]
```

**Complexity:** O(n) time (every node visited once), O(h) space for the recursion/explicit stack, where h is the tree's height (O(log n) balanced, O(n) worst case).

---

### 21.2 Maximum Depth of Binary Tree

**Problem:** Given the root of a binary tree, return its maximum depth (the number of nodes along the longest root-to-leaf path).

```java
class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) return 0;                  // base case: empty tree has depth 0
        int leftDepth = maxDepth(root.left);           // postorder shape: resolve children first
        int rightDepth = maxDepth(root.right);
        return 1 + Math.max(leftDepth, rightDepth);    // combine: this node + the deeper of the two subtrees
    }
}
```

```text
            3
           / \
          9   20
             /  \
            15    7

maxDepth(3) = 1 + max(maxDepth(9), maxDepth(20))
maxDepth(9) = 1 + max(maxDepth(null), maxDepth(null)) = 1 + max(0,0) = 1
maxDepth(20) = 1 + max(maxDepth(15), maxDepth(7)) = 1 + max(1,1) = 2
maxDepth(3) = 1 + max(1, 2) = 3
```

**Complexity:** O(n) time, O(h) space for the recursion stack.

---

### 21.3 Validate Binary Search Tree

**Problem:** Given the root of a binary tree, determine if it is a valid BST.

```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        return validate(root, null, null);
    }

    private boolean validate(TreeNode node, Long min, Long max) {
        if (node == null) return true;     // an empty subtree is trivially valid

        // check against the ACCUMULATED bounds from every ancestor, not just the immediate parent
        if (min != null && node.val <= min) return false;
        if (max != null && node.val >= max) return false;

        // left subtree: upper bound narrows to this node's value
        // right subtree: lower bound narrows to this node's value
        return validate(node.left, min, (long) node.val) &&
               validate(node.right, (long) node.val, max);
    }
}
```

```text
         5
        / \
       3   8
            \
             4      <- invalid: 4 must be > 5 (it's in the right subtree of the ROOT, 5), but 4 < 5

validate(5, null, null)       -> ok, 5 within (null, null)
  validate(3, null, 5)         -> ok, 3 < 5
  validate(8, 5, null)          -> ok, 8 > 5
    validate(4, 5, null)         -> FAILS: 4 <= min(5) -> return false

Overall result: false
```

**Why `Long` instead of `int` for the bounds:** using `int` for `min`/`max` and comparing against `Integer.MIN_VALUE`/`MAX_VALUE` as sentinels would break on a tree that legitimately contains those extreme values. `Long` bounds (with `null` meaning "no bound yet") avoid this edge case entirely.

**Complexity:** O(n) time (every node visited once), O(h) space for the recursion stack.

---

## 22. Common Pitfalls

| # | Pitfall | Fix |
|---|---------|-----|
| 1 | Using array-index math (`2i+1`, etc.) on a general tree | That trick only works for a *complete* tree (like a heap) — general trees/BSTs need explicit `left`/`right` pointers |
| 2 | Checking only immediate parent-child order when validating a BST | The invariant applies against the *entire* ancestor chain, not just the direct parent — use a running (min, max) range |
| 3 | Assuming any binary tree is automatically a BST | A binary tree only constrains shape (≤2 children); ordering is a separate, additional invariant specific to BSTs |
| 4 | Forgetting a plain BST can degenerate into a straight line (O(n) operations) | Sorted/adversarial insertion order causes this; use a self-balancing tree (or `TreeMap`/`TreeSet`) when worst-case guarantees matter |
| 5 | Using recursion on a very deep, unbalanced tree without considering stack depth | Risk of `StackOverflowError`; consider an iterative, explicit-stack traversal for such cases |
| 6 | Mixing up which traversal to use for which problem | Inorder for BST sorted order; postorder for "children's answer needed before parent's"; preorder for reconstruction/serialization |
| 7 | Forgetting the "snapshot queue size per level" trick in BFS | Without it, you get a single flat traversal, not distinct per-level groupings |
| 8 | Forgetting all three deletion cases in BST delete (0, 1, or 2 children) | The two-children case specifically requires finding and promoting the inorder successor (or predecessor) |
| 9 | Assuming `TreeMap`/`TreeSet` iteration order matches insertion order | It's always sorted (equivalent to inorder traversal of the underlying Red-Black tree), never insertion order |
| 10 | Forgetting that a node's height convention can differ (empty tree = -1 vs 0) | Be explicit and consistent about the base case you choose — it changes every downstream calculation by a constant offset |

---

## 23. Quick Cheat Sheet

```text
TERMINOLOGY            root (no parent)   leaf (no children)   height (longest path DOWN to a leaf)
                       depth (path length from ROOT down)      subtree (a node + all its descendants)

NODE STRUCTURE          class TreeNode { int val; TreeNode left, right; }
                       Pointer-based (unlike a heap's flat array) — no shape guarantee to exploit

BINARY TREE vs BST      Binary tree: shape only, <=2 children per node, no value ordering required
                       BST: binary tree + "left subtree < node < right subtree" for EVERY node, not just
                            immediate children

WHY BST IS FAST          Ordering invariant eliminates HALF the remaining tree per comparison -> O(log n)
                       ...but ONLY if the tree stays balanced; sorted-order insertion can degenerate to O(n)

SELF-BALANCING           AVL: strict balance factor (-1,0,1) at every node — tighter, faster lookups
                       Red-Black: looser color-based rules — fewer rotations, used by TreeMap/TreeSet

DFS TRAVERSALS           Preorder:  Node, Left, Right    -> serialization/reconstruction
                       Inorder:   Left, Node, Right     -> SORTED order for a BST (the #1 fact to remember)
                       Postorder: Left, Right, Node      -> "children's answer needed before parent's"

BFS TRAVERSAL             Level order — needs an explicit Queue, not recursion
                       "Snapshot queue.size() per level" trick -> group nodes level by level

BST OPERATIONS            Search/Insert: O(log n) balanced, O(n) worst case — simple recursive descent
                       Delete: 3 cases — 0 children (remove), 1 child (promote it), 2 children
                               (replace value with inorder successor, delete successor from right subtree)

JAVA BUILT-INS            TreeMap<K,V> / TreeSet<E>  — ready-made, self-balancing (Red-Black) BST
                       .first()/.last()/.floor()/.ceiling() all O(log n); iteration is always sorted order

OTHER TREE TYPES          Heap          -> flat array, parent/child order only (see the heap notes)
                       Trie           -> one node per character, O(word length) prefix search
                       Segment Tree    -> range aggregate queries + updates, O(log n) each

KEY PATTERNS              Inorder = sorted (BST)             -> kth smallest, closest value, BST-to-list
                       Postorder = bottom-up                -> height, diameter, balance check
                       Top-down state (preorder-style)       -> path sum, validate BST with bounds
                       "Found in subtree?" return signals     -> Lowest Common Ancestor
                       BFS + level-size snapshot               -> level order, zigzag, right-side view
                       Two traversals -> rebuild tree          -> construct from preorder+inorder
```

**Remember:**

1. Trees are a generalization of linked lists — one node, multiple (not just one) outgoing links — which is exactly why recursion ("solve left, solve right, combine") dominates tree algorithms.
2. A BST's power comes entirely from its ordering invariant holding against the *whole* ancestor chain, not just immediate parents — and that power evaporates if the tree isn't kept balanced.
3. Inorder traversal of a BST is sorted order, full stop — internalizing this one fact unlocks a huge fraction of BST problems instantly.
4. Match the traversal to the problem's data-flow direction: postorder when children's results are needed first, preorder/top-down when context flows from parent to child.
5. Prefer `TreeMap`/`TreeSet` over hand-rolling a balanced BST in real problem-solving — reserve manual AVL/Red-Black implementation for understanding the mechanics, not for production use.
