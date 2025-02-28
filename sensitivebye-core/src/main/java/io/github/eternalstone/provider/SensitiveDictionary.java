package io.github.eternalstone.provider;

import io.github.eternalstone.exception.SensitiveByeException;
import io.github.eternalstone.function.NoArgsFunction;
import io.github.eternalstone.tools.StringUitl;
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

    private AhoCorasickDoubleArrayTrie<String> trie;


    protected List<AhoCorasickDoubleArrayTrie.Hit<String>> handle(NoArgsFunction func, String word) {
        if (StringUitl.isBlank(word)) {
            throw new SensitiveByeException("The word is not allowed null or ''");
        }
        if (trie == null) {
            trie = load((List<String>) func.apply());
        }
        return trie.parseText(word);
    }


    protected void reload(NoArgsFunction func) {
        trie = load((List<String>) func.apply());
    }


    private AhoCorasickDoubleArrayTrie<String> load(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new SensitiveByeException("Failed to load sensitive words");
        }
        AhoCorasickDoubleArrayTrie<String> arrayTrie = new AhoCorasickDoubleArrayTrie<>();
        Map<String, String> treeMap = new TreeMap<>();
        words.forEach(word -> {
            treeMap.put(word, word);
        });
        arrayTrie.build(treeMap);
        return arrayTrie;
    }


}
