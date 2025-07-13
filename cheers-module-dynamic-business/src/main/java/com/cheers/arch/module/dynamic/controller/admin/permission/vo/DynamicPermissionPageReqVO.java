package com.cheers.arch.module.dynamic.controller.admin.permission.vo;

import com.cheers.arch.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 动态业务权限分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicPermissionPageReqVO extends PageParam {

    @Schema(description = "业务模型编码", example = "user")
    private String modelCode;

    @Schema(description = "权限类型", example = "1")
    private Integer type;

    @Schema(description = "权限目标", example = "name")
    private String target;

    @Schema(description = "用户ID", example = "1024")
    private Long userId;

    @Schema(description = "角色ID", example = "2048")
    private Long roleId;

    @Schema(description = "部门ID", example = "4096")
    private Long deptId;
} 