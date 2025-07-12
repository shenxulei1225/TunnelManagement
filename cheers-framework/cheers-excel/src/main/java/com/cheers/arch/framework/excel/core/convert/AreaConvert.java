package com.cheers.arch.framework.excel.core.convert;

import cn.hutool.core.convert.Convert;
// import com.cheers.arch.framework.ip.core.Area;
// import com.cheers.arch.framework.ip.core.utils.AreaUtils;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel 数据地区转换器
 * 
 * 注意：此转换器依赖IP模块，当前暂时禁用相关功能
 *
 * @author HUIHUI
 */
@Slf4j
public class AreaConvert implements Converter<Object> {

    @Override
    public Class<?> supportJavaTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public Object convertToJavaData(ReadCellData readCellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        // TODO: 当IP模块迁移完成后，恢复此功能
        log.warn("[convertToJavaData] 地区转换功能暂时禁用，需要先迁移IP模块");
        return null;
        
        // 原始代码（需要IP模块支持）：
        // // 解析地区编号
        // String label = readCellData.getStringValue();
        // Area area = AreaUtils.parseArea(label);
        // if (area == null) {
        //     log.error("[convertToJavaData][label({}) 解析不掉]", label);
        //     return null;
        // }
        // // 将 value 转换成对应的属性
        // Class<?> fieldClazz = contentProperty.getField().getType();
        // return Convert.convert(fieldClazz, area.getId());
    }

}
