package org.jdbc.dsl.anotation;

import org.jdbc.dsl.converter.FloatConverter;
import org.jdbc.dsl.converter.IntegerConverter;
import org.jdbc.dsl.converter.ShortConverter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 转换器
 * @see Converter
 * @see FloatConverter
 * @see IntegerConverter
 * @see ShortConverter
 * @since 1.0.0
 */
@Target({ METHOD, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Converter {

    Class<org.jdbc.dsl.converter.Converter > name() ;

}
