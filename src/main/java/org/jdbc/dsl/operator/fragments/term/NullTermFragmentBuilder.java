package org.jdbc.dsl.operator.fragments.term;


import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.param.Term;

public class NullTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private String symbol;

    public NullTermFragmentBuilder(String termType, String name, boolean isNot) {
        super(termType, name);
        symbol = isNot ? "is not" : "is";
    }

    @Override
    public PrepareSqlFragments createFragments(String columnFullName, Term term) {

        // column = ?
        return PrepareSqlFragments.of()
                .addSql(columnFullName, symbol, "null");
    }
}
