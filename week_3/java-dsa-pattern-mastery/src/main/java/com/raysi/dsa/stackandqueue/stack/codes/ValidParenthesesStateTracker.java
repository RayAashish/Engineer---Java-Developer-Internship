package com.raysi.dsa.stackandqueue.stack.codes;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ============================================================================
 * PATTERN: Parentheses Matching & State Tracking Stack
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.stack.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Matching brackets: A closing bracket must match the MOST RECENTLY opened
 *    unclosed bracket -> perfectly models LIFO (Last-In, First-Out).
 * 2. In modern Java, ALWAYS prefer `ArrayDeque<T>` over legacy `java.util.Stack`.
 *    Stack extends Vector, incurring unnecessary synchronization lock overhead
 *    on every push/pop. ArrayDeque is unsynchronized and backed by a circular array.
 * 3. Longest Valid Parentheses: Store INDICES on stack, seeding with -1 to serve
 *    as a base for valid length computation: (currIndex - stack.peek()).
 * ============================================================================
 */
public class ValidParenthesesStateTracker {

    /**
     * Determines if an input string of brackets '()[]{}' is valid.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public boolean isValid(String s) {
        if (s == null || s.length() % 2 != 0) return false;

        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else {
                // Must match expected closing bracket
                if (stack.isEmpty() || stack.pop() != c) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    /**
     * Minimum number of parentheses required to add to make string valid.
     * Time Complexity : O(N)
     * Space Complexity: O(1) auxiliary counter optimization
     */
    public int minAddToMakeValid(String s) {
        int openNeeded = 0;
        int closeNeeded = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                closeNeeded++;
            } else {
                if (closeNeeded > 0) {
                    closeNeeded--;
                } else {
                    openNeeded++;
                }
            }
        }

        return openNeeded + closeNeeded;
    }

    /**
     * Finds the length of the longest valid (well-formed) parentheses substring.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public int longestValidParentheses(String s) {
        if (s == null || s.length() < 2) return 0;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1); // Seed base index
        int maxLen = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    // Current closing bracket is invalid; becomes new base anchor
                    stack.push(i);
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }

        return maxLen;
    }

    /**
     * Simplifies canonical Unix-style file path.
     * Time Complexity : O(N)
     * Space Complexity: O(N)
     */
    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        String[] tokens = path.split("/");

        for (String token : tokens) {
            if (token.isEmpty() || token.equals(".")) {
                continue;
            }
            if (token.equals("..")) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else {
                stack.push(token);
            }
        }

        if (stack.isEmpty()) return "/";

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append("/").append(stack.pollLast()); // Read from bottom of stack
        }

        return sb.toString();
    }
}
