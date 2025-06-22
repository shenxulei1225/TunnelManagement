package cn.iocoder.yudao.module.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 区域 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RegionRespVO {

    /**
     * 动态扩展属性(JSON)
     */
    private Map<String, Object> extraAttrs;


    @Schema(description = "区域id", requiredMode = Schema.RequiredMode.REQUIRED, example = "5985")
    @ExcelProperty("区域id")
    private Long id;

    @Schema(description = "区域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("区域名称")
    private String name;

    @Schema(description = "父区域id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27311")
    @ExcelProperty("父区域id")
    private Long parentId;

    @Schema(description = "分类ID")
    @ExcelProperty("分类ID")
    private Long categoryId;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("显示顺序")
    private Integer sort;

    @Schema(description = "区域负责人", example = "30178")
    @ExcelProperty("区域负责人")
    private Long leaderUserId;



    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "区域状态（0正常 1停用）", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}