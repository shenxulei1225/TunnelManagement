package com.cheers.arch.module.system.util;

import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import org.apache.commons.text.StringSubstitutor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工具：根据字段定义的公式(calcExpr)计算派生字段值
 */
public class FieldFormulaUtils {

    /**
     * 将动态字段中的公式字段计算并填充到 attrs
     * @param attrs 现有值 Map，可读写
     * @param fields  当前业务全部字段定义
     */
    public static void applyComputedFields(Map<String, Object> attrs, List<FieldDO> fields) {
        if (attrs == null || fields == null) return;
        // 1. 找到所有 fieldType=COMPUTED 且 calcExpr 不为空
        List<FieldDO> computedFields = fields.stream()
                .filter(f -> "COMPUTED".equalsIgnoreCase(f.getFieldType()))
                .filter(f -> f.getCalcExpr() != null && !f.getCalcExpr().isBlank())
                .collect(Collectors.toList());
        if (computedFields.isEmpty()) return;
        // 2. 使用 StringSubstitutor 进行替换
        computedFields.forEach(field -> {
            String expr = field.getCalcExpr();
            // 支持 ${key} 占位符
            // 将 Object 值转为字符串并执行占位符替换，例如 ${fireZoneSymbol}${fireZoneNumber}
            Map<String, String> strMap = attrs.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            e -> e.getValue() == null ? "" : String.valueOf(e.getValue())));
            String result = StringSubstitutor.replace(expr, strMap, "${", "}");
            attrs.put(field.getFieldCode(), result);
        });
    }
}
