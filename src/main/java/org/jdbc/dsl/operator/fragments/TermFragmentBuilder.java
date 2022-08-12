package org.jdbc.dsl.operator.fragments;


import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.FeatureId;
import org.jdbc.dsl.core.RDBFeatureType;
import org.jdbc.dsl.param.Term;

/**
 * SQL条件片段构造器,用于将{@link Term}转换为对应的where条件.
 * <p>
 * 实现此接口自定义条件类型{@link Term#getTermType()},实现条件复用.
 *
 */
public interface TermFragmentBuilder extends Feature {

    /**
     * 根据termType来创建featureId
     *
     * @param termType termType
     * @return FeatureId
     */
    static FeatureId<TermFragmentBuilder> createFeatureId(String termType) {
        return FeatureId.of(RDBFeatureType.termType.getFeatureId(termType.toLowerCase()));
    }

    @Override
    default String getId() {
        return getType().getFeatureId(getTermType().toLowerCase());
    }

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.termType;
    }

    /**
     * @return 条件类型
     * @see Term#getTermType()
     */
    String getTermType();

    /**
     * 创建SQL条件片段
     *
     * @param columnFullName 列全名,如: schema.table
     * @param column         列对应的元数据. {@link Term#getColumn()}
     * @param term           条件.
     * @return SQL片段
     * @see Term
     */
    SqlFragments createFragments(String columnFullName, Term term);

}
