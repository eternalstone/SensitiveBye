package com.github.eternalstone.attachment.wordsource;

import java.util.List;

/**
 * 敏感词数据源
 *
 * @author Justzone on 2022/8/30 19:14
 */
public interface ISensitiveWordSource {

    List<String> loadSource();

}
