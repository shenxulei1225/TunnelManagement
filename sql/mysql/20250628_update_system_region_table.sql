-- 更新 system_region 表结构
-- 移除不需要的字段：leader_user_id, phone, email
-- 确保 category_id 字段存在

-- 删除不需要的字段（如果存在）
ALTER TABLE system_region DROP COLUMN IF EXISTS leader_user_id;
ALTER TABLE system_region DROP COLUMN IF EXISTS phone;
ALTER TABLE system_region DROP COLUMN IF EXISTS email;

-- 添加 category_id 字段（如果不存在）
ALTER TABLE system_region ADD COLUMN IF NOT EXISTS category_id bigint DEFAULT NULL COMMENT '分类ID' AFTER sort; 