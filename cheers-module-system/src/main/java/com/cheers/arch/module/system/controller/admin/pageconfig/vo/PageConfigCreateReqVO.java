package com.cheers.arch.module.system.controller.admin.pageconfig.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面配置创建 Request VO")
@Data
public class PageConfigCreateReqVO {

    @Schema(description = "配置名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分类配置")
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    @Schema(description = "配置键", requiredMode = Schema.RequiredMode.REQUIRED, example = "device_category_config")
    @NotBlank(message = "配置键不能为空")
    private String configKey;

    @Schema(description = "配置值", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"pageTitle\":\"设备分类\",\"enableDrag\":true}")
    @NotBlank(message = "配置值不能为空")
    private String configValue;

    @Schema(description = "配置类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "page")
    @NotBlank(message = "配置类型不能为空")
    private String configType;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "device")
    @NotBlank(message = "业务类型不能为空")
    private String businessType;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "设备分类页面配置")
    private String remark;
} 