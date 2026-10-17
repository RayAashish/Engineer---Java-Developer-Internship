# Phase 1: Arrays — Patterns, Mental Models & Trigger Signals
> **Author:** raysi  
> **Package:** `com.raysi.dsa.arrays`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Prerequisites:** Basic Java Syntax, Primitive Types vs Reference Arrays, Big-O Notation  

---

# 1. Data Structure Mental Model & Memory Mechanics

### How Arrays Behave in Memory
An array in Java (`int[] arr = new int[N]`) is a **contiguous sequence of homogeneous memory blocks** allocated on the JVM Heap.

```
Index:         0       1       2       3       4
Value:       [ 12  |  45  |   7  |  89  |  23  ]
Memory Addr: 0x1000  0x1004  0x1008  0x100C  0x1010  (Assuming 4 bytes per 32-bit int)
```

1. **Random Access in $O(1)$ Time:**  
   Because memory is contiguous, the physical address of element $i$ is calculated via direct pointer arithmetic:
   $$\text{Address}(arr[i]) = \text{Base Address} + (i \times \text{SizeOfElement})$$
   The CPU performs a single multiply and add instruction to read any index instantaneously.
2. **CPU Cache Line Locality (L1 / L2 / L3 Cache):**  
   Modern CPUs do not fetch individual integers from RAM. When `arr[0]` is accessed, the hardware pre-fetches a **64-byte Cache Line** containing the next 15 contiguous 32-bit integers. Sequential array iteration (`for (int x : arr)`) produces near-zero cache misses, running orders of magnitude faster than pointer-chasing structures like `LinkedList`.
3. **The Bottleneck (Shifting Cost):**  
   Insertion or deletion at index $k$ requires shifting all $(N - 1 - k)$ subsequent elements by one position. In the worst case (inserting at index 0), this is an $O(N)$ memory copy operation (`System.arraycopy`).

---

### The "Constraint-to-Algorithm" Cheat Sheet for Arrays
When reading an array problem in an Online Assessment or technical interview, look at the value of $N$ before reading the narrative:

| Constraint Range ($N$) | Allowed Big-O | Array Patterns & Architectural Signals |
| :--- | :--- | :--- |
| **$N \le 20$** | $O(2^N)$ or $O(N!)$ | Bitmasking subsets, recursion, generating all permutations. |
| **$N \le 500$** | $O(N^3)$ | 3 nested loops (e.g., 3-Sum brute force, 2D subarray sum intervals). |
| **$N \le 2 \times 10^3$** | $O(N^2)$ | Pairwise comparison, nested loop brute force, 2D dynamic programming. |
| **$N \le 2 \times 10^5$** | $O(N \log N)$ or $O(N)$ | **The Interview Sweet Spot:** Two Pointers, Sliding Window, Prefix Sum + HashMap, Kadane's, Sorting + Scan. |
| **$N \le 10^7$** | $O(N)$ | Single-pass scan, Dutch National Flag, In-place state marking, Frequency array. |
| **$N \ge 10^9$ / Range** | $O(\log N)$ or $O(1)$ | **Binary Search on Answer Space**, Math formula, Constant space bit trick. |

---

# 2. The Core Pattern Taxonomy (How to Spot the Pattern)

We break down 80% of product-company array problems into **5 repeatable algorithmic patterns**:

---

## Pattern 1: Two Pointers (Opposite & Same Direction)

### Trigger Keywords in Problem Statement:
- *"Sorted array"* or *"Sort the array without altering relative stability"*
- *"Find a pair / triplet that sums to target $K$"*
- *"Remove duplicates in-place"*, *"Move all zeros to end"*
- *"Container with most water"*, *"Trapping rainwater"*, *"Reverse array / palindrome"*

