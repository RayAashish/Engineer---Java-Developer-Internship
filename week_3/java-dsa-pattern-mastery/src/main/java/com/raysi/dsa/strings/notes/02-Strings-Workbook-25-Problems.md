# Phase 3: Strings — Self-Guided Problem Workbook (25 Problems)
> **Author:** raysi  
> **Package:** `com.raysi.dsa.strings`  
> **Target Audience:** SDE 1 / SDE 2 Candidates Interviewing at Product-Based Companies  
> **Rule of Engagement:** **STRICTLY ZERO SOLUTION CODE.** Read the logic roadmap, write every line of Java code yourself, and test against the strategic test cases.

---

## Pattern 1: Frequency Arrays & Anagram Invariants

---

### Problem #1: Valid Anagram (Difficulty: Easy)
- **Pattern Category:** Pattern 1: Single-Pass Fixed Array Histogram
- **Problem Statement & Constraints:** Given two strings $s$ and $t$, return `true` if $t$ is an anagram of $s$, and `false` otherwise. $1 \le s.length, t.length \le 5 \times 10^4$. Lowercase English letters.
- **2-3 Strategic Test Cases (With Dry-Run Explanation):**
  - *Test Case 1 (Standard):* $s = "anagram", t = "nagaram" \to$ Output: `true`.
  - *Test Case 2 (Length Mismatch):* $s = "rat", t = "car" \to$ Output: `false`.
  - *Test Case 3 (Count Mismatch):* $s = "aacc", t = "ccca" \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Brute Force Approach ($O(N \log N)$ Time):** Sort both character arrays and compare equality.
  - **2. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Check lengths first. Allocate `int[26]`. In a single loop, increment for $s$ characters and decrement for $t$ characters. Finally verify all 26 values are exactly 0.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #2: Group Anagrams (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Canonical Frequency Key Mapping
- **Problem Statement & Constraints:** Given an array of strings $strs$, group the anagrams together in any order. $1 \le strs.length \le 10^4, 0 \le strs[i].length \le 100$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $strs = ["eat","tea","tan","ate","nat","bat"] \to$ Grouped into 3 clusters.
  - *Test Case 2 (Empty String):* $strs = [""] \to$ Output: `[[""]]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N \cdot K)$ Time, $O(N \cdot K)$ Space):** For each string of length $K$, build a 26-element frequency count. Construct a string key `#c0#c1...#c25` and store in `Map<String, List<String>>`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #3: Find All Anagrams in a String (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Fixed-Length Sliding Window Histogram
- **Problem Statement & Constraints:** Given two strings $s$ and $p$, return an array of all start indices of $p$'s anagrams in $s$. $1 \le s.length, p.length \le 3 \times 10^4$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "cbaebabacd", p = "abc" \to$ Output: `[0, 6]`.
  - *Test Case 2 (Overlapping):* $s = "abab", p = "ab" \to$ Output: `[0, 1, 2]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(|S|)$ Time, $O(1)$ Space):** Build frequency array for $p$. Slide a window of fixed size $|p|$ across $s$: increment incoming character count, decrement outgoing character count. Compare 26 entries (or maintain a `matchCount` tracker). Record index `right - |p| + 1` whenever histograms match.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #4: Permutation in String (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Fixed-Length Sliding Window Matching
- **Problem Statement & Constraints:** Given two strings $s1$ and $s2$, return `true` if $s2$ contains a permutation of $s1$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s1 = "ab", s2 = "eidbaooo" \to$ Output: `true` (contains "ba").
  - *Test Case 2 (False Trap):* $s1 = "ab", s2 = "eidboaoo" \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(|S2|)$ Time, $O(1)$ Space):** Maintain frequency counts of $s1$ and initial prefix of $s2$ of length $|s1|$. Track count of matched character positions among the 26 letters. Slide window: adjust `matches` count on incoming and outgoing letters. If `matches == 26`, return true.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #5: Custom Sort String (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Bucket Counting by Ordered Precedence
- **Problem Statement & Constraints:** You are given two strings $order$ and $s$. Permute characters of $s$ so they match the order in $order$. Characters not in $order$ may appear in any order at the end.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $order = "cba", s = "abcd" \to$ Output: `"cbad"`.
  - *Test Case 2 (Repeated Characters):* $order = "cbafg", s = "abcdab" \to$ `"cbaabd"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(|Order| + |S|)$ Time, $O(1)$ Space):** Count frequency of characters in $s$ using `int[26]`. Iterate through $order$: for each character, append it to `StringBuilder` as many times as its count indicates, then set count to 0. Finally append all remaining characters with non-zero counts.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #6: Minimum Deletions to Make Character Frequencies Unique (Difficulty: Medium)
