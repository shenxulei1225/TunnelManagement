package com.cheers.arch.module.system.controller.admin.region.vo;

import java.util.List;

import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 区域树形结构 Response VO")
@Data
public class TreeRegionVO {

    @Schema(description = "节点ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "节点标签", requiredMode = Schema.RequiredMode.REQUIRED)
    private String label;

    @Schema(description = "是否禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean disabled;

    @Schema(description = "是否是叶子节点", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isLeaf;

    @Schema(description = "子节点", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<TreeRegionVO> children;

    /**
     * 从RegionDO创建TreeRegionVO
     */
    public static TreeRegionVO fromRegionDO(RegionDO region) {
        if (region == null) {
            return null;
        }
        TreeRegionVO vo = new TreeRegionVO();
        vo.setId(region.getId());
        vo.setLabel(region.getName());
        vo.setDisabled(region.getStatus() != null && region.getStatus() == 1);
        vo.setIsLeaf(region.getLeaf());
        return vo;
    }
} 