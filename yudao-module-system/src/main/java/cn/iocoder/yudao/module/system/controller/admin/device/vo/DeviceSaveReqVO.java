package cn.iocoder.yudao.module.system.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "管理后台 - 设备档案新增/修改 Request VO")
@Data
public class DeviceSaveReqVO {

    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "主变压器")
    @NotEmpty(message = "设备名称不能为空")
    @Size(max = 100, message = "设备名称长度不能超过100个字符")
    private String name;

    @Schema(description = "设备编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEV001")
    @NotEmpty(message = "设备编号不能为空")
    @Size(max = 50, message = "设备编号长度不能超过50个字符")
    private String deviceCode;

    @Schema(description = "父设备ID", example = "1024")
    private Long parentId;

    @Schema(description = "设备类型ID", example = "1")
    private Long deviceTypeId;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "所属区域ID", example = "1")
    private Long regionId;

    @Schema(description = "显示顺序", example = "1")
    private Integer sort;

    @Schema(description = "设备状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "设备状态不能为空")
    private Integer status;

    @Schema(description = "设备负责人", example = "1024")
    private Long responsibleUserId;

    @Schema(description = "制造商", example = "西门子")
    @Size(max = 100, message = "制造商长度不能超过100个字符")
    private String manufacturer;

    @Schema(description = "设备型号", example = "SGB10-1000")
    @Size(max = 100, message = "设备型号长度不能超过100个字符")
    private String model;

    @Schema(description = "序列号", example = "SN123456789")
    @Size(max = 100, message = "序列号长度不能超过100个字符")
    private String serialNumber;

    @Schema(description = "购买日期", example = "2023-01-01")
    private LocalDateTime purchaseDate;

    @Schema(description = "安装日期", example = "2023-01-15")
    private LocalDateTime installDate;

    @Schema(description = "保修到期日期", example = "2025-01-01")
    private LocalDateTime warrantyExpireDate;

    @Schema(description = "设备描述", example = "主变压器设备")
    @Size(max = 500, message = "设备描述长度不能超过500个字符")
    private String description;

    @Schema(description = "动态扩展属性")
    private Map<String, Object> extraAttrs;

    @Schema(description = "备注", example = "设备运行正常")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
} 