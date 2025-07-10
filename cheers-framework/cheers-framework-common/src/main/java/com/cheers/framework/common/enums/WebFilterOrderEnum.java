package com.cheers.framework.common.enums;

/**
 * Web 过滤器顺序的枚举类，保证过滤器按照符合我们的预期
 *
 * 考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 enums 包下
 */
public interface WebFilterOrderEnum {

    int CORS_FILTER = Integer.MIN_VALUE;

    int TRACE_FILTER = CORS_FILTER + 1;

    int REQUEST_BODY_CACHE_FILTER = TRACE_FILTER + 1;

    int TENANT_CONTEXT_FILTER = REQUEST_BODY_CACHE_FILTER + 1;

    // Spring Security Filter 默认为 -100，可见 org.springframework.security.config.http.SecurityFilters 类
    int DEMO_FILTER = Integer.MIN_VALUE + 1000;

    int TENANT_SECURITY_FILTER = -90;

    int API_ACCESS_LOG_FILTER = -85;

    int XSS_FILTER = -80;

    int TENANT_IGNORE_TOKEN_FILTER = -70;

    int ACTIVITI_FILTER = -65;

    int FLOWABLE_FILTER = -65;

    int CACHE_REQUEST_FILTER = -60;

    int TENANT_TOKEN_FILTER = -50;

    int LOGGING_FILTER = -50;

} 