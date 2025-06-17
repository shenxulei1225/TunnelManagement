package cn.iocoder.yudao.module.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 区域新增/修改 Request VO")
@Data
public class RegionSaveReqVO {

    @Schema(description = "区域id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5985")
    private Long id;

    @Schema(description = "区域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "区域名称不能为空")
    private String name;

    @Schema(description = "父区域id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27311")
    @NotNull(message = "父区域id不能为空")
    private Long parentId;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    @Schema(description = "区域负责人", example = "30178")
    private Long leaderUserId;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "区域类型", example = "1")
    private Integer type;

    @Schema(description = "所在路段")
    private String roadSection;

    @Schema(description = "起始位置")
    private String startPosition;

    @Schema(description = "结束位置")
    private String endPosition;

}