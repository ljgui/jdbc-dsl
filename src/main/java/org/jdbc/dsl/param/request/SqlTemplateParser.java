package org.jdbc.dsl.param.request;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.jdbc.dsl.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SqlTemplateParser {

    /**
     * 进行参数预编译的表达式:#{}
     */
    private static final Pattern PREPARED_PATTERN = Pattern.compile("(?<=#\\{)(.+?)(?=\\})");
    /**
     * 对象属性操作工具
     *
     * @see PropertyUtilsBean
     */
    protected PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
    @Getter
    @Setter
    private String template;

    @Getter
    @Setter
    private Object parameter;


    public SqlRequest parse() {
        List<Object> parameters = new ArrayList<>(64);
        Matcher prepared_matcher = PREPARED_PATTERN.matcher(template);
        //参数预编译sql
        while (prepared_matcher.find()) {
            String group = prepared_matcher.group();
            template = template.replaceFirst(StringUtils.concat("#\\{", group.replace("$", "\\$").replace("[", "\\[").replace("]", "\\]"), "\\}"), "?");
            Object obj = null;
            try {
                obj = propertyUtils.getProperty(parameter, group);
            } catch (Exception e) {
                log.error("", e);
            }
            parameters.add(obj);
        }
        return SqlRequests.prepare(template, parameters.toArray());
    }


    public static SqlRequest parse(String template,Object parameter) {
        SqlTemplateParser parser = new SqlTemplateParser();
        parser.parameter = parameter;
        parser.template = template;
        return parser.parse();
    }


}
