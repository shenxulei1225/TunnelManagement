package com.cheers.arch.framework.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.category.dal.dataobject.CategoryDO;
import com.cheers.arch.framework.category.dal.mysql.CategoryMapper;
import com.cheers.arch.framework.category.service.CategoryService;
import com.cheers.arch.framework.category.service.vo.CategoryCreateReqVO;
import com.cheers.arch.framework.category.service.vo.CategoryUpdateReqVO;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.common.util.tree.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类 Service 实现类
 */
@Service
@Validated
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(CategoryCreateReqVO createReqVO) {
        // 1. 插入
        CategoryDO category = BeanUtils.toBean(createReqVO, CategoryDO.class);
        categoryMapper.insert(category);
        // 2. 返回
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryUpdateReqVO updateReqVO) {
        // 1. 校验存在
        validateCategoryExists(updateReqVO.getId());
        // 2. 更新
        CategoryDO updateObj = BeanUtils.toBean(updateReqVO, CategoryDO.class);
        categoryMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 1. 检查是否为只读节点
        CategoryDO origin = categoryMapper.selectById(id);
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("系统只读分类禁止删除");
        }

        // 2. 查找所有子节点
        List<CategoryDO> children = categoryMapper.selectList(
            new LambdaQueryWrapper<CategoryDO>()
                .likeRight(CategoryDO::getTreePath, origin.getTreePath() + "/" + origin.getId())
                .eq(CategoryDO::getDeleted, false)
        );
        
        // 3. 软删除当前节点和所有子节点
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        if (!children.isEmpty()) {
            ids.addAll(children.stream().map(CategoryDO::getId).toList());
        }
        categoryMapper.deleteBatchIds(ids);
    }

    @Override
    public CategoryDO getCategory(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<CategoryDO> getCategoryList(List<Long> ids) {
        return categoryMapper.selectBatchIds(ids);
    }

    @Override
    public List<CategoryDO> getCategoryTree(String businessType) {
        List<CategoryDO> list = categoryMapper.selectList(
            new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getBusinessType, businessType)
                .eq(CategoryDO::getDeleted, false)
                .orderByAsc(CategoryDO::getSort)
        );
        return TreeUtils.build(list);
    }

    @Override
    public List<CategoryDO> getCategoryListByParentId(Long parentId, String businessType) {
        return categoryMapper.selectList(
            new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getParentId, parentId)
                .eq(CategoryDO::getBusinessType, businessType)
                .eq(CategoryDO::getDeleted, false)
                .orderByAsc(CategoryDO::getSort)
        );
    }

    @Override
    public boolean canDeleteCategory(Long id) {
        // 检查是否有子分类
        Long count = categoryMapper.selectCount(
            new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getParentId, id)
                .eq(CategoryDO::getDeleted, false)
        );
        return count == 0;
    }

    private void validateCategoryExists(Long id) {
        if (categoryMapper.selectById(id) == null) {
            throw new IllegalArgumentException("分类不存在");
        }
    }

} 