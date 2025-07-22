package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 上传页面 Request VO")
@Data
public class UploadPageReqVO {

    @Schema(description = "页面名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理")
    @NotBlank(message = "页面名称不能为空")
    private String pageName;

    @Schema(description = "页面路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "device-management")
    @NotBlank(message = "页面路径不能为空")
    private String pagePath;

    @Schema(description = "页面内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "页面内容不能为空")
    private String pageContent;

    @Schema(description = "页面类型", example = "vue")
    private String pageType = "vue";

    @Schema(description = "是否覆盖", example = "false")
    private Boolean overwrite = false;

    // 以下字段是现有代码中需要的
    @Schema(description = "页面配置", example = "{\"title\":\"设备管理\",\"keepAlive\":true}")
    private PageConfig pageConfig;

    @Schema(description = "页面代码")
    private String pageCode;

    @Schema(description = "组件列表")
    private java.util.List<Object> components;

    @Schema(description = "路由配置")
    private String routeConfig;

    @Schema(description = "菜单配置")
    private String menuConfig;

    // 内部类定义页面配置
    @Data
    public static class PageConfig {
        private String pageName;
        private String pagePath;
        private String menuTitle;
        private String menuIcon;
    }
} 