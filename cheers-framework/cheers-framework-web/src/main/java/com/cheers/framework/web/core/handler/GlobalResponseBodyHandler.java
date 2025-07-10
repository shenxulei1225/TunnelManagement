package com.cheers.framework.web.core.handler;

import com.cheers.framework.common.pojo.CommonResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应结果（ResponseBody）处理器
 *
 * 不同于 ResponseBodyAdvice 接口，只拦截 Controller 返回结果
 */
@ControllerAdvice
public class GlobalResponseBodyHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 如果已经是 CommonResult 类型，则直接返回
        return !returnType.getParameterType().equals(CommonResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                MediaType selectedContentType, Class selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
        // 如果为空，则返回成功的空对象
        if (body == null) {
            return CommonResult.success();
        }
        // 如果已经是 CommonResult 类型，则直接返回
        if (body instanceof CommonResult) {
            return body;
        }
        // 包装成 CommonResult 对象
        return CommonResult.success(body);
    }

} 