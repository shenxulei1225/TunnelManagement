package com.cheers.arch.module.system.service.test.impl;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.TEST_TREE_NODE_NOT_EXISTS;

import java.util.List;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeListReqVO;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.test.TestTreeNodeDO;
import com.cheers.arch.module.system.dal.mysql.test.TestTreeNodeMapper;
import com.cheers.arch.module.system.service.test.TestTreeNodeService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 测试树节点 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TestTreeNodeServiceImpl implements TestTreeNodeService {

    @Resource
    private TestTreeNodeMapper testTreeNodeMapper;

    @Override
    public Long createTestTreeNode(TestTreeNodeSaveReqVO createReqVO) {
        // 插入
        TestTreeNodeDO testTreeNode = BeanUtils.toBean(createReqVO, TestTreeNodeDO.class);
        testTreeNodeMapper.insert(testTreeNode);
        // 返回
        return testTreeNode.getId();
    }

    @Override
    public void updateTestTreeNode(TestTreeNodeSaveReqVO updateReqVO) {
        // 校验存在
        validateTestTreeNodeExists(updateReqVO.getId());
        // 更新
        TestTreeNodeDO updateObj = BeanUtils.toBean(updateReqVO, TestTreeNodeDO.class);
        testTreeNodeMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTestTreeNode(Long id) {
        // 校验存在
        validateTestTreeNodeExists(id);
        
        // 删除子节点
        List<TestTreeNodeDO> children = testTreeNodeMapper.selectChildren(id);
        for (TestTreeNodeDO child : children) {
            deleteTestTreeNode(child.getId());
        }
        
        // 删除当前节点
        testTreeNodeMapper.deleteById(id);
    }

    private void validateTestTreeNodeExists(Long id) {
        if (testTreeNodeMapper.selectById(id) == null) {
            throw exception(TEST_TREE_NODE_NOT_EXISTS);
        }
    }

    @Override
    public TestTreeNodeDO getTestTreeNode(Long id) {
        return testTreeNodeMapper.selectById(id);
    }

    @Override
    public List<TestTreeNodeDO> getTestTreeNodeList(TestTreeNodeListReqVO listReqVO) {
        return testTreeNodeMapper.selectList(listReqVO);
    }

    @Override
    public List<TestTreeNodeDO> getTestTreeNodeListByBusinessType(String businessType) {
        return testTreeNodeMapper.selectListByBusinessType(businessType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveTestTreeNode(Long id, Long parentId, Integer sort) {
        // 校验存在
        validateTestTreeNodeExists(id);
        
        // 更新父节点和排序
        TestTreeNodeDO updateObj = new TestTreeNodeDO();
        updateObj.setId(id);
        updateObj.setParentId(parentId);
        updateObj.setSort(sort);
        testTreeNodeMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateSort(List<TestTreeNodeDO> nodes) {
        for (TestTreeNodeDO node : nodes) {
            if (node.getId() != null && node.getSort() != null) {
                TestTreeNodeDO updateObj = new TestTreeNodeDO();
                updateObj.setId(node.getId());
                updateObj.setSort(node.getSort());
                testTreeNodeMapper.updateById(updateObj);
            }
        }
    }

} 