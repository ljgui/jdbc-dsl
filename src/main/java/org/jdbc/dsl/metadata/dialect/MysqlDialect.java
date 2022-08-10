package org.jdbc.dsl.metadata.dialect;

/**
 * @Description:
 * @Author: ljgui
 * @since 1.0.0
 */
public class MysqlDialect implements Dialect {

    @Override
    public String getQuoteStart() {
        return "`";
    }

    @Override
    public String getQuoteEnd() {
        return "`";
    }

    @Override
    public boolean isColumnToUpperCase() {
        return false;
    }

    @Override
    public String getId() {
        return "mysql";
    }

    @Override
    public String getName() {
        return "Mysql";
    }
}
