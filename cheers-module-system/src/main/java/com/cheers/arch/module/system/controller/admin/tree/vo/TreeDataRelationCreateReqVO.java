package com.cheers.arch.module.system.controller.admin.tree.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用树结构-数据关联创建 Request VO
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 通用树结构-数据关联创建 Request VO")
@Data
public class TreeDataRelationCreateReqVO {

    @Schema(description = "树类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "field_category")
    @NotNull(message = "树类型不能为空")
    private String treeType;

    @Schema(description = "树节点ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "树节点ID不能为空")
    private Long treeNodeId;

    @Schema(description = "数据类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "field_def")
    @NotNull(message = "数据类型不能为空")
    private String dataType;

    @Schema(description = "数据ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "数据ID不能为空")
    private Long dataId;

    @Schema(description = "数据名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备名称")
    @NotNull(message = "数据名称不能为空")
    private String dataName;

    @Schema(description = "数据类型标签", example = "字段")
    private String dataTypeLabel;

    @Schema(description = "显示顺序", example = "1")
    private Integer displayOrder;

    @Schema(description = "是否必填", example = "true")
    private Boolean isRequired;

    @Schema(description = "扩展元数据", example = "{}")
    private String metadata;
} 