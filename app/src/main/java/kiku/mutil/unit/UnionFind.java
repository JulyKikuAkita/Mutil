package kiku.mutil.unit;

public class UnionFind {
    private int mCount;
    private int[] mParent, mRank;

    public UnionFind(int n) {
        mCount = n;
        mParent = new int[n];
        mRank = new int[n];
        for (int i = 0; i < n; i++) {
            mParent[i] = i;
        }
    }

    public int find(int p) {
        while( p != mParent[p] ) {
            mParent[p] = mParent[mParent[p]];
            p = mParent[p];
        }
        return p;
    }

    public boolean union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return false ; //same tree
        //merge smaller tree to bigger tree by rank
        if (mRank[rootP] < mRank[rootQ]) {
            mParent[rootP] = rootQ;
        } else {
            mParent[rootQ] = rootP;
            if (mRank[rootQ] == mRank[rootP]) {
                mRank[rootP]++;
            }
        }
        mCount--;
        return true;
    }

    public int getCount() {
        return mCount;
    }
}
