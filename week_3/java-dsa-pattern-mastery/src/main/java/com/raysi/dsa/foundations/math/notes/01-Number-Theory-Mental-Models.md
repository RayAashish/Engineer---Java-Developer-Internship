# Phase 0: Math & Number Theory — Patterns, Mental Models & Trigger Signals
> **Author:** raysi  
> **Package:** `com.raysi.dsa.foundations.math`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Prerequisites:** Basic Arithmetic, Modulo Arithmetic Properties  

---

# 1. Number Theory Mental Model & Memory Mechanics

### Numeric Representation & Overflow Limits in the JVM
Java uses signed two's complement representation:
- `int`: 32-bit signed integer ($-2^{31}$ to $2^{31} - 1 \approx -2.14 \times 10^9$ to $+2.14 \times 10^9$).
- `long`: 64-bit signed integer ($-2^{63}$ to $2^{63} - 1 \approx \pm 9.22 \times 10^{18}$).

```
Integer Overflow Mechanics:
Integer.MAX_VALUE = 2147483647
Integer.MAX_VALUE + 1 = -2147483648 (Silent Roll-over!)
```

### The "Constraint-to-Algorithm" Cheat Sheet for Math & Number Theory
| Input Magnitude ($N$) | Allowed Time Complexity | Expected Mathematical Pattern |
| :--- | :--- | :--- |
| **$N \le 10^6$** | $O(N \log \log N)$ or $O(N)$ | Sieve of Eratosthenes, Precomputed factorials / inverses. |
| **$N \le 10^9$** | $O(\sqrt{N})$ | Trial division primality testing, Finding all divisors in pairs. |
| **$N \le 10^{18}$** | $O(\log N)$ | Binary Exponentiation, Euclidean GCD, Matrix Exponentiation for linear recurrences. |
| **Any $N$ with Modulo $10^9 + 7$** | $O(1)$ per operation | Modular arithmetic laws (Add, Subtract with $+M$, Multiply with `long`, Fermat's Inverse). |

---

# 2. The Core Pattern Taxonomy (How to Spot the Pattern)

---

## Pattern 1: Euclidean Algorithm & LCM Relationships
### Trigger Keywords:
- *"Common factor"*, *"Simplest fraction"*, *"Repeated periodic alignment"*, *"Tiling a rectangle with largest square tiles"*

### Visual ASCII Dry Run: `gcd(252, 105)`
```
gcd(a, b) = gcd(b, a % b)
Step 1: gcd(252, 105) -> 252 % 105 = 42 -> gcd(105, 42)
Step 2: gcd(105, 42)  -> 105 % 42  = 21 -> gcd(42, 21)
Step 3: gcd(42, 21)   -> 42 % 21   = 0  -> gcd(21, 0)
Terminating: b == 0 -> Return a = 21!
```

### Reusable Java Template:
```java
package com.raysi.dsa.foundations.math;

public class EuclideanTemplate {
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return (a / gcd(a, b)) * b; // Divide first to prevent overflow!
    }
}
```

---

## Pattern 2: Sieve of Eratosthenes & Smallest Prime Factor (SPF)
### Trigger Keywords:
- *"Count primes up to $N$"*, *"Prime factorization of multiple queries"*, *"Sum of proper divisors"*

### Visual ASCII Dry Run: Sieve up to 16
```
Array: [F, F, T, T, T, T, T, T, T, T, T, T, T, T, T, T] (indices 0 to 15)
p=2: prime! Cross out 4, 6, 8, 10, 12, 14.
p=3: prime! Cross out 9, 12, 15 (start from 3*3=9).
p=4: already crossed out! Skip.
Terminates at p * p > 16.
Remaining True: 2, 3, 5, 7, 11, 13.
```

### Reusable Java Template:
```java
package com.raysi.dsa.foundations.math;

import java.util.Arrays;

public class SieveTemplate {
    public static boolean[] generatePrimes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        if (n >= 0) isPrime[0] = false;
        if (n >= 1) isPrime[1] = false;

        for (int p = 2; (long) p * p <= n; p++) {
            if (isPrime[p]) {
                for (int m = p * p; m <= n; m += p) {
                    isPrime[m] = false;
                }
            }
        }
        return isPrime;
    }
}
```

---

## Pattern 3: Binary Exponentiation & Modular Arithmetic
### Trigger Keywords:
- *"Compute $(X^N) \pmod M$"*, *"Answer may be large, return modulo $10^9 + 7$"*, *"Dividing modulo a prime"*

### Visual ASCII Dry Run: $3^{13} \pmod{100}$ ($13 = 1101_2$)
```
13 in binary: 8 + 4 + 1
res = 1, base = 3

bit 0 (1): res = (1 * 3) = 3;         base = (3 * 3) = 9
bit 1 (0): res = 3;                   base = (9 * 9) = 81
bit 2 (1): res = (3 * 81) % 100 = 43; base = (81 * 81) % 100 = 61
bit 3 (1): res = (43 * 61) % 100 = 23; base = (61 * 61) % 100
Total steps: 4 instead of 13 multiplications!
```

### Reusable Java Template:
```java
package com.raysi.dsa.foundations.math;

public class BinaryExponentiationTemplate {
    public static long powerMod(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
}
```

---

# 3. 5 Solved Anchor Problems (Brute $\to$ Better $\to$ Optimal)

---

### Anchor Problem 1: Count Primes Less Than $N$
**Problem:** Given an integer $n$, return the number of prime numbers that are strictly less than $n$.

#### 1. The Brute Force Instinct:
Iterate $i$ from 2 to $n-1$. For each $i$, run trial division checking if any integer from 2 to $\sqrt{i}$ divides $i$.
- **Bottleneck:** $O(N \sqrt{N})$ time. For $N = 5 \times 10^6$, this requires $\approx 10^{10}$ operations, causing TLE.

#### 2. The "Aha!" Observation:
Instead of testing each number independently, mark composite numbers in bulk. Every composite number $m$ has a prime factor $\le \sqrt{m}$. We only need to eliminate multiples starting at $p^2$.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.math;

import java.util.Arrays;

public class CountPrimes {
    public int countPrimes(int n) {
        if (n <= 2) return 0;

        boolean[] isPrime = new boolean[n];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        for (int p = 2; (long) p * p < n; p++) {
            if (isPrime[p]) {
                for (int m = p * p; m < n; m += p) {
                    isPrime[m] = false;
                }
            }
        }

        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) count++;
        }
        return count;
    }
}
```
- **Time Complexity:** $O(N \log(\log N))$
- **Space Complexity:** $O(N)$

---

### Anchor Problem 2: Pow(x, n)
**Problem:** Implement `pow(x, n)`, which calculates $x$ raised to the power $n$ ($x^n$).

#### 1. The Brute Force Instinct:
Multiply $x$ repeatedly $n$ times using a loop: `for (int i=0; i<n; i++) res *= x;`.
- **Bottleneck:** $O(N)$ operations. Fails when $n = 2^{31} - 1 \approx 2 \times 10^9$.

#### 2. The "Aha!" Observation:
Decompose $n$ in binary: $x^n = (x^{n/2})^2$ if $n$ is even, and $x \times (x^{n/2})^2$ if $n$ is odd. Halving $n$ at each step reduces time to logarithmic $O(\log N)$. Handle $n = -2^{31}$ by casting to `long` before negation.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.math;

public class PowerFunction {
    public double myPow(double x, int n) {
        long exp = n;
        if (exp < 0) {
            x = 1.0 / x;
            exp = -exp;
        }

        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result *= x;
            }
            x *= x;
            exp >>= 1;
        }
        return result;
    }
}
```
- **Time Complexity:** $O(\log |N|)$
- **Space Complexity:** $O(1)$

