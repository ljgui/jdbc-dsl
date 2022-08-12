package org.jdbc.dsl.operator.fragments.term;


import org.jdbc.dsl.operator.fragments.PrepareSqlFragments;
import org.jdbc.dsl.operator.fragments.SqlFragments;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.param.TermType;
import org.jdbc.dsl.utils.StringUtils;

public class LikeTermFragmentBuilder extends AbstractTermFragmentBuilder {
    private final boolean not;

    public LikeTermFragmentBuilder(boolean not) {
        super(not ? TermType.nlike : TermType.like, not ? "Not Like" : "Like");
        this.not = not;
    }

    @Override
    public SqlFragments createFragments(String columnFullName, Term term) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();
        fragments.addSql(StringUtils.camelCase2UnderScoreCase(columnFullName));
        if (not) {
            fragments.addSql("not");
        }
        fragments.addSql("like");
        fragments.addSql("?").addParameter(term.getValue());
        return fragments;
    }
}
