package com.cheers.framework.datapermission.core.db;

import com.cheers.framework.datapermission.core.rule.DataPermissionRule;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

@Slf4j
class DataPermissionDatabaseInterceptorTest {

    @Mock
    private Executor executor;

    @Mock
    private MappedStatement ms;

    @Mock
    private BoundSql boundSql;

    @Mock
    private DataPermissionRule rule;

    private DataPermissionDatabaseInterceptor interceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new DataPermissionDatabaseInterceptor();
    }

    @Test
    void testBeforeQuery_NoRules() throws SQLException {
        // 准备参数
        Object parameter = new Object();
        RowBounds rowBounds = new RowBounds();
        ResultHandler resultHandler = null;

        // 调用
        interceptor.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);

        // 验证
        verify(boundSql, never()).getSql();
    }

    @Test
    void testBeforeQuery_NotSelectCommand() throws SQLException {
        // 准备参数
        Object parameter = new Object();
        RowBounds rowBounds = new RowBounds();
        ResultHandler resultHandler = null;

        // Mock 数据
        when(ms.getSqlCommandType()).thenReturn(SqlCommandType.INSERT);

        // 调用
        interceptor.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);

        // 验证
        verify(boundSql, never()).getSql();
    }

    @Test
    void testBeforeQuery_InvalidSql() throws SQLException {
        // 准备参数
        Object parameter = new Object();
        RowBounds rowBounds = new RowBounds();
        ResultHandler resultHandler = null;

        // Mock 数据
        when(ms.getSqlCommandType()).thenReturn(SqlCommandType.SELECT);
        when(boundSql.getSql()).thenReturn("invalid sql");

        // 调用
        interceptor.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);

        // 验证
        verify(boundSql, times(1)).getSql();
    }
} 