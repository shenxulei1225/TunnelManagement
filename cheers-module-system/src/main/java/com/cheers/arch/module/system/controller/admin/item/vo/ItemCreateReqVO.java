package com.cheers.arch.module.system.controller.admin.item.vo;

import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 数据项创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemCreateReqVO extends ItemBaseVO {

    @Schema(description = "数据项类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "device")
    @NotEmpty(message = "数据项类型不能为空")
    private String itemType;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例设备")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEVICE_001")
    @NotEmpty(message = "编码不能为空")
    private String code;

    @Schema(description = "描述", example = "这是一个示例设备")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "排序顺序", example = "1")
    private Integer sortOrder;

    @Schema(description = "图标", example = "el-icon-device")
    private String icon;

    @Schema(description = "颜色", example = "#409EFF")
    private String color;

    @Schema(description = "业务数据", example = "{\"brand\":\"华为\",\"model\":\"P50\"}")
    private Map<String, Object> businessData;

    @Schema(description = "扩展数据", example = "{\"remark\":\"备注信息\"}")
    private Map<String, Object> extraData;
} 