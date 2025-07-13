package com.cheers.arch.module.dynamic.enums;

import com.cheers.arch.framework.common.exception.ErrorCode;

/**
 * 动态业务模块错误码枚举类
 */
public interface ErrorCodeConstants {

    ErrorCode FIELD_DEFINITION_NOT_EXISTS = new ErrorCode(1002001000, "字段定义不存在");
    ErrorCode FIELD_DEFINITION_NAME_DUPLICATE = new ErrorCode(1002001001, "字段定义名称重复");
    ErrorCode FIELD_DEFINITION_CODE_DUPLICATE = new ErrorCode(1002001002, "字段定义编码重复");
    ErrorCode FIELD_DEFINITION_INVALID_TYPE = new ErrorCode(1002001003, "字段定义类型无效");
    ErrorCode FIELD_DEFINITION_INVALID_CONFIG = new ErrorCode(1002001004, "字段定义配置无效");

    ErrorCode BUSINESS_MODEL_NOT_EXISTS = new ErrorCode(1002002000, "业务模型不存在");
    ErrorCode BUSINESS_MODEL_NAME_DUPLICATE = new ErrorCode(1002002001, "业务模型名称重复");
    ErrorCode BUSINESS_MODEL_CODE_DUPLICATE = new ErrorCode(1002002002, "业务模型编码重复");
    ErrorCode BUSINESS_MODEL_INVALID_STATUS = new ErrorCode(1002002003, "业务模型状态无效");
    ErrorCode BUSINESS_MODEL_CODE_EMPTY = new ErrorCode(1002002004, "业务模型编码不能为空");
    ErrorCode BUSINESS_MODEL_CODE_INVALID = new ErrorCode(1002002005, "业务模型编码格式无效，只能包含字母、数字、下划线，且必须以字母开头");
    ErrorCode BUSINESS_MODEL_TABLE_NAME_INVALID = new ErrorCode(1002002006, "表名格式无效，必须以dynamic_开头");
    ErrorCode BUSINESS_MODEL_READONLY_CANNOT_DELETE = new ErrorCode(1002002007, "只读的业务模型不允许删除");

    ErrorCode DYNAMIC_PERMISSION_NOT_EXISTS = new ErrorCode(1002003000, "动态权限不存在");
    ErrorCode DYNAMIC_PERMISSION_DUPLICATE = new ErrorCode(1002003001, "动态权限重复");
    ErrorCode DYNAMIC_PERMISSION_INVALID_LEVEL = new ErrorCode(1002003002, "动态权限级别无效");

    ErrorCode TABLE_NAME_INVALID = new ErrorCode(1002004000, "表名无效");
    ErrorCode TABLE_NOT_EXISTS = new ErrorCode(1002004001, "表不存在");
    ErrorCode TABLE_ALREADY_EXISTS = new ErrorCode(1002004002, "表已存在");
    ErrorCode TABLE_OPERATION_FAILED = new ErrorCode(1002004003, "表操作失败");
    ErrorCode TABLE_BACKUP_FAILED = new ErrorCode(1002004004, "表备份失败");
    ErrorCode TABLE_RESTORE_FAILED = new ErrorCode(1002004005, "表恢复失败");
    ErrorCode TABLE_MIGRATION_FAILED = new ErrorCode(1002004006, "表迁移失败");
    ErrorCode TABLE_SCHEMA_INVALID = new ErrorCode(1002004007, "表结构无效");

    ErrorCode DIRECTORY_NOT_EXISTS = new ErrorCode(1002005000, "目录不存在");
    ErrorCode DIRECTORY_NAME_DUPLICATE = new ErrorCode(1002005001, "目录名称重复");
    ErrorCode DIRECTORY_CODE_DUPLICATE = new ErrorCode(1002005002, "目录编码重复");
    ErrorCode DIRECTORY_HAS_CHILDREN = new ErrorCode(1002005003, "目录下存在子目录，无法删除");
    ErrorCode DIRECTORY_INVALID_PARENT = new ErrorCode(1002005004, "父目录无效");
} 