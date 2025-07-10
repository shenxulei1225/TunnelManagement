package com.cheers.framework.datapermission.core.rule;

import net.sf.jsqlparser.expression.Expression;

/**
 * 数据权限规则接口
 * 通过实现该接口，自定义数据权限规则
 */
public interface DataPermissionRule {

    /**
     * 获得规则名称
     * 用于调试查看
     */
    String getName();

    /**
     * 获得规则的表名称
     * 即哪些表需要应用这个规则
     */
    String getTableName();

    /**
     * 获得规则的表别名
     * 即表的别名，例如：table_name AS t
     */
    default String getTableAlias() {
        return null;
    }

    /**
     * 获得规则的 WHERE 条件表达式
     * 例如：t.dept_id IN (1, 2, 3)
     */
    Expression getExpression();

} 