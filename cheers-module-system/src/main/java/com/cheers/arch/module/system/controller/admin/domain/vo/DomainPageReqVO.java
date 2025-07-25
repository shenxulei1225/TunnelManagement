package com.cheers.arch.module.system.controller.admin.domain.vo;

import com.cheers.arch.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 领域模型分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomainPageReqVO extends PageParam {

    @Schema(description = "领域名称", example = "用户管理")
    private String name;

    @Schema(description = "领域编码", example = "user_management")
    private String code;

    @Schema(description = "父领域ID", example = "0")
    private Long parentId;

    @Schema(description = "领域类型", example = "business")
    private String type;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

} 