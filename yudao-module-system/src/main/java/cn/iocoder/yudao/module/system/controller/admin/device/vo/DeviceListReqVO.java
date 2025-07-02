package cn.iocoder.yudao.module.system.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备档案分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceListReqVO extends PageParam {

    @Schema(description = "设备名称", example = "主变压器")
    private String name;

    @Schema(description = "设备编号", example = "DEV001")
    private String deviceCode;

    @Schema(description = "父设备ID", example = "1024")
    private Long parentId;

    @Schema(description = "设备类型ID", example = "1")
    private Long deviceTypeId;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "所属区域ID", example = "1")
    private Long regionId;

    @Schema(description = "设备状态", example = "0")
    private Integer status;

    @Schema(description = "设备负责人", example = "1024")
    private Long responsibleUserId;

    @Schema(description = "制造商", example = "西门子")
    private String manufacturer;

    @Schema(description = "设备型号", example = "SGB10-1000")
    private String model;

    @Schema(description = "序列号", example = "SN123456789")
    private String serialNumber;

    @Schema(description = "购买日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] purchaseDate;

    @Schema(description = "安装日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] installDate;

    @Schema(description = "保修到期日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] warrantyExpireDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
} 