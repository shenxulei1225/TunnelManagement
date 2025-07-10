package com.cheers.framework.security.core.handler;

import com.cheers.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.cheers.framework.common.pojo.CommonResult;
import com.cheers.framework.common.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 访问一个需要认证的 URL 资源，已经认证（登录）但是没有权限的情况下，返回 403 错误码
 */
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        log.warn("[handle][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(),
                request.getUserPrincipal().getName(), e);
        // 返回 403
        ServletUtils.writeJSON(response, CommonResult.error(GlobalErrorCodeConstants.FORBIDDEN));
    }

} 