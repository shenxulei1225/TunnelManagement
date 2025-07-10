package com.cheers.framework.security.core.filter;

import cn.hutool.core.util.StrUtil;
import com.cheers.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import com.cheers.framework.common.exception.ServiceException;
import com.cheers.framework.common.pojo.CommonResult;
import com.cheers.framework.security.config.SecurityProperties;
import com.cheers.framework.security.core.LoginUser;
import com.cheers.framework.security.core.util.SecurityFrameworkUtils;
import com.cheers.framework.web.core.handler.GlobalExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Token 过滤器，验证 token 的有效性
 * 验证通过后，获得 {@link LoginUser} 信息，并加入到 Spring Security 上下文
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final OAuth2TokenCommonApi oauth2TokenApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParam());
        if (StrUtil.isNotEmpty(token)) {
            try {
                // 1.1 基于 token 构建登录用户
                LoginUser loginUser = buildLoginUserByToken(token);
                // 1.2 设置当前用户
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                response.setStatus(result.getCode());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(String.valueOf(result));
                return;
            }
        }

        // 继续过滤链
        chain.doFilter(request, response);
    }

    private LoginUser buildLoginUserByToken(String token) {
        try {
            com.cheers.framework.common.biz.system.oauth2.LoginUser commonUser = oauth2TokenApi.getLoginUser(token);
            if (commonUser != null) {
                LoginUser user = new LoginUser();
                user.setId(commonUser.getId());
                user.setUserType(commonUser.getUserType());
                user.setTenantId(commonUser.getTenantId());
                user.setToken(token);
                return user;
            }
            return null;
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }

} 