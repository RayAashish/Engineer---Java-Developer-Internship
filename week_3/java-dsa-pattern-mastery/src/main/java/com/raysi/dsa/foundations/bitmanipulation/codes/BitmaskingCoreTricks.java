package com.raysi.dsa.foundations.bitmanipulation.codes;

/**
 * Core Bitwise Tricks & Bitmask Operations
 * Author: raysi
 * Package: com.raysi.dsa.foundations.bitmanipulation.codes
 */
public class BitmaskingCoreTricks {

    // 1. Check if the i-th bit is set (0-indexed)
    public static boolean isIthBitSet(int n, int i) {
        return (n & (1 << i)) != 0;
    }

    // 2. Set the i-th bit
    public static int setIthBit(int n, int i) {
        return n | (1 << i);
    }

    // 3. Clear (unset) the i-th bit
    public static int clearIthBit(int n, int i) {
        return n & ~(1 << i);
    }

    // 4. Toggle the i-th bit
    public static int toggleIthBit(int n, int i) {
        return n ^ (1 << i);
    }

    // 5. Turn off the rightmost set bit (Brian Kernighan's trick)
    // Example: 12 (1100) & 11 (1011) = 8 (1000)
    public static int clearRightmostSetBit(int n) {
        return n & (n - 1);
    }

    // 6. Isolate the rightmost set bit
    // Example: 12 (1100) & -12 = 4 (0100)
    public static int isolateRightmostSetBit(int n) {
        return n & (-n);
    }

    // 7. Check if n is a power of 2
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    // 8. Count set bits (Hamming Weight) in O(number of set bits)
    public static int countSetBits(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1); // Clears lowest set bit each iteration
            count++;
        }
        return count;
    }
}
