package com.cheers.arch.module.dynamic.convert.recommendation;

import java.util.List;

import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.SceneTemplateVO;
import com.cheers.arch.module.dynamic.dal.dataobject.template.DynamicSceneTemplateDO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 动态推荐 Convert
 */
@Mapper
public interface DynamicRecommendationConvert {

    DynamicRecommendationConvert INSTANCE = Mappers.getMapper(DynamicRecommendationConvert.class);

    SceneTemplateVO convert(DynamicSceneTemplateDO bean);

    List<SceneTemplateVO> convertList(List<DynamicSceneTemplateDO> list);
} 