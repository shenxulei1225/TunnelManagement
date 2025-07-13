package com.cheers.arch.framework.directory.controller.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 目录 Response VO")
@Data
public class DirectoryRespVO {

    @Schema(description = "目录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "dynamic_model")
    private String businessType;

    @Schema(description = "父目录ID", example = "0")
    private Long parentId;

    @Schema(description = "目录名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理")
    private String name;

    @Schema(description = "目录编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "device_management")
    private String code;

    @Schema(description = "描述", example = "设备相关的业务模型")
    private String description;

    @Schema(description = "图标", example = "el-icon-cpu")
    private String icon;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "扩展属性", example = "{}")
    private String extData;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "子目录列表")
    private List<DirectoryRespVO> children;
} 