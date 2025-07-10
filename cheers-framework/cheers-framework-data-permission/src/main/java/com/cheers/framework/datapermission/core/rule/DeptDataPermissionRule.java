package com.cheers.framework.datapermission.core.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cheers.framework.common.util.collection.SetUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 部门数据权限规则实现
 */
@Slf4j
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class DeptDataPermissionRule implements DataPermissionRule {

    /**
     * 部门字段名
     */
    private static final String DEPT_COLUMN_NAME = "dept_id";
    
    /**
     * 用户字段名
     */
    private static final String USER_COLUMN_NAME = "user_id";

    /**
     * 所有部门编号的缓存
     * value：部门编号集合
     */
    private static final Map<Integer, Set<Long>> DEPT_ID_CACHE = new HashMap<>();

    @Override
    public String getName() {
        return "部门数据权限";
    }

    @Override
    public String getTableName() {
        return "sys_dept";
    }

    @Override
    public Expression getExpression() {
        // 获取当前用户的数据权限规则
        DeptDataPermissionRuleEnum rule = getPermissionRule();
        if (rule == null) {
            return null;
        }
        // 获取当前用户编号
        Long userId = getCurrentUserId();
        // 获取当前用户的部门编号
        Long deptId = getCurrentUserDeptId();
        
        // 构建查询条件
        Expression expression = null;
        Set<Long> deptIds = new HashSet<>();
        switch (rule) {
            case ALL:
                return null;
            case DEPT_CUSTOM:
                deptIds.addAll(getCustomDeptIds());
                break;
            case DEPT_AND_CHILD:
                deptIds.addAll(getDeptAndChildDeptIds(deptId));
                break;
            case DEPT_AND_PARENT:
                deptIds.addAll(getDeptAndParentDeptIds(deptId));
                break;
            case SELF:
                // 只有自己的数据权限
                expression = new EqualsTo(new Column(USER_COLUMN_NAME), new LongValue(userId));
                break;
        }
        
        if (CollUtil.isNotEmpty(deptIds)) {
            Expression deptExpression = new InExpression(new Column(DEPT_COLUMN_NAME),
                    new ExpressionList(deptIds.stream().map(LongValue::new).collect(Collectors.toList())));
            expression = expression != null ? new OrExpression(expression, deptExpression) : deptExpression;
        }
        return expression;
    }

    /**
     * 获取当前用户的数据权限规则
     * 
     * @return 数据权限规则
     */
    protected DeptDataPermissionRuleEnum getPermissionRule() {
        // TODO: 从Spring Security上下文中获取
        return DeptDataPermissionRuleEnum.ALL;
    }

    /**
     * 获取当前用户编号
     * 
     * @return 用户编号
     */
    protected Long getCurrentUserId() {
        // TODO: 从Spring Security上下文中获取
        return 1L;
    }

    /**
     * 获取当前用户的部门编号
     * 
     * @return 部门编号
     */
    protected Long getCurrentUserDeptId() {
        // TODO: 从Spring Security上下文中获取
        return 100L;
    }

    /**
     * 获取自定义部门的数据权限
     * 
     * @return 部门编号集合
     */
    protected Set<Long> getCustomDeptIds() {
        // TODO: 从Spring Security上下文中获取
        return SetUtils.asSet(100L, 101L, 102L);
    }

    /**
     * 获取部门及子部门的数据权限
     * 
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    protected Set<Long> getDeptAndChildDeptIds(Long deptId) {
        return DEPT_ID_CACHE.computeIfAbsent(3, key -> SetUtils.asSet(100L, 101L, 102L, 103L, 104L));
    }

    /**
     * 获取部门及父部门的数据权限
     * 
     * @param deptId 部门编号
     * @return 部门编号集合
     */
    protected Set<Long> getDeptAndParentDeptIds(Long deptId) {
        return DEPT_ID_CACHE.computeIfAbsent(4, key -> SetUtils.asSet(100L, 99L, 98L));
    }
} 