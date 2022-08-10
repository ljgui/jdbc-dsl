package org.jdbc.dsl.metadata.dialect;


import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.RDBFeatureType;
import org.jdbc.dsl.utils.StringUtils;

/**
 * 数据库方言
 *
 * @see MysqlDialect
 * @see PostgresqlDialect
 * @since 1.0.0
 */
public interface Dialect extends Feature {

    @Override
    default RDBFeatureType getType() {
        return RDBFeatureType.dialect;
    }

    String getQuoteStart();

    String getQuoteEnd();

    default String clearQuote(String string){
        if (string == null || string.isEmpty()) {
            return string;
        }
        if (string.contains(".")) {
            String[] arr = string.split("[.]");
            for (int i = 0; i < arr.length; i++) {
                arr[i] = doClearQuote(arr[i]);
            }
            return String.join(".", arr);
        }
        return doClearQuote(string);
    }

    default String doClearQuote(String string) {
        if (string.startsWith(getQuoteStart())) {
            string = string.substring(getQuoteStart().length());
        }
        if (string.endsWith(getQuoteEnd())) {
            string = string.substring(0, string.length() - getQuoteEnd().length());
        }
        return string;
    }


    boolean isColumnToUpperCase();

    default String quote(String keyword, boolean changeCase) {
        if (keyword.startsWith(getQuoteStart()) && keyword.endsWith(getQuoteEnd())) {
            return keyword;
        }
        return getQuoteStart().concat(isColumnToUpperCase() && changeCase ? keyword.toUpperCase() : keyword).concat(getQuoteEnd());
    }

    default String quote(String keyword) {
        return quote(keyword, true);
    }

    default String buildColumnFullName(String tableName, String columnName) {
        if (columnName.contains(".")) {
            return columnName;
        }
        if (StringUtils.isNullOrEmpty(tableName)) {
            return StringUtils.concat(getQuoteStart(), isColumnToUpperCase() ? columnName.toUpperCase() : columnName, getQuoteEnd());
        }
        return StringUtils.concat(tableName, ".", getQuoteStart(), isColumnToUpperCase() ? columnName.toUpperCase() : columnName, getQuoteEnd());
    }

    Dialect MYSQL = new MysqlDialect();
    Dialect POSTGRES = new PostgresqlDialect();

}
