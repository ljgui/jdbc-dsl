package org.jdbc.dsl.anotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字段映射,不加此注解的，默认是属性名驼峰转下划线转大写
 * @Author: ljgui
 * @since 1.0.0
 */
@Target({ METHOD, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Column {

    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";
}
