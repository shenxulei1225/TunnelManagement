#!/bin/bash

# ===========================================
# TunnelManagement 定时备份设置脚本
# 用于设置数据库定时备份的cron任务
# ===========================================

# 获取脚本绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
BACKUP_SCRIPT="$PROJECT_DIR/script/shell/backup_database.sh"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $*"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $*"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $*"
}

# 检查备份脚本是否存在
check_backup_script() {
    if [ ! -f "$BACKUP_SCRIPT" ]; then
        log_error "备份脚本不存在: $BACKUP_SCRIPT"
        exit 1
    fi
    
    if [ ! -x "$BACKUP_SCRIPT" ]; then
        log_warn "备份脚本没有执行权限，正在添加..."
        chmod +x "$BACKUP_SCRIPT"
    fi
    
    log_info "备份脚本检查通过: $BACKUP_SCRIPT"
}

# 显示当前cron任务
show_current_cron() {
    log_info "当前的cron任务:"
    crontab -l 2>/dev/null | grep -v "^#" | grep -v "^$" || log_warn "没有找到相关的cron任务"
}

# 添加备份任务到cron
setup_cron() {
    local cron_schedule=$1
    local backup_type=$2
    
    # 创建临时cron文件
    local temp_cron="/tmp/tunnel_backup_cron_$$"
    
    # 获取现有的cron任务（排除我们的备份任务）
    crontab -l 2>/dev/null | grep -v "tunnel_management backup" > "$temp_cron"
    
    # 添加新的备份任务
    echo "# TunnelManagement Database Backup - $backup_type" >> "$temp_cron"
    echo "$cron_schedule cd $PROJECT_DIR && $BACKUP_SCRIPT $backup_type >> $PROJECT_DIR/backup/database/logs/cron_backup.log 2>&1" >> "$temp_cron"
    echo "" >> "$temp_cron"
    
    # 安装新的cron任务
    crontab "$temp_cron"
    rm -f "$temp_cron"
    
    log_info "已添加 $backup_type 备份任务: $cron_schedule"
}

# 移除备份任务
remove_cron() {
    # 创建临时cron文件
    local temp_cron="/tmp/tunnel_backup_cron_$$"
    
    # 获取现有的cron任务（排除我们的备份任务）
    crontab -l 2>/dev/null | grep -v "TunnelManagement Database Backup" | grep -v "tunnel_management backup" > "$temp_cron"
    
    # 安装新的cron任务
    crontab "$temp_cron"
    rm -f "$temp_cron"
    
    log_info "已移除所有TunnelManagement备份任务"
}

# 设置推荐的备份策略
setup_recommended() {
    log_info "设置推荐的备份策略..."
    log_info "- 每天凌晨2点执行完整备份"
    log_info "- 每周日凌晨1点清理过期备份"
    
    # 创建临时cron文件
    local temp_cron="/tmp/tunnel_backup_cron_$$"
    
    # 获取现有的cron任务（排除我们的备份任务）
    crontab -l 2>/dev/null | grep -v "TunnelManagement Database Backup" | grep -v "tunnel_management backup" > "$temp_cron"
    
    # 添加推荐的备份任务
    cat >> "$temp_cron" << EOF
# TunnelManagement Database Backup - Recommended Schedule
# Daily full backup at 2:00 AM
0 2 * * * cd $PROJECT_DIR && $BACKUP_SCRIPT full >> $PROJECT_DIR/backup/database/logs/cron_backup.log 2>&1

# Weekly cleanup on Sunday at 1:00 AM  
0 1 * * 0 cd $PROJECT_DIR && $BACKUP_SCRIPT cleanup >> $PROJECT_DIR/backup/database/logs/cron_backup.log 2>&1

EOF
    
    # 安装新的cron任务
    crontab "$temp_cron"
    rm -f "$temp_cron"
    
    log_info "推荐备份策略设置完成"
}

# 显示帮助信息
show_help() {
    cat << EOF
TunnelManagement 定时备份设置脚本

用法: $0 [选项]

选项:
    recommended     设置推荐的备份策略（默认）
                   - 每天凌晨2点完整备份
                   - 每周日凌晨1点清理过期备份
    
    custom          自定义备份任务
    remove          移除所有备份任务
    show            显示当前cron任务
    test            测试备份脚本
    help            显示此帮助信息

示例:
    $0 recommended          # 设置推荐策略
    $0 custom              # 自定义设置
    $0 remove              # 移除所有任务
    $0 show                # 显示当前任务

常用的cron时间格式:
    0 2 * * *              # 每天凌晨2点
    0 */6 * * *            # 每6小时
    0 2 * * 0              # 每周日凌晨2点
    0 1 1 * *              # 每月1号凌晨1点

EOF
}

# 自定义设置
setup_custom() {
    echo "自定义备份任务设置"
    echo "===================="
    
    echo "请选择备份类型:"
    echo "1) 完整备份 (full)"
    echo "2) 清理任务 (cleanup)"
    
    read -p "请选择 [1-2]: " backup_choice
    
    case $backup_choice in
        1)
            backup_type="full"
            ;;
        2)
            backup_type="cleanup"
            ;;
        *)
            log_error "无效选择"
            exit 1
            ;;
    esac
    
    echo ""
    echo "请输入cron时间表达式 (例如: 0 2 * * * 表示每天凌晨2点)"
    echo "格式: 分钟 小时 日 月 星期"
    echo "帮助: https://crontab.guru"
    
    read -p "cron表达式: " cron_schedule
    
    if [ -z "$cron_schedule" ]; then
        log_error "cron表达式不能为空"
        exit 1
    fi
    
    # 验证cron表达式格式（简单验证）
    if ! echo "$cron_schedule" | grep -E '^[0-9*,-/]+ [0-9*,-/]+ [0-9*,-/]+ [0-9*,-/]+ [0-9*,-/]+$' > /dev/null; then
        log_error "cron表达式格式无效"
        exit 1
    fi
    
    echo ""
    echo "即将添加以下任务:"
    echo "类型: $backup_type"
    echo "时间: $cron_schedule"
    
    read -p "确认添加？[y/N]: " confirm
    
    if [[ $confirm =~ ^[Yy]$ ]]; then
        setup_cron "$cron_schedule" "$backup_type"
        log_info "自定义备份任务设置完成"
    else
        log_info "取消设置"
    fi
}

# 测试备份脚本
test_backup() {
    log_info "测试备份脚本..."
    
    cd "$PROJECT_DIR"
    
    if "$BACKUP_SCRIPT" test; then
        log_info "备份脚本测试通过"
    else
        log_error "备份脚本测试失败"
        exit 1
    fi
}

# 主函数
main() {
    local action=${1:-"recommended"}
    
    echo "TunnelManagement 定时备份设置工具"
    echo "===================================="
    echo ""
    
    case "$action" in
        "recommended")
            check_backup_script
            test_backup
            setup_recommended
            show_current_cron
            ;;
        "custom")
            check_backup_script
            test_backup
            setup_custom
            show_current_cron
            ;;
        "remove")
            remove_cron
            show_current_cron
            ;;
        "show")
            show_current_cron
            ;;
        "test")
            check_backup_script
            test_backup
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            log_error "未知选项: $action"
            show_help
            exit 1
            ;;
    esac
    
    echo ""
    log_info "操作完成"
}

# 运行主函数
main "$@" 