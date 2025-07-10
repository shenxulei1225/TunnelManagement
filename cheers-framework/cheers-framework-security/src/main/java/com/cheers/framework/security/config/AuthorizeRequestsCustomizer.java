package com.cheers.framework.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * 自定义的 URL 的安全配置
 * 目的：每个 Maven Module 可以自定义规则
 */
public interface AuthorizeRequestsCustomizer {

    /**
     * 自定义 URL 的安全配置
     *
     * @param configurer 配置
     */
    void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configurer);

} 