- **Pattern Category:** Pattern 1: Greedy Frequency Decrementing
- **Problem Statement & Constraints:** Given a string $s$, return the minimum number of characters you need to delete to make the frequency of each character unique.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "aab" \to$ Output: `0` (freqs: 2, 1).
  - *Test Case 2 (Collision):* $s = "aaabbbcc" \to$ Output: `2` (freqs 3, 3, 2 $\to$ 3, 2, 1 or 3, 1, 0).
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Extra Space):** Compute `int[26]` frequency. Maintain a `HashSet` of used frequencies. For each character's frequency $> 0$: while frequency $> 0$ and already present in set, decrement frequency and increment deletion counter. Add final frequency to set if $> 0$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 2: Expand Around Center & Palindromes

---

### Problem #7: Longest Palindromic Substring (Difficulty: Medium)
- **Pattern Category:** Pattern 2: $2N - 1$ Center Expansion
- **Problem Statement & Constraints:** Given string $s$, return the longest palindromic substring. $1 \le s.length \le 1000$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Odd Length):* $s = "babad" \to$ Output: `"bab"` (or `"aba"`).
  - *Test Case 2 (Even Length):* $s = "cbbd" \to$ Output: `"bb"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(1)$ Space):** Iterate $i$ from $0$ to $n-1$. Expand from $(i, i)$ and $(i, i+1)$ while characters match and bounds are valid. Track maximum span indices $[start, end]$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #8: Palindromic Substrings (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Center Expansion Counting
- **Problem Statement & Constraints:** Given a string $s$, return the number of palindromic substrings in it.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "abc" \to$ Output: `3` ("a", "b", "c").
  - *Test Case 2 (Repeated):* $s = "aaa" \to$ Output: `6` ("a", "a", "a", "aa", "aa", "aaa").
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N^2)$ Time, $O(1)$ Space):** For each index $i$, expand from $(i, i)$ and $(i, i+1)$. Each valid step of expansion corresponds to exactly one unique palindromic substring $\implies$ increment total count.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #9: Valid Palindrome (Difficulty: Easy)
- **Pattern Category:** Pattern 2: Two-Pointer Boundary Convergence
- **Problem Statement & Constraints:** Given string $s$, determine if it is a palindrome considering only alphanumeric characters and ignoring cases.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "A man, a plan, a canal: Panama" \to$ Output: `true`.
  - *Test Case 2 (False Trap):* $s = "race a car" \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Use two pointers `left = 0, right = n - 1`. While `left < right`: skip non-alphanumeric characters with `Character.isLetterOrDigit()`. Compare `Character.toLowerCase(s[left]) == Character.toLowerCase(s[right])`. If mismatch, return false; else `left++, right--`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #10: Valid Palindrome II (Difficulty: Easy)
- **Pattern Category:** Pattern 2: Greedy Branch on First Mismatch
- **Problem Statement & Constraints:** Given a string $s$, return `true` if $s$ can be palindrome after deleting at most one character from it.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "abca" \to$ Output: `true` (delete 'b' or 'c').
  - *Test Case 2 (Boundary):* $s = "abc" \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Two pointers from ends. On first mismatch `s[l] != s[r]`, test two independent sub-ranges: is `s[l + 1 ... r]` a palindrome OR is `s[l ... r - 1]` a palindrome? If either is true, answer is true.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #11: Shortest Palindrome (Difficulty: Hard)
- **Pattern Category:** Pattern 2: Prefix Palindrome Matching via KMP / Rolling Hash
- **Problem Statement & Constraints:** Given string $s$, convert it to a palindrome by adding characters in front of it. Return the shortest palindrome you can find by performing this transformation.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "aacecaaa" \to$ Output: `"aaacecaaa"`.
  - *Test Case 2 (Reversed):* $s = "abcd" \to$ Output: `"dcbabcd"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Find the longest palindromic prefix of $s$. Construct string $T = s + "#" + reverse(s)$. Compute the KMP prefix function (LPS array) for $T$. The last value `lps[T.length - 1]` gives the length of the longest palindromic prefix. Take the remaining suffix of $s$, reverse it, and prepend to $s$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #12: Longest Palindrome by Concatenating Two Letter Words (Difficulty: Medium)
