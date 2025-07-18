package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 预览数据 VO")
@Data
public class PreviewDataVO {

    @Schema(description = "模型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "inspection")
    private String modelCode;

    @Schema(description = "视图类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "list")
    private String viewType;

    @Schema(description = "预览HTML")
    private String previewHtml;

    @Schema(description = "字段配置")
    private List<FieldConfigVO> fieldConfigs;

    @Schema(description = "布局配置")
    private Map<String, Object> layoutConfig;

    @Schema(description = "操作配置")
    private Map<String, Object> actionConfig;

    @Schema(description = "验证结果")
    private ValidationResultVO validationResult;
} 