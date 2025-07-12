package com.cheers.module.dynamic.enums.permission;

import com.cheers.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限级别枚举
 */
@Getter
@AllArgsConstructor
public enum PermissionLevelEnum implements ArrayValuable {

    READ(1, "只读"),
    WRITE(2, "读写"),
    MANAGE(3, "管理");

    /**
     * 级别值
     */
    private final Integer level;

    /**
     * 级别名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return new Integer[]{READ.level, WRITE.level, MANAGE.level};
    }
} 