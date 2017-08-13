package kiku.mutil.unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *  segment tree for 1D rangeSum query, mutable
 *  Given nums = [1, 3, 5]
 sumRange(0, 2) -> 9
 update(1, 2)
 sumRange(0, 2) -> 8
 */
public class SegmentTree1D {
    SegmentTreeNode mRoot;

    public SegmentTree1D(int[] nums) {
        mRoot = buildTree(nums, 0, nums.length - 1);
    }

    public SegmentTree1D(Long[] sumArr, int low, int high) {
        mRoot = buildArrSumTree(sumArr, low, high);
    }

    private SegmentTreeNode buildTree(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        } else {
            SegmentTreeNode node = new SegmentTreeNode(start, end);
            if (start == end) {
                node.mSum = nums[start];
            } else {
                int mid = start + (end - start) / 2;
                node.mLeft = buildTree(nums, start, mid);
                node.mRight = buildTree(nums, mid+1, end);
                node.mSum = node.mLeft.mSum + node.mRight.mSum;
            }
            return node;
        }
    }

    private SegmentTreeNode buildArrSumTree(Long[] sumArr, int low, int high) {
        if (low > high) return null;

        SegmentTreeNode sumNode = new SegmentTreeNode(sumArr[low], sumArr[high]);
        if (low == high) {
            return sumNode;
        } else {
            int mid = low + (high - low) / 2;
            sumNode.mLeft = buildArrSumTree(sumArr, low, mid);
            sumNode.mRight = buildArrSumTree(sumArr, mid + 1, high);
        }
        return sumNode;
    }

    public void update(int i, int val) {
        update(mRoot, i, val);
    }

    private void update(SegmentTreeNode root, int pos, int val) {
        if (root.mStart == root.mEnd) {
            root.mSum = val;
        } else {
            int mid = root.mStart + (root.mEnd - root.mStart) / 2;

            if (pos <= mid) {
                update(root.mLeft, pos, val);
            } else {
                update(root.mRight, pos, val);
            }
            root.mSum = root.mLeft.mSum + root.mRight.mSum;
        }
    }

    /**
     * increase count of segment tree node within given val
     */
    public void increaseSTCount(Long val) {
        increaseSTCount(mRoot, val);
    }

    private void increaseSTCount(SegmentTreeNode root, Long val) {
        if (root == null) return;
        if (val >= root.mMin && val <= root.mMax) {
            root.mCount++;
            increaseSTCount(root.mLeft, val);
            increaseSTCount(root.mRight, val);
        }
    }

    public int getCountWithRange(long min, long max) {
        return getCountWithRange(mRoot, min, max);
    }

    private int getCountWithRange(SegmentTreeNode root, long min, long max) {
        if (root == null) return 0;
        if (min > root.mMax || max < root.mMin) return 0;
        if (min <= root.mMin && max >= root.mMax) return root.mCount;
        return getCountWithRange(root.mLeft, min, max) + getCountWithRange(root.mRight, min, max);
    }

    public int sumRange(int i, int j) {
        return sumRange(mRoot, i, j);
    }

    private int sumRange(SegmentTreeNode root, int start, int end) {
        if (root.mEnd == end && root.mStart == start) {
            return root.mSum;
        } else {
            int mid = mRoot.mStart + (root.mEnd - root.mStart) / 2;
            if (end <= mid) {
                return sumRange(root.mLeft, start, end);
            } else if (start >= mid + 1) {
                return sumRange(root.mRight, start, end);
            } else {
                return sumRange(root.mLeft, start, mid) + sumRange(root.mRight, mid + 1, end);
            }
        }
    }

    public void printTree() {
        Queue<SegmentTreeNode> q = new LinkedList<>();
        q.add(mRoot);
        while (!q.isEmpty()) {
            int len = q.size();
            List<String> res = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                SegmentTreeNode node = q.poll();
                if (node.mLeft != null) q.add(node.mLeft);
                if (node.mRight != null) q.add(node.mRight);
                res.add("@" + "Min: " + node.mMin + " " + "Max: " + node.mMax + " " + "Cnt: " + node.mCount + "| ");
            }
            System.out.println(res.toString());
        }
    }

    public class SegmentTreeNode{
        int mStart, mEnd;
        SegmentTreeNode mLeft, mRight;
        int mSum, mCount;
        long mMin, mMax;

        public SegmentTreeNode(int start, int end) {
            mStart = start;
            mEnd = end;
            mLeft = null;
            mRight = null;
            mSum = 0;
        }

        public SegmentTreeNode(long min, long max) {
            mMin = min;
            mMax = max;
        }
    }
}

