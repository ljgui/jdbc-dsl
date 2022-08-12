package org.jdbc.dsl.operator.fragments.query;

import org.jdbc.dsl.core.FeatureId;
import org.jdbc.dsl.operator.fragments.SqlBuilder;
import org.jdbc.dsl.param.request.SqlRequest;

public interface QuerySqlBuilder extends SqlBuilder<QueryOperatorParameter> {

    String ID_VALUE = "querySqlBuilder";

    FeatureId<QuerySqlBuilder> ID = FeatureId.of(ID_VALUE);

    @Override
    default String getId() {
        return ID_VALUE;
    }

    @Override
    default String getName() {
        return "查询SQL构造器";
    }

    SqlRequest build(QueryOperatorParameter parameter);
}
