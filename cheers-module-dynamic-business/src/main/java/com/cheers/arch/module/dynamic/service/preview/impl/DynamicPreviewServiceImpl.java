package com.cheers.arch.module.dynamic.service.preview.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.FieldConfigVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.PreviewDataVO;
import com.cheers.arch.module.dynamic.controller.admin.recommendation.vo.ValidationResultVO;
import com.cheers.arch.module.dynamic.service.preview.DynamicPreviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态预览服务实现类
 */
@Service
@Slf4j
public class DynamicPreviewServiceImpl implements DynamicPreviewService {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public PreviewDataVO generatePreview(String modelCode, String viewType, List<FieldConfigVO> fields) {
        PreviewDataVO previewData = new PreviewDataVO();
        previewData.setModelCode(modelCode);
        previewData.setViewType(viewType);
        previewData.setFieldConfigs(fields);
        
        // 生成布局配置
        previewData.setLayoutConfig(generateLayoutConfig(viewType, fields));
        
        // 生成操作配置
        previewData.setActionConfig(generateActionConfig(viewType));
        
        // 生成预览HTML
        previewData.setPreviewHtml(generatePreviewHtml(modelCode, viewType, fields));
        
        // 验证字段配置
        previewData.setValidationResult(validateFieldConfig(fields));
        
        return previewData;
    }

    @Override
    public ValidationResultVO validateFieldConfig(List<FieldConfigVO> fields) {
        ValidationResultVO result = new ValidationResultVO();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        
        // 检查必填字段
        for (FieldConfigVO field : fields) {
            if (field.getRequired() != null && field.getRequired()) {
                if (field.getFieldName() == null || field.getFieldName().trim().isEmpty()) {
                    errors.add("必填字段 '" + field.getFieldCode() + "' 缺少字段名称");
                }
            }
            
            // 检查字段编码唯一性
            long duplicateCount = fields.stream()
                    .filter(f -> f.getFieldCode().equals(field.getFieldCode()))
                    .count();
            if (duplicateCount > 1) {
                errors.add("字段编码 '" + field.getFieldCode() + "' 重复");
            }
            
            // 检查字段类型
            if (field.getFieldType() == null || field.getFieldType().trim().isEmpty()) {
                warnings.add("字段 '" + field.getFieldCode() + "' 缺少字段类型");
            }
        }
        
        // 检查是否有主键字段
        boolean hasPrimaryKey = fields.stream()
                .anyMatch(field -> "id".equals(field.getFieldCode()));
        if (!hasPrimaryKey) {
            suggestions.add("建议添加主键字段 'id'");
        }
        
        result.setValid(errors.isEmpty());
        result.setErrors(errors);
        result.setWarnings(warnings);
        result.setSuggestions(suggestions);
        
        return result;
    }

    @Override
    public String generateMenuConfig(String modelCode, String viewCode, String viewName) {
        Map<String, Object> menuConfig = new HashMap<>();
        menuConfig.put("name", viewName);
        menuConfig.put("path", "/" + modelCode + "/" + viewCode);
        menuConfig.put("component", "views/" + modelCode + "/" + viewCode + "/index");
        menuConfig.put("permission", modelCode + ":" + viewCode + ":list");
        menuConfig.put("icon", "el-icon-document");
        menuConfig.put("sort", 100);
        menuConfig.put("status", 1);
        menuConfig.put("visible", 1);
        
        try {
            return objectMapper.writeValueAsString(menuConfig);
        } catch (Exception e) {
            log.error("生成菜单配置失败", e);
            return "{}";
        }
    }

    @Override
    public String generatePreviewHtml(String modelCode, String viewType, List<FieldConfigVO> fields) {
        StringBuilder html = new StringBuilder();
        
        if ("list".equals(viewType)) {
            html.append(generateListPreviewHtml(fields));
        } else if ("form".equals(viewType)) {
            html.append(generateFormPreviewHtml(fields));
        } else if ("detail".equals(viewType)) {
            html.append(generateDetailPreviewHtml(fields));
        }
        
        return html.toString();
    }

    /**
     * 生成列表页预览HTML
     */
    private String generateListPreviewHtml(List<FieldConfigVO> fields) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='preview-list'>");
        html.append("<table class='el-table'>");
        html.append("<thead><tr>");
        
