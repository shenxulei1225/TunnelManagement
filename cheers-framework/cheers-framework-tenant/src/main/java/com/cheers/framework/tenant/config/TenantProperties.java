package com.cheers.framework.tenant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * 多租户配置
 */
@ConfigurationProperties(prefix = "cheers.tenant")
@Validated
@Data
public class TenantProperties {

    /**
     * 是否开启多租户
     */
    private Boolean enable = true;

    /**
     * 需要忽略多租户的表名
     * 即这些表不进行租户的自动过滤
     */
    private Set<String> ignoreTables;

    /**
     * 需要忽略多租户的请求
     * 即这些请求不进行租户的自动过滤
     */
    private Set<String> ignoreUrls;

    /**
     * 需要忽略多租户的访问请求
     * 即这些请求不进行租户的访问控制
     */
    private Set<String> ignoreVisitUrls;

    /**
     * 需要忽略多租户的缓存
     * 即这些缓存不进行租户隔离
     */
    private Set<String> ignoreCaches;

    /**
     * 租户字段名
     * 默认为 tenant_id
     */
    private String columnName = "tenant_id";

    /**
     * 默认租户ID
     * 默认为 1
     */
    @NotNull(message = "默认租户ID不能为空")
    private Long defaultTenantId = 1L;

    /**
     * 超级管理员租户ID
     * 超级管理员可以访问所有租户的数据
     */
    @NotNull(message = "超级管理员租户ID不能为空")
    private Long superTenantId = 0L;

    /**
     * 是否允许跨租户访问
     * 默认为false
     */
    private Boolean enableCrossTenant = false;

    /**
     * 允许跨租户访问的角色编码
     */
    private Set<String> allowCrossTenantRoles;

    /**
     * 数据隔离级别
     * 1: 默认,独立数据库
     * 2: 共享数据库,独立Schema
     * 3: 共享数据库,共享Schema,独立表
     * 4: 共享数据库,共享Schema,共享表,独立字段
     */
    @NotNull(message = "数据隔离级别不能为空")
    private Integer isolationLevel = 4;

} 