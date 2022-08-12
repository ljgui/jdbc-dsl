package org.jdbc.dsl.operator.fragments.term;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jdbc.dsl.operator.fragments.TermFragmentBuilder;
import org.jdbc.dsl.param.Term;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象SQL条件片段构造器，实现{@link TermFragmentBuilder},提供常用的模版方法
 *
 * @see TermFragmentBuilder
 */
@AllArgsConstructor
public abstract class AbstractTermFragmentBuilder implements TermFragmentBuilder {

    @Getter
    private final String termType;

    @Getter
    private final String name;


    /**
     * 尝试转换条件值为List,如果值为字符串则按,分割.
     *
     * @param term   条件
     * @return List值
     */
    @SuppressWarnings("all")
    protected List<Object> convertList(Term term) {
        Object value = term.getValue();
        if (value == null) {
            return Collections.emptyList();
        }
        //逗号分割自动转为list,比如在 in查询时,前端直接传入1,2,3即可.
        //todo 支持转义: 1,2\,3,4 => ["1","2,3","4"]
        if (value instanceof String) {
            return Arrays.asList(((String) value).split(","));
        }

        //数组
        if (value instanceof Object[]) {
           return Arrays.asList(((Object[]) value));
        }

        //集合
        if (value instanceof Collection) {
            return ((Collection<Object>) value)
                    .stream()
                    .collect(Collectors.toList());
        }
        //单个值
        return Arrays.asList(value);
    }

}
