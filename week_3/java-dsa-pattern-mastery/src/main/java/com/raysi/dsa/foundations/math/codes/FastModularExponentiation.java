package com.raysi.dsa.foundations.math.codes;

/**
 * Foundation Math: Fast Modular Exponentiation & Modular Inverse
 * Author: raysi
 * Package: com.raysi.dsa.foundations.math.codes
 */
public class FastModularExponentiation {

    /**
     * Binary Exponentiation: computes (base^exp) % mod in O(log exp) time.
     * Prevents integer overflow by using 64-bit long products and bit shifts.
     */
    public static long powerMod(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;

        while (exp > 0) {
            // If exp is odd, multiply with result
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            // exp = exp / 2
            exp >>= 1;
            // base = (base * base) % mod
            base = (base * base) % mod;
        }
        return result;
    }

    /**
     * Modular Multiplicative Inverse using Fermat's Little Theorem
     * Valid when 'mod' is prime and gcd(a, mod) == 1.
     * a^(mod - 1) ≡ 1 (mod m) => a^(mod - 2) ≡ a^(-1) (mod m)
     */
    public static long modInverse(long a, long primeMod) {
        return powerMod(a, primeMod - 2, primeMod);
    }
}
