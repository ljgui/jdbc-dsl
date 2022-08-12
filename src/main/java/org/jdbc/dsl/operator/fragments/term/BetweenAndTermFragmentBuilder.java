package org.jdbc.dsl.operator.fragments.term;

import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ljgui
 */
public class BetweenAndTermFragmentBuilder extends AbstractTermFragmentBuilder{
    private final String symbol;

    public BetweenAndTermFragmentBuilder(String termType, String name, boolean isNot) {
        super(termType, name);
        symbol = isNot ? "not between ? and ?" : "between ? and ?";
    }

    @Override
    public SqlFragments createFragments(String columnFullName, Term term) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();
        List<Object> val = convertList(term);
        List<Object> values = new ArrayList<>(2);
        if (val.isEmpty()) {
            values.add(null);
            values.add(null);
        } else if (val.size() == 1) {
            values.add(val.get(0));
            values.add(val.get(0));
        } else {
            values.add(val.get(0));
            values.add(val.get(1));
        }

        return fragments
                .addSql(StringUtils.camelCase2UnderScoreCase(columnFullName))
                .addSql(symbol)
                .addParameter(values);
    }

}
