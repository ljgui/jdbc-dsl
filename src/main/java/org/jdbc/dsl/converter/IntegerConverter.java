package org.jdbc.dsl.converter;


import org.jdbc.dsl.utils.CastUtils;

/**
 * @Description:
 * @Author: ljgui
 */
public class IntegerConverter implements Converter<Object,Integer>{

    @Override
    public Integer convert(Object source) {
        return CastUtils.castNumber(source).intValue();
    }
}
