# Phase 3: Strings — Immutability, ASCII Hashing & Sliding Windows
> **Author:** raysi  
> **Package:** `com.raysi.dsa.strings`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Goal:** Build deep intuition for String memory layout in the JVM, fixed-array frequency hashing, palindrome expansions, and sliding window invariants.

---

## 1. String Mental Model & JVM Memory Mechanics

### 1.1 String Immutability & The Constant Pool
In Java, `java.lang.String` is **immutable** and `final`.
```
String s1 = "hello";           // Stored in PermGen / Metaspace String Constant Pool
String s2 = "hello";           // s1 == s2 is true (references identical pooled object)
String s3 = new String("hello");// Heap allocated, s1 == s3 is FALSE!
```

#### Under the Hood (Java 9+ Compact Strings):
Prior to Java 9, strings were stored as `char[]` (2 bytes per character). In modern Java:
```java
public final class String {
    private final byte[] value;  // Compacted storage
    private final byte coder;    // 0 = LATIN-1 (1 byte/char), 1 = UTF-16 (2 bytes/char)
}
```

#### The $O(N^2)$ String Concatenation Trap:
```java
// DISASTER: O(N^2) Time & Massive Heap Garbage
String res = "";
for (int i = 0; i < n; i++) {
    res += s.charAt(i); // Allocates a new byte[], copies all i characters!
}

// OPTIMAL: Amortized O(N) Time
StringBuilder sb = new StringBuilder(n);
for (int i = 0; i < n; i++) {
    sb.append(s.charAt(i)); // Doubling buffer array amortized O(1)
}
```

---

### 1.2 Fixed Array (`int[26]`) vs `HashMap<Character, Integer>`
For interview problems involving lowercase English letters (`'a'` through `'z'`), NEVER use a `HashMap` unless character keys are unbounded Unicode.

| Feature | `int[26]` Frequency Array | `HashMap<Character, Integer>` |
| :--- | :--- | :--- |
| **Memory Allocation** | Single contiguous 104-byte heap block | Multiple `Node` objects, pointers, boxed `Character`/`Integer` |
| **Cache Locality** | Entire array fits in a single 64-byte L1 cache line | Nodes scattered across heap $\implies$ frequent cache misses |
| **Lookup / Update Cost** | Direct index: `count[c - 'a']++` (1 CPU instruction) | `hash()` + table indexing + equals + auto-unboxing (~20-50 cycles) |
| **Equality Check** | `Arrays.equals(a, b)` $\implies$ exactly 26 comparisons | `mapA.equals(mapB)` $\implies$ hash set iterations |

---

### 1.3 Constraints to Big-O Table for Strings

| Input Length ($N$) | Permissible Big-O | Applicable Technique |
| :--- | :--- | :--- |
| $N \le 12$ | $O(N!)$ or $O(2^N)$ | Permutations, Subsets, Backtracking |
| $N \le 1000$ | $O(N^2)$ | Expand Around Center (Palindromes), 2D String DP (LCS, Edit Distance) |
| $N \le 10^5$ | $O(N)$ or $O(N \log N)$ | Sliding Window, Two Pointers, KMP, Frequency Array, Rolling Hash |
| $N \le 10^6$ | $O(N)$ | Single-pass scanning, Trie matching, Z-algorithm |

---

## 2. The 4 Core String Patterns

```
                                  +---------------------------+
                                  |    String Manipulation    |
                                  +---------------------------+
                                                |
          +---------------------+---------------+---------------------+
          |                     |               |                     |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
| Pattern 1: Fixed  | | Pattern 2: Expand | | Pattern 3: In-    | | Pattern 4: State  |
| Frequency Arrays  | | Around Center     | | Place Char Array  | | Sliding Window    |
+-------------------+ +-------------------+ +-------------------+ +-------------------+
  int[26] histogram     2N - 1 centers        cleanSpaces ->      Variable window:    
  Anagram equality      O(1) extra space      reverseAll ->       right expands,      
  Canonical signature   Palindromes           reverseWords        left contracts      
```

---

### Pattern 1: Fixed-Array Frequency Hashing & Canonical Signatures
- **Trigger Keywords:** `"valid anagram"`, `"group anagrams"`, `"permutation in string"`.
- **Mental Model:** Convert strings into a 26-element histogram. Two strings are anagrams if and only if their signatures are identical.
- **Canonical Key Format:** `#1#0#2#0...#0` (count of each character delimited by `#`).

---

### Pattern 2: Expand Around Center (Odd & Even Palindromes)
- **Trigger Keywords:** `"longest palindromic substring"`, `"palindromic substrings"`.
- **Mental Model:** Every palindrome mirrors outwards from its center. In length $N$, there are $2N - 1$ centers:
  - $N$ centers for odd length: $(i, i)$
  - $N - 1$ centers for even length: $(i, i + 1)$
