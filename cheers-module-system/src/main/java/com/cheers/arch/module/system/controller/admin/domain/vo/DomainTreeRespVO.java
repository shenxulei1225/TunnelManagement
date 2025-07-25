package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 领域模型树形结构 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainTreeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "领域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    private String name;

    @Schema(description = "领域编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "user_management")
    private String code;

    @Schema(description = "父领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Long parentId;

    @Schema(description = "领域描述", example = "用户相关的业务领域")
    private String description;

    @Schema(description = "领域类型", example = "business")
    private String type;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    // ================ Element Plus 树组件所需字段 ================

    @Schema(description = "树节点标签（Element Plus tree组件需要）", example = "用户管理")
    private String label;

    @Schema(description = "树节点值（Element Plus tree组件需要）", example = "1024")
    private String value;

    @Schema(description = "是否为叶子节点（Element Plus tree组件需要）", example = "false")
    private Boolean isLeaf;

    @Schema(description = "是否禁用节点（Element Plus tree组件需要）", example = "false")
    private Boolean disabled;

    @Schema(description = "子节点列表（Element Plus tree组件需要）")
    private List<DomainTreeRespVO> children;

    // ================ 统计信息字段 ================

    @Schema(description = "直接关联的字段数量", example = "5")
    private Integer directFieldCount;

    @Schema(description = "直接子领域数量", example = "3")
    private Integer directChildCount;

    @Schema(description = "总字段数量（包含子领域）", example = "15")
    private Integer totalFieldCount;

    @Schema(description = "总子领域数量（包含所有后代）", example = "8")
    private Integer totalChildCount;

    @Schema(description = "层级深度", example = "2")
    private Integer depth;

    // ================ 扩展字段 ================

    @Schema(description = "图标（用于前端展示）", example = "folder")
    private String icon;

    @Schema(description = "是否展开（用于前端展示）", example = "true")
    private Boolean expanded;

    @Schema(description = "节点类型（用于前端区分）", example = "domain")
    private String nodeType;

} 