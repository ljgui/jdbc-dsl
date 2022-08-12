package org.jdbc.dsl.supports.postgresql;

import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.RDBFeatures;
import org.jdbc.dsl.operator.fragments.query.QueryTermsFragmentBuilder;
import org.jdbc.dsl.operator.fragments.query.SelectColumnFragmentBuilder;
import org.jdbc.dsl.supports.DialectFeacture;

/**
 * @Author: ljgui
 * @since 1.0.0
 */
public class PostgresqlDialectFeatures extends DialectFeacture {

    private PostgresqlDialectFeatures(){
        /* 通用查询条件 */
        addFeature(RDBFeatures.eq);
        addFeature(RDBFeatures.is);
        addFeature(RDBFeatures.not);
        addFeature(RDBFeatures.gt);
        addFeature(RDBFeatures.gte);
        addFeature(RDBFeatures.lt);
        addFeature(RDBFeatures.lte);
        addFeature(RDBFeatures.like);
        addFeature(RDBFeatures.nlike);

        addFeature(RDBFeatures.in);
        addFeature(RDBFeatures.notIn);
        addFeature(RDBFeatures.between);
        addFeature(RDBFeatures.notBetween);
        addFeature(RDBFeatures.isNull);
        addFeature(RDBFeatures.notNull);
        addFeature(RDBFeatures.isEmpty);
        addFeature(RDBFeatures.notEmpty);
        addFeature(RDBFeatures.nEmpty);


        addFeature(RDBFeatures.max);
        addFeature(RDBFeatures.min);
        addFeature(RDBFeatures.sum);
        addFeature(RDBFeatures.avg);
        addFeature(RDBFeatures.count);

        //注册默认的where条件构造器
        addFeature(new QueryTermsFragmentBuilder(PostgresqlDialect.getInstance()));
        //注册默认的查询列构造器
        addFeature(SelectColumnFragmentBuilder.of(PostgresqlDialect.getInstance()));
    }

    private static PostgresqlDialectFeatures instance = new PostgresqlDialectFeatures();

    public static PostgresqlDialectFeatures getInstance(){
        return instance;
    }

    @Override
    public void addFeature(Feature feature) {
        features.put(feature.getId(), feature);
    }

}
