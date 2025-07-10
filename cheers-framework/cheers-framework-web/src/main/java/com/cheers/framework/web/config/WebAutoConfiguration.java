package com.cheers.framework.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnProperty(prefix = "cheers.web.cors", name = "enable", havingValue = "true")
    public CorsFilter corsFilter(WebProperties properties) {
        // 创建 CorsConfiguration 对象
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(properties.getCors().getAllowCredentials());
        config.addAllowedOriginPattern(properties.getCors().getAllowOrigin());
        config.addAllowedHeader(properties.getCors().getAllowHeaders());
        config.setMaxAge(properties.getCors().getMaxAge());
        String[] methods = properties.getCors().getAllowMethods().split(",");
        for (String method : methods) {
            config.addAllowedMethod(method);
        }

        // 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cheers.web.doc", name = "enable", havingValue = "true")
    public GroupedOpenApi defaultApi(WebProperties properties) {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .packagesToScan(properties.getDoc().getBasePackage().split(","))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cheers.web.doc", name = "enable", havingValue = "true")
    public OpenAPI customOpenAPI(WebProperties properties) {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getDoc().getTitle())
                        .version(properties.getDoc().getVersion())
                        .description(properties.getDoc().getDescription()));
    }

} 