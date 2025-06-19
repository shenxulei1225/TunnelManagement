-- 添加 extra_attrs 字段用于存储区域的动态扩展属性(JSON)
ALTER TABLE `system_region`
    ADD COLUMN `extra_attrs` JSON NULL COMMENT '扩展属性';
