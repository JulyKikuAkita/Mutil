package kiku.mutil;

import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import kiku.mutil.unit.BITree;
import kiku.mutil.unit.BSTree;
import kiku.mutil.unit.PMap;
import kiku.mutil.unit.SegmentTree1D;
import kiku.mutil.unit.SegmentTree2D;
import kiku.mutil.unit.Trie;
import kiku.mutil.unit.UnionFind;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class Util {
    //##############################################################################################

    public static void readFileByLineToTrie(Trie trie, String path) {
        String line = null;
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                trie.insert(trie.alterToLettersOnly(line));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateTrieSearchResultToFile(Trie trie, String inputFile, String outputFile) {
        String line = null;
        try {
            File file = new File(inputFile);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String input = trie.alterToLettersOnly(line);
                String result  = trie.getPossibleWordWithNumberMapping(input).toString();

                PMap.generateOutputFile(outputFile, Arrays.asList(new String[]{input + ": " + result}));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Here limit to ASCII encoding, not considering UNICODE
     * https://stackoverflow.com/questions/18304804/what-is-the-difference-between-character-isalphabetic-and-character-isletter-in
     */
    public String alterToLowerCaseLettersOnly(String testName) {
        //use regex
        String result = testName.trim().replaceAll("[^a-zA-Z0-9\\s]", "");
        /*
        StringBuilder sb = new StringBuilder();
        for (char c : testName.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
        */
        return result.toLowerCase();
    }

    public void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    //##############################################################################################

    /**
     * not support upper case as A-Z with 065- 090, a-z with 097-122
     */
    @Test
    public void testTrie() {
        Trie trie = new Trie();
        String testName1 = trie.alterToLettersOnly("com.tagged.appium.tests.AdsTest.testChatAdsDelayAnimation");

        trie.insert(testName1);

        assertTrue(trie.searchWord(trie.alterToLettersOnly("com.tagged.appium.tests.AdsTest.testChatAdsDelayAnimation")));
        assertFalse(trie.searchWord("MeetMeTest".toLowerCase()));
        assertTrue(trie.startsWith("com".toLowerCase()));
        assertFalse(trie.startsWith("tagged".toLowerCase()));
    }

    @Test
    public void testTrieT9() {
        String path = "src/main/res/t9Words.txt";
        Trie trie = new Trie();
        readFileByLineToTrie(trie, path);
        String[] t9_4355 = new String[]{"gell", "hell"};
        String[] t9_4332 = new String[]{"idea"};
        String[] t9_43556 = new String[]{"gekko", "hello"};

        String[] t9_968 = new String[]{"wot", "you"};
        String[] t9_63 = new String[]{"me","ne","od","oe","of"};
        String[] t9_627427482 = new String[]{"margarita"};

        System.out.println(trie.getPossibleWordWithNumberMapping("968"));
        System.out.println(trie.getPossibleWordWithNumberMapping("63").toString());
        System.out.println(trie.getPossibleWordWithNumberMapping("627427482").toString());
        System.out.println(trie.getPossibleWordWithNumberMapping("727456").toString()); //<No Result>

        //assertEquals(Arrays.asList(t9_4355).toString(), trie.getPossibleWordWithNumberMapping("4355").toString());
        //assertEquals(Arrays.asList(t9_4332).toString(), trie.getPossibleWordWithNumberMapping("4332").toString());
        //assertEquals(Arrays.asList(t9_43556).toString(), trie.getPossibleWordWithNumberMapping("43556").toString());
    }

    @Test
    public void testTrieMatchPrefix() {
        String path = "src/main/res/text.txt";
        Trie trie = new Trie();
        readFileByLineToTrie(trie, path);

        List<String> res = trie.getAllMatchFreqWithPrefix("", 5);
        System.out.println(res.toString());
        //System.out.println(helperTrieMatchPrefix(prefix).toString());
        //assertEquals(res.toString(), helperTrieMatchPrefix(prefix).toString());
    }

    public List<String> helperTrieMatchPrefix(String prefix) {
        String[] arr = new String[] {"GOING", "GODADDY", "GO", "G", "GOGGGGG", "ABC"};
        List<String> res = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].startsWith(prefix)) {
                res.add(arr[i].toLowerCase());
            }
        }
        Collections.sort(res);
        return res;
    }

    //##############################################################################################
    @Test
    public void testEdgesCanFormTree() {
        int[][] edges1 = new int[][]{{0, 1}, {0, 2}, {0, 3}, {1, 4}};
        int[][] edges2 = new int[][]{{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}};

        assertTrue(validTreeUF(5,edges1));
        assertTrue(validTreeBFS(5, edges1) == validTreeBFS2(5, edges1));
        assertFalse(validTreeUF(5, edges2));
        assertTrue(validTreeDFS(5, edges2) == validTreeBFS(5,edges2));
    }

    @Test
    public void tesAlienDictionaryOrder() {
        String[] words1 = new String[]{"wrt", "wrf", "er", "ett", "rftt"};
        String[] words2 = new String[]{"z", "x"};
        String[] words3 = new String[]{"z", "x", "z"};

        assertEquals("wertf", alienOrderBFS(words1));
        assertEquals(alienOrderBFS2(words1), alienOrderBFS(words1));
        assertEquals(alienOrderDFS2(words1), alienOrderDFS(words1));
        assertEquals("zx", alienOrderBFS(words2));
        assertEquals("zx", alienOrderDFS(words2));
        assertEquals("", alienOrderDFS(words3));
    }

    //##############################################################################################
    @Test
    public void testCaesarCipher() {
        //abc + 2 ->cde
        //xyz+2->zab
        System.out.println(cipher("abc", 2));
        System.out.println(cipher("xyz", 2));
        System.out.println(cipher("abcdefghijklmnopqrstuvwxyzabc", 2));
    }

    public String cipher(String a, int shift) {
        String table = "abcdefghijklmnopqrstuvwxyz";
        if (a.length() < 26) {
            System.out.println(table.indexOf(a));
            int startIdx = table.indexOf(a) + shift % 26;
            int endIdx = startIdx + a.length();
            if ( endIdx > 26) {
                return table.substring(startIdx) + table.substring(0,endIdx % 26);
            }
            return table.substring(startIdx, startIdx + a.length());
        } else {
            int m = a.length() % 26;
            String tmp = a + a.substring(m, m + shift % 26);
            return tmp.substring(shift % 26, (shift % 26)+ a.length());
        }
    }

    //##############################################################################################
    @Test
    public void testCountSmallerOneExceptSelf(){
        int[] nums = new int[] {5,6,2,1};
        int[] ans = new int[]{2,1,1,0};
        assertEquals(countSmallerOneExceptSelf(nums), Arrays.asList(ans));
    }

    /**
     * return list where count the number of smaller elements to the right of nums[i].
     */
    public List<Integer> countSmallerOneExceptSelf(int[] input) {
        List<Integer> result = new ArrayList<>();
        BSTree bstBSTree = new BSTree();

        for (int i = input.length - 1; i >= 0; i--) { //iterate from end
            result.add(bstBSTree.insertNode(input[i]));
        }
        Collections.reverse(result);
        return result;
    }

    //##############################################################################################
    /**
     '.' Matches any single character.
     '*' Matches zero or more of the preceding element.
     The matching should cover the entire input string (not partial).
     */
    @Test
    public void testIsMatch() {
        String[] fulls = new String[]{"aa", "aa", "aa", "ab", "aab"};
        String[] partials = new String[]{"aa", "a*", ".*", ".*", "c*a*b"};

        String[] fulls2 = new String[]{"aa", "aaa"};
        String[] partials2 = new String[]{"a", "aa"};

        for (int i = 0 ; i < fulls.length; i++) {
            assertTrue(isMatch(fulls[i], partials[i]));
        }

        for (int i = 0 ; i < fulls2.length; i++) {
            assertFalse(isMatch(fulls2[i], partials2[i]));
        }
        assertTrue(isMatch("aaa", "*.a") == isMatch2("aaa", "*.a"));
    }

    /**
     * Here are some conditions to figure out, then the logic can be very straightforward.
     1, If p.charAt(j) == s.charAt(i) :  dp[i][j] = dp[i-1][j-1];
     2, If p.charAt(j) == '.' : dp[i][j] = dp[i-1][j-1];
     3, If p.charAt(j) == '*':
     here are two sub conditions:
     a   if p.charAt(j-1) != s.charAt(i) : dp[i][j] = dp[i][j-2]  //in this case, a* only counts as empty
     b   if p.charAt(i-1) == s.charAt(i) or p.charAt(i-1) == '.':
     (i)dp[i][j] = dp[i-1][j]    //in this case, a* counts as multiple a
     (ii)or dp[i][j] = dp[i][j-1]   // in this case, a* counts as single a
     (iii)or dp[i][j] = dp[i][j-2]   // in this case, a* counts as empty
     */

    public boolean isMatch(String full, String p) {
        boolean[][] dp = new boolean[full.length() + 1][p.length() + 1];
        dp[0][0] = true;
        for (int i = 1; i < p.length(); i++) {
            if ( p.charAt(i) == '*' && dp[0][i-1]) {
                dp[0][i + 1] = true;  //3 -b
            }
        }

        for (int i = 0; i < full.length(); i++) {
            for(int j = 0; j < p.length(); j++) {
                if (p.charAt(j) == '.') { //scenario 2
                    dp[i+1][j+1] = dp[i][j];
                } else if (p.charAt(j) == full.charAt(i)) { //scenario 1
                    dp[i+1][j+1] = dp[i][j];
                } else if (p.charAt(j) == '*') { //scenario 3
                    if (j > 0 && p.charAt(j - 1) != full.charAt(i) && p.charAt(j - 1) != '.') {//scenario 3-a
                        dp[i+1][j+1] = dp[i+1][j-1];
                    } else { //scenario 3-b
                        dp[i+1][j+1] = (dp[i][j+1] || dp[i+1][j] || dp[i+1][j-1]);
                    }
                }
            }
        }
        return dp[full.length()][p.length()];
    }

    public boolean isMatch2(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        boolean[][] dp = new boolean[s.length()+1][p.length()+1];
        dp[0][0] = true;
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) == '*' && dp[0][i-1]) { //"*.a" java.lang.ArrayIndexOutOfBoundsException?
                dp[0][i+1] = true;
            }
        }
        for (int i = 0 ; i < s.length(); i++) {
            for (int j = 0; j < p.length(); j++) {
                if (p.charAt(j) == '.') {
                    dp[i+1][j+1] = dp[i][j];
                }
                if (p.charAt(j) == s.charAt(i)) {
                    dp[i+1][j+1] = dp[i][j];
                }
                if (p.charAt(j) == '*') {
                    if (p.charAt(j-1) != s.charAt(i) && p.charAt(j-1) != '.') {
                        dp[i+1][j+1] = dp[i+1][j-1];
                    } else {
                        dp[i+1][j+1] = (dp[i+1][j] || dp[i][j+1] || dp[i+1][j-1]);
                    }
                }
            }
        }
        return dp[s.length()][p.length()];
    }

    //This only check ".*?"
    public boolean isWildMatch(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();
        if (p.length() == 1) {
            if (s.length() == 1) return p.charAt(0) == s.charAt(0) || p.charAt(0) == '.';
            else return false;
        }
        if (p.charAt(1) == '*') {
            return isMatch(s, p.substring(2)) ||
                    (!s.isEmpty() && (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.')
                            && isMatch(s.substring(1), p));
        } else {
            return !s.isEmpty() && (s.charAt(0) == p.charAt(0) || p.charAt(0) == '.')
                    && isMatch(s.substring(1), p.substring(1));
        }
    }

    //##############################################################################################
    /**
     * @param n nodes labeled from 0 to n - 1
     * @param edges a list of undirected edges (each edge is a pair of nodes)
     * @return boolean check whether these edges make up a valid tree
     */
    public boolean validTreeUF(int n, int[][] edges) { //UnionFind
        UnionFind uf = new UnionFind(n);
        for (int[] edge : edges) {
            if (!uf.union(edge[0], edge[1])){ //loop
                return false;
            }
        }
        return uf.getCount() == 1;
    }

    public boolean validTreeDFS(int n, int[][] edges) {
        List<List<Integer>> neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            neighbors.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            neighbors.get(edge[0]).add(edge[1]);
            neighbors.get(edge[1]).add(edge[0]);
        }

        int[] visited = new int[n];
        if (hasCycleDFS(visited, neighbors, 0, -1)) return false;
        for (int v : visited) {
            if (v == 0) return false;
        }
        return true;
    }

    /**
     * @param pred: need to check pred for dfs
     */
    public boolean hasCycleDFS(int[] visited, List<List<Integer>> neighbors, int node, int pred) {
        visited[node] = 1;
        for (Integer neighbor : neighbors.get(node)) {
            if (neighbor != pred) { // exclude current vertex's predecessor
                if (visited[neighbor] == 1) return true;
                if (visited[neighbor] == 0) {
                    if (hasCycleDFS(visited, neighbors, neighbor, node)) return true;
                }
            }
        }
        visited[node] = 2;
        return false;
    }

    public boolean validTreeBFS(int n, int[][] edges) {
        //parse graph to adjacent list
        List<Integer>[] neighbors = new List[n];
        for (int[] edge :edges) {
            int x = edge[0];
            int y = edge[1];
            if ( neighbors[x] == null) {
                neighbors[x] = new ArrayList<>();
            }
            if ( neighbors[y] == null) {
                neighbors[y] = new ArrayList<>();
            }
            neighbors[x].add(y);
            neighbors[y].add(x);
        }

        int[] visited = new int[n];
        Deque<Integer> q = new ArrayDeque<>();
        //start with node 0, n = 0
        q.addLast(0);
        visited[0] = 1;
        while(!q.isEmpty()) {
            Integer node = q.removeFirst();
            for(int neighbor : neighbors[node]) {
                if (visited[neighbor] == 1) return false; //loop
                if (visited[neighbor] == 0) {
                    visited[neighbor] = 1;
                    q.addLast(neighbor);
                }
            }
            visited[node] = 2; //is this necessary?
        }

        for (int v : visited) {
            if ( v == 0) return false; //not a connected component
        }
        return true;
    }

    public boolean validTreeBFS2(int n, int[][] edges) {
        int[] visited = new int[n];
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i=0; i<n; ++i) { adjList.add(new ArrayList<Integer>()); }
        for (int[] edge: edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }
        Deque<Integer> q = new ArrayDeque<>();
        q.addLast(0); visited[0] = 1;  // vertex 0 is in the queue, being visited
        while (!q.isEmpty()) {
            Integer cur = q.removeFirst();
            for (Integer succ: adjList.get(cur)) {
                if (visited[succ] == 1) { return false; }  // loop detected
                if (visited[succ] == 0) { q.addLast(succ); visited[succ] = 1; }
            }
            visited[cur] = 2;  // visit completed
        }
        for (int v: visited) { if (v == 0) { return false; } }  // # of connected components is not 1
        return true;
    }

    //##############################################################################################
    public String alienOrderDFS(String[] words) {
        String res = "";
        if (words == null || words.length == 0) return res;
        Map<Character, Set<Character>> neighbors = new HashMap<>();

        for (int i = 0; i < words.length; i++) {
            for (char c : words[i].toCharArray()) {
                neighbors.putIfAbsent(c, new HashSet<>());
            }
            if (i > 0) {
                String prev = words[i-1], cur = words[i];
                for ( int j = 0; j < prev.length() && j < cur.length(); j++ ) {
                    if (prev.charAt(j) != cur.charAt(j)) {
                        neighbors.get(prev.charAt(j)).add(cur.charAt(j));
                        break;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for(char c : neighbors.keySet()) {
            if (!dfs(c, neighbors, sb, new HashSet<Character>(), new HashSet<Character>() )) {
                return res;
            }
        }
        //Note, sb has duplicates, ex: wertffertftfrtf
        return sb.reverse().toString().substring(0, neighbors.keySet().size());
    }

    private boolean dfs(char c,
                        Map<Character, Set<Character>> neighbors,
                        StringBuilder sb,
                        Set<Character> visited,
                        Set<Character> loop) {
        if (visited.contains(c)) return true;
        if (loop.contains(c)) return false;
        loop.add(c);
        for (char next : neighbors.get(c)) {
            if (!dfs(next, neighbors, sb, visited, loop)) {
                return false;
            }
        }
        visited.add(c);
        sb.append(c);
        return true;

    }

    //dfs
    public String alienOrderDFS2(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }
        Map<Character, Set<Character>> graph = new HashMap<>();
        Stack<Character> stack = new Stack<>();
        Set<Character> visited = new HashSet<Character>();
        Set<Character> loop = new HashSet<Character>();
        // Read to graph
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            for (int j = 0; j < word.length(); j++) {
                char c = word.charAt(j);
                if (!graph.containsKey(c)) {
                    graph.put(c, new HashSet<Character>());
                }
            }
            if (i > 0) {
                String prev = words[i - 1];
                for (int k = 0; k < prev.length() && k < word.length(); k++) {
                    if (prev.charAt(k) != word.charAt(k)) {
                        graph.get(prev.charAt(k)).add(word.charAt(k));
                        break;
                    }
                }
            }
        }
        // DFS
        for (char c : graph.keySet()) {
            if (!dfs2(c, graph, stack, visited, loop)) {
                return "";
            }
        }
        // Print result
        StringBuilder sb = new StringBuilder();
        while (!stack.empty()) {
            sb.append(stack.pop());
        }
        return sb.toString();
    }

    private boolean dfs2(char c,
                         Map<Character, Set<Character>> graph,
                         Stack<Character> stack,
                         Set<Character> visited, Set<Character> loop) {
        if (visited.contains(c)) {
            return true;
        }
        if (loop.contains(c)) {
            return false;
        }
        loop.add(c);
        for (char next : graph.get(c)) {
            if (!dfs2(next, graph, stack, visited, loop)) {
                return false;
            }
        }
        visited.add(c);
        stack.push(c);
        return true;
    }

    //##############################################################################################
    public String alienOrderBFS(String[] words) {
        String res = "";
        if (words == null || words.length == 0) return res;
        Map<Character, Set<Character>> neighbors = new HashMap<>();
        Map<Character, Integer> degree = new HashMap<>();
        for (String word :words) {
            for (char c : word.toCharArray()) {
                degree.put(c, 0);
            }
        }

        //union chars
        for (int i = 0; i < words.length - 1; i++) {
            String cur = words[i];
            String next = words[i + 1];
            int len = Math.min(cur.length(), next.length());
            for (int j = 0; j < len; j++) {
                char c1 = cur.charAt(j);
                char c2 = next.charAt(j);
                if (c1 != c2) {
                    Set<Character> set = new HashSet<>();
                    if ( neighbors.containsKey(c1)) {
                        set = neighbors.get(c1);
                    }
                    if (!set.contains(c2)) {
                        set.add(c2);
                        neighbors.put(c1, set);

                        //update degree for c2
                        degree.put(c2, degree.get(c2) + 1);
                    }
                    break;
                }
            }
        }

        //start topological sort
        Queue<Character> q = new LinkedList<>();
        for (char c : degree.keySet()){
            if (degree.get(c) == 0) q.add(c);
        }

        while(!q.isEmpty()) {
            char cur = q.poll();
            res += cur;
            if (neighbors.containsKey(cur)) {
                for (char neighbor : neighbors.get(cur)) {
                    degree.put(neighbor, degree.get(neighbor) - 1);
                    if (degree.get(neighbor) == 0) q.add(neighbor);
                }
            }
        }
        return res;
    }

    //BFS
    public String alienOrderBFS2(String[] words) {
        Map<Character, Set<Character>> map=new HashMap<Character, Set<Character>>();
        Map<Character, Integer> degree=new HashMap<Character, Integer>();
        String result="";
        if(words==null || words.length==0) return result;
        for(String s: words){
            for(char c: s.toCharArray()){
                degree.put(c,0);
            }
        }
        for(int i=0; i<words.length-1; i++){
            String cur=words[i];
            String next=words[i+1];
            int length=Math.min(cur.length(), next.length());
            for(int j=0; j<length; j++){
                char c1=cur.charAt(j);
                char c2=next.charAt(j);
                if(c1!=c2){
                    Set<Character> set=new HashSet<Character>();
                    if(map.containsKey(c1)) set=map.get(c1);
                    if(!set.contains(c2)){
                        set.add(c2);
                        map.put(c1, set);
                        degree.put(c2, degree.get(c2)+1);
                    }
                    break;
                }
            }
        }
        Queue<Character> q=new LinkedList<Character>();
        for(char c: degree.keySet()){
            if(degree.get(c)==0) q.add(c);
        }
        while(!q.isEmpty()){
            char c=q.remove();
            result+=c;
            if(map.containsKey(c)){
                for(char c2: map.get(c)){
                    degree.put(c2,degree.get(c2)-1);
                    if(degree.get(c2)==0) q.add(c2);
                }
            }
        }
        if(result.length()!=degree.size()) return "";
        return result;
    }

    //##############################################################################################
    @Test
    public void testUpdateRangeSum() {
        int[] nums = new int[] {1,3,5};
        BITree.BIT1DTree biTree = new BITree.BIT1DTree(nums);
        SegmentTree1D st = new SegmentTree1D(nums);

        assertTrue(biTree.sumRange(0,2) == 9);
        assertTrue(st.sumRange(0,2) == 9);
        assertTrue( biTree.sumRange(0, 2) == st.sumRange(0, 2));

        biTree.update(1, 2);
        st.update(1,2);

        assertTrue(biTree.sumRange(0,2) == 8);
        assertTrue(st.sumRange(0,2) == 8);
        assertTrue( biTree.sumRange(0, 2) == st.sumRange(0, 2));
    }

    @Test
    public void testUpdateRangeSum2D() {
        int[][] matrix = new int[][]{
                {3, 0, 1, 4, 2},
                {5, 6, 3, 2, 1},
                {1, 2, 0, 1, 5},
                {4, 1, 0, 1, 7},
                {1, 0, 3, 0, 5}

        };

        BITree.BIT2DTree bit2DTree = new BITree.BIT2DTree(matrix);
        SegmentTree2D st = new SegmentTree2D(matrix);
        assertTrue(bit2DTree.sumRegion(2,1,4,3) == 8);
        assertTrue(st.sumRegion(2,1,4,3) == 8);

        bit2DTree.update(3, 2, 2);
        st.update(3,2,2);
        assertTrue(bit2DTree.sumRegion(2,1,4,3) == 10);
        assertTrue(st.sumRegion(2,1,4,3) == 10);
    }

    //##############################################################################################
    // Given an integer array nums, return the number of range sums
    // that lie in [lower, upper] inclusive.
    @Test
    public void testRangeSumWithBound() {
        int[] nums = new int[] {-2, 5, -1};
        int count = naiveRangeSumWithBound(nums, -2, 2);
        int countST= countRangeSumSegmentTree(nums, -2, 2);
        int countMS = countRangeSumMergeSort(nums, -2, 2);
        assertTrue(count == 3);
        assertTrue(countMS == 3);
        assertTrue(countST == 3);
        assertTrue(countRangeSumSegmentTreeWOSortedArr(nums, -2,2) == 3);
    }

    //Use SegmentTree1D
    public int countRangeSumSegmentTree(int[] nums, int lower, int upper) {
        if(nums == null || nums.length == 0) return 0;
        Set<Long> sumsSet = new HashSet<Long>();
        int res = 0;
        Long[] sums = new Long[nums.length];
        sums[0] = (long)nums[0];
        long ttl = (long) nums[0];
        sumsSet.add(ttl);
        for (int i = 1; i < nums.length; i++) {
            sums[i] = sums[i - 1] + nums[i];
            ttl += (long) nums[i];
            sumsSet.add(ttl);
        }
        Long[] arrFromSet = sumsSet.toArray(new Long[0]);
        Arrays.sort(arrFromSet);
        SegmentTree1D segmentTree = new SegmentTree1D(arrFromSet, 0, arrFromSet.length - 1); //index

        for (int i = nums.length - 1; i >= 0; i--) {
            segmentTree.increaseSTCount(ttl);
            ttl -= (long) nums[i];
            res += segmentTree.getCountWithRange((long)lower + ttl, (long)upper + ttl);
        }
        return res;
    }

    //Use SegmentTree1D
    public int countRangeSumSegmentTreeWOSortedArr(int[] nums, int lower, int upper) {
        if(nums == null || nums.length == 0) return 0;
        int res = 0;
        Long[] sums = new Long[nums.length];
        sums[0] = (long)nums[0];
        long ttl = (long) nums[0];
        for (int i = 1; i < nums.length; i++) {
            sums[i] = sums[i - 1] + nums[i];
            ttl += (long) nums[i];
        }
        SegmentTree1D segmentTree = new SegmentTree1D(sums, 0, sums.length - 1); //index

        for (int i = nums.length - 1; i >= 0; i--) {
            segmentTree.increaseSTCount(ttl);
            ttl -= (long) nums[i];
            res += segmentTree.getCountWithRange((long)lower + ttl, (long)upper + ttl);
        }
        return res;
    }

    public int countRangeSumMergeSort(int[] nums, int lower, int upper) {
        long[] sums = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return countMergeSumMergeSort(sums, lower, upper, 0, sums.length - 1);
    }
    private int countMergeSumMergeSort(long[] sums, int lower, int upper, int start, int end) {
        if (start >= end) return 0;
        int count = 0;
        int mid = start + (end - start) / 2;
        long[] cache = new long[end - start + 1];
        int cacheIdx = 0;
        int left =start, right = mid + 1, rangeLeft = mid + 1, rangeRight = mid + 1;

        count += countMergeSumMergeSort(sums, lower, upper, start, mid)
                + countMergeSumMergeSort(sums, lower, upper, mid + 1, end);

        while (left <= mid) {
            while (rangeRight <= end && sums[rangeRight] - sums[left] <= upper) rangeRight++;
            while (rangeLeft <= end && sums[rangeLeft] - sums[left] < lower) rangeLeft++;
            count += rangeRight - rangeLeft;
            while (right <= end && sums[right] <= sums[left]) {
                cache[cacheIdx++] = sums[right++];
            }
            cache[cacheIdx++] = sums[left++];
        }
        for(int i = 0; i < cacheIdx; i++) {
            sums[start + i] = cache[i];
        }
        return count;
    }

    public int naiveRangeSumWithBound(int[] nums, int lower, int upper) {
        long[] sums = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) { //start from index1
            sums[i + 1] = sums[i] + nums[i];
        }
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j <= nums.length; j++) {
                long rangeSum = sums[j] - sums[i];
                if (rangeSum >= lower && rangeSum <= upper) count++;
            }
        }
        return count;
    }
    //##############################################################################################
    @Test
    public void testCountPrime() {
        assertTrue(countPrime(2) == 1);
        assertTrue(countPrime(100) == 25);
    }

    public int countPrime(int n) {
        if ( n < 2 ) return 0;
        int res = n >>> 1;
        boolean[] isComposite = new boolean[n + 1]; //default False

        for (int i = 3; i * i < n; i += 2) { //ignore even number
            if (isComposite[i]) continue;
            for (int j = i * i; j < n; j+= 2 * i) {
                if (!isComposite[j]) {
                    isComposite[j] = true;
                    res--;
                }
            }
        }
        //just to print all prime numbers
        for (int i = 3; i < n; i++) {
            if (isComposite[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println(" ");
        return res;
    }

    @Test
    public void testNumSquares() {
        assertTrue(numSquares(12) == 3); //12 = 4 + 4 + 4
        assertTrue(numSquares(13) == 2); //13 = 4 + 9
        assertTrue(numSquares(5) == 2);
    }

    // Given a positive integer n, find the least number of perfect square numbers
    // (for example, 1, 4, 9, 16, ...) which sum to n.
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for(int i = 1; i <= n; ++i) {
            int min = Integer.MAX_VALUE;
            int j = 1;
            while(i - j*j >= 0) {
                min = Math.min(min, dp[i - j*j] + 1);
                ++j;
            }
            dp[i] = min;
            //printArray(dp);
        }
        return dp[n];
    }
    //##############################################################################################
    @Test
    public void testDiffWaysToCompute() {
//    Different Ways to Add Parentheses
//            (2*(3-(4*5))) = -34
//            ((2*3)-(4*5)) = -14
//            ((2*(3-4))*5) = -10
//            (2*((3-4)*5)) = -10
//            (((2*3)-4)*5) = 10
        String input = "2*3-4*5";
        Integer[] expected = new Integer[]{-34, -14, -10, -10, 10};
        List<Integer> actual = diffWaysToCompute(input);
        Collections.sort(actual, (a,b) ->(a - b));
        assertEquals(actual, Arrays.asList(expected));
    }

    public List<Integer> diffWaysToCompute(String input) {
        return diffWaysToCompute(input, new HashMap<>());
    }

    private List<Integer> diffWaysToCompute(String input, Map<String, List<Integer>> cache) {
        if (input.length() == 0) {
            return new ArrayList<>();
        }

        if (cache.containsKey(input)){
            return cache.get(input);
        }

        List<Integer> result = new ArrayList<>();
        // "2*3-4*5"
        for (int i = 0; i < input.length() - 1; i++){
            char c = input.charAt(i);
            if (!Character.isDigit(c)){
                List<Integer> leftList = diffWaysToCompute(input.substring(0, i), cache);
                List<Integer> rightList = diffWaysToCompute(input.substring(i + 1), cache);
                for (int left : leftList) {
                    for (int right: rightList) {
                        result.add(compute(left, right, c));
                    }
                }
            }
        }

        if (result.isEmpty()) {
            result.add(Integer.valueOf(input));
        }
        cache.put(input, result);
        return result;
    }

    private int compute(int left, int right, char c) {
        switch (c) {
            case '+':
                //System.out.println( "(" + left + " + " + right + ")");
                return left + right;
            case '-':
                //System.out.println( "(" + left + " - " + right + ")");
                return left - right;
            case '*':
                //System.out.println( "(" + left + " * " + right + ")");
                return left * right;
            default:
                throw new IllegalArgumentException("Invalid operator:" + c);
        }
    }

    // Given a string that contains only digits 0-9 and a target value,
// return all possibilities to add binary operators (not unary) +, -, or *
// between the digits so they evaluate to the target value.
//
//    Examples:
//
//            "123", 6 -> ["1+2+3", "1*2*3"]
//            "232", 8 -> ["2*3+2", "2+3*2"]
//            "105", 5 -> ["1*0+5","10-5"]
//            "00", 0 -> ["0+0", "0-0", "0*0"]
//            "3456237490", 9191 -> []
    public class OperatorsCombination {
        public List<String> addOperators(String num, int target) {
            List<String> result = new ArrayList<>();
            addOperators(num, target, 0, result, new StringBuilder(), 0, 0);
            return result;
        }

        private void addOperators(String num, long target, int index, List<String> result, StringBuilder sb, long curNum, long lastNum) {
            if (index == num.length()) {
                if (curNum == target) {
                    result.add(sb.toString());
                }
                return;
            }
            int len = sb.length();
            long cur = 0;
            for (int i = index; i < num.length(); i++) {
                cur = cur * 10 + num.charAt(i) - '0';
                if (len == 0) {
                    addOperators(num, target, i + 1, result, sb.append(cur), cur, cur);
                    sb.setLength(len);
                } else {
                    addOperators(num, target, i + 1, result, sb.append('+').append(cur), curNum + cur, cur);
                    sb.setLength(len);
                    addOperators(num, target, i + 1, result, sb.append('-').append(cur), curNum - cur, -cur);
                    sb.setLength(len);
                    addOperators(num, target, i + 1, result, sb.append('*').append(cur), curNum - lastNum + lastNum * cur, lastNum * cur);
                    sb.setLength(len);
                }
                if (num.charAt(index) == '0') {
                    break;
                }
            }
        }
    }
    //##############################################################################################
    public class PasswordChecker {
        // It has at least 6 characters and at most 20 characters.
        // It must contain at least one lowercase letter, at least one uppercase letter, and at least one digit.
        // It must NOT contain three repeating characters in a row
        // ("...aaa..." is weak, but "...aa...a..." is strong, assuming other conditions are met).

        public int strongPasswordChecker(String s) {
            int res = 0, a = 1, A = 1, d = 1;
            char[] carr = s.toCharArray();
            int[] arr = new int[carr.length];

            for (int i = 0; i < arr.length;) {
                if (Character.isLowerCase(carr[i])) a = 0;
                if (Character.isUpperCase(carr[i])) A = 0;
                if (Character.isDigit(carr[i])) d = 0;

                int j = i;
                while (i < carr.length && carr[i] == carr[j]) i++;
                arr[j] = i - j;
            }

            int total_missing = (a + A + d);

            if (arr.length < 6) {
                res += total_missing + Math.max(0, 6 - (arr.length + total_missing));

            } else {
                int over_len = Math.max(arr.length - 20, 0), left_over = 0;
                res += over_len;

                for (int k = 1; k < 3; k++) {
                    for (int i = 0; i < arr.length && over_len > 0; i++) {
                        if (arr[i] < 3 || arr[i] % 3 != (k - 1)) continue;
                        arr[i] -= Math.min(over_len, k);
                        over_len -= k;
                    }
                }

                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] >= 3 && over_len > 0) {
                        int need = arr[i] - 2;
                        arr[i] -= over_len;
                        over_len -= need;
                    }
                    if (arr[i] >= 3) left_over += arr[i] / 3;
                }
                res += Math.max(total_missing, left_over);
            }
            return res;
        }
    }

    //##############################################################################################
