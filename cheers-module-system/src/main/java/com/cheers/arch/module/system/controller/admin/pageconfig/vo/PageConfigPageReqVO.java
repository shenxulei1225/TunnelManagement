package com.cheers.arch.module.system.controller.admin.pageconfig.vo;

import static com.cheers.arch.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import java.time.LocalDateTime;

import com.cheers.arch.framework.common.pojo.PageParam;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 页面配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PageConfigPageReqVO extends PageParam {

    @Schema(description = "配置名称，模糊匹配", example = "设备分类配置")
    private String configName;

    @Schema(description = "配置键，模糊匹配", example = "device_category_config")
    private String configKey;

    @Schema(description = "配置类型", example = "page")
    private String configType;

    @Schema(description = "业务类型", example = "device")
    private String businessType;

    @Schema(description = "状态", example = "0")
    private Integer status;

    // 以下字段是现有代码中需要的
    @Schema(description = "业务名称，模糊匹配", example = "设备管理")
    private String businessName;

    @Schema(description = "页面标题，模糊匹配", example = "设备分类管理")
    private String pageTitle;

    @Schema(description = "模板类型", example = "drag-drop-classification")
    private String templateType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
} 