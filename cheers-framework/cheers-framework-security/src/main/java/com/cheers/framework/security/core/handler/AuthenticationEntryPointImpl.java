package com.cheers.framework.security.core.handler;

import com.cheers.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.cheers.framework.common.pojo.CommonResult;
import com.cheers.framework.common.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 认证失败处理类 Bean
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), e);
        // 返回 401
        ServletUtils.writeJSON(response, CommonResult.error(GlobalErrorCodeConstants.UNAUTHORIZED));
    }

} 