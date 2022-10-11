package io.github.eternalstone.provider;

import java.util.List;

/**
 * to do something
 *
 * @author Justzone on 2022/8/30 18:59
 */
public interface ISensitiveWordProvider {

    /**
     * 处理词条
     * @param word
     * @return
     */
    String handle(String word, String symbol);


    /**
     * 检查词条
     * @return 返回检测出包含的敏感词
     */
    List<String> contain(String word);



}