---

### Anchor Problem 3: Greatest Common Divisor of Strings
**Problem:** For two strings `s` and `t`, we say "`t` divides `s`" if and only if `s = t + t + ... + t`. Given `str1` and `str2`, return the largest string `x` that divides both.

#### 1. The Brute Force Instinct:
Try every prefix of `str1` from length $\min(|str1|, |str2|)$ down to 1, verifying whether repeating the prefix matches both strings.
- **Bottleneck:** $O(N^2)$ string concatenations and character comparisons.

#### 2. The "Aha!" Observation:
A common divisor string exists if and only if `str1 + str2 == str2 + str1`. If this condition holds, the length of the GCD string is simply `gcd(str1.length(), str2.length())`!

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.math;

public class StringGCD {
    public String gcdOfStrings(String str1, String str2) {
        if (!(str1 + str2).equals(str2 + str1)) {
            return "";
        }
        int gcdLength = gcd(str1.length(), str2.length());
        return str1.substring(0, gcdLength);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
```
- **Time Complexity:** $O(|str1| + |str2|)$
- **Space Complexity:** $O(|str1| + |str2|)$ for concatenation check.

---

### Anchor Problem 4: Factorial Trailing Zeroes
**Problem:** Given an integer $n$, return the number of trailing zeroes in $n!$.

#### 1. The Brute Force Instinct:
Compute $n!$ and count consecutive modulo 10 zeroes.
- **Bottleneck:** $n!$ overflows 64-bit integer at $n=21$.

#### 2. The "Aha!" Observation:
A trailing zero is produced by a factor of 10, which is $2 \times 5$. In any factorial, factors of 2 vastly outnumber factors of 5. Therefore, trailing zeroes equals the count of prime factor 5 in $n!$:
$$\text{Count} = \lfloor n/5 \rfloor + \lfloor n/25 \rfloor + \lfloor n/125 \rfloor + \dots$$

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.math;

public class TrailingZeroes {
    public int trailingZeroes(int n) {
        int count = 0;
        while (n >= 5) {
            count += n / 5;
            n /= 5;
        }
        return count;
    }
}
```
- **Time Complexity:** $O(\log_5 N)$
- **Space Complexity:** $O(1)$

---

### Anchor Problem 5: Palindrome Number (Without String Conversion)
**Problem:** Given an integer $x$, return `true` if $x$ is a palindrome, and `false` otherwise, without converting to string.

#### 1. The Brute Force Instinct:
Convert `x` to string via `String.valueOf(x)` and run two-pointer check.
- **Bottleneck:** Allocates heap memory for string characters.

#### 2. The "Aha!" Observation:
Negative numbers are never palindromes (due to `-`). For non-negative numbers, reverse only the **second half** of the digits by extracting `x % 10` and dividing `x /= 10` until the reversed half $\ge x$.

#### 3. Optimal Java Solution:
```java
package com.raysi.dsa.foundations.math;

public class PalindromeNumber {
    public boolean isPalindrome(int x) {
        // Negative numbers or numbers ending with 0 (except 0 itself) cannot be palindromes
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int reversedHalf = 0;
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + (x % 10);
            x /= 10;
        }

        // When length is odd, drop middle digit via reversedHalf / 10
        return x == reversedHalf || x == reversedHalf / 10;
    }
}
```
- **Time Complexity:** $O(\log_{10} N)$
- **Space Complexity:** $O(1)$

---

# 4. Common Edge Cases & Off-By-One Traps
1. **Negative Modulo in Java:**
   - In Java, `-7 % 3 = -1`, not `2`! Always normalize: `(x % M + M) % M`.
2. **Modulo Subtraction Underflow:**
   - When computing $(A - B) \pmod M$, if $A < B$, the result becomes negative. Always add $M$: `((A - B) % M + M) % M`.
3. **Multiplication Overflow Before Modulo:**
   - `(a * b) % M` will overflow if `a` and `b` are 32-bit integers around $10^9$. Cast to `long`: `((long) a * b) % M`.
4. **LCM Overflow:**
   - Computing `(a * b) / gcd(a, b)` can overflow 64-bit integer. Always divide first: `(a / gcd(a, b)) * b`.
