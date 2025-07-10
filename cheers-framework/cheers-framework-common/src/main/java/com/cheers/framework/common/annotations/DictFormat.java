package com.cheers.framework.common.annotations;

import java.lang.annotation.*;

/**
 * 字典格式化注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictFormat {

    /**
     * 字典类型
     */
    String value();

} 