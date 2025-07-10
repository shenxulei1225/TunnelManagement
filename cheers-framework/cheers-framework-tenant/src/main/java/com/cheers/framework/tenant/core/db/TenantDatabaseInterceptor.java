package com.cheers.framework.tenant.core.db;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.cheers.framework.tenant.config.TenantProperties;
import com.cheers.framework.tenant.core.context.TenantContextHolder;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;

/**
 * 多租户的 SQL 拦截器
 */
@AllArgsConstructor
public class TenantDatabaseInterceptor implements TenantLineHandler {

    private final TenantProperties properties;

    @Override
    public Expression getTenantId() {
        return new LongValue(TenantContextHolder.getTenantId());
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumnName();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return properties.getIgnoreTables().contains(tableName);
    }

} 