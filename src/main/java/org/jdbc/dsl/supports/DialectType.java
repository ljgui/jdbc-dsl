package org.jdbc.dsl.supports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jdbc.dsl.core.FeatureType;

/**
 * @Description:
 * @Author: ljgui
 */
@AllArgsConstructor
@Getter
public enum DialectType implements FeatureType {
    MYSQL("MYSQL"),
    POSTGRESQL("POSTGRESQL"),
    ;
    private String name;

    @Override
    public String getId() {
        return name();
    }
}
