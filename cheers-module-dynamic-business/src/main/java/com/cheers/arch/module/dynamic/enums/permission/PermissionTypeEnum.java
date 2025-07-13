package com.cheers.arch.module.dynamic.enums.permission;

import com.cheers.arch.framework.common.core.IntArrayValuable;
import lombok.Getter;

/**
 * 权限类型枚举
 */
@Getter
public enum PermissionTypeEnum implements IntArrayValuable {

    FIELD(0, "字段权限"),
    RECORD(1, "记录权限"),
    MODEL(2, "模型权限"),
    SYSTEM(3, "系统权限");

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 类型名称
     */
    private final String name;

    PermissionTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public int[] array() {
        PermissionTypeEnum[] values = values();
        int[] array = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getType();
        }
        return array;
    }
} 