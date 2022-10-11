package io.github.eternalstone.attachment.file.strategy;

import io.github.eternalstone.enums.FileType;
import io.github.eternalstone.tools.ConfigurationUtil;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * to do something
 *
 * @author Justzone on 2022/10/8 19:57
 */
public class YmlFileStrategy implements IConfigurationFileStrategy {

    @Override
    public FileType getFileType() {
        return FileType.YML;
    }

    @Override
    public LinkedHashMap<String, Object> readAndParseFile(String source) throws IOException {
        return ConfigurationUtil.readYamlFile(source, LinkedHashMap.class);
    }

    @Override
    public void writeResultToFile(LinkedHashMap<String, Object> param, String target) throws IOException {
        ConfigurationUtil.writeToYamlFile(param, target);
    }

    @Override
    public String transferResultToString(LinkedHashMap<String, Object> param) {
        return ConfigurationUtil.readYamlFile(param);
    }
}
