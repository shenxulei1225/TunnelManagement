package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面设计创建 Request VO")
@Data
public class PageDesignCreateReqVO {

    @Schema(description = "设计名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理页面")
    @NotBlank(message = "设计名称不能为空")
    private String designName;

    @Schema(description = "页面路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "device-management")
    @NotBlank(message = "页面路径不能为空")
    private String pagePath;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @Schema(description = "设计数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"components\":[],\"layout\":{}}")
    @NotBlank(message = "设计数据不能为空")
    private String designData;

    @Schema(description = "页面配置", example = "{\"title\":\"设备管理\",\"keepAlive\":true}")
    private String pageConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "设备管理页面设计")
    private String remark;
} 