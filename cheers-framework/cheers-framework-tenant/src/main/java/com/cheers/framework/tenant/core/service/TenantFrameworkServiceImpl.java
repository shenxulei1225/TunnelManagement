package com.cheers.framework.tenant.core.service;

import com.cheers.framework.common.biz.system.tenant.TenantCommonApi;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 租户框架 Service 实现类
 */
@RequiredArgsConstructor
public class TenantFrameworkServiceImpl implements TenantFrameworkService {

    private final TenantCommonApi tenantApi;

    @Override
    public List<Long> getTenantIds() {
        return tenantApi.getTenantIds();
    }

    @Override
    public void validTenant(Long id) {
        tenantApi.validTenant(id);
    }

} 