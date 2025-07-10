package com.cheers.framework.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Security 配置属性
 */
@ConfigurationProperties(prefix = "cheers.security")
@Data
public class SecurityProperties {

    /**
     * HTTP 请求时，访问令牌的请求 Header
     */
    private String tokenHeader = "Authorization";

    /**
     * HTTP 请求时，访问令牌的请求参数
     */
    private String tokenParam = "token";

    /**
     * 免登录的 URL 列表
     */
    private List<String> permitAllUrls = Collections.emptyList();

    /**
     * PasswordEncoder 加密复杂度，越高开销越大
     */
    private Integer passwordEncoderLength = 4;

} 