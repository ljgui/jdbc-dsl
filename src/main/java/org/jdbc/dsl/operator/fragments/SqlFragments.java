package org.jdbc.dsl.operator.fragments;


import org.jdbc.dsl.param.request.EmptySqlRequest;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.param.request.SqlRequests;

import java.util.Collections;
import java.util.List;

public interface SqlFragments {

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    List<String> getSql();

    List<Object> getParameters();

    default SqlRequest toRequest() {
        if (isEmpty()) {
            return EmptySqlRequest.INSTANCE;
        }
        return SqlRequests.prepare(String.join(" ", getSql()), getParameters().toArray());
    }

    static SqlFragments single(String sql) {
        return new SqlFragments() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public List<String> getSql() {
                return Collections.singletonList(sql);
            }

            @Override
            public List<Object> getParameters() {
                return Collections.emptyList();
            }

            @Override
            public String toString() {
                return sql;
            }
        };
    }
}
