package com.github.eternalstone.attachment.file.strategy;

import com.github.eternalstone.enums.FileType;

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
