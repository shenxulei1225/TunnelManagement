package com.cheers.arch.module.system.controller.admin.tree.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用树结构-数据关联更新 Request VO
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 通用树结构-数据关联更新 Request VO")
@Data
public class TreeDataRelationUpdateReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "数据名称", example = "设备名称")
    private String dataName;

    @Schema(description = "数据类型标签", example = "字段")
    private String dataTypeLabel;

    @Schema(description = "显示顺序", example = "1")
    private Integer displayOrder;

    @Schema(description = "是否必填", example = "true")
    private Boolean isRequired;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "扩展元数据", example = "{}")
    private String metadata;
} 