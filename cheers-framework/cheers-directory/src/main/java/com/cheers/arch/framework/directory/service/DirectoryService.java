package com.cheers.arch.framework.directory.service;

import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;

import java.util.List;

/**
 * 目录服务接口
 */
public interface DirectoryService {

    /**
     * 创建目录
     *
     * @param directory 目录信息
     * @return 目录ID
     */
    Long createDirectory(DirectoryDO directory);

    /**
     * 更新目录
     *
     * @param directory 目录信息
     */
    void updateDirectory(DirectoryDO directory);

    /**
     * 删除目录
     *
     * @param id 目录ID
     */
    void deleteDirectory(Long id);

    /**
     * 获取目录
     *
     * @param id 目录ID
     * @return 目录信息
     */
    DirectoryDO getDirectory(Long id);

    /**
     * 获取目录
     *
     * @param businessType 业务类型
     * @param code 目录编码
     * @return 目录信息
     */
    DirectoryDO getDirectoryByCode(String businessType, String code);

    /**
     * 获取目录列表
     *
     * @param ids 目录ID列表
     * @return 目录列表
     */
    List<DirectoryDO> getDirectoryList(List<Long> ids);

    /**
     * 获取目录树
     *
     * @param businessType 业务类型
     * @return 目录树
     */
    List<DirectoryDO> getDirectoryTree(String businessType);

    /**
     * 获取目录的子目录列表
     *
     * @param businessType 业务类型
     * @param parentId 父目录ID
     * @return 子目录列表
     */
    List<DirectoryDO> getChildDirectories(String businessType, Long parentId);

    /**
     * 校验目录编码是否唯一
     *
     * @param businessType 业务类型
     * @param code 目录编码
     * @param excludeId 排除的目录ID
     * @return 是否唯一
     */
    boolean isCodeUnique(String businessType, String code, Long excludeId);

    /**
     * 获取租户的所有目录列表
     *
     * @param tenantId 租户ID
     * @param businessType 业务类型
     * @return 目录列表
     */
    List<DirectoryDO> getDirectoryListByTenant(Long tenantId, String businessType);
} 