        // 表头
        for (FieldConfigVO field : fields) {
            html.append("<th>").append(field.getFieldName()).append("</th>");
        }
        html.append("<th>操作</th>");
        html.append("</tr></thead>");
        
        // 示例数据行
        html.append("<tbody><tr>");
        for (FieldConfigVO field : fields) {
            html.append("<td>").append(getFieldPreviewValue(field)).append("</td>");
        }
        html.append("<td><el-button size='small'>编辑</el-button></td>");
        html.append("</tr></tbody>");
        
        html.append("</table>");
        html.append("</div>");
        
        return html.toString();
    }

    /**
     * 生成表单页预览HTML
     */
    private String generateFormPreviewHtml(List<FieldConfigVO> fields) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='preview-form'>");
        html.append("<el-form label-width='120px'>");
        
        for (FieldConfigVO field : fields) {
            html.append("<el-form-item label='").append(field.getFieldName()).append("'>");
            html.append(generateFieldInput(field));
            html.append("</el-form-item>");
        }
        
        html.append("<el-form-item>");
        html.append("<el-button type='primary'>保存</el-button>");
        html.append("<el-button>取消</el-button>");
        html.append("</el-form-item>");
        html.append("</el-form>");
        html.append("</div>");
        
        return html.toString();
    }

    /**
     * 生成详情页预览HTML
     */
    private String generateDetailPreviewHtml(List<FieldConfigVO> fields) {
        StringBuilder html = new StringBuilder();
        html.append("<div class='preview-detail'>");
        html.append("<el-descriptions :column='2' border>");
        
        for (FieldConfigVO field : fields) {
            html.append("<el-descriptions-item label='").append(field.getFieldName()).append("'>");
            html.append(getFieldPreviewValue(field));
            html.append("</el-descriptions-item>");
        }
        
        html.append("</el-descriptions>");
        html.append("</div>");
        
        return html.toString();
    }

    /**
     * 生成字段输入控件
     */
    private String generateFieldInput(FieldConfigVO field) {
        switch (field.getFieldType()) {
            case "string":
                return "<el-input placeholder='请输入" + field.getFieldName() + "' />";
            case "number":
                return "<el-input-number placeholder='请输入" + field.getFieldName() + "' />";
            case "date":
                return "<el-date-picker type='date' placeholder='请选择" + field.getFieldName() + "' />";
            case "datetime":
                return "<el-date-picker type='datetime' placeholder='请选择" + field.getFieldName() + "' />";
            case "textarea":
                return "<el-input type='textarea' placeholder='请输入" + field.getFieldName() + "' />";
            case "select":
                return "<el-select placeholder='请选择" + field.getFieldName() + "'><el-option label='选项1' value='1' /></el-select>";
            default:
                return "<el-input placeholder='请输入" + field.getFieldName() + "' />";
        }
    }

    /**
     * 获取字段预览值
     */
    private String getFieldPreviewValue(FieldConfigVO field) {
        switch (field.getFieldType()) {
            case "string":
                return "示例文本";
            case "number":
                return "123";
            case "date":
                return "2024-01-01";
            case "datetime":
                return "2024-01-01 12:00:00";
            case "textarea":
                return "示例长文本内容";
            case "select":
                return "选项1";
            default:
                return "示例值";
        }
    }

    /**
     * 生成布局配置
     */
    private Map<String, Object> generateLayoutConfig(String viewType, List<FieldConfigVO> fields) {
        Map<String, Object> layout = new HashMap<>();
        layout.put("type", viewType);
        layout.put("columns", 2);
        layout.put("fields", fields.stream()
                .map(field -> {
                    Map<String, Object> fieldLayout = new HashMap<>();
                    fieldLayout.put("code", field.getFieldCode());
                    fieldLayout.put("span", "string".equals(field.getFieldType()) ? 24 : 12);
                    return fieldLayout;
                })
                .collect(Collectors.toList()));
        
        return layout;
    }

    /**
     * 生成操作配置
     */
    private Map<String, Object> generateActionConfig(String viewType) {
        Map<String, Object> actions = new HashMap<>();
        
        if ("list".equals(viewType)) {
            actions.put("create", true);
            actions.put("update", true);
            actions.put("delete", true);
            actions.put("export", true);
        } else if ("form".equals(viewType)) {
            actions.put("save", true);
            actions.put("cancel", true);
        }
        
        return actions;
    }
} 