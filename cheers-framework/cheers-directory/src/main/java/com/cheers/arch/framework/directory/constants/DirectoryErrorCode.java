package com.cheers.arch.framework.directory.constants;

import com.cheers.arch.framework.common.exception.ErrorCode;

/**
 * 目录相关错误码
 */
public interface DirectoryErrorCode {
    ErrorCode DIRECTORY_NOT_FOUND = new ErrorCode(1001001000, "目录不存在");
    ErrorCode DIRECTORY_CODE_DUPLICATE = new ErrorCode(1001001001, "目录编码重复");
    ErrorCode DIRECTORY_HAS_CHILDREN = new ErrorCode(1001001002, "目录下存在子目录，无法删除");
} 