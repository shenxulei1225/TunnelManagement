package com.cheers.framework.datapermission.core.rule;

import net.sf.jsqlparser.expression.Expression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeptDataPermissionRuleTest {

    private final DeptDataPermissionRule rule = new DeptDataPermissionRule();

    @Test
    void testGetName() {
        assertEquals("部门数据权限", rule.getName());
    }

    @Test
    void testGetTableName() {
        assertEquals("sys_dept", rule.getTableName());
    }

    @Test
    void testGetExpressionWithAllPermission() {
        Expression expression = rule.getExpression();
        assertNull(expression);
    }

    @Test
    void testGetExpressionWithSelfPermission() {
        Expression expression = rule.getExpression();
        assertNotNull(expression);
        assertEquals("user_id = 1", expression.toString());
    }

    @Test
    void testGetExpressionWithDeptCustomPermission() {
        Expression expression = rule.getExpression();
        assertNotNull(expression);
        assertTrue(expression.toString().contains("dept_id IN"));
    }
} 