### Visual ASCII Dry Run: Opposite Direction (Two Sum Sorted)
```
Input: nums = [2, 7, 11, 15], Target = 18

Step 1:
  left=0 (val=2), right=3 (val=15)
  Current Sum = 2 + 15 = 17 < 18 (Target)
  Action: Sum is too small. Increment left to access larger values.
  [ 2 ,  7 , 11 , 15 ]
    ^              ^
   left          right

Step 2:
  left=1 (val=7), right=3 (val=15)
  Current Sum = 7 + 15 = 22 > 18
  Action: Sum is too large. Decrement right to access smaller values.
  [ 2 ,  7 , 11 , 15 ]
         ^         ^
        left     right

Step 3:
  left=1 (val=7), right=2 (val=11)
  Current Sum = 7 + 11 = 18 == 18
  Found Target Pair! Return indices [1, 2].
```

### Reusable Java Code Template:
```java
package com.raysi.dsa.arrays;

public class TwoPointersTemplate {
    
    // Pattern 1A: Opposite Direction (Converging from boundaries)
    public int[] oppositeDirection(int[] sortedArr, int target) {
        int left = 0;
        int right = sortedArr.length - 1;
        
        while (left < right) {
            int currentSum = sortedArr[left] + sortedArr[right];
            if (currentSum == target) {
                return new int[]{left, right};
            } else if (currentSum < target) {
                left++; // Need larger sum
            } else {
                right--; // Need smaller sum
            }
        }
        return new int[]{-1, -1};
    }
    
    // Pattern 1B: Same Direction (Slow / Fast runner for in-place deduplication)
    public int sameDirectionInPlace(int[] arr) {
        if (arr.length == 0) return 0;
        int slow = 0;
        for (int fast = 1; fast < arr.length; fast++) {
            if (arr[fast] != arr[slow]) {
                slow++;
                arr[slow] = arr[fast]; // Overwrite unique position
            }
        }
        return slow + 1; // Length of unique prefix
    }
}
```

---

## Pattern 2: Sliding Window (Fixed & Variable Size)

### Trigger Keywords in Problem Statement:
- *"Contiguous subarray"* or *"Contiguous substring"*
- *"Longest / Shortest subarray satisfying condition $C$"*
- *"Maximum sum subarray of exact size $K$"*
- *"Subarray with at most $K$ distinct elements"*

### Visual ASCII Dry Run: Variable Sliding Window (Longest with Sum $\le 6$)
```
Input: nums = [2, 1, 4, 3], MaxSum = 6

right=0: add nums[0]=2. Window=[2], sum=2 <= 6. maxLen=1.
right=1: add nums[1]=1. Window=[2, 1], sum=3 <= 6. maxLen=2.
right=2: add nums[2]=4. Window=[2, 1, 4], sum=7 > 6.
         Violates condition! Shrink from left:
         remove nums[0]=2. left becomes 1. Window=[1, 4], sum=5 <= 6. maxLen=2.
right=3: add nums[3]=3. Window=[1, 4, 3], sum=8 > 6.
         Shrink: remove nums[1]=1. left becomes 2. Window=[4, 3], sum=7 > 6.
         Shrink: remove nums[2]=4. left becomes 3. Window=[3], sum=3 <= 6. maxLen=2.

Result: Max contiguous window length = 2.
```

### Reusable Java Code Template:
```java
package com.raysi.dsa.arrays;

public class SlidingWindowTemplate {

    // Pattern 2A: Fixed Size Window of size K
    public int maxFixedWindowSum(int[] arr, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) windowSum += arr[i];
        
        int maxSum = windowSum;
        for (int right = k; right < arr.length; right++) {
            windowSum += arr[right] - arr[right - k]; // Slide: Add new, drop oldest
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // Pattern 2B: Variable Size Window (Expand Right, Shrink Left)
    public int longestSubarraySatisfyingCondition(int[] arr, int targetLimit) {
        int left = 0;
        int currentSum = 0;
        int maxLength = 0;

        for (int right = 0; right < arr.length; right++) {
            currentSum += arr[right]; // 1. Expand right

            // 2. Shrink while invalid
            while (currentSum > targetLimit && left <= right) {
                currentSum -= arr[left];
                left++;
            }

            // 3. Record valid window length
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }
}
```

