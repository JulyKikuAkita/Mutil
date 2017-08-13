package kiku.mutil;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;

public class UtilPrintTest {
    public static final int[][] dirs = new int[][]{{1,0}, {-1,0}, {0,1}, {0,-1}};

    //##############################################################################################
    @Test
    public void testPrintDR() {
        String[][] matrix ={
                {"a","b","c"},
                {"d","e","f"},
                {"g","h","i"}
        };

        int rows = matrix.length;
        int cols = matrix[0].length;
        printDR(matrix, rows, cols);
        System.out.println();
        printDRRev(matrix, rows, cols);
        System.out.println();
        printSpiralMatrix(matrix);
    }

    public int[] printDiagonalOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return new int[0];
        int m = matrix.length, n = matrix[0].length;

        int[] result = new int[m * n];
        int row = 0, col = 0, d = 0;
        int[][] dirs = {{-1, 1}, {1, -1}};

        for (int i = 0; i < m * n; i++) {
            result[i] = matrix[row][col];
            row += dirs[d][0];
            col += dirs[d][1];

            if (row >= m) { row = m - 1; col += 2; d = 1 - d;}
            if (col >= n) { col = n - 1; row += 2; d = 1 - d;}
            if (row < 0)  { row = 0; d = 1 - d;}
            if (col < 0)  { col = 0; d = 1 - d;}
        }

        return result;
    }

    public void printDRRev(String[][] matrix, int rows, int cols){
        for(int c=cols - 1; c >=0; c--){
            for(int i=0, j=c; j< cols;i++,j++){
                System.out.print(matrix[i][j] +" ");
            }
            System.out.println();
        }

        for(int r =1; r < rows; r++){
            for(int i =r, j= 0; i<rows && j<rows; i++,j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printDR(String[][] matrix, int rows, int cols){
        for(int c=0; c < cols; c++){
            for(int i=0, j=c; i< rows && j>=0;i++,j--){
                System.out.print(matrix[i][j] +" ");
            }
            System.out.println();
        }

        for(int r =1; r < rows; r++){
            for(int i =r, j= cols -1; i<rows && j>=0; i++,j--){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printSpiralMatrix(String[][] matrix) {
        int rowStart = 0;
        int rowEnd = matrix.length-1;
        int colStart = 0;
        int colEnd = matrix[0].length-1;

        while (rowStart <= rowEnd && colStart <= colEnd) {
            for (int i = colStart; i <= colEnd; i ++) {
                System.out.print(matrix[rowStart][i] +" ");
            }
            rowStart ++;
            System.out.println();

            for (int i = rowStart; i <= rowEnd; i ++) {
                System.out.print(matrix[i][colEnd] +" ");
            }
            colEnd --;
            System.out.println();

            for (int i = colEnd; i >= colStart; i --) {
                if (rowStart <= rowEnd)
                    System.out.print(matrix[rowEnd][i] +" ");
            }
            rowEnd --;
            System.out.println();

            for (int i = rowEnd; i >= rowStart; i --) {
                if (colStart <= colEnd)
                    System.out.print(matrix[i][colStart] +" ");
            }
            colStart ++;
            System.out.println();
        }
    }
    //##############################################################################################

    @Test
    public void testMaxIslands() {
        String[][] islands ={
                {"1","1","0"},
                {"1","1","0"},
                {"0","0","1"}
        };
        assertTrue(getMaxIslands(islands) == 4);
    }

    public int getMaxIslands(String[][] islands) {
        if (islands == null || islands.length == 0 || islands[0].length == 0) return 0;
        int max = 0;
        for (int i = 0; i < islands.length; i++) {
            for (int j = 0; j < islands[0].length; j++) {
                if (islands[i][j].equals("1")) {
                    max = Math.max(max, dfs(islands, 1, i, j));
                }
            }
        }
        return max;
    }

    public int dfs(String[][] islands, int max, int i, int j) {
        int cur = 0;
        islands[i][j] = "2";
        for (int[] dir : dirs) {
            int x = i + dir[0];
            int y = j + dir[1];
            if (x >= 0 && x < islands.length && y >= 0 && y < islands[0].length && islands[x][y].equals("1")) {
                cur = dfs(islands, max + 1, x, y);
                //System.out.println(cur + " x :" + x + " y :" + y);
            }
        }
        return Math.max(cur, max);
    }

    //##############################################################################################
//    Get text left justified and no extra space is inserted between words.
//
//    For example,
//    words: ["This", "is", "an", "example", "of", "text", "justification."]
//    L: 16.
//
//    Return the formatted lines as:
//            [
//            "This    is    an",
//            "example  of text",
//            "justification.  "
//            ]
//    Note: Each word is guaranteed not to exceed L in length.

    public class TextLeftJustifier {
        public List<String> fullJustify(String[] words, int maxWidth) {
            List<String> res = new ArrayList<>();
            int start = 0, end = 0;
            while (start < words.length) {
                int len = 0; //get pure word length with one padding
                while (end < words.length && len + words[end].length() <= maxWidth) {
                    len += words[end].length() + 1;
                    end++;
                }

                StringBuilder sb = new StringBuilder();
                if (end - start == 1) { //add only one word in a line
                    sb.append(words[start]);
                    appendSpaces(sb, maxWidth - words[start].length());
                } else if (end == words.length) { //last word
                    for (int i = start; i < end - 1 ;i++) {
                        sb.append(words[i]).append(' ');
                    }
                    sb.append(words[end - 1]);
                    appendSpaces(sb, maxWidth - len + 1); //need to deduct 1 space from the last word
                } else { //need balance space between words
                    int needPadding = maxWidth - len + 1;
                    int paddingBwWords = needPadding / (end - start -1);
                    for (int i = start; i < end - 1 ;i++) { //no between words padding for last word
                        sb.append(words[i]);
                        //need handle if paddingBwWords == 0
                        int offset = 0;
                        if ((needPadding % (end - start - 1)) > (i - start)) {
                            offset = 1;
                        }
                        appendSpaces(sb, paddingBwWords + 1 + offset);
                    }
                    sb.append(words[end - 1]);
                }
                res.add(sb.toString());
                start = end;
            }
            return res;
        }

        private void appendSpaces(StringBuilder sb, int num) {
            for (int i = 0; i < num; i++) {
                sb.append(' ');
            }
        }
    }
    //##############################################################################################


}