- **ASCII Visual:**
```
Odd Center (i = 2):
     s = " r  a  [c]  e  c  a  r "
          <--   center   -->

Even Center (i = 2, i+1 = 3):
     s = " n  o  [o   n] "
          <--   center  -->
```

---

### Pattern 3: In-Place Char Array Two-Pointer Transformations
- **Trigger Keywords:** `"reverse words"`, `"reverse only letters"`, `"in-place string compression"`.
- **Mental Model:** Convert `s.toCharArray()`. Manipulate indices with `read` and `write` pointers without creating intermediate String objects.

---

### Pattern 4: Variable-Length Sliding Window with Character State Maps
- **Trigger Keywords:** `"longest substring without repeating"`, `"minimum window substring"`, `"at most K distinct"`.
- **Mental Model:**
  - `right` pointer expands the window to acquire valid state.
  - `left` pointer contracts the window to restore validity or minimize window size.
  - State maintained via an `int[128]` frequency array and a `matchCounter`.

---

## 3. Five Solved Anchor Problems

---

### Anchor Problem #1: Longest Substring Without Repeating Characters (LeetCode 3)
> **Problem:** Given a string $s$, find the length of the longest substring without repeating characters.

#### 1. Brute Force Instinct
Check all $O(N^2)$ substrings. Use a `HashSet` to verify uniqueness $\implies O(N^3)$ or $O(N^2)$ time.

#### 2. "Aha!" Observation
Use a sliding window `[left, right]`. Instead of incrementing `left` by 1 when a duplicate character is seen, maintain the **last seen index** of each character in an `int[128]` array. If character `c` was previously seen at `prevIndex >= left`, jump `left` directly to `prevIndex + 1`.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.strings;

import java.util.Arrays;

public class LongestSubstringWithoutRepeatingAnchor {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) return 0;

        int[] lastIndex = new int[128];
        Arrays.fill(lastIndex, -1);

        int maxLen = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // If character was seen inside current window, jump left past it
            if (lastIndex[c] >= left) {
                left = lastIndex[c] + 1;
            }

            lastIndex[c] = right;
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$ — `right` visits each character once; `left` only moves forward.
- **Space Complexity:** $O(1)$ — Fixed 128-element array for ASCII.

---

### Anchor Problem #2: Minimum Window Substring (LeetCode 76)
> **Problem:** Given two strings $s$ and $t$, return the minimum window substring of $s$ such that every character in $t$ (including duplicates) is included in the window.

#### 1. Brute Force Instinct
Check all $O(N^2)$ substrings of $s$, counting character frequencies to see if they satisfy $t$ $\implies O(N^2 \cdot |\Sigma|)$.

#### 2. "Aha!" Observation
Maintain frequency of $t$ in `need[128]`. Maintain a counter `required = uniqueCharsInT`.
- Expand `right`: decrement `need[s[right]]`. If count reaches 0, decrement `required`.
- Once `required == 0`, window is valid: record minimum length, then shrink `left` by restoring `need[s[left]]++`. If count becomes $> 0$, increment `required` and stop shrinking.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.strings;

public class MinimumWindowSubstringAnchor {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        int[] need = new int[128];
        int uniqueCount = 0;
        for (char c : t.toCharArray()) {
            if (need[c] == 0) uniqueCount++;
            need[c]++;
        }

        int left = 0, minLen = Integer.MAX_VALUE, startIdx = 0;
        int matched = 0;

        for (int right = 0; right < s.length(); right++) {
            char rChar = s.charAt(right);
            need[rChar]--;
            if (need[rChar] == 0) {
                matched++;
            }

            // When all characters satisfied, shrink from left
            while (matched == uniqueCount) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    startIdx = left;
                }

                char lChar = s.charAt(left);
                need[lChar]++;
                if (need[lChar] > 0) {
                    matched--;
                }
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(startIdx, startIdx + minLen);
    }
}
```
*Complexity:*
- **Time Complexity:** $O(|S| + |T|)$ — Each character in $s$ processed at most twice.
- **Space Complexity:** $O(1)$ auxiliary (fixed 128 array).

---

### Anchor Problem #3: Group Anagrams (LeetCode 49)
> **Problem:** Given an array of strings `strs`, group the anagrams together.

#### 1. Brute Force Instinct
Sort each string alphabetically: `char[] ca = s.toCharArray(); Arrays.sort(ca);`. Use sorted string as HashMap key $\implies O(N \cdot K \log K)$.

#### 2. "Aha!" Observation
Avoid the $O(K \log K)$ sorting cost! Use character counting in $O(K)$. Build a canonical hash string key: `#count_a#count_b...#count_z`. Two strings have the exact same key if and only if they are anagrams.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.strings;

