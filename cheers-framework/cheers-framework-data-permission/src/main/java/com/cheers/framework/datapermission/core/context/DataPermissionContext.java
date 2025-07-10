package com.cheers.framework.datapermission.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限上下文
 * 基于 ThreadLocal 实现，存储当前线程的数据权限信息
 */
public class DataPermissionContext {

    /**
     * 数据权限上下文
     */
    private static final ThreadLocal<Context> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 获得数据权限上下文
     *
     * @return 数据权限上下文
     */
    public static Context get() {
        Context context = CONTEXT.get();
        if (context == null) {
            context = new Context();
            CONTEXT.set(context);
        }
        return context;
    }

    /**
     * 清空数据权限上下文
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * 数据权限上下文
     * 内部类，用于存储数据权限相关的信息
     */
    @Data
    public static class Context {

        /**
         * 是否开启数据权限
         */
        private Boolean enable;

        /**
         * 表名与别名的映射
         * key：表名
         * value：别名
         */
        private Map<String, String> tableAliases = new HashMap<>();

        public Context() {
            this.enable = true;
        }

        public void disable() {
            this.enable = false;
        }

        public void enable() {
            this.enable = true;
        }

        public void addTableAlias(String tableName, String tableAlias) {
            this.tableAliases.put(tableName, tableAlias);
        }

        public String getTableAlias(String tableName) {
            return this.tableAliases.get(tableName);
        }

    }

} 