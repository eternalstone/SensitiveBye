package com.github.eternalstone.provider;

import com.github.eternalstone.exception.SensitiveByeException;
import com.github.eternalstone.function.NoArgsFunction;
import com.github.eternalstone.tools.StringUitl;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * to do something
 *
 * @author Justzone on 2022/8/30 16:30
 */
public class SensitiveDictionary {

    private static AhoCorasickDoubleArrayTrie<String> trie;


    protected static List<AhoCorasickDoubleArrayTrie.Hit<String>> handle(NoArgsFunction func, String word) {
        if (StringUitl.isBlank(word)) {
            throw new SensitiveByeException("The word is not allowed null or ''");
        }
        if (trie == null) {
            trie = load((List<String>) func.apply());
        }
        return trie.parseText(word);
    }


    protected static void reload(NoArgsFunction func) {
        trie = load((List<String>) func.apply());
    }


    private static AhoCorasickDoubleArrayTrie<String> load(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new SensitiveByeException("Failed to load sensitive words");
        }
        AhoCorasickDoubleArrayTrie<String> arrayTrie = new AhoCorasickDoubleArrayTrie<>();
        Map<String, String> treeMap = new TreeMap<>();
        words.parallelStream().forEach(word -> {
            treeMap.put(word, word);
        });
        arrayTrie.build(treeMap);
        return arrayTrie;
    }


}
