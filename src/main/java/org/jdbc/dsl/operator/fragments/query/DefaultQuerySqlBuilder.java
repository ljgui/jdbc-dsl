package org.jdbc.dsl.operator.fragments.query;

import org.jdbc.dsl.core.Paginator;
import org.jdbc.dsl.core.RDBFeatureType;
import org.jdbc.dsl.operator.fragments.BlockSqlFragments;
import org.jdbc.dsl.operator.fragments.FragmentBlock;
import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.supports.DialectFeacture;
import org.jdbc.dsl.supports.DialectType;
import org.jdbc.dsl.supports.Dialects;

import java.util.Optional;

import static org.jdbc.dsl.core.RDBFeatures.orderBy;
import static org.jdbc.dsl.core.RDBFeatures.select;
import static org.jdbc.dsl.operator.fragments.query.QuerySqlFragmentBuilder.where;

public class DefaultQuerySqlBuilder implements QuerySqlBuilder {


    protected Optional<SqlFragments> select(QueryOperatorParameter parameter) {
        return getSqlFragments(parameter, dialectFeacture.getFeature(select));
    }

    private Optional<SqlFragments> getSqlFragments(QueryOperatorParameter parameter, Optional<QuerySqlFragmentBuilder> querySqlFragmentBuilderOpt) {
        Optional<QuerySqlFragmentBuilder> feature = querySqlFragmentBuilderOpt;
        if (feature.isPresent()) {
            SqlFragments fragments = feature.get().createFragments(parameter);
            if (fragments.isNotEmpty()) {
                return Optional.of(fragments);
            }
        }
        return Optional.ofNullable(null);
    }

    protected Optional<SqlFragments> where(QueryOperatorParameter parameter) {
        return getSqlFragments(parameter, dialectFeacture.getFeature(where));
    }


    protected Optional<SqlFragments> orderBy(QueryOperatorParameter parameter) {
        return getSqlFragments(parameter, dialectFeacture.getFeature(orderBy));
    }

    protected SqlFragments from(QueryOperatorParameter parameter) {
        return PrepareSqlFragments
                .of()
                .addSql("FROM")
                .addSql(parameter.getFrom())
                .addSql(parameter.getFromAlias());
    }

    public SqlRequest build(QueryOperatorParameter parameter) {
        String from = parameter.getFrom();
        if (from == null || from.isEmpty()) {
            throw new UnsupportedOperationException("from table or view not set");
        }

        BlockSqlFragments fragments = BlockSqlFragments.of();

        fragments.addBlock(FragmentBlock.before, "SELECT");

        fragments.addBlock(FragmentBlock.selectColumn, select(parameter)
                .orElseGet(() -> PrepareSqlFragments.of().addSql("*")));

        fragments.addBlock(FragmentBlock.selectFrom, from(parameter));
        //where
        where(parameter)
                .ifPresent(where ->
                                   fragments.addBlock(FragmentBlock.where, "WHERE")
                                            .addBlock(FragmentBlock.where, where));
        //group by
        //having

        //order by
        orderBy(parameter)
                .ifPresent(order -> fragments.addBlock(FragmentBlock.orderBy, "ORDER BY")
                                             .addBlock(FragmentBlock.orderBy, order));

        if (Boolean.TRUE.equals(parameter.getForUpdate())) {
            fragments.addBlock(FragmentBlock.after, PrepareSqlFragments.of().addSql("FOR UPDATE"));
        }
        //分页
        else if (parameter.getPageIndex() != null && parameter.getPageSize() != null) {
            Optional<Paginator> feature = dialectFeacture.findFeature(RDBFeatureType.paginator.getId());
            if (feature.isPresent()){
                Paginator paginator = feature.get();
                SqlRequest sqlRequest = paginator.doPaging(fragments, parameter.getPageIndex(), parameter.getPageSize()).toRequest();
                return sqlRequest;
            }
        }
        return fragments.toRequest();
    }

    private Dialect dialect;
    private DialectFeacture dialectFeacture;
    private DefaultQuerySqlBuilder(DialectType dialectType){
        Optional<Dialect> feature = Dialects.of().getFeature(dialectType);
        if (!feature.isPresent()){
            throw new UnsupportedOperationException("unsupported dialect [ "+ dialectType.getName()+" ]");
        }
        dialect = feature.get();
        dialectFeacture = dialect.getDialectFeacture();
    }

    public static DefaultQuerySqlBuilder of(DialectType dialect){
        return new DefaultQuerySqlBuilder(dialect);
    }

}
