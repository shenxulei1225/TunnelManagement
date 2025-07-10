package com.cheers.framework.datapermission.core.aop;

import com.cheers.framework.datapermission.core.annotation.DataPermission;
import com.cheers.framework.datapermission.core.db.DataPermissionContextHolder;
import com.cheers.framework.datapermission.core.rule.DataPermissionRule;
import com.cheers.framework.datapermission.core.rule.DataPermissionRuleFactory;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@Slf4j
class DataPermissionAnnotationInterceptorTest {

    @Mock
    private MethodInvocation invocation;

    @Mock
    private DataPermissionRuleFactory ruleFactory;

    private DataPermissionAnnotationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new DataPermissionAnnotationInterceptor(ruleFactory);
    }

    @Test
    void testInvoke_WithoutAnnotation() throws Throwable {
        // 准备参数
        Method method = TestService.class.getMethod("methodWithoutAnnotation");
        when(invocation.getMethod()).thenReturn(method);

        // 调用
        interceptor.invoke(invocation);

        // 验证
        assertNull(DataPermissionContextHolder.get());
    }

    @Test
    void testInvoke_WithDisabledAnnotation() throws Throwable {
        // 准备参数
        Method method = TestService.class.getMethod("methodWithDisabledAnnotation");
        when(invocation.getMethod()).thenReturn(method);

        // 调用
        interceptor.invoke(invocation);

        // 验证
        assertNull(DataPermissionContextHolder.get());
    }

    @Test
    void testInvoke_WithEnabledAnnotation() throws Throwable {
        // 准备参数
        Method method = TestService.class.getMethod("methodWithEnabledAnnotation");
        when(invocation.getMethod()).thenReturn(method);

        // Mock 数据
        DataPermissionRule rule = mock(DataPermissionRule.class);
        when(rule.getExpression()).thenReturn(mock(Expression.class));
        when(ruleFactory.getRule(any())).thenReturn(rule);

        // 调用
        interceptor.invoke(invocation);

        // 验证
        assertEquals(1, DataPermissionContextHolder.get().length);
    }

    static class TestService {
        public void methodWithoutAnnotation() {
        }

        @DataPermission(enable = false)
        public void methodWithDisabledAnnotation() {
        }

        @DataPermission(enable = true)
        public void methodWithEnabledAnnotation() {
        }
    }
} 