package com.raysi.dsa.foundations.bitmanipulation.codes;

/**
 * XOR Properties & Single Number Patterns
 * Author: raysi
 * Package: com.raysi.dsa.foundations.bitmanipulation.codes
 */
public class XorSingleNumberPatterns {

    /**
     * Pattern: Single Number I (Every element appears twice except one)
     * Invariants: a ^ a = 0, a ^ 0 = a, associative & commutative
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public static int singleNumberI(int[] nums) {
        int xor = 0;
        for (int x : nums) {
            xor ^= x;
        }
        return xor;
    }

    /**
     * Pattern: Single Number III (Every element appears twice except TWO numbers)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public static int[] singleNumberIII(int[] nums) {
        // Step 1: XOR all elements -> result is (num1 ^ num2)
        int xor = 0;
        for (int x : nums) {
            xor ^= x;
        }

        // Step 2: Find a set bit where num1 and num2 differ (isolate rightmost set bit)
        int differentiatingBit = xor & (-xor);

        // Step 3: Partition into two buckets based on this bit
        int num1 = 0;
        int num2 = 0;
        for (int x : nums) {
            if ((x & differentiatingBit) != 0) {
                num1 ^= x;
            } else {
                num2 ^= x;
            }
        }
        return new int[]{num1, num2};
    }

    /**
     * Pattern: Single Number II (Every element appears 3 times except one)
     * Bit Count Modulo 3
     * Time Complexity: O(32 * N) = O(N)
     * Space Complexity: O(1)
     */
    public static int singleNumberII(int[] nums) {
        int result = 0;
        for (int bit = 0; bit < 32; bit++) {
            int bitCount = 0;
            for (int x : nums) {
                if (((x >> bit) & 1) == 1) {
                    bitCount++;
                }
            }
            if (bitCount % 3 != 0) {
                result |= (1 << bit);
            }
        }
        return result;
    }
}
