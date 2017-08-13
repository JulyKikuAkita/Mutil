package kiku.mutil.unit;

/**
 * Implication:
 *  You are given an integer array nums and you have to return a new counts array.
 *  The counts array has the property where counts[i] is the number of smaller elements to the right of nums[i].

     Given nums = [5, 2, 6, 1]

     To the right of 5 there are 2 smaller elements (2 and 1).
     To the right of 2 there is only 1 smaller element (1).
     To the right of 6 there is 1 smaller element (1).
     To the right of 1 there is 0 smaller element.
     Return the array [2, 1, 1, 0].
 */
public class BSTree {
    private TreeNode mRoot;

    public int insertNode(int val) {
        if ( mRoot == null) {
            mRoot = new TreeNode(val);
            return 0;
        }

        int count = 0;
        TreeNode cur = mRoot;
        while(true) {
            if (cur.mVal > val) { //insert left sub tree
                cur.mLeftCount++;
                if (cur.mLeft != null) {
                    cur = cur.mLeft;
                } else {
                    cur.mLeft = new TreeNode(val);
                    break;
                }
            } else if (cur.mVal < val) {  //insert right sub tree
                count += cur.mLeftCount + cur.mSelfCount;
                if (cur.mRight != null) {
                    cur = cur.mRight;
                } else {
                    cur.mRight = new TreeNode(val);
                    break;
                }
            } else { //insert current node
                cur.mSelfCount++;
                count += cur.mLeftCount;
                break;
            }
        }
        return count;
    }

    private class TreeNode {
        private TreeNode mLeft;
        private TreeNode mRight;
        private int mVal;
        private int mLeftCount;
        private int mSelfCount;

        TreeNode(int val) {
            mVal = val;
            mSelfCount = 1;
        }
    }
}
