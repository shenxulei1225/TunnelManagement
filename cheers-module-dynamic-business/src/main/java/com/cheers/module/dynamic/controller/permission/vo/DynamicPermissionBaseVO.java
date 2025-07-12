package com.cheers.module.dynamic.controller.permission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 动态业务权限 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class DynamicPermissionBaseVO {

    @Schema(description = "模型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "user")
    @NotEmpty(message = "模型编码不能为空")
    private String modelCode;

    @Schema(description = "权限类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权限类型不能为空")
    private Integer type;

    @Schema(description = "权限目标", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权限目标不能为空")
    private Long target;

    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "角色编号", example = "2048")
    private Long roleId;

    @Schema(description = "部门编号", example = "4096")
    private Long deptId;

} 