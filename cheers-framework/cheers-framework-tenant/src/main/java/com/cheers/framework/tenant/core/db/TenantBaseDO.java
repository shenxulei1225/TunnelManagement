package com.cheers.framework.tenant.core.db;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 拓展多租户的 BaseDO 基类
 */
@Data
public abstract class TenantBaseDO {

    /**
     * 多租户编号
     */
    @TableField("tenant_id")
    private Long tenantId;

} 