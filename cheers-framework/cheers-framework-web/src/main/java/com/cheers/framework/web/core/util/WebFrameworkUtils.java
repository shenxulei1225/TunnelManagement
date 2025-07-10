package com.cheers.framework.web.core.util;

import com.cheers.framework.web.config.WebProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Web 框架工具类
 */
public class WebFrameworkUtils {

    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";
    private static final String REQUEST_ATTRIBUTE_VISIT_TENANT_ID = "visit_tenant_id";

    public static final String HEADER_TENANT_ID = "tenant-id";

    public static final Integer INVALID_REQUEST_PARAM_CODE = 400;
    public static final Integer UNAUTHORIZED_CODE = 401;
    public static final Integer FORBIDDEN_CODE = 403;
    public static final Integer NOT_FOUND_CODE = 404;
    public static final Integer METHOD_NOT_ALLOWED_CODE = 405;
    public static final Integer INTERNAL_SERVER_ERROR_CODE = 500;

    private static WebProperties properties;

    public WebFrameworkUtils(WebProperties webProperties) {
        WebFrameworkUtils.properties = webProperties;
    }

    /**
     * 获得租户编号，从 header 中
     */
    public static Long getTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        return Objects.nonNull(tenantId) ? Long.valueOf(tenantId) : null;
    }

    public static void setLoginUserId(ServletRequest request, Long userId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, userId);
    }

    /**
     * 获得当前用户的编号，从请求中
     * 注意：该方法仅限于 framework 框架使用！！！
     */
    public static Long getLoginUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID);
    }

    public static void setLoginUserType(ServletRequest request, Integer userType) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE, userType);
    }

    /**
     * 获得当前用户的类型，从请求中
     * 注意：该方法仅限于 framework 框架使用！！！
     */
    public static Integer getLoginUserType(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Integer) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE);
    }

    public static Long getLoginUserIdFromRequest() {
        HttpServletRequest request = getRequest();
        return getLoginUserId(request);
    }

    public static Integer getLoginUserTypeFromRequest() {
        HttpServletRequest request = getRequest();
        return getLoginUserType(request);
    }

    public static Long getTenantIdFromRequest() {
        HttpServletRequest request = getRequest();
        return getTenantId(request);
    }

    /**
     * 设置当前访问的租户编号
     */
    public static void setVisitTenantId(ServletRequest request, Long tenantId) {
        request.setAttribute(REQUEST_ATTRIBUTE_VISIT_TENANT_ID, tenantId);
    }

    /**
     * 获得当前访问的租户编号
     */
    public static Long getVisitTenantId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_VISIT_TENANT_ID);
    }

    /**
     * 获得当前访问的租户编号
     */
    public static Long getVisitTenantIdFromRequest() {
        HttpServletRequest request = getRequest();
        return getVisitTenantId(request);
    }

    private static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

} 