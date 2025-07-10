package com.cheers.framework.excel.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * Excel 工具类
 */
public class ExcelUtils {

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response 响应
     * @param filename 文件名
     * @param sheetName sheet名
     * @param head Excel头
     * @param data 数据列表
     * @param <T> 数据类型
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                               Class<T> head, List<T> data) throws IOException {
        // 设置响应信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFilename + ".xlsx");
        // 导出 Excel
        write(response.getOutputStream(), sheetName, head, data);
    }

    /**
     * 写入Excel到输出流
     * 使用最大宽度自适应策略
     */
    public static <T> void write(ServletOutputStream outputStream, String sheetName, Class<T> head, List<T> data) {
        EasyExcel.write(outputStream, head)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName)
                .doWrite(data);
    }

    /**
     * 写入Excel文件
     * 使用最大宽度自适应策略
     */
    public static <T> void write(String path, String sheetName, Class<T> head, Collection<T> data) {
        EasyExcel.write(path, head)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName)
                .doWrite(data);
    }

    /**
     * 读取Excel文件
     */
    @SneakyThrows
    public static <T> List<T> read(MultipartFile file, Class<T> head) {
        return EasyExcel.read(file.getInputStream(), head, null)
                .sheet()
                .doReadSync();
    }

} 