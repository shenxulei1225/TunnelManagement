package com.cheers.framework.datapermission.core.aop;

import com.cheers.framework.datapermission.core.annotation.DataPermission;
import com.cheers.framework.datapermission.core.rule.DataPermissionRule;
import com.cheers.framework.datapermission.core.rule.DataPermissionRuleFactory;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link DataPermission} 注解的拦截器
 * 1. 在执行方法前，获得对应的数据权限规则
 * 2. 在执行方法后，将数据权限规则移除
 */
@AllArgsConstructor
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

    private final DataPermissionRuleFactory ruleFactory;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 获得数据权限规则
        List<DataPermissionRule> rules = this.getRules(methodInvocation);
        try {
            // 如果没有规则，则直接执行
            if (rules == null || rules.isEmpty()) {
                return methodInvocation.proceed();
            }

            // 设置数据权限规则
            ruleFactory.setRules(rules);
            // 执行逻辑
            return methodInvocation.proceed();
        } finally {
            // 清空数据权限规则
            ruleFactory.clearRules();
        }
    }

    private List<DataPermissionRule> getRules(MethodInvocation methodInvocation) {
        // 获取方法
        Method method = methodInvocation.getMethod();
        // 获得方法上的 @DataPermission 注解
        DataPermission annotation = AnnotationUtils.findAnnotation(method, DataPermission.class);
        // 如果方法上没有，则从类上获取
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), DataPermission.class);
        }
        // 如果没有注解，则不进行数据权限处理
        if (annotation == null) {
            return null;
        }
        // 如果禁用，则不进行数据权限处理
        if (!annotation.enable()) {
            return null;
        }

        // 创建数据权限规则
        return ruleFactory.createRules(annotation.rules());
    }

} 