//    # Link to check regex:
//    http://regexr.com/
//    http://www.regular-expressions.info/refcapture.html
//
//    Tag validation:
//    Input: "<DIV>  unmatched <  </DIV>"
//    Output: False
//
//    Input: "<DIV> closed tags with invalid tag name  <b>123</b> </DIV>"
//    Output: False
//
//    Input: "<DIV> unmatched tags with invalid tag name  </1234567890> and <CDATA[[]]>  </DIV>"
//    Output: False
//
//    Input: "<DIV>  unmatched start tag <B>  and unmatched end tag </C>  </DIV>"
//    Output: False

    public class TagValidator {
        Stack < String > stack = new Stack < > ();
        boolean contains_tag = false;
        public boolean isValidTagName(String s, boolean ending) {
            if (ending) {
                if (!stack.isEmpty() && stack.peek().equals(s))
                    stack.pop();
                else
                    return false;
            } else {
                contains_tag = true;
                stack.push(s);
            }
            return true;
        }
        public boolean isValid(String code) {
            String regex = "<[A-Z]{0,9}>([^<]*(<((\\/?[A-Z]{1,9}>)|(!\\[CDATA\\[(.*?)]]>)))?)*";
            if (!Pattern.matches(regex, code))
                return false;
            for (int i = 0; i < code.length(); i++) {
                boolean ending = false;
                if (stack.isEmpty() && contains_tag)
                    return false;
                if (code.charAt(i) == '<') {
                    if (code.charAt(i + 1) == '!') {
                        i = code.indexOf("]]>", i + 1);
                        continue;
                    }
                    if (code.charAt(i + 1) == '/') {
                        i++;
                        ending = true;
                    }
                    int closeindex = code.indexOf('>', i + 1);
                    if (closeindex < 0 || !isValidTagName(code.substring(i + 1, closeindex), ending))
                        return false;
                    i = closeindex;
                }
            }
            return stack.isEmpty();
        }

        //Use regex directly to validate if tag is properly closed
        public boolean isValidRegex(String code) {
            if (code.equals("t")) return false;
            code = code.replaceAll("<!\\[CDATA\\[.*?\\]\\]>", "c");

            String prev = "";
            while (!code.equals(prev)) {
                prev = code;
                code = code.replaceAll("<([A-Z]{1,9})>[^<]*</\\1>", "t");
            }

            return code.equals("t");
        }
    }
    //##############################################################################################
