package com.cheers.uxdesigner.config;

import cn.iocoder.yudao.framework.security.config.AuthorizeRequestsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * UX Designer 模块的 Security 配置
 * 临时测试配置，用于API功能验证
 */
@Configuration("uxDesignerSecurityConfiguration")
public class SecurityConfiguration {

    @Bean("uxDesignerAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                // UX Designer 工作台API - 临时允许所有访问用于功能测试，不需要认证token
                registry.requestMatchers("/admin-api/uxdesigner/workspace/project/page").permitAll();
                registry.requestMatchers("/admin-api/uxdesigner/workspace/file/page").permitAll();
                registry.requestMatchers("/admin-api/uxdesigner/workspace/project/test").permitAll();
            }
        };
    }
} 