package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面设计 Response VO")
@Data
public class PageDesignRespVO {

    @Schema(description = "设计编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "设计名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理页面")
    private String designName;

    @Schema(description = "页面路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "device-management")
    private String pagePath;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long templateId;

    @Schema(description = "设计数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"components\":[],\"layout\":{}}")
    private String designData;

    @Schema(description = "页面配置", example = "{\"title\":\"设备管理\",\"keepAlive\":true}")
    private String pageConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "备注", example = "设备管理页面设计")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
} 