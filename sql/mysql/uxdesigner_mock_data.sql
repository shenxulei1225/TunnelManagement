-- ----------------------------
-- UX Designer 模块丰富的模拟数据
-- ----------------------------

-- 清空现有数据
DELETE FROM `uxd_workspace_file`;
DELETE FROM `uxd_workspace_project`;

-- 重置自增ID
ALTER TABLE `uxd_workspace_project` AUTO_INCREMENT = 1;
ALTER TABLE `uxd_workspace_file` AUTO_INCREMENT = 1;

-- ----------------------------
-- 插入项目数据
-- ----------------------------
INSERT INTO `uxd_workspace_project` (`name`, `description`, `cover`, `type`, `status`, `user_id`, `team_id`, `starred`, `remark`, `creator`) VALUES 
('Universal X Designer 主项目', '品牌核心设计项目，体验一切，导出无界。包含主要的用户界面设计、品牌视觉系统和交互规范。', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=400&h=300&fit=crop', 'web', 1, 1, 1, 1, '核心设计项目，优先级最高', 'admin'),

('电商平台设计系统', '现代化电商平台的完整设计系统，包含购物流程、支付界面、商品展示等核心功能模块。', 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400&h=300&fit=crop', 'web', 1, 1, 1, 0, '电商项目设计规范', 'admin'),

('移动端设计规范', '移动端设计系统和组件库，支持iOS和Android平台，包含完整的UI组件和交互动效。', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=400&h=300&fit=crop', 'mobile', 1, 1, 1, 1, '移动端项目，跨平台设计', 'admin'),

('企业级管理后台', '面向B端用户的企业级管理系统界面设计，包含数据可视化、表单设计、权限管理等功能。', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=400&h=300&fit=crop', 'web', 1, 1, 1, 0, '企业级后台管理系统', 'admin'),

('社交媒体应用', '新一代社交媒体应用的界面设计，注重用户体验和社交互动，支持多媒体内容分享。', 'https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=400&h=300&fit=crop', 'mobile', 1, 1, 1, 1, '社交类应用设计', 'admin'),

('金融科技产品', '金融科技产品的用户界面设计，包含投资理财、数据分析、风险控制等核心功能模块。', 'https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=400&h=300&fit=crop', 'web', 1, 1, 1, 0, '金融产品设计，安全第一', 'admin'),

('教育平台界面', '在线教育平台的完整设计方案，支持课程管理、在线学习、师生互动等教育场景。', 'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=400&h=300&fit=crop', 'web', 1, 1, 1, 0, '教育类产品设计', 'admin'),

('智能家居控制', '智能家居控制系统的界面设计，支持多设备管理、场景模式、语音控制等智能化功能。', 'https://images.unsplash.com/photo-1558618047-3c8c76cd567c?w=400&h=300&fit=crop', 'mobile', 1, 1, 1, 1, 'IoT智能家居产品', 'admin');

-- ----------------------------
-- 插入文件数据
-- ----------------------------
INSERT INTO `uxd_workspace_file` (`name`, `type`, `thumbnail`, `content`, `category`, `starred`, `file_size`, `project_id`, `user_id`, `team_id`, `status`, `remark`, `creator`) VALUES 

-- Universal X Designer 主项目文件
('品牌首页设计', 'design', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"artboards":[{"name":"首页","width":1440,"height":900}]}', 'drafts', 1, 2048, 1, 1, 1, 1, '品牌主页设计，体现Experience Everything理念', 'admin'),

('用户旅程地图', 'figjam', 'https://images.unsplash.com/photo-1559028006-448665bd7c7f?w=300&h=200&fit=crop', '{"version":"1.0","type":"figjam","elements":[],"boards":[{"name":"用户旅程","type":"journey_map"}]}', 'drafts', 1, 1536, 1, 1, 1, 1, '用户体验旅程分析', 'admin'),

('设计系统规范', 'design', 'https://images.unsplash.com/photo-1586717799252-bd134ad00e26?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"components":["buttons","forms","navigation"]}', 'templates', 1, 3072, 1, 1, 1, 1, '完整的设计系统文档', 'admin'),

('交互原型', 'design', 'https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"prototype":true}', 'drafts', 0, 4096, 1, 1, 1, 1, '高保真交互原型', 'admin'),

-- 电商平台设计系统文件
('商品详情页', 'design', 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"pages":["product_detail"]}', 'drafts', 0, 2560, 2, 1, 1, 1, '电商商品详情页设计', 'admin'),

('购物车流程', 'figjam', 'https://images.unsplash.com/photo-1563013544-824ae1b704d3?w=300&h=200&fit=crop', '{"version":"1.0","type":"figjam","elements":[],"flow":"shopping_cart"}', 'drafts', 1, 1800, 2, 1, 1, 1, '购物车用户流程设计', 'admin'),

('支付界面', 'design', 'https://images.unsplash.com/photo-1563013544-824ae1b704d3?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"secure":true}', 'drafts', 0, 2200, 2, 1, 1, 1, '安全支付界面设计', 'admin'),

-- 移动端设计规范文件
('移动端组件库', 'template', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=300&h=200&fit=crop', '{"version":"1.0","type":"template","elements":[],"platform":"mobile"}', 'templates', 1, 3500, 3, 1, 1, 1, '移动端可重用组件库', 'admin'),

('应用图标设计', 'design', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"icons":true}', 'drafts', 1, 1200, 3, 1, 1, 1, 'iOS和Android应用图标', 'admin'),

('启动页设计', 'design', 'https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"splash":true}', 'drafts', 0, 800, 3, 1, 1, 1, '应用启动页面设计', 'admin'),

-- 企业级管理后台文件
('仪表板设计', 'design', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"dashboard":true}', 'drafts', 0, 3200, 4, 1, 1, 1, '管理后台仪表板界面', 'admin'),

('数据可视化', 'design', 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"charts":true}', 'drafts', 1, 2800, 4, 1, 1, 1, '数据图表和可视化组件', 'admin'),

('用户权限管理', 'design', 'https://images.unsplash.com/photo-1557804506-669a67965ba0?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"permissions":true}', 'drafts', 0, 2100, 4, 1, 1, 1, '权限管理界面设计', 'admin'),

-- 社交媒体应用文件
('动态时间线', 'design', 'https://images.unsplash.com/photo-1611162617474-5b21e879e113?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"timeline":true}', 'drafts', 1, 2600, 5, 1, 1, 1, '社交动态时间线界面', 'admin'),

('聊天界面', 'design', 'https://images.unsplash.com/photo-1577563908411-5077b6dc7624?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"chat":true}', 'drafts', 0, 1900, 5, 1, 1, 1, '即时聊天界面设计', 'admin'),

('个人资料页', 'design', 'https://images.unsplash.com/photo-1607706189992-eae578626c86?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"profile":true}', 'drafts', 1, 1700, 5, 1, 1, 1, '用户个人资料页面', 'admin'),

-- 金融科技产品文件
('投资理财界面', 'design', 'https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"investment":true}', 'drafts', 0, 3100, 6, 1, 1, 1, '投资理财产品界面', 'admin'),

('风险评估流程', 'figjam', 'https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=300&h=200&fit=crop', '{"version":"1.0","type":"figjam","elements":[],"risk_assessment":true}', 'drafts', 1, 2200, 6, 1, 1, 1, '风险评估流程设计', 'admin'),

-- 教育平台界面文件
('在线课程界面', 'design', 'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"course":true}', 'drafts', 0, 2400, 7, 1, 1, 1, '在线课程学习界面', 'admin'),

('师生互动设计', 'design', 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"interaction":true}', 'drafts', 1, 2000, 7, 1, 1, 1, '师生互动功能设计', 'admin'),

-- 智能家居控制文件
('设备控制面板', 'design', 'https://images.unsplash.com/photo-1558618047-3c8c76cd567c?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"iot_control":true}', 'drafts', 1, 2700, 8, 1, 1, 1, '智能设备控制界面', 'admin'),

('场景模式设计', 'design', 'https://images.unsplash.com/photo-1614730321146-b6fa6a46bcb4?w=300&h=200&fit=crop', '{"version":"1.0","type":"design","elements":[],"scene_mode":true}', 'drafts', 0, 1600, 8, 1, 1, 1, '智能场景模式界面', 'admin');

-- 更新统计信息
ANALYZE TABLE `uxd_workspace_project`;
ANALYZE TABLE `uxd_workspace_file`; 