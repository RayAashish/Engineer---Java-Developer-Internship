package com.raysi.dsa.stackandqueue.queue.codes;

import java.util.*;

/**
 * ============================================================================
 * PATTERN: PriorityQueue (Binary Heap) & Dual-Heap Balancing
 * AUTHOR : raysi
 * PACKAGE: com.raysi.dsa.stackandqueue.queue.codes
 * ============================================================================
 * 
 * CORE INTUITION:
 * 1. Top-K Largest: Maintain a MIN-HEAP of size K.
 *    Any element smaller than heap top is discarded.
 *    Heap always holds the K largest seen so far -> heap.peek() is the K-th largest.
 *    Time: O(N log K) instead of O(N log N) sorting.
 * 
 * 2. Median from Data Stream: Maintain two balancing heaps:
 *    - `maxHeap` stores the smaller half of numbers.
 *    - `minHeap` stores the larger half of numbers.
 *    - Invariant: maxHeap.size() == minHeap.size() OR maxHeap.size() == minHeap.size() + 1.
 *    - Median is either maxHeap.peek() or the average of both roots!
 * ============================================================================
 */
public class TopKElementsPriorityQueueHeap {

    /**
     * Kth Largest Element in an Array via Min-Heap.
     * Time Complexity : O(N log K)
     * Space Complexity: O(K)
     */
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll(); // Evict smallest element
            }
        }

        return minHeap.peek();
    }

    /**
     * Top K Frequent Elements via Min-Heap.
     * Time Complexity : O(N log K)
     * Space Complexity: O(N)
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }

        // Min-heap ordered by frequency
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            Comparator.comparingInt(freqMap::get)
        );

        for (int num : freqMap.keySet()) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }

        return result;
    }

    /**
     * Find Median from Data Stream using Dual Balancing Heaps.
     * addNum()   : O(log N)
     * findMedian(): O(1)
     */
    public static class MedianFinder {
        private final PriorityQueue<Integer> maxHeap; // stores smaller half
        private final PriorityQueue<Integer> minHeap; // stores larger half

        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            // Step 1: Push to maxHeap first
            maxHeap.offer(num);

            // Step 2: Ensure all elements in maxHeap <= elements in minHeap
            minHeap.offer(maxHeap.poll());

            // Step 3: Maintain size invariant: maxHeap can have at most 1 more element
            if (maxHeap.size() < minHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            } else {
                return (maxHeap.peek() + (double) minHeap.peek()) / 2.0;
            }
        }
    }
}
