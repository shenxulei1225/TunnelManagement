package com.cheers.framework.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.cheers.framework.common.biz.system.tenant.TenantCommonApi;
import com.cheers.framework.common.enums.WebFilterOrderEnum;
import com.cheers.framework.mybatis.core.util.MyBatisUtils;
import com.cheers.framework.security.core.service.SecurityFrameworkService;
import com.cheers.framework.tenant.core.aop.TenantIgnore;
import com.cheers.framework.tenant.core.aop.TenantIgnoreAspect;
import com.cheers.framework.tenant.core.db.TenantDatabaseInterceptor;
import com.cheers.framework.tenant.core.service.TenantFrameworkService;
import com.cheers.framework.tenant.core.service.TenantFrameworkServiceImpl;
import com.cheers.framework.tenant.core.web.TenantContextWebFilter;
import com.cheers.framework.tenant.core.web.TenantVisitContextInterceptor;
import com.cheers.framework.web.config.WebProperties;
import com.cheers.framework.web.core.handler.GlobalExceptionHandler;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.cheers.framework.common.util.collection.CollectionUtils.convertList;

/**
 * 多租户的自动配置
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "cheers.tenant", value = "enable", matchIfMissing = true) // 允许使用 cheers.tenant.enable=false 禁用多租户
@EnableConfigurationProperties(TenantProperties.class)
public class TenantAutoConfiguration {

    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public TenantFrameworkService tenantFrameworkService(TenantCommonApi tenantApi) {
        return new TenantFrameworkServiceImpl(tenantApi);
    }

    // ========== AOP ==========

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    // ========== DB ==========

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties properties,
                                                                MybatisPlusInterceptor interceptor) {
        TenantDatabaseInterceptor handler = new TenantDatabaseInterceptor(properties);
        TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor();
        inner.setTenantLineHandler(handler);
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        interceptor.addInnerInterceptor(inner);
        return inner;
    }

    // ========== WEB ==========

    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter(TenantProperties tenantProperties) {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter(tenantProperties));
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);
        addIgnoreUrls(tenantProperties);
        return registrationBean;
    }

    /**
     * 如果 Controller 接口上，有 {@link TenantIgnore} 注解，那么添加到忽略的 URL 中
     *
     * @param tenantProperties 租户配置
     */
    private void addIgnoreUrls(TenantProperties tenantProperties) {
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @TenantIgnore 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(TenantIgnore.class)) {
                continue;
            }
            // 添加到忽略的 URL 中
            if (entry.getKey().getPatternsCondition() != null) {
                tenantProperties.getIgnoreUrls().addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition() != null) {
                Set<PathPattern> patterns = entry.getKey().getPathPatternsCondition().getPatterns();
                tenantProperties.getIgnoreUrls().addAll(
                        convertList(patterns, PathPattern::toString));
            }
        }
    }

    @Bean
    public TenantVisitContextInterceptor tenantVisitContextInterceptor(TenantProperties tenantProperties,
                                                                      SecurityFrameworkService securityFrameworkService) {
        return new TenantVisitContextInterceptor(tenantProperties, securityFrameworkService);
    }

    @Bean
    public WebMvcConfigurer tenantWebMvcConfigurer(TenantProperties tenantProperties,
                                                  TenantVisitContextInterceptor tenantVisitContextInterceptor) {
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tenantVisitContextInterceptor)
                        .excludePathPatterns(tenantProperties.getIgnoreVisitUrls().toArray(new String[0]));
            }
        };
    }

} 