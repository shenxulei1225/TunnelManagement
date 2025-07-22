package com.cheers.arch.module.system.dal.dataobject.test;

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
 * 测试树节点 DO
 *
 * @author 芋道源码
 */
@TableName("system_test_tree_node")
@KeySequence("system_test_tree_node_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTreeNodeDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    
    /**
     * 节点名称
     */
    private String name;
    
    /**
     * 节点类型
     */
    private String type;
    
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 父节点编号
     */
    private Long parentId;
    
    /**
     * 节点描述
     */
    private String description;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态
     *
     * 枚举 {@link com.cheers.arch.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    
    /**
     * 节点计数（关联子节点或数据数量）
     */
    private Integer count;
    
    /**
     * 扩展属性（JSON格式）
     */
    private String extraAttrs;

} 