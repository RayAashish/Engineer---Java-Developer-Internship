# Phase 0: Bit Manipulation — Patterns, Mental Models & Trigger Signals
> **Author:** raysi  
> **Package:** `com.raysi.dsa.foundations.bitmanipulation`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Prerequisites:** Binary Number Representation, Two's Complement  

---

# 1. Bitwise Mental Model & Memory Mechanics

### Two's Complement & The Bitwise Operators
All integers in Java are represented in binary using **32-bit Two's Complement**:
- Positive numbers: standard binary (MSB is `0`).
- Negative numbers: invert all bits of positive magnitude and add `1` (`-x = ~x + 1`).
- The 32nd bit is the **Sign Bit** (`0` = positive, `1` = negative).

```
Operator Reference Table:
Name              Symbol   Example (a=5 (0101), b=3 (0011))   Result
AND                 &      a & b = 0101 & 0011                0001 (1)
OR                  |      a | b = 0101 | 0011                0111 (7)
XOR                 ^      a ^ b = 0101 ^ 0011                0110 (6)
NOT (Complement)    ~      ~a = ~0000...0101                  1111...1010 (-6)
Left Shift          <<     a << 1 = 0101 << 1                 1010 (10)  (Multiply by 2)
Signed Right Shift  >>     -4 >> 1 (Preserves sign bit)        -2         (Divide by 2)
Unsigned Right Shift>>>    -4 >>> 1 (Fills MSB with 0)         2147483646
```

### The "Constraint-to-Algorithm" Cheat Sheet for Bit Manipulation
| Constraint ($N$) | Allowed Time Complexity | Expected Bitwise Pattern |
| :--- | :--- | :--- |
| **$N \le 20$** | $O(2^N)$ | Generating all subsets using bitmasks from $0$ to $(1 \ll N) - 1$. |
| **$N \le 30$** | $O(N)$ or $O(1)$ | Bitmask DP (Traveling Salesman), matching states as integer masks. |
| **$N \le 10^7$** | $O(N)$ | Single-pass XOR cancellation, parity accumulation. |
| **$N \le 10^9$ / Range** | $O(\log N)$ or $O(32) \approx O(1)$ | Bit counting (Brian Kernighan), Bitwise AND across prefix range, Bit-by-bit accumulation. |

---

# 2. The Core Pattern Taxonomy (How to Spot the Pattern)

---

## Pattern 1: Bitmask State Representation & In-Place Bit Twiddling
### Trigger Keywords:
- *"Subset selection"*, *"Toggle visited states without extra boolean array"*, *"Check if $i$-th flag is active"*

### The 6 Essential Bitwise Shortcuts:
```
1. Test i-th bit:        (n & (1 << i)) != 0
2. Set i-th bit:         n | (1 << i)
3. Clear i-th bit:       n & ~(1 << i)
4. Toggle i-th bit:      n ^ (1 << i)
5. Clear rightmost 1:    n & (n - 1)      <-- Brian Kernighan's trick
6. Isolate rightmost 1:  n & (-n)         <-- Two's complement trick
```

### Reusable Java Template:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class BitTwiddlingTemplate {
    public static boolean checkBit(int n, int i) { return (n & (1 << i)) != 0; }
    public static int setBit(int n, int i) { return n | (1 << i); }
    public static int clearBit(int n, int i) { return n & ~(1 << i); }
    public static int toggleBit(int n, int i) { return n ^ (1 << i); }
    public static int dropLowestSetBit(int n) { return n & (n - 1); }
    public static int lowestSetBit(int n) { return n & (-n); }
}
```

---

## Pattern 2: XOR Cancellation & Bucketing
### Trigger Keywords:
- *"Every element appears twice except one (or two)"*, *"Missing number in range $[0, n]$"*, *"Find the duplicate element"*

### Visual ASCII Dry Run: `[4, 1, 2, 1, 2]`
```
Properties: a ^ a = 0,  a ^ 0 = a,  a ^ b = b ^ a
Accumulating XOR:
  0 ^ 4 = 4
  4 ^ 1 = 5
  5 ^ 2 = 7
  7 ^ 1 = 6  (Notice: 1 cancelled out!)
  6 ^ 2 = 4  (Notice: 2 cancelled out!)
