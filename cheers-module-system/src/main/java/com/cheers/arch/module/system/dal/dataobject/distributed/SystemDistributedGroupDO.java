package com.cheers.arch.module.system.dal.dataobject.distributed;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 系统分布式组 DO
 *
 * @author cheers
 */
@TableName("system_distributed_group")
@KeySequence("system_distributed_group_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemDistributedGroupDO extends BaseDO {

    /**
     * 分布式组ID
     */
    @TableId
    private Long id;

    /**
     * 分布式组名称
     */
    private String name;

    /**
     * 分布式组编码
     */
    private String code;

    /**
     * 组类型
     */
    private String groupType;

    /**
     * 描述
     */
    private String description;

    /**
     * 颜色
     */
    private String color;

    /**
     * 图标
     */
    private String icon;

    /**
     * 状态
     *
     * 枚举 {@link com.cheers.arch.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

} 