package com.cheers.arch.framework.directory.service;

import java.util.List;

import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;

/**
 * 通用目录服务接口 - 标准化实现
 */
public interface DirectoryService {

    /**
     * 创建目录
     */
    Long createDirectory(DirectoryDO directory);

    /**
     * 更新目录
     */
    void updateDirectory(DirectoryDO directory);

    /**
     * 删除目录
     */
    void deleteDirectory(Long id);

    /**
     * 获取目录
     */
    DirectoryDO getDirectory(Long id);

    /**
     * 根据业务类型和编码获取目录
     */
    DirectoryDO getDirectoryByCode(String businessType, String code);

    /**
     * 获取目录列表
     */
    List<DirectoryDO> getDirectoryList(List<Long> ids);

    /**
     * 获取指定业务类型的完整目录树
     */
    List<DirectoryDO> getDirectoryTree(String businessType);

    /**
     * 获取子目录列表
     */
    List<DirectoryDO> getChildDirectories(String businessType, Long parentId);

    /**
     * 移动目录（用于拖拽调整）
     */
    boolean moveDirectory(Long id, Long newParentId);

    /**
     * 调整目录排序
     */
    boolean updateSort(Long id, Integer sort);

    /**
     * 校验目录编码是否唯一
     */
    boolean isCodeUnique(String businessType, String code, Long excludeId);

    /**
     * 获取租户的所有目录列表
     */
    List<DirectoryDO> getDirectoryListByTenant(Long tenantId, String businessType);
} 