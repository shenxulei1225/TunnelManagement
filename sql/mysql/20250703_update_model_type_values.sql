-- 更新model_type字段值
-- 执行时间：2025-07-03

-- 将原来的系统类型（值为2）更新为新的系统类型（值为0）
UPDATE `dynamic_business_model` 
SET `model_type` = 0 
WHERE `model_type` = 2;

-- 确保所有记录都有正确的类型值
UPDATE `dynamic_business_model` 
SET `model_type` = 1 
WHERE `model_type` IS NULL OR `model_type` NOT IN (0, 1); 