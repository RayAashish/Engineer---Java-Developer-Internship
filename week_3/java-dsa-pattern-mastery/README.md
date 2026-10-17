# Java DSA Pattern Mastery
> **Author:** raysi  
> **Target:** SDE 1 / SDE 2 Interviews at Product-Based Companies  
> **Philosophy:** Logic-Building Over Memorization • Brute-to-Optimal Thinking • Pattern Recognition  

---

## Executive Overview
Most developers fail technical interviews not because they don't know syntax or basic data structures, but because they **lack the mental models to transition from Brute Force to Optimal patterns** when facing unseen problems.

This repository is an engineered, pattern-first curriculum designed to develop intuitive algorithmic thinking across 10 structured phases—from basic logic building to dynamic programming.

```
                            THE 10-PHASE MASTERY ROADMAP
 ┌─────────────────────────────────────────────────────────────────────────────┐
 │  Phase 0: Foundations (Math & Bit Manipulation)                             │
 │  Phase 1: Arrays (Two Pointers, Sliding Window, Prefix Sum, Binary Search)   │
 │  Phase 2: 2D Matrix (Grid Coordinates & Directions)                         │
 │  Phase 3: Strings (Hashing, Windows, Anagrams & Palindromes)                │
 │  Phase 4: Linked Lists (Pointer Safety & Fast/Slow Tortoise-Hare)           │
 │  Phase 5: Stacks & Queues (Monotonic Order & State Machines)                │
 │  Phase 6: Recursion & Backtracking (Decision Trees & State Rollback)       │
 │  Phase 7: Binary Trees & BST (Pass-Down vs Return-Up Traversals)            │
 │  Phase 8: Graphs (BFS/DFS, Topological Sort, Shortest Paths & DSU)          │
 │  Phase 9: Dynamic Programming (Recursion -> Memoization -> Tabulation)      │
 └─────────────────────────────────────────────────────────────────────────────┘
```

---

## The "Constraint-to-Algorithm" Cheat Sheet (The Legal Spoiler)
In technical interviews, input constraints at the bottom of the problem are a direct hint toward the expected time and space complexity:

| Input Size ($N$) | Allowed Time Complexity | Core Pattern & Algorithm Signals |
| :--- | :--- | :--- |
| **$N \le 10$ to $20$** | $O(2^N)$ or $O(N!)$ | **Backtracking, Bitmasking, Recursion** (Subsets, Permutations, Traveling Salesman, N-Queens) |
| **$N \le 100$ to $500$** | $O(N^3)$ | **Floyd-Warshall, Matrix Chain Multiplication, 3-Loop Grid DP** |
| **$N \le 10^3$ to $2 \times 10^3$** | $O(N^2)$ | **2D Dynamic Programming, Nested Traversal, Selection/Insertion Sort, Pairwise Comparisons** |
| **$N \le 10^5$ to $2 \times 10^5$** | $O(N \log N)$ or $O(N)$ | **Sorting, Two Pointers, Sliding Window, Prefix Sum + HashMap, Monotonic Stack/Queue, Heap ($K \log N$), 1D DP** |
| **$N \ge 10^6$ to $10^7$** | $O(N)$ | **Single-Pass Array Scan, Dutch National Flag, Kadane's, Bitwise XOR, Hash Array** |
| **$N \ge 10^9$ or Target Space** | $O(\log N)$ or $O(1)$ | **Binary Search on Answer Space, Euclidean GCD, Fast Modular Exponentiation, Math Formulas** |

---

## The 4 Golden Rules to Stop Struggling with Logic

### 1. Read Constraints Before Reading the Story
Before writing a single line of code, inspect the input magnitude. If $N \le 10^5$, an $O(N^2)$ nested loop **will TLE (Time Limit Exceeded)**. You must design an $O(N)$ or $O(N \log N)$ solution.

### 2. Never Jump Straight to Optimal Code
Always conceptualize and trace the $O(N^2)$ Brute Force approach first. Ask the golden question:
> *"What calculation is the inner loop $j$ repeating that I can cache, precompute, or track using pointers or a monotonic invariant?"*
- Re-scanning for target complements $\to$ **HashMap / HashSet ($O(1)$ lookup)**
- Re-calculating contiguous subarray sums $\to$ **Prefix Sum or Sliding Window**
- Re-searching for next greater/smaller element $\to$ **Monotonic Stack**
- Re-evaluating subproblems $\to$ **Dynamic Programming Memoization**

### 3. Solve in "Pattern Batches" of 5
Neural pathways are formed through focused repetition, not random context-switching. When learning a pattern (e.g., Variable Sliding Window), solve 5 variations back-to-back until the template becomes second nature.

### 4. The 25-Minute Rule
- **Minutes 0–10:** Dry-run test cases on paper. Sketch the Brute Force loops.
- **Minutes 10–20:** Identify the redundant work and formulate the Optimal invariant.
- **Minutes 20–25:** Translate logic into clean Java.
- **If stuck after 25 mins:** Read the Optimal Logic Roadmap hint, dry-run the invariant, reset your editor, code from scratch, and leave `[ ] Revised Without Hints` unchecked for spaced repetition.

---

## Repository Directory Blueprint

```
java-dsa-pattern-mastery/
├── README.md
└── src/main/java/com/raysi/dsa/
    ├── foundations/                 # Phase 0: Math & Bit Manipulation
    ├── arrays/                      # Phase 1: Two Pointers, Sliding Window, Prefix Sum, Binary Search
    ├── matrix/                      # Phase 2: 2D Grid Navigation & Matrix In-Place Transforms
    ├── strings/                     # Phase 3: ASCII Hashing, Sliding Windows & Palindromes
    ├── linkedlist/                  # Phase 4: Pointer Rewiring & Fast/Slow Pointers
    ├── stackandqueue/               # Phase 5: Monotonic Order & State Management
    ├── recursionandbacktracking/    # Phase 6: Decision Trees & State Space Search
    ├── trees/                       # Phase 7: Hierarchical Recursion & Tree Views
    ├── graphs/                      # Phase 8: Grid Traversals, Shortest Paths & Disjoint Sets
    └── dp/                          # Phase 9: 1D/2D DP, Subsequences, Knapsack & LIS
```
