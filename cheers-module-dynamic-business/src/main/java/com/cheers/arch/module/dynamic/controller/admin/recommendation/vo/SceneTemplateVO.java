package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 场景模板 VO")
@Data
public class SceneTemplateVO {

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "inspection_daily")
    private String code;

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "日常巡检")
    private String name;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "inspection")
    private String businessType;

    @Schema(description = "场景描述", example = "适用于日常巡检业务场景")
    private String description;

    @Schema(description = "推荐字段配置（JSON格式）")
    private String recommendedFields;

    @Schema(description = "默认布局配置")
    private String defaultLayout;

    @Schema(description = "验证规则配置")
    private String validationRules;

    @Schema(description = "图标", example = "el-icon-search")
    private String icon;

    @Schema(description = "排序", example = "100")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
} 