---

## Pattern 3: Prefix Sum + HashMap Lookup

### Trigger Keywords in Problem Statement:
- *"Count of subarrays with sum equal to $K$"*
- *"Longest subarray with sum equal to $K$ (contains NEGATIVE numbers)"*
- *"Subarray with sum divisible by $K$"*
- *"Equal number of 0s and 1s"* (transform 0 to -1)

### Visual ASCII Dry Run: Finding Subarray with Sum = $K$
```
Mathematical Principle:
Sum(Subarray from index i+1 to j) = PrefixSum[j] - PrefixSum[i]
If we require SubarraySum == K, then:
PrefixSum[i] = PrefixSum[j] - K

nums = [3, 4, 7, 2, -3, 1, 4, 2], K = 7
At index j=2 (num=7):
  Running PrefixSum = 3 + 4 + 7 = 14
  Needed previous prefix: PrefixSum - K = 14 - 7 = 7
  Did we see prefix 7 earlier?
  Yes, at index 1 (3 + 4 = 7)!
  Therefore, elements between index 1 and 2 (nums[2..2] = [7]) sum to 7!
```

### Reusable Java Code Template:
```java
package com.raysi.dsa.arrays;

import java.util.HashMap;
import java.util.Map;

public class PrefixSumHashMapTemplate {

    // Count subarrays with sum == K
    public int subarraySumEqualsK(int[] nums, int k) {
        int count = 0;
        int currentPrefixSum = 0;
        
        // Map stores (prefixSum -> frequency)
        Map<Integer, Integer> prefixFreq = new HashMap<>();
        prefixFreq.put(0, 1); // CRITICAL: Base case for prefix equals K directly

        for (int x : nums) {
            currentPrefixSum += x;
            int targetNeeded = currentPrefixSum - k;
            
            if (prefixFreq.containsKey(targetNeeded)) {
                count += prefixFreq.get(targetNeeded);
            }
            
            prefixFreq.put(currentPrefixSum, prefixFreq.getOrDefault(currentPrefixSum, 0) + 1);
        }
        return count;
    }
}
```

---

## Pattern 4: Single-Pass Invariant State Tracking (Kadane's & Dutch National Flag)

### Trigger Keywords in Problem Statement:
- *"Maximum contiguous subarray sum"*
- *"Sort an array consisting of only 0s, 1s, and 2s"* (3 distinct partitions)
- *"Single pass with $O(1)$ extra space"*

### Visual ASCII Dry Run: Dutch National Flag (3 Pointers)
```
Partitioning Invariants:
[ 0 .. low-1 ]     -> Strictly 0
[ low .. mid-1 ]   -> Strictly 1
[ mid .. high ]    -> Unprocessed / Unknown
[ high+1 .. N-1 ]  -> Strictly 2

Array: [2, 0, 2, 1, 1, 0]
Initial: low=0, mid=0, high=5

Step 1: nums[mid]=2 -> Swap(mid, high), high--
        nums: [0, 0, 2, 1, 1, 2], low=0, mid=0, high=4
Step 2: nums[mid]=0 -> Swap(low, mid), low++, mid++
        nums: [0, 0, 2, 1, 1, 2], low=1, mid=1, high=4
Step 3: nums[mid]=0 -> Swap(low, mid), low++, mid++
        nums: [0, 0, 2, 1, 1, 2], low=2, mid=2, high=4
Step 4: nums[mid]=2 -> Swap(mid, high), high--
        nums: [0, 0, 1, 1, 2, 2], low=2, mid=2, high=3
Step 5: nums[mid]=1 -> mid++
        nums: [0, 0, 1, 1, 2, 2], low=2, mid=3, high=3
Step 6: nums[mid]=1 -> mid++
        nums: [0, 0, 1, 1, 2, 2], low=2, mid=4, high=3 (mid > high: Terminate!)
```

