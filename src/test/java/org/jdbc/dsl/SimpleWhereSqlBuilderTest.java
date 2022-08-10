package org.jdbc.dsl;

import lombok.Getter;
import lombok.Setter;
import org.jdbc.dsl.anotation.Column;
import org.jdbc.dsl.core.Query;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.param.QueryParam;
import org.jdbc.dsl.param.TermType;
import org.jdbc.dsl.utils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description TODO
 * @Author lujiangui
 * @Date 2022/3/9 11:31
 * @Version
 */
public class SimpleWhereSqlBuilderTest {

    @Test
    public void testSqlBuild(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of("SYS_USER");
        Query<Object, QueryParam> query = Query.of()
                .and("phone", TermType.like, "135")
                .includes("id","age","img");

        NestConditional<Query<Object, QueryParam>> nest = query.nest();
        nest.and("name", TermType.eq, "abc")
                .and("age", TermType.gte, 18);

        SqlRequest sqlRequest = sqlBuilder.build(query);
        Assert.assertEquals(sqlRequest.getSql(),"SELECT id,age,img FROM SYS_USER  WHERE PHONE LIKE ? AND (NAME=? AND AGE>=?)");
        Assert.assertEquals(sqlRequest.getParameters()[0],"135");
        Assert.assertEquals(sqlRequest.getParameters()[1],"abc");
        Assert.assertEquals(sqlRequest.getParameters()[2],18);
    }


    @Test
    public void testMethodRefrence(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
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

        SqlRequest sqlRequest = sqlBuilder.build(query);
        Assert.assertEquals(sqlRequest.getSql(),"SELECT DEPT_ID,AGE,USER_NAME FROM USER_ENTITY  WHERE ID=? AND USER_NAME LIKE ? AND DESCRITION LIKE ? OR ACCOUNT=? AND (AGE>=? AND AGE<=?)");
        Assert.assertEquals(sqlRequest.getParameters()[0],"6666");
        Assert.assertEquals(sqlRequest.getParameters()[1],"张三%");
        Assert.assertEquals(sqlRequest.getParameters()[2],"%旺财%");
        Assert.assertEquals(sqlRequest.getParameters()[3],"wangcai");
    }


    @Test
    public void testOrderByAndPaging(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
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

        //排序
        query.orderByAsc(UserEntity::getCreateTime);
        query.orderByDesc(UserEntity::getId);

        //分页
        query.doPaging(2,30);

        SqlRequest sqlRequest = sqlBuilder.build(query);

        System.out.println(sqlRequest.getSql());

        Assert.assertEquals(sqlRequest.getSql(),"SELECT DEPT_ID,AGE,USER_NAME FROM USER_ENTITY  WHERE ID=? AND USER_NAME LIKE ? AND DESCRITION LIKE ? OR ACCOUNT=? AND (AGE>=? AND AGE<=?) ORDER BY createTime asc,id desc LIMIT 30 OFFSET 30");
        Assert.assertEquals(sqlRequest.getParameters()[0],"6666");
        Assert.assertEquals(sqlRequest.getParameters()[1],"张三%");
        Assert.assertEquals(sqlRequest.getParameters()[2],"%旺财%");
        Assert.assertEquals(sqlRequest.getParameters()[3],"wangcai");
        System.out.println(sqlRequest.toNativeSql());
    }


    private JdbcTemplate jdbcTemplate;

    @Test
    public void testIn(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
        Query<Object, QueryParam> query = Query.of().includes(UserEntity::getAccount,UserEntity::getId)
                .in(UserEntity::getId,Arrays.asList("111","222","333"));
        SqlRequest sqlRequest = sqlBuilder.build(query);
        System.out.println(sqlRequest.toNativeSql());
    }


    @Test
    public void testNotIn(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
        Query<Object, QueryParam> query = Query.of().includes(UserEntity::getAccount,UserEntity::getId)
                .notIn(UserEntity::getId,Arrays.asList("111","222","333"));
        SqlRequest sqlRequest = sqlBuilder.build(query);
        System.out.println(sqlRequest.toNativeSql());
    }

    @Test
    public void testAnyValues(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
        Query<Object, QueryParam> query = Query.of().includes(UserEntity::getAccount,UserEntity::getId)
                .anyValues(UserEntity::getId,Arrays.asList("123","456"));
        SqlRequest sqlRequest = sqlBuilder.build(query);
        System.out.println(sqlRequest.toNativeSql());
    }


    @Test
    public void testExclude(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
        Query<Object, QueryParam> query = Query.of().includes(UserEntity.class)
                .excludes(UserEntity::getCreateTime,UserEntity::getAccount)
                .anyValues(UserEntity::getId,Arrays.asList("123","456"));
        SqlRequest sqlRequest = sqlBuilder.build(query);
        System.out.println(sqlRequest.toNativeSql());
    }


    @Test
    public void testNest(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
        Query<Object, QueryParam> query = Query.of().includes(UserEntity.class)
                .excludes(UserEntity::getCreateTime,UserEntity::getAccount)
                .anyValues(UserEntity::getId,Arrays.asList("123","456"));

        NestConditional<Query<Object, QueryParam>> nest = query.nest();
        nest.not("pre_hphm","未识别")
                .not("pre_hphm","-1")
                .not("pre_hphm","无牌")
                .not("pre_hphm","无车牌");

        SqlRequest sqlRequest = sqlBuilder.build(query);
        System.out.println(sqlRequest.toNativeSql());
    }

    @Test
    public void testGroupBy(){
        SimpleSelectSqlBuilder sqlBuilder = SimpleSelectSqlBuilder.of(UserEntity.class);
        Query<Object, QueryParam> query = Query.of().includes(UserEntity.class)
                .excludes(UserEntity::getCreateTime,UserEntity::getAccount)
                .anyValues(UserEntity::getId,Arrays.asList("123","456"))
                .groupby("abc")
                .groupby(UserEntity::getUserName)
                .orderByAsc(UserEntity::getUserName);

        SqlRequest sqlRequest = sqlBuilder.build(query);
        System.out.println(sqlRequest.toNativeSql());
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
