package com.cheers.arch.module.system.dal.dataobject.pageconfig;

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
 * 页面配置 DO
 *
 * @author cheers
 */
@TableName("system_page_config")
@KeySequence("system_page_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageConfigDO extends BaseDO {

    /**
     * 配置ID
     */
    @TableId
    private Long id;
    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置数据
     */
    private String configData;
    /**
     * 业务名称
     */
    private String businessName;
    /**
     * 页面标题
     */
    private String pageTitle;
    /**
     * 模板类型
     */
    private String templateType;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 租户编号
     */
    private Long tenantId;

} 