package com.cheers.module.dynamic.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.module.dynamic.dal.dataobject.base.TenantBaseDO;
import com.cheers.module.dynamic.enums.BusinessModelStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 业务模型 DO
 */
@TableName("dynamic_business_model")
@KeySequence("dynamic_business_model_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessModelDO extends TenantBaseDO {

    /**
     * 模型ID
     */
    @TableId
    private Long id;

    /**
     * 模型编码
     */
    private String code;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 模型配置（JSON格式）
     */
    private String config;

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 状态
     *
     * 枚举 {@link BusinessModelStatusEnum}
     */
    private BusinessModelStatusEnum status;

    /**
     * 备注
     */
    private String remark;
} 