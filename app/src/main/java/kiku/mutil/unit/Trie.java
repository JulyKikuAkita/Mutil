package kiku.mutil.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

// Your Trie object will be instantiated and called as such:
// Trie trie = new Trie();
// trie.insert("somestring");
// trie.search("key");
public class Trie {
    private final static int ALPHABET_SIZE = 26;
    private TrieNode mRoot;
    // the t9 mapped array which maps number to string on the typing board
    private String[] t9 = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public Trie() {
        mRoot = new TrieNode();
    }

    // breadth first search for a number string use queue
    public List<String> getPossibleWordWithNumberMapping(String strNum) { //867
        Queue<String> q = new LinkedList<>();
        List<WordFreq> res = new ArrayList<>();
        q.add("");
        for (char digit : strNum.toCharArray()) {
            if (Character.isDigit(digit)) {
                int level = q.size();
                for (int i = 0; i < level; i++) {
                    String leftPart = q.remove();
                    for (char c : t9[digit - '0'].toCharArray()) {
                        String tmp = leftPart + c;
                        TrieNode node = findNode(tmp);
                        if ( node != null) {
                            if (tmp.length() == strNum.length() && node.mIsEnd) {
                                res.add(node.mWordFreq);
                            } else {
                                q.add(tmp);
                            }
                        }
                    }
                }
            }
        }
        return getWordByFrequencyDesc(res);
    }

    private List<String> getWordByFrequencyDesc(List<WordFreq> list) {
        List<String> res = new ArrayList<>();
        Collections.sort(list, (a, b) -> (b.mFrequency - a.mFrequency));
        for (WordFreq wf : list) {
            //res.add(wf.mWord + " " + wf.mFrequency);
            res.add(wf.mWord);
        }
        if (res.size() == 0) {
            res.add("< No Result>");
        }
        return res;
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        word = alterToLettersOnly(word);
        TrieNode cur = mRoot;
        for ( char c : word.toCharArray()) {
            int idx = c - 'a';
            if (cur.mChildren[idx] == null) {
                cur.mChildren[idx] = new TrieNode();
            }
            cur = cur.mChildren[idx];
        }
        cur.mIsEnd = true;
        cur.mFrequency ++;
        cur.mWordFreq = new WordFreq(word, cur.mFrequency);
    }

    // Returns if the word is in the trie.
    public boolean searchWord(String word) {
        return findNode(word) != null && findNode(word).mIsEnd;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    public List<String> getAllWordWithPrefix(String prefix) {
        String a_prefix= alterToLettersOnly(prefix);
        List<String> res = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>();
        TrieNode node = findNode(a_prefix);
        if (node != null){
            //dfs find all match by key
            findAllMatchNodes(node, res, new StringBuilder(), a_prefix);
            return res;
        } else {
            return null;
        }
    }

    /**
     * return top k frequency word by given prefix
     */
    public List<String> getAllMatchFreqWithPrefix(String prefix, int k) {
        String a_prefix= alterToLettersOnly(prefix);
        Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>((a, b) -> (b - a)); //desc
        TrieNode node = findNode(a_prefix);
        List<String> res = new ArrayList<>();

        if (node != null){
            //dfs find all match by key
            findAllMatchNodesByFreq(node, map, new StringBuilder(), a_prefix);
            for(int freq : map.keySet()) {
                for( String s : map.get(freq)) {
                    res.add(s + " " + freq);
                }
                k--;
                if (k == 0) break;
            }
        }
        return res;
    }

    private void findAllMatchNodesByFreq(TrieNode node, Map<Integer, List<String>> res, StringBuilder sb, String prefix) {
        if (node == null) return;
        if (node.mIsEnd) {
            res.putIfAbsent(node.mFrequency, new ArrayList<>());
            res.get(node.mFrequency).add(prefix + sb.toString());
            // do not return here, each end word may not necessary be leaf
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.mChildren[i] != null) {
                sb.append((char) ('a' + i));
                findAllMatchNodesByFreq(node.mChildren[i], res, sb, prefix);
                sb.setLength(sb.length() - 1);
            }
        }
    }

    private void findAllMatchNodes(TrieNode node, List<String> res, StringBuilder sb, String prefix) {
        if (node == null) return;
        if (node.mIsEnd) {
            //res.add(prefix + sb.toString());
            res.add(node.mWordFreq.mWord);
        }
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.mChildren[i] != null) {
                //sb.append((char) ('a' + i));
                findAllMatchNodes(node.mChildren[i], res, sb, prefix);
                //sb.setLength(sb.length() - 1);
            }
        }
    }

    private TrieNode findNode(String str) {
        TrieNode cur = mRoot;
        for (char c : str.toCharArray()) {
            cur = cur.mChildren[c - 'a'];
            if (cur == null) return null;
        }
        return cur;
    }

    private void deleteNode(TrieNode node) {
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.mChildren != null) {
                deleteNode(node.mChildren[i]);
            }
        }
        node = null;
    }

    //##############################################################################################
    /**
     * Just a helper to format string to be ready to insert to Trie Node
     * Here limit to ASCII encoding, not considering UNICODE
     * https://stackoverflow.com/questions/18304804/what-is-the-difference-between-character-isalphabetic-and-character-isletter-in
     */
    public String alterToLettersOnly(String testName) {
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
    //##############################################################################################

    class TrieNode {
        TrieNode[] mChildren;
        boolean mIsEnd;
        int mFrequency;
        WordFreq mWordFreq;

        public TrieNode() {
            //not support upper case as A-Z with 065- 090, a-z with 097-122
            mChildren = new TrieNode[ALPHABET_SIZE];
            mIsEnd = false;
            mFrequency = 0;
            mWordFreq = null;
        }
    }

    class WordFreq implements Comparable<WordFreq>{
        String mWord;
        int mFrequency;

        public WordFreq(String word, int freq) {
            mWord = word;
            mFrequency = freq;
        }

        @Override
        public int compareTo(WordFreq that) {
            return that.mFrequency - this.mFrequency;  //desc
        }
    }
}
