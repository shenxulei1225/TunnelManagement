package com.cheers.system.dal.dataobject.region;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 区域批量导入记录 DO
 *
 * @author cheers
 */
@TableName("system_region_import")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionImportDO extends BaseDO {

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
     * 分类ID
     */
    private Long categoryId;

    /**
     * 导入文件名
     */
    private String fileName;

    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 成功记录数
     */
    private Integer successCount;

    /**
     * 失败记录数
     */
    private Integer failedCount;

    /**
     * 状态：0-待处理，1-处理中，2-成功，3-失败
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 租户编号
     */
    private Long tenantId;
} 