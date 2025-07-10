package com.cheers.framework.datapermission.core.util;

import com.cheers.framework.datapermission.core.context.DataPermissionContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据权限工具类
 */
@Slf4j
public class DataPermissionUtils {

    private DataPermissionUtils() {
    }

    /**
     * 开启数据权限
     */
    public static void enable() {
        DataPermissionContext.get().setEnable(true);
    }

    /**
     * 禁用数据权限
     */
    public static void disable() {
        DataPermissionContext.get().setEnable(false);
    }

    /**
     * 获得数据权限上下文
     *
     * @return 数据权限上下文
     */
    public static DataPermissionContext.Context getContext() {
        return DataPermissionContext.get();
    }

    /**
     * 清空数据权限上下文
     */
    public static void clear() {
        DataPermissionContext.clear();
    }

} 