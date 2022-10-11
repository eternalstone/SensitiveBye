package io.github.eternalstone.properties;

import java.io.Serializable;

/**
 * to do something
 *
 * @author Justzone on 2022/9/28 11:57
 */
public class FieldElement implements Serializable {

    /**
     * 字段脱敏开关
     */
    private boolean enabled = true;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
