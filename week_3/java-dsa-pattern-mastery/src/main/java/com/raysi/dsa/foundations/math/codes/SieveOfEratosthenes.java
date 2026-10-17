package com.raysi.dsa.foundations.math.codes;

import java.util.Arrays;

/**
 * Foundation Math: Sieve of Eratosthenes & Prime Factorization
 * Author: raysi
 * Package: com.raysi.dsa.foundations.math.codes
 */
public class SieveOfEratosthenes {

    /**
     * Standard Sieve of Eratosthenes
     * Finds primes up to N
     * Time Complexity: O(N log(log N))
     * Space Complexity: O(N)
     */
    public static boolean[] sieve(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        if (n >= 0) isPrime[0] = false;
        if (n >= 1) isPrime[1] = false;

        for (int p = 2; (long) p * p <= n; p++) {
            if (isPrime[p]) {
                // Optimization: Start marking multiples from p * p
                for (int multiple = p * p; multiple <= n; multiple += p) {
                    isPrime[multiple] = false;
                }
            }
        }
        return isPrime;
    }

    /**
     * Smallest Prime Factor (SPF) Sieve
     * Enables O(log N) prime factorization per query for numbers <= N
     */
    public static int[] smallestPrimeFactorSieve(int n) {
        int[] spf = new int[n + 1];
        for (int i = 0; i <= n; i++) spf[i] = i;

        for (int p = 2; (long) p * p <= n; p++) {
            if (spf[p] == p) { // p is prime
                for (int mult = p * p; mult <= n; mult += p) {
                    if (spf[mult] == mult) {
                        spf[mult] = p; // Mark smallest prime factor
                    }
                }
            }
        }
        return spf;
    }
}
