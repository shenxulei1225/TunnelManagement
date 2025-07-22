package com.cheers.arch.module.system.controller.admin.tree.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 树配置响应 VO")
@Data
public class TreeConfigRespVO {

    @Schema(description = "配置ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "树类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "field_category")
    private String treeType;

    @Schema(description = "树名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "字段分类树")
    private String treeName;

    @Schema(description = "描述", example = "用于管理字段分类的树形结构")
    private String description;

    @Schema(description = "允许的数据类型列表")
    private List<String> allowedDataTypes;

    @Schema(description = "最大层级", example = "5")
    private Integer maxLevel;

    @Schema(description = "节点名称标签", requiredMode = Schema.RequiredMode.REQUIRED, example = "名称")
    private String nodeNameLabel;

    @Schema(description = "节点编码标签", example = "编码")
    private String nodeCodeLabel;

    @Schema(description = "排序类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sortType;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "配置JSON", example = "{}")
    private String configJson;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
} 