### Reusable Java Code Template:
```java
package com.raysi.dsa.arrays;

public class SinglePassInvariantTemplate {

    // Kadane's Algorithm: Max Subarray Sum
    public int kadanesAlgorithm(int[] nums) {
        int maxSoFar = nums[0];
        int currentMax = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // Decision: Extend existing subarray or start fresh from current element
            currentMax = Math.max(nums[i], currentMax + nums[i]);
            maxSoFar = Math.max(maxSoFar, currentMax);
        }
        return maxSoFar;
    }

    // Dutch National Flag: 3-way partition
    public void sortZeroOneTwo(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low, mid);
                low++;
                mid++;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                swap(nums, mid, high);
                high--; // Notice mid is NOT incremented; swapped element must be re-tested
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
```

---

## Pattern 5: Binary Search on Answer Space

### Trigger Keywords in Problem Statement:
- *"Find minimum value of $X$ such that condition $F(X)$ is possible"*
- *"Find maximum value such that..."*
- *"Minimize the maximum allocation"* (Painter's partition, Book allocation, Ship packages)
- Monotonicity Property: If answer $K$ is valid, every value $\ge K$ is also valid!

### Visual ASCII Dry Run: Monotonic Predicate Curve
```
Answer Range: Low = Min possible value, High = Max possible value
Predicate isFeasible(speed):

Speed (k):       1      2      3      4      5      6      7
isFeasible(k):  FALSE  FALSE  FALSE  TRUE   TRUE   TRUE   TRUE
                                       ^
                            First TRUE = Optimal Answer!
```

### Reusable Java Code Template:
```java
package com.raysi.dsa.arrays;

public class BinarySearchOnAnswerTemplate {

    public int findMinimumValidAnswer(int[] items, int threshold) {
        int low = getMinimumPossibleAnswer(items);
        int high = getMaximumPossibleAnswer(items);
        int optimalAns = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (isFeasible(items, mid, threshold)) {
                optimalAns = mid;      // Record possible answer
                high = mid - 1;       // Try to find an even smaller valid answer
            } else {
                low = mid + 1;        // Impossible at mid; must increase answer
            }
        }
        return optimalAns;
    }

    private boolean isFeasible(int[] items, int candidate, int threshold) {
        // Evaluate condition in O(N) time
        return true; 
    }

    private int getMinimumPossibleAnswer(int[] items) { return 1; }
    private int getMaximumPossibleAnswer(int[] items) { return 1_000_000_000; }
}
```

---

# 3. 5 Solved Anchor Problems (Brute $\to$ Better $\to$ Optimal Evolution)

---

### Anchor Problem 1: Two Sum II — Input Array Is Sorted
**Problem:** Given a 1-indexed sorted array of integers `numbers`, find two numbers such that they add up to a specific `target`. Return their 1-based indices.

#### 1. The Brute Force Instinct:
Iterate with two nested loops: for every index $i$ from 0 to $N-1$, iterate $j$ from $i+1$ to $N-1$ and check if `numbers[i] + numbers[j] == target`.
- **Why it bottlenecks:** It completely ignores the sorted nature of the array, performing $O(N^2)$ comparisons.

#### 2. The "Aha!" Observation (The Bridge):
Because the array is sorted, if the sum of the smallest available number (`numbers[left]`) and the largest available number (`numbers[right]`) is smaller than `target`, **no number in the array can pair with `numbers[left]` to reach `target`**! We can safely discard `left` and do `left++`. The sorted monotonic relationship eliminates $N$ candidate pairs in $O(1)$ time.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.arrays;

public class TwoSumSorted {
    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) {
                return new int[]{left + 1, right + 1}; // 1-indexed
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }
}
```
- **Time Complexity:** $O(N)$ — In each step, either `left` increments or `right` decrements. Exactly $N$ pointer moves.
- **Space Complexity:** $O(1)$ — Only two scalar pointers used.

---

### Anchor Problem 2: Longest Substring Without Repeating Characters
**Problem:** Given a string `s`, find the length of the longest substring without repeating characters.

#### 1. The Brute Force Instinct:
Generate all $O(N^2)$ substrings. For each substring, iterate through its characters and use a `HashSet` to verify uniqueness in $O(N)$.
- **Why it bottlenecks:** Overall $O(N^3)$ time. Substrings starting at index $i$ recalculate character uniqueness that was already checked for previous prefixes.

#### 2. The "Aha!" Observation (The Bridge):
Use a dynamic Sliding Window `[left, right]`. Store the last seen index of each character in an ASCII frequency/index map. When character `s[right]` was previously seen at `prevIndex` inside the current window (`prevIndex >= left`), **we do not need to increment `left` one step at a time**. We jump `left` directly to `prevIndex + 1`!

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.arrays;

public class LongestUniqueSubstring {
    public int lengthOfLongestSubstring(String s) {
        int[] lastSeen = new int[128]; // ASCII index lookup table
        java.util.Arrays.fill(lastSeen, -1);

        int maxLength = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (lastSeen[c] >= left) {
                left = lastSeen[c] + 1; // Jump past duplicate
            }
            lastSeen[c] = right;
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }
}
```
- **Time Complexity:** $O(N)$ — Single pass over string of length $N$.
- **Space Complexity:** $O(1)$ — Fixed auxiliary array of size 128.

