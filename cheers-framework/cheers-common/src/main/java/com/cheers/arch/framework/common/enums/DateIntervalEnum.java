package com.cheers.arch.framework.common.enums;

import com.cheers.arch.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 时间间隔枚举
 *
 * @author cheers
 */
@Getter
@AllArgsConstructor
public enum DateIntervalEnum implements ArrayValuable<Integer> {

    DAY(1, "天"),
    WEEK(7, "周"),
    MONTH(30, "月"),
    QUARTER(90, "季度"),
    YEAR(365, "年"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(e -> e.interval).toArray(Integer[]::new);

    /**
     * 时间间隔
     */
    private final Integer interval;
    /**
     * 时间间隔名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static DateIntervalEnum findByInterval(Integer interval) {
        return Arrays.stream(values()).filter(e -> e.interval.equals(interval))
                .findFirst().orElse(null);
    }

}