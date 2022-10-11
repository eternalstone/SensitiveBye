package com.github.eternalstone.attachment.file.strategy;

import com.github.eternalstone.enums.FileType;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * 文件操作策略接口
 *
 * @author Justzone on 2022/10/8 19:56
 */
public interface IConfigurationFileStrategy {

    /**
     * 获取文件处理类
     * @return
     */
    FileType getFileType();

    /**
     * 读取并解析配置文件
     * @param source 源配置文件绝对路径
     * @return LinkedHashMap<String, Object> 配置文件键值对
     */
    LinkedHashMap<String, Object> readAndParseFile(String source) throws IOException;

    /**
     * 将结果写出成文件
     * @param param
     */
    void writeResultToFile(LinkedHashMap<String, Object> param, String target) throws IOException;

    /**
     * 将结果写出成字符串
     * @param param
     * @return
     */
    String transferResultToString(LinkedHashMap<String, Object> param);

}
