package com.cheers.framework.security.core;

import lombok.Data;

import java.util.List;

/**
 * 登录用户信息
 */
@Data
public class LoginUser {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 角色编号列表
     */
    private List<String> roleIds;

    /**
     * 部门编号
     */
    private Long deptId;

    /**
     * 岗位编号
     */
    private Long postId;

    /**
     * 访问租户编号
     */
    private Long visitTenantId;

} 