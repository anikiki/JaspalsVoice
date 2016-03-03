package uk.co.jaspalsvoice.jv;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cristi on 8/8/2015.
 */
public class TrieT9 {

    private static class Node {
        char c;
        boolean isEndOfWord;
        List<Node> children = new LinkedList<Node>();
        List<String> words = new ArrayList<>();

        public Node(char c) {
            this.c = c;
        }
    }

    Node root = new Node((char) 0);

    public void add(String encodedWord, String clearWord) {
        if (encodedWord == null || encodedWord.length() == 0) {
            return;
        }
        add(root, clearWord, encodedWord.toCharArray(), 0);
    }

    private void add(Node root, String clearWord, char[] s, int index) {
        if (index == s.length) {
            root.isEndOfWord = true;
            addWordIfNotExists(root, clearWord);
            return;
        }
        for (Node kid : root.children) {
            if (kid.c == s[index]) {
                add(kid, clearWord, s, index + 1);
                return;
            }
        }

        for (int i = index; i < s.length; i++) {
            Node n = new Node(s[i]);
            root.children.add(n);
            root = n;
        }
        root.isEndOfWord = true;
        addWordIfNotExists(root, clearWord);
    }

    private void addWordIfNotExists(Node root, String newWord) {
        if (!root.words.contains(newWord)) {
            root.words.add(newWord);
        }
    }

    public List<String> search(String s) {
        if (s == null || s.length() == 0) {
            return new LinkedList<>();
        }
        return search(root, s.toCharArray(), 0);
    }

    private List<String> search(Node root, char[] s, int index) {
        if (index == s.length) {
            return addWords(root, 3);
        }
        for (Node kid : root.children) {
            if (kid.c == s[index]) {
                return search(kid, s, index + 1);
            }
        }
        return new LinkedList<>();
    }

    private List<String> addWords(Node root, int maxResults) {
        List<String> results = new LinkedList<>();
        List<Node> nextNodes = new LinkedList<>();
        nextNodes.add(root);
        while (!nextNodes.isEmpty() && results.size() < maxResults) {
            Node currentNode = nextNodes.get(0);
            addFromNode(currentNode, results, maxResults);
            for (Node node : currentNode.children) {
                nextNodes.add(node);
            }
            nextNodes.remove(0);
        }
        return results;
    }

    private void addFromNode(Node root, List<String> results, int maxResults) {
        for (int i = 0; i < root.words.size() && results.size() < maxResults; i++) {
            results.add(root.words.get(i));
        }
    }
}
