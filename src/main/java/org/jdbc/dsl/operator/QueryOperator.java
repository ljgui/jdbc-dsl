package org.jdbc.dsl.operator;


import org.jdbc.dsl.Conditional;
import org.jdbc.dsl.LogicalOperation;
import org.jdbc.dsl.MethodReferenceColumn;
import org.jdbc.dsl.StaticMethodReferenceColumn;
import org.jdbc.dsl.operator.dml.query.SelectColumn;
import org.jdbc.dsl.operator.dml.query.SortOrder;
import org.jdbc.dsl.param.QueryParam;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.param.request.SqlRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 查询操作抽象类,提供DSL查询操作
 * <pre>{@code
 * database
 * .dml()
 * .query()
 * .select(Selects.count("id","total"))
 * .from("user")
 * .where(dsl-> dsl.is("name","1"))
 * .execute()
 * .reactive(map())
 * .subscribe(data-> );
 * }</pre>
 *
 */
public abstract class QueryOperator implements LogicalOperation<QueryOperator> {

    /**
     * 指定查询列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public abstract QueryOperator select(String... columns);

    /**
     * 指定查询列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public abstract QueryOperator select(Collection<String> columns);

    /**
     * 指定查询列
     *
     * @param columns 列
     * @return QueryOperator
     * @see SelectColumn
     */
    public abstract QueryOperator select(SelectColumn... columns);


    /**
     * 使用方法引用来指定查询列
     * <pre>{@code
     *     select(userEntity::getId,userEntity::getName)
     * }</pre>
     *
     * @param columns 方法引用
     * @param <T>     泛型
     * @return QueryOperator
     */
    @SafeVarargs
    public final <T> QueryOperator select(MethodReferenceColumn<T>... columns) {
        return select(Arrays.stream(columns)
                            .map(MethodReferenceColumn::getColumn)
                            .toArray(String[]::new));
    }

    /**
     * 使用静态方法引用来指定查询列
     * <pre>{@code
     *     select(UserEntity::getId,UserEntity::getName)
     * }</pre>
     *
     * @param columns 方法引用
     * @param <T>     泛型
     * @return QueryOperator
     */
    @SafeVarargs
    public final <T> QueryOperator select(StaticMethodReferenceColumn<T>... columns) {
        return select(Arrays.stream(columns)
                            .map(StaticMethodReferenceColumn::getColumn)
                            .toArray(String[]::new));
    }

    /**
     * 指定忽略查询的列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public abstract QueryOperator selectExcludes(Collection<String> columns);

    /**
     * 指定忽略查询的列
     *
     * @param columns 列名
     * @return QueryOperator
     */
    public QueryOperator selectExcludes(String... columns) {
        return selectExcludes(Arrays.asList(columns));
    }

    /**
     * 使用静态方法引用来指定不查询的列
     * <pre>{@code
     *     selectExcludes(UserEntity::getId,UserEntity::getName)
     * }</pre>
     *
     * @param columns 方法引用
     * @param <T>     泛型
     * @return QueryOperator
     */
    @SafeVarargs
    public final <T> QueryOperator selectExcludes(StaticMethodReferenceColumn<T>... columns) {
        return selectExcludes(Arrays.stream(columns)
                                    .map(StaticMethodReferenceColumn::getColumn)
                                    .collect(Collectors.toSet()));
    }

    /**
     * 通过回调函数来指定where条件
     * <pre>
     *     where(dsl->dsl.and("name","name"))
     * </pre>
     *
     * @param conditionalConsumer conditionalConsumer
     * @return QueryOperator
     */
    public abstract QueryOperator where(Consumer<Conditional<?>> conditionalConsumer);


    /**
     * 添加过滤条件
     *
     * @param term term
     * @return QueryOperator
     */
    public abstract QueryOperator where(Term term);

    /**
     * 添加多个过滤条件
     *
     * @param terms 过滤条件
     * @return QueryOperator
     */
    public abstract QueryOperator where(Collection<Term> terms);

    /**
     * 通过{@link QueryParam}来设置查询
     *
     * @param param param
     * @return QueryOperator
     * @see QueryParam#getTerms()
     * @see QueryParam#getContext()
     * @see QueryParam#getSorts()
     * @see QueryParam#getPageSize()
     * @see QueryParam#getPageIndex()
     */
    public abstract QueryOperator setParam(QueryParam param);


    public abstract QueryOperator orderBy(SortOrder... operators);

    public abstract QueryOperator paging(int pageIndex, int pageSize);

    public abstract QueryOperator forUpdate();

    public abstract QueryOperator context(Map<String, Object> context);

    /**
     * 获取SQL请求
     *
     * @return 获取SQL请求
     */
    public abstract SqlRequest getSql();


}
