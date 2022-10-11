package io.github.eternalstone.attachment.file.strategy;

import io.github.eternalstone.enums.FileType;

/**
 * to do something
 *
 * @author Justzone on 2022/10/8 19:57
 */
public class YamlFileStrategy extends YmlFileStrategy {

    @Override
    public FileType getFileType() {
        return FileType.YAML;
    }

}
