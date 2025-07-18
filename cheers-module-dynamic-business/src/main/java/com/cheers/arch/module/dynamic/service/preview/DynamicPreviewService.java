package com.cheers.arch.module.dynamic.service.preview;

import java.util.List;

import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldConfigVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.PreviewDataVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.ValidationResultVO;

/**
 * 动态预览服务接口
 */
public interface DynamicPreviewService {

    /**
     * 生成界面预览数据
     *
     * @param modelCode 模型编码
     * @param viewType 视图类型
     * @param fields 字段配置列表
     * @return 预览数据
     */
    PreviewDataVO generatePreview(String modelCode, String viewType, List<FieldConfigVO> fields);

    /**
     * 验证字段配置
     *
     * @param fields 字段配置列表
     * @return 验证结果
     */
    ValidationResultVO validateFieldConfig(List<FieldConfigVO> fields);

    /**
     * 生成菜单配置
     *
     * @param modelCode 模型编码
     * @param viewCode 视图编码
     * @param viewName 视图名称
     * @return 菜单配置JSON
     */
    String generateMenuConfig(String modelCode, String viewCode, String viewName);

    /**
     * 生成界面HTML
     *
     * @param modelCode 模型编码
     * @param viewType 视图类型
     * @param fields 字段配置列表
     * @return HTML字符串
     */
    String generatePreviewHtml(String modelCode, String viewType, List<FieldConfigVO> fields);
} 