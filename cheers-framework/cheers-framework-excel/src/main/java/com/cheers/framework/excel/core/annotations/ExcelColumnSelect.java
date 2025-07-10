package com.cheers.framework.excel.core.annotations;

import com.cheers.framework.excel.core.function.ExcelColumnSelectFunction;

import java.lang.annotation.*;

/**
 * Excel 下拉框注解
 * 用于Excel导入时,提供下拉选择功能
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelColumnSelect {

    /**
     * 下拉框的值
     * 优先级高于 function
     */
    String[] value() default {};

    /**
     * 下拉框的值的函数
     * 优先级低于 value
     */
    Class<? extends ExcelColumnSelectFunction> function() default ExcelColumnSelectFunction.class;

} 