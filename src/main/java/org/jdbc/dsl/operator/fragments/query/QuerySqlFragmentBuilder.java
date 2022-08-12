package org.jdbc.dsl.operator.fragments.query;

import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.FeatureType;
import org.jdbc.dsl.core.RDBFeatureType;
import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.operator.fragments.SqlFragments;

/**
 * @Author: ljgui
 * @since 1.0.0
 */
public interface QuerySqlFragmentBuilder extends Feature {
    //feature id
    String where = "queryTermsFragmentBuilder";
    String selectColumns = "selectColumnFragmentBuilder";
    String join = "joinSqlFragmentBuilder";
    String sortOrder = "sortOrderFragmentBuilder";

    @Override
    default FeatureType getType() {
        return RDBFeatureType.fragment;
    }

    Dialect getDialect();

    SqlFragments createFragments(QueryOperatorParameter parameter);

}
