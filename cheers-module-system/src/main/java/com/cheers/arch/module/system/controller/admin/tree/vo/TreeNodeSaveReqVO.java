package com.cheers.arch.module.system.controller.admin.tree.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 树节点保存 Request VO")
@Data
public class TreeNodeSaveReqVO {

    @Schema(description = "节点ID", example = "1")
    private Long id;

    @Schema(description = "父节点ID", example = "0")
    private Long parentId;

    @Schema(description = "节点名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "基础信息")
    @NotBlank(message = "节点名称不能为空")
    private String name;

    @Schema(description = "节点编码", example = "basic_info")
    private String code;

    @Schema(description = "数据类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "category")
    @NotBlank(message = "数据类型不能为空")
    private String dataType;

    @Schema(description = "数据类型标签", example = "字段分类")
    private String dataTypeLabel;

    @Schema(description = "显示顺序", example = "1")
    private Integer displayOrder;

    @Schema(description = "是否必填", example = "false")
    private Boolean isRequired;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "元数据", example = "{}")
    private String metadata;
} 