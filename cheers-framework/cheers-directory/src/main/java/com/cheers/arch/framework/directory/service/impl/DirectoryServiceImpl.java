package com.cheers.arch.framework.directory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.directory.constants.DirectoryErrorCode;
import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;
import com.cheers.arch.framework.directory.dal.mysql.DirectoryMapper;
import com.cheers.arch.framework.directory.service.DirectoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目录服务实现类
 */
@Service
@Slf4j
public class DirectoryServiceImpl implements DirectoryService {

    @Resource
    private DirectoryMapper directoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDirectory(DirectoryDO directory) {
        // 校验编码唯一性
        if (!isCodeUnique(directory.getBusinessType(), directory.getCode(), null)) {
            throw new ServiceException(DirectoryErrorCode.DIRECTORY_CODE_DUPLICATE);
        }

        // 设置默认值
        if (directory.getParentId() == null) {
            directory.setParentId(0L);
        }
        if (directory.getSort() == null) {
            directory.setSort(0);
        }
        if (directory.getStatus() == null) {
            directory.setStatus(1);
        }

        // 插入数据
        directoryMapper.insert(directory);

        return directory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDirectory(DirectoryDO directory) {
        // 校验存在
        validateDirectoryExists(directory.getId());

        // 校验编码唯一性
        if (!isCodeUnique(directory.getBusinessType(), directory.getCode(), directory.getId())) {
            throw new ServiceException(DirectoryErrorCode.DIRECTORY_CODE_DUPLICATE);
        }

        // 更新数据
        directoryMapper.updateById(directory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDirectory(Long id) {
        // 校验存在
        DirectoryDO directory = validateDirectoryExists(id);

        // 检查是否有子目录
        List<DirectoryDO> children = getChildDirectories(directory.getBusinessType(), id);
        if (!children.isEmpty()) {
            throw new ServiceException(DirectoryErrorCode.DIRECTORY_HAS_CHILDREN);
        }

        // 删除目录
        directoryMapper.deleteById(id);
    }

    @Override
    public DirectoryDO getDirectory(Long id) {
        return directoryMapper.selectById(id);
    }

    @Override
    public DirectoryDO getDirectoryByCode(String businessType, String code) {
        return directoryMapper.selectOne(new LambdaQueryWrapper<DirectoryDO>()
                .eq(DirectoryDO::getBusinessType, businessType)
                .eq(DirectoryDO::getCode, code));
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
        // 获取所有目录
        List<DirectoryDO> allDirectories = directoryMapper.selectList(
                new LambdaQueryWrapper<DirectoryDO>()
                        .eq(DirectoryDO::getBusinessType, businessType)
                        .orderByAsc(DirectoryDO::getSort)
                        .orderByAsc(DirectoryDO::getId));

        // 构建树形结构
        return buildDirectoryTree(allDirectories, 0L);
    }

    @Override
    public List<DirectoryDO> getChildDirectories(String businessType, Long parentId) {
        return directoryMapper.selectList(
                new LambdaQueryWrapper<DirectoryDO>()
                        .eq(DirectoryDO::getBusinessType, businessType)
                        .eq(DirectoryDO::getParentId, parentId)
                        .orderByAsc(DirectoryDO::getSort)
                        .orderByAsc(DirectoryDO::getId));
    }

    @Override
    public boolean isCodeUnique(String businessType, String code, Long excludeId) {
        LambdaQueryWrapperX<DirectoryDO> queryWrapper = new LambdaQueryWrapperX<DirectoryDO>()
                .eq(DirectoryDO::getBusinessType, businessType)
                .eq(DirectoryDO::getCode, code);
        
        if (excludeId != null) {
            queryWrapper.ne(DirectoryDO::getId, excludeId);
        }
        
        return directoryMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public List<DirectoryDO> getDirectoryListByTenant(Long tenantId, String businessType) {
        return directoryMapper.selectList(
                new LambdaQueryWrapper<DirectoryDO>()
                        .eq(DirectoryDO::getTenantId, tenantId)
                        .eq(DirectoryDO::getBusinessType, businessType)
                        .orderByAsc(DirectoryDO::getSort)
                        .orderByAsc(DirectoryDO::getId));
    }

    /**
     * 构建目录树
     */
    private List<DirectoryDO> buildDirectoryTree(List<DirectoryDO> allDirectories, Long parentId) {
        return allDirectories.stream()
                .filter(dir -> dir.getParentId().equals(parentId))
                .map(dir -> {
                    // 递归构建子目录
                    List<DirectoryDO> children = buildDirectoryTree(allDirectories, dir.getId());
                    // 这里可以设置children属性，如果需要的话
                    return dir;
                })
                .collect(Collectors.toList());
    }

    /**
     * 校验目录是否存在
     */
    private DirectoryDO validateDirectoryExists(Long id) {
        DirectoryDO directory = getDirectory(id);
        if (directory == null) {
            throw new ServiceException(DirectoryErrorCode.DIRECTORY_NOT_FOUND);
        }
        return directory;
    }
} 