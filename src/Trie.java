import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';

    TrieNode root;

    private class TrieNode {
        // TODO: Create your TrieNode class here.
        boolean endFlag = false;
        boolean[] presentChars = new boolean[62];
        TrieNode[] children = new TrieNode[62];

        public TrieNode () {

        }

        public boolean hasNext(int i) {
            return presentChars[i];
        }

        public void addNode(int i) {
            TrieNode temp = new TrieNode();
            temp.endFlag = false;
            presentChars[i] = true;
            children[i] = temp;
        }
    }
    public Trie() {
        // TODO: Initialise a trie class here.
        root = new TrieNode();
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // TODO
        TrieNode curr = root;
        TrieNode prev = root;
        int i = 0;
        while(i < s.length()) {
            int pos = charPos(s.charAt(i));
            if(!curr.hasNext(pos)) {
                curr.addNode(pos);
            }
            prev = curr;
            curr = curr.children[pos];
            i++;
        }
        prev.endFlag = true;
    }

    int charPos(char c) {
        if(c < '9') {
            return c -  '0';
        } else if (c < 'Z') {
            return c - '0' - 8;
        } else {
            return  c - '0' - 13;
        }
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        TrieNode curr = root;
        TrieNode prev = root;
        int i = 0;
        while (i < s.length()) {
            int pos = charPos(s.charAt(i));
            if(!curr.hasNext(pos)) {
                return false;
            }
            prev = curr;
            curr = curr.children[pos];
            i++;
        }
        return prev.endFlag;
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        // TODO
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("0");
        t.insert("z");
        /*
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);

         */
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
