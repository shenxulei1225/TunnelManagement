package com.cheers.arch.module.dynamic.enums.permission;

import com.cheers.arch.framework.common.core.IntArrayValuable;
import lombok.Getter;

/**
 * 权限级别枚举
 */
@Getter
public enum PermissionLevelEnum implements IntArrayValuable {

    NONE(0, "无权限"),
    READ(1, "只读"),
    WRITE(2, "读写"),
    ADMIN(3, "管理");

    /**
     * 级别值
     */
    private final Integer level;

    /**
     * 级别名称
     */
    private final String name;

    PermissionLevelEnum(Integer level, String name) {
        this.level = level;
        this.name = name;
    }

    @Override
    public int[] array() {
        PermissionLevelEnum[] values = values();
        int[] array = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getLevel();
        }
        return array;
    }
} 