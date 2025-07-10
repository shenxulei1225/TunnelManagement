package com.cheers.framework.web.core.filter;

import com.cheers.framework.web.config.WebProperties;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * API 请求的过滤器，应用到所有的 API 请求中
 */
public class ApiRequestFilter extends OncePerRequestFilter {

    private final WebProperties webProperties;

    public ApiRequestFilter(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 只过滤 API 请求的地址
        return !request.getRequestURI().startsWith(webProperties.getApiPrefix());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 继续过滤
        filterChain.doFilter(request, response);
    }

} 