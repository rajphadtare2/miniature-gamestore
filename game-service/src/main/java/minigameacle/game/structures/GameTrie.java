package minigameacle.game.structures;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord = false;
    List<String> words = new ArrayList<>();

    static final int MAX_SUGGESTIONS = 5;

    void insertWordForNode(String word) {
        if (words.size() < MAX_SUGGESTIONS) {
            words.add(word);
        }
    }
}

public class GameTrie {
    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
            current.insertWordForNode(word);
        }
        current.isEndOfWord = true;
    }

    public List<String> suggest(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return Collections.emptyList();
            }
            current = current.children.get(ch);
        }
        return current.words;
    }
}

