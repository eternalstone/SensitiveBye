package io.github.eternalstone.enums;

/**
 * to do something
 *
 * @author Justzone on 2022/9/30 15:48
 */
public enum FileType {

    NONE(""),

    YML("yml"),

    YAML("yaml"),

    PROPERTIES("properties");

    private String ext;

    FileType(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        return ext;
    }

    public static FileType getFileType(String ext) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getExt().equals(ext)) {
                return fileType;
            }
        }
        return NONE;
    }
}
