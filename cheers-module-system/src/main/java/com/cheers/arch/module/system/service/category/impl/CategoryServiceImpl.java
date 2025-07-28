package com.cheers.arch.module.system.service.category.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.enums.CommonStatusEnum;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
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

/**
 * 分类 Service 实现类
 * 
 * @author cheers
 */
@Service
@Validated
@Slf4j
public class CategoryServiceImpl extends AbstractTreeService<CategoryMapper, CategoryDO, Long, TenantBaseDO> implements CategoryService {

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveCategory(Long categoryId, Long targetParentId, Integer targetSort) {
        // 校验移动的分类存在
        CategoryDO category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }

        // 校验目标父分类的有效性
        if (targetParentId != null && targetParentId != 0) {
            CategoryDO targetParent = categoryMapper.selectById(targetParentId);
            if (targetParent == null) {
                throw new IllegalArgumentException("目标父分类不存在");
            }
            
            // 防止循环引用：不能移动到自己的子节点下
            if (isDescendant(categoryId, targetParentId)) {
                throw new IllegalArgumentException("不能移动到自己的子节点下");
            }
        }

        // 如果目标父分类和当前父分类相同，只需要调整排序
        if (Objects.equals(category.getParentId(), targetParentId)) {
            // 同父级内部调整排序
            adjustSortWithinParent(targetParentId, categoryId, targetSort);
        } else {
            // 移动到不同父级
            moveToNewParent(categoryId, targetParentId, targetSort);
        }
        
        // 更新树路径和层级
        updateTreePathAndLevel(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortCategories(List<Map<String, Object>> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return;
        }

        // 批量更新排序
        for (Map<String, Object> sortItem : sortList) {
            Long id = Long.valueOf(sortItem.get("id").toString());
            Integer sort = Integer.valueOf(sortItem.get("sort").toString());
            
            CategoryDO updateObj = new CategoryDO();
            updateObj.setId(id);
            updateObj.setSort(sort);
            categoryMapper.updateById(updateObj);
        }
    }

