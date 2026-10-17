# Phase 0: Bit Manipulation — Self-Guided Problem Workbook (20 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.foundations.bitmanipulation`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Bitmask Arithmetic & Low-Bit Operations

---

### Problem #1: Number of 1 Bits (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Brian Kernighan's Algorithm
- **Problem Statement & Constraints:** Write a function that takes an unsigned 32-bit integer $n$ and returns the number of '1' bits it has (Hamming weight). $0 \le n \le 2^{32} - 1$.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $n = 11$ (binary `0000...1011`) $\to$ Output: `3`.
  - *Test Case 2 (Boundary):* $n = 0 \to$ Output: `0`.
  - *Test Case 3 (Boundary / Signed Edge):* $n = 2^{31} - 1$ (`0111...1111`) $\to$ Output: `31`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(32)$ Time, $O(1)$ Space):** Loop 32 times, test the lowest bit with `(n & 1)`, and logical right-shift `n >>>= 1`.
  - **2. Optimal Approach ($O(K)$ Time where $K$ is set bits, $O(1)$ Space):** While $n \ne 0$, repeatedly clear the lowest set bit using $n = n \ \& \ (n - 1)$ and increment counter. Terminates in exactly $K$ iterations.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Counting Bits (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Bit DP / Low-Bit Relation
- **Problem Statement & Constraints:** Given an integer $n$ ($0 \le n \le 10^5$), return an array `ans` of length $n + 1$ such that for each $i$ ($0 \le i \le n$), `ans[i]` is the number of 1's in the binary representation of $i$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 5 \to$ Output: `[0, 1, 1, 2, 1, 2]`.
  - *Test Case 2 (Boundary):* $n = 0 \to$ Output: `[0]`.
  - *Test Case 3 (Power of 2):* $n = 4 \to$ Output: `[0, 1, 1, 2, 1]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N \log N)$ Time, $O(N)$ Space):** Run `Integer.bitCount(i)` for each integer $0 \le i \le n$.
  - **2. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Use transition `ans[i] = ans[i >> 1] + (i & 1)` or `ans[i] = ans[i & (i - 1)] + 1`. The number of bits in $i$ equals the number of bits in $i$ stripped of its lowest bit, plus 1.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Power of Two (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Single Set Bit Invariant
- **Problem Statement & Constraints:** Given an integer $n$ ($-2^{31} \le n \le 2^{31} - 1$), return `true` if it is a power of two. Otherwise, return `false`.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 16 \to$ Output: `true`.
  - *Test Case 2 (Boundary / Negative Trap):* $n = -16 \to$ Output: `false`. Negative powers don't exist in integer range.
  - *Test Case 3 (Zero Edge):* $n = 0 \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(\log N)$ Time):** Repeatedly divide by 2 while $n > 0$ and $n \% 2 == 0$.
  - **2. Optimal Approach ($O(1)$ Time, $O(1)$ Space):** A number is a power of two if and only if $n > 0$ and clearing its lowest set bit yields 0: `(n & (n - 1)) == 0`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Power of Four (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Bitmask Alignment Check
- **Problem Statement & Constraints:** Given an integer $n$ ($-2^{31} \le n \le 2^{31} - 1$), return `true` if it is a power of four.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 16 \to$ Output: `true` (binary `10000`, 1 at index 4).
  - *Test Case 2 (Failure Trap):* $n = 8 \to$ Output: `false` (binary `1000`, 1 at odd index 3, power of 2 but not 4).
  - *Test Case 3 (Boundary):* $n = 1 \to$ Output: `true` ($4^0 = 1$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time, $O(1)$ Space):** Check three conditions: (1) $n > 0$, (2) $n$ is power of 2 via `(n & (n - 1)) == 0`, and (3) the single set bit resides at an even index: `(n & 0x55555555) != 0` (or `n % 3 == 1`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Reverse Bits of a 32-bit Integer (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Bit Shift & Reversal
- **Problem Statement & Constraints:** Reverse bits of a given 32-bit unsigned integer $n$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 43261596$ (`00000010100101000001111010011100`) $\to$ Output: `964176192` (`00111001011110000010100101000000`).
  - *Test Case 2 (Boundary):* $n = 1 \to$ Output: $2^{31} = -2147483648$ (as signed 32-bit).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(32)$ Time, $O(1)$ Space):** Loop 32 times. Maintain `result = 0`. In each step, shift `result = (result << 1) | (n & 1)` and unsigned-shift $n >>>= 1$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: XOR Invariants & Cancellation

---

### Problem #6: Single Number I (Difficulty: Easy)
- **Pattern Category:** Pattern 2: XOR Self-Inverse Invariant
- **Problem Statement & Constraints:** Given a non-empty array of integers $nums$ where every element appears twice except for one. Find that single one. $1 \le nums.length \le 3 \times 10^4$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [4, 1, 2, 1, 2] \to$ Output: `4`.
  - *Test Case 2 (Boundary):* $nums = [1] \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Initialize $xor = 0$. XOR all elements in $nums$. By properties $a \oplus a = 0$ and $a \oplus 0 = a$, all paired numbers cancel out, leaving the lone element.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Single Number II (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Modulo-3 Bit Frequency Counter
- **Problem Statement & Constraints:** Given an integer array $nums$ where every element appears three times except for one, which appears exactly once. Find the single element. Must achieve $O(N)$ time and $O(1)$ extra memory.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [2, 2, 3, 2] \to$ Output: `3`.
  - *Test Case 2 (Negative Numbers):* $nums = [0, 1, 0, 1, 0, 1, 99] \to$ Output: `99`.
  - *Test Case 3 (Boundary):* $nums = [-2, -2, 1, 1, 4, 1, 4, 4, -4, -2] \to$ Output: `-4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Better Approach ($O(32N)$ Time, $O(1)$ Space):** For each bit position $0 \le bit < 32$, count how many numbers have that bit set. Since triplets contribute $3 \times 1 \equiv 0 \pmod 3$, set `ans |= (count % 3) << bit`.
  - **2. Optimal Digital Logic Approach ($O(N)$ Time, $O(1)$ Space):** Simulate a finite state machine using two bitmasks `ones` and `twos`.
    - `ones = (ones ^ num) & ~twos`
    - `twos = (twos ^ num) & ~ones`
    Return `ones`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Single Number III (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Lowest-Set-Bit Partitioning
- **Problem Statement & Constraints:** Given an integer array $nums$ in which exactly two elements appear once and all other elements appear twice. Find the two elements in any order. $O(N)$ time and $O(1)$ space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1, 2, 1, 3, 2, 5] \to$ Output: `[3, 5]`.
  - *Test Case 2 (Boundary):* $nums = [-1, 0] \to$ Output: `[-1, 0]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):**
    - Step 1: XOR all elements $\to$ yields $xy = x \oplus y$.
    - Step 2: Extract lowest set bit of $xy$: `diff = xy & (-xy)`. Since $x \ne y$, at least one bit differs.
    - Step 3: Partition all numbers into two buckets based on whether that specific bit is set (`(num & diff) != 0`). XOR each bucket separately to isolate $x$ and $y$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Missing Number (Difficulty: Easy)
- **Pattern Category:** Pattern 2: XOR Index vs Value Matching
- **Problem Statement & Constraints:** Given an array $nums$ containing $n$ distinct numbers in the range $[0, n]$, return the only number missing from the array.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [3, 0, 1] \to$ Output: `2`.
  - *Test Case 2 (Boundary):* $nums = [0, 1] \to$ Output: `2`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Initialize $res = n$. For each index $i$ from $0$ to $n-1$, compute $res = res \oplus i \oplus nums[i]$. Matching indices and numbers eliminate each other; missing number remains.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Find the Duplicate Number (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Bit Position Population Count
- **Problem Statement & Constraints:** Given an array $nums$ containing $n + 1$ integers where each integer is between $1$ and $n$ inclusive. There is only one repeated number. Find it without modifying $nums$ and using $O(1)$ extra space.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1, 3, 4, 2, 2] \to$ Output: `2`.
  - *Test Case 2 (Boundary):* $nums = [3, 1, 3, 4, 2] \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Bit Approach ($O(32N)$ Time, $O(1)$ Space):** Iterate through each bit position $0 \le bit < 32$. Count the number of set bits among integers in $[1, n]$ vs the count of set bits among numbers in $nums$. If $count(nums) > count([1, n])$, the duplicate number must possess that set bit. Assemble duplicate bit-by-bit.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Decode XORed Permutation (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Global XOR Prefix Synthesis
- **Problem Statement & Constraints:** Given an encoded array $encoded$ of length $n - 1$ where $encoded[i] = perm[i] \oplus perm[i + 1]$ and $perm$ is a permutation of first $n$ positive integers ($n$ is always odd). Return the original array $perm$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $encoded = [6, 5, 4, 6] \to$ Output: `[2, 4, 1, 5, 3]`.
  - *Test Case 2 (Boundary):* $encoded = [3, 1] \to$ Output: `[1, 2, 3]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):**
    - Total XOR of $perm$ is $total = 1 \oplus 2 \oplus \dots \oplus n$.
    - Notice $encoded[1] \oplus encoded[3] \oplus \dots = perm[1] \oplus perm[2] \oplus perm[3] \oplus \dots \oplus perm[n-1]$.
    - XORing $total$ with all odd-indexed $encoded$ elements isolates $perm[0]$.
    - Once $perm[0]$ is known, sequentially construct $perm[i] = perm[i - 1] \oplus encoded[i - 1]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Subsets & Combinatorics via Bitmasks

