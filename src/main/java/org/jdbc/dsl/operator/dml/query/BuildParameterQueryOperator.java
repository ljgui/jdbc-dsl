package org.jdbc.dsl.operator.dml.query;

import lombok.Getter;
import org.jdbc.dsl.Conditional;
import org.jdbc.dsl.anotation.Table;
import org.jdbc.dsl.core.Query;
import org.jdbc.dsl.operator.QueryOperator;
import org.jdbc.dsl.operator.fragments.query.QueryOperatorParameter;
import org.jdbc.dsl.param.QueryParam;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import static org.jdbc.dsl.operator.dml.query.SortOrder.asc;
import static org.jdbc.dsl.operator.dml.query.SortOrder.desc;

public class BuildParameterQueryOperator extends QueryOperator {

    @Getter
    private final QueryOperatorParameter parameter = new QueryOperatorParameter();

    public BuildParameterQueryOperator(String from) {
        parameter.setFrom(from);
    }

    public BuildParameterQueryOperator(Class from) {
        Annotation table = from.getAnnotation(Table.class);
        if (table==null){
            String simpleName = from.getSimpleName();
            simpleName = StringUtils.toLowerCaseFirstOne(simpleName);
            String tableName = StringUtils.camelCase2UnderScoreCase(simpleName).toUpperCase();
            parameter.setFrom(tableName);
        }else{
            String value = ((Table) table).value();
            parameter.setFrom(value);
        }
    }

    @Override
    public QueryOperator select(Collection<String> columns) {

        columns.stream()
               .map(SelectColumn::of)
               .forEach(parameter.getSelect()::add);
        return this;
    }

    @Override
    public QueryOperator select(String... columns) {
        return select(Arrays.asList(columns));
    }

    @Override
    public QueryOperator select(SelectColumn... column) {
        for (SelectColumn selectColumn : column) {
            parameter.getSelect().add(selectColumn);
        }
        return this;
    }

    @Override
    public QueryOperator selectExcludes(Collection<String> columns) {
        parameter.getSelectExcludes().addAll(columns);
        return this;
    }

    @Override
    public QueryOperator where(Consumer<Conditional<?>> conditionalConsumer) {
        Query<?, QueryParam> query = Query.of();
        conditionalConsumer.accept(query);
        parameter.getWhere().addAll(query.getParam().getTerms());
        return this;
    }

    @Override
    public QueryOperator where(Term term) {
        parameter.getWhere().add(term);
        return this;
    }

    @Override
    public QueryOperator setParam(QueryParam param) {
        QueryOperator operator = this;
        if (param.isPaging()) {
            operator = operator.paging(param.getPageIndex(), param.getPageSize());
        }
        return operator
                .where(param.getTerms())
                .select(param.getIncludes().toArray(new String[0]))
                .selectExcludes(param.getExcludes().toArray(new String[0]))
                .orderBy(param.getSorts().stream()
                              .map(sort -> "asc".equals(sort.getOrder()) ?
                                      asc(sort.getName()) :
                                      desc(sort.getName()))
                              .toArray(SortOrder[]::new))
                .context(param.getContext());
    }

    @Override
    public QueryOperator where(Collection<Term> term) {
        parameter.getWhere().addAll(term);
        return this;
    }

    @Override
    public QueryOperator orderBy(SortOrder... operators) {
        for (SortOrder operator : operators) {
            parameter.getOrderBy().add(operator);
        }
        return this;
    }

    @Override
    public QueryOperator paging(int pageIndex, int pageSize) {
        parameter.setPageIndex(pageIndex);
        parameter.setPageSize(pageSize);
        return this;
    }

    @Override
    public QueryOperator forUpdate() {
        parameter.setForUpdate(true);
        return this;
    }

    @Override
    public QueryOperator context(Map<String, Object> context) {
        parameter.setContext(context);
        return this;
    }

    @Override
    public SqlRequest getSql() {
        throw new UnsupportedOperationException();
    }

}
