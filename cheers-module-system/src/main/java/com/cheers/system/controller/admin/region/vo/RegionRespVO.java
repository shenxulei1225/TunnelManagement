package com.cheers.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "管理后台 - 区域 Response VO")
@Data
public class RegionRespVO {

    @Schema(description = "区域编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "区域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "华东区")
    private String name;

    @Schema(description = "父区域编号", example = "1024")
    private Long parentId;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "所属分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "区域状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "扩展属性", example = "{\"key1\":\"value1\",\"key2\":\"value2\"}")
    private Map<String, Object> extraAttrs;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

} 