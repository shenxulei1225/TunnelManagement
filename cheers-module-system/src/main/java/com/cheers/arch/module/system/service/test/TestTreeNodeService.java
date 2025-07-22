package com.cheers.arch.module.system.service.test;

import java.util.List;

import jakarta.validation.Valid;

import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeListReqVO;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.test.TestTreeNodeDO;

/**
 * 测试树节点 Service 接口
 *
 * @author 芋道源码
 */
public interface TestTreeNodeService {

    /**
     * 创建测试树节点
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTestTreeNode(@Valid TestTreeNodeSaveReqVO createReqVO);

    /**
     * 更新测试树节点
     *
     * @param updateReqVO 更新信息
     */
    void updateTestTreeNode(@Valid TestTreeNodeSaveReqVO updateReqVO);

    /**
     * 删除测试树节点
     *
     * @param id 编号
     */
    void deleteTestTreeNode(Long id);

    /**
     * 获得测试树节点
     *
     * @param id 编号
     * @return 测试树节点
     */
    TestTreeNodeDO getTestTreeNode(Long id);

    /**
     * 获得测试树节点列表
     *
     * @param listReqVO 查询条件
     * @return 测试树节点列表
     */
    List<TestTreeNodeDO> getTestTreeNodeList(TestTreeNodeListReqVO listReqVO);

    /**
     * 获得指定业务类型的测试树节点列表
     *
     * @param businessType 业务类型
     * @return 测试树节点列表
     */
    List<TestTreeNodeDO> getTestTreeNodeListByBusinessType(String businessType);

    /**
     * 移动测试树节点
     *
     * @param id 节点编号
     * @param parentId 新的父节点编号
     * @param sort 新的排序
     */
    void moveTestTreeNode(Long id, Long parentId, Integer sort);

    /**
     * 批量更新节点排序
     *
     * @param nodes 节点列表（包含id和sort字段）
     */
    void batchUpdateSort(List<TestTreeNodeDO> nodes);

} 