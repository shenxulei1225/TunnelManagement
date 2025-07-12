package com.cheers.arch.framework.common.enums;

import com.cheers.arch.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 全局用户类型枚举
 *
 * @author cheers
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum implements ArrayValuable<Integer> {

    MEMBER(1, "会员"),
    ADMIN(2, "管理员"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(e -> e.value).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static UserTypeEnum valueOf(Integer value) {
        return Arrays.stream(values()).filter(e -> e.value.equals(value))
                .findFirst().orElse(null);
    }

}
