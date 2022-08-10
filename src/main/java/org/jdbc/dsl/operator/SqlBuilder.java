package org.jdbc.dsl.operator;



import org.jdbc.dsl.metadata.dialect.PostgresqlDialect;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.render.SqlAppender;
import org.jdbc.dsl.utils.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @Author lujiangui
 * @since 1.0.0
 */
public interface SqlBuilder {

   default  void buildWhere(String prefix, List<Term> terms, SqlAppender appender) {
        if (terms == null || terms.isEmpty()) return;
        int index = -1;
        String prefixTmp = StringUtils.concat(prefix, StringUtils.isNullOrEmpty(prefix) ? "" : ".");
        for (Term term : terms) {
            index++;
            boolean nullTerm = StringUtils.isNullOrEmpty(term.getColumn());
            //不是空条件，值为空
            if (!nullTerm && StringUtils.isNullOrEmpty(term.getValue())) continue;
            //不是空条件，值为集合，但集合为空
            if (!nullTerm &&  term.getValue() instanceof Collection && ((Collection)term.getValue()).isEmpty()) continue;
            //是空条件，但是无嵌套
            if (nullTerm && term.getTerms().isEmpty()) continue;
            //添加类型，and 或者 or
            appender.add(StringUtils.concat(" ", term.getType().toString().toUpperCase(), " "));
            //用于sql预编译的参数名
            prefix = StringUtils.concat(prefixTmp, "terms[", index, "]");

            if (!term.getTerms().isEmpty()) {
                //构建嵌套的条件
                SqlAppender nest = new SqlAppender();
                buildWhere(prefix, term.getTerms(), nest);
                //如果嵌套结果为空,
                if (nest.isEmpty()) {
                    appender.removeLast();//删除最后一个（and 或者 or）
                    continue;
                }
                if (nullTerm) {
                    //删除 第一个（and 或者 or）
                    nest.removeFirst();
                }
                appender.add("(");
                if (!nullTerm){
                    appender.add(PostgresqlDialect.wrapperWhere(prefix,term));
                }
                appender.addAll(nest);
                appender.add(")");
            }else {
                if (!nullTerm){
                    appender.add(PostgresqlDialect.wrapperWhere(prefix,term));
                }
            }
        }

    }

}
