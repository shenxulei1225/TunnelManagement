# TunnelManagement Git备份策略

## 概述

本文档描述了TunnelManagement项目中docs和backup目录的Git版本控制策略，实现了"自动备份"的目标。

## 🎯 设计目标

1. **保留重要结构**：确保备份和文档目录结构在版本控制中
2. **避免大文件**：排除大型备份数据文件，避免Git仓库臃肿
3. **便于部署**：新环境克隆后即可使用，无需手动创建目录
4. **跨平台支持**：支持Mac、Linux、Windows等不同操作系统

## 📁 目录策略

### docs/ 目录 ✅ 完全包含

```
docs/
├── README.md                           # ✅ 完全纳入版本控制
├── database-management.md              # ✅ 数据库管理指南
├── git-backup-strategy.md              # ✅ 本文档
├── design/                            # ✅ 设计文档
├── development/                       # ✅ 开发文档
├── structure/                         # ✅ 结构说明
└── summary/                           # ✅ 总结文档
```

**策略：** 文档是项目的重要资产，完全纳入版本控制。

### backup/ 目录 🎯 结构化包含

```
backup/
├── README.md                          # ✅ 说明文档
└── database/
    ├── full/                          # ✅ 目录结构保留
    │   ├── .gitkeep                   # ✅ 确保目录存在
    │   ├── 202506/                    # ❌ 实际备份文件被忽略
    │   │   └── *.sql.gz              # ❌ 大型文件不纳入版本控制
    │   └── latest_full_backup.sql.gz  # ❌ 符号链接被忽略
    ├── incremental/                   # ✅ 目录结构保留
    │   └── .gitkeep                   # ✅ 确保目录存在
    ├── safety/                        # ✅ 目录结构保留
    │   └── .gitkeep                   # ✅ 确保目录存在
    └── logs/                          # ✅ 目录结构保留
        ├── .gitkeep                   # ✅ 确保目录存在
        ├── *.log                      # ❌ 详细日志被忽略
        └── last_full_backup_time      # ✅ 小型配置文件保留
```

**策略：** 保留目录结构和配置，排除大型数据文件。

## ⚙️ .gitignore 配置

```gitignore
######################################################################
# Database Backup Files - 数据库备份文件配置
# 保留目录结构和脚本，排除大型备份文件

# 排除实际的备份数据文件（.sql.gz）
backup/database/full/**/*.sql.gz
backup/database/incremental/**/*.sql.gz
backup/database/safety/**/*.sql.gz

# 排除大型日志文件（保留最新的几个小日志文件用于参考）
backup/database/logs/*.log
!backup/database/logs/.gitkeep

# 保留备份目录结构
!backup/
!backup/database/
!backup/database/full/
!backup/database/incremental/
!backup/database/safety/
!backup/database/logs/

# 保留配置和时间戳文件
!backup/database/logs/last_full_backup_time

######################################################################
# Package Files - 打包文件
packages/
```

## 🚀 自动备份实现

### 1. 目录结构自动化

通过.gitkeep文件确保关键目录在克隆时存在：

```bash
# 这些目录会在git clone时自动创建
backup/database/full/
backup/database/incremental/
backup/database/safety/
backup/database/logs/
```

### 2. 脚本版本控制

所有备份相关脚本都纳入版本控制：

```bash
script/shell/
├── backup_database.sh           # ✅ 备份脚本
├── restore_database.sh          # ✅ 恢复脚本
├── setup_backup_cron.sh         # ✅ 定时任务脚本
└── package_for_windows.sh       # ✅ 跨平台打包脚本
```

### 3. 文档自动化

完整的文档体系确保团队成员了解备份策略：

- `docs/database-management.md` - 完整管理指南
- `backup/README.md` - 备份目录说明
- `docs/git-backup-strategy.md` - 本策略文档

## 🌐 跨平台恢复

### Windows平台

使用打包脚本创建Windows恢复包：

```bash
# 在Mac/Linux上执行
./script/shell/package_for_windows.sh package

# 生成的包可传输到Windows
packages/tunnel_management_windows_recovery_[时间戳].zip
```