- **Pattern Category:** Pattern 2: Hash Frequency Pairing
- **Problem Statement & Constraints:** Given an array of strings $words$ where each word consists of 2 lowercase letters, return the maximum length of a palindrome you can create by concatenating elements.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $words = ["lc","cl","gg"] \to$ Output: `6` ("lc" + "gg" + "cl").
  - *Test Case 2 (Center Palindrome Word):* $words = ["ab","ty","yt","lc","cl","ab"] \to$ Output: `8`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Use a $26 \times 26$ 2D frequency array. For each 2-letter word $(u, v)$: if reverse $(v, u)$ has positive count, pair them ($+4$ length) and decrement reverse count. Else increment $(u, v)$ count. Finally, if any symmetric word $(u, u)$ has count $> 0$, place one in the very center ($+2$ length).
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 3: Two-Pointer & In-Place Transformations

---

### Problem #13: Reverse Words in a String (Difficulty: Medium)
- **Pattern Category:** Pattern 3: In-Place Char Array 3-Step Reversal
- **Problem Statement & Constraints:** Reverse words in $s$, stripping multiple spaces and leading/trailing spaces.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "the sky is blue" \to$ Output: `"blue is sky the"`.
  - *Test Case 2 (Multiple Spaces):* $s = "  hello world  " \to$ Output: `"world hello"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space for char array):** (1) Clean redundant spaces using two pointers `read` and `write`. (2) Reverse entire array $[0, len - 1]$. (3) Reverse each individual word bounded by space.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #14: Reverse Only Letters (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Non-Letter Skipping Two-Pointer
- **Problem Statement & Constraints:** Given a string $s$, reverse the string according to rules: all characters that are not letters remain in the same position; all letters reverse positions.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "ab-cd" \to$ Output: `"dc-ba"`.
  - *Test Case 2 (Boundary):* $s = "a-bC-dEf-ghIj" \to$ Output: `"j-Ih-gfE-dCba"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Convert to `char[]`. Two pointers: advance `left` if `!isLetter(left)`, decrement `right` if `!isLetter(right)`. When both are letters, swap and increment/decrement.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #15: Reverse Vowels of a String (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Vowel Set Two-Pointer Convergence
