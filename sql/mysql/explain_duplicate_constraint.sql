-- 详细分析约束条件和重复记录

-- 第一步：查看表的完整约束定义
SELECT '=== 第一步：查看表的约束定义 ===' as step;
SHOW CREATE TABLE system_field_hierarchy_rel;

-- 第二步：查看具体的唯一约束包含哪些字段
SELECT '=== 第二步：约束uk_field_hierarchy包含的字段 ===' as step;
SELECT 
    COLUMN_NAME as '约束字段',
    ORDINAL_POSITION as '字段顺序',
    '这些字段组合不能重复' as '说明'
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'system_field_hierarchy_rel' 
    AND CONSTRAINT_NAME = 'uk_field_hierarchy'
ORDER BY ORDINAL_POSITION;

-- 第三步：查看错误记录的详细信息 (50-8-1)
SELECT '=== 第三步：错误中提到的记录详情 ===' as step;
SELECT 
    '错误信息分析' as type,
    'Duplicate entry 50-8-1' as error_message,
    '字段50 + 分组8 + 租户1 = 重复组合' as meaning;

-- 查看这个组合的所有记录
SELECT 
    id as '记录ID',
    field_id as '字段ID',
    hierarchy_group_id as '分组ID',
    tenant_id as '租户ID',
    CONCAT(field_id, '-', hierarchy_group_id, '-', tenant_id) as '组合标识',
    sort as '排序',
    create_time as '创建时间',
    update_time as '更新时间',
    creator as '创建者',
    updater as '更新者',
    deleted as '删除标记',
    CASE 
        WHEN deleted = 0 THEN '有效记录'
        WHEN deleted = 1 THEN '已删除'
        ELSE '未知状态'
    END as '记录状态',
    CASE
        WHEN create_time = (
            SELECT MIN(create_time) 
            FROM system_field_hierarchy_rel sub
            WHERE sub.field_id = system_field_hierarchy_rel.field_id 
                AND sub.hierarchy_group_id = system_field_hierarchy_rel.hierarchy_group_id
                AND sub.tenant_id = system_field_hierarchy_rel.tenant_id
        ) THEN '👑 最早的记录'
        WHEN create_time = (
            SELECT MAX(create_time) 
            FROM system_field_hierarchy_rel sub
            WHERE sub.field_id = system_field_hierarchy_rel.field_id 
                AND sub.hierarchy_group_id = system_field_hierarchy_rel.hierarchy_group_id
                AND sub.tenant_id = system_field_hierarchy_rel.tenant_id
        ) THEN '🆕 最新的记录'
        ELSE '📄 中间记录'
    END as '记录类型'
FROM system_field_hierarchy_rel 
WHERE field_id = 50 
    AND hierarchy_group_id = 8 
    AND tenant_id = 1
ORDER BY create_time;

-- 第四步：分析这个字段的所有分组关联
SELECT '=== 第四步：字段50的所有分组关联情况 ===' as step;
SELECT 
    field_id as '字段ID',
    hierarchy_group_id as '分组ID',
    tenant_id as '租户ID',
    COUNT(*) as '该组合记录数',
    GROUP_CONCAT(id ORDER BY create_time) as '记录ID列表',
    GROUP_CONCAT(deleted ORDER BY create_time) as '删除状态列表',
    MIN(create_time) as '最早时间',
    MAX(create_time) as '最晚时间',
    CASE 
        WHEN COUNT(*) > 1 THEN '❌ 重复组合'
        ELSE '✅ 正常组合'
    END as '状态'
FROM system_field_hierarchy_rel 
WHERE field_id = 50
GROUP BY field_id, hierarchy_group_id, tenant_id
ORDER BY hierarchy_group_id;

-- 第五步：显示约束冲突的原理
SELECT '=== 第五步：约束冲突原理解释 ===' as step;
SELECT 
    '唯一约束uk_field_hierarchy的作用' as explanation,
    '防止同一个(field_id, hierarchy_group_id, tenant_id)组合出现多次' as purpose,
    '当尝试插入已存在的组合时就会报DuplicateKeyException' as when_error_occurs;

-- 第六步：查看所有重复的组合（前10个）
SELECT '=== 第六步：数据库中所有重复的组合 ===' as step;
SELECT 
    field_id as '字段ID',
    hierarchy_group_id as '分组ID',
    tenant_id as '租户ID',
    CONCAT(field_id, '-', hierarchy_group_id, '-', tenant_id) as '重复组合',
    COUNT(*) as '重复次数',
    GROUP_CONCAT(id ORDER BY create_time) as '所有记录ID',
    GROUP_CONCAT(CASE WHEN deleted=0 THEN id END ORDER BY create_time) as '有效记录ID',
    GROUP_CONCAT(CASE WHEN deleted=1 THEN id END ORDER BY create_time) as '已删除记录ID',
    MIN(create_time) as '最早创建',
    MAX(create_time) as '最晚创建'
FROM system_field_hierarchy_rel 
GROUP BY field_id, hierarchy_group_id, tenant_id
HAVING COUNT(*) > 1
ORDER BY COUNT(*) DESC, field_id, hierarchy_group_id
LIMIT 10;

-- 第七步：检查deleted字段是否在约束中
SELECT '=== 第七步：检查deleted字段是否影响约束 ===' as step;
SELECT 
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM information_schema.KEY_COLUMN_USAGE 
            WHERE TABLE_SCHEMA = DATABASE() 
                AND TABLE_NAME = 'system_field_hierarchy_rel' 
                AND CONSTRAINT_NAME = 'uk_field_hierarchy'
                AND COLUMN_NAME = 'deleted'
        ) THEN 'deleted字段包含在约束中，软删除记录不冲突'
        ELSE 'deleted字段不在约束中，软删除记录仍会冲突'
    END as '删除字段影响分析',
    '这决定了软删除的记录是否还会参与唯一性检查' as '重要性说明';

-- 第八步：具体的重复判断示例
SELECT '=== 第八步：重复判断示例说明 ===' as step;
SELECT 
    '示例：要插入 (field_id=50, hierarchy_group_id=8, tenant_id=1)' as scenario,
    '检查：数据库中是否已存在这个组合' as check_process,
    '结果：如果存在就报错 DuplicateKeyException' as result,
    '解决：要么删除旧记录，要么修改新记录的组合' as solution; 