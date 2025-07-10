package com.cheers.framework.tenant.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 多租户上下文持有器
 * 使用 TransmittableThreadLocal 实现线程变量的父子线程传递
 */
public class TenantContextHolder {

    /**
     * 当前租户编号
     */
    private static final ThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 是否忽略租户
     */
    private static final ThreadLocal<Boolean> IGNORE = new TransmittableThreadLocal<>();

    /**
     * 当前访问的租户编号
     * 用于跨租户访问时使用
     */
    private static final ThreadLocal<Long> VISIT_TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 获得租户编号
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * 设置租户编号
     */
    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * 清空租户编号
     */
    public static void clearTenantId() {
        TENANT_ID.remove();
    }

    /**
     * 获得是否忽略租户
     */
    public static Boolean isIgnore() {
        return IGNORE.get();
    }

    /**
     * 设置是否忽略租户
     */
    public static void setIgnore(Boolean ignore) {
        IGNORE.set(ignore);
    }

    /**
     * 清空是否忽略租户
     */
    public static void clearIgnore() {
        IGNORE.remove();
    }

    /**
     * 获得访问租户编号
     */
    public static Long getVisitTenantId() {
        return VISIT_TENANT_ID.get();
    }

    /**
     * 设置访问租户编号
     */
    public static void setVisitTenantId(Long visitTenantId) {
        VISIT_TENANT_ID.set(visitTenantId);
    }

    /**
     * 清空访问租户编号
     */
    public static void clearVisitTenantId() {
        VISIT_TENANT_ID.remove();
    }

    /**
     * 清空所有上下文
     */
    public static void clear() {
        clearTenantId();
        clearIgnore();
        clearVisitTenantId();
    }

    /**
     * 是否允许跨租户访问
     * 当访问租户编号与当前租户编号不一致时,返回true
     */
    public static boolean isAllowCrossTenant() {
        Long visitTenantId = getVisitTenantId();
        if (visitTenantId == null) {
            return false;
        }
        Long tenantId = getTenantId();
        return !visitTenantId.equals(tenantId);
    }

} 