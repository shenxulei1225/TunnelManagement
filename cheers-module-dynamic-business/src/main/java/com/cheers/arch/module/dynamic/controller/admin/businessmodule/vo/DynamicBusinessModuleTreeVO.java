package com.cheers.arch.module.dynamic.controller.admin.businessmodule.vo;

import com.cheers.arch.framework.trees.core.TreeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 动态业务分组树形 VO")
@Data
public class DynamicBusinessModuleTreeVO implements TreeVO<Long, DynamicBusinessModuleTreeVO> {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "父节点 ID，0 表示根", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Long parentId;

    @Schema(description = "节点编码", example = "INSPECTION")
    private String code;

    @Schema(description = "展示名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "巡检系统")
    private String name;

    @Schema(description = "树路径，如 \"1/15/37\"", example = "1/15/37")
    private String treePath;

    @Schema(description = "层级深度", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer level;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "是否系统只读，1=只读（禁止删除/改名）", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean readonly;

    @Schema(description = "状态：1=启用，0=禁用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "子节点列表")
    private List<DynamicBusinessModuleTreeVO> children;
} 