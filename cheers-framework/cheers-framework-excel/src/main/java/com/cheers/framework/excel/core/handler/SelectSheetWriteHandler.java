package com.cheers.framework.excel.core.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.cheers.framework.excel.core.annotations.ExcelColumnSelect;
import com.cheers.framework.excel.core.function.ExcelColumnSelectFunction;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 下拉框处理器
 */
@RequiredArgsConstructor
public class SelectSheetWriteHandler implements SheetWriteHandler {

    private final ApplicationContext applicationContext;
    private final Class<?> head;

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 获得 Sheet 表单
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 遍历所有字段，查找 @ExcelColumnSelect 注解
        Field[] fields = head.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            ExcelColumnSelect annotation = fields[i].getAnnotation(ExcelColumnSelect.class);
            if (annotation == null) {
                continue;
            }
            // 获得下拉框的值
            String[] values = getSelectValues(annotation);
            // 创建下拉框约束
            DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
            // 设置下拉框的位置
            CellRangeAddressList rangeList = new CellRangeAddressList(1, 1000, i, i);
            // 创建下拉框并添加到 Sheet 中
            DataValidation validation = helper.createValidation(constraint, rangeList);
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);
        }
    }

    private String[] getSelectValues(ExcelColumnSelect annotation) {
        // 优先使用 value 属性
        if (annotation.value().length > 0) {
            return annotation.value();
        }
        // 其次使用 function 属性
        if (annotation.function() != ExcelColumnSelectFunction.class) {
            ExcelColumnSelectFunction function = applicationContext.getBean(annotation.function());
            List<String> values = new ArrayList<>(function.getSelectValues());
            return values.toArray(new String[0]);
        }
        throw new IllegalArgumentException("@ExcelColumnSelect 注解的 value 和 function 属性必须设置一个");
    }

} 