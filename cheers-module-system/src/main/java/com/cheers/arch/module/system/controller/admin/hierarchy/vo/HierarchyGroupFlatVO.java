package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统分级组扁平化 VO（避免数据冗余）
 * 只包含节点自身信息，不包含子节点数据
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 系统分级组扁平化 VO")
@Data
public class HierarchyGroupFlatVO {

    @Schema(description = "分级组ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "分级组名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分级")
    private String name;

    @Schema(description = "父分级组ID", example = "1")
    private Long parentId;

    @Schema(description = "层级", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer level;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "关联对象数量", example = "10")
    private Long count;

} 