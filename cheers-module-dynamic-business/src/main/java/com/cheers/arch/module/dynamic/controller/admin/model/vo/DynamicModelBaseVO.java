package com.cheers.arch.module.dynamic.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 动态业务模型 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class DynamicModelBaseVO {

    @Schema(description = "模型编码", example = "user")
    private String code;

    @Schema(description = "模型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    @NotEmpty(message = "模型名称不能为空")
    private String name;

    @Schema(description = "描述", example = "用户管理模型")
    private String description;

    @Schema(description = "数据结构类型（1:树形，2:列表）", example = "1")
    private Integer structureType;

    @Schema(description = "存储策略（1:单表，2:分表）", example = "1")
    private Integer storageStrategy;

    @Schema(description = "数据表名", example = "sys_user")
    private String tableName;

    @Schema(description = "所属目录ID", example = "1")
    private Long directoryId;

    @Schema(description = "模型配置", example = "{}")
    private String config;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "是否只读", example = "false")
    private Boolean readonly;

    @Schema(description = "模型类型（0:系统，1:自定义）", example = "1")
    private Integer modelType;
} 