package com.cheers.arch.module.system.controller.admin.item.vo;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据项 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ItemBaseVO {

    @Schema(description = "数据项类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "device")
    @NotBlank(message = "数据项类型不能为空")
    private String itemType;

    @Schema(description = "数据项名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "监控摄像头")
    @NotBlank(message = "数据项名称不能为空")
    private String name;

    @Schema(description = "数据项编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "CAM001")
    @NotBlank(message = "数据项编码不能为空")
    private String code;

    @Schema(description = "数据项描述", example = "监控摄像头设备")
    private String description;

    @Schema(description = "数据项状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "数据项状态不能为空")
    private Integer status;

    @Schema(description = "排序顺序", example = "1")
    private Integer sortOrder;

    @Schema(description = "图标", example = "el-icon-camera")
    private String icon;

    @Schema(description = "颜色", example = "#409EFF")
    private String color;

    @Schema(description = "业务数据", example = "{\"brand\":\"海康威视\",\"model\":\"DS-2CD2T86G2-4I\"}")
    private Map<String, Object> businessData;

    @Schema(description = "扩展数据", example = "{\"installDate\":\"2024-01-01\",\"location\":\"大门口\"}")
    private Map<String, Object> extraData;

} 