//    Input:"5/3+1/3"
//    Output: "2/1"
//    Input:"-1/2+1/2+1/3"
//    Output: "1/3"
    public class GCD_LCM {
        public String fractionAddition(String expression) {
            List < Character > sign = new ArrayList < > ();
            for (int i = 1; i < expression.length(); i++) {
                if (expression.charAt(i) == '+' || expression.charAt(i) == '-')
                    sign.add(expression.charAt(i));
            }
            List < Integer > num = new ArrayList < > ();
            List < Integer > den = new ArrayList < > ();
            for (String sub: expression.split("\\+")) {
                for (String subsub: sub.split("-")) {
                    if (subsub.length() > 0) {
                        String[] fraction = subsub.split("/");
                        num.add(Integer.parseInt(fraction[0]));
                        den.add(Integer.parseInt(fraction[1]));
                    }
                }
            }
            if (expression.charAt(0) == '-')
                num.set(0, -num.get(0));
            int lcm = 1;
            for (int x: den) {
                lcm = lcm_(lcm, x);
            }

            int res = lcm / den.get(0) * num.get(0);
            for (int i = 1; i < num.size(); i++) {
                if (sign.get(i - 1) == '+')
                    res += lcm / den.get(i) * num.get(i);
                else
                    res -= lcm / den.get(i) * num.get(i);
            }
            int g = gcd(Math.abs(res), Math.abs(lcm));
            return (res / g) + "/" + (lcm / g);
        }
        public int lcm_(int a, int b) {
            return a * b / gcd(a, b);
        }
        public int gcd(int a, int b) {
            while (b != 0) {
                int t = b;
                b = a % b;
                a = t;
            }
            return a;
        }
    }

    //##############################################################################################
    //Validate ipv4 vs ipv6

    public class IPV4V6 {
        public String validIPAddress(String IP) {
            if (isValidIPv4(IP)) return "IPv4";
            else if (isValidIPv6(IP)) return "IPv6";
            else return "Neither";
        }

        public boolean isValidIPv4(String ip) {
            if (ip.length()<7) return false;
            if (ip.charAt(0)=='.') return false;
            if (ip.charAt(ip.length()-1)=='.') return false;
            String[] tokens = ip.split("\\.");
            if (tokens.length!=4) return false;
            for(String token:tokens) {
                if (!isValidIPv4Token(token)) return false;
            }
            return true;
        }

        public boolean isValidIPv4Token(String token) {
            if (token.startsWith("0") && token.length()>1) return false;
            try {
                int parsedInt = Integer.parseInt(token);
                if (parsedInt<0 || parsedInt>255) return false;
                if (parsedInt==0 && token.charAt(0)!='0') return false;
            } catch(NumberFormatException nfe) {
                return false;
            }
            return true;
        }

        public boolean isValidIPv6(String ip) {
            if (ip.length()<15) return false;
            if (ip.charAt(0)==':') return false;
            if (ip.charAt(ip.length()-1)==':') return false;
            String[] tokens = ip.split(":");
            if (tokens.length!=8) return false;
            for(String token: tokens) {
                if (!isValidIPv6Token(token)) return false;
            }
            return true;
        }

        public boolean isValidIPv6Token(String token) {
            if (token.length()>4 || token.length()==0) return false;
            char[] chars = token.toCharArray();
            for(char c:chars) {
                boolean isDigit = c>=48 && c<=57;
                boolean isUppercaseAF = c>=65 && c<=70;
                boolean isLowerCaseAF = c>=97 && c<=102;
                if (!(isDigit || isUppercaseAF || isLowerCaseAF))
                    return false;
            }
            return true;
        }
    }
    //##############################################################################################
