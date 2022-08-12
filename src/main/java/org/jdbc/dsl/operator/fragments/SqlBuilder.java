package org.jdbc.dsl.operator.fragments;

import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.RDBFeatureType;
import org.jdbc.dsl.param.request.SqlRequest;

import static org.jdbc.dsl.core.RDBFeatureType.sqlBuilder;

public interface SqlBuilder<T> extends Feature {
    @Override
    default RDBFeatureType getType() {
        return sqlBuilder;
    }

    SqlRequest build(T parameter);
}
