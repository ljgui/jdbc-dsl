package org.jdbc.dsl.operator.fragments.query;

import lombok.Getter;
import lombok.Setter;
import org.jdbc.dsl.operator.fragments.*;
import org.jdbc.dsl.param.Term;

import java.util.List;

/**
 * @Author: ljgui
 * @since 1.0.0
 */
public abstract class AbstractTermsFragmentBuilder<T> {

    @Setter
    @Getter
    private boolean useBlock = false;

    /**
     * 构造{@link PrepareSqlFragments}.
     *
     * @param parameter parameter
     * @param terms     terms
     * @return BlockSqlFragments
     * @see PrepareSqlFragments
     */
    private PrepareSqlFragments createPrepareFragments(T parameter, List<Term> terms) {
        PrepareSqlFragments fragments = PrepareSqlFragments.of();

        int index = 0;
        boolean termAvailable;
        boolean lastTermAvailable = false;
        for (Term term : terms) {
            index++;
            SqlFragments termFragments;
            //值为null时忽略条件
            termFragments = term.getValue() == null ? EmptySqlFragments.INSTANCE : createTermFragments(parameter, term);

            termAvailable = termFragments.isNotEmpty();
            if (termAvailable) {
                if (index != 1 && lastTermAvailable) {
                    //and or
                    fragments.addSql(term.getType().name());
                }
                fragments.addFragments(termFragments);
                lastTermAvailable = termAvailable;
            }

            List<Term> nest = term.getTerms();
            //嵌套条件
            if (nest != null && !nest.isEmpty()) {
                //递归....
                SqlFragments nestFragments = createTermFragments(parameter, nest);
                if (nestFragments.isNotEmpty()) {
                    //and or
                    if (termAvailable || lastTermAvailable) {
                        fragments.addSql(term.getType().name());
                    }
                    fragments.addSql("(");
                    fragments.addFragments(nestFragments);
                    fragments.addSql(")");
                    lastTermAvailable = true;
                    continue;
                }
            }
        }
        return fragments;
    }


    /**
     * 构造{@link BlockSqlFragments},通常用于分页的场景,可以获取到SQL的每一个组成部分.
     *
     * @param parameter parameter
     * @param terms     terms
     * @return BlockSqlFragments
     * @see BlockSqlFragments
     */
    private BlockSqlFragments createBlockFragments(T parameter, List<Term> terms) {
        BlockSqlFragments fragments = BlockSqlFragments.of();

        int index = 0;
        boolean termAvailable;
        boolean lastTermAvailable = false;
        for (Term term : terms) {
            index++;
            SqlFragments termFragments;
            termFragments = term.getValue() == null ? EmptySqlFragments.INSTANCE : createTermFragments(parameter, term);
            termAvailable = termFragments.isNotEmpty();
            if (termAvailable) {
                BlockSqlFragments termBlock = BlockSqlFragments.of();
                if (index != 1 && lastTermAvailable) {
                    //and or
                    termBlock.addBlock(FragmentBlock.before, term.getType().name());
                }
                termBlock.addBlock(FragmentBlock.term, termFragments);
                fragments.addBlock(FragmentBlock.term, termBlock);
                lastTermAvailable = termAvailable;
            }
            BlockSqlFragments nestBlock = BlockSqlFragments.of();

            List<Term> nest = term.getTerms();
            //嵌套条件
            if (nest != null && !nest.isEmpty()) {
                //递归....
                SqlFragments nestFragments = createTermFragments(parameter, nest);
                if (nestFragments.isNotEmpty()) {
                    //and or
                    if (termAvailable || lastTermAvailable) {
                        nestBlock.addBlock(FragmentBlock.before, term.getType().name());
                    }
                    nestBlock.addBlock(FragmentBlock.before, "(");
                    nestBlock.addBlock(FragmentBlock.term, nestFragments);
                    nestBlock.addBlock(FragmentBlock.after, ")");

                    fragments.addBlock(FragmentBlock.term, nestBlock);
                    lastTermAvailable = true;
                    continue;
                }
            }


        }

        return fragments;
    }


    protected SqlFragments createTermFragments(T parameter, List<Term> terms) {
        return isUseBlock() ? createBlockFragments(parameter, terms) : createPrepareFragments(parameter, terms);
    }


    /**
     * 构造单个条件的SQL片段,方法无需处理{@link Term#getTerms()}.
     * <p>
     * 如果{@link Term#getValue()}为{@code null},此方法不会被调用.
     *
     * @param parameter 参数
     * @param term      条件
     * @return SqlFragments
     */
    protected abstract SqlFragments createTermFragments(T parameter, Term term);

}
