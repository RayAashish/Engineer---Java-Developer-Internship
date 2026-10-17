# Phase 0: Math & Number Theory — Self-Guided Problem Workbook (20 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.foundations.math`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **ZERO SOLUTION CODE PROVIDED.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Divisibility, GCD & LCM Properties

---

### Problem #1: Water and Jug Problem (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Bézout's Identity / GCD Solvability
- **Problem Statement & Constraints:** You are given two jugs with capacities $x$ and $y$ liters ($1 \le x, y \le 10^6$) and a target capacity $z$ ($1 \le z \le 10^6$). Determine whether it is possible to measure exactly $z$ liters using these two jugs.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $x = 3, y = 5, z = 4 \to$ Output: `true`.  
    *Explanation:* $4 = 2(5) - 2(3)$. Any linear combination of capacities can be measured.
  - *Test Case 2 (Tricky / Boundary):* $x = 2, y = 6, z = 5 \to$ Output: `false`.  
    *Explanation:* $\gcd(2, 6) = 2$. Any measured quantity must be a multiple of 2; 5 is odd, so impossible.
  - *Test Case 3 (Failure Trap):* $x = 1, y = 2, z = 5 \to$ Output: `false`.  
    *Explanation:* Even though $\gcd(1, 2) = 1$ divides 5, $z > x + y$, which exceeds total combined jug volume.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(x \times y)$ Time):** BFS graph search exploring all state pairs $(currX, currY)$ with transitions: fill, empty, pour.
  - **2. Optimal Approach ($O(\log(\min(x, y)))$ Time, $O(1)$ Space):** By Bézout's Identity, $z$ liters can be measured if and only if: (1) $z \le x + y$, and (2) $z$ is an exact multiple of $\gcd(x, y)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Simplified Fractions (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Coprime Generation
- **Problem Statement & Constraints:** Given an integer $n$ ($1 \le n \le 100$), return a list of all simplified fractions between 0 and 1 (exclusive) such that the denominator is $\le n$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 3 \to$ Output: `["1/2", "1/3", "2/3"]`.
  - *Test Case 2 (Boundary):* $n = 1 \to$ Output: `[]`.
  - *Test Case 3 (Failure Trap):* $n = 4 \to$ Output includes `"1/4"`, `"3/4"`, but excludes `"2/4"` since $\gcd(2, 4) = 2 \ne 1$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2 \log N)$ Time, $O(1)$ Extra Space):** Iterate denominator $d$ from 2 to $n$, and numerator $num$ from 1 to $d-1$. Include fraction `num + "/" + d` if and only if $\gcd(num, d) == 1$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Mirror Reflection (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Ray Bouncing Lattice via LCM
- **Problem Statement & Constraints:** A square room has corners labeled 0, 1, 2, 3. Laser ray from (0,0) hits east wall at height $q$ ($1 \le q \le p \le 1000$). Determine which corner detector the ray hits first.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $p = 2, q = 1 \to$ Output: `2`.
  - *Test Case 2 (Boundary):* $p = 3, q = 1 \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log(\min(p, q)))$ Time, $O(1)$ Space):** Unfold reflections onto a 2D grid. The ray reaches a corner when vertical distance is a multiple of $p$, and horizontal distance is a multiple of $q$. Divide both $p$ and $q$ by $\gcd(p, q)$. Parity of $p$ and $q$ uniquely determines corner: $p$ even, $q$ odd $\to 2$; $p$ odd, $q$ odd $\to 1$; $p$ odd, $q$ even $\to 0$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Ugly Number II (Difficulty: Medium)
- **Pattern Category:** Pattern 1: 3-Pointer Prime Multiple Merging
- **Problem Statement & Constraints:** An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5. Given $n$ ($1 \le n \le 1690$), return the $n$-th ugly number.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 10 \to$ Output: `12` (sequence: 1, 2, 3, 4, 5, 6, 8, 9, 10, 12).
  - *Test Case 2 (Boundary):* $n = 1 \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Better Approach ($O(N \log N)$ Time):** Min-heap tracking next candidates, using a HashSet to filter duplicates.
  - **2. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Dynamic array `ugly[n]`. Three pointers `p2 = 0, p3 = 0, p5 = 0`. At step $i$, next value is $\min(ugly[p2] \times 2, ugly[p3] \times 3, ugly[p5] \times 5)$. Advance whichever pointer(s) matched the chosen minimum.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Prime Numbers & Sieve of Eratosthenes

---

