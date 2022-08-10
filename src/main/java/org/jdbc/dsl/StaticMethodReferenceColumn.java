package org.jdbc.dsl;

import java.io.Serializable;
import java.util.function.Function;

public interface StaticMethodReferenceColumn<T> extends Function<T, Object>, Serializable {

    default String getColumn() {
        return MethodReferenceConverter.convertToColumn(this);
    }
}
