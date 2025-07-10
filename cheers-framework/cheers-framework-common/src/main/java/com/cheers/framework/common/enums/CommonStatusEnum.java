package com.cheers.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭");

    private final Integer status;
    private final String name;

    public static CommonStatusEnum valueOf(Integer status) {
        return Arrays.stream(values())
                .filter(item -> item.status.equals(status))
                .findFirst()
                .orElse(null);
    }

} 