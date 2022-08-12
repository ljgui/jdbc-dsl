# jdbc-dsl
简单的dsl工具，让sql拼接更优雅便捷

## 1、示例代码
```
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("u_user");//u_user 为表名称

        Query<Object, QueryParam> q = Query.of()
                .like$("phone",  "135")
                .includes("id","age","img")
                .in("name",Arrays.asList("1","2","3"))
                .doPaging(0,10);
        // 嵌套查询
        NestConditional<Query<Object, QueryParam>> nest = q.nest();
        nest.and("name", TermType.eq, "abc")
                .and("age", TermType.gte, 18);

        query.setParam(q.getParam());


        QueryOperatorParameter parameter = query.getParameter();

        SqlRequest request = DefaultQuerySqlBuilder.of(DialectType.MYSQL).build(parameter);
        String nativeSql = request.toNativeSql(); //生成本地sql
        String sql = request.getSql(); // 获取预编译sql
        System.out.println(nativeSql); 
        //SELECT id , img , age FROM u_user WHERE PHONE like '135%' and NAME in( '1' , '2' , '3' ) and ( NAME = 'abc' and AGE >= 18 ) limit 0,10
        System.out.println(sql); 
        //SELECT id , img , age FROM u_user WHERE PHONE like ? and NAME in( ? , ? , ? ) and ( NAME = ? and AGE >= ? ) limit ?,?
```

聚合函数查询示例：
```
        BuildParameterQueryOperator query = new BuildParameterQueryOperator("test");
        query.select(SelectColumn.of("age","total").max())
                .where(dsl ->
                        dsl.is("name", "1234").gte("age",20)
                                .gt(UserEntity::getId,"abc")
                );

```
