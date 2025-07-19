package com.cheers.arch.module.system.service.directory;

import java.util.List;

import com.cheers.arch.module.system.dal.dataobject.directory.DirectoryDO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryUpdateReqVO;

/**
 * 通用目录服务接口 - 标准化实现
 */
public interface DirectoryService {

    // ========== VO 方法（推荐使用，安全） ==========

    /**
     * 创建目录（推荐使用，安全）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDirectory(DirectoryCreateReqVO createReqVO);

    /**
     * 更新目录（推荐使用，安全）
     *
     * @param updateReqVO 更新信息
     */
    void updateDirectory(DirectoryUpdateReqVO updateReqVO);

    // ========== 核心实现方法（内部使用） ==========

    /**
     * 创建目录（内部使用，已设置 tenantId）
     *
     * @param directory 目录信息
     * @return 编号
     */
    Long createDirectory(DirectoryDO directory);

    /**
     * 更新目录（内部使用，已设置 tenantId）
     *
     * @param directory 目录信息
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