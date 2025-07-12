package com.cheers.arch.module.dynamic.enums;

import com.cheers.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务模型状态枚举
 */
@Getter
@AllArgsConstructor
public enum BusinessModelStatusEnum implements ArrayValuable {

    DISABLE(0, "禁用"),
    ENABLE(1, "启用"),
    DRAFT(2, "草稿"),
    ARCHIVED(3, "已归档");

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
        return new Integer[]{DISABLE.status, ENABLE.status, DRAFT.status, ARCHIVED.status};
    }
} 