//    # Example 1:
//            #
//            # rectangles = [
//            #   [1,1,3,3],
//            #   [3,1,4,2],
//            #   [3,2,4,4],
//            #   [1,3,2,4],
//            #   [2,3,3,4]
//            # ]
//            #
//            # Return true. All 5 rectangles together form an exact cover of a rectangular region.
//            #
//            # Example 2:
//            #
//            # rectangles = [
//            #   [1,1,2,3],
//            #   [1,3,2,4],
//            #   [3,1,4,2],
//            #   [3,2,4,4]
//            # ]
//            #
//            # Return false. Because there is a gap between the two rectangular regions.
//            #
//            # Example 3:
//            #
//            # rectangles = [
//            #   [1,1,3,3],
//            #   [3,1,4,2],
//            #   [1,3,2,4],
//            #   [3,2,4,4]
//            # ]
//            #
//            # Return false. Because there is a gap in the top center.
//#
//        # Example 4:
//            #
//            # rectangles = [
//            #   [1,1,3,3],
//            #   [3,1,4,2],
//            #   [1,3,2,4],
//            #   [2,2,4,4]
//            # ]
//            #
//            # Return false. Because two of the rectangles overlap with each other.
//    sweep line solution
//    Standard sweep line solution.
//    Basic idea:
//    Sort by x-coordinate.
//    Insert y-interval into TreeSet, and check if there are intersections.
//    Delete y-interval.


    public class Event implements Comparable<Event> {
        int time;
        int[] rect;

        public Event(int time, int[] rect) {
            this.time = time;
            this.rect = rect;
        }

        public int compareTo(Event that) {
            if (this.time != that.time) return this.time - that.time;
            else return this.rect[0] - that.rect[0];
        }
    }

    public boolean isRectangleCover(int[][] rectangles) {
        PriorityQueue<Event> pq = new PriorityQueue<Event> ();
        // border of y-intervals
        int[] border= {Integer.MAX_VALUE, Integer.MIN_VALUE};
        for (int[] rect : rectangles) {
            Event e1 = new Event(rect[0], rect);
            Event e2 = new Event(rect[2], rect);
            pq.add(e1);
            pq.add(e2);
            if (rect[1] < border[0]) border[0] = rect[1];
            if (rect[3] > border[1]) border[1] = rect[3];
        }
        TreeSet<int[]> set = new TreeSet<int[]> (new Comparator<int[]>() {
            @Override
            // if two y-intervals intersects, return 0
            public int compare (int[] rect1, int[] rect2) {
                if (rect1[3] <= rect2[1]) return -1;
                else if (rect2[3] <= rect1[1]) return 1;
                else return 0;
            }
        });
        int yRange = 0;
        while (!pq.isEmpty()) {
            int time = pq.peek().time;
            while (!pq.isEmpty() && pq.peek().time == time) {
                Event e = pq.poll();
                int[] rect = e.rect;
                if (time == rect[2]) {
                    set.remove(rect);
                    yRange -= rect[3] - rect[1];
                } else {
                    if (!set.add(rect)) return false;
                    yRange += rect[3] - rect[1];
                }
            }
            // check intervals' range
            if (!pq.isEmpty() && yRange != border[1] - border[0]) {
                return false;
                //if (set.isEmpty()) return false;
                //if (yRange != border[1] - border[0]) return false;
            }
        }
        return true;
    }
    //##############################################################################################
