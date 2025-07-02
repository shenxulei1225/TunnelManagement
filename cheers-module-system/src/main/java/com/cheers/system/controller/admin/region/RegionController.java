package com.cheers.system.controller.admin.region;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.cheers.system.controller.admin.region.vo.*;
import com.cheers.system.dal.dataobject.region.RegionDO;
import com.cheers.system.service.region.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 区域")
@RestController
@RequestMapping("/system/region")
@Validated
@Component("cheersRegionController")
public class RegionController {

    @Resource
    private RegionService regionService;

    @PostMapping("/create")
    @Operation(summary = "创建区域")
    @PreAuthorize("@ss.hasPermission('system:region:create')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<Long> createRegion(@Valid @RequestBody RegionSaveReqVO createReqVO) {
        return success(regionService.createRegion(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新区域")
    @PreAuthorize("@ss.hasPermission('system:region:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> updateRegion(@Valid @RequestBody RegionSaveReqVO updateReqVO) {
        regionService.updateRegion(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除区域")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:region:delete')")
    @ApiAccessLog(operateType = DELETE)
    public CommonResult<Boolean> deleteRegion(@RequestParam("id") Long id) {
        regionService.deleteRegion(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得区域")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<RegionRespVO> getRegion(@RequestParam("id") Long id) {
        RegionDO region = regionService.getRegion(id);
        return success(BeanUtils.toBean(region, RegionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得区域分页")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<PageResult<RegionRespVO>> getRegionPage(@Valid RegionPageReqVO pageReqVO) {
        PageResult<RegionDO> pageResult = regionService.getRegionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RegionRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得区域列表")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<List<RegionRespVO>> getRegionList(@Valid RegionListReqVO listReqVO) {
        List<RegionDO> list = regionService.getRegionList(listReqVO);
        return success(BeanUtils.toBean(list, RegionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出区域 Excel")
    @PreAuthorize("@ss.hasPermission('system:region:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRegionExcel(@Valid RegionListReqVO listReqVO,
                                  HttpServletResponse response) throws IOException {
        List<RegionDO> list = regionService.getRegionList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "区域.xls", "数据", RegionRespVO.class,
                BeanUtils.toBean(list, RegionRespVO.class));
    }

    // ==================== 批量导入相关接口 ====================
    
    @GetMapping("/get-import-template")
    @Operation(summary = "获取批量导入模板")
    @PreAuthorize("@ss.hasPermission('system:region:create')")
    public void getImportTemplate(@RequestParam("categoryId") Long categoryId,
                                HttpServletResponse response) throws IOException {
        regionService.downloadImportTemplate(categoryId, response);
    }
    
    @PostMapping("/import-excel")
    @Operation(summary = "批量导入区域数据")
    @PreAuthorize("@ss.hasPermission('system:region:create')")
    @ApiAccessLog(operateType = CREATE)
    public CommonResult<RegionImportResultVO> importRegion(@RequestParam("categoryId") Long categoryId,
                                                         @RequestParam("file") MultipartFile file) throws IOException {
        RegionImportResultVO result = regionService.importRegions(categoryId, file);
        return success(result);
    }
    
    @GetMapping("/get-import-result/{batchId}")
    @Operation(summary = "查询导入结果")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<RegionImportResultVO> getImportResult(@PathVariable("batchId") String batchId) {
        RegionImportResultVO result = regionService.getImportResult(batchId);
        return success(result);
    }

}