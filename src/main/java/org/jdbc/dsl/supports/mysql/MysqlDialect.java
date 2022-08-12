package org.jdbc.dsl.supports.mysql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jdbc.dsl.core.FeatureType;
import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.supports.DialectFeacture;
import org.jdbc.dsl.supports.DialectType;

/**
 * @Author: ljgui
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    public DialectFeacture getDialectFeacture() {
        return MysqlDialectFeatures.getInstance();
    }

    @Override
    public String getId() {
        return DialectType.MYSQL.getId();
    }

    @Override
    public String getName() {
        return DialectType.MYSQL.getName();
    }

    @Override
    public FeatureType getType() {
        return DialectType.MYSQL;
    }


    private static final MysqlDialect instance = new MysqlDialect();
    public static MysqlDialect getInstance(){
        return instance;
    }

}
