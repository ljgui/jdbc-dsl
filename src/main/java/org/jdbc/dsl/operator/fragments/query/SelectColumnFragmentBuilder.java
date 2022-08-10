package org.jdbc.dsl.operator.fragments.query;

import lombok.NoArgsConstructor;
import org.jdbc.dsl.metadata.dialect.Dialect;
import org.jdbc.dsl.operator.dml.query.QueryOperatorParameter;
import org.jdbc.dsl.operator.dml.query.QuerySqlFragmentBuilder;
import org.jdbc.dsl.operator.fragments.SqlFragments;

/**
 * @Description:
 * @Author: ljgui
 */
public class SelectColumnFragmentBuilder implements QuerySqlFragmentBuilder {

    private Dialect dialect;

    @Override
    public String getId() {
        return selectColumns;
    }

    @Override
    public String getName() {
        return "查询列";
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public SqlFragments createFragments(QueryOperatorParameter parameter) {

        return null;
    }

    public SelectColumnFragmentBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    public static SelectColumnFragmentBuilder of(Dialect dialect){
      return new SelectColumnFragmentBuilder(dialect);
    }

}
