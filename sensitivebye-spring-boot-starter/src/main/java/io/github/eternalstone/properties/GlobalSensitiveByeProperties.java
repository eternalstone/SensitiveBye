package io.github.eternalstone.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * to do something
 *
 * @author Justzone on 2022/9/4 14:58
 */
@ConfigurationProperties(prefix = GlobalSensitiveByeProperties.PREFIX)
public class GlobalSensitiveByeProperties {

    public static final String PREFIX = "sensitive-bye";

    /**
     * 字段脱敏配置项
     */
    @NestedConfigurationProperty
    private FieldElement field = new FieldElement();

    /**
     * mybatis配置项
     */
    @NestedConfigurationProperty
    private MybatisElement mybatis = new MybatisElement();

    /**
     * 日志配置项
     */
    @NestedConfigurationProperty
    private LogElement log = new LogElement();

    public FieldElement getField() {
        return field;
    }

    public void setField(FieldElement field) {
        this.field = field;
    }

    public MybatisElement getMybatis() {
        return mybatis;
    }

    public void setMybatis(MybatisElement mybatis) {
        this.mybatis = mybatis;
    }

    public LogElement getLog() {
        return log;
    }

    public void setLog(LogElement log) {
        this.log = log;
    }
}
