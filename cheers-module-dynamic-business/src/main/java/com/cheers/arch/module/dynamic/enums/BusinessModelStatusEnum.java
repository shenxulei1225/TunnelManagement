package com.cheers.arch.module.dynamic.enums;

import com.cheers.arch.framework.common.core.IntArrayValuable;
import lombok.Getter;

/**
 * 业务模型状态枚举
 */
@Getter
public enum BusinessModelStatusEnum implements IntArrayValuable {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    ARCHIVED(2, "已归档"),
    DELETED(3, "已删除");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态名称
     */
    private final String name;

    BusinessModelStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    @Override
    public int[] array() {
        BusinessModelStatusEnum[] values = values();
        int[] array = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getStatus();
        }
        return array;
    }
} 