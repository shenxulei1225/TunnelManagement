package com.cheers.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户类型枚举
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    MEMBER(1, "会员"),
    ADMIN(2, "管理员");

    private final Integer value;
    private final String name;

    public static UserTypeEnum valueOf(Integer value) {
        return Arrays.stream(values())
                .filter(item -> item.value.equals(value))
                .findFirst()
                .orElse(null);
    }

} 