---

### Problem #12: Subsets via Power Set Bitmask (Difficulty: Medium)
- **Pattern Category:** Pattern 3: $[0, 2^N - 1]$ State Enumeration
- **Problem Statement & Constraints:** Given an integer array $nums$ of unique elements ($1 \le nums.length \le 10$), return all possible subsets (the power set).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1, 2, 3] \to$ Output: `[[], [1], [2], [1,2], [3], [1,3], [2,3], [1,2,3]]`.
  - *Test Case 2 (Boundary):* $nums = [0] \to$ Output: `[[], [0]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Bitmask Approach ($O(N \cdot 2^N)$ Time, $O(N \cdot 2^N)$ Space):** Iterate integer $mask$ from $0$ to $(1 \ll n) - 1$. For each mask, examine bit $i$ from $0$ to $n-1$: if $((mask \gg i) \ \& \ 1) == 1$, add $nums[i]$ to the current subset.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #13: Subsets II (With Duplicates) (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Lexicographical Bitmask Deduplication
- **Problem Statement & Constraints:** Given an integer array $nums$ that may contain duplicates ($1 \le nums.length \le 10$), return all possible subsets without duplicates.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [1, 2, 2] \to$ Output: `[[], [1], [1,2], [1,2,2], [2], [2,2]]`.
  - *Test Case 2 (Boundary):* $nums = [0] \to$ Output: `[[], [0]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot 2^N)$ Time, $O(N \cdot 2^N)$ Space):**
    - Sort $nums$ first so duplicates are adjacent.
    - Iterate mask from $0$ to $(1 \ll n) - 1$.
    - When considering bit $i > 0$: if $nums[i] == nums[i-1]$, bit $i$ can only be included if bit $i-1$ is also included in $mask$. If $bit(i)$ is 1 but $bit(i-1)$ is 0, skip this mask to eliminate duplicate combinations.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Letter Case Permutation (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Case Toggle via XOR with $32$
- **Problem Statement & Constraints:** Given a string $s$, you can transform each letter individually to be lowercase or uppercase. Return a list of all possible strings. $1 \le s.length \le 12$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "a1b2" \to$ Output: `["a1b2", "a1B2", "A1b2", "A1B2"]`.
  - *Test Case 2 (Boundary):* $s = "3z4" \to$ Output: `["3z4", "3Z4"]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot 2^K)$ Time where $K$ is letter count):** Notice ASCII lowercase and uppercase differ by exactly bit 5 ($32$, or `1 << 5`). Toggle letter case with $char \oplus 32$. Enumerate $2^K$ mask combinations where the $j$-th letter is toggled if the $j$-th bit in mask is 1.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Maximum Length of a Concatenated String with Unique Characters (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Bitmask Disjointness Check
- **Problem Statement & Constraints:** Given an array of strings $arr$, return the maximum possible length of a string formed by concatenation of a subsequence of $arr$ that has all unique characters. $1 \le arr.length \le 16$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $arr = ["un", "iq", "ue"] \to$ Output: `4` (`"uniq"` or `"ique"`).
  - *Test Case 2 (Boundary):* $arr = ["cha", "r", "act", "ers"] \to$ Output: `6`.
  - *Test Case 3 (Internal Duplicate):* $arr = ["aa", "bb"] \to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(2^N)$ Time, $O(2^N)$ Space):**
    - Convert each string into a 26-bit mask representing character presence. Strings with internal duplicates are discarded immediately.
    - Maintain a list of reachable combined masks starting with `[0]`.
    - For each string mask $m$, check all existing valid masks $currMask$: if $(currMask \ \& \ m) == 0$, add $(currMask \ | \ m)$ to list and update max length with `Integer.bitCount(currMask | m)`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Range Bitwise Operations & Bit Partitioning

---

### Problem #16: Bitwise AND of Numbers Range (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Common Prefix Identification
- **Problem Statement & Constraints:** Given two integers $left$ and $right$, return the bitwise AND of all numbers in this range, inclusive: $left \ \& \ (left+1) \ \& \ \dots \ \& \ right$. $0 \le left \le right \le 2^{31} - 1$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $left = 5, right = 7 \to$ Output: `4` ($5 = 101_2, 6 = 110_2, 7 = 111_2 \to 100_2 = 4$).
  - *Test Case 2 (Boundary):* $left = 0, right = 0 \to$ Output: `0`.
  - *Test Case 3 (High Range Shift):* $left = 1, right = 2147483647 \to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N)$ Time):** Iteratively AND from $left$ to $right$ $\implies$ Time Limit Exceeded when range is $10^9$.
  - **2. Optimal Approach ($O(\log N)$ Time, $O(1)$ Space):** As soon as numbers change in range, the lower bits inevitably flip between 0 and 1, forcing them to become 0 in the bitwise AND. Thus, the answer is simply the common binary prefix of $left$ and $right$.
    - Shift both $left$ and $right$ rightward until $left == right$, counting shifts $shift$.
    - Result is $left \ll shift$ (or repeatedly clear lowest bit of $right$ via $right = right \ \& \ (right - 1)$ while $right > left$).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Maximum XOR of Two Numbers in an Array (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Bitwise Prefix Masking / Greedy Bit Evaluation
- **Problem Statement & Constraints:** Given an integer array $nums$ ($1 \le nums.length \le 2 \times 10^5$), return the maximum result of $nums[i] \oplus nums[j]$ where $0 \le i \le j < nums.length$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $nums = [3, 10, 5, 25, 2, 8] \to$ Output: `28` ($5 \oplus 25 = 28$).
  - *Test Case 2 (Boundary):* $nums = [14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70] \to$ Output: `127`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N^2)$ Time):** Check every pair $\implies$ TLE for $N = 2 \times 10^5$.
  - **2. Optimal Approach ($O(31N)$ Time, $O(N)$ Space):** Greedy bit-by-bit from bit 30 down to 0:
    - Assume candidate answer can have current bit set: `candidate = maxXor | (1 << bit)`.
    - Extract prefixes of all numbers using a prefix mask: `mask |= (1 << bit)`. Store prefixes in a `HashSet`.
    - Check if there exist two prefixes $p_1, p_2$ such that $p_1 \oplus p_2 == candidate$, which is equivalent to testing if `prefixes.contains(candidate ^ p)`. If true, set `maxXor = candidate`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Sum of Two Integers (Without '+' or '-') (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Half-Adder State Simulation
- **Problem Statement & Constraints:** Given two integers $a$ and $b$, return the sum of the two integers without using the operators `+` and `-`. $-1000 \le a, b \le 1000$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $a = 1, b = 2 \to$ Output: `3`.
  - *Test Case 2 (Negative Values):* $a = -2, b = 3 \to$ Output: `1`.
  - *Test Case 3 (Carry Propagation):* $a = -1, b = 1 \to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(32)$ Time, $O(1)$ Space):**
    - $a \oplus b$ gives the sum without carry (XOR simulates half adder sum).
    - $(a \ \& \ b) \ll 1$ gives the carry.
    - While carry $b \ne 0$, update $a = a \oplus b$ and $b = carry$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Minimum Flips to Make a OR b Equal to c (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Bitwise Truth Table Flips
- **Problem Statement & Constraints:** Given 3 positive integers $a$, $b$, and $c$. Return the minimum flips needed in some bits of $a$ and $b$ so that $(a \ | \ b) == c$. $1 \le a, b, c \le 10^9$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $a = 2, b = 6, c = 5 \to$ Output: `3`.
  - *Test Case 2 (Boundary):* $a = 4, b = 2, c = 7 \to$ Output: `1`.
  - *Test Case 3 (Identity):* $a = 1, b = 2, c = 3 \to$ Output: `0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(32)$ Time, $O(1)$ Space):**
    - Iterate through each bit $0 \le i < 30$:
      - Extract $bitA = (a \gg i) \ \& \ 1$, $bitB = (b \gg i) \ \& \ 1$, $bitC = (c \gg i) \ \& \ 1$.
      - If $bitC == 0$: both $bitA$ and $bitB$ must be flipped to 0 $\implies$ add $(bitA + bitB)$ flips.
      - If $bitC == 1$: at least one of $bitA$ or $bitB$ must be 1. If both are 0 $\implies$ add 1 flip.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Bitwise ORs of Subarrays (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Monotonic Bitwise Accumulation
- **Problem Statement & Constraints:** Given an integer array $arr$, return the number of distinct bitwise ORs of all non-empty subarrays of $arr$. $1 \le arr.length \le 5 \times 10^4$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $arr = [1, 1, 2] \to$ Output: `3` (distinct values: 1, 2, 3).
  - *Test Case 2 (Boundary):* $arr = [0] \to$ Output: `1`.
  - *Test Case 3 (Bit Propagation):* $arr = [1, 2, 4] \to$ Output: `6` (1, 2, 4, 3, 6, 7).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Key Observation ($O(30N)$ Time, $O(30N)$ Space):** A 32-bit integer has at most 32 bits that can be set. As a subarray grows rightward, its bitwise OR only monotonically gains bits (never loses bits). Thus, at any index $i$, there are at most 32 distinct subarray OR values ending at $i$.
  - **2. Optimal Approach:** Maintain a `Set` of current OR values `curr`. For each element $x$ in $arr$, form `next = {x} U {v | x for v in curr}`. Add all elements in `next` to a global result `Set`. Update `curr = next`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
