package com.cheers.arch.module.system.service.directory.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cheers.arch.module.system.dal.dataobject.directory.DirectoryDO;
import com.cheers.arch.module.system.dal.mysql.directory.DirectoryMapper;
import com.cheers.arch.module.system.service.directory.DirectoryService;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryUpdateReqVO;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;

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

    // ========== VO 方法（推荐使用，安全） ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDirectory(DirectoryCreateReqVO createReqVO) {
        // 转换为 DO 并调用核心实现
        DirectoryDO directory = BeanUtils.toBean(createReqVO, DirectoryDO.class);
        return createDirectory(directory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDirectory(DirectoryUpdateReqVO updateReqVO) {
        // 转换为 DO 并调用核心实现
        DirectoryDO directory = BeanUtils.toBean(updateReqVO, DirectoryDO.class);
        updateDirectory(directory);
    }

    // ========== 核心实现方法（内部使用） ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDirectory(DirectoryDO directory) {
        // 设置租户ID（安全保证）
        directory.setTenantId(TenantContextHolder.getTenantId());
        
        // 1. 校验编码唯一性（同一业务类型下）
        if (!isCodeUnique(directory.getBusinessType(), directory.getCode(), null)) {
            throw new IllegalArgumentException("目录编码已存在");
        }
        
        // 2. 校验父目录存在性和业务类型一致性
        if (directory.getParentId() != null && directory.getParentId() > 0) {
            DirectoryDO parent = directoryMapper.selectById(directory.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("父目录不存在");
            }
            
            // 校验业务类型一致性
            if (!directory.getBusinessType().equals(parent.getBusinessType())) {
                throw new IllegalArgumentException("父目录与当前目录的业务类型不一致");
            }
            
            // 3. 校验同一父目录下编码唯一性
            DirectoryDO existingSibling = directoryMapper.selectOne(
                new LambdaQueryWrapper<DirectoryDO>()
                    .eq(DirectoryDO::getBusinessType, directory.getBusinessType())
                    .eq(DirectoryDO::getParentId, directory.getParentId())
                    .eq(DirectoryDO::getCode, directory.getCode())
            );
            if (existingSibling != null) {
                throw new IllegalArgumentException("同一父目录下已存在相同编码的目录");
            }
        } else {
            // 4. 根目录编码唯一性检查
            DirectoryDO existingRoot = directoryMapper.selectOne(
                new LambdaQueryWrapper<DirectoryDO>()
                    .eq(DirectoryDO::getBusinessType, directory.getBusinessType())
                    .eq(DirectoryDO::getParentId, 0L)
                    .eq(DirectoryDO::getCode, directory.getCode())
            );
            if (existingRoot != null) {
                throw new IllegalArgumentException("根目录下已存在相同编码的目录");
            }
        }
        
        directoryMapper.insert(directory);
        return directory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDirectory(DirectoryDO directory) {
        // 设置租户ID（安全保证）
        directory.setTenantId(TenantContextHolder.getTenantId());
        
        // 1. 校验目录是否存在
        DirectoryDO existingDirectory = directoryMapper.selectById(directory.getId());
        if (existingDirectory == null) {
            throw new IllegalArgumentException("目录不存在");
        }
        
        // 2. 校验编码唯一性（同一业务类型下）
        if (!isCodeUnique(directory.getBusinessType(), directory.getCode(), directory.getId())) {
            throw new IllegalArgumentException("目录编码已存在");
        }
        
        // 3. 校验父目录存在性和业务类型一致性
        if (directory.getParentId() != null && directory.getParentId() > 0) {
            DirectoryDO parent = directoryMapper.selectById(directory.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("父目录不存在");
            }
            
            // 校验业务类型一致性
            if (!directory.getBusinessType().equals(parent.getBusinessType())) {
                throw new IllegalArgumentException("父目录与当前目录的业务类型不一致");
            }
            
            // 4. 校验不是移动到自己的子目录
            if (isChildOf(directory.getParentId(), directory.getId())) {
                throw new IllegalArgumentException("不能移动到自己的子目录，会导致循环引用");
            }
            
            // 5. 校验同一父目录下编码唯一性
            DirectoryDO existingSibling = directoryMapper.selectOne(
                new LambdaQueryWrapper<DirectoryDO>()
                    .eq(DirectoryDO::getBusinessType, directory.getBusinessType())
                    .eq(DirectoryDO::getParentId, directory.getParentId())
                    .eq(DirectoryDO::getCode, directory.getCode())
                    .ne(DirectoryDO::getId, directory.getId())
            );
            if (existingSibling != null) {
                throw new IllegalArgumentException("同一父目录下已存在相同编码的目录");
            }
        } else {
            // 6. 根目录编码唯一性检查
            DirectoryDO existingRoot = directoryMapper.selectOne(
                new LambdaQueryWrapper<DirectoryDO>()
                    .eq(DirectoryDO::getBusinessType, directory.getBusinessType())
                    .eq(DirectoryDO::getParentId, 0L)
                    .eq(DirectoryDO::getCode, directory.getCode())
                    .ne(DirectoryDO::getId, directory.getId())
            );
            if (existingRoot != null) {
                throw new IllegalArgumentException("根目录下已存在相同编码的目录");
            }
        }
        
        directoryMapper.updateById(directory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDirectory(Long id) {
        // 1. 校验目录是否存在
        DirectoryDO directory = directoryMapper.selectById(id);
        if (directory == null) {
            throw new IllegalArgumentException("目录不存在");
        }
        
        // 2. 检查是否有子目录
        List<DirectoryDO> children = directoryMapper.selectList(
            new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getParentId, id)
        );
        
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("存在子目录，无法删除");
        }
        
        // 3. 检查目录状态（可选：禁用的目录可以删除）
        if (directory.getStatus() != null && directory.getStatus() == 1) {
            log.warn("删除启用状态的目录: id={}, name={}", id, directory.getName());
        }
        
        // 4. 执行删除
        directoryMapper.deleteById(id);
        log.info("目录删除成功: id={}, name={}, businessType={}", 
                id, directory.getName(), directory.getBusinessType());
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
        return directoryMapper.selectList(new LambdaQueryWrapper<DirectoryDO>().in(DirectoryDO::getId, ids));
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
            // 1. 校验源目录是否存在
            DirectoryDO sourceDirectory = directoryMapper.selectById(id);
            if (sourceDirectory == null) {
                throw new IllegalArgumentException("源目录不存在");
            }
            
            // 2. 校验循环引用
            if (id.equals(newParentId)) {
                throw new IllegalArgumentException("不能移动到自己下面");
            }
            
            // 3. 校验新父目录存在性和业务类型一致性
            if (newParentId != null && newParentId > 0) {
                DirectoryDO parent = directoryMapper.selectById(newParentId);
                if (parent == null) {
                    throw new IllegalArgumentException("目标父目录不存在");
                }
                
                // 校验业务类型一致性
                if (!sourceDirectory.getBusinessType().equals(parent.getBusinessType())) {
                    throw new IllegalArgumentException("不能移动到不同业务类型的目录下");
                }
                
                // 4. 校验不是移动到自己的子目录（防止循环引用）
                if (isChildOf(newParentId, id)) {
                    throw new IllegalArgumentException("不能移动到自己的子目录，会导致循环引用");
                }
                
                // 5. 校验目标父目录下是否已存在相同编码的目录
                DirectoryDO existingSibling = directoryMapper.selectOne(
                    new LambdaQueryWrapper<DirectoryDO>()
                        .eq(DirectoryDO::getBusinessType, sourceDirectory.getBusinessType())
                        .eq(DirectoryDO::getParentId, newParentId)
                        .eq(DirectoryDO::getCode, sourceDirectory.getCode())
                        .ne(DirectoryDO::getId, id)
                );
                if (existingSibling != null) {
                    throw new IllegalArgumentException("目标父目录下已存在相同编码的目录");
                }
            } else {
                // 6. 移动到根目录时，校验是否已存在相同编码的根目录
                DirectoryDO existingRoot = directoryMapper.selectOne(
                    new LambdaQueryWrapper<DirectoryDO>()
                        .eq(DirectoryDO::getBusinessType, sourceDirectory.getBusinessType())
                        .eq(DirectoryDO::getParentId, 0L)
                        .eq(DirectoryDO::getCode, sourceDirectory.getCode())
                        .ne(DirectoryDO::getId, id)
                );
                if (existingRoot != null) {
                    throw new IllegalArgumentException("根目录下已存在相同编码的目录");
                }
            }
            
            // 7. 校验源目录状态
            if (sourceDirectory.getStatus() != null && sourceDirectory.getStatus() == 0) {
                throw new IllegalArgumentException("禁用的目录不能移动");
            }
            
            // 8. 执行移动操作
            directoryMapper.update(null,
                new LambdaUpdateWrapper<DirectoryDO>()
                    .set(DirectoryDO::getParentId, newParentId)
                    .eq(DirectoryDO::getId, id)
            );
            
            log.info("目录移动成功: id={}, newParentId={}, businessType={}", 
                    id, newParentId, sourceDirectory.getBusinessType());
            return true;
            
        } catch (IllegalArgumentException e) {
            log.warn("目录移动验证失败: id={}, newParentId={}, error={}", id, newParentId, e.getMessage());
            throw e; // 重新抛出验证异常，让调用方知道具体错误
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
     * 使用递归方式检查，避免循环引用
     */
    private boolean isChildOf(Long childId, Long parentId) {
        if (childId == null || parentId == null || childId.equals(parentId)) {
            return false;
        }
        
        DirectoryDO child = directoryMapper.selectById(childId);
        if (child == null || child.getParentId() == null || child.getParentId() == 0) {
            return false;
        }
        
        // 直接子目录检查
        if (child.getParentId().equals(parentId)) {
            return true;
        }
        
        // 递归检查上级目录
        return isChildOf(child.getParentId(), parentId);
    }
} 