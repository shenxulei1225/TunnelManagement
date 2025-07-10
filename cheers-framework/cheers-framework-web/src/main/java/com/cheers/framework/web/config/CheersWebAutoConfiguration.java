package com.cheers.framework.web.config;

import com.cheers.framework.web.core.filter.ApiRequestFilter;
import com.cheers.framework.web.core.filter.CacheRequestBodyFilter;
import com.cheers.framework.web.core.handler.GlobalExceptionHandler;
import com.cheers.framework.web.core.handler.GlobalResponseBodyHandler;
import com.cheers.framework.web.core.util.WebFrameworkUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;

@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class CheersWebAutoConfiguration implements WebMvcConfigurer {

    private final WebProperties webProperties;

    public CheersWebAutoConfiguration(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public GlobalResponseBodyHandler globalResponseBodyHandler() {
        return new GlobalResponseBodyHandler();
    }

    @Bean
    public WebFrameworkUtils webFrameworkUtils(WebProperties webProperties) {
        return new WebFrameworkUtils(webProperties);
    }

    // ========== Filter 相关 ==========

    /**
     * 创建 CorsFilter Bean，解决跨域问题
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterBean() {
        // 创建 CorsConfiguration 对象
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 设置访问源地址
        config.addAllowedHeader("*"); // 设置访问源请求头
        config.addAllowedMethod("*"); // 设置访问源请求方法
        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置
        return createFilterBean(new CorsFilter(source), Integer.MIN_VALUE);
    }

    /**
     * 创建 RequestBodyCacheFilter Bean，可重复读取请求内容
     */
    @Bean
    public FilterRegistrationBean<CacheRequestBodyFilter> requestBodyCacheFilter() {
        return createFilterBean(new CacheRequestBodyFilter(), Integer.MIN_VALUE + 1);
    }

    /**
     * 创建 ApiRequestFilter Bean，用于处理 API 请求
     */
    @Bean
    public FilterRegistrationBean<ApiRequestFilter> apiRequestFilter() {
        return createFilterBean(new ApiRequestFilter(webProperties), Integer.MIN_VALUE + 2);
    }

    private static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, int order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }

    /**
     * 配置 Spring MVC 路由规则
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurePathMatch(configurer, webProperties.getApiPrefix());
    }

    public static void configurePathMatch(PathMatchConfigurer configurer, String adminApi) {
        AntPathMatcher antPathMatcher = new AntPathMatcher(".");
        configurer.addPathPrefix(adminApi, clazz -> clazz.isAnnotationPresent(RestController.class))
                .setPathMatcher(antPathMatcher);
    }

} 