//    Input:
//            [[0,1,10], [2,0,5]]
//    Output:
//            2
//    Explanation:
//    Person #0 gave person #1 $10.
//            Person #2 gave person #0 $5.
//
//    Two transactions are needed. One way to settle the debt is person #1 pays person #0 and #2 $5 each.
//    Example 2:
//
//    Input:
//            [[0,1,10], [1,0,1], [1,2,5], [2,0,5]]
//
//    Output:
//            1
//
//    Explanation:
//    Person #0 gave person #1 $10.
//            Person #1 gave person #0 $1.
//            Person #1 gave person #2 $5.
//            Person #2 gave person #0 $5.
//
//            Therefore, person #1 only need to give person #0 $4, and all debt is settled.
    public class Venmo {
        public int minTransfers(int[][] transactions) {
            if(transactions == null || transactions.length == 0) return 0;
            Map<Integer, Integer> acc = new HashMap<>();
            for(int i = 0;i<transactions.length;i++){
                int id1 = transactions[i][0];
                int id2 = transactions[i][1];
                int m = transactions[i][2];
                acc.put(id1, acc.getOrDefault(id1, 0)-m);
                acc.put(id2, acc.getOrDefault(id2, 0)+m);
            }
            List<Integer> negs = new ArrayList<>();
            List<Integer> poss = new ArrayList<>();
            for(Integer key:acc.keySet()){
                int m = acc.get(key);
                if(m == 0) continue;
                if(m<0) negs.add(-m);
                else poss.add(m);
            }
            int ans = Integer.MAX_VALUE;
            Stack<Integer> stNeg = new Stack<>(), stPos = new Stack<>();
            for(int i =0;i<1000;i++){
                for(Integer num:negs) stNeg.push(num);
                for(Integer num:poss) stPos.push(num);
                int cur = 0;
                while(!stNeg.isEmpty()){
                    int n = stNeg.pop();
                    int p = stPos.pop();
                    cur++;
                    if(n == p) continue;
                    if(n>p){
                        stNeg.push(n-p);
                    } else {
                        stPos.push(p-n);
                    }
                }
                ans = Math.min(ans, cur);
                Collections.shuffle(negs);
                Collections.shuffle(poss);
            }
            return ans;
        }

    }
    //##############################################################################################
    //skyline:
    public List<int[]> getSkyline(int[][] buildings) {
        List<int[]> result = new ArrayList<>();
        List<int[]> height = new ArrayList<>();
        for(int[] b:buildings) {
            // start point has negative height value
            height.add(new int[]{b[0], -b[2]});
            // end point has normal height value
            height.add(new int[]{b[1], b[2]});
        }

        // sort $height, based on the first value, if necessary, use the second to
        // break ties
        Collections.sort(height, (a, b) -> {
            if(a[0] != b[0])
                return a[0] - b[0];
            return a[1] - b[1];
        });

        // Use a maxHeap to store possible heights
        Queue<Integer> pq = new PriorityQueue<>((a, b) -> (b - a));

        // Provide a initial value to make it more consistent
        pq.offer(0);

        // Before starting, the previous max height is 0;
        int prev = 0;

        // visit all points in order
        for(int[] h:height) {
            if(h[1] < 0) { // a start point, add height
                pq.offer(-h[1]);
            } else {  // a end point, remove height
                pq.remove(h[1]);
            }
            int cur = pq.peek(); // current max height;

            // compare current max height with previous max height, update result and
            // previous max height if necessary
            if(prev != cur) {
                result.add(new int[]{h[0], cur});
                prev = cur;
            }
        }
        return result;
    }

    //Divide and Conquer:
    public List<int[]> getSkylineDivideConquer(int[][] buildings) {
        List<int[]> result = new ArrayList<>();
        Wall[] walls = new Wall[buildings.length * 2]; // x 2
        SortedMap<Integer, Integer> heightMap = new TreeMap<>();
        int index = 0;
        int curHeight = 0;
        for (int i = 0; i < buildings.length; i++) {
            walls[i * 2] = new Wall(buildings[i][0], buildings[i][2]);
            walls[(i * 2) + 1] = new Wall(buildings[i][1], -buildings[i][2]); //end point
        }
        Arrays.sort(walls);
        while (index < walls.length) {
            do {// given same x, merge the same y
                if (walls[index].height > 0) { //start point
                    heightMap.put(walls[index].height, heightMap.getOrDefault(walls[index].height, 0) + 1);
                } else { //end point
                    Integer val = heightMap.get(-walls[index].height);
                    if (val == 1) {
                        heightMap.remove(-walls[index].height);
                    } else {
                        heightMap.put(-walls[index].height, val - 1);
                    }
                }
                index++;
            } while (index < walls.length && walls[index].position == walls[index - 1].position);
            int maxHeight = heightMap.isEmpty() ? 0 : heightMap.lastKey();
            if (curHeight != maxHeight) {
                result.add(new int[] {walls[index - 1].position, maxHeight});
                curHeight = maxHeight;
            }
        }
        return result;

    }

    private class Wall implements Comparable<Wall>{
        private int position;
        private int height;

        public Wall(int position, int height) {
            this.position = position;
            this.height = height;
        }

        @Override
        public int compareTo(Wall other) {
            if (other == null) {
                return 0;
            }
            return Integer.compare(this.position, other.position);
        }
    }
    //##############################################################################################
