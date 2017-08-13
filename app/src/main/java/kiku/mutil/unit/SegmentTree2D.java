package kiku.mutil.unit;

public class SegmentTree2D {
    SegmentTree2DNode mRoot;

    public SegmentTree2D(int[][] matrix) {
        if (matrix.length == 0) {
            mRoot = null;
        } else {
            mRoot = buildTree(matrix, 0, 0, matrix.length-1, matrix[0].length-1);
        }
    }

    public void update(int row, int col, int val) {
        update2D(mRoot, row, col, val);
    }

    private void update2D(SegmentTree2DNode root, int row, int col, int val) {
        if (root.mRow1 == root.mRow2 && root.mRow1 == row
            && root.mCol1 == root.mCol2 && root.mCol1 == col) {
            root.mSum = val;
            return;
        }

        int rowMid = root.mRow1 + (root.mRow2 - root.mRow1) / 2;
        int colMid = root.mCol1 + (root.mCol2 - root.mCol1) / 2;
        SegmentTree2DNode next;

        if (row <= rowMid) {
            if (col <= colMid) {
                next = root.c1;
            } else {
                next = root.c2;
            }
        } else {
            if (col <= colMid) {
                next = root.c3;
            } else {
                next = root.c4;
            }
        }
        root.mSum -= next.mSum;
        update2D(next, row, col, val);
        root.mSum += next.mSum;
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        return sumRegion2D(mRoot, row1, col1, row2, col2);
    }

    private int sumRegion2D(SegmentTree2DNode root, int row1, int col1, int row2, int col2) {
        if (root.mRow1 == row1 && root.mCol1 == col1
                && root.mCol2 == col2
                && root.mRow2 == row2) return root.mSum;

        int rowMid = root.mRow1 + (root.mRow2 - root.mRow1) / 2;
        int colMid = root.mCol1 + (root.mCol2 - root.mCol1) / 2;
        if (rowMid >= row2) {
            if (colMid >= col2) {
                return sumRegion2D(root.c1, row1, col1, row2, col2);
            } else if (colMid + 1 <= col1) {
                return sumRegion2D(root.c2, row1, col1, row2, col2);
            } else {
                return sumRegion2D(root.c1, row1, col1, row2, colMid)
                        + sumRegion2D(root.c2, row1, colMid +1, row2, col2);
            }
        } else if (rowMid + 1 <= row1) {
            if (colMid >= col2) {
                return sumRegion2D(root.c3, row1, col1, row2, col2);
            } else if (colMid + 1 <= col1) {
                return sumRegion2D(root.c4, row1, col1, row2, col2);
            } else {
                return sumRegion2D(root.c3, row1, col1, row2, colMid)
                        + sumRegion2D(root.c4, row1, colMid +1, row2, col2);
            }
        } else {
            if (colMid >= col2) {
                return sumRegion2D(root.c1, row1, col1, rowMid, col2) +
                        sumRegion2D(root.c3, rowMid + 1, col1, row2, col2);
            } else if(colMid + 1 <= col1) {
                return sumRegion2D(root.c2, row1, col1, rowMid, col2) +
                        sumRegion2D(root.c4, rowMid + 1, col1, row2, col2);
            } else {
                return sumRegion2D(root.c1, row1, col1, rowMid, colMid) +
                        sumRegion2D(root.c2, row1, colMid + 1, rowMid, col2) +
                        sumRegion2D(root.c3, rowMid + 1, col1, row2, colMid) +
                        sumRegion2D(root.c4, rowMid + 1, colMid + 1, row2, col2);
            }
        }
    }

    private SegmentTree2DNode buildTree(int[][] matrix, int row1, int col1, int row2, int col2) {
        if (row2 < row1 || col2 < col1) return null;
        SegmentTree2DNode node = new SegmentTree2DNode(row1, col1, row2, col2);
        if (row1 == row2 && col1 == col2) {
            node.mSum = matrix[row1][col1];
            return node;
        }
        int rowMid = row1 + (row2 - row1) / 2;
        int colMid = col1 + (col2 - col1) / 2;
        node.c1 = buildTree(matrix, row1, col1, rowMid, colMid);
        node.c2 = buildTree(matrix, row1, colMid + 1, rowMid, col2);
        node.c3 = buildTree(matrix, rowMid + 1, col1, row2, colMid);
        node.c4 = buildTree(matrix, rowMid + 1, colMid + 1, row2, col2);
        node.mSum += node.c1 != null ? node.c1.mSum : 0;
        node.mSum += node.c2 != null ? node.c2.mSum : 0;
        node.mSum += node.c3 != null ? node.c3.mSum : 0;
        node.mSum += node.c4 != null ? node.c4.mSum : 0;
        return node;
    }

    public class SegmentTree2DNode {
        int mRow1, mRow2, mCol1, mCol2, mSum;
        SegmentTree2DNode c1, c2, c3, c4;
        public SegmentTree2DNode (int row1, int col1, int row2, int col2) {
            mRow1 = row1;
            mCol1 = col1;
            mRow2 = row2;
            mCol2 = col2;
            mSum = 0;
        }
    }
}
