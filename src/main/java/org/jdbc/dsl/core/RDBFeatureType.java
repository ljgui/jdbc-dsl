package org.jdbc.dsl.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: ljgui
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum RDBFeatureType implements FeatureType{

    termType("SQL条件"),
    sqlBuilder("SQL构造器"),
    dialect("数据库方言"),
    function("函数"),
    fragment("SQL片段"),
    paginator("分页器"),;


    @Override
    public String getId() {
        return name();
    }

    private final String name;

    public String getFeatureId(String suffix) {
        return getId().concat(":").concat(suffix);
    }
}
