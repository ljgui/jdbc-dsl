package org.jdbc.dsl.core;


import org.jdbc.dsl.*;
import org.jdbc.dsl.param.QueryParam;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.utils.PropertyUtils;
import org.jdbc.dsl.utils.StringUtils;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @Description 查询条件构造器,用于构造{@link QueryParam} 以及设置执行器进行执行
 * @Author lujiangui
 * @Since 1.0.0
 */
public final class Query<T, Q extends QueryParam> implements Conditional<Query<T, Q>> {
    private Q param;
    private Accepter<Query<T, Q>, Object> accepter = this::and;

    public Query(Q param) {
        this.param = param;
    }

    public Q getParam() {
        return param;
    }

    public Query<T, Q> setParam(Q param) {
        this.param = param;
        return this;
    }

    public Query<T, Q> excludes(String... columns) {
        param.excludes(columns);
        return this;
    }

    public Query<T, Q> includes(String... columns) {
        param.includes(columns);
        return this;
    }

    public Query<T, Q> includes(Class clazz) {
        for (String column : PropertyUtils.getColumn(clazz)) {
            param.includes(column);
        }
        return this;
    }

    @SafeVarargs
    public final <B> Query<T, Q> select(StaticMethodReferenceColumn<B>... columns) {
        return select(Arrays.stream(columns).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    public final <B> Query<T, Q> includes(StaticMethodReferenceColumn<B>... columns) {
        return includes(Arrays.stream(columns).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    public final <B> Query<T, Q> includes(MethodReferenceColumn<B>... columns) {
        return includes(Arrays.stream(columns).map(MethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    public final <B> Query<T, Q> excludes(StaticMethodReferenceColumn<B>... columns) {
        return excludes(Arrays.stream(columns).map(StaticMethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    @SafeVarargs
    public final <B> Query<T, Q> excludes(MethodReferenceColumn<B>... columns) {
        return excludes(Arrays.stream(columns).map(MethodReferenceColumn::getColumn).toArray(String[]::new));
    }

    public <B> Query<T, Q> orderByAsc(StaticMethodReferenceColumn<B> column) {
        param.orderBy(StringUtils.camelCase2UnderScoreCaseUpper(column.getColumn())).asc();
        return this;
    }

    public <B> Query<T, Q> order(StaticMethodReferenceColumn<B> column, String ascOrDesc) {
        if ("asc".equalsIgnoreCase(ascOrDesc)){
            param.orderBy(StringUtils.camelCase2UnderScoreCaseUpper(column.getColumn())).asc();
        }else {
            param.orderBy(StringUtils.camelCase2UnderScoreCaseUpper(column.getColumn())).desc();
        }
        return this;
    }

    public <B> Query<T, Q> orderByDesc(StaticMethodReferenceColumn<B> column) {
        param.orderBy(StringUtils.camelCase2UnderScoreCaseUpper(column.getColumn())).desc();
        return this;
    }

    public Query<T, Q> orderByAsc(String column) {
        param.orderBy(column).asc();
        return this;
    }

    public Query<T, Q> orderByDesc(String column) {
        param.orderBy(column).desc();
        return this;
    }


    public Query<T, Q> groupby(String column){
        param.groupBy(column);
        return this;
    }

    public Query<T, Q> groupby(String... column){
        param.groupBy(column);
        return this;
    }

    public <B> Query<T, Q> groupby(StaticMethodReferenceColumn<B> column) {
        param.groupBy(StringUtils.camelCase2UnderScoreCaseUpper(column.getColumn()));
        return this;
    }

    public Query<T, Q> doPaging(int pageIndex, int pageSize) {
        param.doPaging(pageIndex, pageSize);
        return this;
    }

    public Query<T, Q> noPaging() {
        param.setPaging(false);
        return this;
    }


    public <R> R execute(Function<Q, R> function) {
        return function.apply(param);
    }

    public NestConditional<Query<T, Q>> nest() {
        return new SimpleNestConditional<>(this, this.param.nest());
    }

    @Override
    public NestConditional<Query<T, Q>> orNest() {
        return new SimpleNestConditional<>(this, this.param.orNest());
    }


    @Override
    public Query<T, Q> and() {
        this.accepter = this::and;
        return this;
    }

    @Override
    public Query<T, Q> or() {
        this.accepter = this::or;
        return this;
    }

    @Override
    public Query<T, Q> accept(Term term) {
        param.addTerm(term);
        return this;
    }

    @Override
    public Query<T, Q> and(String column, String termType, Object value) {
        if (value == null) {
            return this;
        }
        this.param.and(column, termType, value);
        return this;
    }

    @Override
    public Query<T, Q> or(String column, String termType, Object value) {
        if (value == null) {
            return this;
        }
        this.param.or(column, termType, value);
        return this;
    }

    public Query<T, Q> where(String column, String termType, Object value) {
        if (value == null) {
            return this;
        }
        and(column, termType, value);
        return this;
    }

    @Override
    public Accepter<Query<T, Q>, Object> getAccepter() {
        return accepter;
    }

    public Query<T, Q> selectExcludes(String... columns) {
        return excludes(columns);
    }

    public Query<T, Q> select(String... columns) {
        param.includes(columns);
        return this;
    }

    public static <R, P extends QueryParam> Query<R, P> of(P param) {
        return new Query<>(param);
    }

    public static <R> Query<R, QueryParam> of() {
        return of(new QueryParam());
    }

}
