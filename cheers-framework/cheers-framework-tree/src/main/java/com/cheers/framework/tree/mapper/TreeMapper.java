package com.cheers.arch.framework.tree.mapper;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.tree.core.TreeEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 树形结构通用Mapper接口
 *
 * @param <T> 实体类型
 * @param <ID> ID类型
 * @param <B> 基础实体类型
 */
public interface TreeMapper<T extends TreeEntity<ID>, ID, B> extends BaseMapperX<T> {

    /**
     * 查询直接子节点
     */
    default List<T> selectDirectChildren(ID parentId) {
        return selectList(new LambdaQueryWrapperX<T>()
                .eq(T::getParentId, parentId)
                .orderByAsc(T::getSort));
    }

    /**
     * 查询所有子孙节点
     */
    default List<T> selectDescendants(String treePath) {
        return selectList(new LambdaQueryWrapperX<T>()
                .likeRight(T::getTreePath, treePath)
                .orderByAsc(T::getLevel)
                .orderByAsc(T::getSort));
    }

    /**
     * 批量更新节点的treePath
     */
    @Update("UPDATE ${tableName} SET tree_path = REPLACE(tree_path, #{oldTreePath}, #{newTreePath}), " +
            "level = (LENGTH(REPLACE(tree_path, #{oldTreePath}, #{newTreePath})) - LENGTH(REPLACE(REPLACE(tree_path, #{oldTreePath}, #{newTreePath}), '/', ''))) " +
            "WHERE tree_path LIKE CONCAT(#{oldTreePath}, '%')")
    void updateDescendantsTreePath(@Param("tableName") String tableName,
                                 @Param("oldTreePath") String oldTreePath,
                                 @Param("newTreePath") String newTreePath);

    /**
     * 查询是否存在子节点
     */
    default boolean hasChildren(ID id) {
        return selectCount(new LambdaQueryWrapperX<T>()
                .eq(T::getParentId, id)) > 0;
    }

    /**
     * 获取同级节点中最大的排序值
     */
    default Integer selectMaxSortByParentId(ID parentId) {
        T entity = selectOne(new LambdaQueryWrapperX<T>()
                .eq(T::getParentId, parentId)
                .orderByDesc(T::getSort)
                .last("LIMIT 1"));
        return entity != null ? entity.getSort() : 0;
    }
} 