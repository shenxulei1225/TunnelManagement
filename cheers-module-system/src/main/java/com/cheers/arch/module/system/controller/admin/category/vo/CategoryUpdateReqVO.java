package com.cheers.arch.module.system.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 分类更新 Request VO")
@Data
public class CategoryUpdateReqVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "分类编号不能为空")
    private Long id;

    @Schema(description = "父节点 ID，0 表示根", example = "1024")
    private Long parentId;

    @Schema(description = "节点编码", example = "DEVICE_CATEGORY")
    private String code;

    @Schema(description = "展示名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分类")
    @NotBlank(message = "展示名称不能为空")
    private String name;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "图标", example = "icon-device")
    private String icon;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private Integer status;

    @Schema(description = "是否系统只读", example = "false")
    private Boolean readonly;

    @Schema(description = "分类描述", example = "用于对设备进行分类管理")
    private String description;
} 