### Problem #5: Prime Arrangements (Difficulty: Easy-Medium)
- **Pattern Category:** Pattern 2: Sieve Counting + Factorial Modulo
- **Problem Statement & Constraints:** Return the number of permutations of 1 to $n$ ($1 \le n \le 100$) such that prime numbers are at prime indices (1-indexed). Return modulo $10^9 + 7$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 5 \to$ Output: `12`. Prime count $P=3$ (2, 3, 5), non-primes $C=2$ (1, 4). Ans = $3! \times 2! = 6 \times 2 = 12$.
  - *Test Case 2 (Boundary):* $n = 100 \to$ Output: `682289015`. Tests 64-bit modulo multiplication.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log \log N)$ Time, $O(N)$ Space):** Use Sieve to count prime numbers $P$ in range $[1, n]$. Number of composite/1 positions is $C = n - P$. The answer is $(P! \times C!) \pmod{10^9 + 7}$. Accumulate factorials using 64-bit `long` multiplications.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Closest Prime Numbers in Range (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Segmented Sieve / Range Prime Scan
- **Problem Statement & Constraints:** Given two positive integers `left` and `right` ($1 \le left \le right \le 10^6$), find two prime numbers $p_1 < p_2$ in range $[left, right]$ such that $p_2 - p_1$ is minimized.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `left = 10, right = 19` $\to$ Output: `[11, 13]`. Difference = 2.
  - *Test Case 2 (Boundary):* `left = 4, right = 6` $\to$ Output: `[-1, -1]`. Only 5 is prime.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(R \log \log R)$ Time, $O(R)$ Space):** Generate primes up to $right$ using Sieve. Collect primes in range $[left, right]$. Linear scan through collected primes tracking minimum adjacent difference. Early exit if difference of 2 is found (twin primes).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #7: Distinct Prime Factors of Product of Array (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Smallest Prime Factor (SPF) Frequency Set
