package com.cheers.arch.module.system.service.category.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryDO;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryBizTypeRelDO;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryUpdateReqVO;
import com.cheers.arch.module.system.dal.mysql.category.CategoryMapper;
import com.cheers.arch.module.system.dal.mysql.category.CategoryBizTypeRelMapper;
import com.cheers.arch.module.system.service.category.CategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryBizTypeRelMapper categoryBizTypeRelMapper;

    @Override
    public List<CategoryDO> getCategoryTree() {
        // 确保有根节点
        ensureRoot();
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getDeleted, false)
                .orderByAsc(CategoryDO::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public List<CategoryDO> getCategoryListByParentId(Long parentId) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getDeleted, false)
                .eq(CategoryDO::getParentId, parentId != null ? parentId : 0L)
                .orderByAsc(CategoryDO::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public CategoryDO getCategory(Long id) {
        return categoryMapper.selectById(id);
    }

    /**
     * 自动补齐根节点（只读）
     */
    private void ensureRoot() {
        long count = categoryMapper.selectCount(new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getParentId, 0L)
                .eq(CategoryDO::getCode, "ROOT")
                .eq(CategoryDO::getDeleted, false));
        if (count == 0) {
            CategoryDO root = new CategoryDO()
                    .setParentId(0L)
                    .setCode("ROOT")
                    .setName("根分类")
                    .setDescription("系统根分类，所有分类的父级")
                    .setTreePath("0")
                    .setLevel(1)
                    .setSort(0)
                    .setReadonly(true);
            categoryMapper.insert(root);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(CategoryCreateReqVO createReqVO) {
        // 转换为 DO 并调用核心实现
        CategoryDO category = BeanUtils.toBean(createReqVO, CategoryDO.class);
        return createCategory(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryUpdateReqVO updateReqVO) {
        // 转换为 DO 并调用核心实现
        CategoryDO category = BeanUtils.toBean(updateReqVO, CategoryDO.class);
        updateCategory(category);
    }

    // ========== 核心实现方法（内部使用） ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(CategoryDO bean) {
        // 设置租户ID（安全保证）
        bean.setTenantId(TenantContextHolder.getTenantId());
        
        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        categoryMapper.insert(bean);
        return bean.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategory(CategoryDO bean) {
        CategoryDO origin = categoryMapper.selectById(bean.getId());
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("只读分类禁止修改");
        }

        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        return categoryMapper.updateById(bean) > 0;
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
            ids.addAll(children.stream().map(CategoryDO::getId).collect(Collectors.toList()));
        }
        
        categoryMapper.update(null,
            new LambdaUpdateWrapper<CategoryDO>()
                .set(CategoryDO::getDeleted, true)
                .in(CategoryDO::getId, ids)
        );
    }

    @Override
    public boolean canDeleteCategory(Long id) {
        // 检查是否为只读节点
        CategoryDO category = categoryMapper.selectById(id);
        if (category != null && Boolean.TRUE.equals(category.getReadonly())) {
            return false;
        }
        
        // 检查是否有子节点
        long childCount = categoryMapper.selectCount(new LambdaQueryWrapper<CategoryDO>()
                .eq(CategoryDO::getParentId, id)
                .eq(CategoryDO::getDeleted, false));
        return childCount == 0;
    }

    @Override
    public List<CategoryDO> getCategoryList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return categoryMapper.selectList(new LambdaQueryWrapper<CategoryDO>().in(CategoryDO::getId, ids));
    }

    @Override
    public Long createCategoryBizTypeRel(Long categoryId, String businessType, Long businessId, String relType, Integer sort, Boolean required) {
        CategoryBizTypeRelDO rel = new CategoryBizTypeRelDO();
        rel.setCategoryId(categoryId);
        rel.setBusinessType(businessType);
        rel.setBusinessId(businessId);
        rel.setRelType(relType);
        rel.setSort(sort != null ? sort : 0);
        rel.setRequired(required != null ? required : false);
        rel.setTenantId(TenantContextHolder.getTenantId());
        categoryBizTypeRelMapper.insert(rel);
        return rel.getId();
    }

    @Override
    public void deleteCategoryBizTypeRel(Long id) {
        categoryBizTypeRelMapper.deleteById(id);
    }

    @Override
    public List<CategoryBizTypeRelDO> getCategoryBizTypeRelList(String businessType, Long businessId) {
        LambdaQueryWrapper<CategoryBizTypeRelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBizTypeRelDO::getDeleted, false)
                .eq(CategoryBizTypeRelDO::getBusinessType, businessType)
                .eq(CategoryBizTypeRelDO::getBusinessId, businessId)
                .orderByAsc(CategoryBizTypeRelDO::getSort);
        return categoryBizTypeRelMapper.selectList(wrapper);
    }

    @Override
    public List<CategoryDO> getCategoryTreeByBusinessType(String businessType) {
        // 通过关联关系获取该业务类型下的分类
        List<CategoryBizTypeRelDO> rels = categoryBizTypeRelMapper.selectList(
            new LambdaQueryWrapper<CategoryBizTypeRelDO>()
                .eq(CategoryBizTypeRelDO::getBusinessType, businessType)
                .eq(CategoryBizTypeRelDO::getDeleted, false)
        );
        
        if (rels.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Long> categoryIds = rels.stream()
                .map(CategoryBizTypeRelDO::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        
        return categoryMapper.selectList(new LambdaQueryWrapper<CategoryDO>().in(CategoryDO::getId, categoryIds));
    }

    /**
     * 生成 treePath & level
     */
    private void fillTree(CategoryDO bean) {
        if (bean.getParentId() == null) {
            bean.setParentId(0L);
        }
        if (bean.getParentId() == 0L) {
            bean.setTreePath("0");
            bean.setLevel(1);
        } else {
            CategoryDO parent = categoryMapper.selectById(bean.getParentId());
            String parentPath = parent != null ? parent.getTreePath() : "0";
            bean.setTreePath(parentPath + "/" + bean.getParentId());
            bean.setLevel(parent != null ? parent.getLevel() + 1 : 2);
        }
    }
} 