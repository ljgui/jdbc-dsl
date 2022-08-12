package org.jdbc.dsl.operator.fragments.query;

import org.jdbc.dsl.core.FeatureId;
import org.jdbc.dsl.supports.postgresql.PostgresqlDialectFeatures;
import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.operator.fragments.EmptySqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.operator.fragments.TermFragmentBuilder;
import org.jdbc.dsl.param.Term;

import java.util.Optional;

import static org.jdbc.dsl.operator.fragments.TermFragmentBuilder.createFeatureId;

/**
 * @Author: ljgui
 */
public class QueryTermsFragmentBuilder extends AbstractTermsFragmentBuilder<QueryOperatorParameter> implements QuerySqlFragmentBuilder {

    private Dialect dialect;

    @Override
    public String getId() {
        return where;
    }

    @Override
    public String getName() {
        return "查询条件";
    }

    @Override
    protected SqlFragments createTermFragments(QueryOperatorParameter parameter, Term term) {
        String columnName = term.getColumn();
        if (columnName == null) {
            return EmptySqlFragments.INSTANCE;
        }
        FeatureId<TermFragmentBuilder> featureId = createFeatureId(term.getTermType());
        Optional<TermFragmentBuilder> feature = PostgresqlDialectFeatures.getInstance().findFeature(featureId);
        if (feature.isPresent()){
            TermFragmentBuilder termFragmentBuilder = feature.get();
            return termFragmentBuilder.createFragments(columnName, term);
        }
        return EmptySqlFragments.INSTANCE;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {
        return createTermFragments(parameter,parameter.getWhere());
    }

    public QueryTermsFragmentBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    public static QueryTermsFragmentBuilder of(Dialect dialect){
        return new QueryTermsFragmentBuilder(dialect);
    }
}
