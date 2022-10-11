package io.github.eternalstone.tools;

import io.github.eternalstone.exception.SensitiveByeException;
import io.github.eternalstone.provider.model.OrderProperties;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件操作工具类
 *
 * @author Justzone on 2022/9/30 14:14
 */
public class ConfigurationUtil {

    /**
     * 从 yaml 文件读取转到 Java 对象
     *
     * @param filePath 文件路径
     * @param clazz    类型
     * @param <T>
     * @return
     */
    public static <T> T readYamlFile(String filePath, Class<T> clazz) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        Yaml yaml = new Yaml();
        T object = yaml.loadAs(inputStream, clazz);
        inputStream.close();
        return object;
    }

    /**
     * 从 yaml 文件读取转到 Java 对象
     *
     * @param inputStream 文件流
     * @param clazz       类型
     * @param <T>
     * @return
     */
    public static <T> T readYamlFile(InputStream inputStream, Class<T> clazz) throws IOException {
        if (inputStream == null) {
            return null;
        }
        Yaml yaml = new Yaml();
        T object = yaml.loadAs(inputStream, clazz);
        inputStream.close();
        return object;
    }

    /**
     * 对象转yaml字符串
     *
     * @param object 对象
     * @return
     */
    public static String readYamlFile(Object object) {
        if (object == null) {
            throw new SensitiveByeException("object null is not allowed");
        }
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        StringWriter sw = new StringWriter();
        yaml.dump(object, sw);
        return sw.toString();
    }

    /**
     * 将 Java 对象转成yaml输出到目标文件
     *
     * @param object 对象
     * @param target 目标文件路径
     */
    public static void writeToYamlFile(Object object, String target) throws IOException {
        if (object == null) {
            return;
        }
        Writer writer = new FileWriter(target);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        yaml.dump(object, writer);
        writer.close();
    }

    /**
     * 从 properties 文件读取转到 Java 对象
     *
     * @param filePath 文件路径
     * @return
     */
    public static Properties readPropertiesFile(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(filePath);
        Properties properties = new OrderProperties();
        properties.load(inputStream);
        inputStream.close();
        return properties;
    }

    /**
     * 将 Properties对象转成string
     *
     * @param properties
     * @return
     */
    public static String readPropertiesFile(Properties properties) {
        if (properties == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            builder.append(key).append("=").append(properties.get(key)).append("\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * 从 properties 文件读取转到 Java 对象
     *
     * @param inputStream 文件流
     * @return
     */
    public static Properties readPropertiesFile(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        Properties properties = new OrderProperties();
        properties.load(inputStream);
        inputStream.close();
        return properties;
    }


    /**
     * 将 properties 对象写入target文件
     *
     * @param properties
     * @param target
     */
    public static void writePropertiesFile(Properties properties, String target) throws IOException {
        if (properties == null) {
            return;
        }
        Writer writer = new FileWriter(target);
        properties.store(writer, "");
        writer.close();
    }

    /**
     * 将Map 转成 Properties对象
     *
     * @param map
     * @return
     */
    public static Properties convert(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Properties properties = new OrderProperties();
        mapToPorperties(null, properties, map);
        return properties;
    }

    /**
     * 将Properties对象转成linkedHashMap对象
     */
    public static LinkedHashMap<String, Object> convert(Properties properties) {
        if (properties == null) {
            return null;
        }
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>(properties.stringPropertyNames().size());
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            resultMap.put(key, properties.get(key));
        }
        return resultMap;
    }


    /**
     * 递归子配置
     *
     * @param name
     * @param properties
     * @param fromMap
     */
    private static void mapToPorperties(String name, final Properties properties, Map<String, Object> fromMap) {
        fromMap.forEach((key, value) -> {
            if (value instanceof Map) {
                String relKey = name != null ? name.concat(".").concat(key) : key;
                mapToPorperties(relKey, properties, (Map<String, Object>) value);
            } else {
                String relKey = name != null ? name.concat(".").concat(key) : key;
                properties.setProperty(relKey, value.toString());
            }
        });
    }


}
