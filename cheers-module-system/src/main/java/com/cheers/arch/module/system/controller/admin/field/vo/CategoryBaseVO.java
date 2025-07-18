package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
* 分类 Base VO，提供给添加、修改、详情、列表等接口使用
*/
@Data
public class CategoryBaseVO {

    @Schema(description = "父节点 ID，0 表示根", example = "0")
    private Long parentId;

    @Schema(description = "节点编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "category_code")
    @NotNull(message = "节点编码不能为空")
    private String code;

    @Schema(description = "展示名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "分类名称")
    @NotNull(message = "展示名称不能为空")
    private String name;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "是否系统只读", example = "false")
    private Boolean readonly;

} 