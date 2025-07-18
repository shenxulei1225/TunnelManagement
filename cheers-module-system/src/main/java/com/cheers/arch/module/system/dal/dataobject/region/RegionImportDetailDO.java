package com.cheers.arch.module.system.dal.dataobject.region;

import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 区域批量导入详情 DO
 *
 * @author cheers
 */
@TableName("system_region_import_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionImportDetailDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 批次ID
     */
    private String batchId;

    /**
     * 导入记录ID
     */
    private Long importId;

    /**
     * 行号
     */
    private Integer rowNumber;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 上级区域名称
     */
    private String parentName;

    /**
     * 上级区域ID
     */
    private Long parentId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-启用，1-禁用
     */
    private Integer status;

    /**
     * 动态字段数据（JSON格式）
     */
    private String dynamicFields;

    /**
     * 处理状态：0-待处理，1-成功，2-失败
     */
    private Integer processStatus;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 生成的区域ID
     */
    private Long regionId;

    /**
     * 租户编号
     */
    private Long tenantId;
} 