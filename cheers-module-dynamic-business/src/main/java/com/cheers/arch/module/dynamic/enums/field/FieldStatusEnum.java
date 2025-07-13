package com.cheers.arch.module.dynamic.enums.field;

import com.cheers.arch.framework.common.core.IntArrayValuable;
import lombok.Getter;

/**
 * 字段状态枚举
 */
@Getter
public enum FieldStatusEnum implements IntArrayValuable {

    ENABLE(0, "启用"),
    DISABLE(1, "禁用"),
    DELETED(2, "已删除"),
    ARCHIVED(3, "已归档");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态名称
     */
    private final String name;

    FieldStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    @Override
    public int[] array() {
        FieldStatusEnum[] values = values();
        int[] array = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getStatus();
        }
        return array;
    }
} 