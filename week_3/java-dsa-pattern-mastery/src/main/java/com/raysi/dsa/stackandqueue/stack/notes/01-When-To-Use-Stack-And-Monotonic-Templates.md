# Phase 5: Stack — Monotonic Order, LIFO State & Memory Mechanics
> **Author:** raysi  
> **Package:** `com.raysi.dsa.stackandqueue.stack`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Master LIFO state parsing, monotonic stack invariants, histogram geometric width calculation, and trap water layering.

---

## 1. Stack Mental Model & JVM Memory Mechanics

### 1.1 The JVM Execution Stack vs Heap
The stack data structure directly models how modern CPUs and the Java Virtual Machine execute code:
- Every thread in the JVM is allocated a private **Call Stack** (typically 1 MB default size `-Xss1m`).
- Each method call pushes a **Stack Frame** containing: Local Variable Table, Operand Stack, and Frame Data (return address).
- Deep recursion without tail-call optimization exhausts thread stack memory, throwing `java.lang.StackOverflowError`.
- Converting recursive algorithms to iterative stack simulations moves state to the JVM Heap (`-Xmx`), where hundreds of megabytes can be utilized safely.

---

### 1.2 Java Collection Selection: `ArrayDeque` vs Legacy `Stack`
One of the most common interview penalties is declaring `Stack<Integer> stack = new Stack<>()`.

| Data Structure | Implementation | Thread Safety | Performance |
| :--- | :--- | :--- | :--- |
| `java.util.Stack` | Extends `Vector` (1995 legacy) | Synchronized (Acquires monitor lock on every `push`/`pop`) | **Slow** (severe lock contention and synchronization overhead) |
| `java.util.LinkedList` | Doubly linked list nodes | Unsynchronized | **Suboptimal** (node object allocations, pointer dereferencing) |
| `java.util.ArrayDeque` | Circular resizable array | Unsynchronized | **Fastest** (amortized $O(1)$, cache friendly, zero lock overhead) |

**Golden Rule:** Always declare:
```java
Deque<Integer> stack = new ArrayDeque<>();
```

---

### 1.3 The Monotonic Stack Invariant & The Amortized $O(N)$ Proof
A **Monotonic Stack** enforces that elements are stored in strictly increasing or strictly decreasing order.

```
Incoming element: X
While stack is not empty and stack.peek() violates invariant:
    stack.pop();
stack.push(X);
```

#### Why is the nested loop strictly $O(N)$ and NOT $O(N^2)$?
Even though there is a `while` loop nested inside a `for` loop over $N$ items:
1. Every element $i \in [0, N - 1]$ is pushed onto the stack **at most once**.
2. Once an element is popped from the stack, it is **never pushed again**.
3. Across the entire execution of the outer loop, the total number of `pop()` operations cannot exceed the total number of `push()` operations ($N$).
4. Total operations $= N \text{ (pushes)} + \le N \text{ (pops)} \le 2N = O(N)$.

---

### 1.4 Constraints to Big-O Table for Stacks

| Constraints ($N$) | Permissible Big-O | Target Stack Pattern |
| :--- | :--- | :--- |
| $N \le 10^3$ | $O(N^2)$ | Brute Force nested scans |
| $N \le 10^5$ | $O(N)$ | Monotonic Stack (Next Greater/Smaller, Histogram) |
| $N \le 10^5$ | $O(N)$ | State matching (Parentheses, Simplify Path, Polish Notation) |

---

## 2. The 4 Core Stack Patterns

```
                                  +---------------------------+
                                  |      Stack Patterns       |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: State  | | Pattern 2: Mono   | | Pattern 3: Mono   | | Pattern 4: Value  |
| Tracking / Syntax | | Decreasing (NGE)  | | Increasing (Area) | | Delta MinStack    |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  Bracket Matching      Next Greater Elem     Next Smaller Elem     O(1) getMin()       
  Path parsing          Daily Temperatures    Histogram Width       Zero extra space    
  LIFO Evaluation       Circular Wrap 2N      Water Trapping        diff = val - min    
```

