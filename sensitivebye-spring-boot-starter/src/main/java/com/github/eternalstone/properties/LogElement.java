package com.github.eternalstone.properties;

import java.io.Serializable;

/**
 * to do something
 *
 * @author Justzone on 2022/9/27 16:46
 */
public class LogElement implements Serializable {

    /**
     * 脱敏开关
     */
    private boolean enabled = false;

    /**
     * 规则配置
     */

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
