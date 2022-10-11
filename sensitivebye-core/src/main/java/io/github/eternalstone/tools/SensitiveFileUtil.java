package io.github.eternalstone.tools;

import io.github.eternalstone.attachment.file.handle.AbstractFileHandler;
import io.github.eternalstone.attachment.file.handle.IFileHandler;
import io.github.eternalstone.attachment.file.handle.SensitiveFileHandler;
import io.github.eternalstone.attachment.file.strategy.IConfigurationFileStrategy;
import io.github.eternalstone.attachment.file.strategy.PropertiesFileStrategy;
import io.github.eternalstone.attachment.file.strategy.YamlFileStrategy;
import io.github.eternalstone.attachment.file.strategy.YmlFileStrategy;
import io.github.eternalstone.enums.FileType;
import io.github.eternalstone.exception.SensitiveByeException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 敏感文件操作类
 *
 * @author Justzone on 2022/9/30 14:13
 */
public class SensitiveFileUtil {

    private static final Map<FileType, IConfigurationFileStrategy> STRATEGY_MAP = new ConcurrentHashMap<>(3);
    private static final AbstractFileHandler DEFAULT_HANDLER = new SensitiveFileHandler();

    static {
        STRATEGY_MAP.put(FileType.YML, new YmlFileStrategy());
        STRATEGY_MAP.put(FileType.YAML, new YamlFileStrategy());
        STRATEGY_MAP.put(FileType.PROPERTIES, new PropertiesFileStrategy());
    }

    private static FileType getFileType(String path) {
        if (StringUitl.isBlank(path) || StringUitl.indexOf(path, ".") < 0) {
            throw new SensitiveByeException("the path is not a springboot configuration file");
        }
        String ext = StringUitl.right(path, (path.length() - path.lastIndexOf(".") - 1));
        return FileType.getFileType(ext);
    }

    /**
     * 读取source文件，进行敏感过滤后写出到target文件
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void sensitiveByeToFile(String source, String target) throws IOException {
        sensitiveByeToFile(source, target, null);
    }

    /**
     * 读取source文件，进行敏感过滤后写出到target文件
     *
     * @param source
     * @param target
     * @param handler 过滤链
     * @throws IOException
     */
    public static void sensitiveByeToFile(String source, String target, IFileHandler handler) throws IOException {
        IConfigurationFileStrategy strategy = STRATEGY_MAP.get(getFileType(source));
        LinkedHashMap<String, Object> linkedHashMap = strategy.readAndParseFile(source);
        if (handler == null) {
            handler = DEFAULT_HANDLER;
        }
        handler.doFilter(linkedHashMap);
        strategy.writeResultToFile(linkedHashMap, target);
    }

    /**
     * 读取source文件，进行敏感过滤后转成 String
     *
     * @param source
     * @return
     * @throws IOException
     */
    public static String sensitiveByeToString(String source) throws IOException {
        return sensitiveByeToString(source, null);
    }

    /**
     * 读取source文件，进行敏感过滤后转成 String
     *
     * @param source
     * @param handler 过滤链
     * @return
     * @throws IOException
     */
    public static String sensitiveByeToString(String source, IFileHandler handler) throws IOException {
        IConfigurationFileStrategy strategy = STRATEGY_MAP.get(getFileType(source));
        LinkedHashMap<String, Object> linkedHashMap = strategy.readAndParseFile(source);
        if (handler == null) {
            handler = DEFAULT_HANDLER;
        }
        handler.doFilter(linkedHashMap);
        return strategy.transferResultToString(linkedHashMap);
    }

}
