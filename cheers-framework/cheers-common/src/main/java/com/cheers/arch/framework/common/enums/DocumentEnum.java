package com.cheers.arch.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档地址
 *
 * @author cheers
 */
@Getter
@AllArgsConstructor
public enum DocumentEnum {

    REDIS_INSTALL("https://redis.io/docs/getting-started/", "Redis 安装文档"),
    TENANT("tenant.md", "SaaS 多租户文档"),
    ;

    private final String url;
    private final String memo;

}