Result = 4! Exactly the single number.
```

### Reusable Java Template:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class XorCancellationTemplate {
    public static int findSingleNumber(int[] nums) {
        int xor = 0;
        for (int x : nums) xor ^= x;
        return xor;
    }
}
```

---

## Pattern 3: Power Set Generation via Iterative Bitmasks
### Trigger Keywords:
- *"Return all possible subsets (the power set)"*, *"Generate all combinations of size $N \le 20$"*

### Visual ASCII Dry Run: `nums = ['A', 'B', 'C']` ($N=3$)
```
Count of subsets = 2^3 = 8 (masks from 0 to 7):
Mask 0 (000_2) -> []
Mask 1 (001_2) -> ['A']
Mask 2 (010_2) -> ['B']
Mask 3 (011_2) -> ['A', 'B']
Mask 4 (100_2) -> ['C']
Mask 5 (101_2) -> ['A', 'C']
Mask 6 (110_2) -> ['B', 'C']
Mask 7 (111_2) -> ['A', 'B', 'C']
```

### Reusable Java Template:
```java
package com.raysi.dsa.foundations.bitmanipulation;

import java.util.ArrayList;
import java.util.List;

public class BitmaskSubsetsTemplate {
    public static List<List<Integer>> generateSubsets(int[] nums) {
        int n = nums.length;
        int totalSubsets = 1 << n; // 2^n
        List<List<Integer>> result = new ArrayList<>(totalSubsets);

        for (int mask = 0; mask < totalSubsets; mask++) {
            List<Integer> currentSubset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    currentSubset.add(nums[i]);
                }
            }
            result.add(currentSubset);
        }
        return result;
    }
}
```

---

# 3. 5 Solved Anchor Problems (Brute $\to$ Better $\to$ Optimal)

---

### Anchor Problem 1: Single Number
**Problem:** Given a non-empty array of integers `nums`, every element appears twice except for one. Find that single one in $O(N)$ time and $O(1)$ space.

#### 1. The Brute Force Instinct:
Count frequencies using a `HashMap<Integer, Integer>`. Return the key with frequency 1.
- **Bottleneck:** $O(N)$ extra space to store the map.

#### 2. The "Aha!" Observation:
XOR has commutative and associative properties. Any number XORed with itself is 0 ($x \oplus x = 0$), and any number XORed with 0 is itself ($x \oplus 0 = x$). XORing all numbers causes duplicate pairs to annihilate each other, leaving strictly the single unique number in $O(1)$ space.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class SingleNumber {
    public int singleNumber(int[] nums) {
        int result = 0;
        for (int x : nums) {
            result ^= x;
        }
        return result;
    }
}
```
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(1)$

---

### Anchor Problem 2: Number of 1 Bits (Hamming Weight)
**Problem:** Given a positive integer $n$, write a function that returns the number of set bits it has (also known as the Hamming weight).

#### 1. The Brute Force Instinct:
Iterate all 32 bits from 0 to 31, doing `(n >> i) & 1` and checking if non-zero.
- **Bottleneck:** Always loops 32 times, even if $n = 1$ or has only one bit set.

#### 2. The "Aha!" Observation:
Brian Kernighan's trick: the expression `n & (n - 1)` **always unsets the lowest (rightmost) set bit** of $n$. We only loop as many times as there are 1 bits in $n$.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class HammingWeight {
    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1); // Clears rightmost set bit
            count++;
        }
        return count;
    }
}
```
- **Time Complexity:** $O(\text{Number of set bits}) \le 32$
- **Space Complexity:** $O(1)$

---

### Anchor Problem 3: Counting Bits (Range $[0, n]$)
**Problem:** Given an integer $n$, return an array `ans` of length $n + 1$ such that for each $i$ ($0 \le i \le n$), `ans[i]` is the number of 1's in the binary representation of $i$. Solve in linear time $O(N)$.

