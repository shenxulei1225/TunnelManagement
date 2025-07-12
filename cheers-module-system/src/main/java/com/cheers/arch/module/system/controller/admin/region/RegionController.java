package com.cheers.arch.module.system.controller.admin.region;

import com.cheers.arch.framework.apilog.core.annotation.ApiAccessLog;
import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.excel.core.util.ExcelUtils;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionListReqVO;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionRespVO;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;
import com.cheers.arch.module.system.service.region.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.cheers.arch.module.system.enums.RegionType;

import static com.cheers.arch.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Slf4j
@Tag(name = "管理后台 - 区域")
@RestController
@RequestMapping("/system/region")
@Validated
@ConditionalOnProperty(prefix = "system.region.controller", name = "enabled", havingValue = "true", matchIfMissing = false)
public class RegionController {

    @Resource
    private RegionService regionService;

    @PostMapping("/create")
    @Operation(summary = "创建区域")
    @PreAuthorize("@ss.hasPermission('system:region:create')")
    public CommonResult<Long> createRegion(@Valid @RequestBody RegionSaveReqVO createReqVO) {
        log.info("[createRegion][开始创建] 请求参数: {}", createReqVO);
        try {
            Long id = regionService.createRegion(createReqVO);
            log.info("[createRegion][创建成功] 结果: {}", id);
            return success(id);
        } catch (Exception e) {
            log.error("[createRegion][创建异常]", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @Operation(summary = "更新区域")
    @PreAuthorize("@ss.hasPermission('system:region:update')")
    public CommonResult<Boolean> updateRegion(@Valid @RequestBody RegionSaveReqVO updateReqVO) {
        log.info("[updateRegion][开始更新] 请求参数: {}", updateReqVO);
        try {
            regionService.updateRegion(updateReqVO);
            log.info("[updateRegion][更新成功]");
            return success(true);
        } catch (Exception e) {
            log.error("[updateRegion][更新异常]", e);
            throw e;
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除区域")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:region:delete')")
    public CommonResult<Boolean> deleteRegion(@RequestParam("id") Long id) {
        log.info("[deleteRegion][开始删除] 请求参数: {}", id);
        try {
            regionService.deleteRegion(id);
            log.info("[deleteRegion][删除成功]");
            return success(true);
        } catch (Exception e) {
            log.error("[deleteRegion][删除异常]", e);
            throw e;
        }
    }

    @GetMapping("/get")
    @Operation(summary = "获得区域")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<RegionRespVO> getRegion(@RequestParam("id") Long id) {
        log.info("[getRegion][开始查询] 请求参数: {}", id);
        try {
            RegionDO region = regionService.getRegion(id);
            log.info("[getRegion][查询成功] 结果: {}", region);
            RegionRespVO vo = BeanUtils.toBean(region, RegionRespVO.class);
        // 手动拷贝 Map，BeanUtils 对泛型 Map 可能不生效
        vo.setExtraAttrs(region.getExtraAttrs());
        return success(vo);
        } catch (Exception e) {
            log.error("[getRegion][查询异常]", e);
            throw e;
        }
    }

    @GetMapping("/page")
    @Operation(summary = "获得区域分页")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<PageResult<RegionRespVO>> getRegionPage(@Valid RegionListReqVO pageReqVO) {
        log.info("[getRegionPage][开始分页查询] 请求参数: {}", pageReqVO);
        try {
            PageResult<RegionDO> pageResult = regionService.getRegionPage(pageReqVO);
            log.info("[getRegionPage][分页查询成功] 总条数: {}", pageResult.getTotal());
            return success(BeanUtils.toBean(pageResult, RegionRespVO.class));
        } catch (Exception e) {
            log.error("[getRegionPage][分页查询异常]", e);
            throw e;
        }
    }

    @GetMapping("/list")
    @Operation(summary = "获得区域列表")
    @PreAuthorize("@ss.hasPermission('system:region:query')")
    public CommonResult<List<RegionRespVO>> getRegionList(@Valid RegionListReqVO listReqVO) {
        log.info("[getRegionList][开始查询] 请求参数: {}", listReqVO);
        try {
            List<RegionDO> list = regionService.getRegionList(listReqVO);
            log.info("[getRegionList][查询成功] 结果数量: {}", list.size());
            if (log.isDebugEnabled()) {
                log.debug("[getRegionList][详细数据]: {}", list);
            }
            return success(BeanUtils.toBean(list, RegionRespVO.class));
        } catch (Exception e) {
            log.error("[getRegionList][查询异常]", e);
            throw e;
        }
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

    // 新增：区域类型列表接口
    @GetMapping("/type-list")
    @Operation(summary = "获得区域类型列表")
    public CommonResult<List<RegionTypeInfo>> getRegionTypeList() {
        List<RegionTypeInfo> typeList = Arrays.stream(RegionType.values())
                .map(type -> new RegionTypeInfo(type.getId(), type.getDescription()))
                .collect(Collectors.toList());
        return success(typeList);
    }

    // 区域类型 DTO
    public static class RegionTypeInfo {
        private int id;
        private String description;
        public RegionTypeInfo(int id, String description) {
            this.id = id;
            this.description = description;
        }
        public int getId() { return id; }
        public String getDescription() { return description; }
    }
}