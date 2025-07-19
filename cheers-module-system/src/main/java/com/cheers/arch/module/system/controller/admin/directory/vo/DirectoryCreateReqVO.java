package com.cheers.arch.module.system.controller.admin.directory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 目录创建 Request VO")
@Data
public class DirectoryCreateReqVO {

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "dynamic_model")
    @NotEmpty(message = "业务类型不能为空")
    private String businessType;

    @Schema(description = "父目录ID", example = "0")
    private Long parentId;

    @Schema(description = "目录名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理")
    @NotEmpty(message = "目录名称不能为空")
    private String name;

    @Schema(description = "目录编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "device_management")
    @NotEmpty(message = "目录编码不能为空")
    private String code;

    @Schema(description = "描述", example = "设备相关的业务模型")
    private String description;

    @Schema(description = "图标", example = "el-icon-cpu")
    private String icon;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "扩展属性", example = "{}")
    private String extData;
} 