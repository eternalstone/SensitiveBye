package com.github.eternalstone.attachment.file.handle;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * to do something
 *
 * @author Justzone on 2022/10/8 20:51
 */
public class SensitiveFileHandler extends AbstractFileHandler {

    private Set<String> keys;

    public SensitiveFileHandler() {
        //初始化一个默认规则库
        keys = new HashSet<>();
        //关键词
        keys.add("username");
        keys.add("password");
        keys.add("accessKey");
        keys.add("access-key");
        keys.add("accessKeyId");
        keys.add("access-key-id");
        keys.add("secretKey");
        keys.add("secret-key");
        keys.add("accessSecret");
        keys.add("access-secret");
        keys.add("accessKeySecret");
        keys.add("access-key-secret");
        keys.add("token");
        //spring相关
        keys.add("spring.datasource.username");
        keys.add("spring.datasource.password");
        keys.add("spring.redis.username");
        keys.add("spring.redis.password");
        keys.add("spring.elasticsearch.username");
        keys.add("spring.elasticsearch.password");
        keys.add("spring.data.mongodb.uri");
    }

    @Override
    public void doFilter(LinkedHashMap<String, Object> param) {
        subFilter(param);
        if (nextHandler != null) {
            nextHandler.doFilter(param);
        }
    }

    private void subFilter(Map<String, Object> map){
        for (Object key : map.keySet()) {
            Object v = map.get(key);
            if (v instanceof String || v instanceof Integer) {
                if (keys.stream().anyMatch(k -> key.toString().contains(k))) {
                    map.put(key.toString(), "******");
                }
            }
            if(v instanceof Map){
                subFilter((Map<String, Object>) v);
            }
        }
    }
}
