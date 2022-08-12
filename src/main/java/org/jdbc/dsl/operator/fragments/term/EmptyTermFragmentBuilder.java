package org.jdbc.dsl.operator.fragments.term;


import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.param.Term;

public class EmptyTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private final String symbol;

    public EmptyTermFragmentBuilder(String termType, String name, boolean not) {
        super(termType, name);
        symbol = not ? "!=" : "=";
    }

    @Override
    public PrepareSqlFragments createFragments(String columnFullName, Term term) {

        // column = ?
        return PrepareSqlFragments.of()
                .addSql(columnFullName, symbol, "''");
    }
}
