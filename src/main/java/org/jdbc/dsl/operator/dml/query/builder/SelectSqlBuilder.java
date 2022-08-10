package org.jdbc.dsl.operator.dml.query.builder;


import lombok.AllArgsConstructor;
import org.jdbc.dsl.metadata.dialect.Dialect;
import org.jdbc.dsl.metadata.dialect.PostgresqlDialect;
import org.jdbc.dsl.operator.SqlBuilder;
import org.jdbc.dsl.operator.dml.query.QueryOperatorParameter;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.operator.fragments.query.SelectColumnFragmentBuilder;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.param.request.SqlRequests;
import org.jdbc.dsl.param.QueryParam;
import org.jdbc.dsl.render.SqlAppender;
import org.jdbc.dsl.utils.StringUtils;
import org.jdbc.dsl.core.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 简单查询构建器
 * @Author ljgui
 * @since 1.0.0
 */
@AllArgsConstructor
public class SelectSqlBuilder implements SqlBuilder {

    private String table;
    private Dialect dialect;

    public static SelectSqlBuilder of(String table){
        return new SelectSqlBuilder(table,PostgresqlDialect.getInstance());
    }
    public static SelectSqlBuilder of(String table,Dialect dialect){
        return new SelectSqlBuilder(table,dialect);
    }

    public static SelectSqlBuilder of(Class tableEntity){
        String simpleName = tableEntity.getSimpleName();
        simpleName = StringUtils.toLowerCaseFirstOne(simpleName);
        String table = StringUtils.camelCase2UnderScoreCase(simpleName).toUpperCase();
        return new SelectSqlBuilder(table,PostgresqlDialect.getInstance());
    }
    public static SelectSqlBuilder of(Class tableEntity,Dialect dialect){
        String simpleName = tableEntity.getSimpleName();
        simpleName = StringUtils.toLowerCaseFirstOne(simpleName);
        String table = StringUtils.camelCase2UnderScoreCase(simpleName).toUpperCase();
        return new SelectSqlBuilder(table,dialect);
    }


    public SqlRequest build(Query query){
        QueryParam param = query.getParam();
         List<String> selectColumn = SelectColumnFragmentBuilder.of(dialect).createFragments(QueryOperatorParameter.of(param.getIncludes(), param.getExcludes())).getSql();

        SqlAppender whereSql = new SqlAppender();
        buildWhere("",param.getTerms(),whereSql);
        if (!whereSql.isEmpty()){
            whereSql.removeFirst();
        }

        return  doBuild(selectColumn,whereSql,param);
    }

    private SqlRequest doBuild(List<String> selectColumn, SqlAppender whereSql,QueryParam param) {
        SqlAppender appender = new SqlAppender();
        appender.add("SELECT ");
        if (!selectColumn.isEmpty()){
            selectColumn.forEach(column->{
                appender.add(column);
                appender.add(",");
            });
            appender.removeLast();
        }
        appender.addSpc(" FROM "+ table);
        if (!whereSql.isEmpty()){
            appender.add(" WHERE ", "").addAll(whereSql);
        }

        if (!param.getGroupBy().isEmpty()){
            appender.add(" GROUP BY ");
            param.getGroupBy().forEach(col -> appender.add(col, ","));
            appender.removeLast();
        }

        if (!param.getSorts().isEmpty()) {
            appender.add(" ORDER BY ");
            param.getSorts().forEach(sort -> appender.add(sort.getName(), " ", sort.getOrder(), ","));
            appender.removeLast();
        }
        if (param.isPaging()) {
            String pageSql = PostgresqlDialect.doPaging(param.getPageIndex(), param.getPageSize());
            appender.add(pageSql);
        }

       return SqlRequests.template(appender.toString(),param);
    }


    /*private List<String> parseSelectColumn(QueryParam param) {
        Set<String> includes = param.getIncludes(),
                    excludes = param.getExcludes();
        boolean includesIsEmpty = includes.isEmpty(),
                excludesIsEmpty = excludes.isEmpty();
        List<String> selectColumn = new ArrayList<>();

        if (includesIsEmpty){
            selectColumn.add("*");
        }else if ( !includesIsEmpty && excludesIsEmpty){
            selectColumn = includes.stream()
                    .map(x->{
                         if (!x.contains(" as ")){
                            return StringUtils.camelCase2UnderScoreCase(x).toUpperCase();
                         }
                         return x;
                    }).collect(Collectors.toList());
        }else if (!includesIsEmpty && !excludesIsEmpty){
            selectColumn = includes.stream()
                    .filter(i -> !excludes.contains(i))
                    .map(x->{
                        if (!x.contains(" as ")){
                            return StringUtils.camelCase2UnderScoreCase(x).toUpperCase();
                        }
                        return x;
                    }).collect(Collectors.toList());
        }
        return selectColumn;
    }*/


}
