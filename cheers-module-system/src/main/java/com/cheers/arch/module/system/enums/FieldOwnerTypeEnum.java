package com.cheers.arch.module.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扩展字段归属类型
 */
@Getter
@AllArgsConstructor
public enum FieldOwnerTypeEnum {

    REGION("REGION", "区域"),
    DEVICE("DEVICE", "设备"),
    ALARM("ALARM", "告警");

    private final String code;
    private final String name;

    public static String getName(String code) {
        for (FieldOwnerTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value.name;
            }
        }
        return code;
    }
}
