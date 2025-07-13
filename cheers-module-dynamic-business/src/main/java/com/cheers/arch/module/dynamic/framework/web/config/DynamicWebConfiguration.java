package com.cheers.arch.module.dynamic.framework.web.config;

import com.cheers.arch.framework.swagger.config.CheersSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态业务模块的 web 组件的 Configuration
 *
 * @author Cheers
 */
@Configuration(proxyBeanMethods = false)
public class DynamicWebConfiguration {

    /**
     * 动态业务模块的 API 分组
     */
    @Bean
    public GroupedOpenApi dynamicGroupedOpenApi() {
        return CheersSwaggerAutoConfiguration.buildGroupedOpenApi("dynamic");
    }

} 