package io.github.eternalstone.attachment.file.strategy;

import io.github.eternalstone.enums.FileType;
import io.github.eternalstone.tools.ConfigurationUtil;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * to do something
 *
 * @author Justzone on 2022/10/8 20:02
 */
public class PropertiesFileStrategy implements IConfigurationFileStrategy {

    @Override
    public FileType getFileType() {
        return FileType.PROPERTIES;
    }

    @Override
    public LinkedHashMap<String, Object> readAndParseFile(String source) throws IOException {
        Properties properties = ConfigurationUtil.readPropertiesFile(source);
        return ConfigurationUtil.convert(properties);
    }

    @Override
    public void writeResultToFile(LinkedHashMap<String, Object> param, String target) throws IOException {
        ConfigurationUtil.writePropertiesFile(ConfigurationUtil.convert(param), target);
    }

    @Override
    public String transferResultToString(LinkedHashMap<String, Object> param) {
        return ConfigurationUtil.readPropertiesFile(ConfigurationUtil.convert(param));
    }
}
