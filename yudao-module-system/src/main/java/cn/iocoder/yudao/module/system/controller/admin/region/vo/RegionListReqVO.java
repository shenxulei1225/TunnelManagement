package cn.iocoder.yudao.module.system.controller.admin.region.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 区域分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RegionListReqVO extends PageParam {

    @Schema(description = "区域名称", example = "王五")
    private String name;

    @Schema(description = "父区域id", example = "27311")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "区域负责人", example = "30178")
    private Long leaderUserId;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "区域状态（0正常 1停用）", example = "1")
    private Integer status;

    @Schema(description = "区域类型", example = "1")
    private Integer type;

    @Schema(description = "所在路段（模糊查询）")
    private String roadSection;

    @Schema(description = "起始位置（模糊查询）")
    private String startPosition;

    @Schema(description = "结束位置（模糊查询）")
    private String endPosition;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}