---

### Pattern 1: State Tracking & Nested Syntax
- **Trigger Keywords:** `"valid parentheses"`, `"simplify path"`, `"evaluate reverse polish notation"`.
- **Mental Model:** A newly opened nested scope must be resolved before enclosing scopes can finish. Push open scopes; pop and validate upon closure.

---

### Pattern 2: Monotonic Decreasing Stack (Next Greater Element)
- **Trigger Keywords:** `"next greater element"`, `"warmer temperatures"`, `"stock span"`.
- **Mental Model:** Stack preserves elements in decreasing order: $[8, 6, 4, 2]$.
  - When incoming element is $7$: elements $2, 4, 6$ have found their next greater element ($7$)!
  - Pop $2, 4, 6$, record $7$ as their answer, then push $7$. Stack becomes $[8, 7]$.

---

### Pattern 3: Monotonic Increasing Stack (Next Smaller Element & Histogram Geometry)
- **Trigger Keywords:** `"largest rectangle in histogram"`, `"maximal rectangle"`, `"trapping rain water"`.
- **Mental Model:** Stack preserves elements in increasing order: $[2, 4, 5, 8]$.
  - When incoming element is shorter ($3$), the tall bars ($8, 5, 4$) can extend no further right.
  - The incoming bar is their **Right Boundary** (Next Less Element).
  - The bar below them on the stack is their **Left Boundary** (Previous Less Element).
  - Width $= right - left - 1$.

---

### Pattern 4: MinStack with Value-Delta Compression
- **Trigger Keywords:** `"min stack"`, `"max stack"`, `"retrieve minimum in O(1)"`.
- **Mental Model:** Push the delta `val - min`. If `val < min`, delta is negative, which signals that a new minimum was established. During `pop()`, if delta is negative, restore the old minimum via `min = min - delta`.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Valid Parentheses (LeetCode 20)
> **Problem:** Given a string $s$ containing `'('`, `')'`, `'{'`, `'}'`, `'['`, `']'`, determine if the input string is valid.

#### 1. Brute Force Instinct
Repeatedly replace `"()"`, `"{}"`, `"[]"` with empty string `""` until no pairs remain.
*Cost:* $O(N^2)$ time with heavy string allocations.

