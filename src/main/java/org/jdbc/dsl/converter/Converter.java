package org.jdbc.dsl.converter;

/**
 * @Description: 数据转换
 * @Author: ljgui
 * @since 1.0.0
 */
public interface Converter<S,T> {
    T convert(S source);
}
