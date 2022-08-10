package org.jdbc.dsl.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.jdbc.dsl.anotation.Column;
import org.jdbc.dsl.anotation.Ignore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description TODO
 * @Author lujiangui
 * @Date 2022/3/11 15:24
 * @Version
 */
public class PropertyUtils {


    public static final PropertyUtilsBean propertyBeanUtils = BeanUtilsBean.getInstance().getPropertyUtils();

    /**
     *  获取指定class对象的属性名称，并且进行驼峰转下划线再转大写
     * @return
     */
    public static String[]  getColumn(Class clazz){
        List<Field> fields = new ArrayList<>();
        getWithSuperclassField(fields,clazz);
        List<String> columns = new ArrayList<>();
        for (Field field : fields) {
            Ignore ignore = field.getAnnotation(Ignore.class);
            if (ignore!=null){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column==null || (StringUtils.isNullOrEmpty(column.name()) && StringUtils.isNullOrEmpty(column.value()))){
                columns.add(field.getName());
            }else {
                columns.add(StringUtils.isNullOrEmpty(column.value())?column.name():column.value());
            }
        }
        return columns.toArray(new String[0]);
    }

    /**
     * 获取包括父类的属性
     * @param clazz
     * @return
     */
    private static List<Field> getWithSuperclassField(List<Field> fields,Class clazz) {
        if (clazz.equals(Object.class)){
            return fields;
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return getWithSuperclassField(fields,clazz.getSuperclass());
    }


}
