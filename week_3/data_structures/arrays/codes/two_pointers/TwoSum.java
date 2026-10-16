package codes.two_pointers;

import java.util.Arrays;

/**
 * TwoSum
 * on a sorted array
 */
public class TwoSum {

    public int[] twoSum(int[] nums, int sum){
        int l = 0, r = nums.length - 1;
        while (l < r){
            int n = nums[l] + nums[r];
            if (n == sum)
                return new int[]{l, r};
            else if (n > sum)
                r--;
            else
                l++;
        }
        return new int[]{-1, -1};
    }
    public static void main(String[] args) {
        int[] nums = {23, 45, 78, 90, 123, 1000};
        TwoSum twoSum = new TwoSum();
        System.out.println(Arrays.toString(twoSum.twoSum(nums, 1123)));
    }
}