包含内容：
- ✅ 备份文件（实际数据）
- ✅ Windows恢复脚本（.bat + .ps1）
- ✅ 完整文档
- ✅ 配置文件

### Linux/Unix平台

直接使用Git仓库中的脚本：

```bash
git clone [repository]
cd TunnelManagement

# 目录结构已就绪，直接使用
./script/shell/backup_database.sh full
./script/shell/restore_database.sh interactive
```

## 📊 存储效率对比

| 策略 | Git仓库大小 | 克隆速度 | 备份完整性 | 跨平台支持 |
|------|------------|---------|------------|------------|
| 完全包含 | 很大 (>100MB) | 慢 | ✅ 完整 | ❌ 困难 |
| 完全排除 | 小 | 快 | ❌ 缺失结构 | ❌ 需手动配置 |
| **结构化包含** | **适中** | **快** | **✅ 结构完整** | **✅ 完全支持** |

## 🔄 工作流程

### 开发环境搭建

1. **克隆仓库**
   ```bash
   git clone [repository]
   cd TunnelManagement
   ```

2. **验证目录结构**
   ```bash
   ls -la backup/database/
   # 应该看到所有必要的目录
   ```

3. **配置数据库连接**
   ```bash
   # 修改脚本中的数据库配置
   vim script/shell/backup_database.sh
   ```

4. **执行首次备份**
   ```bash
   ./script/shell/backup_database.sh full
   ```

### 生产环境部署

1. **克隆代码**
   ```bash
   git clone [repository]
   cd TunnelManagement
   ```

2. **设置定时备份**
   ```bash
   ./script/shell/setup_backup_cron.sh recommended
   ```

3. **验证备份系统**
   ```bash
   ./script/shell/backup_database.sh test
   ```

### 数据迁移

1. **源环境打包**
   ```bash
   ./script/shell/package_for_windows.sh package
   ```

2. **传输到目标环境**
   ```bash
   scp packages/recovery_package.zip user@target:/path/
   ```

3. **目标环境恢复**
   ```bash
   unzip recovery_package.zip
   ./scripts/restore_database.bat  # Windows
   # 或
   ./script/shell/restore_database.sh interactive  # Linux/Mac
   ```

## ✅ 优势总结

1. **版本控制优化**
   - Git仓库保持精简
   - 克隆速度快
   - 避免大文件问题

2. **部署便利性**
   - 新环境即装即用
   - 目录结构自动就绪
   - 脚本直接可用

3. **跨平台兼容**
   - 支持Mac、Linux、Windows
   - 提供专门的跨平台工具
   - 文档完整详细

4. **安全性保障**
   - 敏感数据不进入公共仓库
   - 提供安全的传输方案
   - 完整的恢复机制

5. **维护友好**
   - 文档完整易懂
   - 脚本功能清晰
   - 错误处理完善

## 🛠️ 维护指南

### 定期检查

1. **每月检查**
   ```bash
   # 检查Git仓库大小
   du -sh .git/
   
   # 检查备份文件是否被误提交
   git log --stat | grep "\.sql\.gz"
   ```

2. **清理操作**
   ```bash
   # 清理本地包文件
   ./script/shell/package_for_windows.sh clean
   
   # 清理过期备份
   ./script/shell/backup_database.sh cleanup
   ```

### 团队协作

1. **新成员入门**
   - 提供`docs/database-management.md`
   - 演示完整的备份恢复流程
   - 确保理解Git策略

2. **代码审查**
   - 检查是否有大文件被误添加
   - 验证.gitignore配置正确
   - 确保文档同步更新

## 📞 技术支持

如遇问题，请参考：

1. **文档资源**
   - `docs/database-management.md` - 完整操作指南
   - `backup/README.md` - 备份目录说明
   - 各脚本的内置help功能

2. **常见问题**
   - Git仓库过大：检查是否有大文件被误提交
   - 目录不存在：运行`git checkout .`恢复.gitkeep文件
   - 脚本权限：执行`chmod +x script/shell/*.sh`

---

**最后更新：** 2025-06-28  
**版本：** 1.0.0  
**策略状态：** ✅ 已实施 