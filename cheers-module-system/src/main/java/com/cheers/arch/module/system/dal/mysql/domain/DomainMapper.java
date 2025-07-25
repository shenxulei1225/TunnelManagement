package com.cheers.arch.module.system.dal.mysql.domain;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainPageReqVO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 领域模型 Mapper
 *
 * @author cheers
 */
@Mapper
public interface DomainMapper extends BaseMapperX<DomainDO> {

    default PageResult<DomainDO> selectPage(DomainPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DomainDO>()
                .likeIfPresent(DomainDO::getName, reqVO.getName())
                .likeIfPresent(DomainDO::getCode, reqVO.getCode())
                .eqIfPresent(DomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DomainDO::getType, reqVO.getType())
                .eqIfPresent(DomainDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DomainDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DomainDO::getId));
    }

    default List<DomainDO> selectList(DomainPageReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .likeIfPresent(DomainDO::getName, reqVO.getName())
                .likeIfPresent(DomainDO::getCode, reqVO.getCode())
                .eqIfPresent(DomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DomainDO::getType, reqVO.getType())
                .eqIfPresent(DomainDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DomainDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DomainDO::getId));
    }

    default DomainDO selectByCode(String code) {
        return selectOne(DomainDO::getCode, code);
    }

    default List<DomainDO> selectByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getParentId, parentId)
                .orderByAsc(DomainDO::getSort));
    }

    default DomainDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getParentId, parentId)
                .eq(DomainDO::getName, name));
    }

    /**
     * 统计指定领域的直接子领域数量
     */
    default Integer countChildrenByParentId(Long parentId) {
        return Math.toIntExact(selectCount(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getParentId, parentId)));
    }

    /**
     * 获取指定领域的所有后代领域ID（递归）
     */
    default List<Long> selectAllDescendantIds(Long parentId) {
        List<Long> result = new ArrayList<>();
        collectDescendantIds(parentId, result);
        return result;
    }

    /**
     * 递归收集所有后代领域ID
     */
    default void collectDescendantIds(Long parentId, List<Long> result) {
        List<DomainDO> children = selectByParentId(parentId);
        for (DomainDO child : children) {
            result.add(child.getId());
            collectDescendantIds(child.getId(), result);
        }
    }

    /**
     * 批量删除领域（根据ID列表）
     */
    default void deleteBatchByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapperX<DomainDO>()
                .in(DomainDO::getId, ids));
    }

    // ================ 对标Category的高级查询功能 ================

    /**
     * 根据树路径查询领域及其所有子领域
     */
    default List<DomainDO> selectByTreePath(String treePath) {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .likeRight(DomainDO::getTreePath, treePath)
                .orderByAsc(DomainDO::getLevel, DomainDO::getSort));
    }

    /**
     * 根据层级查询领域列表
     */
    default List<DomainDO> selectByLevel(Integer level) {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getLevel, level)
                .orderByAsc(DomainDO::getSort));
    }

    /**
     * 获取只读领域列表
     */
    default List<DomainDO> selectReadonlyDomains() {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getReadonly, 1)
                .orderByAsc(DomainDO::getLevel, DomainDO::getSort));
    }

    /**
     * 根据类型查询领域列表
     */
    default List<DomainDO> selectByType(String type) {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getType, type)
                .orderByAsc(DomainDO::getLevel, DomainDO::getSort));
    }

    /**
     * 查询指定深度范围内的领域
     */
    default List<DomainDO> selectByLevelRange(Integer minLevel, Integer maxLevel) {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .ge(minLevel != null, DomainDO::getLevel, minLevel)
                .le(maxLevel != null, DomainDO::getLevel, maxLevel)
                .orderByAsc(DomainDO::getLevel, DomainDO::getSort));
    }

    /**
     * 查询叶子节点（无子领域的领域）
     */
    default List<DomainDO> selectLeafNodes() {
        return selectList(new LambdaQueryWrapperX<DomainDO>()
                .eq(DomainDO::getChildCount, 0)
                .orderByAsc(DomainDO::getLevel, DomainDO::getSort));
    }

} 