//    Given a non-empty string s and a dictionary wordDict containing a list of non-empty words,
// determine if s can be segmented into a space-separated sequence of one or more dictionary words.
// You may assume the dictionary does not contain duplicate words.
//
//    For example, given
//    s = "leetcode",
//    dict = ["leet", "code"].
//
//    Return true because "leetcode" can be segmented as "leet code".
//
//    DP:
    public boolean wordBreak(String s, Set<String> dict) {
        boolean[] f = new boolean[s.length() + 1];
        f[0] = true;

        //Second DP
        for(int i=1; i <= s.length(); i++){
            for(int j=0; j < i; j++){
                if(f[j] && dict.contains(s.substring(j, i))){
                    f[i] = true;
                    break;
                }
            }
        }
        return f[s.length()];
    }


//    Word break II
//    Given a non-empty string s and a dictionary wordDict containing a list of non-empty words,
// add spaces in s to construct a sentence where each word is a valid dictionary word.
// You may assume the dictionary does not contain duplicate words.
//
//    Return all such possible sentences.
//
//            For example, given
//    s = "catsanddog",
//    dict = ["cat", "cats", "and", "sand", "dog"].
//
//    A solution is ["cats and dog", "cat sand dog"].
//
//    Using DFS directly will lead to TLE, so I just used HashMap to save the previous
// results to prune duplicated branches, as the following:

    public List<String> wordBreak2(String s, Set<String> wordDict) {
        return DFS(s, wordDict, new HashMap<String, LinkedList<String>>());
    }

    // DFS function returns an array including all substrings derived from s.
    List<String> DFS(String s, Set<String> wordDict, HashMap<String, LinkedList<String>>map) {
        if (map.containsKey(s))
            return map.get(s);

        LinkedList<String>res = new LinkedList<String>();
        if (s.length() == 0) {
            res.add("");
            return res;
        }
        for (String word : wordDict) {
            if (s.startsWith(word)) {
                List<String>sublist = DFS(s.substring(word.length()), wordDict, map);
                for (String sub : sublist)
                    res.add(word + (sub.isEmpty() ? "" : " ") + sub);
            }
        }
        map.put(s, res);
        return res;
    }
    //##############################################################################################
    public class TextJus {
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
                if (end - start == 0) { //word length < required width
                    throw new IllegalArgumentException(String.format("%s > maxWidth %s", words[end], maxWidth));

                } else if (end - start == 1) { //add only one word in a line
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

        public List<String> fullJustify2(String[] words, int maxWidth) {
            List<String> result = new ArrayList<>();
            int start = 0;
            int end = 0;
            while (end < words.length) {
                int len = 0;
                while (end < words.length && len + words[end].length() <= maxWidth) {
                    len += words[end++].length() + 1;
                }
                len--;
                StringBuilder sb = new StringBuilder();
                if (end == words.length || end - start == 1) {
                    for (int i = start; i < end; i++) {
                        sb.append(words[i]).append(' ');
                    }
                    sb.setLength(sb.length() - 1);
                    addSpaces(sb, maxWidth - sb.length());
                } else {
                    int numIntervals = end - start - 1;
                    int spaces = numIntervals + (maxWidth - len);
                    for (int i = start; i < end - 1; i++) {
                        sb.append(words[i]);
                        addSpaces(sb, spaces / numIntervals + (spaces % numIntervals > i - start ? 1 : 0));
                    }
                    sb.append(words[end - 1]);
                }
                start = end;
                result.add(sb.toString());
            }
            return result;
        }

        private void addSpaces(StringBuilder sb, int count) {
            for (int i = 0; i < count; i++) {
                sb.append(' ');
            }
        }

    }



    //##############################################################################################

}


