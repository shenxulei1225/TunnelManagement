package com.cheers.framework.security.core.service;

import cn.hutool.core.collection.CollUtil;
import com.cheers.framework.common.biz.system.permission.PermissionCommonApi;
import com.cheers.framework.security.core.LoginUser;
import com.cheers.framework.security.core.util.SecurityFrameworkUtils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Security 框架 Service 实现类
 */
@RequiredArgsConstructor
public class SecurityFrameworkServiceImpl implements SecurityFrameworkService {

    private final PermissionCommonApi permissionApi;

    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    public boolean hasAnyPermissions(String... permissions) {
        // 如果为空，说明已经有权限
        if (CollUtil.isEmpty(Arrays.asList(permissions))) {
            return true;
        }

        // 获得当前登录的用户。如果为空，说明没有权限
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser == null) {
            return false;
        }
        // 如果是超级管理员，直接通过
        if (permissionApi.isAdmin(loginUser.getId())) {
            return true;
        }

        // 判断是否有权限
        return permissionApi.hasAnyPermissions(loginUser.getId(), permissions);
    }

    @Override
    public boolean hasRole(String role) {
        return hasAnyRoles(role);
    }

    @Override
    public boolean hasAnyRoles(String... roles) {
        // 如果为空，说明已经有权限
        if (CollUtil.isEmpty(Arrays.asList(roles))) {
            return true;
        }

        // 获得当前登录的用户。如果为空，说明没有权限
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser == null) {
            return false;
        }
        // 如果是超级管理员，直接通过
        if (permissionApi.isAdmin(loginUser.getId())) {
            return true;
        }

        // 判断是否有角色
        return permissionApi.hasAnyRoles(loginUser.getId(), roles);
    }

    @Override
    public LoginUser getLoginUser() {
        return SecurityFrameworkUtils.getLoginUser();
    }

    @Override
    public List<String> getRoleIds() {
        return SecurityFrameworkUtils.getLoginUser().getRoleIds();
    }

} 