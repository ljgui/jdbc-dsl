package org.jdbc.dsl.param.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class SqlRequests {

    /**
     * 使用预编译参数创建SQL请求
     * <pre>{@code
     *     //错误的用法
     *     of("select * from order where id = '"+id+"'");
     *     //正确的用法
     *     of("select * from order where id = ?",id);
     * }</pre>
     *
     * @param sql        SQL
     * @param parameters 参数
     * @return SqlRequest
     */
    public static SqlRequest prepare(String sql, Object... parameters) {
        return PrepareSqlRequest.of(sql, parameters);
    }

    /**
     * 使用SQL模版进行创建SQL请求
     * <pre>{@code
     *  template("select * from order where id = #{id}",{id:test})
     * }</pre>
     *
     * @param template  模版
     * @param parameter 参数
     * @return SqlRequest
     */
    public static SqlRequest template(String template, Object parameter) {
        return SqlTemplateParser.parse(template, parameter);
    }
}
