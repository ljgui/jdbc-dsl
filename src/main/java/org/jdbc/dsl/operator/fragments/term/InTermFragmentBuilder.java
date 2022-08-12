package org.jdbc.dsl.operator.fragments.term;


import org.jdbc.dsl.operator.fragments.EmptySqlFragments;
import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.param.Term;

import java.util.List;

public class InTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private final String symbol;

    public InTermFragmentBuilder(String termType, String name, boolean isNot) {
        super(termType, name);
        symbol = isNot ? "not in(" : "in(";
    }

    @Override
    public SqlFragments createFragments(String columnFullName, Term term) {

        List<Object> value = convertList(term);
        if (value == null || value.isEmpty()) {
            return EmptySqlFragments.INSTANCE;
        }
        int len = value.size();

        PrepareSqlFragments fragments = PrepareSqlFragments.of();
        //参数数量大于 500时,使用(column in (?,?,?) or column in(?,?,?))
        if (len > 500) {
            fragments.addSql("(");
        }
        fragments.addSql(columnFullName)
                 .addSql(symbol);

        int flag = 0;
        for (int i = 0; i < len; i++) {
            if (flag++ != 0) {
                fragments.addSql(",");
            }
            fragments.addSql("?");
            if (flag > 500 && i != len - 1) {
                flag = 0;
                fragments.addSql(") or")
                         .addSql(columnFullName)
                         .addSql(symbol);
            }
        }
        if (len > 500) {
            fragments.addSql(")");
        }
        return fragments
                .addSql(")")
                .addParameter(value);
    }
}
