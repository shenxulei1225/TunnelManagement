package cn.iocoder.yudao.module.system.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

@Schema(description = "管理后台 - 设备档案 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceRespVO {

    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("设备ID")
    private Long id;

    @Schema(description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "主变压器")
    @ExcelProperty("设备名称")
    private String name;

    @Schema(description = "设备编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "DEV001")
    @ExcelProperty("设备编号")
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
    @ExcelProperty("显示顺序")
    private Integer sort;

    @Schema(description = "设备状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("设备状态")
    private Integer status;

    @Schema(description = "设备负责人", example = "1024")
    private Long responsibleUserId;

    @Schema(description = "制造商", example = "西门子")
    @ExcelProperty("制造商")
    private String manufacturer;

    @Schema(description = "设备型号", example = "SGB10-1000")
    @ExcelProperty("设备型号")
    private String model;

    @Schema(description = "序列号", example = "SN123456789")
    @ExcelProperty("序列号")
    private String serialNumber;

    @Schema(description = "购买日期", example = "2023-01-01")
    @ExcelProperty("购买日期")
    private LocalDateTime purchaseDate;

    @Schema(description = "安装日期", example = "2023-01-15")
    @ExcelProperty("安装日期")
    private LocalDateTime installDate;

    @Schema(description = "保修到期日期", example = "2025-01-01")
    @ExcelProperty("保修到期日期")
    private LocalDateTime warrantyExpireDate;

    @Schema(description = "设备描述", example = "主变压器设备")
    @ExcelProperty("设备描述")
    private String description;

    @Schema(description = "动态扩展属性")
    private Map<String, Object> extraAttrs;

    @Schema(description = "备注", example = "设备运行正常")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建者", example = "admin")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "更新者", example = "admin")
    @ExcelProperty("更新者")
    private String updater;
} 