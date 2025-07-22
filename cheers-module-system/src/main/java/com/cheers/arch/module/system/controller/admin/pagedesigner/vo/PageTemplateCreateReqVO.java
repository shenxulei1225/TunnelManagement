package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面模板创建 Request VO")
@Data
public class PageTemplateCreateReqVO {

    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "拖拽分类模板")
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    @Schema(description = "模板标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "drag-drop-classification")
    @NotBlank(message = "模板标识不能为空")
    private String templateKey;

    @Schema(description = "模板分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "form")
    @NotBlank(message = "模板分类不能为空")
    private String category;

    @Schema(description = "模板内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模板内容不能为空")
    private String templateContent;

    @Schema(description = "预览图片", example = "template-preview.png")
    private String previewImage;

    @Schema(description = "模板描述", example = "支持拖拽排序的分类管理模板")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort = 0;
} 