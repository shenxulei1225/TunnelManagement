package com.cheers.framework.excel.core.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.cheers.framework.excel.core.annotations.ExcelProperty;
import com.cheers.framework.excel.core.convert.ExcelDataConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel写入处理器
 * 支持下拉框、数据校验、单元格格式化等功能
 */
@Slf4j
public class ExcelSheetWriteHandler implements SheetWriteHandler {

    /**
     * 数据转换器
     */
    private final Map<String, ExcelDataConvert> converts;

    /**
     * 数据校验器
     */
    private final Map<String, DataValidation> validations;

    /**
     * 单元格样式
     */
    private final Map<String, CellStyle> styles;

    /**
     * 列宽配置
     */
    private final Map<Integer, Integer> columnWidths;

    public ExcelSheetWriteHandler() {
        this.converts = new HashMap<>();
        this.validations = new HashMap<>();
        this.styles = new HashMap<>();
        this.columnWidths = new HashMap<>();
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 初始化配置
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        // 设置列宽
        setColumnWidth(sheet);

        // 创建样式
        createStyles(workbook);

        // 创建数据校验
        createValidations(sheet);

        log.debug("[afterSheetCreate][创建Sheet完成]");
    }

    /**
     * 添加数据转换器
     */
    public void addConvert(String field, ExcelDataConvert convert) {
        converts.put(field, convert);
    }

    /**
     * 添加数据校验
     */
    public void addValidation(String field, String[] values) {
        DataValidationHelper helper = new DataValidationHelper() {
            @Override
            public DataValidationConstraint createExplicitListConstraint(String[] listOfValues) {
                return new DataValidationConstraint() {
                    @Override
                    public int getValidationType() {
                        return ValidationType.LIST;
                    }

                    @Override
                    public String[] getExplicitListValues() {
                        return listOfValues;
                    }

                    @Override
                    public void setExplicitListValues(String[] values) {
                    }

                    @Override
                    public String getFormula1() {
                        return null;
                    }

                    @Override
                    public void setFormula1(String formula) {
                    }

                    @Override
                    public String getFormula2() {
                        return null;
                    }

                    @Override
                    public void setFormula2(String formula) {
                    }

                    @Override
                    public int getOperator() {
                        return 0;
                    }

                    @Override
                    public void setOperator(int operator) {
                    }
                };
            }

            @Override
            public DataValidationConstraint createCustomConstraint(String formula) {
                return null;
            }

            @Override
            public DataValidationConstraint createDateConstraint(int operatorType, String formula1, String formula2, String dateFormat) {
                return null;
            }

            @Override
            public DataValidationConstraint createDecimalConstraint(int operatorType, String formula1, String formula2) {
                return null;
            }

            @Override
            public DataValidationConstraint createFormulaListConstraint(String listFormula) {
                return null;
            }

            @Override
            public DataValidationConstraint createIntegerConstraint(int operatorType, String formula1, String formula2) {
                return null;
            }

            @Override
            public DataValidationConstraint createNumericConstraint(int validationType, int operatorType, String formula1, String formula2) {
                return null;
            }

            @Override
            public DataValidationConstraint createTextLengthConstraint(int operatorType, String formula1, String formula2) {
                return null;
            }

            @Override
            public DataValidationConstraint createTimeConstraint(int operatorType, String formula1, String formula2) {
                return null;
            }

            @Override
            public DataValidation createValidation(DataValidationConstraint constraint, CellRangeAddressList cellRangeAddressList) {
                return new DataValidation() {
                    @Override
                    public DataValidationConstraint getValidationConstraint() {
                        return constraint;
                    }

                    @Override
                    public CellRangeAddressList getRegions() {
                        return cellRangeAddressList;
                    }

                    @Override
                    public void setErrorStyle(int errorStyle) {
                    }

                    @Override
                    public int getErrorStyle() {
                        return 0;
                    }

                    @Override
                    public void setEmptyCellAllowed(boolean allowed) {
                    }

                    @Override
                    public boolean getEmptyCellAllowed() {
                        return false;
                    }

                    @Override
                    public void setSuppressDropDownArrow(boolean suppress) {
                    }

                    @Override
                    public boolean getSuppressDropDownArrow() {
                        return false;
                    }

                    @Override
                    public void setShowPromptBox(boolean show) {
                    }

                    @Override
                    public boolean getShowPromptBox() {
                        return false;
                    }

                    @Override
                    public void setShowErrorBox(boolean show) {
                    }

                    @Override
                    public boolean getShowErrorBox() {
                        return false;
                    }

                    @Override
                    public void createPromptBox(String title, String text) {
                    }

                    @Override
                    public String getPromptBoxTitle() {
                        return null;
                    }

                    @Override
                    public String getPromptBoxText() {
                        return null;
                    }

                    @Override
                    public void createErrorBox(String title, String text) {
                    }

                    @Override
                    public String getErrorBoxTitle() {
                        return null;
                    }

                    @Override
                    public String getErrorBoxText() {
                        return null;
                    }
                };
            }
        };

        DataValidationConstraint constraint = helper.createExplicitListConstraint(values);
        CellRangeAddressList regions = new CellRangeAddressList(1, 1000, converts.size(), converts.size());
        DataValidation validation = helper.createValidation(constraint, regions);
        validations.put(field, validation);
    }

    /**
     * 添加列宽配置
     */
    public void addColumnWidth(int columnIndex, int width) {
        columnWidths.put(columnIndex, width);
    }

    /**
     * 设置列宽
     */
    private void setColumnWidth(Sheet sheet) {
        columnWidths.forEach((columnIndex, width) -> {
            sheet.setColumnWidth(columnIndex, width * 256);
        });
    }

    /**
     * 创建样式
     */
    private void createStyles(Workbook workbook) {
        // 标题样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 12);
        titleStyle.setFont(titleFont);
        styles.put("title", titleStyle);

        // 内容样式
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.put("content", contentStyle);

        // 数字样式
        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        styles.put("number", numberStyle);

        // 日期样式
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setAlignment(HorizontalAlignment.CENTER);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));
        styles.put("date", dateStyle);
    }

    /**
     * 创建数据校验
     */
    private void createValidations(Sheet sheet) {
        validations.forEach((field, validation) -> {
            sheet.addValidationData(validation);
        });
    }

} 