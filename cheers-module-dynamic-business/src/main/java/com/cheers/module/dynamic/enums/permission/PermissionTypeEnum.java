package com.cheers.arch.module.dynamic.enums.permission;

import com.cheers.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限类型枚举
 */
@Getter
@AllArgsConstructor
public enum PermissionTypeEnum implements ArrayValuable {

    MODEL(1, "模型权限"),
    FIELD(2, "字段权限"),
    DATA(3, "数据权限");

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 类型名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return new Integer[]{MODEL.type, FIELD.type, DATA.type};
    }
} 