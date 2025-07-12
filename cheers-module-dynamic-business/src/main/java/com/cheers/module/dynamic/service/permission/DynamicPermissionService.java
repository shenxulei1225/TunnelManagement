package com.cheers.arch.module.dynamic.service.permission;

import com.cheers.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.dal.dataobject.permission.DynamicPermissionDO;

import java.util.List;
import java.util.Set;

/**
 * 动态业务权限服务接口
 */
public interface DynamicPermissionService {

    /**
     * 创建权限
     *
     * @param permission 权限信息
     * @return 权限ID
     */
    Long createPermission(DynamicPermissionDO permission);

    /**
     * 更新权限
     *
     * @param permission 权限信息
     */
    void updatePermission(DynamicPermissionDO permission);

    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    void deletePermission(Long id);

    /**
     * 获取权限
     *
     * @param id 权限ID
     * @return 权限信息
     */
    DynamicPermissionDO getPermission(Long id);

    /**
     * 获取权限列表
     *
     * @param modelCode 业务模型编码
     * @param type 权限类型
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 权限列表
     */
    List<DynamicPermissionDO> getPermissionList(String modelCode, Integer type, Long userId, Long roleId, Long deptId);

    /**
     * 获取权限分页
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param modelCode 业务模型编码
     * @param type 权限类型
     * @param target 权限目标
     * @param userId 用户ID
     * @param roleId 角色ID
     * @param deptId 部门ID
     * @return 权限分页
     */
    PageResult<DynamicPermissionDO> getPermissionPage(Integer pageNo, Integer pageSize, String modelCode, Integer type,
                                                     String target, Long userId, Long roleId, Long deptId);

    /**
     * 判断是否有指定权限
     *
     * @param modelCode 业务模型编码
     * @param type 权限类型
     * @param target 权限目标
     * @param level 权限级别
     * @return 是否有权限
     */
    boolean hasPermission(String modelCode, Integer type, String target, Integer level);

    /**
     * 获取有权限的目标列表
     *
     * @param modelCode 业务模型编码
     * @param type 权限类型
     * @param level 权限级别
     * @return 目标列表
     */
    Set<String> getPermissionTargets(String modelCode, Integer type, Integer level);
} 