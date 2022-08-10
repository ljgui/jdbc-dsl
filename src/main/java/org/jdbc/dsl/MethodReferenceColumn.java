package org.jdbc.dsl;

import java.io.Serializable;
import java.util.function.Supplier;

public interface MethodReferenceColumn<T> extends Supplier<T>, Serializable {

    default String getColumn() {
        return MethodReferenceConverter.convertToColumn(this);
    }
}