---

### Anchor Problem 3: Subarray Sum Equals $K$ (Contains Negative Numbers)
**Problem:** Given an integer array `nums` and an integer `k`, return the total number of subarrays whose sum equals `k`.

#### 1. The Brute Force Instinct:
Calculate sum of every contiguous slice `nums[i..j]` using two nested loops:
- **Why it bottlenecks:** $O(N^2)$ time. Moreover, standard two-pointer sliding window **fails completely** because negative numbers break the monotonicity of window shrinking!

#### 2. The "Aha!" Observation (The Bridge):
Express subarray sum as prefix differences:
$$\text{Sum}(nums[i..j]) = \text{prefixSum}[j] - \text{prefixSum}[i-1] = K \implies \text{prefixSum}[i-1] = \text{prefixSum}[j] - K$$
As we traverse, we only need to query a HashMap: *"How many times have we seen a prefix sum equal to `(currentPrefixSum - K)` in the past?"*

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.arrays;

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int prefixSum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // Crucial: represents an empty prefix

        for (int x : nums) {
            prefixSum += x;
            int complement = prefixSum - k;
            if (map.containsKey(complement)) {
                count += map.get(complement);
            }
            map.put(prefixSum, map.getOrDefault(prefixSum, 0) + 1);
        }
        return count;
    }
}
```
- **Time Complexity:** $O(N)$ — Single pass through array with $O(1)$ average hash map queries.
- **Space Complexity:** $O(N)$ — Stores at most $N$ distinct prefix sums.

---

### Anchor Problem 4: Maximum Subarray Sum (Kadane's Algorithm)
**Problem:** Given an integer array `nums`, find the subarray with the largest sum and return its sum.

#### 1. The Brute Force Instinct:
Compute sum of all $N(N+1)/2$ subarrays, tracking the maximum.
- **Why it bottlenecks:** $O(N^2)$ time. A negative running sum is carried forward even when it hurts future elements.

#### 2. The "Aha!" Observation (The Bridge):
At index $i$, we ask a single local question:
> *"Does the previous running sum help me, or does it drag me down?"*
If `currentSum < 0`, adding it to `nums[i]` will make `nums[i]` smaller. Therefore, if `currentSum < 0`, discard it and reset `currentSum = nums[i]`. Otherwise, extend: `currentSum += nums[i]`.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.arrays;

public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        int currentMax = nums[0];
        int globalMax = nums[0];

        for (int i = 1; i < nums.length; i++) {
            currentMax = Math.max(nums[i], currentMax + nums[i]);
            globalMax = Math.max(globalMax, currentMax);
        }
        return globalMax;
    }
}
```
- **Time Complexity:** $O(N)$ — Exactly one loop over the array.
- **Space Complexity:** $O(1)$ — Two scalar integer variables.

