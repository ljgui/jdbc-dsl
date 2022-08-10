package org.jdbc.dsl.param;

/**
 * 提供默认支持的查询条件类型,用于动态指定查询条件
 */
public interface TermType {
    /**
     * ==
     *
     */
    String eq      = "eq";
    /**
     * !=
     *
     */
    String not     = "not";
    /**
     * like
     *
     */
    String like    = "like";
    /**
     * not like
     *
     */
    String nlike   = "nlike";
    /**
     * >
     *
     */
    String gt      = "gt";
    /**
     * <
     *
     */
    String lt      = "lt";
    /**
     * >=
     *
     * @since 1.0
     */
    String gte     = "gte";
    /**
     * <=
     *
     */
    String lte     = "lte";
    /**
     * in
     *
     */
    String in      = "in";
    /**
     * notin
     *
     */
    String nin     = "nin";
    /**
     * =''
     *
     */
    String empty   = "empty";
    /**
     * !=''
     *
     */
    String nempty  = "nempty";
    /**
     * is null
     *
     */
    String isnull  = "isnull";
    /**
     * not null
     *
     */
    String notnull = "notnull";
    /**
     * between
     *
     */
    String btw     = "btw";
    /**
     * not between
     *
     */
    String nbtw    = "nbtw";

}