- **Problem Statement & Constraints:** Given string $s$, reverse only all the vowels in the string.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "IceCreAm" \to$ Output: `"AceCreIm"`.
  - *Test Case 2 (No Vowels):* $s = "xyz" \to$ Output: `"xyz"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(N)$ Space):** Convert to `char[]`. Use lookup boolean array `isVowel[128]` for both uppercase and lowercase vowels. Move `left` until vowel, move `right` until vowel, swap and advance.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #16: String Compression (Difficulty: Medium)
- **Pattern Category:** Pattern 3: In-Place Write Pointer Run-Length Encoding
- **Problem Statement & Constraints:** Given array of characters $chars$, compress it using run-length encoding. Return new length. Modify input in-place with $O(1)$ extra memory.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $chars = ["a","a","b","b","c","c","c"] \to$ Output: `6`, modified to `["a","2","b","2","c","3"]`.
  - *Test Case 2 (Single Run):* $chars = ["a"] \to$ Output: `1`, modified to `["a"]` (no digit appended for length 1).
  - *Test Case 3 (Multi-Digit Count):* 12 'b's $\to$ modified to `['b', '1', '2']`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** `read = 0, write = 0`. While `read < n`: record current char, count consecutive occurrences with inner while-loop. Write char to `chars[write++]`. If count $> 1$, convert count to string and write each digit character sequentially.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #17: Is Subsequence (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Greedy Two-Pointer Scanning
- **Problem Statement & Constraints:** Given two strings $s$ and $t$, return `true` if $s$ is a subsequence of $t$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "abc", t = "ahbgdc" \to$ Output: `true`.
  - *Test Case 2 (False Trap):* $s = "axc", t = "ahbgdc" \to$ Output: `false`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(|T|)$ Time, $O(1)$ Space):** `i = 0, j = 0`. Iterate $j$ through $t$: if $s[i] == t[j]$, advance $i++$. Return `i == s.length()`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #18: Longest Common Prefix (Difficulty: Easy)
- **Pattern Category:** Pattern 3: Horizontal Scan / Vertical Reduction
- **Problem Statement & Constraints:** Write a function to find the longest common prefix string amongst an array of strings. Return `""` if none.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $strs = ["flower","flow","flight"] \to$ Output: `"fl"`.
  - *Test Case 2 (Disjoint):* $strs = ["dog","racecar","car"] \to$ Output: `""`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(S)$ Time where $S$ is sum of all characters):** Set `prefix = strs[0]`. Iterate each subsequent string $s$: while `s.indexOf(prefix) != 0`, truncate `prefix = prefix.substring(0, prefix.length() - 1)`. If prefix becomes empty, return `""`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

## Pattern 4: Sliding Windows & Polynomial Hashing

---

### Problem #19: Longest Substring Without Repeating Characters (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Last-Seen Index Jumping Window
- **Problem Statement & Constraints:** Find length of longest substring without repeating characters.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "abcabcbb" \to$ Output: `3`.
  - *Test Case 2 (All Identical):* $s = "bbbbb" \to$ Output: `1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Use `lastIdx[128]` initialized to -1. As `right` iterates from $0$ to $n-1$: if `lastIdx[s[right]] >= left`, update `left = lastIdx[s[right]] + 1`. Record `lastIdx[s[right]] = right` and maximize `right - left + 1`.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #20: Minimum Window Substring (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Target Character Satisfaction Window
- **Problem Statement & Constraints:** Given strings $s$ and $t$, find the minimum window in $s$ containing all characters of $t$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "ADOBECODEBANC", t = "ABC" \to$ Output: `"BANC"`.
  - *Test Case 2 (Impossible):* $s = "a", t = "aa" \to$ Output: `""`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(|S| + |T|)$ Time, $O(1)$ Space):** Build `need[128]` for $t$. Track `matchedUnique` against total unique chars in $t$. Expand `right`: decrement `need[s[right]]`; when count hits 0, increment `matchedUnique`. When all unique chars are satisfied, shrink `left` while updating minimum window until a required char count becomes $> 0$.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #21: Longest Repeating Character Replacement (Difficulty: Medium)
- **Pattern Category:** Pattern 4: Max-Frequency Invariant Window
- **Problem Statement & Constraints:** Given string $s$ and integer $k$, you can choose any character of string and change it to any other uppercase English letter at most $k$ times. Return the length of the longest substring containing the same letter.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "ABAB", k = 2 \to$ Output: `4`.
  - *Test Case 2 (Boundary):* $s = "AABABBA", k = 1 \to$ Output: `4`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(N)$ Time, $O(1)$ Space):** Window length is `(right - left + 1)`. Window is valid if `(windowLen - maxFreqCharInWindow) <= k`. Maintain `maxFreq` seen so far. If current window requires $> k$ replacements, shrink `left++` and decrement its count.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #22: Substring with Concatenation of All Words (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Word-Chunked Sliding Window
