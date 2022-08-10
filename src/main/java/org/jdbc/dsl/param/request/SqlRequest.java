package org.jdbc.dsl.param.request;


import org.jdbc.dsl.utils.SqlUtils;

public interface SqlRequest {

    String getSql();

    Object[] getParameters();

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default String toNativeSql(){
        return SqlUtils.toNativeSql(getSql(), getParameters());
    }
}
