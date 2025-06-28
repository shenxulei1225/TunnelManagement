-- ----------------------------
-- UX Designer 模块数据库表结构 (PostgreSQL)
-- ----------------------------

-- ----------------------------
-- 工作台文件表
-- ----------------------------
DROP TABLE IF EXISTS uxd_workspace_file;
CREATE TABLE uxd_workspace_file (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(50) NOT NULL,
  thumbnail VARCHAR(500),
  content TEXT,
  category VARCHAR(50) NOT NULL,
  starred BOOLEAN NOT NULL DEFAULT FALSE,
  file_size BIGINT,
  project_id BIGINT,
  user_id BIGINT NOT NULL,
  team_id BIGINT,
  status SMALLINT NOT NULL DEFAULT 1,
  remark VARCHAR(500),
  creator VARCHAR(64) DEFAULT '',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater VARCHAR(64) DEFAULT '',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  tenant_id BIGINT NOT NULL DEFAULT 0
);

COMMENT ON TABLE uxd_workspace_file IS '工作台文件表';
COMMENT ON COLUMN uxd_workspace_file.id IS '文件ID';
COMMENT ON COLUMN uxd_workspace_file.name IS '文件名称';
COMMENT ON COLUMN uxd_workspace_file.type IS '文件类型';
COMMENT ON COLUMN uxd_workspace_file.thumbnail IS '缩略图URL';
COMMENT ON COLUMN uxd_workspace_file.content IS '文件内容';
COMMENT ON COLUMN uxd_workspace_file.category IS '文件分类';
COMMENT ON COLUMN uxd_workspace_file.starred IS '是否收藏';
COMMENT ON COLUMN uxd_workspace_file.file_size IS '文件大小';
COMMENT ON COLUMN uxd_workspace_file.project_id IS '所属项目ID';
COMMENT ON COLUMN uxd_workspace_file.user_id IS '用户ID';
COMMENT ON COLUMN uxd_workspace_file.team_id IS '团队ID';
COMMENT ON COLUMN uxd_workspace_file.status IS '状态（0=回收站 1=正常）';
COMMENT ON COLUMN uxd_workspace_file.remark IS '备注';

-- 创建索引
CREATE INDEX idx_uxd_workspace_file_user_id ON uxd_workspace_file (user_id);
CREATE INDEX idx_uxd_workspace_file_team_id ON uxd_workspace_file (team_id);
CREATE INDEX idx_uxd_workspace_file_project_id ON uxd_workspace_file (project_id);
CREATE INDEX idx_uxd_workspace_file_category ON uxd_workspace_file (category);
CREATE INDEX idx_uxd_workspace_file_status ON uxd_workspace_file (status);
CREATE INDEX idx_uxd_workspace_file_create_time ON uxd_workspace_file (create_time);

-- ----------------------------
-- 工作台项目表
-- ----------------------------
DROP TABLE IF EXISTS uxd_workspace_project;
CREATE TABLE uxd_workspace_project (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  cover VARCHAR(500),
  type VARCHAR(50) NOT NULL,
  status SMALLINT NOT NULL DEFAULT 1,
  user_id BIGINT NOT NULL,
  team_id BIGINT,
  starred BOOLEAN NOT NULL DEFAULT FALSE,
  remark VARCHAR(500),
  creator VARCHAR(64) DEFAULT '',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updater VARCHAR(64) DEFAULT '',
  update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  deleted BOOLEAN NOT NULL DEFAULT FALSE,
  tenant_id BIGINT NOT NULL DEFAULT 0
);

COMMENT ON TABLE uxd_workspace_project IS '工作台项目表';
COMMENT ON COLUMN uxd_workspace_project.id IS '项目ID';
COMMENT ON COLUMN uxd_workspace_project.name IS '项目名称';
COMMENT ON COLUMN uxd_workspace_project.description IS '项目描述';
COMMENT ON COLUMN uxd_workspace_project.cover IS '项目封面';
COMMENT ON COLUMN uxd_workspace_project.type IS '项目类型';
COMMENT ON COLUMN uxd_workspace_project.status IS '项目状态（0=归档 1=正常）';
COMMENT ON COLUMN uxd_workspace_project.user_id IS '用户ID';
COMMENT ON COLUMN uxd_workspace_project.team_id IS '团队ID';
COMMENT ON COLUMN uxd_workspace_project.starred IS '是否收藏';
COMMENT ON COLUMN uxd_workspace_project.remark IS '备注';

-- 创建索引
CREATE INDEX idx_uxd_workspace_project_user_id ON uxd_workspace_project (user_id);
CREATE INDEX idx_uxd_workspace_project_team_id ON uxd_workspace_project (team_id);
CREATE INDEX idx_uxd_workspace_project_status ON uxd_workspace_project (status);
CREATE INDEX idx_uxd_workspace_project_create_time ON uxd_workspace_project (create_time);

-- ----------------------------
-- 插入测试数据
-- ----------------------------

-- 插入示例项目
INSERT INTO uxd_workspace_project (name, description, cover, type, status, user_id, team_id, starred, remark, creator) VALUES 
('Universal X Designer 主项目', '核心设计项目，包含主要的用户界面设计', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=400', 'web', 1, 1, 1, TRUE, '核心项目', 'admin'),
('移动端设计规范', '移动端设计系统和组件库', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=400', 'mobile', 1, 1, 1, FALSE, '移动端项目', 'admin');

-- 插入示例文件
INSERT INTO uxd_workspace_file (name, type, thumbnail, content, category, starred, file_size, project_id, user_id, team_id, status, remark, creator) VALUES 
('首页设计稿', 'design', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=300', '{"version":"1.0","type":"design","elements":[]}', 'drafts', TRUE, 2048, 1, 1, 1, 1, '主页设计文件', 'admin'),
('用户流程图', 'figjam', 'https://images.unsplash.com/photo-1559028006-448665bd7c7f?w=300', '{"version":"1.0","type":"figjam","elements":[]}', 'drafts', FALSE, 1536, 1, 1, 1, 1, '用户体验流程', 'admin'),
('组件库模板', 'template', 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=300', '{"version":"1.0","type":"template","elements":[]}', 'templates', TRUE, 3072, 2, 1, 1, 1, '可重用组件', 'admin'),
('登录页面设计', 'design', 'https://images.unsplash.com/photo-1517077304055-6e89abbf09b0?w=300', '{"version":"1.0","type":"design","elements":[]}', 'drafts', FALSE, 1792, 1, 1, 1, 1, '用户登录界面', 'admin'); 