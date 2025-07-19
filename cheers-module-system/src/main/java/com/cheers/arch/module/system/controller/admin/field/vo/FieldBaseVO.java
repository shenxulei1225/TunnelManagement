package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 字段 Base VO，提供给添加、修改、详情、列表等接口使用
*/
@Data
public class FieldBaseVO {

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "field_code")
    @NotNull(message = "字段编码不能为空")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "字段名称")
    @NotNull(message = "字段名称不能为空")
    private String fieldName;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "STRING")
    @NotNull(message = "字段类型不能为空")
    private String fieldType;

    @Schema(description = "显示名称", example = "显示名称")
    private String display;

    @Schema(description = "字段描述", example = "字段描述")
    private String description;

    @Schema(description = "是否自定义字段", example = "true")
    private Boolean isCustom;

    @Schema(description = "单位", example = "个")
    private String unit;

    @Schema(description = "枚举值JSON", example = "[\"选项1\",\"选项2\"]")
    private String enumJson;

    @Schema(description = "计算表达式", example = "a + b")
    private String calcExpr;

    @Schema(description = "默认值", example = "默认值")
    private String defaultValue;

    @Schema(description = "是否必填", example = "true")
    private Boolean required;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private Integer status;

} 