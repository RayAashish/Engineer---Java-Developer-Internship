package com.raysi.dsa.foundations.math.codes;

/**
 * Foundation Math: Euclidean GCD and LCM
 * Author: raysi
 * Package: com.raysi.dsa.foundations.math.codes
 */
public class EuclideanGCDAndLCM {

    /**
     * Euclidean Algorithm for Greatest Common Divisor
     * Theorem: gcd(a, b) = gcd(b, a % b)
     * Time Complexity: O(log(min(a, b)))
     * Space Complexity: O(log(min(a, b))) recursive stack, or O(1) iterative
     */
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    /**
     * Least Common Multiple via GCD
     * Identity: a * b = gcd(a, b) * lcm(a, b)
     * Prevention of Overflow: Divide before multiplying!
     */
    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) return 0;
        return (Math.abs(a) / gcd(a, b)) * Math.abs(b);
    }

    /**
     * Extended Euclidean Algorithm:
     * Finds integers x, y such that a*x + b*y = gcd(a, b) (Bézout's identity)
     */
    public static long[] extendedGcd(long a, long b) {
        if (b == 0) {
            return new long[]{a, 1, 0}; // {gcd, x, y}
        }
        long[] next = extendedGcd(b, a % b);
        long g = next[0];
        long x1 = next[1];
        long y1 = next[2];

        long x = y1;
        long y = x1 - (a / b) * y1;
        return new long[]{g, x, y};
    }
}
