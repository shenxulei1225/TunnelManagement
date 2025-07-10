package com.cheers.framework.excel.core.annotations;

import java.lang.annotation.*;

/**
 * Excel属性注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

    /**
     * 属性名称
     */
    String value() default "";

    /**
     * 日期格式
     */
    String dateFormat() default "";

    /**
     * 数字格式
     */
    String numberFormat() default "";

    /**
     * 是否忽略该字段
     */
    boolean ignore() default false;

} 