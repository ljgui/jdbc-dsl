package org.jdbc.dsl.operator.fragments.query;

import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.operator.dml.query.SelectColumn;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.utils.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
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
        List<SelectColumn> select = parameter.getSelect();
        Set<String> selectExcludes = parameter.getSelectExcludes();
        String selectString = select.stream().filter(Objects::nonNull)
                .filter(column -> !selectExcludes.contains(column.getColumn()) && !selectExcludes.contains(column.getAlias()))
                .map(col -> StringUtils.isNullOrEmpty(col.getAlias()) ? dialect.getQuoteStart().concat(col.getColumn()).concat(dialect.getQuoteEnd())
                        : dialect.getQuoteStart().concat(col.getColumn()).concat(dialect.getQuoteEnd()).concat(" as ").concat(col.getAlias()))
                .collect(Collectors.joining(","));
        return SqlFragments.single(selectString);
    }

    public SelectColumnFragmentBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    public static SelectColumnFragmentBuilder of(Dialect dialect){
      return new SelectColumnFragmentBuilder(dialect);
    }

}
