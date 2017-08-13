package kiku.mutil.unit;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DigitTrie {
    DNode root;

    public DigitTrie() {
        root = new DNode();
    }

    public void insert(String word) {
        String digit = wordToNumber(word);
        DNode cur = root;
        for (char c : digit.toCharArray()) {
            if (cur.child[c - '0'] == null) {
                cur.child[c - '0'] = new DNode();
            }
            cur = cur.child[c - '0'];
        }
        cur.isEnd = true;
        cur.words.add(word);
    }

    private String wordToNumber(String word) {
        StringBuilder sb = new StringBuilder();
        int value = 0;
        for (char c : word.toCharArray()) {
            if (c < 'a' || c > 'z')
                return sb.toString();
            else if (c >= 'a' && c <= 'c')
                value = 2;
            else if (c >= 'd' && c <= 'f')
                value = 3;
            else if (c >= 'g' && c <= 'i')
                value = 4;
            else if (c >= 'j' && c <= 'l')
                value = 5;
            else if (c >= 'm' && c <= 'o')
                value = 6;
            else if (c >= 'p' && c <= 's')
                value = 7;
            else if (c >= 't' && c <= 'v')
                value = 8;
            else // if (c >= 'w' && c <= 'z')
                value = 9;
            sb.append(value);
        }
        return sb.toString();
    }

    public Set<String> search(String number) {
        DNode cur = root;
        for (char c : number.toCharArray()) {
            if (cur.child[c - '0'] == null) return Collections.EMPTY_SET;
            cur = cur.child[c - '0'];
        }
        return cur.words;
    }

    class DNode{
        DNode[] child;
        Set<String> words;
        boolean isEnd;

        public DNode() {
            child = new DNode[10];
            words = new HashSet<>();
            isEnd = false;
        }
    }
}
