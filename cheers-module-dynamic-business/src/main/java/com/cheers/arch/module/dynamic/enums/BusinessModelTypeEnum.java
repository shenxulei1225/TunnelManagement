package com.cheers.arch.module.dynamic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务模型类型枚举
 */
@AllArgsConstructor
@Getter
public enum BusinessModelTypeEnum {

    SYSTEM(0, "系统"),
    CUSTOM(1, "自定义");

    /**
     * 类型值
     */
    private final Integer type;
    
    /**
     * 类型名称
     */
    private final String name;

    /**
     * 根据类型值获取枚举
     */
    public static BusinessModelTypeEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (BusinessModelTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为系统类型
     */
    public static boolean isSystem(Integer type) {
        return SYSTEM.getType().equals(type);
    }

    /**
     * 判断是否为自定义类型
     */
    public static boolean isCustom(Integer type) {
        return CUSTOM.getType().equals(type);
    }
} 