- **Problem Statement & Constraints:** Given string $s$ and array of strings $words$ where each word has identical length $L$. Find all starting indices of substring(s) in $s$ that is a concatenation of each word in $words$ exactly once.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "barfoothefoobarman", words = ["foo","bar"] \to$ Output: `[0, 9]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Approach ($O(L \cdot N)$ Time, $O(M)$ Space where $M$ is words count):** Run $L$ separate sliding windows starting at offsets $offset \in [0, L - 1]$. Advance in increments of $L$. Track word frequencies in the current window using a hash map. Shrink from left if a word is seen too many times.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #23: Find the Index of the First Occurrence in a String (Difficulty: Easy / Medium)
- **Pattern Category:** Pattern 4: Rabin-Karp Rolling Hash / KMP
- **Problem Statement & Constraints:** Given two strings $haystack$ and $needle$, return the index of the first occurrence of $needle$ in $haystack$, or -1 if $needle$ is not part of $haystack$.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $haystack = "sadbutsad", needle = "sad" \to$ Output: `0`.
  - *Test Case 2 (Not Found):* $haystack = "leetcode", needle = "leeto" \to$ Output: `-1`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. KMP Approach ($O(N + M)$ Time, $O(M)$ Space):** Precompute LPS (longest proper prefix which is also suffix) array of $needle$. Match against $haystack$; on mismatch, fall back using LPS table without backtracking haystack pointer.
  - **2. Rabin-Karp Rolling Hash ($O(N)$ Average Time, $O(1)$ Space):** Maintain rolling polynomial hash $H = (H \cdot B - s[out] \cdot B^M + s[in]) \pmod M$. Compare string slice only on hash collisions.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #24: Repeated DNA Sequences (Difficulty: Medium)
- **Pattern Category:** Pattern 4: 2-Bit Rolling Bitmask Hash
- **Problem Statement & Constraints:** Given a string $s$ representing a DNA sequence composed of 'A', 'C', 'G', 'T', return all 10-letter-long sequences (substrings) that occur more than once.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT" \to$ Output: `["AAAAACCCCC","CCCCCAAAAA"]`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Bitmask Hash ($O(N)$ Time, $O(N)$ Space):** Encode 4 nucleotides into 2 bits: A=0, C=1, G=2, T=3. A 10-char string fits into a 20-bit integer ($2 \times 10$). Slide a 20-bit mask: `hash = ((hash << 2) | code) & 0xFFFFF`. Use a `HashSet<Integer>` to detect repeated integer hashes and add matching substrings to result.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`

---

### Problem #25: Minimum Window Subsequence (Difficulty: Hard)
- **Pattern Category:** Pattern 4: Two-Pointer Forward Search & Backward Shrink
- **Problem Statement & Constraints:** Given strings $s1$ and $s2$, find the minimum contiguous substring $W$ of $s1$, so that $s2$ is a subsequence of $W$. If multiple, return the one with the smallest starting index.
- **2-3 Strategic Test Cases:**
  - *Test Case 1 (Standard):* $s1 = "abcdebdde", s2 = "bde" \to$ Output: `"bcde"`.
- **Complexity & Logic Roadmap (NO CODE!):**
  - **1. Optimal Two-Pointer Scanning ($O(|S1| \cdot |S2|)$ Time, $O(1)$ Space):**
    - Forward Scan: Advance pointer in $s1$ matching characters of $s2$ left-to-right until $s2$ is completely matched at index $right$.
    - Backward Shrink: Now trace backwards from $right$ through $s1$ matching characters of $s2$ right-to-left until reaching index $left$. This guarantees the shortest start for this instance of $s2$.
    - Update minimum window, then reset $s1$ pointer to $left + 1$ and repeat forward search.
- **Self-Check Status:** `[ ] Solved Brute  [ ] Solved Optimal  [ ] Revised Without Hints`
