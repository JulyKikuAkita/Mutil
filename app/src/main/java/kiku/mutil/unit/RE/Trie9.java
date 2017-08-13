package kiku.mutil.unit.RE;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

class Trie {
    String value;
    public Trie[] children;
    List<String> words;

    public Trie() {
        children = new Trie[10]; // 10 digits
        words = new LinkedList<String>();

    }

    public static List<String> getWords(String t9, Trie trie) {
        //  find the trie
        // return the words
        for (int i = 0; i < t9.length(); i++) {
            int digit = Integer.parseInt(t9.substring(i, i + 1));
            if (trie.children[digit] == null) {
                trie.children[digit] = new Trie();
            }
            trie = trie.children[digit];
        }

        return trie.words;
    }

    public static String stringTot9(String word) {
        word = word.toLowerCase();
        StringBuilder sb = new StringBuilder(word.length());

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int value;
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

    public static void addWord(String word, Trie trie) {
        //find the trie where the word should be
        if (word == null)
            return;
        String t9 = stringTot9(word);
        for (int i = 0; i < t9.length(); i++) {
            int digit = Integer.parseInt(t9.substring(i, i + 1));
            if (trie.children[digit] == null) {
                trie.children[digit] = new Trie();
            }
            trie = trie.children[digit];
        }
        trie.words.add(word);
    }
}

/*
 * load dictionary for each word, get its t9 digits
 *
 */

public class Trie9 {
    /*
     * https://github.com/eginhard/puzzles/blob/master/hackerrang/t9.java
     */
    public static void main(String[] args) {
        Trie9 test = new Trie9("src/t9/dictionary.txt", "src/t9/t9-test-input.txt");
        test.loadDictionary();
        test.getOutput();
    }

    Trie root;
    String dictionary;
    String inputfile;

    public Trie9(String dictionary, String inputfile) {
        root = new Trie();
        this.dictionary = dictionary;
        this.inputfile = inputfile;
    }

    public void getOutput() {
        LinkedList<Integer> digits = new LinkedList<Integer>();
        try {
            File file = new File(inputfile);
            FileReader fileReader = new FileReader(inputfile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line.trim();
                List<String> result = Trie.getWords(line, root);
                if (result == null||result.size()==0) {
                    System.out.println(line + ": <No Results>");

                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String s : result) {
                        sb.append(s + ",");
                    }
                    sb.deleteCharAt(sb.length()-1);
                    System.out.println(line + ": " + sb);
                }
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i : digits) {
        }

    }

    public void loadDictionary() {
        // build the trie

        try {
            File file = new File(dictionary);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line.trim();
                Trie.addWord(line, root);
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
