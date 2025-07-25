package com.cheers.arch.module.system.controller.admin.domain;

import com.cheers.arch.framework.apilog.core.annotation.ApiAccessLog;
import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageParam;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.excel.core.util.ExcelUtils;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainExcelVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldConfig;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldMoveReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldRelRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainMoveReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainPageReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainSaveReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainStatisticsRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainTreeRespVO;
import com.cheers.arch.module.system.convert.domain.DomainConvert;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainDO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainFieldRelDO;
import com.cheers.arch.module.system.service.domain.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.cheers.arch.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 领域模型")
@RestController
@RequestMapping("/system/domain")
@Validated
public class DomainController {

    @Resource
    private DomainService domainService;

    @PostMapping("/create")
    @Operation(summary = "新增领域模型")
    @PreAuthorize("@ss.hasPermission('system:domain:create')")
    public CommonResult<Long> createDomain(@Valid @RequestBody DomainSaveReqVO createReqVO) {
        return success(domainService.createDomain(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新领域模型")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> updateDomain(@Valid @RequestBody DomainSaveReqVO updateReqVO) {
        domainService.updateDomain(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除领域模型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:domain:delete')")
    public CommonResult<Boolean> deleteDomain(@RequestParam("id") Long id) {
        domainService.deleteDomain(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得领域模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<DomainRespVO> getDomain(@RequestParam("id") Long id) {
        DomainDO domain = domainService.getDomain(id);
        return success(DomainConvert.INSTANCE.convert(domain));
    }

    @GetMapping("/list")
    @Operation(summary = "获得领域模型列表")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainRespVO>> getDomainList(@Valid DomainPageReqVO reqVO) {
        List<DomainDO> list = domainService.getDomainList(reqVO);
        return success(DomainConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/list-with-statistics")
    @Operation(summary = "获得领域模型列表（带统计信息）")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainRespVO>> getDomainListWithStatistics(@Valid DomainPageReqVO reqVO) {
        List<DomainRespVO> list = domainService.getDomainListWithStatistics(reqVO);
        return success(list);
    }

    @GetMapping("/tree")
    @Operation(summary = "获得领域模型树形结构（Element Plus Tree格式，带统计信息）")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainTreeRespVO>> getDomainTree(@Valid DomainPageReqVO reqVO) {
        List<DomainTreeRespVO> tree = domainService.getDomainTree(reqVO);
        return success(tree);
    }

    @GetMapping("/page")
    @Operation(summary = "获得领域模型分页")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<PageResult<DomainRespVO>> getDomainPage(@Valid DomainPageReqVO pageReqVO) {
        PageResult<DomainDO> pageResult = domainService.getDomainPage(pageReqVO);
        return success(DomainConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出领域模型 Excel")
    @PreAuthorize("@ss.hasPermission('system:domain:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDomainExcel(@Valid DomainPageReqVO exportReqVO,
                                  HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DomainDO> list = domainService.getDomainList(exportReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "领域模型.xls", "数据", DomainExcelVO.class,
                DomainConvert.INSTANCE.convertList02(list));
    }

    @PostMapping("/field-rel/add")
    @Operation(summary = "添加领域字段关联")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> addDomainFieldRel(@RequestParam("domainId") Long domainId,
                                                    @RequestParam("fieldId") Long fieldId,
                                                    @RequestParam(value = "required", defaultValue = "false") Boolean required,
                                                    @RequestParam(value = "sort", defaultValue = "0") Integer sort,
                                                    @RequestParam(value = "remark", required = false) String remark) {
        domainService.addDomainFieldRel(domainId, fieldId, required, sort, remark);
        return success(true);
    }

    @DeleteMapping("/field-rel/remove")
    @Operation(summary = "删除领域字段关联")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> removeDomainFieldRel(@RequestParam("domainId") Long domainId,
                                                       @RequestParam("fieldId") Long fieldId) {
        domainService.removeDomainFieldRel(domainId, fieldId);
        return success(true);
    }

    @GetMapping("/field-rel/list")
    @Operation(summary = "获得领域字段关联列表")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainFieldRelDO>> getDomainFieldRelList(@RequestParam("domainId") Long domainId) {
        List<DomainFieldRelDO> list = domainService.getDomainFieldRelList(domainId);
        return success(list);
    }

    @GetMapping("/fields")
    @Operation(summary = "获得指定领域下的字段列表（带字段详情）")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainFieldRelRespVO>> getFieldsByDomainId(@RequestParam("domainId") Long domainId) {
        List<DomainFieldRelRespVO> list = domainService.getFieldsWithDetailsByDomainId(domainId);
        return success(list);
    }

    @GetMapping("/domains-by-field")
    @Operation(summary = "获得指定字段关联的领域列表（带领域详情）")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainFieldRelRespVO>> getDomainsByFieldId(@RequestParam("fieldId") Long fieldId) {
        List<DomainFieldRelRespVO> list = domainService.getDomainsWithDetailsByFieldId(fieldId);
        return success(list);
    }

    @PostMapping("/move")
    @Operation(summary = "拖拽移动领域")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> moveDomain(@Valid @RequestBody DomainMoveReqVO moveReqVO) {
        domainService.moveDomain(moveReqVO.getId(), moveReqVO.getTargetParentId(), moveReqVO.getTargetSort());
        return success(true);
    }

    @PostMapping("/batch-sort")
    @Operation(summary = "批量更新领域排序")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> batchUpdateSort(@RequestParam("parentId") Long parentId,
                                                  @RequestBody Map<Long, Integer> sorts) {
        domainService.updateDomainSorts(parentId, sorts);
        return success(true);
    }

        @PostMapping("/field-move")
    @Operation(summary = "移动字段到其他领域")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> moveFieldToDomain(@Valid @RequestBody DomainFieldMoveReqVO moveReqVO) {
        domainService.moveFieldToDomain(
            moveReqVO.getFieldId(),
            moveReqVO.getSourceDomainId(),
            moveReqVO.getTargetDomainId(),
            moveReqVO.getRequired(),
            moveReqVO.getSort(),
            moveReqVO.getRemark()
        );
        return success(true);
    }

    // ================ 统计功能接口 ================

    @GetMapping("/statistics")
    @Operation(summary = "获取领域统计信息")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<DomainStatisticsRespVO> getDomainStatistics(@RequestParam("domainId") Long domainId) {
        DomainStatisticsRespVO statistics = domainService.getDomainStatistics(domainId);
        return success(statistics);
    }

    @PostMapping("/statistics/batch")
    @Operation(summary = "批量获取领域统计信息")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainStatisticsRespVO>> getBatchDomainStatistics(@RequestBody List<Long> domainIds) {
        List<DomainStatisticsRespVO> statistics = domainService.getBatchDomainStatistics(domainIds);
        return success(statistics);
    }

    // ================ 批量处理接口 ================

    @PostMapping("/delete/batch")
    @Operation(summary = "批量删除领域")
    @PreAuthorize("@ss.hasPermission('system:domain:delete')")
    public CommonResult<Boolean> deleteDomainsBatch(@RequestBody List<Long> domainIds) {
        domainService.deleteDomainsBatch(domainIds);
        return success(true);
    }

    @PostMapping("/field-relations/remove/batch")
    @Operation(summary = "批量删除字段的所有领域关联关系")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> removeFieldDomainRelationsBatch(@RequestBody List<Long> fieldIds) {
        domainService.removeFieldDomainRelationsBatch(fieldIds);
        return success(true);
    }

    // ================ 对标Category的批量关联功能 ================

    @PostMapping("/field-relations/batch-create")
    @Operation(summary = "批量创建字段领域关联")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> batchCreateFieldDomainRels(
            @RequestParam("fieldId") Long fieldId,
            @RequestBody List<Long> domainIds) {
        domainService.batchCreateFieldDomainRels(fieldId, domainIds);
        return success(true);
    }

    @PostMapping("/field-relations/batch-create-with-config")
    @Operation(summary = "批量创建字段领域关联（带配置）")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> batchCreateFieldDomainRelsWithConfig(
            @RequestParam("fieldId") Long fieldId,
            @RequestBody Map<Long, DomainFieldConfig> domainConfigMap) {
        domainService.batchCreateFieldDomainRels(fieldId, domainConfigMap);
        return success(true);
    }

    @PostMapping("/field-relations/replace")
    @Operation(summary = "替换字段的所有领域关联")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> replaceFieldDomainRels(
            @RequestParam("fieldId") Long fieldId,
            @RequestBody List<Long> domainIds) {
        domainService.replaceFieldDomainRels(fieldId, domainIds);
        return success(true);
    }

    @PostMapping("/field-relations/replace-with-config")
    @Operation(summary = "替换字段的所有领域关联（带配置）")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> replaceFieldDomainRelsWithConfig(
            @RequestParam("fieldId") Long fieldId,
            @RequestBody Map<Long, DomainFieldConfig> domainConfigMap) {
        domainService.replaceFieldDomainRels(fieldId, domainConfigMap);
        return success(true);
    }

    @PostMapping("/field-config/batch-update")
    @Operation(summary = "批量更新字段在领域中的配置")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> batchUpdateFieldConfigInDomain(
            @RequestParam("domainId") Long domainId,
            @RequestBody Map<Long, DomainFieldConfig> fieldConfigMap) {
        domainService.batchUpdateFieldConfigInDomain(domainId, fieldConfigMap);
        return success(true);
    }

    @GetMapping("/field-relations/exists")
    @Operation(summary = "检查字段领域关联是否存在")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<Boolean> existsFieldDomainRel(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam("domainId") Long domainId) {
        boolean exists = domainService.existsFieldDomainRel(fieldId, domainId);
        return success(exists);
    }

    @GetMapping("/field-config/map")
    @Operation(summary = "获取字段在领域中的配置映射")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<Map<Long, DomainFieldConfig>> getFieldConfigMapInDomain(
            @RequestParam("domainId") Long domainId) {
        Map<Long, DomainFieldConfig> configMap = domainService.getFieldConfigMapInDomain(domainId);
        return success(configMap);
    }

    // ================ 对标Category的高级查询功能 ================

    @GetMapping("/by-tree-path")
    @Operation(summary = "根据树路径获取领域及其所有子领域")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainRespVO>> getDomainsByTreePath(@RequestParam("treePath") String treePath) {
        List<DomainDO> list = domainService.getDomainsByTreePath(treePath);
        return success(DomainConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/by-level")
    @Operation(summary = "根据层级获取领域列表")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainRespVO>> getDomainsByLevel(@RequestParam("level") Integer level) {
        List<DomainDO> list = domainService.getDomainsByLevel(level);
        return success(DomainConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/readonly")
    @Operation(summary = "获取只读领域列表")
    @PreAuthorize("@ss.hasPermission('system:domain:query')")
    public CommonResult<List<DomainRespVO>> getReadonlyDomains() {
        List<DomainDO> list = domainService.getReadonlyDomains();
        return success(DomainConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/tree-info/update")
    @Operation(summary = "更新领域的树形路径信息")
    @PreAuthorize("@ss.hasPermission('system:domain:update')")
    public CommonResult<Boolean> updateDomainTreeInfo(@RequestParam("domainId") Long domainId) {
        domainService.updateDomainTreeInfo(domainId);
        return success(true);
    }

} 