- **Problem Statement & Constraints:** Given an array of positive integers `nums` ($1 \le N \le 10^4$, $2 \le \text{nums}[i] \le 1000$), return the number of distinct prime factors in the product of all elements.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `nums = [2, 4, 3, 7, 10, 6]` $\to$ Output: `4` (distinct primes: 2, 3, 5, 7).
  - *Test Case 2 (Boundary):* `nums = [2, 4, 8, 16]` $\to$ Output: `1` (only 2).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force (Fails immediately):** Multiplying elements causes instant 64-bit overflow.
  - **2. Optimal Approach ($O(N \sqrt{\max A})$ Time, $O(\text{Primes})$ Space):** Factorize each array element individually into a `HashSet<Integer>`. Return `set.size()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Four Divisors (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Divisor Pair Counting
- **Problem Statement & Constraints:** Given an integer array `nums`, return the sum of divisors of the integers that have exactly 4 divisors ($1 \le N \le 10^4$, $1 \le \text{nums}[i] \le 10^5$).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `nums = [21, 4, 7]` $\to$ Output: `32`. Divisors of 21: 1, 3, 7, 21 (sum 32). 4 has 3 divisors; 7 has 2 divisors.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \sqrt{M})$ Time):** For each number $x$, find divisors by iterating $d$ from 1 to $\sqrt{x}$. If $d$ divides $x$, add both $d$ and $x/d$ (ensuring $d \ne x/d$). If divisor count strictly equals 4, add divisor sum to global total.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Binary Exponentiation & Combinatorics

---

### Problem #9: Super Pow (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Modular Exponentiation with Array Exponents
- **Problem Statement & Constraints:** Calculate $a^b \pmod{1337}$, where $a$ is a positive integer and $b$ is an extremely large positive integer given as an array of digits ($1 \le b.length \le 2000$).
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $a = 2, b = [1, 0] \to$ Output: `1024`.
  - *Test Case 2 (Boundary):* $a = 2, b = [3] \to$ Output: `8`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(B \log(\text{base}))$ Time):** Use recursive property: $a^{[A_1, A_2, \dots, A_k]} = (a^{[A_1, \dots, A_{k-1}]})^{10} \times a^{A_k} \pmod{1337}$. Pop last digit, compute power 10 of remaining prefix, multiply with digit power, and take modulo 1337.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Count Good Numbers (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Fast Exponentiation with Modulo $10^9 + 7$
- **Problem Statement & Constraints:** A digit string is good if digits at even indices are even (0, 2, 4, 6, 8 $\to$ 5 choices) and digits at odd indices are prime (2, 3, 5, 7 $\to$ 4 choices). Given $n$ ($1 \le n \le 10^{15}$), return total good numbers modulo $10^9 + 7$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 1 \to$ Output: `5`.
  - *Test Case 2 (Standard):* $n = 4 \to$ Output: $5 \times 4 \times 5 \times 4 = 400$.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log N)$ Time, $O(1)$ Space):** Even indices count = $(n + 1) / 2$, odd indices count = $n / 2$. Total = $(5^{\text{evens}} \times 4^{\text{odds}}) \pmod{10^9 + 7}$. Use binary exponentiation `powerMod(base, exp, 1000000007)` to handle $n = 10^{15}$ without TLE.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Unique Paths (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Combinatorial Formula $\binom{N}{K}$ Without Overflow
- **Problem Statement & Constraints:** A robot on an $m \times n$ grid ($1 \le m, n \le 100$) can only move right or down. Find total unique paths from top-left to bottom-right.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $m = 3, n = 7 \to$ Output: `28`.
  - *Test Case 2 (Boundary):* $m = 3, n = 2 \to$ Output: `3`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Better Approach ($O(M \times N)$ Time, $O(N)$ Space):** 1D DP tabulation.
  - **2. Optimal Approach ($O(\min(M, N))$ Time, $O(1)$ Space):** Total moves = $(m - 1) + (n - 1)$. Total paths = $\binom{m + n - 2}{m - 1}$. Compute combinations iteratively multiplying $(N - i + 1)$ and dividing by $i$ to prevent overflow.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Reordered Power of 2 (Difficulty: Medium)
- **Pattern Category:** Pattern 3: Digit Signature Hashing
- **Problem Statement & Constraints:** Given an integer $n$, return `true` if we can reorder the digits such that the resulting number is a power of 2 ($1 \le n \le 10^9$). Leading zero not allowed.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $n = 1 \to$ Output: `true`.
  - *Test Case 2 (Boundary):* $n = 10 \to$ Output: `false`.
  - *Test Case 3 (Standard):* $n = 46 \to$ Output: `true` (can reorder to 64).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(30 \times \log N) \approx O(1)$ Time):** Powers of 2 up to $10^9$ are only 30 numbers ($2^0$ to $2^{29}$). Compute digit frequency count array of $n$. Check if it matches the digit frequency count of any of the 30 powers of 2.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Digit Manipulation & Base Conversions

---

### Problem #13: Reverse Integer (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Overflow-Guarded Digit Reversal
- **Problem Statement & Constraints:** Given a signed 32-bit integer $x$, return $x$ with its digits reversed. If reversing causes the value to go outside $[-2^{31}, 2^{31} - 1]$, return 0.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $x = 123 \to$ Output: `321`.
  - *Test Case 2 (Boundary):* $x = -123 \to$ Output: `-321`.
  - *Test Case 3 (Failure Trap):* $x = 1534236469 \to$ Output: `0` (reversal exceeds $2^{31}-1$).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log_{10} X)$ Time, $O(1)$ Space):** Pop digits via `pop = x % 10`, `x /= 10`. Before multiplying `res * 10`, verify overflow condition: `res > Integer.MAX_VALUE / 10` or underflow `res < Integer.MIN_VALUE / 10`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Excel Sheet Column Title (Difficulty: Easy)
- **Pattern Category:** Pattern 4: 1-Based Modulo Base-26 Conversion
- **Problem Statement & Constraints:** Given an integer `columnNumber` ($1 \le \text{col} \le 2^{31} - 1$), return its corresponding Excel column title (e.g., 1 $\to$ "A", 28 $\to$ "AB").
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `columnNumber = 28` $\to$ Output: `"AB"`.
  - *Test Case 2 (Boundary):* `columnNumber = 701` $\to$ Output: `"ZY"`.
  - *Test Case 3 (Failure Trap):* `columnNumber = 26` $\to$ Output: `"Z"` (not `"A0"`).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log_{26} N)$ Time, $O(1)$ Space):** Since alphabet is 1-indexed (A=1, Z=26), decrement `columnNumber--` at the start of each iteration to shift to 0-indexed arithmetic, append `(char)('A' + columnNumber % 26)`, then `columnNumber /= 26`. Reverse output string.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: String to Integer (atoi) (Difficulty: Medium)
- **Pattern Category:** Pattern 4: State-Machine String Parsing with Clamping
- **Problem Statement & Constraints:** Implement `myAtoi(string s)`: ignore leading whitespace, read optional sign ('+' or '-'), parse digits into 32-bit integer, clamp outside $[-2^{31}, 2^{31} - 1]$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `s = "   -42"` $\to$ Output: `-42`.
  - *Test Case 2 (Boundary):* `s = "4193 with words"` $\to$ Output: `4193`.
  - *Test Case 3 (Failure Trap):* `s = "-91283472332"` $\to$ Output: `-2147483648` (clamped to `Integer.MIN_VALUE`).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Skip whitespace. Read sign. Traverse digits building result, checking overflow condition `res > (Integer.MAX_VALUE - digit) / 10` before multiplication. Clamp accordingly.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: Add Digits (Digital Root) (Difficulty: Easy)
- **Pattern Category:** Pattern 4: Congruence Modulo 9
- **Problem Statement & Constraints:** Given an integer `num`, repeatedly add all its digits until the result has only one digit. Solve in $O(1)$ time without loops.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `num = 38 \to 3 + 8 = 11 \to 1 + 1 = 2 \to` Output: `2`.
  - *Test Case 2 (Boundary):* `num = 0 \to` Output: `0`.
  - *Test Case 3 (Failure Trap):* `num = 18 \to 1 + 8 = 9 \to` Output: `9` (not 0).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time, $O(1)$ Space):** Digital root formula: if `num == 0` return 0; else if `num % 9 == 0` return 9; else return `num % 9`. (Compact: `1 + (num - 1) % 9`).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 5: Geometric Coordinate & Line Sweep Math

---

### Problem #17: Max Points on a Line (Difficulty: Hard)
- **Pattern Category:** Pattern 5: Slope Hashing with Fraction Normalization
- **Problem Statement & Constraints:** Given an array of points where `points[i] = [xi, yi]` ($1 \le N \le 300$), return the maximum number of points that lie on the same straight line.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `points = [[1,1],[2,2],[3,3]]` $\to$ Output: `3`.
  - *Test Case 2 (Failure Trap):* `points = [[0,0],[1,1],[0,0]]` $\to$ Output: `3` (duplicate points).
  - *Test Case 3 (Failure Trap):* Floating-point precision error on vertical lines or near slopes.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(N)$ Space):** For each point $i$, calculate slope to all other points $j$. **Do not use `double` slopes due to precision loss!** Represent slope as irreducible fraction `dy / dx` by dividing both by `gcd(dy, dx)`. Store `"dy/dx"` in a HashMap. Track maximum count per origin.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Minimum Lines to Represent a Line Chart (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Cross-Multiplication Collinearity Check
- **Problem Statement & Constraints:** Given 2D array `stockPrices` ($1 \le N \le 10^5$), return minimum number of lines needed to represent the chart.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `stockPrices = [[1,7],[2,6],[3,5],[4,0]]` $\to$ Output: `2`. Points 1, 2, 3 share slope -1 (1 line); point 4 changes direction (+1 line).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \log N)$ Time, $O(1)$ Space):** Sort points by x-coordinate. Check if 3 consecutive points $(p_1, p_2, p_3)$ are collinear. Compare slopes using cross-multiplication with 64-bit `long` to avoid division and precision errors:
    $$\Delta y_1 \times \Delta x_2 == \Delta y_2 \times \Delta x_1$$
    Increment line count whenever slopes differ.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #19: Angle Between Hands of a Clock (Difficulty: Medium)
- **Pattern Category:** Pattern 5: Relative Angular Velocity
- **Problem Statement & Constraints:** Given integers `hour` and `minutes`, return the smaller angle (in degrees) formed between the hour and the minute hand.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `hour = 12, minutes = 30` $\to$ Output: `165.0`.
  - *Test Case 2 (Boundary):* `hour = 3, minutes = 30` $\to$ Output: `75.0`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(1)$ Time, $O(1)$ Space):** Minute hand moves $6^\circ$ per minute ($360^\circ / 60$). Hour hand moves $30^\circ$ per hour + $0.5^\circ$ per minute ($30^\circ / 60$). Angle diff = $|hourAngle - minuteAngle|$. Return $\min(diff, 360 - diff)$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Valid Perfect Square (Difficulty: Easy)
- **Pattern Category:** Pattern 5: Binary Search on Monotonic Squares
- **Problem Statement & Constraints:** Given a positive integer `num` ($1 \le num \le 2^{31} - 1$), return `true` if `num` is a perfect square, without using built-in functions like `Math.sqrt`.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* `num = 16` $\to$ Output: `true`.
  - *Test Case 2 (Boundary):* `num = 14` $\to$ Output: `false`.
  - *Test Case 3 (Failure Trap):* `num = 2147395600`. $mid \times mid$ overflows 32-bit integer!
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(\log N)$ Time, $O(1)$ Space):** Binary search range $[1, num]$. Compute $sq = (long) mid \times mid$. If $sq == num$, return `true`. If $sq < num$, `low = mid + 1`, else `high = mid - 1`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
