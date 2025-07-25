package com.cheers.arch.module.system.service.category.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.cheers.arch.framework.trees.service.AbstractTreeService;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryBizTypeRelDO;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryDO;
import com.cheers.arch.module.system.dal.mysql.category.CategoryBizTypeRelMapper;
import com.cheers.arch.module.system.dal.mysql.category.CategoryMapper;
import com.cheers.arch.module.system.service.business.BusinessTypeService;
import com.cheers.arch.module.system.service.category.CategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class CategoryServiceImpl extends AbstractTreeService<CategoryMapper, CategoryDO, Long, TenantBaseDO> 
        implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryBizTypeRelMapper categoryBizTypeRelMapper;

    @Resource
    private BusinessTypeService businessTypeService;

    // ==================== AbstractTreeService 抽象方法实现 ====================

    @Override
    protected CategoryMapper getMapper() {
        return categoryMapper;
    }

    @Override
    protected String getTableName() {
        return "system_category";
    }

    @Override
    protected List<CategoryDO> getChildrenByParentId(Long parentId) {
        return categoryMapper.selectByParentId(parentId != null ? parentId : 0L);
    }

    @Override
    public List<CategoryDO> getCategoryTree() {
        // 确保有根节点
        ensureRoot();
        // 获取所有节点并构建树结构
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryDO::getDeleted, false)
                .orderByAsc(CategoryDO::getSort);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public List<CategoryDO> getCategoryListByParentId(Long parentId) {
        // 使用框架方法获取子节点
        return getChildren(parentId != null ? parentId : 0L);
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
        CategoryDO bean = BeanUtils.toBean(createReqVO, CategoryDO.class);
        return createCategory(bean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryUpdateReqVO updateReqVO) {
        CategoryDO bean = BeanUtils.toBean(updateReqVO, CategoryDO.class);
        updateCategory(bean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(CategoryDO bean) {
        // 使用框架的创建方法，自动处理treePath和level生成
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        createNode(bean);
        return bean.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategory(CategoryDO bean) {
        // 1. 检查是否为只读节点
        CategoryDO origin = categoryMapper.selectById(bean.getId());
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("只读分类禁止修改");
        }

        // 使用框架的更新方法，自动处理treePath和level变更
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        updateNode(bean);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 1. 检查是否为只读节点
        CategoryDO origin = categoryMapper.selectById(id);
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("系统只读分类禁止删除");
        }

        // 2. 使用框架的删除方法，自动处理子节点删除
        deleteNode(id);
    }

    @Override
    public boolean canDeleteCategory(Long id) {
        // 检查是否为只读节点
        CategoryDO category = categoryMapper.selectById(id);
        if (category != null && Boolean.TRUE.equals(category.getReadonly())) {
            return false;
        }
        
        // 使用框架方法检查是否有子节点
        List<CategoryDO> children = getChildren(id);
        return children.isEmpty();
    }

    @Override
    public List<CategoryDO> getCategoryList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return categoryMapper.selectList(new LambdaQueryWrapper<CategoryDO>().in(CategoryDO::getId, ids));
    }

    @Override
    public Long createCategoryBizTypeRel(Long categoryId, String businessType, Long businessId, Integer sort, Boolean required) {
        // 验证业务类型是否存在
        if (!businessTypeService.existsByTypeCode(businessType)) {
            throw new IllegalArgumentException("业务类型不存在: " + businessType);
        }
        
        CategoryBizTypeRelDO rel = new CategoryBizTypeRelDO();
        rel.setCategoryId(categoryId);
        rel.setBusinessType(businessType);
        rel.setBusinessId(businessId);
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
        // 验证业务类型是否存在
        if (!businessTypeService.existsByTypeCode(businessType)) {
            log.warn("业务类型不存在: {}", businessType);
            return new ArrayList<>();
        }
        
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

    // 注意：原来的fillTree方法已经被TreeEntity框架替代
    // 框架会自动处理treePath和level的生成
} 