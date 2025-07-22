package com.cheers.arch.module.system.dal.mysql.test;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.controller.admin.test.vo.TestTreeNodeListReqVO;
import com.cheers.arch.module.system.dal.dataobject.test.TestTreeNodeDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 测试树节点 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface TestTreeNodeMapper extends BaseMapperX<TestTreeNodeDO> {

    default List<TestTreeNodeDO> selectList(TestTreeNodeListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<TestTreeNodeDO>()
                .likeIfPresent(TestTreeNodeDO::getName, reqVO.getName())
                .eqIfPresent(TestTreeNodeDO::getType, reqVO.getType())
                .eqIfPresent(TestTreeNodeDO::getBusinessType, reqVO.getBusinessType())
                .eqIfPresent(TestTreeNodeDO::getStatus, reqVO.getStatus())
                .orderByAsc(TestTreeNodeDO::getSort)
        );
    }

    default List<TestTreeNodeDO> selectListByBusinessType(String businessType) {
        return selectList(new LambdaQueryWrapperX<TestTreeNodeDO>()
                .eq(TestTreeNodeDO::getBusinessType, businessType)
                .eq(TestTreeNodeDO::getStatus, 0) // 只返回启用状态的节点
                .orderByAsc(TestTreeNodeDO::getSort)
        );
    }

    default List<TestTreeNodeDO> selectChildren(Long parentId) {
        return selectList(new LambdaQueryWrapperX<TestTreeNodeDO>()
                .eq(TestTreeNodeDO::getParentId, parentId)
                .eq(TestTreeNodeDO::getStatus, 0)
                .orderByAsc(TestTreeNodeDO::getSort)
        );
    }

} 