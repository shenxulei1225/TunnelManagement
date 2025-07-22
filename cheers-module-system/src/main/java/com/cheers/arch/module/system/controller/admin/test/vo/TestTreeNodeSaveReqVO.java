package com.cheers.arch.module.system.controller.admin.test.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 测试树节点新增/修改 Request VO")
@Data
public class TestTreeNodeSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "节点名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "总公司")
    @NotBlank(message = "节点名称不能为空")
    @Size(max = 50, message = "节点名称长度不能超过50个字符")
    private String name;

    @Schema(description = "节点类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "dept")
    @NotBlank(message = "节点类型不能为空")
    @Size(max = 20, message = "节点类型长度不能超过20个字符")
    private String type;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "dept")
    @NotBlank(message = "业务类型不能为空")
    @Size(max = 20, message = "业务类型长度不能超过20个字符")
    private String businessType;

    @Schema(description = "父节点编号", example = "1024")
    private Long parentId;

    @Schema(description = "节点描述", example = "这是一个测试节点")
    @Size(max = 200, message = "节点描述长度不能超过200个字符")
    private String description;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "节点计数", example = "5")
    private Integer count;

    @Schema(description = "扩展属性", example = "{}")
    private String extraAttrs;

} 