    @Override
    public List<CategoryDO> searchCategories(String keyword, String businessType) {
        LambdaQueryWrapperX<CategoryDO> queryWrapper = new LambdaQueryWrapperX<CategoryDO>()
                .like(CategoryDO::getName, keyword)
                .or()
                .like(CategoryDO::getCode, keyword)
                .or()
                .like(CategoryDO::getDescription, keyword);
        
        queryWrapper.eq(CategoryDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                   .orderByAsc(CategoryDO::getSort);
        
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOperateCategories(String operation, List<Long> categoryIds, Map<String, Object> params) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        switch (operation.toLowerCase()) {
            case "enable":
                // 批量启用
                for (Long categoryId : categoryIds) {
                    CategoryDO updateObj = new CategoryDO();
                    updateObj.setId(categoryId);
                    updateObj.setStatus(CommonStatusEnum.ENABLE.getStatus());
                    categoryMapper.updateById(updateObj);
                }
                break;
                
            case "disable":
                // 批量禁用
                for (Long categoryId : categoryIds) {
                    CategoryDO updateObj = new CategoryDO();
                    updateObj.setId(categoryId);
                    updateObj.setStatus(CommonStatusEnum.DISABLE.getStatus());
                    categoryMapper.updateById(updateObj);
                }
                break;
                
            case "delete":
                // 批量删除
                for (Long categoryId : categoryIds) {
                    deleteCategory(categoryId);
                }
                break;
                
            case "move":
                // 批量移动
                if (params != null && params.containsKey("targetParentId")) {
                    Long targetParentId = Long.valueOf(params.get("targetParentId").toString());
                    for (int i = 0; i < categoryIds.size(); i++) {
                        moveCategory(categoryIds.get(i), targetParentId, i + 1);
                    }
                }
                break;
                
            default:
                throw new IllegalArgumentException("不支持的操作类型: " + operation);
        }
    }

    /**
     * 检查是否为子孙节点
     */
    private boolean isDescendant(Long ancestorId, Long descendantId) {
        CategoryDO descendant = categoryMapper.selectById(descendantId);
        while (descendant != null && descendant.getParentId() != null && descendant.getParentId() != 0) {
            if (Objects.equals(descendant.getParentId(), ancestorId)) {
                return true;
            }
            descendant = categoryMapper.selectById(descendant.getParentId());
        }
        return false;
    }

    /**
     * 在同一父级内调整排序
     */
    private void adjustSortWithinParent(Long parentId, Long moveCategoryId, Integer targetSort) {
        List<CategoryDO> siblings = categoryMapper.selectList(
                new LambdaQueryWrapperX<CategoryDO>()
                        .eq(CategoryDO::getParentId, parentId != null ? parentId : 0)
                        .orderByAsc(CategoryDO::getSort)
        );
        
        if (targetSort == null) {
            targetSort = siblings.size();
        }

        // 重新计算所有兄弟节点的排序
        int newSort = 1;
        for (CategoryDO sibling : siblings) {
            if (Objects.equals(sibling.getId(), moveCategoryId)) {
                continue; // 跳过移动的节点
            }
            
            if (newSort == targetSort) {
                newSort++; // 为移动的节点预留位置
            }
            
            if (!Objects.equals(sibling.getSort(), newSort)) {
                CategoryDO updateObj = new CategoryDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(newSort);
                categoryMapper.updateById(updateObj);
            }
            newSort++;
        }

        // 更新移动节点的排序
        CategoryDO updateObj = new CategoryDO();
        updateObj.setId(moveCategoryId);
        updateObj.setSort(targetSort);
        categoryMapper.updateById(updateObj);
    }

    /**
     * 移动到新的父级
     */
    private void moveToNewParent(Long categoryId, Long targetParentId, Integer targetSort) {
        // 1. 获取目标父级下的所有子节点
        List<CategoryDO> targetSiblings = categoryMapper.selectList(
                new LambdaQueryWrapperX<CategoryDO>()
                        .eq(CategoryDO::getParentId, targetParentId != null ? targetParentId : 0)
                        .orderByAsc(CategoryDO::getSort)
        );
        
        if (targetSort == null || targetSort > targetSiblings.size() + 1) {
            targetSort = targetSiblings.size() + 1;
        }

        // 2. 调整目标父级下其他节点的排序
        for (CategoryDO sibling : targetSiblings) {
            if (sibling.getSort() >= targetSort) {
                CategoryDO updateObj = new CategoryDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(sibling.getSort() + 1);
                categoryMapper.updateById(updateObj);
            }
        }

        // 3. 更新移动节点的父级和排序
        CategoryDO updateObj = new CategoryDO();
        updateObj.setId(categoryId);
        updateObj.setParentId(targetParentId);
        updateObj.setSort(targetSort);
        categoryMapper.updateById(updateObj);

        // 4. 重新整理原父级下的排序
        CategoryDO originalCategory = categoryMapper.selectById(categoryId);
        if (originalCategory != null && originalCategory.getParentId() != null) {
            reorderSiblings(originalCategory.getParentId());
        }
    }

    /**
     * 重新整理兄弟节点的排序，消除空隙
     */
    private void reorderSiblings(Long parentId) {
        List<CategoryDO> siblings = categoryMapper.selectList(
                new LambdaQueryWrapperX<CategoryDO>()
                        .eq(CategoryDO::getParentId, parentId != null ? parentId : 0)
                        .orderByAsc(CategoryDO::getSort)
        );
        
        int sort = 1;
        for (CategoryDO sibling : siblings) {
            if (!Objects.equals(sibling.getSort(), sort)) {
                CategoryDO updateObj = new CategoryDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(sort);
                categoryMapper.updateById(updateObj);
            }
            sort++;
        }
    }

    /**
     * 更新树路径和层级
     */
    private void updateTreePathAndLevel(Long categoryId) {
        CategoryDO category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return;
        }

        // 构建新的树路径和层级
        StringBuilder pathBuilder = new StringBuilder();
        int level = 1;
        
        if (category.getParentId() != null && category.getParentId() != 0) {
            CategoryDO parent = categoryMapper.selectById(category.getParentId());
            if (parent != null) {
                pathBuilder.append(parent.getTreePath()).append("/");
                level = parent.getLevel() + 1;
            }
        }
        
        pathBuilder.append(categoryId);
        
        // 更新当前节点
        CategoryDO updateObj = new CategoryDO();
        updateObj.setId(categoryId);
        updateObj.setTreePath(pathBuilder.toString());
        updateObj.setLevel(level);
        categoryMapper.updateById(updateObj);
        
        // 递归更新所有子节点
        updateChildrenTreePath(categoryId);
    }

    /**
     * 递归更新所有子节点的树路径和层级
     */
    private void updateChildrenTreePath(Long parentId) {
        List<CategoryDO> children = categoryMapper.selectList(
                new LambdaQueryWrapperX<CategoryDO>()
                        .eq(CategoryDO::getParentId, parentId)
        );
        
        for (CategoryDO child : children) {
            updateTreePathAndLevel(child.getId());
        }
    }

    /**
     * 构建树形结构
     */
    private List<CategoryDO> buildTree(List<CategoryDO> categories) {
        // 实现树形结构构建逻辑
        return categories.stream()
                .filter(category -> category.getParentId() == null || category.getParentId() == 0)
                .collect(java.util.stream.Collectors.toList());
    }

} 