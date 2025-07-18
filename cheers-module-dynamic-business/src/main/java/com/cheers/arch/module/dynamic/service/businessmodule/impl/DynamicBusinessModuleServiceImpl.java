package com.cheers.arch.module.dynamic.service.businessmodule.impl;

import com.cheers.arch.module.dynamic.dal.dataobject.businessmodule.DynamicBusinessModuleDO;
import com.cheers.arch.module.dynamic.dal.mysql.businessmodule.DynamicBusinessModuleMapper;
import com.cheers.arch.module.dynamic.service.businessmodule.DynamicBusinessModuleService;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.StrUtil;

import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.util.Objects;

@Service
public class DynamicBusinessModuleServiceImpl implements DynamicBusinessModuleService {

    @Resource
    private DynamicBusinessModuleMapper dynamicBusinessModuleMapper;

    @Override
    public List<DynamicBusinessModuleDO> getBusinessModuleTree(String bizType) {
        // TODO: 如需业务类型根节点可参考原 ensureBizRoots()
        LambdaQueryWrapper<DynamicBusinessModuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DynamicBusinessModuleDO::getDeleted, false)
                .orderByAsc(DynamicBusinessModuleDO::getSort);
        // 如需按业务类型筛选可补充逻辑
        return dynamicBusinessModuleMapper.selectList(wrapper);
    }

    @Override
    public List<DynamicBusinessModuleDO> getBusinessModuleSubTree(Long parentId) {
        LambdaQueryWrapper<DynamicBusinessModuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DynamicBusinessModuleDO::getDeleted, false)
                .eq(DynamicBusinessModuleDO::getParentId, parentId)
                .orderByAsc(DynamicBusinessModuleDO::getSort);
        return dynamicBusinessModuleMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean dragBusinessModule(Long dragId, Long targetParentId, String position, Long targetId) {
        // 获取拖拽节点
        DynamicBusinessModuleDO dragNode = dynamicBusinessModuleMapper.selectById(dragId);
        if (dragNode == null) {
            throw new IllegalArgumentException("拖拽节点不存在");
        }
        
        // 检查是否拖拽到自己或自己的子节点
        if (Objects.equals(dragId, targetParentId) || isChildOf(dragId, targetParentId)) {
            throw new IllegalArgumentException("不能拖拽到自己或自己的子节点");
        }
        
        // 获取目标父节点
        DynamicBusinessModuleDO targetParent = null;
        if (targetParentId != 0L) {
            targetParent = dynamicBusinessModuleMapper.selectById(targetParentId);
            if (targetParent == null) {
                throw new IllegalArgumentException("目标父节点不存在");
            }
        }
        
        // 计算新的排序值
        Integer newSort = calculateNewSort(targetParentId, position, targetId);
        
        // 更新拖拽节点
        dragNode.setParentId(targetParentId);
        dragNode.setSort(newSort);
        fillTree(dragNode);
        
        // 更新拖拽节点及其所有子节点的树路径
        updateTreePath(dragNode);
        
        return dynamicBusinessModuleMapper.updateById(dragNode) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBusinessModule(DynamicBusinessModuleDO bean) {
        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        dynamicBusinessModuleMapper.insert(bean);
        return bean.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBusinessModule(DynamicBusinessModuleDO bean) {
        DynamicBusinessModuleDO origin = dynamicBusinessModuleMapper.selectById(bean.getId());
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("系统只读业务分组禁止修改");
        }
        fillTree(bean);
        if (StrUtil.isBlank(bean.getCode())) {
            bean.setCode(null);
        }
        return dynamicBusinessModuleMapper.updateById(bean) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBusinessModule(Long id) {
        DynamicBusinessModuleDO origin = dynamicBusinessModuleMapper.selectById(id);
        if (origin != null && Boolean.TRUE.equals(origin.getReadonly())) {
            throw new IllegalStateException("系统只读业务分组禁止删除");
        }
        List<DynamicBusinessModuleDO> children = dynamicBusinessModuleMapper.selectList(
            new LambdaQueryWrapper<DynamicBusinessModuleDO>()
                .likeRight(DynamicBusinessModuleDO::getTreePath, origin.getTreePath() + "/" + origin.getId())
                .eq(DynamicBusinessModuleDO::getDeleted, false)
        );
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        if (!children.isEmpty()) {
            ids.addAll(children.stream().map(DynamicBusinessModuleDO::getId).collect(Collectors.toList()));
        }
        return dynamicBusinessModuleMapper.update(null,
            new LambdaUpdateWrapper<DynamicBusinessModuleDO>()
                .set(DynamicBusinessModuleDO::getDeleted, true)
                .in(DynamicBusinessModuleDO::getId, ids)
        ) > 0;
    }

    /**
     * 生成 treePath & level
     */
    private void fillTree(DynamicBusinessModuleDO bean) {
        if (bean.getParentId() == null) {
            bean.setParentId(0L);
        }
        if (bean.getParentId() == 0L) {
            bean.setTreePath("0");
            bean.setLevel(1);
        } else {
            DynamicBusinessModuleDO parent = dynamicBusinessModuleMapper.selectById(bean.getParentId());
            String parentPath = parent != null ? parent.getTreePath() : "0";
            bean.setTreePath(parentPath + "/" + bean.getParentId());
            bean.setLevel(parent != null ? parent.getLevel() + 1 : 2);
        }
    }

    /** 根据名称生成唯一编码 */
    private String genCode(String name) {
        String letters = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (letters.isEmpty()) {
            letters = "MOD";
        }
        if (letters.length() > 8) {
            letters = letters.substring(0, 8);
        }
        return letters + System.currentTimeMillis();
    }

    /**
     * 检查 targetId 是否是 dragId 的子节点
     */
    private boolean isChildOf(Long dragId, Long targetId) {
        if (targetId == null || targetId == 0L) {
            return false;
        }
        DynamicBusinessModuleDO target = dynamicBusinessModuleMapper.selectById(targetId);
        if (target == null) {
            return false;
        }
        return target.getTreePath().contains("/" + dragId + "/") || target.getTreePath().endsWith("/" + dragId);
    }

    /**
     * 计算新的排序值
     */
    private Integer calculateNewSort(Long parentId, String position, Long targetId) {
        List<DynamicBusinessModuleDO> siblings = dynamicBusinessModuleMapper.selectList(
            new LambdaQueryWrapper<DynamicBusinessModuleDO>()
                .eq(DynamicBusinessModuleDO::getParentId, parentId)
                .eq(DynamicBusinessModuleDO::getDeleted, false)
                .orderByAsc(DynamicBusinessModuleDO::getSort)
        );
        
        if (siblings.isEmpty()) {
            return 1;
        }
        
        if ("inner".equals(position)) {
            // 放在目标节点内部，排序值设为1
            return 1;
        }
        
        // 找到目标节点
        DynamicBusinessModuleDO targetNode = null;
        if (targetId != null) {
            targetNode = dynamicBusinessModuleMapper.selectById(targetId);
        }
        
        if (targetNode == null) {
            // 如果没有目标节点，放在最后
            return siblings.get(siblings.size() - 1).getSort() + 1;
        }
        
        // 根据位置计算新的排序值
        if ("before".equals(position)) {
            // 放在目标节点前面
            return targetNode.getSort();
        } else if ("after".equals(position)) {
            // 放在目标节点后面
            return targetNode.getSort() + 1;
        }
        
        return targetNode.getSort() + 1;
    }

    /**
     * 更新树路径
     */
    private void updateTreePath(DynamicBusinessModuleDO node) {
        // 更新当前节点及其所有子节点的树路径
        List<DynamicBusinessModuleDO> children = dynamicBusinessModuleMapper.selectList(
            new LambdaQueryWrapper<DynamicBusinessModuleDO>()
                .likeRight(DynamicBusinessModuleDO::getTreePath, node.getTreePath() + "/" + node.getId())
                .eq(DynamicBusinessModuleDO::getDeleted, false)
        );
        
        for (DynamicBusinessModuleDO child : children) {
            child.setTreePath(node.getTreePath() + "/" + node.getId());
            child.setLevel(node.getLevel() + 1);
            dynamicBusinessModuleMapper.updateById(child);
        }
    }
}
