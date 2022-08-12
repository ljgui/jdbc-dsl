package org.jdbc.dsl.operator.fragments.term;


import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.utils.StringUtils;

public class SymbolTermFragmentBuilder extends AbstractTermFragmentBuilder {

    private final String symbol;

    public SymbolTermFragmentBuilder(String termType, String name, String symbol) {
        super(termType, name);
        this.symbol = symbol;
    }

    @Override
    public SqlFragments createFragments(String columnFullName, Term term) {
        return PrepareSqlFragments.of()
                .addSql(StringUtils.camelCase2UnderScoreCase(columnFullName), symbol, "?")
                .addParameter(term.getValue());
    }
}
