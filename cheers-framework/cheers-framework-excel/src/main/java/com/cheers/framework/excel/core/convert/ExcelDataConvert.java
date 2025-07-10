package com.cheers.framework.excel.core.convert;

/**
 * Excel数据转换接口
 */
public interface ExcelDataConvert {

    /**
     * 转换Excel数据为Java对象
     *
     * @param value Excel单元格的值
     * @return Java对象
     */
    Object convertToJavaData(Object value);

    /**
     * 转换Java对象为Excel数据
     *
     * @param value Java对象
     * @return Excel单元格的值
     */
    Object convertToExcelData(Object value);

} 