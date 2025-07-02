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
 * 区域批量导入日志 DO
 *
 * @author cheers
 */
@TableName("system_region_import_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionImportLogDO extends BaseDO {

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
     * 文件名
     */
    private String fileName;

    /**
     * 分类ID
     */
    private Long categoryId;

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
     * 错误信息摘要
     */
    private String errorMessage;

} 