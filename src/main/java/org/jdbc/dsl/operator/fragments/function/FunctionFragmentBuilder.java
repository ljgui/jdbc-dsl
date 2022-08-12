package org.jdbc.dsl.operator.fragments.function;

import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.FeatureId;
import org.jdbc.dsl.core.RDBFeatureType;
import org.jdbc.dsl.operator.fragments.SqlFragments;

import java.util.Map;

public interface FunctionFragmentBuilder extends Feature {

    static FeatureId<FunctionFragmentBuilder> createFeatureId(String suffix){
        return FeatureId.of(RDBFeatureType.function.getId().concat(":").concat(suffix));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getFunction());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.function;
    }

    String getFunction();

    SqlFragments create(String columnFullName, Map<String, Object> opts);

}
