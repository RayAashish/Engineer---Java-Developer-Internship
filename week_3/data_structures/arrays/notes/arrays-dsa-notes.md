# Arrays as a Data Structure: Detailed DSA Notes

> This document covers arrays from a **problem-solving / DSA** angle — the patterns, techniques, and reasoning used to solve array problems efficiently. For Java-specific syntax (declaration, multi-dimensional arrays, `Arrays` utility methods, varargs), see the dedicated Java arrays notes — this document builds on top of that and focuses entirely on **how to think about and solve array problems**.

An **array** is the simplest possible data structure: a fixed-size, **contiguous** block of memory holding elements of the same type, each reachable in O(1) via its index. Despite — or really, *because of* — that simplicity, arrays are the single most common substrate for algorithmic problems: an enormous fraction of all interview problems either operate directly on an array or reduce to one.

```text
Index:   0   1   2   3   4   5
Value:  [3,  7,  1,  9,  4,  2]
         ^
     O(1) access via index — the entire reason arrays are fast
```

---

## Table of Contents

1. [Introduction: Why Arrays Are Foundational](#1-introduction-why-arrays-are-foundational)
2. [The "Brain" Behind It: Contiguous Memory](#2-the-brain-behind-it-contiguous-memory)
3. [Static Arrays vs Dynamic Arrays](#3-static-arrays-vs-dynamic-arrays)
4. [Why Dynamic Array Resizing Is Amortized O(1)](#4-why-dynamic-array-resizing-is-amortized-o1)
5. [Complexity Summary Table](#5-complexity-summary-table)
6. [Two Pointers — The Theory](#6-two-pointers--the-theory)
7. [Sliding Window — The Theory](#7-sliding-window--the-theory)
8. [Prefix Sum — The Theory](#8-prefix-sum--the-theory)
9. [Kadane's Algorithm — The Theory](#9-kadanes-algorithm--the-theory)
10. [Binary Search on Arrays (and "Binary Search on the Answer")](#10-binary-search-on-arrays-and-binary-search-on-the-answer)
11. [Sorting-Based Techniques](#11-sorting-based-techniques)
12. [In-Place Array Manipulation Tricks](#12-in-place-array-manipulation-tricks)
13. [Hashing Alongside Arrays](#13-hashing-alongside-arrays)
14. [2D Arrays / Matrices as a Special Case](#14-2d-arrays--matrices-as-a-special-case)
15. [Types of Problems Arrays Solve](#15-types-of-problems-arrays-solve)
16. [Recognizing Which Technique to Use](#16-recognizing-which-technique-to-use)
17. [Top 20 Array Problems (By Pattern)](#17-top-20-array-problems-by-pattern)
18. [Sample Solutions (Worked)](#18-sample-solutions-worked)
19. [Common Pitfalls](#19-common-pitfalls)
20. [Quick Cheat Sheet](#20-quick-cheat-sheet)

---

## 1. Introduction: Why Arrays Are Foundational

Nearly every other data structure is either **built on top of an array** (dynamic arrays/`ArrayList`, heaps, hash tables' bucket arrays) or **commonly represented as one** (a string is effectively a character array; a matrix/grid is an array of arrays). Mastering array techniques pays off far beyond "array problems" specifically — it's the base layer underneath most of DSA.

```text
What makes arrays special as a STARTING point for problem-solving:

- O(1) random access by index (unlike a linked list, which needs O(n) traversal to reach the middle)
- Contiguous memory -> excellent cache locality -> fast in practice, not just in Big-O
- The SIMPLEST structure -> most problems get introduced on arrays first, before moving to trees/graphs
- A huge number of general ALGORITHMIC TECHNIQUES (not data-structure-specific) are taught on arrays first:
  two pointers, sliding window, prefix sums, binary search, greedy, sorting-based reasoning
```

---

## 2. The "Brain" Behind It: Contiguous Memory

An array's defining trait is that its elements sit in **one unbroken block of memory**, back to back. This single fact is what makes O(1) access possible: to find element `i`, the computer just computes `baseAddress + i * elementSize` — pure arithmetic, no searching, no pointer-chasing.

```text
Base address: 1000
Element size: 4 bytes (int)

arr[0] -> address 1000 + 0*4 = 1000
arr[3] -> address 1000 + 3*4 = 1012
arr[i] -> address 1000 + i*4     <- O(1), a single multiply-and-add, regardless of i
```

### 2.1 Why this is "the brain" of nearly every array technique

Almost every array algorithmic trick exists specifically to **exploit** this O(1) indexed access — two pointers walk indices toward/away from each other in O(1) steps; sliding window maintains a range defined purely by two indices; binary search halves the index range repeatedly; prefix sums precompute values **indexed** the same way as the original array. None of these would make sense, or would even be expressible as cleanly, on a structure without O(1) random access (like a linked list).

### 2.2 The cost side of the same coin: insertion/deletion in the middle

Because elements are packed contiguously with no gaps, inserting or deleting anywhere **except the end** requires physically **shifting** every subsequent element to close or open the gap — an O(n) operation. This is the direct tradeoff for O(1) access, and it's exactly why structures like linked lists exist for insertion-heavy workloads (see the stacks-and-queues notes for the array-vs-linked tradeoff explored generally).

```java
int[] arr = {1, 2, 3, 4, 5};
// To insert 99 at index 1: must shift {2,3,4,5} each one position right first
// [1, 99, 2, 3, 4, 5]   <- O(n) shift cost
```

---

## 3. Static Arrays vs Dynamic Arrays

| | Static array (plain `int[]`) | Dynamic array (`ArrayList`) |
|---|-----------------------------------|----------------------------------|
| Size | Fixed at creation, never changes | Grows/shrinks as needed |
| Underlying storage | One contiguous block, exactly that size | A contiguous block, usually with **extra unused capacity** at the end |
| Append cost | N/A — size is fixed | O(1) **amortized** (see section 4) |
| When full | Can't grow — must be a separate array | Automatically allocates a new, larger array and copies elements over |

```java
int[] staticArr = new int[5];          // exactly 5 slots, forever
List<Integer> dynamicArr = new ArrayList<>();  // starts small, grows automatically as elements are added
```

---

## 4. Why Dynamic Array Resizing Is Amortized O(1)

When a dynamic array (`ArrayList`) runs out of capacity, it allocates a **new**, larger backing array (typically **double** the old size) and copies every existing element over — an O(n) operation. Despite this, `add()` is still considered O(1) **amortized**. Understanding why is a classic, important piece of DSA theory.

```text
Starting capacity 1, doubling each time it's full:

Add 1st element:  capacity 1 -> full -> no resize needed yet (first element)
Add 2nd element:  capacity 1 is full -> RESIZE to 2 (copy 1 element)      cost: 1
Add 3rd element:  capacity 2 is full -> RESIZE to 4 (copy 2 elements)     cost: 2
Add 5th element:  capacity 4 is full -> RESIZE to 8 (copy 4 elements)     cost: 4
Add 9th element:  capacity 8 is full -> RESIZE to 16 (copy 8 elements)    cost: 8
...

Total cost of resizing across n insertions ≈ 1 + 2 + 4 + 8 + ... + n/2  ≈  2n  (a geometric series)
Average (amortized) cost per insertion  =  2n / n  =  O(1)
```

**The key insight:** even though *some* individual `add()` calls are expensive (O(n), when a resize happens to trigger), those expensive calls become **exponentially rarer** as the array grows, and the **total** cost summed across all n insertions is still only O(n) — which, spread evenly ("amortized") across all n operations, works out to O(1) per operation on average.

> This is precisely why doubling (not, say, adding a fixed +10 capacity each time) is the standard growth strategy — a fixed-increment growth strategy would make resizing happen every ~10 insertions forever, costing O(n) total resize work spread over only O(n/10) insertions, which degrades to O(n) **per insertion** amortized, not O(1).

---

## 5. Complexity Summary Table

| Operation | Static Array | Dynamic Array (`ArrayList`) |
|-----------|-----------------|----------------------------------|
| Access by index | O(1) | O(1) |
| Update by index | O(1) | O(1) |
| Search (unsorted) | O(n) | O(n) |
| Search (sorted, binary search) | O(log n) | O(log n) |
| Append at the end | N/A (fixed size) | O(1) amortized |
| Insert/delete at an arbitrary index | O(n) (shift required) | O(n) (shift required) |
| Insert/delete at the end | N/A | O(1) amortized |
| Space | O(n) | O(n), plus some unused capacity |

---

## 6. Two Pointers — The Theory

**Two pointers** means maintaining **two index variables** that move through the array according to some rule, instead of using nested loops — collapsing an O(n²) brute-force scan into a single O(n) pass.

### 6.1 Opposite-direction two pointers (converging)

Start one pointer at each end, moving them **toward each other**, typically used on a **sorted** array when searching for a pair/triplet satisfying some condition.

```java
boolean hasPairWithSum(int[] sortedArr, int target) {
    int left = 0, right = sortedArr.length - 1;
    while (left < right) {
        int sum = sortedArr[left] + sortedArr[right];
        if (sum == target) return true;
        else if (sum < target) left++;    // sum too small -> need a bigger value -> move left pointer up
        else right--;                      // sum too large -> need a smaller value -> move right pointer down
    }
    return false;
}
```

```text
Why moving "left++" when sum is too small is always safe (never misses a valid pair):
Since the array is SORTED, arr[left] paired with ANY index between left and right-1 is still <= current sum.
The only way to get a LARGER sum is to increase the smaller element — so left++ is the only productive move.
```

### 6.2 Same-direction two pointers (fast/slow, or read/write)

Both pointers move in the **same direction**, often at different speeds, or one acts as a "write position" while the other scans — common for in-place array modification (section 12).

```java
int removeDuplicates(int[] sortedArr) {
    int writeIndex = 1;                      // "slow" pointer: next position to write a unique value
    for (int readIndex = 1; readIndex < sortedArr.length; readIndex++) {   // "fast" pointer: scans ahead
        if (sortedArr[readIndex] != sortedArr[writeIndex - 1]) {
            sortedArr[writeIndex] = sortedArr[readIndex];
            writeIndex++;
        }
    }
    return writeIndex;    // the new logical length, with duplicates compacted out
}
```

---

## 7. Sliding Window — The Theory

A **sliding window** maintains a **contiguous range** `[left, right]` of the array, expanding and contracting that range incrementally, instead of re-examining every possible subarray from scratch. It's the dominant technique for "subarray"/"substring" problems with a size or sum constraint.

### 7.1 Fixed-size window

The window size `k` never changes — slide it one step at a time, removing the element leaving the window and adding the element entering it.

```java
int maxSumFixedWindow(int[] arr, int k) {
    int windowSum = 0;
    for (int i = 0; i < k; i++) windowSum += arr[i];    // build the FIRST window

    int maxSum = windowSum;
    for (int i = k; i < arr.length; i++) {
        windowSum += arr[i] - arr[i - k];   // add the new element, remove the one that just left the window
        maxSum = Math.max(maxSum, windowSum);
    }
    return maxSum;
}
```

### 7.2 Variable-size window

The window **grows** by moving `right`, and **shrinks** by moving `left`, based on whether the current window still satisfies some condition — this is the more common and more powerful form.

```java
int minSubArrayLen(int target, int[] nums) {
    int left = 0, sum = 0, minLen = Integer.MAX_VALUE;

    for (int right = 0; right < nums.length; right++) {
        sum += nums[right];                    // GROW the window by including nums[right]

        while (sum >= target) {                  // window is "valid" -> try to SHRINK it as much as possible
            minLen = Math.min(minLen, right - left + 1);
            sum -= nums[left];
            left++;
        }
    }
    return minLen == Integer.MAX_VALUE ? 0 : minLen;
}
```

### 7.3 Why sliding window is O(n), not O(n²)

Although there's a nested-looking structure (an outer loop moving `right`, an inner `while` moving `left`), **each pointer only ever moves forward, never backward**, across the entire run. Since `left` and `right` each traverse the array at most once in total (not once *per outer iteration*), the combined work across the whole algorithm is O(n) — the exact same amortized argument used for monotonic stacks/deques in the stacks-and-queues notes.

---

## 8. Prefix Sum — The Theory

A **prefix sum array** precomputes, for every index `i`, the **cumulative sum** of all elements from the start up to `i`. Once built, the sum of **any** subarray can be answered in O(1), turning a problem that would otherwise need repeated O(n) range-summing into O(n) total preprocessing + O(1) per query.

```java
int[] buildPrefixSum(int[] arr) {
    int[] prefix = new int[arr.length + 1];     // prefix[0] = 0 (sum of zero elements) — a common, useful convention
    for (int i = 0; i < arr.length; i++) {
        prefix[i + 1] = prefix[i] + arr[i];
    }
    return prefix;
}

int rangeSum(int[] prefix, int left, int right) {   // inclusive range [left, right], 0-indexed into the ORIGINAL array
    return prefix[right + 1] - prefix[left];
}
```

```text
arr:     [2, 4, 1, 7, 3]
prefix:  [0, 2, 6, 7, 14, 17]
          ^  ^  ^  ^   ^   ^
        idx:0  1  2  3   4   5

sum of arr[1..3] (values 4,1,7) = prefix[4] - prefix[1] = 14 - 2 = 12   <- matches 4+1+7=12, correct!
```

### 8.1 Why `prefix[right+1] - prefix[left]`, not `prefix[right] - prefix[left]`

`prefix[i]` is defined as the sum of the first `i` elements (indices `0` to `i-1`). To get the sum from index `left` through `right` **inclusive**, you need "everything up through `right`" minus "everything up through, but not including, `left`" — which is exactly `prefix[right+1] - prefix[left]`. Getting this off-by-one wrong is the single most common prefix-sum bug (see the pitfalls table).

### 8.2 Extension: prefix sum for subarray-sum-equals-K (with a hash map)

Combining prefix sums with a hash map (storing *how many times* each prefix sum value has occurred) turns "count subarrays summing to K" into a single O(n) pass — this pattern is worked in full in section 18.

---

## 9. Kadane's Algorithm — The Theory

**Kadane's algorithm** solves the **maximum subarray sum** problem (find the contiguous subarray with the largest sum) in a single O(n) pass, using a beautifully simple greedy/DP insight.

```java
int maxSubArray(int[] nums) {
    int currentSum = nums[0];
    int maxSum = nums[0];

    for (int i = 1; i < nums.length; i++) {
        currentSum = Math.max(nums[i], currentSum + nums[i]);   // the KEY decision, every step
        maxSum = Math.max(maxSum, currentSum);
    }
    return maxSum;
}
```

### 9.1 The core insight

At every position, there are only **two possibilities** for the best subarray *ending exactly here*: either **extend** the best subarray ending at the previous position (by adding the current element), or **start fresh** from the current element alone (if the running sum so far has become a net negative drag).

```text
"currentSum + nums[i]"  vs  "nums[i]" alone:

If currentSum (so far) is NEGATIVE, adding it only makes nums[i] worse — better to abandon it and start fresh.
If currentSum (so far) is POSITIVE (or zero), keeping it can only help — extend the existing subarray.

This decision is made FRESH at every index — Kadane's is a simple form of dynamic programming,
where "the best subarray ending at i" depends only on "the best subarray ending at i-1".
```

```text
nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]

i=0: currentSum=-2, maxSum=-2
i=1: currentSum=max(1, -2+1=-1)=1        maxSum=max(-2,1)=1
i=2: currentSum=max(-3, 1-3=-2)=-2        maxSum=max(1,-2)=1
i=3: currentSum=max(4, -2+4=2)=4          maxSum=max(1,4)=4
i=4: currentSum=max(-1, 4-1=3)=3          maxSum=max(4,3)=4
i=5: currentSum=max(2, 3+2=5)=5           maxSum=max(4,5)=5
i=6: currentSum=max(1, 5+1=6)=6           maxSum=max(5,6)=6
i=7: currentSum=max(-5, 6-5=1)=1          maxSum=max(6,1)=6
i=8: currentSum=max(4, 1+4=5)=5           maxSum=max(6,5)=6

Result: 6   (the subarray [4,-1,2,1], summing to 6)
```

**Complexity:** O(n) time, O(1) space — a dramatic improvement over the naive O(n²) (try every subarray) or O(n³) (try every subarray, resumming from scratch each time) brute force.

---

## 10. Binary Search on Arrays (and "Binary Search on the Answer")

Binary search works on any **sorted** array to find a target in O(log n), by repeatedly halving the search space.

```java
int binarySearch(int[] sortedArr, int target) {
    int low = 0, high = sortedArr.length - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;    // overflow-safe midpoint (see the operators notes)
        if (sortedArr[mid] == target) return mid;
        else if (sortedArr[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}
```

### 10.1 "Binary search on the answer" — a more advanced, very common pattern

Many problems don't look like a search at all, but secretly have a **monotonic** property (e.g., "if X works, then anything bigger/smaller than X also works") — which means you can **binary search over the range of possible answers** themselves, not over the array's values.

```text
Recognize this pattern when:
- The problem asks for a MINIMUM or MAXIMUM value satisfying some condition
  ("minimum days to...", "maximum value such that...", "smallest capacity to...")
- You can write a function canAchieve(x) that returns true/false, and that function is MONOTONIC
  (once it becomes true for some x, it stays true for all larger — or smaller — x)
```

```java
// Example shape: "find the minimum capacity such that we can ship all packages within D days"
int minCapacity(int[] weights, int days) {
    int low = maxOf(weights);          // capacity must be at least the heaviest single package
    int high = sumOf(weights);          // capacity never needs to exceed shipping everything in one day

    while (low < high) {
        int mid = low + (high - low) / 2;
        if (canShipWithinDays(weights, days, mid)) {
            high = mid;                   // mid WORKS — try to find something even smaller
        } else {
            low = mid + 1;                 // mid does NOT work — need more capacity
        }
    }
    return low;
}
```

This pattern is covered further via the "trigger phrases" in section 16 — it's one of the highest-leverage, most under-recognized techniques in array/optimization problems.

---

## 11. Sorting-Based Techniques

Sorting an array first (O(n log n)) often **unlocks** a much simpler or faster algorithm for whatever comes after — a very common and powerful preprocessing step.

```text
Why sort first:
- Enables two-pointer techniques (section 6.1) that only work correctly on sorted data
- Makes duplicates/near-duplicates trivially adjacent, simplifying dedup/grouping problems
- Turns "find pairs/triplets with property X" into a tractable two-pointer or binary-search problem
- Greedy interval-scheduling problems almost always start by sorting intervals by start (or end) time
```

```java
// Classic example: "3Sum" — find all triplets summing to zero
// Sorting FIRST turns an O(n^3) brute force into O(n^2): fix one element, two-pointer the rest
Arrays.sort(nums);
for (int i = 0; i < nums.length - 2; i++) {
    int left = i + 1, right = nums.length - 1;
    while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];
        // ... two-pointer logic as in section 6.1
    }
}
```

### 11.1 The Dutch National Flag algorithm (3-way partitioning)

A specialized single-pass technique for partitioning an array into **three** groups (e.g., sort an array of only 0s, 1s, and 2s) without using a general sort, in O(n) time and O(1) extra space, using **three pointers**.

```java
void sortColors(int[] nums) {       // array of only 0, 1, 2
    int low = 0, mid = 0, high = nums.length - 1;

    while (mid <= high) {
        if (nums[mid] == 0) {
            swap(nums, low++, mid++);     // 0 belongs at the front — advance both low and mid
        } else if (nums[mid] == 1) {
            mid++;                          // 1 is already in the correct middle region — just move on
        } else {                             // nums[mid] == 2
            swap(nums, mid, high--);         // 2 belongs at the end — swap it there, but DON'T advance mid yet
                                               // (the swapped-in value at mid hasn't been examined yet!)
        }
    }
}
```

---

## 12. In-Place Array Manipulation Tricks

Several problems ask for an O(1) extra space solution — modifying the array **in place** rather than building a new one.

### 12.1 Reversal for rotation

Rotating an array by `k` positions can be done in O(1) extra space using **three reversals**.

```java
void rotate(int[] nums, int k) {
    k %= nums.length;
    reverse(nums, 0, nums.length - 1);       // reverse the WHOLE array
    reverse(nums, 0, k - 1);                   // reverse the first k elements
    reverse(nums, k, nums.length - 1);          // reverse the remaining elements
}
```

```text
nums = [1,2,3,4,5,6,7], k=3

Reverse whole:        [7,6,5,4,3,2,1]
Reverse first k=3:    [5,6,7,4,3,2,1]
Reverse remaining:    [5,6,7,1,2,3,4]   <- correctly rotated right by 3
```

### 12.2 Negative-indexing trick for "mark presence" problems

For problems like "find all duplicates" or "find the missing number" within a bounded range `[1, n]`, you can use the **sign** of `arr[value - 1]` itself as a visited marker, avoiding any extra space.

```text
Core idea: for each value v encountered, go to index (v-1) and NEGATE the value stored there.
If you ever find a value that's ALREADY negative at that index, v is a duplicate.
```

### 12.3 Cyclic sort for "missing/duplicate number in range [1,n]" problems

Repeatedly swap each element into its "correct" position (`value` belongs at `index = value - 1`) until every element is where it should be — then a single pass reveals any missing/duplicate value by checking which index doesn't hold its expected value.

---

## 13. Hashing Alongside Arrays

A huge fraction of array problems become O(n) instead of O(n²) simply by trading O(n) extra space for a **hash set/map** that remembers what's been seen so far, avoiding repeated linear scans.

```java
// Classic example: Two Sum
int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> seen = new HashMap<>();   // value -> its index
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (seen.containsKey(complement)) {
            return new int[]{seen.get(complement), i};
        }
        seen.put(nums[i], i);
    }
    return new int[]{-1, -1};
}
```

This pattern — "for each element, check whether its *complement/partner* has already been seen" — is one of the single most common building blocks across array problems, and it's worked in full in section 18.

---

## 14. 2D Arrays / Matrices as a Special Case

A matrix is just "an array of arrays," and most 1D techniques extend naturally — but a few patterns are specific to the 2D case.

### 14.1 Row/column prefix sums for 2D range-sum queries

Extending section 8's idea to two dimensions lets you answer "sum of any rectangular sub-region" in O(1) after O(rows × cols) preprocessing.

### 14.2 Spiral/diagonal/boundary traversal

Problems asking to traverse a matrix in spiral order, by diagonals, or around its boundary are really just **careful index-bookkeeping** exercises — typically maintaining four boundary variables (`top`, `bottom`, `left`, `right`) that shrink inward as each ring/direction is fully consumed.

### 14.3 In-place rotation

Rotating a square matrix 90° in place is commonly done as **transpose, then reverse each row** (or reverse each column, then transpose) — directly analogous to the three-reversal rotation trick from section 12.1, just applied across two dimensions.

---

## 15. Types of Problems Arrays Solve

```text
- Searching and counting (target value, kth element, frequency analysis)
- Subarray/substring problems with a size, sum, or product constraint (sliding window)
- Pair/triplet/quadruplet problems with a sum or difference constraint (two pointers, hashing)
- Range-query problems (prefix sums, difference arrays)
- Optimization: minimum/maximum value satisfying a condition (binary search on the answer)
- In-place rearrangement and partitioning (Dutch flag, cyclic sort, reversal tricks)
- Interval scheduling and merging (sort-based greedy)
- Matrix/grid traversal and transformation
- Dynamic-programming-adjacent problems expressed over a 1D array (Kadane's, climbing stairs, house robber)
```

---

## 16. Recognizing Which Technique to Use

```text
Trigger phrases:

"sorted array" + "pair/triplet with sum ..."          -> two pointers (opposite-direction)
"contiguous subarray/substring" + a SIZE or SUM limit  -> sliding window
"range sum query" (called repeatedly)                   -> prefix sum
"maximum/minimum subarray sum"                           -> Kadane's algorithm
"find target in sorted array" / "find insertion point"   -> binary search
"minimum/maximum X such that condition holds"             -> binary search on the answer (monotonic predicate)
"sort" mentioned, or duplicates/grouping needed early      -> sort first, then two pointers / greedy
"only values 0,1,2" or "partition into 3 groups"           -> Dutch National Flag (3-way partition)
"numbers in range [1,n], find missing/duplicate"            -> cyclic sort, or negative-marking trick
"O(1) extra space" explicitly required                       -> in-place tricks (reversal, cyclic sort, marking)
"find pair/complement" without a sorted-array guarantee        -> hashing (HashMap/HashSet), not two pointers
"rotate matrix" / "spiral" / "set matrix zeroes"                -> 2D index bookkeeping, in-place where possible
```

### 16.1 A practical decision flow

```text
1. Does a HASH SET/MAP make an O(n^2) brute force O(n)?  (complements, duplicates, frequency)
     YES -> use hashing
2. Is the array SORTED (or can/should it be sorted)?
     YES -> consider two pointers or binary search
3. Is the question about a CONTIGUOUS range with a size/sum constraint?
     YES -> sliding window (fixed or variable size)
4. Is the question about REPEATED range-sum queries?
     YES -> prefix sum
5. Is the question "what's the min/max X such that some condition holds," and is that condition MONOTONIC in X?
     YES -> binary search on the answer
6. Does the problem explicitly require O(1) extra space / in-place modification?
     YES -> look for a reversal/partition/cyclic-sort/marking trick specific to the constraints given
```

---

## 17. Top 20 Array Problems (By Pattern)

★ = worked in section 18; the rest are for you to solve.

### Two Pointers family

1. ★ Two Sum (hashing variant) / Two Sum II (sorted, two-pointer variant)
2. 3Sum
3. Container With Most Water
4. Trapping Rain Water (two-pointer variant)

### Sliding Window family

5. ★ Maximum Sum Subarray of Size K / Minimum Size Subarray Sum
6. Longest Substring Without Repeating Characters
7. Longest Repeating Character Replacement

### Prefix Sum family

8. ★ Subarray Sum Equals K
9. Product of Array Except Self
10. Range Sum Query – Immutable

### Kadane's / DP-on-Array family

11. ★ Maximum Subarray (Kadane's)
12. Maximum Product Subarray
13. Best Time to Buy and Sell Stock

### Binary Search family

14. Search in Rotated Sorted Array
15. Find First and Last Position of Element in Sorted Array
16. Koko Eating Bananas (binary search on the answer)
17. Capacity To Ship Packages Within D Days (binary search on the answer)

### Sorting / In-Place family

18. Sort Colors (Dutch National Flag)
19. Merge Intervals
20. Find the Duplicate Number (cyclic sort / Floyd's cycle technique)

---

## 18. Sample Solutions (Worked)

### 18.1 Two Sum

**Problem:** Given an array of integers `nums` and an integer `target`, return the indices of the two numbers that add up to `target`.

```java
import java.util.*;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();   // value -> index

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);     // record AFTER checking, so we never use the same element twice
        }
        return new int[]{-1, -1};      // no valid pair found
    }
}
```

```text
nums = [2, 7, 11, 15], target = 9

i=0, nums[0]=2: complement=7, seen={} -> not found -> seen={2:0}
i=1, nums[1]=7: complement=2, seen={2:0} -> FOUND! seen.get(2)=0 -> return [0, 1]
```

**Why check-before-insert order matters:** if `nums[i]` were inserted into `seen` *before* checking, an element could incorrectly be paired with itself (e.g., `target = 4`, `nums[i] = 2` would find its own just-inserted entry).

**Complexity:** O(n) time, O(n) space — a direct improvement over the O(n²) brute force (checking every pair).

---

### 18.2 Minimum Size Subarray Sum

**Problem:** Given an array of positive integers `nums` and a target sum, find the minimal length of a contiguous subarray whose sum is `>= target`. Return 0 if no such subarray exists.

```java
class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];                 // GROW: include nums[right] in the window

            while (sum >= target) {               // window is valid — try to SHRINK as much as possible
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
}
```

```text
nums = [2,3,1,2,4,3], target = 7

right=0 (val=2): sum=2,  2<7, no shrink
right=1 (val=3): sum=5,  5<7, no shrink
right=2 (val=1): sum=6,  6<7, no shrink
right=3 (val=2): sum=8,  8>=7 -> minLen=min(INF,4)=4 -> sum-=nums[0]=2 -> sum=6, left=1 -> 6<7, stop shrinking
right=4 (val=4): sum=10, 10>=7 -> minLen=min(4,4)=4 -> sum-=nums[1]=3 -> sum=7, left=2 -> 7>=7 ->
                                    minLen=min(4,3)=3 -> sum-=nums[2]=1 -> sum=6, left=3 -> 6<7, stop
right=5 (val=3): sum=9,  9>=7 -> minLen=min(3,3)=3 -> sum-=nums[3]=2 -> sum=7, left=4 -> 7>=7 ->
                                    minLen=min(3,2)=2 -> sum-=nums[4]=4 -> sum=3, left=5 -> 3<7, stop

Result: 2   (subarray [4,3], summing to 7)
```

**Complexity:** O(n) time — `left` and `right` each traverse the array at most once in total (same amortized argument as section 7.3). O(1) space.

---

### 18.3 Subarray Sum Equals K

**Problem:** Given an array of integers `nums` and an integer `k`, return the **total number** of contiguous subarrays whose sum equals `k`. (Note: unlike sections 18.2, elements can be negative, so sliding window's "shrink when too big" logic doesn't apply here — this is exactly why prefix sum + hashing is needed instead.)

```java
import java.util.*;

class Solution {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixSumCount = new HashMap<>();
        prefixSumCount.put(0, 1);     // base case: a prefix sum of 0 has occurred once (before any elements)

        int sum = 0, count = 0;

        for (int num : nums) {
            sum += num;                                  // running prefix sum up to the current index

            // if (sum - k) has occurred before, that earlier point + current point forms a subarray summing to k
            if (prefixSumCount.containsKey(sum - k)) {
                count += prefixSumCount.get(sum - k);
            }

            prefixSumCount.merge(sum, 1, Integer::sum);    // record this prefix sum occurrence
        }
        return count;
    }
}
```

```text
nums = [1, 2, 3], k = 3

prefixSumCount = {0: 1}
sum=0, count=0

num=1: sum=1. need (sum-k)=1-3=-2, not in map. count stays 0. map: {0:1, 1:1}
num=2: sum=3. need (sum-k)=3-3=0, IS in map with count 1 -> count += 1 -> count=1. map: {0:1, 1:1, 3:1}
num=3: sum=6. need (sum-k)=6-3=3, IS in map with count 1 -> count += 1 -> count=2. map: {0:1,1:1,3:1,6:1}

Result: 2   (subarrays [1,2] summing to 3, and [3] summing to 3)
```

**Why `sum - k`, and why this works:** if the prefix sum up to the current index is `sum`, and at some *earlier* index the prefix sum was exactly `sum - k`, then the elements **between** those two points sum to exactly `(sum) - (sum - k) = k` — precisely the subarray being counted. The hash map lets this check happen in O(1) per element, rather than re-summing every possible starting point.

**Complexity:** O(n) time, O(n) space — compare this to the O(n²) brute force of checking every subarray's sum directly.

---

## 19. Common Pitfalls

| # | Pitfall | Fix |
|---|---------|-----|
| 1 | Using two pointers (opposite-direction) on an UNSORTED array | That technique's correctness relies specifically on sorted order — sort first, or use hashing instead |
| 2 | Off-by-one errors in prefix sum range queries | Remember `prefix[right+1] - prefix[left]` for an inclusive `[left, right]` range, with `prefix[0] = 0` |
| 3 | Forgetting Kadane's needs to reset to the CURRENT element (not 0) when the running sum turns negative | The decision is `max(nums[i], currentSum + nums[i])`, not `max(0, currentSum + nums[i])` — the latter is a different (non-negative-only) variant |
| 4 | Assuming sliding window is O(n^2) because of the nested loop shape | Both pointers only move forward across the whole run — the combined work is O(n), amortized |
| 5 | Using sliding window's "shrink while sum too big" logic on arrays with NEGATIVE numbers | That shrink logic assumes sums only grow as the window grows — breaks with negatives; use prefix sum + hashing instead |
| 6 | Computing `(low + high) / 2` for binary search midpoint | Risk of integer overflow for large indices — use `low + (high - low) / 2` instead (see the operators notes) |
| 7 | Forgetting "binary search on the answer" requires a MONOTONIC predicate | If `canAchieve(x)` isn't monotonic in x, binary search over the answer range gives wrong results |
| 8 | In the Dutch National Flag algorithm, advancing `mid` after swapping with `high` | The swapped-in value at `mid` hasn't been examined yet — only advance `mid` after a swap with `low` (or when the value is already `1`) |
| 9 | Forgetting to initialize `prefixSumCount` with `{0: 1}` in subarray-sum-equals-K style problems | Misses subarrays that start from index 0 itself |
| 10 | Treating `ArrayList.add()` as always strictly O(1), with no caveats | It's O(1) **amortized** — individual calls can occasionally cost O(n) when a resize is triggered |
| 11 | Forgetting arrays have O(n) insertion/deletion in the middle | This is the direct tradeoff for O(1) indexed access — plan accordingly if the problem is insertion-heavy |

---

## 20. Quick Cheat Sheet

```text
WHY ARRAYS ARE FAST     Contiguous memory -> O(1) indexed access via base+i*size arithmetic
                       Tradeoff: O(n) insert/delete in the middle (shifting required)

DYNAMIC ARRAY GROWTH     Doubling capacity on overflow -> O(1) AMORTIZED append
                       (occasional O(n) resize, but exponentially rarer as the array grows)

TWO POINTERS             Opposite-direction: sorted array, converge toward each other (pair-sum style)
                       Same-direction: read/write or fast/slow (in-place dedup, partitioning)

SLIDING WINDOW            Fixed size: add new element, remove one that left the window
                       Variable size: grow with right, shrink with left while condition violated/satisfied
                       O(n) amortized — both pointers move forward only, across the WHOLE run

PREFIX SUM                prefix[i] = sum of first i elements;  prefix[0] = 0
                       range sum [left,right] inclusive = prefix[right+1] - prefix[left]
                       Combine with a HashMap of prefix-sum counts for "count subarrays summing to K"

KADANE'S                  currentSum = max(nums[i], currentSum + nums[i])   <- decide fresh at EVERY index
                       maxSum = max(maxSum, currentSum)
                       O(n) time, O(1) space — maximum subarray sum

BINARY SEARCH              Standard: find target in SORTED array, O(log n)
                       On the answer: search the RANGE OF POSSIBLE ANSWERS when canAchieve(x) is monotonic
                       ALWAYS: mid = low + (high-low)/2   (overflow-safe)

SORTING-BASED               Sort first to unlock two-pointers / greedy / easy dedup
                       Dutch National Flag: 3-way in-place partition in O(n), O(1) space (low/mid/high pointers)

HASHING                    Trade O(n) space for turning O(n^2) "find a pair/complement" into O(n)
                       Classic shape: for each element, check whether its COMPLEMENT was already seen

2D / MATRIX                 2D prefix sums for O(1) rectangular range-sum queries
                       Spiral/boundary traversal: track shrinking top/bottom/left/right boundaries
                       In-place rotation: transpose + reverse rows (or columns)
```

**Remember:**

1. Everything about array efficiency traces back to one fact: contiguous memory gives O(1) indexed access, at the cost of O(n) insertion/deletion in the middle.
2. Two pointers and sliding window both look deceptively like O(n²) nested loops, but are O(n) amortized because each pointer only ever advances forward across the whole run — never backward, never restarting.
3. Prefix sums turn repeated range-sum queries from O(n) each into O(1) each, at the cost of O(n) one-time preprocessing — and combining them with a hash map unlocks "count subarrays with property X" problems elegantly.
4. Kadane's algorithm is a tiny, elegant dynamic program: at every index, decide fresh whether to extend the running subarray or abandon it and start over.
5. "Binary search on the answer" is one of the most underused, high-leverage techniques — whenever a problem asks for a min/max value under a monotonic condition, consider searching the *answer space*, not the array itself.
