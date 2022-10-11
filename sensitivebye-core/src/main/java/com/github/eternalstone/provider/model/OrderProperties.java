package com.github.eternalstone.provider.model;

import java.util.*;

/**
 * 使用LinkedHashSet使properties读取有序
 *
 * @author Justzone on 2022/10/9 14:50
 */
public class OrderProperties extends Properties {

    private final LinkedHashSet<Object> keys = new LinkedHashSet<>();

    @Override
    public Enumeration<Object> keys() {
        return Collections.enumeration(keys);
    }

    @Override
    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }

    @Override
    public Set<Object> keySet() {
        return keys;
    }

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<>();
        for (Object key : this.keys) {
            set.add((String) key);
        }
        return set;
    }

}
