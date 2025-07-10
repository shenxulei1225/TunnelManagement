package com.cheers.module.dynamic.enums.field;

import com.cheers.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 显示类型枚举
 */
@Getter
@AllArgsConstructor
public enum DisplayTypeEnum implements ArrayValuable {

    INPUT("input", "输入框"),
    TEXTAREA("textarea", "文本域"),
    NUMBER_INPUT("number", "数字输入框"),
    DATE_PICKER("date", "日期选择器"),
    TIME_PICKER("time", "时间选择器"),
    DATETIME_PICKER("datetime", "日期时间选择器"),
    SELECT("select", "下拉选择器"),
    MULTI_SELECT("multi_select", "多选选择器"),
    RADIO("radio", "单选框"),
    CHECKBOX("checkbox", "复选框"),
    SWITCH("switch", "开关"),
    SLIDER("slider", "滑块"),
    RATE("rate", "评分"),
    COLOR_PICKER("color", "颜色选择器"),
    UPLOAD("upload", "上传控件"),
    RICH_TEXT("rich_text", "富文本编辑器"),
    CASCADER("cascader", "级联选择器"),
    TREE_SELECT("tree_select", "树选择器"),
    CUSTOM("custom", "自定义组件");

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
        DisplayTypeEnum[] values = values();
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getType();
        }
        return array;
    }
} 