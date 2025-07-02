package com.cheers.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 区域列表 Request VO")
@Data
public class RegionListReqVO {

    @Schema(description = "区域名称", example = "华东区")
    private String name;

    @Schema(description = "父区域编号", example = "1024")
    private Long parentId;

    @Schema(description = "显示顺序", example = "1")
    private Integer sort;

    @Schema(description = "区域状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

} 