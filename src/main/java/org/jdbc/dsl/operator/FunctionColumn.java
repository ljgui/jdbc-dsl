package org.jdbc.dsl.operator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: ljgui
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode
public class FunctionColumn {

    private String column;

    private String function;

    private Map<String, Object> opts;

    @Override
    public String toString() {
        return function + "(" + column + ")";
    }

}
