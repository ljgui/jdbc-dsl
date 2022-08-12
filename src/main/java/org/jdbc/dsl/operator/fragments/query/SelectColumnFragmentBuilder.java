package org.jdbc.dsl.operator.fragments.query;

import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.function.FunctionFragmentBuilder;
import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.operator.dml.query.SelectColumn;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.jdbc.dsl.operator.fragments.function.FunctionFragmentBuilder.createFeatureId;

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
        Set<SelectColumn> columns = createSelectColumns(parameter);
        PrepareSqlFragments fragments = columns
                .stream()
                .map(column -> this.createFragments(column))
                .filter(Objects::nonNull)
                .reduce(PrepareSqlFragments.of(), (main, source) -> main
                        .addFragments(source)
                        .addSql(","));

        fragments.removeLastSql();

        return fragments;
    }

    private Set<SelectColumn> createSelectColumns(QueryOperatorParameter parameter) {
        Set<SelectColumn> columns = new LinkedHashSet<>(parameter.getSelect());
        Set<String> excludes = parameter.getSelectExcludes();
        if (columns.isEmpty()) {
            return columns;
        }
        Set<SelectColumn> realColumns = new LinkedHashSet<>();
        for (SelectColumn column : parameter.getSelect()) {
            String columnName = column.getColumn();
            if (column.getFunction() != null || columnName == null || excludes.contains(columnName)) {
                realColumns.add(column);
                continue;
            }
            realColumns.add(column);
        }
        return realColumns;
    }


    public PrepareSqlFragments createFragments(SelectColumn selectColumn) {
        String columnStr = selectColumn.getColumn();
        return this
                .createFragments(columnStr,selectColumn)
                .map(fragments -> {
                    PrepareSqlFragments sqlFragments = PrepareSqlFragments.of().addFragments(fragments);
                    if (! StringUtils.isNullOrEmpty(selectColumn.getAlias())){
                        sqlFragments.addSql("as").addSql(selectColumn.getAlias());
                    }
                    return sqlFragments;
                }).orElse(null);
    }


    public Optional<SqlFragments> createFragments(String columnFullName, SelectColumn column) {
        String function = column.getFunction();
        if (function != null) {
            Optional<FunctionFragmentBuilder> feature = dialect.getDialectFeacture().findFeature(createFeatureId(function));
            if (feature.isPresent()){
                SqlFragments sqlFragments = feature.get().create(columnFullName, column.getOpts());
                if (sqlFragments.isEmpty()) {
                    throw new UnsupportedOperationException("unsupported function:" + column);
                }
                return Optional.of(sqlFragments);
            }
        }
        return ofNullable(columnFullName)
                .map(PrepareSqlFragments::of);
    }

    public SelectColumnFragmentBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    public static SelectColumnFragmentBuilder of(Dialect dialect){
      return new SelectColumnFragmentBuilder(dialect);
    }

}
