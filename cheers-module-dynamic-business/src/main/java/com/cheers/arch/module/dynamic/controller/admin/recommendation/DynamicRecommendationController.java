package com.cheers.arch.module.dynamic.controller.admin.recommendation;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldMatchVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldRecommendationVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.PreviewDataVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.PreviewRequestVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.SceneTemplateVO;
import com.cheers.arch.module.dynamic.service.preview.DynamicPreviewService;
import com.cheers.arch.module.dynamic.service.recommendation.DynamicRecommendationService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "管理后台 - 动态推荐")
@RestController
@RequestMapping("/dynamic/recommendation")
@Validated
public class DynamicRecommendationController {

    @Resource
    private DynamicRecommendationService recommendationService;

    @Resource
    private DynamicPreviewService previewService;

    @GetMapping("/templates")
    @Operation(summary = "获取场景模板推荐")
    public CommonResult<List<SceneTemplateVO>> getRecommendedTemplates(
            @Parameter(description = "业务类型", required = true) @RequestParam String businessType) {
        return success(recommendationService.getRecommendedTemplates(businessType));
    }

    @GetMapping("/fields")
    @Operation(summary = "获取字段推荐")
    public CommonResult<List<FieldRecommendationVO>> getFieldRecommendations(
            @Parameter(description = "模板编码", required = true) @RequestParam String templateCode) {
        return success(recommendationService.getFieldRecommendations(templateCode));
    }

    @GetMapping("/user-fields")
    @Operation(summary = "获取基于用户行为的字段推荐")
    public CommonResult<List<FieldRecommendationVO>> getUserBasedRecommendations(
            @Parameter(description = "用户ID", required = true) @RequestParam Long userId,
            @Parameter(description = "业务类型", required = true) @RequestParam String businessType) {
        return success(recommendationService.getUserBasedRecommendations(userId, businessType));
    }

    @GetMapping("/field-matches")
    @Operation(summary = "获取智能字段匹配")
    public CommonResult<List<FieldMatchVO>> getFieldMatches(
            @Parameter(description = "业务类型", required = true) @RequestParam String businessType,
            @Parameter(description = "场景类型", required = true) @RequestParam String sceneType) {
        return success(recommendationService.getIntelligentFieldMatches(businessType, sceneType));
    }

    @PostMapping("/preview")
    @Operation(summary = "生成界面预览")
    public CommonResult<PreviewDataVO> generatePreview(@Valid @RequestBody PreviewRequestVO request) {
        return success(previewService.generatePreview(request.getModelCode(), request.getViewType(), request.getFields()));
    }

    @PostMapping("/record-behavior")
    @Operation(summary = "记录用户行为")
    public CommonResult<Boolean> recordUserBehavior(
            @Parameter(description = "用户ID", required = true) @RequestParam Long userId,
            @Parameter(description = "业务类型", required = true) @RequestParam String businessType,
            @Parameter(description = "场景类型", required = true) @RequestParam String sceneType,
            @Parameter(description = "模型编码", required = true) @RequestParam String modelCode,
            @Parameter(description = "字段使用情况", required = true) @RequestParam String fieldUsage) {
        recommendationService.recordUserBehavior(userId, businessType, sceneType, modelCode, fieldUsage);
        return success(true);
    }
} 