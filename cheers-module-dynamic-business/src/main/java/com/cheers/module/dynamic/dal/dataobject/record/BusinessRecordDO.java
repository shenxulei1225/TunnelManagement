package com.cheers.arch.module.dynamic.dal.dataobject.record;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.module.dynamic.dal.dataobject.base.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 业务数据记录
 */
@TableName("dynamic_business_record")
@KeySequence("dynamic_business_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessRecordDO extends TenantBaseDO {

    /**
     * 记录ID
     */
    @TableId
    private Long id;

    /**
     * 业务模型编码
     */
    private String modelCode;

    /**
     * 父记录ID（树形结构使用）
     */
    private Long parentId;

    /**
     * 树路径（树形结构使用）
     */
    private String treePath;

    /**
     * 层级（树形结构使用）
     */
    private Integer level;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;

    /**
     * 数据JSON（存储所有字段值）
     */
    private String data;
} 