package com.cheers.framework.tenant.core.web;

import com.cheers.framework.tenant.config.TenantProperties;
import com.cheers.framework.tenant.core.context.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 多租户 Context Web 过滤器
 * 将请求header中的tenantId设置到租户上下文中
 */
public class TenantContextWebFilter extends OncePerRequestFilter {

    private final TenantProperties properties;

    private static final String HEADER_TENANT_ID = "tenant-id";

    public TenantContextWebFilter(TenantProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
        // 如果是忽略的请求，则跳过
        String requestURI = request.getRequestURI();
        if (properties.getIgnoreUrls().contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // 获得租户编号
        String tenantIdStr = request.getHeader(HEADER_TENANT_ID);
        if (tenantIdStr != null) {
            Long tenantId = Long.valueOf(tenantIdStr);
            TenantContextHolder.setTenantId(tenantId);
        } else {
            // 如果未传递租户编号，则使用默认租户
            TenantContextHolder.setTenantId(properties.getDefaultTenantId());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // 清理租户上下文
            TenantContextHolder.clearTenantId();
        }
    }

} 