package com.cheers.framework.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

/**
 * Web 配置属性
 */
@ConfigurationProperties(prefix = "cheers.web")
@Validated
@Data
public class WebProperties {

    /**
     * API 前缀，实现所有 Controller 提供的 RESTFul API 的统一前缀
     *
     * 意义：通过该前缀，避免 API 提供的 RESTFul API 跟其它项目冲突，
     * 默认为 /api/
     */
    @NotEmpty(message = "API 前缀不能为空")
    private String apiPrefix = "/api/";

    /**
     * 跨域配置
     */
    private CorsProperties cors = new CorsProperties();

    /**
     * Admin UI 配置
     */
    private UiProperties ui = new UiProperties();

    /**
     * 文档配置
     */
    private DocProperties doc = new DocProperties();

    /**
     * App API 配置
     */
    private AppApiProperties appApi = new AppApiProperties();

    /**
     * 跨域配置
     */
    @Data
    public static class CorsProperties {

        /**
         * 是否允许跨域
         *
         * 默认为 false
         */
        private Boolean enable = false;

        /**
         * 允许跨域的域名
         *
         * 默认值为 *，即允许所有域名
         */
        private String allowOrigin = "*";

        /**
         * 允许跨域的请求方法
         */
        private String allowMethods = "GET,POST,PUT,DELETE,OPTIONS";

        /**
         * 允许跨域的请求头
         */
        private String allowHeaders = "*";

        /**
         * 是否允许发送Cookie
         */
        private Boolean allowCredentials = false;

        /**
         * 跨域检测有效期
         */
        private Long maxAge = 1800L;
    }

    /**
     * Admin UI 配置
     */
    @Data
    public static class UiProperties {

        /**
         * 访问地址
         *
         * 默认为 /admin-ui/
         */
        private String url = "/admin-ui/";

    }

    /**
     * 文档配置
     */
    @Data
    public static class DocProperties {

        /**
         * 是否开启文档
         */
        private Boolean enable = true;

        /**
         * 标题
         */
        private String title;

        /**
         * 描述
         */
        private String description;

        /**
         * 版本
         */
        private String version;

        /**
         * 扫描的包
         */
        @NotEmpty(message = "扫描的 package 不能为空")
        private String basePackage;

    }

    /**
     * App API 配置
     */
    @Data
    public static class AppApiProperties {

        /**
         * 是否开启
         */
        private Boolean enable = false;

        /**
         * 接口前缀
         */
        private String prefix = "/app-api";

        /**
         * 安全配置
         */
        private SecurityConfig security = new SecurityConfig();

        /**
         * 安全配置
         */
        @Data
        public static class SecurityConfig {

            /**
             * 认证头
             */
            private String authHeader = "Authorization";

            /**
             * Token 前缀
             */
            private String tokenPrefix = "Bearer";

        }

    }

    public AppApiProperties getAppApi() {
        return appApi;
    }

} 