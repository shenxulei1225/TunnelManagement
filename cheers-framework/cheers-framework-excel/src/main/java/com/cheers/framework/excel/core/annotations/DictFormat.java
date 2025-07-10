package com.cheers.framework.excel.core.annotations;

import java.lang.annotation.*;

/**
 * Excel 字典格式化
 * 用于Excel导出时,将字典值转换为字典标签
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