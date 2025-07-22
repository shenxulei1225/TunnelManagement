package com.cheers.arch.module.system.controller.admin.tree.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 树节点响应 VO")
@Data
public class TreeNodeRespVO {

    @Schema(description = "节点ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "树类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "field_category")
    private String treeType;

    @Schema(description = "父节点ID", example = "0")
    private Long parentId;

    @Schema(description = "节点名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "基础信息")
    private String name;

    @Schema(description = "节点编码", example = "basic_info")
    private String code;

    @Schema(description = "数据类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "category")
    private String dataType;

    @Schema(description = "数据类型标签", example = "字段分类")
    private String dataTypeLabel;

    @Schema(description = "显示顺序", example = "1")
    private Integer displayOrder;

    @Schema(description = "是否必填", example = "false")
    private Boolean isRequired;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "元数据", example = "{}")
    private String metadata;

    @Schema(description = "子节点列表")
    private List<TreeNodeRespVO> children;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
} 