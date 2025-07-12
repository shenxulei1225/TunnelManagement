package com.cheers.arch.module.dynamic.dal.dataobject.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cheers.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户基础实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantBaseDO extends BaseDO {

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

} 