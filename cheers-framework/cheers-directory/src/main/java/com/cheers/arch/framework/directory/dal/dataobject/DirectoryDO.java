package com.cheers.arch.framework.directory.dal.dataobject;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 通用目录 DO
 */
@TableName("system_directory")
@KeySequence("system_directory_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DirectoryDO extends TenantBaseDO {

    /**
     * 目录ID
     */
    @TableId
    private Long id;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 父目录ID（0表示根目录）
     */
    private Long parentId;

    /**
     * 目录名称
     */
    private String name;

    /**
     * 目录编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;

    /**
     * 扩展属性（JSON格式）
     */
    private String extData;
} 