package com.cheers.arch.module.dynamic.dal.dataobject.permission;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.module.dynamic.dal.dataobject.base.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态业务权限 DO
 */
@TableName("dynamic_permission")
@KeySequence("dynamic_permission_seq") 
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicPermissionDO extends TenantBaseDO {

    /**
     * 权限ID
     */
    @TableId
    private Long id;

    /**
     * 业务模型编码
     */
    private String modelCode;

    /**
     * 权限类型
     * 1: 模型权限
     * 2: 字段权限
     * 3: 数据权限
     */
    private Integer type;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 权限目标
     * 模型权限：模型编码
     * 字段权限：字段编码
     * 数据权限：数据ID
     */
    private String target;

    /**
     * 权限级别
     * 1: 只读
     * 2: 读写
     * 3: 管理
     */
    private Integer level;

    /**
     * 权限配置（JSON格式）
     * 包含详细的权限规则
     */
    private String config;

    /**
     * 状态
     * 0: 禁用
     * 1: 启用
     */
    private Integer status;
} 