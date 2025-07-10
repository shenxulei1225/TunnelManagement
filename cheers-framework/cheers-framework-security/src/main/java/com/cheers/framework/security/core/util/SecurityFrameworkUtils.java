package com.cheers.framework.security.core.util;

import com.cheers.framework.security.core.LoginUser;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Security 框架工具类
 */
public class SecurityFrameworkUtils {

    private SecurityFrameworkUtils() {}

    /**
     * 从请求中，获得认证信息
     *
     * @param request 请求
     * @param header 认证头
     * @param param 认证参数
     * @return 认证信息
     */
    public static String obtainAuthorization(HttpServletRequest request,
                                           String header, String param) {
        // 1. 获得 Token
        String token = request.getHeader(header);
        if (!StringUtils.hasText(token)) {
            token = request.getParameter(param);
        }
        return token;
    }

    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    @Nullable
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication.getPrincipal() : null;
    }

    /**
     * 获得当前用户的编号，从上下文中
     *
     * @return 用户编号
     */
    @Nullable
    public static Long getLoginUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request 请求
     */
    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // 创建 Authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 设置到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 清空当前用户
     */
    public static void clearLoginUser() {
        SecurityContextHolder.clearContext();
    }

} 