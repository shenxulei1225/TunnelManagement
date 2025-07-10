package com.cheers.framework.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务逻辑异常 Exception
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {

    /**
     * 业务错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message) {
        this.code = GlobalErrorCodeConstants.SERVER_ERROR.getCode();
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.code = GlobalErrorCodeConstants.SERVER_ERROR.getCode();
        this.message = message;
    }

    public static class GlobalErrorCodeConstants {
        public static final ErrorCode SERVER_ERROR = new ErrorCode(500, "系统异常");
    }

} 