#### 1. The Brute Force Instinct:
Call Brian Kernighan's function for every integer $i$ from 0 to $n$.
- **Bottleneck:** $O(N \log N)$ total time.

#### 2. The "Aha!" Observation:
Use Bitwise Dynamic Programming! Notice:
`i & (i - 1)` has strictly fewer bits than `i` and is strictly smaller than `i`. Thus:
$$\text{ans}[i] = \text{ans}[i \ \& \ (i - 1)] + 1$$

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class CountingBits {
    public int[] countBits(int n) {
        int[] ans = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ans[i] = ans[i & (i - 1)] + 1;
        }
        return ans;
    }
}
```
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(1)$ extra space (excluding return array).

---

### Anchor Problem 4: Single Number III (Two Unique Numbers)
**Problem:** Given an integer array `nums`, in which exactly two elements appear only once and all the other elements appear exactly twice. Find the two elements in $O(N)$ time and $O(1)$ space.

#### 1. The Brute Force Instinct:
Sort the array and scan adjacent pairs, or use a HashSet.
- **Bottleneck:** Sorting is $O(N \log N)$; HashSet is $O(N)$ extra space.

#### 2. The "Aha!" Observation:
XORing all elements yields `xor = a ^ b`. Because $a \ne b$, `xor != 0`, meaning there is at least one set bit where $a$ and $b$ differ. Isolate this differentiating bit using `diff = xor & (-xor)`. Partition all array numbers into two groups based on whether this bit is set. $a$ and $b$ will fall into separate groups, and all duplicate pairs will cancel out inside their respective groups!

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class SingleNumberIII {
    public int[] singleNumber(int[] nums) {
        int xor = 0;
        for (int x : nums) xor ^= x;

        // Isolate rightmost set bit
        int diff = xor & (-xor);

        int a = 0;
        int b = 0;
        for (int x : nums) {
            if ((x & diff) != 0) {
                a ^= x;
            } else {
                b ^= x;
            }
        }
        return new int[]{a, b};
    }
}
```
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(1)$

---

### Anchor Problem 5: Bitwise AND of Numbers Range
**Problem:** Given two integers `left` and `right` representing the range $[left, right]$, return the bitwise AND of all numbers in this range, inclusive.

#### 1. The Brute Force Instinct:
Loop `for (int i = left; i <= right; i++) res &= i;`.
- **Bottleneck:** When $left=1, right=2 \times 10^9$, loops $2 \times 10^9$ times (TLE) and overflows 32-bit int.

#### 2. The "Aha!" Observation:
If any bit position flips between 0 and 1 anywhere in the range, the bitwise AND for that column **must be 0**. Therefore, the answer is simply the **Common Binary Prefix** of `left` and `right`, with all trailing bits zeroed out!

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.bitmanipulation;

public class RangeBitwiseAnd {
    public int rangeBitwiseAnd(int left, int right) {
        int shiftCount = 0;
        // Shift right until left and right share the exact common prefix
        while (left < right) {
            left >>= 1;
            right >>= 1;
            shiftCount++;
        }
        return left << shiftCount;
    }
}
```
- **Time Complexity:** $O(32) = O(1)$
- **Space Complexity:** $O(1)$

---

# 4. Common Edge Cases & Off-By-One Traps
1. **Operator Precedence Hazard:**
   - In Java, `==` has higher precedence than `&`!
   ```java
   if (n & 1 == 0)   // BUG! Compiles as n & (1 == 0) -> n & false (compile error)
   if ((n & 1) == 0) // CORRECT! Always parenthesize bitwise operations!
   ```
2. **Left Shift Integer Overflow:**
   - `1 << 31` overflows positive signed integers, becoming negative `-2147483648`.
   - When shifting $> 30$ positions, cast literal to `1L`: `1L << i`.
3. **`>>` (Signed) vs `>>>` (Unsigned):**
   - `-1 >> 1` remains `-1` (sign-extended with 1s). It will loop forever in `while (n > 0)`!
   - Use `n >>> 1` or terminate on `n != 0`.
