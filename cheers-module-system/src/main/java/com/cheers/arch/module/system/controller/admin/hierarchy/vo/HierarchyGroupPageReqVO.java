package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import static com.cheers.arch.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import java.time.LocalDateTime;

import com.cheers.arch.framework.common.pojo.PageParam;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统分级组分页 Request VO
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 系统分级组分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HierarchyGroupPageReqVO extends PageParam {

    @Schema(description = "分级组名称", example = "设备分级")
    private String name;

    @Schema(description = "分级组编码", example = "DEVICE_HIERARCHY")
    private String code;

    @Schema(description = "父分级组ID", example = "1")
    private Long parentId;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

} 