---

### Anchor Problem 5: Capacity To Ship Packages Within $D$ Days
**Problem:** A conveyor belt has packages with weights `weights[i]`. Ship all packages within `days` days. A boat carries packages in contiguous order up to capacity $C$. Find the minimum capacity $C$.

#### 1. The Brute Force Instinct:
Try every possible capacity from `max(weights)` to `sum(weights)` incrementally: test capacity $C=10, 11, 12 \dots$ until one finishes within `days`.
- **Why it bottlenecks:** Search space is $S = \sum \text{weights}$. Sequential scan takes $O(S \times N)$, causing TLE when weights are large ($10^9$).

#### 2. The "Aha!" Observation (The Bridge):
Feasibility is **monotonic**:
- If capacity $C=15$ can ship packages in $D$ days, any capacity $> 15$ can also ship them in $\le D$ days.
- If capacity $C=10$ fails, any capacity $< 10$ will also fail.
Because the truth value transitions monotonically (`FALSE, FALSE, ... TRUE, TRUE`), we can **Binary Search the Capacity Range** in $O(\log(\sum W))$ steps!

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.arrays;

public class ShipPackages {
    public int shipWithinDays(int[] weights, int days) {
        int low = 0;
        int high = 0;
        for (int w : weights) {
            low = Math.max(low, w); // Must at least fit the heaviest single package
            high += w;              // Maximum possible capacity is shipping all in 1 day
        }

        int answer = high;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (canShip(weights, days, mid)) {
                answer = mid;
                high = mid - 1; // Try smaller capacity
            } else {
                low = mid + 1;  // Insufficient capacity
            }
        }
        return answer;
    }

    private boolean canShip(int[] weights, int maxDays, int capacity) {
        int daysUsed = 1;
        int currentLoad = 0;

        for (int w : weights) {
            if (currentLoad + w > capacity) {
                daysUsed++;
                currentLoad = 0;
            }
            currentLoad += w;
        }
        return daysUsed <= maxDays;
    }
}
```
- **Time Complexity:** $O(N \log(\sum \text{weights}))$ — Very fast ($30 \times 10^5 \approx 3 \times 10^6$ operations).
- **Space Complexity:** $O(1)$ — Constant memory.

---

# 4. Common Edge Cases & Off-By-One Traps

1. **Integer Overflow in Midpoint Calculation:**
   ```java
   // BUG: (left + right) can exceed Integer.MAX_VALUE (2^31 - 1) and become negative!
   int mid = (left + right) / 2;
   
   // FIX: Always use unsigned subtraction offset:
   int mid = left + (right - left) / 2;
   ```
2. **`while (left < right)` vs `while (left <= right)` in Binary Search:**
   - Use `while (left <= right)` when `high = mid - 1` and `low = mid + 1` (Standard target search or answer space search).
   - Use `while (left < right)` when `high = mid` (Boundary convergence / search for inflection point).
3. **Prefix Sum Base Case Absence (`map.put(0, 1)`):**
   - If a subarray starting at index `0` itself sums to $K$, `prefixSum - K = 0`. If `(0 -> 1)` is not present in the map, this subarray will be skipped, causing an off-by-one undercount!
4. **All Negative Numbers in Kadane's Algorithm:**
   - Initializing `maxSoFar = 0` produces wrong output `0` on array `[-5, -2, -8]`.
   - Always initialize `maxSoFar = nums[0]` and `currentMax = nums[0]`.
5. **Sliding Window Shrink Boundary Condition:**
   - When writing `while (condition && left <= right)`, ensure window doesn't invert (`left > right`) when the current single element itself violates constraints.
