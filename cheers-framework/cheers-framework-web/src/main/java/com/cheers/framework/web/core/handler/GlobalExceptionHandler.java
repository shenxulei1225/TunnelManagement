package com.cheers.framework.web.core.handler;

import com.cheers.framework.common.exception.ServiceException;
import com.cheers.framework.common.pojo.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.cheers.framework.common.exception.enums.GlobalErrorCodeConstants.*;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理所有异常，主要是记录异常日志
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleException(Exception ex, HttpServletRequest request) {
        log.error("[handleException]", ex);
        return CommonResult.error(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 处理所有异常，主要是记录异常日志
     */
    public CommonResult<?> allExceptionHandler(HttpServletRequest request, Throwable ex) {
        log.error("[allExceptionHandler]", ex);
        return CommonResult.error(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 处理 SpringMVC 请求参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("[handleMissingServletRequestParameterException]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("[handleMethodArgumentTypeMismatchException]", ex);
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[handleMethodArgumentNotValidException]", ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null; // 断言，避免告警
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> handleBindException(BindException ex) {
        log.warn("[handleBindException]", ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", fieldError.getDefaultMessage()));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("[handleConstraintViolationException]", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return CommonResult.error(BAD_REQUEST.getCode(), String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult<?> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("[handleNoHandlerFoundException]", ex);
        return CommonResult.error(NOT_FOUND.getCode(), String.format("请求地址不存在:%s", ex.getRequestURL()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("[handleHttpRequestMethodNotSupportedException]", ex);
        return CommonResult.error(METHOD_NOT_ALLOWED.getCode(), String.format("请求方法不正确:%s", ex.getMessage()));
    }

    /**
     * 处理业务异常 ServiceException
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> handleServiceException(ServiceException ex) {
        log.info("[handleServiceException]", ex);
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

} 