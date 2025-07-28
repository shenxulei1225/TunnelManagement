package com.cheers.arch.module.system.controller.admin.hierarchy;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupCreateReqVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupPageReqVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupTreeVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupDO;
import com.cheers.arch.module.system.service.hierarchy.HierarchyGroupService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "管理后台 - 系统分级组")
@RestController
@RequestMapping("/system/hierarchy-group")
@Validated
public class HierarchyGroupController {

    @Resource
    private HierarchyGroupService hierarchyGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建系统分级组")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:create')")
    public CommonResult<Long> createHierarchyGroup(@Valid @RequestBody HierarchyGroupCreateReqVO createReqVO) {
        return success(hierarchyGroupService.createHierarchyGroup(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新系统分级组")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:update')")
    public CommonResult<Boolean> updateHierarchyGroup(@Valid @RequestBody HierarchyGroupUpdateReqVO updateReqVO) {
        hierarchyGroupService.updateHierarchyGroup(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除系统分级组")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:delete')")
    public CommonResult<Boolean> deleteHierarchyGroup(@RequestParam("id") Long id) {
        hierarchyGroupService.deleteHierarchyGroup(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得分组")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<HierarchyGroupDO> getHierarchyGroup(@RequestParam("id") Long id) {
        HierarchyGroupDO hierarchyGroup = hierarchyGroupService.getHierarchyGroup(id);
        return success(hierarchyGroup);
    }

    @GetMapping("/page")
    @Operation(summary = "获得系统分组分页")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<PageResult<HierarchyGroupDO>> getHierarchyGroupPage(@Valid HierarchyGroupPageReqVO pageVO) {
        PageResult<HierarchyGroupDO> pageResult = hierarchyGroupService.getHierarchyGroupPage(pageVO);
        return success(pageResult);
    }

    @GetMapping("/tree")
    @Operation(summary = "获得系统分组树形结构")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<HierarchyGroupTreeVO> getHierarchyGroupTree() {
        HierarchyGroupTreeVO tree = hierarchyGroupService.getHierarchyGroupTree();
        return success(tree);
    }

    @GetMapping("/list-by-parent")
    @Operation(summary = "根据父ID获得分组列表")
    @Parameter(name = "parentId", description = "父ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<List<HierarchyGroupDO>> getHierarchyGroupListByParentId(@RequestParam("parentId") Long parentId) {
        List<HierarchyGroupDO> list = hierarchyGroupService.getHierarchyGroupListByParentId(parentId);
        return success(list);
    }

    @GetMapping("/get-by-code")
    @Operation(summary = "根据编码获得分组")
    @Parameter(name = "code", description = "编码", required = true, example = "DEVICE_HIERARCHY")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<HierarchyGroupDO> getHierarchyGroupByCode(@RequestParam("code") String code) {
        HierarchyGroupDO hierarchyGroup = hierarchyGroupService.getHierarchyGroupByCode(code);
        return success(hierarchyGroup);
    }

    @GetMapping("/list-by-usage-type")
    @Operation(summary = "根据用途类型获得分组列表")
    @Parameter(name = "usageType", description = "用途类型", required = true, example = "FIELD")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<List<HierarchyGroupDO>> getHierarchyGroupListByUsageType(@RequestParam("usageType") String usageType) {
        List<HierarchyGroupDO> list = hierarchyGroupService.getHierarchyGroupListByUsageType(usageType);
        return success(list);
    }

    @GetMapping("/list-by-usage-and-group-type")
    @Operation(summary = "根据用途类型和分组类型获得分组列表")
    @Parameter(name = "usageType", description = "用途类型", required = true, example = "BUSINESS")
    @Parameter(name = "groupType", description = "分组类型", required = true, example = "EQUIPMENT")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<List<HierarchyGroupDO>> getHierarchyGroupListByUsageTypeAndGroupType(
            @RequestParam("usageType") String usageType,
            @RequestParam("groupType") String groupType) {
        List<HierarchyGroupDO> list = hierarchyGroupService.getHierarchyGroupListByUsageTypeAndGroupType(usageType, groupType);
        return success(list);
    }

    @GetMapping("/tree-by-usage-type")
    @Operation(summary = "根据用途类型获得分组树形结构")
    @Parameter(name = "usageType", description = "用途类型", required = true, example = "FIELD")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<HierarchyGroupTreeVO> getHierarchyGroupTreeByUsageType(@RequestParam("usageType") String usageType) {
        HierarchyGroupTreeVO tree = hierarchyGroupService.getHierarchyGroupTreeByUsageType(usageType);
        return success(tree);
    }

    @PutMapping("/move")
    @Operation(summary = "移动分组（拖拽排序）")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:update')")
    public CommonResult<Boolean> moveHierarchyGroup(
            @Parameter(description = "分组ID", required = true) @RequestParam("hierarchyId") Long hierarchyId,
            @Parameter(description = "目标父分组ID", required = true) @RequestParam("targetParentId") Long targetParentId,
            @Parameter(description = "目标排序", required = true) @RequestParam("targetSort") Integer targetSort) {
        hierarchyGroupService.moveHierarchyGroup(hierarchyId, targetParentId, targetSort);
        return success(true);
    }

    @PutMapping("/sort")
    @Operation(summary = "批量更新分组排序")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:update')")
    public CommonResult<Boolean> sortHierarchyGroups(@RequestBody List<Map<String, Object>> sortList) {
        hierarchyGroupService.sortHierarchyGroups(sortList);
        return success(true);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索分组")
    @PreAuthorize("@ss.hasPermission('system:hierarchy-group:query')")
    public CommonResult<List<HierarchyGroupDO>> searchHierarchyGroups(
            @Parameter(description = "搜索关键词", required = true) @RequestParam("keyword") String keyword,
            @Parameter(description = "用途类型", required = false) @RequestParam(value = "usageType", required = false) String usageType) {
        List<HierarchyGroupDO> result = hierarchyGroupService.searchHierarchyGroups(keyword, usageType);
        return success(result);
    }

} 