import java.util.*;

public class GroupAnagramsAnchor {
    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) return new ArrayList<>();

        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            int[] count = new int[26];
            for (int i = 0; i < s.length(); i++) {
                count[s.charAt(i) - 'a']++;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                sb.append('#').append(count[i]);
            }
            String key = sb.toString();

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N \cdot K)$ where $N$ is number of strings, $K$ is maximum string length.
- **Space Complexity:** $O(N \cdot K)$ to store keys and grouped strings.

---

### Anchor Problem #4: Longest Palindromic Substring (LeetCode 5)
> **Problem:** Given a string $s$, return the longest palindromic substring in $s$.

#### 1. Brute Force Instinct
Enumerate all $O(N^2)$ substrings and check each for palindrome property in $O(N) \implies O(N^3)$ time. 2D DP uses $O(N^2)$ space.

#### 2. "Aha!" Observation
Expand Around Center uses $O(1)$ auxiliary space and $O(N^2)$ time. For every index $i$, expand for odd length centered at $(i, i)$ and even length centered at $(i, i+1)$. Keep track of maximum span.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.strings;

public class LongestPalindromicSubstringAnchor {
    public String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";

        int start = 0, end = 0;

        for (int i = 0; i < s.length(); i++) {
            int lenOdd = expand(s, i, i);
            int lenEven = expand(s, i, i + 1);
            int maxLen = Math.max(lenOdd, lenEven);

            if (maxLen > end - start + 1) {
                start = i - (maxLen - 1) / 2;
                end = i + maxLen / 2;
            }
        }

        return s.substring(start, end + 1);
    }

    private int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N^2)$
- **Space Complexity:** $O(1)$ auxiliary memory.

---

### Anchor Problem #5: Reverse Words in a String (LeetCode 151)
> **Problem:** Given an input string $s$, reverse the order of the words. Return a string with a single space separating the words and no leading/trailing spaces.

#### 1. Brute Force Instinct
`s.trim().split("\\s+")`, reverse array, and `String.join(" ", words)`.
*Cost:* Multiple regex passes, heavy object allocation, $O(N)$ space.

#### 2. "Aha!" Observation
Three-step in-place transformation on `char[]`:
1. Clean spaces using two pointers (`read`, `write`).
2. Reverse entire cleaned character array.
3. Reverse each individual word delimited by space.

#### 3. Optimal Java Implementation
```java
package com.raysi.dsa.strings;

public class ReverseWordsAnchor {
    public String reverseWords(String s) {
        char[] a = s.toCharArray();
        int n = a.length;

        // Step 1: Clean spaces
        int len = cleanSpaces(a, n);

        // Step 2: Reverse whole string
        reverse(a, 0, len - 1);

        // Step 3: Reverse each word
        int start = 0;
        for (int end = 0; end < len; end++) {
            if (a[end] == ' ') {
                reverse(a, start, end - 1);
                start = end + 1;
            }
        }
        reverse(a, start, len - 1);

        return new String(a, 0, len);
    }

    private int cleanSpaces(char[] a, int n) {
        int read = 0, write = 0;
        while (read < n) {
            while (read < n && a[read] == ' ') read++;
            while (read < n && a[read] != ' ') a[write++] = a[read++];
            while (read < n && a[read] == ' ') read++;
            if (read < n) a[write++] = ' ';
        }
        return write;
    }

    private void reverse(char[] a, int l, int r) {
        while (l < r) {
            char tmp = a[l];
            a[l++] = a[r];
            a[r--] = tmp;
        }
    }
}
```
*Complexity:*
- **Time Complexity:** $O(N)$
- **Space Complexity:** $O(N)$ for primitive `char[]`.

---

## 4. Common Edge Cases & Off-By-One Traps

1. **Reference Equality `==` vs `.equals()`:**
   - In Java, `str1 == str2` checks reference identity. Never use `==` for String content comparison. Always use `.equals()`.
2. **Character Offset Normalization:**
   - Writing `s.charAt(i) - 'A'` when input contains lowercase letters creates negative index $\to$ `ArrayIndexOutOfBoundsException`. Verify character case assumptions.
3. **Palindrome Expansion Length Formula:**
   - When loop terminates, `left` and `right` have exceeded palindrome boundaries by 1 on each side.
   - Valid span is from `left + 1` to `right - 1`.
   - Length is $(right - 1) - (left + 1) + 1 = right - left - 1$.
4. **Window Shrinking Condition in Sliding Window:**
   - Forgetting to update `left` character frequency before incrementing `left++` causes stale window state.
