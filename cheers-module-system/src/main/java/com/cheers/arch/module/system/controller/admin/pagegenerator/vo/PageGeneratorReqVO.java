package com.cheers.arch.module.system.controller.admin.pagegenerator.vo;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 页面生成器 Request VO")
@Data
public class PageGeneratorReqVO {

    @Schema(description = "页面名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理")
    @NotBlank(message = "页面名称不能为空")
    private String pageName;

    @Schema(description = "页面路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "device-management")
    @NotBlank(message = "页面路径不能为空")
    private String pagePath;

    @Schema(description = "菜单标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备管理")
    @NotBlank(message = "菜单标题不能为空")
    private String menuTitle;

    @Schema(description = "菜单图标", requiredMode = Schema.RequiredMode.REQUIRED, example = "ep:monitor")
    @NotBlank(message = "菜单图标不能为空")
    private String menuIcon;

    @Schema(description = "模板类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "drag-drop-classification")
    @NotBlank(message = "模板类型不能为空")
    private String template;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "device")
    @NotBlank(message = "业务类型不能为空")
    private String businessType;

    @Schema(description = "树类型", example = "device_category")
    private String treeType;

    @Schema(description = "数据源", example = "universal_tree")
    private String dataSource;

    @Schema(description = "创建API", example = "createDevice")
    private String createApi;

    @Schema(description = "更新API", example = "updateDevice")
    private String updateApi;

    @Schema(description = "删除API", example = "deleteDevice")
    private String deleteApi;

    @Schema(description = "移动API", example = "moveTreeNode")
    private String moveApi;

    @Schema(description = "权限列表")
    private List<String> permissions;

    @Schema(description = "是否生成路由", example = "true")
    @NotNull(message = "是否生成路由不能为空")
    private Boolean generateRoute;

    @Schema(description = "是否生成菜单", example = "true")
    @NotNull(message = "是否生成菜单不能为空")
    private Boolean generateMenu;

    @Schema(description = "是否立即生效", example = "true")
    @NotNull(message = "是否立即生效不能为空")
    private Boolean immediateEffect;

    @Schema(description = "是否保存配置", example = "true")
    @NotNull(message = "是否保存配置不能为空")
    private Boolean saveConfig;
} 