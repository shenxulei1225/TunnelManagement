package com.cheers.arch.module.system.controller.admin.pagegenerator.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 生成页面结果 Response VO")
@Data
public class GeneratedPageRespVO {

    @Schema(description = "页面ID", example = "1024")
    private Long pageId;

    @Schema(description = "页面名称", example = "设备管理")
    private String pageName;

    @Schema(description = "页面路径", example = "device-management")
    private String pagePath;

    @Schema(description = "菜单ID", example = "2048")
    private Long menuId;

    @Schema(description = "路由配置")
    private Object routeConfig;

    @Schema(description = "菜单配置")
    private Object menuConfig;

    @Schema(description = "生成的文件列表")
    private List<String> generatedFiles;

    @Schema(description = "生成状态", example = "success")
    private String status;

    @Schema(description = "生成消息", example = "页面生成成功")
    private String message;

    @Schema(description = "生成时间")
    private LocalDateTime generatedTime;
} 