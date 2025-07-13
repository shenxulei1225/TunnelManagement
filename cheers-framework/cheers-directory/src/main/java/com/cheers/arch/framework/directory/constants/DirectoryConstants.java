package com.cheers.arch.framework.directory.constants;

/**
 * 目录常量
 */
public interface DirectoryConstants {

    /**
     * 业务类型常量
     */
    interface BusinessType {
        /**
         * 动态业务模型
         */
        String DYNAMIC_MODEL = "dynamic_model";

        /**
         * 文件管理
         */
        String FILE_MANAGEMENT = "file_management";

        /**
         * 资产管理
         */
        String ASSET_MANAGEMENT = "asset_management";

        /**
         * 设备管理
         */
        String DEVICE_MANAGEMENT = "device_management";

        /**
         * 项目管理
         */
        String PROJECT_MANAGEMENT = "project_management";

        /**
         * 人员管理
         */
        String PERSONNEL_MANAGEMENT = "personnel_management";

        /**
         * 财务管理
         */
        String FINANCE_MANAGEMENT = "finance_management";
    }

    /**
     * 状态常量
     */
    interface Status {
        /**
         * 禁用
         */
        Integer DISABLED = 0;

        /**
         * 启用
         */
        Integer ENABLED = 1;
    }
} 