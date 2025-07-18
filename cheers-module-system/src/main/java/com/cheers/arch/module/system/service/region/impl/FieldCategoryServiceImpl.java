package com.cheers.arch.module.system.service.region.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO;
import com.cheers.arch.module.system.dal.mysql.region.FieldCategoryMapper;
import com.cheers.arch.module.system.service.region.FieldCategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;

@Service
public class FieldCategoryServiceImpl implements FieldCategoryService {

    @Resource
    private FieldCategoryMapper categoryMapper;

    @Override
    public List<FieldCategoryDO> getCategoryTree(String bizType) {
        // 确保每个 BizTypeEnum 都有一个根节点
        ensureBizRoots();
        LambdaQueryWrapper<FieldCategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FieldCategoryDO::getDeleted, false)
                .orderByAsc(FieldCategoryDO::getSort);
        if (cn.hutool.core.util.StrUtil.isNotBlank(bizType)) {
            // 找到业务根节点
            FieldCategoryDO root = categoryMapper.selectOne(new LambdaQueryWrapper<FieldCategoryDO>()
                    .eq(FieldCategoryDO::getParentId, 0L)
                    .eq(FieldCategoryDO::getCode, bizType)
                    .eq(FieldCategoryDO::getDeleted, false));
            if (root == null) {
                return java.util.Collections.emptyList();
            }
            wrapper.and(w -> w.eq(FieldCategoryDO::getId, root.getId())
                    .or().likeRight(FieldCategoryDO::getTreePath, root.getTreePath() + "/" + root.getId()));
        }
        return categoryMapper.selectList(wrapper);
    }

    /**
     * 自动补齐各业务类型的根节点（只读）
     */
    private void ensureBizRoots() {
        for (com.cheers.arch.module.system.enums.BizTypeEnum bt : com.cheers.arch.module.system.enums.BizTypeEnum.values()) {
            long count = categoryMapper.selectCount(new LambdaQueryWrapper<FieldCategoryDO>()
                    .eq(FieldCategoryDO::getParentId, 0L)
                    .eq(FieldCategoryDO::getCode, bt.name())
                    .eq(FieldCategoryDO::getDeleted, false));
            if (count == 0) {
                FieldCategoryDO root = new FieldCategoryDO()
                        .setParentId(0L)
                        .setCode(bt.name())
                        .setName(bt.getLabel())
                        .setTreePath("0")
                        .setLevel(1)
                        .setSort(0)
                        .setReadonly(true);
                categoryMapper.insert(root);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(FieldCategoryDO bean) {
        // 只读节点禁止新增子节点修改名称？ 子节点可以，所以仅检查自身 readonly

        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        categoryMapper.insert(bean);
        return bean.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCategory(FieldCategoryDO bean) {
        FieldCategoryDO origin = categoryMapper.selectById(bean.getId());
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("系统只读分类禁止修改");
        }

        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        return categoryMapper.updateById(bean) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCategory(Long id) {
        // 1. 检查是否为只读节点
        FieldCategoryDO origin = categoryMapper.selectById(id);
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("系统只读分类禁止删除");
        }

        // 2. 查找所有子节点
        List<FieldCategoryDO> children = categoryMapper.selectList(
            new LambdaQueryWrapper<FieldCategoryDO>()
                .likeRight(FieldCategoryDO::getTreePath, origin.getTreePath() + "/" + origin.getId())
                .eq(FieldCategoryDO::getDeleted, false)
        );
        
        // 3. 软删除当前节点和所有子节点
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        if (!children.isEmpty()) {
            ids.addAll(children.stream().map(FieldCategoryDO::getId).collect(Collectors.toList()));
        }
        
        return categoryMapper.update(null,
            new LambdaUpdateWrapper<FieldCategoryDO>()
                .set(FieldCategoryDO::getDeleted, true)
                .in(FieldCategoryDO::getId, ids)
        ) > 0;
    }

    /**
     * 生成 treePath & level
     */
    private void fillTree(FieldCategoryDO bean) {
        if (bean.getParentId() == null) {
            bean.setParentId(0L);
        }
        if (bean.getParentId() == 0L) {
            bean.setTreePath("0");
            bean.setLevel(1);
        } else {
            FieldCategoryDO parent = categoryMapper.selectById(bean.getParentId());
            String parentPath = parent != null ? parent.getTreePath() : "0";
            bean.setTreePath(parentPath + "/" + bean.getParentId());
            bean.setLevel(parent != null ? parent.getLevel() + 1 : 2);
        }
    }

    /** 根据名称生成唯一编码 */
    private String genCode(String name) {
        String letters = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (letters.isEmpty()) {
            letters = "CAT";
        }
        if (letters.length() > 8) {
            letters = letters.substring(0, 8);
        }
        return letters + System.currentTimeMillis();
    }
}
