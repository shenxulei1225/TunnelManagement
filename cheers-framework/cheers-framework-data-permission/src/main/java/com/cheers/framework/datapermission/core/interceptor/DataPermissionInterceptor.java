package com.cheers.framework.datapermission.core.interceptor;

import com.cheers.framework.datapermission.core.annotation.DataPermission;
import com.cheers.framework.datapermission.core.context.DataPermissionContext;
import com.cheers.framework.datapermission.core.rule.DataPermissionRule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.core.annotation.AnnotationUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

/**
 * 数据权限拦截器，通过 MyBatis 拦截器实现
 */
@RequiredArgsConstructor
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@SuppressWarnings("deprecation")
public class DataPermissionInterceptor implements Interceptor {

    /**
     * 数据权限规则列表
     */
    @Getter
    private final List<DataPermissionRule> rules;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 判断是否开启数据权限
        if (!DataPermissionContext.get().getEnable()) {
            return invocation.proceed();
        }

        // 获得 StatementHandler 对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        // 只处理查询语句
        if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()) {
            return invocation.proceed();
        }

        // 获得方法上的 @DataPermission 注解
        String methodName = mappedStatement.getId();
        Class<?> clazz = Class.forName(methodName.substring(0, methodName.lastIndexOf(".")));
        DataPermission dataPermission = AnnotationUtils.findAnnotation(clazz, DataPermission.class);
        if (dataPermission == null || !dataPermission.enable()) {
            return invocation.proceed();
        }

        // 获得 SQL 语句
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String sql = boundSql.getSql();

        // 解析 SQL 语句
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (!(statement instanceof Select)) {
            return invocation.proceed();
        }

        // 获得查询对象
        Select select = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

        // 获得表名
        String tableName = plainSelect.getFromItem().toString();

        // 构建权限条件
        Expression where = plainSelect.getWhere();
        for (DataPermissionRule rule : rules) {
            if (!rule.getTableName().equals(tableName)) {
                continue;
            }
            Expression expression = rule.getExpression();
            if (expression == null) {
                continue;
            }
            if (where == null) {
                where = expression;
            } else {
                where = new AndExpression(where, new Parenthesis(expression));
            }
        }

        // 设置权限条件
        if (where != null) {
            plainSelect.setWhere(where);
            metaObject.setValue("delegate.boundSql.sql", select.toString());
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
} 