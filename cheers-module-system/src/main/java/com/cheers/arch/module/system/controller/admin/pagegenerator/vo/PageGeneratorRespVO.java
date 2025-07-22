package com.cheers.arch.module.system.controller.admin.pagegenerator.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面生成器 Response VO")
@Data
public class PageGeneratorRespVO {

    @Schema(description = "页面ID", example = "1024")
    private String id;

    @Schema(description = "页面名称", example = "设备管理")
    private String pageName;

    @Schema(description = "页面路径", example = "device-management")
    private String pagePath;

    @Schema(description = "菜单标题", example = "设备管理")
    private String menuTitle;

    @Schema(description = "菜单图标", example = "ep:monitor")
    private String menuIcon;

    @Schema(description = "模板类型", example = "drag-drop-classification")
    private String template;

    @Schema(description = "业务类型", example = "device")
    private String businessType;

    @Schema(description = "页面代码")
    private String pageCode;

    @Schema(description = "路由配置")
    private String routeConfig;

    @Schema(description = "菜单配置")
    private String menuConfig;

    @Schema(description = "状态", example = "active")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 