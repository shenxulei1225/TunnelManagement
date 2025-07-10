package com.cheers.framework.security.core.service;

import com.cheers.framework.security.core.LoginUser;

import java.util.List;

/**
 * Security 框架 Service 接口，定义权限相关的校验操作
 */
public interface SecurityFrameworkService {

    /**
     * 判断是否有权限
     *
     * @param permission 权限
     * @return 是否
     */
    boolean hasPermission(String permission);

    /**
     * 判断是否有权限，任一一个即可
     *
     * @param permissions 权限列表
     * @return 是否
     */
    boolean hasAnyPermissions(String... permissions);

    /**
     * 判断是否有角色
     *
     * @param role 角色
     * @return 是否
     */
    boolean hasRole(String role);

    /**
     * 判断是否有角色，任一一个即可
     *
     * @param roles 角色列表
     * @return 是否
     */
    boolean hasAnyRoles(String... roles);

    /**
     * 获得当前用户
     *
     * @return 当前用户
     */
    LoginUser getLoginUser();

    /**
     * 获得当前用户的角色编号数组
     *
     * @return 角色编号数组
     */
    List<String> getRoleIds();

} 