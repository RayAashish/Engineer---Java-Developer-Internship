# Phase 5: Stack — Self-Guided Problem Workbook (25 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.stackandqueue.stack`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: State Tracking & Nested Syntax Parsers

---

### Problem #1: Valid Parentheses (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Bracket Matching Stack
- **Problem Statement & Constraints:** Given string $s$ containing `'()[]{}'`, determine if input string is valid.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $s = "()[]{}" \to$ Output: `true`.
  - *Test Case 2 (Interleaved Mismatch):* $s = "([)]" \to$ Output: `false`.
  - *Test Case 3 (Premature Empty):* $s = "]" \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Use `ArrayDeque<Character>`. When seeing open bracket, push matching close bracket. When seeing closing bracket, pop and ensure it matches current character. Return `stack.isEmpty()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Minimum Add to Make Parentheses Valid (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Open/Close Mismatch Counter
- **Problem Statement & Constraints:** Return the minimum number of parentheses you must add to make $s$ valid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "())" \to$ Output: `1`.
  - *Test Case 2 (Standard):* $s = "(((" \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Maintain `openNeeded` and `closeNeeded`. On `'('`, increment `closeNeeded`. On `')'`, if `closeNeeded > 0`, decrement it; else increment `openNeeded`. Return `openNeeded + closeNeeded`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Minimum Remove to Make Valid Parentheses (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Invalid Index Masking
- **Problem Statement & Constraints:** Remove minimum number of parentheses so that the resulting string is valid.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "lee(t(c)o)de)" \to$ Output: `"lee(t(c)o)de"`.
  - *Test Case 2 (Consecutive Invalid):* $s = "))((" \to$ Output: `""`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Use a stack of integer indices. Push indices of `'('`. On `')'`, if stack non-empty, pop matching `'('` index; else record current `')'` index in a set of invalid indices. Any indices remaining in stack are unmatched `'('` and added to set. Reconstruct string skipping indices in set.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Longest Valid Parentheses (Difficulty: Hard)
- **Pattern Category:** Pattern 1: Base Index Seeding Stack
- **Problem Statement & Constraints:** Given string of `'('` and `')'`, find length of longest valid parentheses substring.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = ")()())" \to$ Output: `4` ("()()").
  - *Test Case 2 (Nested):* $s = "(()" \to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Push seed base index `-1` onto stack. On `'('`, push index $i$. On `')'`, pop stack: if empty, push current index $i$ as new base; else maximize `i - stack.peek()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Simplify Path (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Directory Token Stack
- **Problem Statement & Constraints:** Simplify an absolute Unix-style file path into canonical path.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $path = "/home//foo/" \to$ Output: `"/home/foo"`.
  - *Test Case 2 (Parent Directory):* $path = "/a/./b/../../c/" \to$ Output: `"/c"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Split by `'/'`. Iterate tokens: ignore `""` and `"."`. On `".."`: pop stack if non-empty. For other names: push token. Join stack tokens with `'/'` from bottom to top.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Evaluate Reverse Polish Notation (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Postfix Arithmetic Evaluation
- **Problem Statement & Constraints:** Evaluate arithmetic expression in Reverse Polish Notation (`+`, `-`, `*`, `/`).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `tokens = ["2","1","+","3","*"]` $\to$ Output: `9` ($(2+1) \times 3$).
  - *Test Case 2 (Division):* `tokens = ["4","13","5","/","+"]` $\to$ Output: `6` ($4 + (13 / 5)$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Integer stack. Iterate tokens: if operand, parse `Integer.parseInt(token)` and push. If operator: pop $b$, pop $a$, perform operation $(a \text{ op } b)$ (order matters for `-` and `/`), push result back onto stack. Final answer is `stack.pop()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Basic Calculator II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Operator Precedence Sign Stacking
- **Problem Statement & Constraints:** Given string $s$ containing non-negative integers, `+`, `-`, `*`, `/`, evaluate without `eval()`.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "3+2*2" \to$ Output: `7`.
  - *Test Case 2 (Division Precedence):* $s = " 3/2 " \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Maintain `currNum` and `lastOp = '+'`. When hitting operator or string end: if `lastOp == '+'`, push `currNum`; if `'-'`, push `-currNum`; if `'*'`, push `stack.pop() * currNum`; if `'/'`, push `stack.pop() / currNum`. Sum all stack elements at end.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Monotonic Decreasing Stack (Next Greater Element)

---

### Problem #8: Next Greater Element I (Difficulty: Easy)
- **Pattern Category:** Pattern 2: Monotonic Decreasing Stack + HashMap
- **Problem Statement & Constraints:** Find next greater element for each element in $nums1$ from $nums2$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums1 = [4,1,2], nums2 = [1,3,4,2] \to$ Output: `[-1,3,-1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N + M)$ Time, $O(M)$ Space):** Monotonic decreasing stack over $nums2$. While incoming $> stack.peek()$, map `map.put(stack.pop(), incoming)`. Query $nums1$ against map.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Next Greater Element II (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Circular Index 2N Scan
- **Problem Statement & Constraints:** Circular array: return next greater number for every element.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1,2,1] \to$ Output: `[2,-1,2]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Loop index $i$ from $0$ to $2N - 1$. Maintain monotonic decreasing stack of indices. When `nums[i % n] > nums[stack.peek()]`, pop and write result for `stack.pop()`. Only push $i$ when $i < n$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Daily Temperatures (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Day Index Waiting Span
- **Problem Statement & Constraints:** Days until warmer temperature.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $temps = [73,74,75,71,69,72,76,73] \to$ Output: `[1,1,4,2,1,1,0,0]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Stack of indices. While `temps[curr] > temps[stack.peek()]`, pop `prevDay` and record `res[prevDay] = curr - prevDay`. Push `curr`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Online Stock Span (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Consecutive Less-or-Equal Span Accumulation
- **Problem Statement & Constraints:** Find span of stock's price today (consecutive days prior price was $\le$ today).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Prices: [100, 80, 60, 70, 60, 75, 85] $\to$ Spans: [1, 1, 1, 2, 1, 4, 6].
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach (Amortized $O(1)$ Time per Call):** Monotonic decreasing stack storing pairs `[price, span]`. When incoming `price` arrives, `span = 1`. While `stack.peek()[0] <= price`, add `stack.pop()[1]` to `span`. Push `[price, span]`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: 132 Pattern (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Reverse Scan with Candidate "2" Tracker
- **Problem Statement & Constraints:** Find if there exists indices $i < j < k$ such that $nums[i] < nums[k] < nums[j]$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [3, 1, 4, 2] \to$ Output: `true` (1, 4, 2).
  - *Test Case 2 (False Trap):* $nums = [1, 2, 3, 4] \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Scan backwards from right to left. Maintain candidate for "2" (`third = -INF`). Maintain monotonic decreasing stack representing candidate "3"s. When `nums[i] > stack.peek()`, update `third = stack.pop()`. If `nums[i] < third`, 132 pattern found!
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #13: Sum of Subarray Minimums (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Contribution Technique via Monotonic Stack
- **Problem Statement & Constraints:** Return sum of $\min(b)$ over all contiguous subarrays $b$ modulo $10^9 + 7$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $arr = [3, 1, 2, 4] \to$ Output: `17`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** For each element $arr[i]$, count subarrays where it is the minimum: $(i - PLE) \times (NLE - i) \times arr[i]$. Use monotonic stack to find Previous Less Element (strict) and Next Less Element (non-strict, to prevent double counting).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Monotonic Increasing Stack (Histogram Geometry & Water)

---

### Problem #14: Largest Rectangle in Histogram (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Bounded Width Monotonic Increasing Stack
- **Problem Statement & Constraints:** Return area of largest rectangle in histogram. $1 \le heights.length \le 10^5$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $heights = [2,1,5,6,2,3] \to$ Output: `10` ($5 \times 2$).
  - *Test Case 2 (Monotonic Heights):* $heights = [2,4] \to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Monotonic increasing index stack. Append virtual 0-height bar at $i = n$. When `currHeight < heights[stack.peek()]`, pop bar $H$, right boundary is $i$, left boundary is `stack.peek()`. Width is `stack.isEmpty() ? i : (i - stack.peek() - 1)`. Maximize $H \times width$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Maximal Rectangle (Difficulty: Hard)
- **Pattern Category:** Pattern 3: 2D Grid to Cumulative 1D Histograms
- **Problem Statement & Constraints:** Find largest rectangle containing only 1s in 2D binary matrix.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* 2D binary matrix $\to$ returns area of largest subrectangle.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \cdot C)$ Time, $O(C)$ Space):** Maintain 1D `heights[C]`. For each row: if cell is '1', `heights[c]++`; else reset `heights[c] = 0`. Run `largestRectangleArea(heights)` on each row.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Trapping Rain Water (Difficulty: Hard)
- **Pattern Category:** Pattern 3: Monotonic Decreasing Stack Horizontal Layering
- **Problem Statement & Constraints:** Compute trapped rain water volume.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $height = [0,1,0,2,1,0,1,3,2,1,2,1] \to$ Output: `6`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Monotonic decreasing stack of indices. When `height[curr] > height[stack.peek()]`: pop `bottom`. If stack empty, break. Water volume: $(\min(height[stack.peek()], height[curr]) - height[bottom]) \times (curr - stack.peek() - 1)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Remove K Digits (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Monotonic Increasing Greedy Digit Pruning
- **Problem Statement & Constraints:** Given non-negative integer string $num$ and integer $k$, remove $k$ digits so that the remaining number is the smallest possible.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $num = "1432219", k = 3 \to$ Output: `"1219"`.
  - *Test Case 2 (Leading Zeros):* $num = "10200", k = 1 \to$ Output: `"200"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** To minimize magnitude, earlier digits must be smaller. While $k > 0$ and `stack.peek() > digit`, pop stack and decrement $k$. Push digit. If $k > 0$ after loop, truncate from tail. Strip leading zeros.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Remove Duplicate Letters (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Lexicographical Monotonic Stack with Last Occurrence
- **Problem Statement & Constraints:** Remove duplicate letters so that every letter appears once and the result is the smallest in lexicographical order.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "bcabc" \to$ Output: `"abc"`.
  - *Test Case 2 (Standard):* $s = "cbacdcbc" \to$ Output: `"acdb"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Record last index of each character. Track `boolean[] inStack`. For char $c$: if already in stack, skip. While `c < stack.peek()` and `lastIndex[stack.peek()] > currIndex`, pop stack and mark `inStack = false`. Push $c$, mark `inStack = true`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Steps to Make Array Non-decreasing (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Monotonic Stack with Step Counter DP
- **Problem Statement & Constraints:** In one step, remove all $nums[i]$ where $nums[i - 1] > nums[i]$. Return total steps to stop removals.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [5,3,4,4,7,3,6,11,8,5,11] \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Scan from right to left. Maintain stack of pairs `[value, stepsToEat]`. When seeing larger element on left, it eats smaller elements to its right; its survival steps $= \max(currentSteps + 1, peekSteps)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: MinStack, Expression Simulation & State Reversal

---

### Problem #20: Min Stack (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Delta Encoding / Dual Stack
- **Problem Statement & Constraints:** Design a stack supporting `push`, `pop`, `top`, and `getMin` in $O(1)$ time.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* Push [-2, 0, -3], getMin() -> -3, pop(), top() -> 0, getMin() -> -2.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time, $O(1)$ Extra Space):** Store difference `(val - min)`. If $val < min$, difference is negative $\implies$ establish new minimum. On pop, if top $< 0$, restore old minimum: $min = min - top$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Decode String (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Nested String Multiplication Stack
- **Problem Statement & Constraints:** Given encoded string like `"3[a2[c]]"`, return decoded string (`"accaccacc"`).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "3[a]2[bc]" \to$ Output: `"aaabcbc"`.
  - *Test Case 2 (Nested):* $s = "3[a2[c]]" \to$ Output: `"accaccacc"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Two stacks: `countStack` and `stringStack`. On `'['`, push current count and current `StringBuilder`, reset count and builder. On `']'`, pop count $k$, repeat current builder $k$ times, and append to popped builder.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Asteroid Collision (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Opposing Vector Collision Simulation
- **Problem Statement & Constraints:** Asteroids moving right ($> 0$) and left ($< 0$) with given size. Smaller explodes; equal both explode. Return remaining.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $asteroids = [5, 10, -5] \to$ Output: `[5, 10]`.
  - *Test Case 2 (Both Explode):* $asteroids = [8, -8] \to$ Output: `[]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Stack. Asteroid moves right: push. Asteroid moves left: while stack top is positive and smaller, pop top. If top equals current magnitude, pop top and destroy both. If top is larger, destroy incoming. Else push left-moving asteroid.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Validate Stack Sequences (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Greedy Push/Pop Simulation
- **Problem Statement & Constraints:** Given `pushed` and `popped` sequences with distinct values, return `true` if this could have been the result of a sequence of push and pop operations.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Valid):* $pushed = [1,2,3,4,5], popped = [4,5,3,2,1] \to$ Output: `true`.
  - *Test Case 2 (Invalid):* $pushed = [1,2,3,4,5], popped = [4,3,5,1,2] \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** For each value in `pushed`, push onto stack. While stack is non-empty and `stack.peek() == popped[popIdx]`, pop stack and increment `popIdx`. Return `popIdx == popped.length`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Score of Parentheses (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Depth Contribution Counting
- **Problem Statement & Constraints:** Compute score of balanced parentheses: `"()"` has score 1, $AB$ has score $A + B$, $(A)$ has score $2 \times A$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "(()(()))" \to$ Output: `6`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Every `"()"` at nesting depth $d$ contributes exactly $2^d$ to the total score. Maintain `depth = 0`. On `'('`, `depth++`. On seeing `")"` immediately following `"("`, add `1 << (depth - 1)`. On `')'`, `depth--`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Car Fleet (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Collision Time Monotonic Stack
- **Problem Statement & Constraints:** $n$ cars traveling to target mile. Given `position` and `speed`. Cars cannot pass each other; faster catches slower and forms a fleet. Return number of fleets.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $target = 12, pos = [10,8,0,5,3], speed = [2,4,1,1,3] \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(N)$ Space):**
    - Sort cars descending by starting position (closest to target first).
    - For each car, compute arrival time to target: $time = (target - pos) / speed$.
    - Iterate times: if a car behind arrives earlier or at same time as the car in front, it merges into the front car's fleet. Only start a new fleet when a car takes strictly longer than previous fleet leader.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
