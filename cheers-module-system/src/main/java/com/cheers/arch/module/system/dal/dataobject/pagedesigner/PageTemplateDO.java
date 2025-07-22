package com.cheers.arch.module.system.dal.dataobject.pagedesigner;

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
 * 页面模板 DO
 *
 * @author cheers
 */
@TableName("system_page_template")
@KeySequence("system_page_template_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageTemplateDO extends TenantBaseDO {

    /**
     * 模板ID
     */
    @TableId
    private String id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 模板类型
     */
    private String templateType;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 模板配置(JSON)
     */
    private String templateConfig;

    /**
     * 预览图片
     */
    private String previewImage;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

} 