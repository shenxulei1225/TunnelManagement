#!/bin/bash
# TunnelManagement 脚本清理工具
# 保留核心的跨平台备份恢复脚本，删除重复和测试文件

echo "================================================"
echo "TunnelManagement 脚本清理工具"
echo "================================================"
echo ""

# 创建备份目录
BACKUP_DIR="../../backup/scripts_backup_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$BACKUP_DIR"

echo "1. 备份现有脚本到: $BACKUP_DIR"
cp -r . "$BACKUP_DIR/"
echo "✅ 脚本备份完成"

echo ""
echo "2. 分析当前脚本文件..."

# 需要保留的核心文件
KEEP_FILES=(
    # Mac 备份恢复
    "backup_database.sh"
    "restore_database.sh"
    "setup_backup_cron.sh"
    
    # Windows 备份恢复
    "backup_windows.bat"
    "backup_data_windows.ps1"
    "restore_windows.bat"
    "restore_data_windows.ps1"
    "restore_database_flexible.bat"
    
    # 跨平台工具
    "package_for_windows.sh"
    "list_backups_windows.ps1"
    "test_connection_windows.ps1"
    
    # 配置文件
    "backup_config.json"
    "restore_config.json"
    
    # 文档
    "README_windows_backup.md"
    
    # 部署脚本
    "deploy.sh"
)

# 需要删除的文件（重复、测试、临时文件）
DELETE_FILES=(
    "restore_from_windows.sh"      # 空文件
    "restore_database_simple.bat"  # 功能重复
    "test_backup.ps1"             # 测试文件
    "README_backup_windows.md"    # 重复文档
)

echo "保留的核心文件:"
for file in "${KEEP_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "  ✅ $file"
    else
        echo "  ❌ $file (不存在)"
    fi
done

echo ""
echo "将要删除的文件:"
for file in "${DELETE_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "  🗑️  $file"
    fi
done

echo ""
read -p "确认执行清理？(y/N): " confirm
if [[ $confirm != [yY] ]]; then
    echo "清理已取消"
    exit 0
fi

echo ""
echo "3. 执行清理..."

# 删除重复和无用文件
for file in "${DELETE_FILES[@]}"; do
    if [ -f "$file" ]; then
        rm "$file"
        echo "  🗑️  删除: $file"
    fi
done

echo ""
echo "4. 创建目录结构..."

# 创建分类目录
mkdir -p mac windows cross-platform config docs

# 移动文件到对应目录
echo "  📁 整理文件结构..."

# Mac 相关
mv backup_database.sh mac/ 2>/dev/null
mv restore_database.sh mac/ 2>/dev/null
mv setup_backup_cron.sh mac/ 2>/dev/null

# Windows 相关
mv backup_windows.bat windows/ 2>/dev/null
mv backup_data_windows.ps1 windows/ 2>/dev/null
mv restore_windows.bat windows/ 2>/dev/null
mv restore_data_windows.ps1 windows/ 2>/dev/null
mv restore_database_flexible.bat windows/ 2>/dev/null
mv list_backups_windows.ps1 windows/ 2>/dev/null
mv test_connection_windows.ps1 windows/ 2>/dev/null

# 跨平台工具
mv package_for_windows.sh cross-platform/ 2>/dev/null
mv deploy.sh cross-platform/ 2>/dev/null

# 配置文件
mv backup_config.json config/ 2>/dev/null
mv restore_config.json config/ 2>/dev/null

# 文档
mv README_windows_backup.md docs/ 2>/dev/null

echo ""
echo "5. 清理完成!"
echo ""
echo "新的目录结构:"
echo "script/shell/"
echo "├── mac/                    # Mac 备份恢复脚本"
echo "├── windows/                # Windows 备份恢复脚本"
echo "├── cross-platform/         # 跨平台工具"
echo "├── config/                 # 配置文件"
echo "└── docs/                   # 文档"
echo ""
echo "备份位置: $BACKUP_DIR"
echo ""
echo "✅ 脚本整理完成！" 