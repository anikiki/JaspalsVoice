package uk.co.jaspalsvoice.jv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cristian.zamfirescu on 11/01/16.
 */
public class TestT9 {
    private static HashMap<Integer, Set<Character>> keyboard;
    private static Map<Character, Integer> charMapping;

    private static void initMap() {
        if (keyboard != null && charMapping != null) {
            return;
        }
        keyboard = new HashMap<>();
        charMapping = new HashMap<>();

        HashSet<Character> keys = new HashSet<>();
        keys.add('a');
        keys.add('b');
        keys.add('c');
        keyboard.put(2, keys);

        charMapping.put('a', 2);
        charMapping.put('b', 2);
        charMapping.put('c', 2);


        keys = new HashSet<>();
        keys.add('d');
        keys.add('e');
        keys.add('f');
        keyboard.put(3, keys);

        charMapping.put('d', 3);
        charMapping.put('e', 3);
        charMapping.put('f', 3);

        keys = new HashSet<>();
        keys.add('g');
        keys.add('h');
        keys.add('i');
        keyboard.put(4, keys);

        charMapping.put('g', 4);
        charMapping.put('h', 4);
        charMapping.put('i', 4);

        keys = new HashSet<>();
        keys.add('j');
        keys.add('k');
        keys.add('l');
        keyboard.put(5, keys);

        charMapping.put('j', 5);
        charMapping.put('k', 5);
        charMapping.put('l', 5);

        keys = new HashSet<>();
        keys.add('m');
        keys.add('n');
        keys.add('o');
        keyboard.put(6, keys);

        charMapping.put('m', 6);
        charMapping.put('n', 6);
        charMapping.put('o', 6);

        keys = new HashSet<>();
        keys.add('p');
        keys.add('q');
        keys.add('r');
        keys.add('s');
        keyboard.put(7, keys);

        charMapping.put('p', 7);
        charMapping.put('q', 7);
        charMapping.put('r', 7);
        charMapping.put('s', 7);

        keys = new HashSet<>();
        keys.add('t');
        keys.add('u');
        keys.add('v');
        keyboard.put(8, keys);

        charMapping.put('t', 8);
        charMapping.put('u', 8);
        charMapping.put('v', 8);

        keys = new HashSet<>();
        keys.add('w');
        keys.add('x');
        keys.add('y');
        keys.add('z');
        keyboard.put(9, keys);

        charMapping.put('w', 9);
        charMapping.put('x', 9);
        charMapping.put('y', 9);
        charMapping.put('z', 9);
    }

    private static String encodeWord(String word) {
        initMap();
        StringBuilder encoded = new StringBuilder("");

        for (int i = 0; i < word.length(); i++) {
            encoded.append(String.valueOf(charMapping.get(word.charAt(i))));
        }

        return encoded.toString();
    }


    private static List<String> readAllLines() throws IOException {
        List<String> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File("words.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    public static final void main(String[] args) {
        //24885
        String[] words = {"agt", "bhuv", "civuj"};

        TrieT9 trieT9 = new TrieT9();
//        for (String word : words) {
//            trieT9.add(encodeWord(word), word);
//        }

        try {
            long startTime = System.currentTimeMillis();
            List<String> allWords = readAllLines();
            long readTimeEnd = System.currentTimeMillis();
            for (String word : allWords) {
                trieT9.add(encodeWord(word), word);
            }
            long insertTimeEnd = System.currentTimeMillis();

            String searchWord = "fro";
            System.out.println("Searching for " + searchWord);
            System.out.println(trieT9.search(encodeWord(searchWord)));
            long searchAndPrintEnd = System.currentTimeMillis();

            System.out.println("read time " + (readTimeEnd - startTime));
            System.out.println("insert time " + (insertTimeEnd - readTimeEnd));
            System.out.println("search time " + (searchAndPrintEnd - insertTimeEnd));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
