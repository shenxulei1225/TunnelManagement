package com.cheers.arch.module.dynamic.service.recommendation;

import java.util.List;

import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldMatchVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldRecommendationVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.SceneTemplateVO;

/**
 * 动态推荐服务接口
 */
public interface DynamicRecommendationService {

    /**
     * 根据业务类型获取推荐场景模板
     *
     * @param businessType 业务类型
     * @return 场景模板列表
     */
    List<SceneTemplateVO> getRecommendedTemplates(String businessType);

    /**
     * 根据场景模板推荐字段
     *
     * @param templateCode 模板编码
     * @return 字段推荐列表
     */
    List<FieldRecommendationVO> getFieldRecommendations(String templateCode);

    /**
     * 根据用户历史行为推荐字段
     *
     * @param userId 用户ID
     * @param businessType 业务类型
     * @return 字段推荐列表
     */
    List<FieldRecommendationVO> getUserBasedRecommendations(Long userId, String businessType);

    /**
     * 智能字段匹配
     *
     * @param businessType 业务类型
     * @param sceneType 场景类型
     * @return 字段匹配列表
     */
    List<FieldMatchVO> getIntelligentFieldMatches(String businessType, String sceneType);

    /**
     * 记录用户行为
     *
     * @param userId 用户ID
     * @param businessType 业务类型
     * @param sceneType 场景类型
     * @param modelCode 模型编码
     * @param fieldUsage 字段使用情况
     */
    void recordUserBehavior(Long userId, String businessType, String sceneType, String modelCode, String fieldUsage);
} 