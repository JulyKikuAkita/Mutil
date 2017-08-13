package kiku.mutil.unit;

/**
 * Binary Indexed Trees (BIT or Fenwick tree):
 * https://www.topcoder.com/community/data-science/data-science-
 * tutorials/binary-indexed-trees/
 *
 * Example: given an array a[0]...a[7], we use a array BIT[9] to
 * represent a tree, where index [2] is the parent of [1] and [3], [6]
 * is the parent of [5] and [7], [4] is the parent of [2] and [6], and
 * [8] is the parent of [4]. I.e.,
 *
 * BIT[] as a binary tree:
 *            ______________*
 *            ______*
 *            __*     __*
 *            *   *   *   *
 * indices: 0 1 2 3 4 5 6 7 8
 *
 * BIT[i] = ([i] is a left child) ? the partial sum from its left most
 * descendant to itself : the partial sum from its parent (exclusive) to
 * itself. (check the range of "__").
 *
 * Eg. BIT[1]=a[0], BIT[2]=a[1]+BIT[1]=a[1]+a[0], BIT[3]=a[2],
 * BIT[4]=a[3]+BIT[3]+BIT[2]=a[3]+a[2]+a[1]+a[0],
 * BIT[6]=a[5]+BIT[5]=a[5]+a[4],
 * BIT[8]=a[7]+BIT[7]+BIT[6]+BIT[4]=a[7]+a[6]+...+a[0], ...
 *
 * Thus, to update a[1]=BIT[2], we shall update BIT[2], BIT[4], BIT[8],
 * i.e., for current [i], the next update [j] is j=i+(i&-i) //double the
 * last 1-bit from [i].
 *
 * Similarly, to get the partial sum up to a[6]=BIT[7], we shall get the
 * sum of BIT[7], BIT[6], BIT[4], i.e., for current [i], the next
 * summand [j] is j=i-(i&-i) // delete the last 1-bit from [i].
 *
 * To obtain the original value of a[7] (corresponding to index [8] of
 * BIT), we have to subtract BIT[7], BIT[6], BIT[4] from BIT[8], i.e.,
 * starting from [idx-1], for current [i], the next subtrahend [j] is
 * j=i-(i&-i), up to j==idx-(idx&-idx) exclusive. (However, a quicker
 * way but using extra space is to store the original array.)
 * // Your NumArray object will be instantiated and called as such:
 // NumArray numArray = new NumArray(nums);
 // numArray.sumRange(0, 1);
 // numArray.update(1, 10);
 // numArray.sumRange(1, 2);
 */
public class BITree {

    public static class BIT1DTree{
        int[] mNums;
        int[] mBIT;
        int mTotal1D;

        public BIT1DTree(int[] nums) {
            mNums = nums;
            mTotal1D = nums.length;
            mBIT = new int[nums.length + 1];
            for (int i = 0; i < nums.length; i++) {
                initBIT(i, nums[i]);
            }
        }

        private void initBIT(int i, int val) {
            i++;
            while (i <= mTotal1D) {
                mBIT[i] += val;
                i += (i & -i);
            }
        }

        public void update(int i, int val) {
            int diff = val - mNums[i];
            mNums[i] = val;
            initBIT(i, diff);
        }

        /**
         * If we want to read cumulative frequency for some integer idx, we add to sum tree[idx],
         * substract last bit of idx from itself (also we can write – remove the last digit;
         * change the last digit to zero) and repeat this while idx is greater than zero.
         */
        public int getSum(int i) {
            int sum = 0;
            i++;
            while (i > 0) {
                sum += mBIT[i];
                i -= ( i & -i);
            }
            return sum;
        }

        public int sumRange(int i, int j) {
            return getSum(j) - getSum(i - 1);
        }
    }

    /**
     * Using 2D Binary Indexed Tree, 2D BIT Def:
     * bit[i][j] saves the rangeSum of [i-(i&-i), i] x [j-(j&-j), j]
     * note bit index == matrix index + 1
     */
    public static class BIT2DTree{
        int[][] mBIT2D;
        int[][] mNums2D;
        int mRow;
        int mCol;

        public BIT2DTree(int[][] matrix) {
            if (matrix.length == 0 || matrix[0].length == 0) return;
            mRow = matrix.length;
            mCol = matrix[0].length;
            mBIT2D = new int[mRow + 1][mCol + 1];
            mNums2D = new int[mRow][mCol];
            for (int i = 0; i < mRow; i++) {
                for (int j = 0; j < mCol; j++) {
                    update(i, j, matrix[i][j]);
                }
            }
        }

        public void update(int row, int col, int val) {
            if (mRow == 0 || mCol == 0) return;
            int delta = val - mNums2D[row][col];
            mNums2D[row][col] = val;
            for (int i = row + 1; i <= mRow; i += i & (-i)) {
                for (int j = col + 1; j <= mCol; j += j & (-j)) {
                    mBIT2D[i][j] += delta;
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            if (mRow == 0 || mCol == 0) return 0;
            return sum(row2 + 1, col2 + 1)
                    + sum(row1, col1)
                    - sum(row1, col2 + 1)
                    - sum(row2 + 1, col1);
        }

        public int sum(int row, int col) {
            int sum = 0;
            for (int i = row; i > 0; i-= i & (-i)) {
                for (int j = col; j> 0; j -= j & (-j)) {
                    sum += mBIT2D[i][j];
                }
            }
            return sum;
        }
    }

}
