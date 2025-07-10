package com.cheers.framework.datapermission.core.aop;

import com.cheers.framework.datapermission.core.annotation.DataPermission;
import com.cheers.framework.datapermission.core.rule.DataPermissionRuleFactory;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * {@link DataPermission} 注解的 Advisor 实现类
 *
 * @author 芋道源码
 */
@Getter
public class DataPermissionAnnotationAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public DataPermissionAnnotationAdvisor(DataPermissionRuleFactory ruleFactory) {
        // 创建切面
        this.advice = new DataPermissionAnnotationInterceptor(ruleFactory);
        // 创建切点
        this.pointcut = new ComposablePointcut(new AnnotationMatchingPointcut(DataPermission.class, true))
                .union(new AnnotationMatchingPointcut(null, DataPermission.class));
    }
} 