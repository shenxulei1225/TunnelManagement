package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统分级组更新 Request VO
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 系统分级组更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HierarchyGroupUpdateReqVO extends HierarchyGroupBaseVO {

    @Schema(description = "分级组ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "分级组ID不能为空")
    private Long id;

} 