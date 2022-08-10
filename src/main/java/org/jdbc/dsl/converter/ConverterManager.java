package org.jdbc.dsl.converter;


import java.util.*;

/**
 * @Description:
 * @Author: ljgui
 */
public class ConverterManager {

    private static Map<Class,Converter> converters = new LinkedHashMap<>();

    static {
        converters.put(FloatConverter.class,new FloatConverter());
        converters.put(IntegerConverter.class,new IntegerConverter());
        converters.put(ShortConverter.class,new ShortConverter());
    }

    public static Converter getConverter(Class clazz){
        Converter converter = converters.get(clazz);
        if (converter==null){
            throw new UnsupportedOperationException("不支持的转换器: "+clazz.getName());
        }
        return converter;
    }


    public static Converter putIfAbsentConverter(Class<Converter> clazz){
        if (clazz==null){
            return null;
        }
        if (converters.containsKey(clazz)){
            return converters.get(clazz);
        }
        Converter converter = null;
        try {
            converter = clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("转换器必须要有无参构造函数: "+clazz.getName());
        }
        converters.put(clazz,converter);
        return converter;
    }





}
