package com.cheers.framework.datapermission.config;

import com.cheers.framework.datapermission.core.aop.DataPermissionAnnotationAdvisor;
import com.cheers.framework.datapermission.core.db.DataPermissionDatabaseInterceptor;
import com.cheers.framework.datapermission.core.rule.DataPermissionRuleFactory;
import com.cheers.framework.mybatis.core.util.MyBatisUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限自动配置类
 */
@AutoConfiguration
public class CheersDataPermissionAutoConfiguration {

    /**
     * 创建 DataPermissionAnnotationAdvisor 对象
     */
    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor(DataPermissionRuleFactory ruleFactory) {
        return new DataPermissionAnnotationAdvisor(ruleFactory);
    }

    /**
     * 创建 DataPermissionDatabaseInterceptor 对象
     */
    @Bean
    public DataPermissionDatabaseInterceptor dataPermissionDatabaseInterceptor() {
        return new DataPermissionDatabaseInterceptor();
    }

    /**
     * 注册 MyBatis 拦截器
     */
    @Bean
    public Interceptor mybatisDataPermissionInterceptor(DataPermissionDatabaseInterceptor interceptor) {
        return MyBatisUtils.addInterceptor(interceptor);
    }
} 