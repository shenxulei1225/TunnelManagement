package com.cheers.framework.datapermission.core.db;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.cheers.framework.datapermission.core.rule.DataPermissionRule;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 * 数据权限拦截器，通过 SQL 解析，实现数据权限的功能
 */
@Slf4j
public class DataPermissionDatabaseInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                          ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 获得数据权限规则
        DataPermissionRule[] rules = DataPermissionContextHolder.get();
        if (rules == null || rules.length == 0) {
            return;
        }

        // 获得 SQL 类型
        if (!SqlCommandType.SELECT.equals(ms.getSqlCommandType())) {
            return;
        }

        // 解析 SQL
        String sql = boundSql.getSql();
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (Exception e) {
            log.error("[beforeQuery][SQL({}) 解析失败]", sql, e);
            return;
        }
        if (!(statement instanceof Select)) {
            return;
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
            String newSql = select.toString();
            try {
                // 使用反射修改 BoundSql 中的 sql 字段
                Field sqlField = BoundSql.class.getDeclaredField("sql");
                sqlField.setAccessible(true);
                sqlField.set(boundSql, newSql);
            } catch (Exception e) {
                log.error("[beforeQuery][更新SQL失败] newSql: {}", newSql, e);
            }
        }
    }
}
 