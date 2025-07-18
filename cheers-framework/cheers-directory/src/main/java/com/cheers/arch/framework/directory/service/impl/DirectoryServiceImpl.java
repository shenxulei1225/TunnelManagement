package com.cheers.arch.framework.directory.service.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;
import com.cheers.arch.framework.directory.dal.mysql.DirectoryMapper;
import com.cheers.arch.framework.directory.service.DirectoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用目录服务实现 - 标准化 MyBatis Plus 实现
 */
@Slf4j
@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Resource
    private DirectoryMapper directoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDirectory(DirectoryDO directory) {
        // 校验编码唯一性
        if (!isCodeUnique(directory.getBusinessType(), directory.getCode(), null)) {
            throw new IllegalArgumentException("目录编码已存在");
        }
        
        // 校验父目录存在性
        if (directory.getParentId() != null && directory.getParentId() > 0) {
            DirectoryDO parent = directoryMapper.selectById(directory.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("父目录不存在");
            }
        }
        
        directoryMapper.insert(directory);
        return directory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDirectory(DirectoryDO directory) {
        // 校验编码唯一性
        if (!isCodeUnique(directory.getBusinessType(), directory.getCode(), directory.getId())) {
            throw new IllegalArgumentException("目录编码已存在");
        }
        
        directoryMapper.updateById(directory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDirectory(Long id) {
        // 检查是否有子目录
        List<DirectoryDO> children = directoryMapper.selectList(
            new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getParentId, id)
        );
        
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("存在子目录，无法删除");
        }
        
        directoryMapper.deleteById(id);
    }

    @Override
    public DirectoryDO getDirectory(Long id) {
        return directoryMapper.selectById(id);
    }

    @Override
    public DirectoryDO getDirectoryByCode(String businessType, String code) {
        return directoryMapper.selectOne(
            new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getBusinessType, businessType)
                .eq(DirectoryDO::getCode, code)
                .last("LIMIT 1")
        );
    }

    @Override
    public List<DirectoryDO> getDirectoryList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return directoryMapper.selectBatchIds(ids);
    }

    @Override
    public List<DirectoryDO> getDirectoryTree(String businessType) {
        return directoryMapper.selectList(
            new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getBusinessType, businessType)
                .orderByAsc(DirectoryDO::getSort)
                .orderByAsc(DirectoryDO::getId)
        );
    }

    @Override
    public List<DirectoryDO> getChildDirectories(String businessType, Long parentId) {
        return directoryMapper.selectList(
            new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getBusinessType, businessType)
                .eq(DirectoryDO::getParentId, parentId)
                .orderByAsc(DirectoryDO::getSort)
                .orderByAsc(DirectoryDO::getId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveDirectory(Long id, Long newParentId) {
        try {
            // 校验循环引用
            if (id.equals(newParentId)) {
                throw new IllegalArgumentException("不能移动到自己下面");
            }
            
            // 校验新父目录存在
            if (newParentId != null && newParentId > 0) {
                DirectoryDO parent = directoryMapper.selectById(newParentId);
                if (parent == null) {
                    throw new IllegalArgumentException("目标父目录不存在");
                }
                
                // 校验不是移动到自己的子目录
                if (isChildOf(newParentId, id)) {
                    throw new IllegalArgumentException("不能移动到自己的子目录");
                }
            }
            
            // 更新父目录
            directoryMapper.update(null,
                new LambdaUpdateWrapper<DirectoryDO>()
                    .set(DirectoryDO::getParentId, newParentId)
                    .eq(DirectoryDO::getId, id)
            );
            
            return true;
        } catch (Exception e) {
            log.error("移动目录失败: id={}, newParentId={}", id, newParentId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSort(Long id, Integer sort) {
        try {
            directoryMapper.update(null,
                new LambdaUpdateWrapper<DirectoryDO>()
                    .set(DirectoryDO::getSort, sort)
                    .eq(DirectoryDO::getId, id)
            );
            return true;
        } catch (Exception e) {
            log.error("更新目录排序失败: id={}, sort={}", id, sort, e);
            return false;
        }
    }

    @Override
    public boolean isCodeUnique(String businessType, String code, Long excludeId) {
        LambdaQueryWrapper<DirectoryDO> wrapper = new LambdaQueryWrapper<DirectoryDO>()
            .eq(DirectoryDO::getBusinessType, businessType)
            .eq(DirectoryDO::getCode, code);
            
        if (excludeId != null) {
            wrapper.ne(DirectoryDO::getId, excludeId);
        }
        
        return directoryMapper.selectCount(wrapper) == 0;
    }

    @Override
    public List<DirectoryDO> getDirectoryListByTenant(Long tenantId, String businessType) {
        return directoryMapper.selectList(
            new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getTenantId, tenantId)
                .eq(DirectoryDO::getBusinessType, businessType)
                .orderByAsc(DirectoryDO::getSort)
                .orderByAsc(DirectoryDO::getId)
        );
    }

    /**
     * 检查 childId 是否是 parentId 的子目录
     */
    private boolean isChildOf(Long childId, Long parentId) {
        DirectoryDO child = directoryMapper.selectById(childId);
        if (child == null || child.getParentId() == null || child.getParentId() == 0) {
            return false;
        }
        
        if (child.getParentId().equals(parentId)) {
            return true;
        }
        
        return isChildOf(child.getParentId(), parentId);
    }
} 