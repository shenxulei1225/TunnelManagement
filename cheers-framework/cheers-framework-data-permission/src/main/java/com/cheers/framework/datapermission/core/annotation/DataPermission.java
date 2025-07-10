package com.cheers.framework.datapermission.core.annotation;

import com.cheers.framework.datapermission.core.rule.DataPermissionRule;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * 可声明在类或者方法上，标识使用该数据权限规则
 * 如果声明在类上，则该类的所有方法都会进行数据权限的处理
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 当前类或方法是否开启数据权限
     * 即使不添加 @DataPermission 注解，默认是开启状态
     * 可通过设置 enable = false 禁用
     */
    boolean enable() default true;

    /**
     * 数据权限规则数组
     * 为空时，会使用默认的全局规则
     */
    Class<? extends DataPermissionRule>[] rules() default {};

} 