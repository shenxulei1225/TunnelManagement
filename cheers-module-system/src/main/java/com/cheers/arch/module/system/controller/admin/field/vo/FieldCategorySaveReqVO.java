package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段分类保存 Request VO")
@Data
public class FieldCategorySaveReqVO {

    @Schema(description = "分类编号", example = "1024")
    private Long id;

    @Schema(description = "父分类编号", example = "0")
    private Long parentId;

    @Schema(description = "分类编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "SYSTEM")
    @NotBlank(message = "分类编码不能为空")
    private String code;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "系统分类")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
