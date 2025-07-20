package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统分级组 Tree VO（Element Plus Tree 专用）
 * 只包含 Element Plus Tree 组件真正需要的字段
 * 返回单个根节点，避免数据冗余
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 系统分级组 Tree VO")
@Data
public class HierarchyGroupTreeVO {

    @Schema(description = "分级组ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "分级组名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分级")
    private String name;

    @Schema(description = "子分级组列表")
    private List<HierarchyGroupTreeVO> children;

    // ==================== 可选扩展字段 ====================
    
    @Schema(description = "分级组编码", example = "DEVICE_HIERARCHY")
    private String code;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "关联对象数量", example = "10")
    private Long count;

    @Schema(description = "图标", example = "Folder")
    private String icon;

} 