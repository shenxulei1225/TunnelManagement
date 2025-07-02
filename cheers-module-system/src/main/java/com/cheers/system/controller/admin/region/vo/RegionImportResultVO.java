package com.cheers.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 区域批量导入结果 Response VO")
@Data
public class RegionImportResultVO {

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "batch_20240702_001")
    private String batchId;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "管廊段")
    private String categoryName;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "regions.xlsx")
    private String fileName;

    @Schema(description = "总记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer totalCount;

    @Schema(description = "成功记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "95")
    private Integer successCount;

    @Schema(description = "失败记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Integer failedCount;

    @Schema(description = "导入状态：0-处理中，1-成功，2-失败", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "错误信息", example = "第3行：名称不能为空")
    private String errorMessage;

    @Schema(description = "详细错误列表")
    private List<ImportErrorDetail> errorDetails;

    @Schema(description = "导入时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime importTime;

    @Schema(description = "导入用户", example = "admin")
    private String importUser;

    @Data
    @Schema(description = "导入错误详情")
    public static class ImportErrorDetail {
        @Schema(description = "行号", example = "3")
        private Integer rowNum;
        
        @Schema(description = "错误字段", example = "name")
        private String fieldName;
        
        @Schema(description = "错误信息", example = "名称不能为空")
        private String errorMessage;
        
        @Schema(description = "原始数据")
        private String originalData;
    }
} 