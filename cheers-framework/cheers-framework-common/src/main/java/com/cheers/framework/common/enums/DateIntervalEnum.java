package com.cheers.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间间隔枚举
 */
@Getter
@AllArgsConstructor
public enum DateIntervalEnum {

    HOUR(1, "小时"),
    DAY(2, "天"),
    WEEK(3, "周"),
    MONTH(4, "月"),
    QUARTER(5, "季度"),
    YEAR(6, "年"),
    CUSTOM(7, "自定义");

    private final Integer type;
    private final String name;

} 