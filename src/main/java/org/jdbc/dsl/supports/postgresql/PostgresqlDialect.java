package org.jdbc.dsl.supports.postgresql;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jdbc.dsl.core.FeatureType;
import org.jdbc.dsl.param.Term;
import org.jdbc.dsl.param.TermType;
import org.jdbc.dsl.render.SqlAppender;
import org.jdbc.dsl.supports.Dialect;
import org.jdbc.dsl.supports.DialectFeacture;
import org.jdbc.dsl.supports.DialectType;
import org.jdbc.dsl.utils.StringUtils;

import java.util.*;

/**
 * postgresql 方言
 * @Author ljgui
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostgresqlDialect implements Dialect {

    static Map<String,Mapper> termTypeMappers = new HashMap<>();

    static {
        termTypeMappers.put(TermType.eq, (term, wherePrefix) -> new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), "=#{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.not, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), "!=#{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.like, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), " LIKE #{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.nlike, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), " NOT LIKE #{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.isnull, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), " IS NULL").toString());
        termTypeMappers.put(TermType.notnull, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), " IS NOT NULL").toString());
        termTypeMappers.put(TermType.gt, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), ">#{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.lt, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), "<#{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.gte, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), ">=#{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.lte, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), "<=#{", wherePrefix, ".value}").toString());
        termTypeMappers.put(TermType.empty, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), "=''").toString());
        termTypeMappers.put(TermType.nempty, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), "!=''").toString());
        termTypeMappers.put(TermType.in, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), " IN(#{", wherePrefix,".value})").toString());
        termTypeMappers.put(TermType.nin, (term, wherePrefix) ->new SqlAppender().add(StringUtils.camelCase2UnderScoreCase(term.getColumn()).toUpperCase(), " NOT IN(#{", wherePrefix,".value})").toString());
        termTypeMappers.put(TermType.btw, (term, wherePrefix) -> {
            SqlAppender sqlAppender = new SqlAppender();
            List<Object> objects = param2list(term.getValue());
            if (objects.size() == 1)
                objects.add(objects.get(0));
            term.setValue(objects);
            sqlAppender.add(term.getColumn(), " ").addSpc("BETWEEN")
                    .add(" #{", wherePrefix, ".value[0]}")
                    .add(" AND ", "#{", wherePrefix, ".value[1]}");
            return sqlAppender.toString();
        });
    }


    protected static List<Object> param2list(Object value) {
        if (value == null) return new ArrayList<>();
        if (value instanceof List) return ((List) value);
        if (value instanceof Collection) return new ArrayList<>(((Collection) value));

        if (!(value instanceof Collection)) {
            if (value instanceof String) {
                String[] arr = ((String) value).split("[, ;]");
                Object[] objArr = new Object[arr.length];
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Object val = str;
                    if (StringUtils.isInt(str))
                        val = StringUtils.toInt(str);
                    else if (StringUtils.isDouble(str))
                        val = StringUtils.toDouble(str);
                    objArr[i] = val;
                }
                return Arrays.asList(objArr);
            } else if (value.getClass().isArray()) {
                return Arrays.asList(((Object[]) value));
            } else {
                return new ArrayList<>(Arrays.asList(value));
            }
        }
        return new ArrayList<>();
    }


    public static String wrapperWhere(String wherePrefix, Term term) {
        Mapper mapper = termTypeMappers.get(term.getTermType());
        if (mapper == null) mapper = termTypeMappers.get(TermType.eq);
        return mapper.accept(term,wherePrefix);
    }

    public static String doPaging(Integer pageIndex, Integer pageSize) {

        return new StringBuilder()
                .append(" LIMIT ")
                .append(pageSize)
                .append(" OFFSET ")
                .append(pageSize * (pageIndex-1)).toString();
    }


    @Override
    public String getQuoteStart() {
        return "\"";
    }

    @Override
    public String getQuoteEnd() {
        return "\"";
    }

    @Override
    public boolean isColumnToUpperCase() {
        return false;
    }

    @Override
    public DialectFeacture getDialectFeacture() {
        return PostgresqlDialectFeatures.getInstance();
    }

    @Override
    public String getId() {
        return DialectType.POSTGRESQL.getId();
    }

    @Override
    public String getName() {
        return DialectType.POSTGRESQL.getName();
    }

    @Override
    public FeatureType getType() {
        return DialectType.POSTGRESQL;
    }

    interface Mapper {
        String accept(Term term, String wherePrefix);
    }

    private static final PostgresqlDialect instance = new PostgresqlDialect();
    public static PostgresqlDialect getInstance(){
        return instance;
    }

}
