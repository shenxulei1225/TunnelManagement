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
 * 页面部署记录 DO
 *
 * @author cheers
 */
@TableName("system_page_deploy_record")
@KeySequence("system_page_deploy_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDeployRecordDO extends TenantBaseDO {

    /**
     * 部署记录ID
     */
    @TableId
    private String id;

    /**
     * 页面设计ID
     */
    private String pageDesignId;

    /**
     * 部署状态
     */
    private String deployStatus;

    /**
     * 部署路径
     */
    private String deployPath;

    /**
     * 部署消息
     */
    private String deployMessage;

    /**
     * 部署时间
     */
    private LocalDateTime deployTime;

} 