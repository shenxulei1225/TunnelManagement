package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面模板 Response VO")
@Data
public class PageTemplateRespVO {

    @Schema(description = "模板编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "拖拽分类模板")
    private String templateName;

    @Schema(description = "模板标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "drag-drop-classification")
    private String templateKey;

    @Schema(description = "模板分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "form")
    private String category;

    @Schema(description = "模板内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String templateContent;

    @Schema(description = "预览图片", example = "template-preview.png")
    private String previewImage;

    @Schema(description = "模板描述", example = "支持拖拽排序的分类管理模板")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
} 