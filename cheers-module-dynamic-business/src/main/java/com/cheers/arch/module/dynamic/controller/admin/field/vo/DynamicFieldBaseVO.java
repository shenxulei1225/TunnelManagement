package com.cheers.arch.module.dynamic.controller.admin.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 动态业务字段 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class DynamicFieldBaseVO {

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "name")
    @NotEmpty(message = "字段编码不能为空")
    private String code;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "姓名")
    @NotEmpty(message = "字段名称不能为空")
    private String name;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "STRING")
    @NotNull(message = "字段类型不能为空")
    private String type;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否必填不能为空")
    private Integer required;

    @Schema(description = "默认值", example = "")
    private String defaultValue;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "业务模型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "业务模型ID不能为空")
    private Long modelId;
} 