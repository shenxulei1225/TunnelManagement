package com.cheers.arch.module.system.controller.admin.hierarchy.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统分级组创建 Request VO
 *
 * @author cheers
 */
@Schema(description = "管理后台 - 系统分级组创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HierarchyGroupCreateReqVO extends HierarchyGroupBaseVO {

} 