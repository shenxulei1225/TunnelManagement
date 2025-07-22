package com.cheers.arch.module.system.dal.dataobject.pagedesigner;

import java.time.LocalDateTime;

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
 * 页面设计 DO
 *
 * @author cheers
 */
@TableName("system_page_design")
@KeySequence("system_page_design_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDesignDO extends TenantBaseDO {

    /**
     * 页面设计ID
     */
    @TableId
    private String id;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面路径
     */
    private String pagePath;

    /**
     * 菜单标题
     */
    private String menuTitle;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 页面描述
     */
    private String description;

    /**
     * 页面类型
     */
    private String pageType;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 状态
     */
    private String status;

    /**
     * 构建状态
     */
    private String buildStatus;

    /**
     * 组件配置(JSON)
     */
    private String components;

    /**
     * 页面配置(JSON)
     */
    private String config;

    /**
     * 页面代码
     */
    private String pageCode;

    /**
     * 路由配置(JSON)
     */
    private String routeConfig;

    /**
     * 菜单配置(JSON)
     */
    private String menuConfig;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

} 