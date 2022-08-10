package org.jdbc.dsl.utils;


import org.jdbc.dsl.param.request.PrepareSqlRequest;
import org.jdbc.dsl.param.request.SqlRequest;
import org.jdbc.dsl.utils.time.DateFormatter;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class SqlUtils {

    public static String sqlParameterToString(Object[] parameters) {
        if (parameters == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Object param : parameters) {
            if (i++ != 0) {
                builder.append(",");
            }
            builder.append(param);
            builder.append("(");
            builder.append(param == null ? "null" : param.getClass().getSimpleName());
            builder.append(")");
        }
        return builder.toString();
    }


    public static void printSql(Logger log, SqlRequest sqlRequest) {
        if (log.isDebugEnabled()) {
            if (sqlRequest.isNotEmpty()) {
                boolean hasParameter = sqlRequest.getParameters() != null && sqlRequest.getParameters().length > 0;

                log.debug("==>  {}: {}", hasParameter ? "Preparing" : "  Execute", sqlRequest.getSql());
                if (hasParameter) {
                    log.debug("==> Parameters: {}", sqlParameterToString(sqlRequest.getParameters()));
                    if (sqlRequest instanceof PrepareSqlRequest) {
                        log.debug("==>     Native: {}", sqlRequest.toNativeSql());
                    }
                }
            }
        }
    }

    public static String getSql(Logger log, SqlRequest sqlRequest) {
        if (log.isDebugEnabled()) {
            if (sqlRequest.isNotEmpty()) {
                boolean hasParameter = sqlRequest.getParameters() != null && sqlRequest.getParameters().length > 0;

                log.info("==>  {}: {}", hasParameter ? "Preparing" : "  Execute", sqlRequest.getSql());
                if (hasParameter) {
                    log.info("==> Parameters: {}", sqlParameterToString(sqlRequest.getParameters()));
                    if (sqlRequest instanceof PrepareSqlRequest) {
                        log.info("==>     Native: {}", sqlRequest.toNativeSql());
                    }
                }
            }
        }
        return sqlRequest.toNativeSql();
    }

    public static String toNativeSql(String sql, Object... parameters) {
        if(parameters==null){
            return sql;
        }

        String[] stringParameter = new String[parameters.length];
        int len = 0;
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if (parameter instanceof Number) {
                stringParameter[i] = parameter.toString();
            } else if (parameter instanceof Date) {
                stringParameter[i] = "'" + DateFormatter.toString((Date) parameter,"yyyy-MM-dd HH:mm:ss") + "'";
            } else if (parameter == null) {
                stringParameter[i] = "null";
            } else if (parameter instanceof Collection){
                Object collect = ((Collection) parameter).stream().map(x -> "'" + x.toString() + "'").collect(Collectors.joining(","));
                stringParameter[i] = collect.toString();
            } else {
                stringParameter[i] = "'" + parameter + "'";
            }
            len += stringParameter.length;
        }
        StringBuilder builder = new StringBuilder(sql.length() + len + 16);
        //针对any values做特殊处理
        // TODO 有更优的方式？
        int anyIndex = 0;
        if (sql.contains("ANY (VALUES")){
            anyIndex = sql.indexOf("ANY (VALUES");
        }
        boolean isAnyValuesParam = false;
        int parameterIndex = 0;
        for (int i = 0, sqlLen = sql.length(); i < sqlLen; i++) {
            char c = sql.charAt(i);
            if (c == '?') {
                if (stringParameter.length >= parameterIndex) {
                    if (isAnyValuesParam){
                        String[] p = stringParameter[parameterIndex++].split(",");
                        String stringParam = Arrays.stream(p).filter(StringUtils::hasText).map(x -> "(".concat(x).concat(")")).collect(Collectors.joining(","));
                        builder.append(stringParam);
                    }else {
                        builder.append(stringParameter[parameterIndex++]);
                    }
                } else {
                    builder.append("null");
                }
                if (isAnyValuesParam){
                    isAnyValuesParam=false;
                }
            } else {
                builder.append(c);
            }
            if (anyIndex>0 && i == anyIndex){
                isAnyValuesParam=true;
            }
        }

        return builder.toString();
    }


}
