package com.cheers.framework.datapermission.core.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 部门数据权限规则枚举
 */
@Getter
@AllArgsConstructor
public enum DeptDataPermissionRuleEnum {

    /**
     * 全部数据权限
     */
    ALL(1, "全部数据权限"),

    /**
     * 指定部门数据权限
     */
    DEPT_CUSTOM(2, "指定部门数据权限"),

    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD(3, "部门及以下数据权限"),

    /**
     * 部门及以上数据权限
     */
    DEPT_AND_PARENT(4, "部门及以上数据权限"),

    /**
     * 仅本人数据权限
     */
    SELF(5, "仅本人数据权限");

    /**
     * 规则编号
     */
    private final Integer code;

    /**
     * 规则名称
     */
    private final String name;

} 