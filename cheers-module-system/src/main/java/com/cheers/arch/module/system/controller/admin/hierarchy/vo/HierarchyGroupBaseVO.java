package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统分级组 Base VO，提供给添加、修改、详情、列表的 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class HierarchyGroupBaseVO {

    @Schema(description = "分级组名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分级")
    @NotNull(message = "分级组名称不能为空")
    private String name;

    @Schema(description = "分级组编码", example = "DEVICE_HIERARCHY")
    private String code;

    @Schema(description = "父分级组ID", example = "1")
    private Long parentId;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "颜色", example = "#409EFF")
    private String color;

    @Schema(description = "图标", example = "Folder")
    private String icon;

    @Schema(description = "描述", example = "设备分级描述")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

} 