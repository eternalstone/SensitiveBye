package com.github.eternalstone.example.sensitvebye;

import com.github.eternalstone.attachment.file.handle.AbstractFileHandler;
import java.util.LinkedHashMap;


/**
 * to do something
 *
 * @author Justzone on 2022/10/9 20:42
 */
public class SensitiveCustomeFilterHandler extends AbstractFileHandler {

    @Override
    public void doFilter(LinkedHashMap<String, Object> param) {
        param.remove("test");
    }
}