#### 2. "Aha!" Observation
When an open bracket is seen, push its matching closing bracket onto `ArrayDeque`. When a closing bracket is seen, pop from stack: it must match the current character!

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class ValidParenthesesAnchor {
    public boolean isValid(String s) {
        if (s == null || s.length() % 2 != 0) return false;

        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else {
                if (stack.isEmpty() || stack.pop() != c) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(N)$

---

### Anchor Problem #2: Next Greater Element II (LeetCode 503)
> **Problem:** Given a circular integer array $nums$, return the next greater number for every element. If none, return -1.

#### 1. Brute Force Instinct
For each element $i$, loop circularly up to $N - 1$ steps forward $\implies O(N^2)$.

#### 2. "Aha!" Observation
Double the array virtually by iterating index $i$ from $0$ to $2N - 1$, accessing elements via `nums[i % n]`. Maintain a monotonic decreasing stack of indices. Push indices only during the first pass ($i < n$).

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.stack;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class NextGreaterElementIIAnchor {
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, -1);

        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < 2 * n; i++) {
            int num = nums[i % n];
            while (!stack.isEmpty() && nums[stack.peek()] < num) {
                res[stack.pop()] = num;
            }
            if (i < n) {
                stack.push(i);
            }
        }
        return res;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — $2N$ iterations, each index pushed once, popped once.
- **Space Complexity:** $O(N)$ for stack.

---

### Anchor Problem #3: Daily Temperatures (LeetCode 739)
> **Problem:** Given an array of integers $temperatures$, return an array $answer$ such that $answer[i]$ is the number of days you have to wait after the $i$-th day to get a warmer temperature.

#### 1. Brute Force Instinct
For every day, scan forward until encountering a higher temperature $\implies O(N^2)$.

#### 2. "Aha!" Observation
Monotonic decreasing stack of day indices. When a warmer day $currDay$ arrives, it resolves the waiting period for all days on the stack with cooler temperatures: `answer[prevDay] = currDay - prevDay`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class DailyTemperaturesAnchor {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int currDay = 0; currDay < n; currDay++) {
            int currTemp = temperatures[currDay];
            while (!stack.isEmpty() && temperatures[stack.peek()] < currTemp) {
                int prevDay = stack.pop();
                answer[prevDay] = currDay - prevDay;
            }
            stack.push(currDay);
        }
        return answer;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(N)$

---

### Anchor Problem #4: Largest Rectangle in Histogram (LeetCode 84)
> **Problem:** Given an array of integers $heights$ representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

#### 1. Brute Force Instinct
For each pair of bars $(i, j)$, find minimum height in between and compute area $\implies O(N^2)$.

#### 2. "Aha!" Observation
For each bar $H$, find its maximal left and right boundaries where heights are $\ge H$.
- Maintain a monotonic increasing stack of bar indices.
- When an incoming bar is shorter, it serves as the **Right Boundary** for the popped bar.
- The new stack top after popping is the **Left Boundary**.
- $\text{Width} = i - \text{stack.peek()} - 1$.
- Add a dummy bar of height 0 at index $N$ to flush all remaining bars from the stack.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class LargestRectangleInHistogramAnchor {
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) return 0;

        int n = heights.length;
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;

        for (int i = 0; i <= n; i++) {
            int currHeight = (i == n) ? 0 : heights[i];

            while (!stack.isEmpty() && currHeight < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : (i - stack.peek() - 1);
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — Single pass.
- **Space Complexity:** $O(N)$ auxiliary stack.

---

### Anchor Problem #5: Trapping Rain Water (LeetCode 42)
> **Problem:** Given $n$ non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.

#### 1. Brute Force Instinct
For each bar $i$, find max height to left and max height to right: $\text{water} = \min(leftMax, rightMax) - height[i] \implies O(N^2)$.

#### 2. "Aha!" Observation
Monotonic decreasing stack computes horizontal trapped water strips:
- As long as heights decrease, push onto stack.
- When a taller bar $right$ arrives, it forms a trough with the popped bar as bottom, and the new stack peek as left boundary.
- $\text{Trapped Water} = (\min(height[left], height[right]) - height[bottom]) \times (right - left - 1)$.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.stackandqueue.stack;

import java.util.ArrayDeque;
import java.util.Deque;

public class TrappingRainWaterAnchor {
    public int trap(int[] height) {
        if (height == null || height.length < 3) return 0;

        Deque<Integer> stack = new ArrayDeque<>();
        int totalWater = 0;

        for (int right = 0; right < height.length; right++) {
            while (!stack.isEmpty() && height[right] > height[stack.peek()]) {
                int bottomIdx = stack.pop();
                if (stack.isEmpty()) break; // No left wall

                int left = stack.peek();
                int boundedHeight = Math.min(height[left], height[right]) - height[bottomIdx];
                int width = right - left - 1;

                totalWater += (boundedHeight * width);
            }
            stack.push(right);
        }
        return totalWater;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(N)$

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Storing Values vs Storing Indices:**
   - In 95% of monotonic stack problems, **store indices**, not values! Storing the index gives you both the value (`nums[i]`) AND enables $O(1)$ distance/width calculations (`currIdx - prevIdx`).
2. **Sentinel Bar in Histograms:**
   - Forgetting the virtual 0-height bar at index $N$ means strictly increasing inputs (e.g., `[1, 2, 3, 4]`) will never trigger any pops, returning 0 area!
3. **Empty Stack After Pop:**
   - In histogram width computation: if the stack becomes empty after `pop()`, it means the popped bar was the smallest element seen so far $\implies$ its width extends all the way to index 0 ($\text{width} = i$).
