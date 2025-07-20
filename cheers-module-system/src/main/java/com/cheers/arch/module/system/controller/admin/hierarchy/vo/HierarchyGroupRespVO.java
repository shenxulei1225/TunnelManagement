package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统分级组 Response VO
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 系统分级组 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HierarchyGroupRespVO extends HierarchyGroupBaseVO {

    @Schema(description = "分级组ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "层级", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer level;

    @Schema(description = "分级组路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "/1/2")
    private String path;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "子分级组列表")
    private List<HierarchyGroupRespVO> children;

    @Schema(description = "关联对象数量", example = "10")
    private Long count;

} 