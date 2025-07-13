-- 插入测试业务模型数据
-- 执行时间：2025-07-03

-- 插入系统类型的业务模型（不允许删除）
INSERT INTO `dynamic_business_model` (
    `tenant_id`, `code`, `name`, `description`, `table_name`, `model_type`, 
    `structure_type`, `storage_strategy`, `status`, `creator`
) VALUES (
    1, 'system_user', '系统用户模型', '系统内置的用户管理模型，不允许删除', 'dynamic_system_user', 0,
    1, 1, 1, 'admin'
);

-- 插入另一个系统类型的业务模型（不允许删除）
INSERT INTO `dynamic_business_model` (
    `tenant_id`, `code`, `name`, `description`, `table_name`, `model_type`, 
    `structure_type`, `storage_strategy`, `status`, `creator`
) VALUES (
    1, 'system_config', '系统配置模型', '系统内置的配置管理模型，不允许删除', 'dynamic_system_config', 0,
    2, 1, 1, 'admin'
);

-- 插入自定义类型的业务模型（允许删除）
INSERT INTO `dynamic_business_model` (
    `tenant_id`, `code`, `name`, `description`, `table_name`, `model_type`, 
    `structure_type`, `storage_strategy`, `status`, `creator`
) VALUES (
    1, 'custom_project', '项目管理系统', '自定义的项目管理模型，可以删除', 'dynamic_custom_project', 1,
    1, 1, 1, 'admin'
);

-- 插入另一个自定义类型的业务模型
INSERT INTO `dynamic_business_model` (
    `tenant_id`, `code`, `name`, `description`, `table_name`, `model_type`, 
    `structure_type`, `storage_strategy`, `status`, `creator`
) VALUES (
    1, 'custom_task', '任务管理模型', '自定义的任务管理模型，可以删除', 'dynamic_custom_task', 1,
    2, 1, 0, 'admin'
); 