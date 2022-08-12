package org.jdbc.dsl.core;

import org.jdbc.dsl.operator.fragments.SqlFragments;

public interface Paginator extends Feature {

    @Override
    default String getId() {
        return getType().getId();
    }

    @Override
    default String getName() {
        return getType().getName();
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.paginator;
    }

    SqlFragments doPaging(SqlFragments fragments, int pageIndex, int pageSize);

}
