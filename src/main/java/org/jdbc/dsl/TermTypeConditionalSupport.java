package org.jdbc.dsl;


import org.jdbc.dsl.param.Term;

public interface TermTypeConditionalSupport {

    /**
     * 条件接收器,用于处理接受到的条件并进行对应的操作如{@link Term}
     * @param <T>
     */
    interface Accepter<T,O> {
        T accept(String column, String termType, O value);
    }

    interface SimpleAccepter<T,O> {
        T accept(String column, O value);
    }

}