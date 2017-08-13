package kiku.mutil.unit.RE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class TrieHer {
	/*
	 * find five auto complete order by frequency
	 *
	 */

        // auto complete with trie
        // https://github.com/shirleyyoung0812/Google-interview-questions/blob/master/src/Trie/AutoCompleteTrie.java
	/*
	 * simple trie performance 75% complex trie stores highest scoring 5
	 * results on each node/ call to fetch words based on a prefix should be
	 * O(len of prefix) precomputing
	 * retrieval is efficient
	 */
        public static void main(String[] args) {
            TrieHer test = new TrieHer("src/main/res/text.txt");
            System.out.println(test.getAutoComplete("abc"));
/*
		// testing non-alphabetical chars
		AutoComplete test2 = new AutoComplete("src/autocomplete_test2.txt");
		System.out.println(test2.getAutoComplete("a"));

		// empty input
		AutoComplete test3 = new AutoComplete("src/autocomplete_test3.txt");
		System.out.println(test3.getAutoComplete("ab"));

		// capital letters
		AutoComplete test4 = new AutoComplete("src/autocomplete_test4.txt");
		System.out.println(test4.getAutoComplete("aB"));
*/

        }

        HashMap<String, Integer> map;
        TrieNode root;

        public TrieHer(String input) {
            root = new TrieNode();
            map = new HashMap<String, Integer>();

            loadFile(input);
            insertWords();
            precompute(root);
        }

        public void precompute(TrieNode temp) {
            for (char c : temp.children.keySet()) {
                TrieNode child = temp.children.get(c);
                child.compute(map);
                precompute(child);
            }
        }

        private void insertWords() {
            for (String key : map.keySet()) {
                System.out.println(key + " frequency: " + map.get(key));
                insert(key);
            }
        }

        private void insert(String word) {
            if (word == null)
                return;
            TrieNode temp = root;
            String prefix = "";
            for (char c : word.toCharArray()) {
                prefix = prefix + c;
                if (!temp.children.containsKey(c)) {
                    temp.children.put(c, new TrieNode(prefix));
                }
                temp = temp.children.get(c);
            }
            temp.isWord = true;

        }

        public Collection<String> getAutoComplete(String prefix) {
            TrieNode temp = root;
            for (char c : prefix.toCharArray()) {
                if (!temp.children.containsKey(c)) {
                    return Collections.emptyList();
                }
                temp = temp.children.get(c);
            }
            return temp.result;
        }

        private boolean search(String word) {
            TrieNode temp = root;

            for (char c : word.toCharArray()) {
                if (!temp.children.containsKey(c)) {
                    return false;
                }
                temp = temp.children.get(c);
            }
            return temp.value.equals(word);
        }

        public void loadFile(String input) {

            try {
                File file = new File(input);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line.trim();
                    if (map.containsKey(line)) {
                        int f = map.get(line);
                        f++;
                        map.put(line, f);
                    } else {
                        map.put(line, 1);
                    }

                }
                fileReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Pair {
        String word;
        int freq;

        public Pair(String str, int f) {
            this.freq = f;
            this.word = str;
        }
    }

    class TrieNodeComparator implements Comparator<Pair> {

        @Override
        public int compare(Pair o1, Pair o2) {
            // TODO Auto-generated method stub
            return o2.freq - o1.freq;
        }

    }

    class TrieNode {
        boolean isWord;
        String value;
        HashMap<Character, TrieNode> children;
        List<String> result;

        //need to edit the compute method
        public void compute(HashMap<String, Integer> map) {
            PriorityQueue<Pair> words = new PriorityQueue<Pair>(5, new Comparator<Pair>() {
                @Override
                public int compare(Pair o1, Pair o2) {
                    return o2.freq - o1.freq;
                }
            });

            for (String child : this.getAllChildren()) {
                words.add(new Pair(child, map.get(child)));

            }
            int count = 0;
            while (!words.isEmpty() && count < 5) {
                result.add(words.poll().word);
                count++;
            }
        }

        public TrieNode() {
            value = "";
            isWord = false;
            children = new HashMap<Character, TrieNode>();
            result = new LinkedList<String>();
        }

        public TrieNode(String value) {
            this.value = value;
            isWord = false;
            children = new HashMap<Character, TrieNode>();
            result = new LinkedList<String>();

        }

        public Collection<String> getAllChildren() {
            List<String> result = new ArrayList<String>();
            if (this.isWord)
                result.add(this.value);// only add word
            for (char c : children.keySet()) {
                TrieNode child = children.get(c);
                Collection<String> childrenprefix = child.getAllChildren();
                result.addAll(childrenprefix);
            }
            return result;
        }
    }

