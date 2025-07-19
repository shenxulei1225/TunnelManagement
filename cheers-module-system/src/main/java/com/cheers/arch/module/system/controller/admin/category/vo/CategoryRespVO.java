package com.cheers.arch.module.system.controller.admin.category.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 分类 Response VO")
@Data
public class CategoryRespVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "父节点 ID，0 表示根", example = "1024")
    private Long parentId;

    @Schema(description = "节点编码", example = "DEVICE_CATEGORY")
    private String code;

    @Schema(description = "展示名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分类")
    private String name;

    @Schema(description = "树路径，如 \"1/15/37\"", example = "0/1024")
    private String treePath;

    @Schema(description = "层级深度", example = "2")
    private Integer level;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "图标", example = "icon-device")
    private String icon;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private Integer status;

    @Schema(description = "是否只读", example = "false")
    private Boolean readonly;

    @Schema(description = "分类描述", example = "用于对设备进行分类管理")
    private String description;

    @Schema(description = "多租户编号", example = "1")
    private Long tenantId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

    @Schema(description = "创建者", example = "1024")
    private String creator;

    @Schema(description = "更新者", example = "1024")
    private String updater;
} 