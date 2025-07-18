package com.cheers.arch.module.dynamic.service.recommendation.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldMatchVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldRecommendationVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.SceneTemplateVO;
import com.cheers.arch.module.dynamic.dal.dataobject.behavior.DynamicUserBehaviorDO;
import com.cheers.arch.module.dynamic.dal.dataobject.template.DynamicSceneTemplateDO;
import com.cheers.arch.module.dynamic.dal.mysql.behavior.DynamicUserBehaviorMapper;
import com.cheers.arch.module.dynamic.dal.mysql.template.DynamicSceneTemplateMapper;
import com.cheers.arch.module.dynamic.service.recommendation.DynamicRecommendationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态推荐服务实现类
 */
@Service
@Slf4j
public class DynamicRecommendationServiceImpl implements DynamicRecommendationService {

    @Resource
    private DynamicSceneTemplateMapper sceneTemplateMapper;

    @Resource
    private DynamicUserBehaviorMapper userBehaviorMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public List<SceneTemplateVO> getRecommendedTemplates(String businessType) {
        // 根据业务类型查询场景模板
        List<DynamicSceneTemplateDO> templates = sceneTemplateMapper.selectByBusinessType(businessType);
        
        return templates.stream()
                .filter(template -> template.getStatus() == 1) // 只返回启用的模板
                .map(this::convertToSceneTemplateVO)
                .sorted(Comparator.comparing(SceneTemplateVO::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldRecommendationVO> getFieldRecommendations(String templateCode) {
        // 根据模板编码查询场景模板
        DynamicSceneTemplateDO template = sceneTemplateMapper.selectByCode(templateCode);
        if (template == null) {
            return Collections.emptyList();
        }

        try {
            // 解析推荐字段配置
            List<FieldRecommendationVO> recommendations = objectMapper.readValue(
                    template.getRecommendedFields(), 
                    new TypeReference<List<FieldRecommendationVO>>() {}
            );
            return recommendations;
        } catch (Exception e) {
            log.error("解析推荐字段配置失败", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<FieldRecommendationVO> getUserBasedRecommendations(Long userId, String businessType) {
        // 查询用户历史行为
        List<DynamicUserBehaviorDO> behaviors = userBehaviorMapper.selectByUserIdAndBusinessType(userId, businessType);
        
        // 分析用户行为，生成推荐字段
        Map<String, Integer> fieldUsageCount = new HashMap<>();
        for (DynamicUserBehaviorDO behavior : behaviors) {
            try {
                Map<String, Object> fieldUsage = objectMapper.readValue(
                        behavior.getFieldUsage(), 
                        new TypeReference<Map<String, Object>>() {}
                );
                
                // 统计字段使用频率
                for (String fieldCode : fieldUsage.keySet()) {
                    fieldUsageCount.merge(fieldCode, 1, Integer::sum);
                }
            } catch (Exception e) {
                log.error("解析用户行为数据失败", e);
            }
        }

        // 根据使用频率生成推荐
        return fieldUsageCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10) // 取前10个最常用的字段
                .map(entry -> {
                    FieldRecommendationVO recommendation = new FieldRecommendationVO();
                    recommendation.setFieldCode(entry.getKey());
                    recommendation.setFieldName(getFieldNameByCode(entry.getKey()));
                    recommendation.setFieldType(getFieldTypeByCode(entry.getKey()));
                    recommendation.setRequired(false);
                    recommendation.setRecommendationReason("基于您的使用习惯推荐");
                    recommendation.setWeight(entry.getValue() * 0.1); // 根据使用频率计算权重
                    return recommendation;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldMatchVO> getIntelligentFieldMatches(String businessType, String sceneType) {
        List<FieldMatchVO> matches = new ArrayList<>();
        
        // 基于业务类型和场景类型进行智能匹配
        Map<String, Double> fieldScores = calculateFieldScores(businessType, sceneType);
        
        for (Map.Entry<String, Double> entry : fieldScores.entrySet()) {
            FieldMatchVO match = new FieldMatchVO();
            match.setFieldCode(entry.getKey());
            match.setFieldName(getFieldNameByCode(entry.getKey()));
            match.setFieldType(getFieldTypeByCode(entry.getKey()));
            match.setMatchScore(entry.getValue());
            match.setMatchReason("基于业务场景智能匹配");
            match.setRecommended(entry.getValue() > 0.7); // 匹配度大于0.7的字段推荐
            matches.add(match);
        }
        
        // 按匹配度排序
        matches.sort(Comparator.comparing(FieldMatchVO::getMatchScore).reversed());
        return matches;
    }

    @Override
    public void recordUserBehavior(Long userId, String businessType, String sceneType, String modelCode, String fieldUsage) {
        DynamicUserBehaviorDO behavior = new DynamicUserBehaviorDO();
        behavior.setUserId(userId);
        behavior.setBusinessType(businessType);
        behavior.setSceneType(sceneType);
        behavior.setModelCode(modelCode);
        behavior.setFieldUsage(fieldUsage);
        behavior.setOperationType("create");
        behavior.setOperationTime(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        
        userBehaviorMapper.insert(behavior);
    }

    /**
     * 转换为场景模板VO
     */
    private SceneTemplateVO convertToSceneTemplateVO(DynamicSceneTemplateDO template) {
        SceneTemplateVO vo = new SceneTemplateVO();
        vo.setId(template.getId());
        vo.setCode(template.getCode());
        vo.setName(template.getName());
        vo.setBusinessType(template.getBusinessType());
        vo.setDescription(template.getDescription());
        vo.setRecommendedFields(template.getRecommendedFields());
        vo.setDefaultLayout(template.getDefaultLayout());
        vo.setValidationRules(template.getValidationRules());
        vo.setIcon(template.getIcon());
        vo.setSort(template.getSort());
        vo.setStatus(template.getStatus());
        vo.setCreateTime(template.getCreateTime());
        
        return vo;
    }

    /**
     * 根据字段编码获取字段名称
     */
    private String getFieldNameByCode(String fieldCode) {
        // 这里可以根据字段编码映射到字段名称
        // 实际实现中可以从字段定义表中查询
        Map<String, String> fieldNameMap = new HashMap<>();
        fieldNameMap.put("inspection_date", "巡检日期");
        fieldNameMap.put("inspection_person", "巡检人员");
        fieldNameMap.put("inspection_result", "巡检结果");
        fieldNameMap.put("inspection_notes", "巡检备注");
        
        return fieldNameMap.getOrDefault(fieldCode, fieldCode);
    }

    /**
     * 根据字段编码获取字段类型
     */
    private String getFieldTypeByCode(String fieldCode) {
        // 这里可以根据字段编码映射到字段类型
        Map<String, String> fieldTypeMap = new HashMap<>();
        fieldTypeMap.put("inspection_date", "date");
        fieldTypeMap.put("inspection_person", "string");
        fieldTypeMap.put("inspection_result", "enum");
        fieldTypeMap.put("inspection_notes", "textarea");
        
        return fieldTypeMap.getOrDefault(fieldCode, "string");
    }

    /**
     * 计算字段匹配分数
     */
    private Map<String, Double> calculateFieldScores(String businessType, String sceneType) {
        Map<String, Double> scores = new HashMap<>();
        
        // 基于业务类型和场景类型的规则匹配
        if ("inspection".equals(businessType)) {
            if ("daily".equals(sceneType)) {
                scores.put("inspection_date", 0.9);
                scores.put("inspection_person", 0.8);
                scores.put("inspection_result", 0.7);
                scores.put("inspection_notes", 0.6);
            } else if ("key".equals(sceneType)) {
                scores.put("inspection_date", 0.9);
                scores.put("inspection_person", 0.9);
                scores.put("inspection_result", 0.8);
                scores.put("inspection_notes", 0.8);
                scores.put("inspection_priority", 0.7);
            }
        }
        
        return scores;
    }
} 