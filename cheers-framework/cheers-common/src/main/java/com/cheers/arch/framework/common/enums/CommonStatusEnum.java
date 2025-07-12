package com.cheers.arch.framework.common.enums;

import com.cheers.arch.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 *
 * @author cheers
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements ArrayValuable<Integer> {

    ENABLE(0, "开启"),
    DISABLE(1, "关闭"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(e -> e.status).toArray(Integer[]::new);

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
        return ARRAYS;
    }

    /**
     * 判断是否为禁用状态
     *
     * @param status 状态值
     * @return 是否禁用
     */
    public static boolean isDisable(Integer status) {
        return DISABLE.getStatus().equals(status);
    }

}
