package com.cheers.framework.common.pojo;

import com.cheers.framework.common.exception.ErrorCode;
import com.cheers.framework.common.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通用返回
 *
 * @param <T> 数据泛型
 */
@Data
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误提示，用户可阅读
     */
    private String msg;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     *
     * @param result 传入的 result 对象
     * @param <T>    返回的泛型
     * @return 新的 result 对象
     */
    public static <T> CommonResult<T> error(CommonResult<?> result) {
        return error(result.getCode(), result.getMsg());
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode()),
                "code 必须是错误的！");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = "";
        return result;
    }

    public static <T> CommonResult<T> success() {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = null;
        result.msg = "";
        return result;
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(code, msg);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     */
    @JsonIgnore // 避免 jackson 序列化
    public T getCheckedData() {
        checkError();
        return data;
    }

    public static class GlobalErrorCodeConstants {

        /**
         * 成功
         */
        public static final ErrorCode SUCCESS = new ErrorCode(0, "成功");
        /**
         * 客户端错误
         */
        public static final ErrorCode BAD_REQUEST = new ErrorCode(400, "请求参数不正确");
        /**
         * 访问权限不足
         */
        public static final ErrorCode FORBIDDEN = new ErrorCode(403, "没有该操作权限");
        /**
         * 未授权
         */
        public static final ErrorCode UNAUTHORIZED = new ErrorCode(401, "账号未登录");
        /**
         * 服务器异常
         */
        public static final ErrorCode SERVER_ERROR = new ErrorCode(500, "系统异常");

    }

} 