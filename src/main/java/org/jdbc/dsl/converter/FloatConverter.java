package org.jdbc.dsl.converter;


import org.jdbc.dsl.utils.CastUtils;

/**
 * @Description:
 * @Author: ljgui
 */
public class FloatConverter implements Converter<Object,Float>{
    @Override
    public Float convert(Object source) {
        return CastUtils.castNumber(source).floatValue();
    }
}
