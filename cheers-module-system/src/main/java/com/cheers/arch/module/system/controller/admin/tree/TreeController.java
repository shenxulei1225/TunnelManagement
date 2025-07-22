package com.cheers.arch.module.system.controller.admin.tree;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeDataRelationCreateReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeDataRelationUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeDataRelationDO;
import com.cheers.arch.module.system.service.tree.TreeDataRelationService;

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

@Tag(name = "管理后台 - 通用树管理")
@RestController
@RequestMapping("/system/tree")
@Validated
public class TreeController {

    @Resource
    private TreeDataRelationService treeDataRelationService;

    @PostMapping("/data")
    @Operation(summary = "创建树数据关联")
    @PreAuthorize("@ss.hasPermission('system:tree:create')")
    public CommonResult<Long> createTreeDataRelation(@Valid @RequestBody TreeDataRelationCreateReqVO createReqVO) {
        Long id = treeDataRelationService.createTreeDataRelation(createReqVO);
        return success(id);
    }

    @PutMapping("/data")
    @Operation(summary = "更新树数据关联")
    @PreAuthorize("@ss.hasPermission('system:tree:update')")
    public CommonResult<Boolean> updateTreeDataRelation(@Valid @RequestBody TreeDataRelationUpdateReqVO updateReqVO) {
        treeDataRelationService.updateTreeDataRelation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/data")
    @Operation(summary = "删除树数据关联")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:tree:delete')")
    public CommonResult<Boolean> deleteTreeDataRelation(@RequestParam("id") Long id) {
        treeDataRelationService.deleteTreeDataRelation(id);
        return success(true);
    }

    @GetMapping("/data")
    @Operation(summary = "获得树数据关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<TreeDataRelationDO> getTreeDataRelation(@RequestParam("id") Long id) {
        TreeDataRelationDO treeDataRelation = treeDataRelationService.getTreeDataRelation(id);
        return success(treeDataRelation);
    }

    @GetMapping("/data/list")
    @Operation(summary = "获得树数据关联列表")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @Parameter(name = "treeNodeId", description = "树节点ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<List<TreeDataRelationDO>> getTreeDataRelationList(
            @RequestParam("treeType") String treeType,
            @RequestParam("treeNodeId") Long treeNodeId) {
        List<TreeDataRelationDO> list = treeDataRelationService.getTreeDataRelationList(treeType, treeNodeId);
        return success(list);
    }

    @PostMapping("/data/add")
    @Operation(summary = "添加数据到树节点")
    @PreAuthorize("@ss.hasPermission('system:tree:create')")
    public CommonResult<Long> addDataToTreeNode(
            @RequestParam("treeType") String treeType,
            @RequestParam("treeNodeId") Long treeNodeId,
            @RequestParam("dataType") String dataType,
            @RequestParam("dataId") Long dataId,
            @RequestParam("dataName") String dataName,
            @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
            @RequestParam(value = "isRequired", required = false) Boolean isRequired,
            @RequestParam(value = "metadata", required = false) String metadata) {
        Long id = treeDataRelationService.addDataToTreeNode(treeType, treeNodeId, dataType, dataId, 
                dataName, displayOrder, isRequired, metadata);
        return success(id);
    }

    @DeleteMapping("/data/remove")
    @Operation(summary = "从树节点移除数据")
    @PreAuthorize("@ss.hasPermission('system:tree:delete')")
    public CommonResult<Boolean> removeDataFromTreeNode(
            @RequestParam("treeType") String treeType,
            @RequestParam("treeNodeId") Long treeNodeId,
            @RequestParam("dataType") String dataType,
            @RequestParam("dataId") Long dataId) {
        treeDataRelationService.removeDataFromTreeNode(treeType, treeNodeId, dataType, dataId);
        return success(true);
    }

    @PostMapping("/data/batch-add")
    @Operation(summary = "批量添加数据到树节点")
    @PreAuthorize("@ss.hasPermission('system:tree:create')")
    public CommonResult<Boolean> batchAddDataToTreeNode(
            @RequestParam("treeType") String treeType,
            @RequestParam("treeNodeId") Long treeNodeId,
            @Valid @RequestBody List<TreeDataRelationCreateReqVO> dataList) {
        treeDataRelationService.batchAddDataToTreeNode(treeType, treeNodeId, dataList);
        return success(true);
    }

    @GetMapping("/data/search")
    @Operation(summary = "搜索树节点中的数据")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<List<TreeDataRelationDO>> searchTreeNodeData(
            @RequestParam("treeType") String treeType,
            @RequestParam("treeNodeId") Long treeNodeId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "dataType", required = false) String dataType) {
        List<TreeDataRelationDO> list = treeDataRelationService.searchTreeNodeData(treeType, treeNodeId, keyword, dataType);
        return success(list);
    }

    @GetMapping("/data/stats")
    @Operation(summary = "获取树节点的数据统计")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<Map<String, Long>> getTreeNodeDataStats(
            @RequestParam("treeType") String treeType,
            @RequestParam("treeNodeId") Long treeNodeId) {
        Map<String, Long> stats = treeDataRelationService.getTreeNodeDataStats(treeType, treeNodeId);
        return success(stats);
    }

    @GetMapping("/data/distribution")
    @Operation(summary = "获取数据类型在树中的分布")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<List<TreeDataRelationDO>> getDataDistribution(
            @RequestParam("treeType") String treeType,
            @RequestParam("dataType") String dataType) {
        List<TreeDataRelationDO> list = treeDataRelationService.getDataDistribution(treeType, dataType);
        return success(list);
    }
} 