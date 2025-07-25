package com.cheers.arch.module.system.controller.admin.item.vo;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 数据项 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemRespVO extends ItemBaseVO {

    @Schema(description = "数据项ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "数据项类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "device")
    private String itemType;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例设备")
    private String name;

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEVICE_001")
    private String code;

    @Schema(description = "描述", example = "这是一个示例设备")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-01 00:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-01 00:00:00")
    private LocalDateTime updateTime;

    @Schema(description = "创建者", example = "admin")
    private String creator;

    @Schema(description = "更新者", example = "admin")
    private String updater;
} 