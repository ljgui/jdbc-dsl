package org.jdbc.dsl;

import lombok.Getter;
import lombok.Setter;
import org.jdbc.dsl.anotation.Column;
import org.jdbc.dsl.core.Query;
import org.jdbc.dsl.operator.FunctionColumn;
import org.jdbc.dsl.operator.dml.query.SelectColumn;
import org.jdbc.dsl.param.QueryParam;
import org.jdbc.dsl.param.TermType;
import org.jdbc.dsl.supports.DialectType;
import org.jdbc.dsl.supports.postgresql.PostgresqlDialect;
import org.jdbc.dsl.operator.dml.query.BuildParameterQueryOperator;
import org.jdbc.dsl.operator.fragments.query.DefaultQuerySqlBuilder;
import org.jdbc.dsl.operator.fragments.query.QueryOperatorParameter;
import org.jdbc.dsl.param.request.PrepareSqlRequest;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.utils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description TODO
 * @Author lujiangui
 * @Date 2022/3/9 11:31
 * @Version
 */
public class DefaultQuerySqlBuilderTest {


    @Test
    public void testFromParameter() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");

        query.setParam(Query.of()
                .select("id", "total")
                .where("name", "1234")
                .doPaging(10,0)
                .getParam());


        QueryOperatorParameter parameter = query.getParameter();

        Assert.assertEquals(parameter.getFrom(), "u_user");
        Assert.assertFalse(parameter.getSelect().isEmpty());
        Assert.assertFalse(parameter.getWhere().isEmpty());
        Assert.assertEquals(parameter.getPageIndex(), Integer.valueOf(10));
        Assert.assertEquals(parameter.getPageSize(), Integer.valueOf(0));
    }


    @Test
    public void testQuery() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");

        Query<Object, QueryParam> q = Query.of()
                .and("phone", TermType.like, "135")
                .includes("id","age","img")
                .doPaging(10,0);

        NestConditional<Query<Object, QueryParam>> nest = q.nest();
        nest.and("name", TermType.eq, "abc")
                .and("age", TermType.gte, 18);

        query.setParam(q.getParam());


        QueryOperatorParameter parameter = query.getParameter();

        Assert.assertEquals(parameter.getFrom(), "u_user");
        Assert.assertFalse(parameter.getSelect().isEmpty());
        Assert.assertFalse(parameter.getWhere().isEmpty());
        Assert.assertEquals(parameter.getPageIndex(), Integer.valueOf(10));
        Assert.assertEquals(parameter.getPageSize(), Integer.valueOf(0));
    }

    @Test
    public void testSqlBuild() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");

        Query<Object, QueryParam> q = Query.of()
                .like$("phone",  "135")
                .$like("phone",  "135")
                .$like$("phone",  "135")
                .includes("id","age","img")
                .in("name",Arrays.asList("1","2","3"))
                .doPaging(0,10);

        NestConditional<Query<Object, QueryParam>> nest = q.nest();
        nest.and("name", TermType.eq, "abc")
                .and("age", TermType.gte, 18);

        query.setParam(q.getParam());


        QueryOperatorParameter parameter = query.getParameter();

        SqlRequest request = DefaultQuerySqlBuilder.of(DialectType.MYSQL).build(parameter);
        String nativeSql = request.toNativeSql();
        String sql = request.getSql();
        System.out.println(nativeSql);
        System.out.println(sql);

    }


    @Test
    public void test() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");

        query.select("*")
//                .leftJoin("detail", join -> join.as("info").on("test.id=info.id"))
                .where(dsl ->
                        dsl.is("name", "1234")
                                .is("info.comment", "1234")
                                .gt(UserEntity::getId,"abc")
                )
//                .forUpdate()
                .paging(0, 10);

        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(DialectType.POSTGRESQL);

        long time = System.currentTimeMillis();

        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());

        System.out.println(System.currentTimeMillis() - time);

        Assert.assertNotNull(sqlRequest);
        Assert.assertNotNull(sqlRequest.getSql());
        System.out.println((sqlRequest).toNativeSql());

    }


    @Test
    public void testAlias() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");
        Query<Object, QueryParam> q = Query.of()
                .like$("phone",  "135")
                .includes("id","age","img")
                .in("name",Arrays.asList("1","2","3"))
                .doPaging(0,20);
        query.setParam(q.getParam());
        query.select("*")
                .select(SelectColumn.of("age","ageAlias"))
                .where(dsl ->
                        dsl.is("name", "1234")
                                .is("info.comment", "1234")
                                .gt(UserEntity::getId,"abc")
                )
                .paging(0, 10);


        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(DialectType.POSTGRESQL);
        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());
        System.out.println((sqlRequest).toNativeSql());

    }

    @Test
    public void testFunction() {
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");
        query.select(SelectColumn.of("age","total").max())
                .where(dsl ->
                        dsl.is("name", "1234").gte("age",20)
                                .gt(UserEntity::getId,"abc")
                );


        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(DialectType.POSTGRESQL);
        SqlRequest sqlRequest = sqlBuilder.build(query.getParameter());
        System.out.println((sqlRequest).toNativeSql());

    }


    @Test
    public void testMethodRefrence(){
        BuildParameterQueryOperator operator = new BuildParameterQueryOperator(UserEntity.class);
        Query<Object, QueryParam> query = Query.of()
                .and(UserEntity::getId,"6666")
                .like$(UserEntity::getUserName,"张三")
                .$like$(UserEntity::getDescrition,"旺财")
                .or(UserEntity::getAccount,"wangcai")
                .includes(UserEntity::getDeptId, UserEntity::getAge, UserEntity::getUserName);
        //子查询
        NestConditional<Query<Object, QueryParam>> nest = query.nest();
        nest.and(UserEntity::getAge,TermType.gte,18)
                .and(UserEntity::getAge,TermType.lte,25);

        operator.setParam(query.getParam());
        DefaultQuerySqlBuilder sqlBuilder = DefaultQuerySqlBuilder.of(DialectType.MYSQL);
        SqlRequest sqlRequest = sqlBuilder.build(operator.getParameter());
//        Assert.assertEquals(sqlRequest.getSql(),"SELECT \"DEPT_ID\",\"AGE\",\"USER_NAME\" FROM USER_ENTITY WHERE ID = ? and USER_NAME like ? and DESCRITION like ? or ACCOUNT = ? and ( AGE >= ? and AGE <= ? )");
        Assert.assertEquals(sqlRequest.getSql(),"SELECT `DEPT_ID`,`AGE`,`USER_NAME` FROM USER_ENTITY WHERE ID = ? and USER_NAME like ? and DESCRITION like ? or ACCOUNT = ? and ( AGE >= ? and AGE <= ? )");
        Assert.assertEquals(sqlRequest.getParameters()[0],"6666");
        Assert.assertEquals(sqlRequest.getParameters()[1],"张三%");
        Assert.assertEquals(sqlRequest.getParameters()[2],"%旺财%");
        Assert.assertEquals(sqlRequest.getParameters()[3],"wangcai");
    }



    @Setter
    @Getter
    public static class UserEntity extends Parent implements Serializable {
        private String id;
//        @Column("aa_bb")
        private String userName;
        private Integer age;
        private String deptId;
        private String descrition;
        private String account;
//        @Ignore
        private Date createTime;
    }

    public static class Parent{
        @Column("pre_hphm")
        private String hphm;
        private String driverImg;
    }


}
