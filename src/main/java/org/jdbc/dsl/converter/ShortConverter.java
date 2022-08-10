package org.jdbc.dsl.converter;


import org.jdbc.dsl.utils.CastUtils;

/**
 * @Description: 把源对象转成short类型
 * @Author: ljgui
 */
public class ShortConverter implements Converter<Object,Short> {

    @Override
    public Short convert(Object source) {
        return CastUtils.castNumber(source).shortValue();
    }
}
