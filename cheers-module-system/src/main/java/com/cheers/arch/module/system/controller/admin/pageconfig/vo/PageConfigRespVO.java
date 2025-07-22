package com.cheers.arch.module.system.controller.admin.pageconfig.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面配置 Response VO")
@Data
public class PageConfigRespVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "配置名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备分类配置")
    private String configName;

    @Schema(description = "配置键", requiredMode = Schema.RequiredMode.REQUIRED, example = "device_category_config")
    private String configKey;

    @Schema(description = "配置值", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"pageTitle\":\"设备分类\",\"enableDrag\":true}")
    private String configValue;

    @Schema(description = "配置类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "page")
    private String configType;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "device")
    private String businessType;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "备注", example = "设备分类页面配置")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
}