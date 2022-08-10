package org.jdbc.dsl.param;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class Column implements Serializable {
    /**
     * 字段名
     */
    private String name;

    /**
     * 类型
     */
    private String type;
}