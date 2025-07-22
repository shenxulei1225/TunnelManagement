package com.cheers.arch.module.system.controller.admin.tree.vo;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 树配置保存 Request VO")
@Data
public class TreeConfigSaveReqVO {

    @Schema(description = "配置ID", example = "1")
    private Long id;

    @Schema(description = "树类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "field_category")
    @NotBlank(message = "树类型不能为空")
    private String treeType;

    @Schema(description = "树名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "字段分类树")
    @NotBlank(message = "树名称不能为空")
    private String treeName;

    @Schema(description = "描述", example = "用于管理字段分类的树形结构")
    private String description;

    @Schema(description = "允许的数据类型列表")
    private List<String> allowedDataTypes;

    @Schema(description = "最大层级", example = "5")
    private Integer maxLevel;

    @Schema(description = "节点名称标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "名称")
    @NotBlank(message = "节点名称标签不能为空")
    private String nodeNameLabel;

    @Schema(description = "节点编码标签", example = "编码")
    private String nodeCodeLabel;

    @Schema(description = "排序类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序类型不能为空")
    private Integer sortType;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "配置JSON", example = "{}")
    private String configJson;
} 