package com.cheers.framework.datapermission.core.db;

import com.cheers.framework.datapermission.core.rule.DataPermissionRule;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据权限上下文
 * 基于 ThreadLocal 实现，存储数据权限的信息
 *
 * @author 芋道源码
 */
public class DataPermissionContextHolder {

    /**
     * 使用 TransmittableThreadLocal 实现线程变量的父子传递
     */
    private static final ThreadLocal<LinkedList<DataPermissionRule[]>> RULES = new TransmittableThreadLocal<>();

    /**
     * 获得当前的数据权限规则
     *
     * @return 数据权限规则数组
     */
    public static DataPermissionRule[] get() {
        LinkedList<DataPermissionRule[]> rules = RULES.get();
        return rules != null && !rules.isEmpty() ? rules.peek() : null;
    }

    /**
     * 添加数据权限规则
     *
     * @param rules 数据权限规则数组
     */
    public static void add(DataPermissionRule... rules) {
        LinkedList<DataPermissionRule[]> dataPermissionRules = RULES.get();
        if (dataPermissionRules == null) {
            dataPermissionRules = new LinkedList<>();
            RULES.set(dataPermissionRules);
        }
        dataPermissionRules.push(rules);
    }

    /**
     * 移除数据权限规则
     */
    public static void remove() {
        LinkedList<DataPermissionRule[]> dataPermissionRules = RULES.get();
        if (dataPermissionRules != null && !dataPermissionRules.isEmpty()) {
            dataPermissionRules.pop();
        }
        if (dataPermissionRules == null || dataPermissionRules.isEmpty()) {
            RULES.remove();
        }
    }

    /**
     * 清空数据权限规则
     */
    public static void clear() {
        RULES.remove();
    }

} 