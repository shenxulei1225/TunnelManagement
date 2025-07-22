-- 模板生成器菜单配置
-- 创建时间：2025-01-21
-- 说明：为模板生成器功能创建菜单配置

-- 插入模板生成器主菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '模板生成器', '', 1, 2, 
    (SELECT id FROM system_menu WHERE name = '动态业务' AND parent_id = 0 LIMIT 1),
    'template-generator', 'ep:magic-stick', NULL, 0, 1, 0, 0, 
    '1', NOW(), '1', NOW(), 0
);

-- 插入模板配置子菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '模板配置', '', 2, 1, 
    (SELECT id FROM system_menu WHERE name = '模板生成器' AND type = 1 LIMIT 1),
    'configure', 'ep:setting', 'dynamic/template-generator/configure', 0, 1, 0, 0, 
    '1', NOW(), '1', NOW(), 0
);

-- 插入生成历史子菜单
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES (
    '生成历史', '', 2, 2, 
    (SELECT id FROM system_menu WHERE name = '模板生成器' AND type = 1 LIMIT 1),
    'history', 'ep:clock', 'dynamic/template-generator/history', 0, 1, 0, 0, 
    '1', NOW(), '1', NOW(), 0
);

-- 为模板生成器添加按钮权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES 
-- 模板选择按钮
('模板生成器查询', 'dynamic:template-generator:query', 3, 1, 
 (SELECT id FROM system_menu WHERE name = '模板生成器' AND type = 1 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 页面生成按钮
('页面生成', 'dynamic:template-generator:generate', 3, 2, 
 (SELECT id FROM system_menu WHERE name = '模板生成器' AND type = 1 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 配置管理按钮
('配置管理', 'dynamic:template-generator:config', 3, 3, 
 (SELECT id FROM system_menu WHERE name = '模板生成器' AND type = 1 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 历史查看按钮
('历史查看', 'dynamic:template-generator:history', 3, 4, 
 (SELECT id FROM system_menu WHERE name = '模板生成器' AND type = 1 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0);

-- 为模板配置页面添加按钮权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES 
-- 配置保存按钮
('配置保存', 'dynamic:template-config:save', 3, 1, 
 (SELECT id FROM system_menu WHERE name = '模板配置' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 配置验证按钮
('配置验证', 'dynamic:template-config:validate', 3, 2, 
 (SELECT id FROM system_menu WHERE name = '模板配置' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 预览生成按钮
('预览生成', 'dynamic:template-config:preview', 3, 3, 
 (SELECT id FROM system_menu WHERE name = '模板配置' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0);

-- 为生成历史页面添加按钮权限
INSERT INTO system_menu (
    name, permission, type, sort, parent_id, path, icon, component, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted
) VALUES 
-- 历史查询按钮
('历史查询', 'dynamic:generation-history:query', 3, 1, 
 (SELECT id FROM system_menu WHERE name = '生成历史' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 重新生成按钮
('重新生成', 'dynamic:generation-history:regenerate', 3, 2, 
 (SELECT id FROM system_menu WHERE name = '生成历史' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 删除页面按钮
('删除页面', 'dynamic:generation-history:delete', 3, 3, 
 (SELECT id FROM system_menu WHERE name = '生成历史' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0),

-- 导出代码按钮
('导出代码', 'dynamic:generation-history:export', 3, 4, 
 (SELECT id FROM system_menu WHERE name = '生成历史' AND type = 2 LIMIT 1),
 '', '', NULL, 0, 1, 0, 0, '1', NOW(), '1', NOW(), 0);

-- 更新菜单排序
UPDATE system_menu SET sort = 3 WHERE name = '智能推荐' AND type = 1;
UPDATE system_menu SET sort = 4 WHERE name = '智能推荐' AND type = 2; 