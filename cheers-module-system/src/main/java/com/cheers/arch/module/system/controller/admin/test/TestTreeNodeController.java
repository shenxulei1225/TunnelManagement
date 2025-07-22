package com.cheers.arch.module.system.controller.admin.test;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeListReqVO;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeRespVO;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.test.TestTreeNodeDO;
import com.cheers.arch.module.system.service.test.TestTreeNodeService;

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

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "管理后台 - 测试树节点")
@RestController
@RequestMapping("/system/test-tree-node")
@Validated
@Slf4j
public class TestTreeNodeController {

    @Resource
    private TestTreeNodeService testTreeNodeService;

    @PostMapping("/create")
    @Operation(summary = "创建测试树节点")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:create')")
    public CommonResult<Long> createTestTreeNode(@Valid @RequestBody TestTreeNodeSaveReqVO createReqVO) {
        return success(testTreeNodeService.createTestTreeNode(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新测试树节点")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:update')")
    public CommonResult<Boolean> updateTestTreeNode(@Valid @RequestBody TestTreeNodeSaveReqVO updateReqVO) {
        testTreeNodeService.updateTestTreeNode(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除测试树节点")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:delete')")
    public CommonResult<Boolean> deleteTestTreeNode(@RequestParam("id") Long id) {
        testTreeNodeService.deleteTestTreeNode(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得测试树节点")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:query')")
    public CommonResult<TestTreeNodeRespVO> getTestTreeNode(@RequestParam("id") Long id) {
        TestTreeNodeDO testTreeNode = testTreeNodeService.getTestTreeNode(id);
        return success(BeanUtils.toBean(testTreeNode, TestTreeNodeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得测试树节点列表")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:query')")
    public CommonResult<List<TestTreeNodeRespVO>> getTestTreeNodeList(@Valid TestTreeNodeListReqVO listReqVO) {
        List<TestTreeNodeDO> list = testTreeNodeService.getTestTreeNodeList(listReqVO);
        return success(BeanUtils.toBean(list, TestTreeNodeRespVO.class));
    }

    @GetMapping("/list-by-business-type")
    @Operation(summary = "获得指定业务类型的测试树节点列表")
    @Parameter(name = "businessType", description = "业务类型", required = true, example = "dept")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:query')")
    public CommonResult<List<TestTreeNodeRespVO>> getTestTreeNodeListByBusinessType(@RequestParam("businessType") String businessType) {
        List<TestTreeNodeDO> list = testTreeNodeService.getTestTreeNodeListByBusinessType(businessType);
        return success(buildTree(BeanUtils.toBean(list, TestTreeNodeRespVO.class)));
    }

    @PutMapping("/move")
    @Operation(summary = "移动测试树节点")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:update')")
    public CommonResult<Boolean> moveTestTreeNode(@RequestParam("id") Long id,
                                                  @RequestParam("parentId") Long parentId,
                                                  @RequestParam("sort") Integer sort) {
        testTreeNodeService.moveTestTreeNode(id, parentId, sort);
        return success(true);
    }

    @PutMapping("/batch-update-sort")
    @Operation(summary = "批量更新节点排序")
    @PreAuthorize("@ss.hasPermission('test:enhanced-tree:update')")
    public CommonResult<Boolean> batchUpdateSort(@RequestBody List<TestTreeNodeDO> nodes) {
        testTreeNodeService.batchUpdateSort(nodes);
        return success(true);
    }

    /**
     * 构建树形结构
     * 参考 AuthConvert.buildMenuTree 的实现模式
     */
    private List<TestTreeNodeRespVO> buildTree(List<TestTreeNodeRespVO> nodes) {
        if (CollUtil.isEmpty(nodes)) {
            return Collections.emptyList();
        }
        
        // 排序，保证节点的有序性
        nodes.sort(Comparator.comparing(TestTreeNodeRespVO::getSort, Comparator.nullsLast(Comparator.naturalOrder())));
        
        // 构建节点映射表
        Map<Long, TestTreeNodeRespVO> treeNodeMap = new LinkedHashMap<>();
        nodes.forEach(node -> {
            node.setChildren(new ArrayList<>());  // 初始化children为可变的ArrayList
            treeNodeMap.put(node.getId(), node);
        });
        
        // 处理父子关系
        treeNodeMap.values().stream()
                .filter(node -> node.getParentId() != null && !node.getParentId().equals(0L))
                .forEach(childNode -> {
                    // 获得父节点
                    TestTreeNodeRespVO parentNode = treeNodeMap.get(childNode.getParentId());
                    if (parentNode == null) {
                        log.error("[buildTree][节点({}) 找不到父节点({})]", 
                                childNode.getId(), childNode.getParentId());
                        return;
                    }
                    // 将自己添加到父节点中
                    parentNode.getChildren().add(childNode);
                });
        
        // 返回所有根节点
        return treeNodeMap.values().stream()
                .filter(node -> node.getParentId() == null || node.getParentId().equals(0L))
                .collect(Collectors.toList());
    }

} 