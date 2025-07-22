package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段分类更新 Request VO")
@Data
public class FieldCategoryUpdateReqVO {

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "字段ID不能为空")
    private Long fieldId;

    @Schema(description = "分类ID", example = "2048")
    private Long categoryId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

} 