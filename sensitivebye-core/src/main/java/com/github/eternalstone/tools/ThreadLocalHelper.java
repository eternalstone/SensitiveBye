package com.github.eternalstone.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * to do something
 *
 * @author Justzone on 2022/8/31 13:55
 */
public class ThreadLocalHelper {

    private static final ThreadLocal<Map<String, Object>> THREAD_DATA = new ThreadLocal();

    public static void put(Map<String, Object> data) {
        THREAD_DATA.set(data);
    }

    public static void put(final String key, final Object value) {
        Map<String, Object> dataMap = getAll();
        if (null != dataMap && !dataMap.isEmpty()) {
            dataMap.put(key, value);
        } else {
            put(new HashMap<String, Object>(8) {
                {
                    this.put(key, value);
                }
            });
        }
    }

    public static <T> T get(String key) {
        Map<String, Object> dataMap = getAll();
        return null != dataMap && !dataMap.isEmpty() ? (T) dataMap.get(key) : null;
    }

    public static Map<String, Object> getAll() {
        return THREAD_DATA.get();
    }

    public static void remove() {
        THREAD_DATA.remove();
    }

}
