package com.cheers.module.dynamic.enums.field;

import com.cheers.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段状态枚举
 */
@Getter
@AllArgsConstructor
public enum FieldStatusEnum implements ArrayValuable {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    HIDDEN(2, "隐藏"),
    READONLY(3, "只读");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return new Integer[]{DISABLE.status, ENABLE.status, HIDDEN.status, READONLY.status};
    }
} 