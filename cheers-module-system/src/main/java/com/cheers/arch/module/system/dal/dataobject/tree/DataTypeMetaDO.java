package com.cheers.arch.module.system.dal.dataobject.tree;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 数据类型元数据 DO
 *
 * @author cheers
 */
@TableName("system_data_type_meta")
@KeySequence("system_data_type_meta_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataTypeMetaDO extends TenantBaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 数据类型编码
     */
    private String dataType;

    /**
     * 数据类型名称
     */
    private String dataName;

    /**
     * 描述
     */
    private String description;

    /**
     * 图标
     */
    private String icon;

    /**
     * 颜色
     */
    private String color;

    /**
     * 对应的数据表名
     */
    private String tableName;

    /**
     * ID字段名
     */
    private String idField;

    /**
     * 名称字段名
     */
    private String nameField;

    /**
     * 状态字段名
     */
    private String statusField;

    /**
     * 扩展配置（JSON格式）
     */
    private String configJson;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
} 