package com.cheers.arch.module.system.dal.dataobject.domain;

import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 领域模型 DO
 *
 * @author cheers
 */
@TableName(value = "system_domain", autoResultMap = true)
@KeySequence("system_domain_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainDO extends TenantBaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 领域名称
     */
    private String name;

    /**
     * 领域编码
     */
    private String code;

    /**
     * 父领域ID
     *
     * 关联 {@link #id}
     */
    private Long parentId;

    /**
     * 领域描述
     */
    private String description;

    /**
     * 领域类型
     */
    private String type;

    /**
     * 状态
     * 0: 禁用, 1: 启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    // ================ 新增字段（对标Category功能） ================

    /**
     * 系统只读标识
     * 0: 普通领域, 1: 系统只读领域
     */
    private Integer readonly;

    /**
     * 完整路径（用于快速查询子树）
     * 格式：1/2/3
     */
    private String treePath;

    /**
     * 层级深度
     * 根节点为1，依次递增
     */
    private Integer level;

    /**
     * 直接字段数量
     */
    private Integer fieldCount;

    /**
     * 直接子领域数量
     */
    private Integer childCount;

    /**
     * 总字段数量（包含子领域）
     */
    private Integer totalFieldCount;

    /**
     * 总子领域数量（包含所有后代）
     */
    private Integer totalChildCount;

} 