package com.cheers.arch.module.system.dal.dataobject.hierarchy;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 系统分级组 DO
 *
 * @author cheers
 */
@TableName("system_hierarchy_group")
@KeySequence("system_hierarchy_group_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyGroupDO extends TenantBaseDO {

    /**
     * 分级组ID
     */
    @TableId
    private Long id;

    /**
     * 分级组名称
     */
    private String name;

    /**
     * 分级组编码
     */
    private String code;

    /**
     * 父分级组ID
     */
    private Long parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 分级组路径
     */
    private String path;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 颜色
     */
    private String color;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 分组类型：EQUIPMENT(设备), PERSONNEL(人员), LOCATION(位置), MANAGEMENT(管理), DISPLAY(展示), MEASUREMENT(度量), STRUCTURE(结构), ENVIRONMENT(环境), GENERAL(通用)
     */
    private String groupType;

    /**
     * 用途类型：FIELD(字段分组), BUSINESS(业务分组)
     */
    private String usageType;

    /**
     * 状态
     *
     * 枚举 {@link com.cheers.arch.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

} 