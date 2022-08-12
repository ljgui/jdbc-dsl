package org.jdbc.dsl;

import lombok.Getter;
import lombok.Setter;
import org.jdbc.dsl.anotation.Column;
import org.jdbc.dsl.core.Query;
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
        System.out.println(((PrepareSqlRequest) sqlRequest).toNativeSql());

    }


    @Test
    public void testColumn(){
        String[] column = PropertyUtils.getColumn(UserEntity.class);
        Arrays.stream(column).forEach(System.out::println);
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
