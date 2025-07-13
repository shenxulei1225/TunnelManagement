-- 为业务模型表添加只读字段
-- 执行时间：2025-07-03
-- 说明：添加readonly字段来控制业务模型是否可删除，与status字段独立

ALTER TABLE `dynamic_business_model` 
ADD COLUMN `readonly` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否只读（0:可编辑，1:只读）' AFTER `status`;

-- 更新现有数据，系统类型的模型设置为只读
UPDATE `dynamic_business_model` 
SET `readonly` = b'1' 
WHERE `model_type` = 0;

-- 自定义类型的模型默认可编辑
UPDATE `dynamic_business_model` 
SET `readonly` = b'0' 
WHERE `model_type` = 1;

-- 添加索引以提高查询性能
ALTER TABLE `dynamic_business_model` 
ADD INDEX `idx_readonly` (`readonly`);

-- 验证数据
SELECT 
    id,
    name,
    code,
    model_type,
    status,
    readonly,
    CASE 
        WHEN readonly = 1 THEN '只读'
        ELSE '可编辑'
    END as readonly_desc
FROM `dynamic_business_model` 
ORDER BY id; 