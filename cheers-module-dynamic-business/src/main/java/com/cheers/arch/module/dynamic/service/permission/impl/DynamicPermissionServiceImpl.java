package com.cheers.arch.module.dynamic.service.permission.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.security.core.util.SecurityFrameworkUtils;
import com.cheers.arch.module.dynamic.dal.dataobject.permission.DynamicPermissionDO;
import com.cheers.arch.module.dynamic.dal.mysql.permission.DynamicPermissionMapper;
import com.cheers.arch.module.dynamic.enums.ErrorCodeConstants;
import com.cheers.arch.module.dynamic.enums.permission.PermissionLevelEnum;
import com.cheers.arch.module.dynamic.service.permission.DynamicPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;

/**
 * 动态业务权限服务实现类
 */
@Service
@Slf4j
public class DynamicPermissionServiceImpl implements DynamicPermissionService {

    @Resource
    private DynamicPermissionMapper permissionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermission(DynamicPermissionDO permission) {
        permissionMapper.insert(permission);
        return permission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(DynamicPermissionDO permission) {
        // 校验存在
        validatePermissionExists(permission.getId());
        // 更新
        permissionMapper.updateById(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        // 校验存在
        validatePermissionExists(id);
        // 删除
        permissionMapper.deleteById(id);
    }

    @Override
    public DynamicPermissionDO getPermission(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public List<DynamicPermissionDO> getPermissionList(String modelCode, Integer type, Long userId, Long roleId, Long deptId) {
        return permissionMapper.selectList(new LambdaQueryWrapperX<DynamicPermissionDO>()
                .eq(DynamicPermissionDO::getModelCode, modelCode)
                .eqIfPresent(DynamicPermissionDO::getType, type)
                .eqIfPresent(DynamicPermissionDO::getUserId, userId)
                .eqIfPresent(DynamicPermissionDO::getRoleId, roleId)
                .eqIfPresent(DynamicPermissionDO::getDeptId, deptId)
                .eq(DynamicPermissionDO::getStatus, 1));
    }

    @Override
    public PageResult<DynamicPermissionDO> getPermissionPage(Integer pageNo, Integer pageSize, String modelCode,
                                                           Integer type, String target, Long userId, Long roleId, Long deptId) {
        Page<DynamicPermissionDO> page = permissionMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapperX<DynamicPermissionDO>()
                        .eq(DynamicPermissionDO::getModelCode, modelCode)
                        .eqIfPresent(DynamicPermissionDO::getType, type)
                        .eqIfPresent(DynamicPermissionDO::getTarget, target)
                        .eqIfPresent(DynamicPermissionDO::getUserId, userId)
                        .eqIfPresent(DynamicPermissionDO::getRoleId, roleId)
                        .eqIfPresent(DynamicPermissionDO::getDeptId, deptId)
                        .eq(DynamicPermissionDO::getStatus, 1));
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean hasPermission(String modelCode, Integer type, String target, Integer level) {
        // 获取当前用户
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId == null) {
            return false;
        }

        // 查询用户权限
        List<DynamicPermissionDO> permissions = getPermissionList(modelCode, type, loginUserId, null, null);
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }

        // 判断是否有足够的权限级别
        return permissions.stream()
                .filter(permission -> Objects.equals(permission.getTarget(), target))
                .anyMatch(permission -> permission.getLevel() >= level);
    }

    @Override
    public Set<String> getPermissionTargets(String modelCode, Integer type, Integer level) {
        // 获取当前用户
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId == null) {
            return Collections.emptySet();
        }

        // 查询用户权限
        List<DynamicPermissionDO> permissions = getPermissionList(modelCode, type, loginUserId, null, null);
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }

        // 获取有权限的目标
        Set<String> targets = new HashSet<>();
        permissions.stream()
                .filter(permission -> permission.getLevel() >= level)
                .forEach(permission -> targets.add(permission.getTarget()));
        return targets;
    }

    private void validatePermissionExists(Long id) {
        if (id == null) {
            return;
        }
        DynamicPermissionDO permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new ServiceException(ErrorCodeConstants.DYNAMIC_PERMISSION_NOT_EXISTS);
        }
    }
} 