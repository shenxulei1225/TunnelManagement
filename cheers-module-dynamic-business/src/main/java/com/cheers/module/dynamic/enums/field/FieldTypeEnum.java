package com.cheers.arch.module.dynamic.enums.field;

import com.cheers.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型枚举
 */
@Getter
@AllArgsConstructor
public enum FieldTypeEnum implements ArrayValuable {

    TEXT("text", "文本"),
    TEXTAREA("textarea", "长文本"),
    NUMBER("number", "数字"),
    DECIMAL("decimal", "小数"),
    DATE("date", "日期"),
    DATETIME("datetime", "日期时间"),
    TIME("time", "时间"),
    SELECT("select", "下拉选择"),
    MULTI_SELECT("multi_select", "多选"),
    RADIO("radio", "单选"),
    CHECKBOX("checkbox", "复选框"),
    SWITCH("switch", "开关"),
    FILE("file", "文件"),
    IMAGE("image", "图片"),
    RICH_TEXT("rich_text", "富文本"),
    CASCADER("cascader", "级联选择"),
    REFERENCE("reference", "引用"),
    CUSTOM("custom", "自定义");

    /**
     * 类型编码
     */
    private final String type;

    /**
     * 类型名称
     */
    private final String name;

    @Override
    public String[] array() {
        FieldTypeEnum[] values = values();
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getType();
        }
        return array;
    }
} 