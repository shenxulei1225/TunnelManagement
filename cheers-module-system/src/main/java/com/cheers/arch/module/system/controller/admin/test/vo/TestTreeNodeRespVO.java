package com.cheers.arch.module.system.controller.admin.test.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 测试树节点 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestTreeNodeRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "节点名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "总公司")
    private String name;

    @Schema(description = "节点类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "dept")
    private String type;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "dept")
    private String businessType;

    @Schema(description = "父节点编号", example = "1024")
    private Long parentId;

    @Schema(description = "节点描述", example = "这是一个测试节点")
    private String description;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "节点计数", example = "5")
    private Integer count;

    @Schema(description = "扩展属性", example = "{}")
    private String extraAttrs;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "子节点列表")
    private List<TestTreeNodeRespVO> children;

} 