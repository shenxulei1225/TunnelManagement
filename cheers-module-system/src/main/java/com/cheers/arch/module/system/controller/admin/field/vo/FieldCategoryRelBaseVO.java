package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 字段分类关联 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class FieldCategoryRelBaseVO {

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "字段ID不能为空")
    private Long fieldId;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "分类ID不能为空") 
    private Long categoryId;

    @Schema(description = "排序", example = "1")
    private Integer sort;

} 