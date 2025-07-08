#!/bin/bash

# ===========================================
# TunnelManagement 数据库恢复脚本
# 支持从备份文件恢复MySQL数据库
# ===========================================

set -e

# 获取当前时间戳
DATE=$(date +%Y%m%d_%H%M%S)

# ===========================================
# 配置参数 - 请根据实际环境修改
# ===========================================

# 数据库连接配置
DB_HOST="127.0.0.1"
DB_PORT="3306"
DB_USER="root"
DB_PASSWORD="Coolhomer"
DB_NAME="tunnel_management"  # 默认数据库名，可通过参数覆盖

# 备份路径配置
BACKUP_BASE_DIR="./backup/database"
BACKUP_FULL_DIR="$BACKUP_BASE_DIR/full"
BACKUP_LOG_DIR="$BACKUP_BASE_DIR/logs"
RESTORE_LOG_FILE="$BACKUP_LOG_DIR/restore_${DATE}.log"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ===========================================
# 日志函数
# ===========================================

log() {
    local level=$1
    shift
    echo -e "[$(date '+%Y-%m-%d %H:%M:%S')] [$level] $*" | tee -a "$RESTORE_LOG_FILE"
}

log_info() {
    log "${GREEN}INFO${NC}" "$@"
}

log_error() {
    log "${RED}ERROR${NC}" "$@"
}

log_warn() {
    log "${YELLOW}WARN${NC}" "$@"
}

log_success() {
    log "${GREEN}SUCCESS${NC}" "$@"
}

# ===========================================
# 初始化函数
# ===========================================

init_directories() {
    mkdir -p "$BACKUP_LOG_DIR"
    log_info "日志目录初始化完成"
}

check_dependencies() {
    log_info "检查依赖..."
    
    # 检查mysql是否存在
    if ! command -v mysql &> /dev/null; then
        log_error "mysql 命令未找到，请安装MySQL客户端工具"
        exit 1
    fi
    
    # 检查gunzip是否存在（用于解压.gz文件）
    if ! command -v gunzip &> /dev/null; then
        log_error "gunzip 命令未找到，请安装gzip工具"
        exit 1
    fi
    
    log_info "依赖检查完成"
}

test_connection() {
    log_info "测试数据库连接..."
    
    if mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1" 2>/dev/null; then
        log_info "数据库连接测试成功"
    else
        log_error "数据库连接失败，请检查连接参数"
        exit 1
    fi
}

# ===========================================
# 备份文件相关函数
# ===========================================

list_backup_files() {
    log_info "可用的备份文件："
    echo ""
    
    local backup_files=()
    local index=1
    
    # 查找所有备份文件
    while IFS= read -r -d '' file; do
        local file_date=$(basename "$file" | sed 's/.*_\([0-9]\{8\}_[0-9]\{6\}\)\.sql\.gz/\1/')
        local file_size=$(du -h "$file" | cut -f1)
        local readable_date=$(echo "$file_date" | sed 's/\([0-9]\{4\}\)\([0-9]\{2\}\)\([0-9]\{2\}\)_\([0-9]\{2\}\)\([0-9]\{2\}\)\([0-9]\{2\}\)/\1-\2-\3 \4:\5:\6/')
        
        backup_files+=("$file")
        printf "%2d) %s (%s) - %s\n" "$index" "$(basename "$file")" "$file_size" "$readable_date"
        ((index++))
    done < <(find "$BACKUP_FULL_DIR" -name "*.sql.gz" -type f -print0 | sort -z)
    
    if [ ${#backup_files[@]} -eq 0 ]; then
        log_warn "没有找到备份文件"
        return 1
    fi
    
    echo ""
    echo "0) 手动输入备份文件路径"
    echo "q) 退出"
    echo ""
    
    return 0
}

select_backup_file() {
    local selected_file=""
    
    while true; do
        if ! list_backup_files; then
            return 1
        fi
        
        read -p "请选择要恢复的备份文件 [编号]: " choice
        
        case "$choice" in
            "q"|"Q")
                log_info "用户取消操作"
                exit 0
                ;;
            "0")
                read -p "请输入备份文件的完整路径: " custom_file
                if [ -f "$custom_file" ]; then
                    selected_file="$custom_file"
                    break
                else
                    log_error "文件不存在: $custom_file"
                    continue
                fi
                ;;
            *)
                if [[ "$choice" =~ ^[0-9]+$ ]]; then
                    local backup_files=()
                    while IFS= read -r -d '' file; do
                        backup_files+=("$file")
                    done < <(find "$BACKUP_FULL_DIR" -name "*.sql.gz" -type f -print0 | sort -z)
                    
                    local index=$((choice - 1))
                    if [ $index -ge 0 ] && [ $index -lt ${#backup_files[@]} ]; then
                        selected_file="${backup_files[$index]}"
                        break
                    else
                        log_error "无效的选择: $choice"
                        continue
                    fi
                else
                    log_error "请输入有效的数字"
                    continue
                fi
                ;;
        esac
    done
    
    echo "$selected_file"
}

verify_backup_file() {
    local backup_file=$1
    
    log_info "验证备份文件: $(basename "$backup_file")"
    
    # 检查文件是否存在
    if [ ! -f "$backup_file" ]; then
        log_error "备份文件不存在: $backup_file"
        return 1
    fi
    
    # 检查文件是否为空
    if [ ! -s "$backup_file" ]; then
        log_error "备份文件为空: $backup_file"
        return 1
    fi
    
    # 检查是否为有效的gzip文件
    if ! gunzip -t "$backup_file" 2>/dev/null; then
        log_error "备份文件损坏或不是有效的gzip文件: $backup_file"
        return 1
    fi
    
    local file_size=$(du -h "$backup_file" | cut -f1)
    log_info "备份文件验证成功，大小: $file_size"
    
    return 0
}

# ===========================================
# 数据库操作函数
# ===========================================

backup_current_database() {
    log_info "备份当前数据库（恢复前安全备份）..."
    
    local safety_backup_dir="$BACKUP_BASE_DIR/safety"
    mkdir -p "$safety_backup_dir"
    
    local safety_backup_file="$safety_backup_dir/${DB_NAME}_safety_before_restore_${DATE}.sql.gz"
    
    # 执行安全备份
    mysqldump -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        --single-transaction \
        --routines \
        --triggers \
        --events \
        --set-gtid-purged=OFF \
        --default-character-set=utf8mb4 \
        --lock-tables=false \
        --add-drop-database \
        --databases "$DB_NAME" 2>/dev/null | gzip > "$safety_backup_file"
    
    if [ $? -eq 0 ]; then
        local file_size=$(du -h "$safety_backup_file" | cut -f1)
        log_info "安全备份完成: $safety_backup_file (大小: $file_size)"
        echo "$safety_backup_file"
    else
        log_error "安全备份失败"
        return 1
    fi
}

drop_database() {
    log_warn "删除现有数据库: $DB_NAME"
    
    mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        -e "DROP DATABASE IF EXISTS \`$DB_NAME\`;" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        log_info "数据库删除成功"
    else
        log_error "数据库删除失败"
        return 1
    fi
}

create_database() {
    log_info "创建新数据库: $DB_NAME"
    
    mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        -e "CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        log_info "数据库创建成功"
    else
        log_error "数据库创建失败"
        return 1
    fi
}

restore_from_backup() {
    local backup_file=$1
    
    log_info "开始从备份文件恢复数据库..."
    log_info "备份文件: $(basename "$backup_file")"
    log_info "目标数据库: $DB_NAME"
    
    # 检查备份文件中的数据库名称
    local backup_db_name=$(gunzip -c "$backup_file" | head -20 | grep -oP "CREATE DATABASE.*?\K\`[^`]+\`" | head -1 | tr -d '`' || echo "")
    
    if [ -n "$backup_db_name" ] && [ "$backup_db_name" != "$DB_NAME" ]; then
        log_warn "备份文件中的数据库名称: $backup_db_name"
        log_warn "目标数据库名称: $DB_NAME"
        log_info "将自动进行数据库名称转换"
        
        # 使用sed替换数据库名称并导入
        gunzip -c "$backup_file" | sed "s/\`$backup_db_name\`/\`$DB_NAME\`/g" | \
        sed "s/Database: $backup_db_name/Database: $DB_NAME/g" | \
        mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" 2>/dev/null
    else
        # 直接解压并导入数据库
        gunzip -c "$backup_file" | mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" 2>/dev/null
    fi
    
    if [ $? -eq 0 ]; then
        log_success "数据库恢复成功"
    else
        log_error "数据库恢复失败"
        return 1
    fi
}

verify_restore() {
    log_info "验证数据库恢复结果..."
    
    # 检查数据库是否存在
    local db_exists=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        -e "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME='$DB_NAME';" 2>/dev/null | grep -c "$DB_NAME")
    
    if [ "$db_exists" -eq 0 ]; then
        log_error "恢复验证失败：数据库不存在"
        return 1
    fi
    
    # 检查表数量
    local table_count=$(mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        -e "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='$DB_NAME';" 2>/dev/null | tail -n 1)
    
    log_info "恢复验证成功"
    log_info "数据库: $DB_NAME"
    log_info "表数量: $table_count"
    
    return 0
}

# ===========================================
# 恢复模式函数
# ===========================================

restore_interactive() {
    log_info "=== 交互式恢复模式 ==="
    
    echo ""
    echo -e "${YELLOW}警告: 恢复操作将会覆盖现有数据库！${NC}"
    echo -e "${YELLOW}建议在恢复前进行安全备份。${NC}"
    echo ""
    
    read -p "是否继续？[y/N]: " confirm
    if [[ ! $confirm =~ ^[Yy]$ ]]; then
        log_info "用户取消操作"
        exit 0
    fi
    
    # 选择备份文件
    local backup_file
    backup_file=$(select_backup_file)
    
    if [ -z "$backup_file" ]; then
        log_error "未选择备份文件"
        exit 1
    fi
    
    # 验证备份文件
    if ! verify_backup_file "$backup_file"; then
        exit 1
    fi
    
    # 显示恢复信息
    echo ""
    log_info "=== 恢复确认 ==="
    log_info "备份文件: $(basename "$backup_file")"
    log_info "目标数据库: $DB_NAME"
    log_info "数据库服务器: $DB_HOST:$DB_PORT"
    echo ""
    
    read -p "确认执行恢复操作？[y/N]: " final_confirm
    if [[ ! $final_confirm =~ ^[Yy]$ ]]; then
        log_info "用户取消操作"
        exit 0
    fi
    
    # 执行恢复
    perform_restore "$backup_file"
}

restore_from_file() {
    local backup_file=$1
    
    if [ -z "$backup_file" ]; then
        log_error "请指定备份文件路径"
        show_help
        exit 1
    fi
    
    log_info "=== 从指定文件恢复 ==="
    log_info "备份文件: $backup_file"
    
    # 验证备份文件
    if ! verify_backup_file "$backup_file"; then
        exit 1
    fi
    
    # 执行恢复
    perform_restore "$backup_file"
}

restore_latest() {
    log_info "=== 从最新备份恢复 ==="
    
    # 查找最新的备份文件
    local latest_backup
    latest_backup=$(find "$BACKUP_FULL_DIR" -name "*.sql.gz" -type f -printf '%T@ %p\n' | sort -n | tail -1 | cut -d' ' -f2-)
    
    if [ -z "$latest_backup" ]; then
        log_error "没有找到备份文件"
        exit 1
    fi
    
    log_info "最新备份文件: $(basename "$latest_backup")"
    
    # 验证备份文件
    if ! verify_backup_file "$latest_backup"; then
        exit 1
    fi
    
    echo ""
    echo -e "${YELLOW}警告: 将从最新备份恢复数据库，这将覆盖现有数据！${NC}"
    read -p "确认继续？[y/N]: " confirm
    if [[ ! $confirm =~ ^[Yy]$ ]]; then
        log_info "用户取消操作"
        exit 0
    fi
    
    # 执行恢复
    perform_restore "$latest_backup"
}

perform_restore() {
    local backup_file=$1
    
    log_info "=== 开始恢复操作 ==="
    
    # 1. 安全备份当前数据库
    local safety_backup
    safety_backup=$(backup_current_database)
    if [ $? -ne 0 ]; then
        log_error "安全备份失败，恢复操作中止"
        exit 1
    fi
    
    # 2. 删除现有数据库
    if ! drop_database; then
        log_error "删除现有数据库失败"
        exit 1
    fi
    
    # 3. 恢复数据库
    if ! restore_from_backup "$backup_file"; then
        log_error "数据库恢复失败，尝试恢复安全备份..."
        
        # 恢复失败，尝试从安全备份恢复
        if [ -n "$safety_backup" ] && [ -f "$safety_backup" ]; then
            gunzip -c "$safety_backup" | mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" 2>/dev/null
            if [ $? -eq 0 ]; then
                log_warn "已从安全备份恢复到恢复前状态"
            else
                log_error "安全备份恢复也失败，请手动检查数据库状态"
            fi
        fi
        exit 1
    fi
    
    # 4. 验证恢复结果
    if ! verify_restore; then
        log_error "恢复验证失败"
        exit 1
    fi
    
    log_success "=== 数据库恢复完成 ==="
    log_info "安全备份位置: $safety_backup"
    log_info "可以删除安全备份文件以节省空间"
}

# ===========================================
# 帮助函数
# ===========================================

show_help() {
    cat << EOF
TunnelManagement 数据库恢复脚本

用法: $0 [选项] [恢复模式] [数据库参数]

恢复模式:
    interactive     交互式恢复（默认）- 选择备份文件进行恢复
    latest          从最新备份恢复
    file <路径>     从指定备份文件恢复
    list            列出可用的备份文件
    test            测试数据库连接
    help            显示此帮助信息

数据库参数:
    --host <主机>       数据库主机地址 (默认: 127.0.0.1)
    --port <端口>       数据库端口 (默认: 3306)
    --user <用户名>     数据库用户名 (默认: root)
    --password <密码>   数据库密码 (默认: Coolhomer)
    --database <数据库名> 目标数据库名 (默认: tunnel_management)
    --backup-dir <路径> 备份目录路径 (默认: ./backup/database)

基本示例:
    $0 interactive                                    # 使用默认配置交互式恢复
    $0 latest                                        # 从最新备份恢复
    $0 file ./backup/database/full/202506/backup.sql.gz  # 从指定文件恢复
    $0 list                                          # 列出可用备份
    $0 test                                          # 测试连接

跨平台示例:
    # Windows环境（不同数据库名）
    $0 interactive --database tunnel_management_win
    
    # 远程数据库
    $0 latest --host 192.168.1.100 --user backup_user --password mypass
    
    # 自定义所有参数
    $0 interactive --host localhost --port 3307 --database my_tunnel --user admin --password secret123

恢复过程:
    1. 验证备份文件完整性
    2. 备份当前数据库（安全备份）
    3. 删除现有数据库
    4. 从备份文件恢复数据
    5. 验证恢复结果

注意事项:
    ⚠️  恢复操作将完全覆盖现有数据库
    ⚠️  如果目标数据库名与备份中的不同，会自动处理
    ✅  恢复前会自动创建安全备份
    ✅  恢复失败时会尝试从安全备份恢复
    ✅  建议在恢复前停止应用服务

当前配置:
    主机: $DB_HOST:$DB_PORT
    数据库: $DB_NAME
    备份目录: $BACKUP_BASE_DIR

EOF
}

# ===========================================
# 参数解析函数
# ===========================================

parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            --host)
                DB_HOST="$2"
                shift 2
                ;;
            --port)
                DB_PORT="$2"
                shift 2
                ;;
            --user)
                DB_USER="$2"
                shift 2
                ;;
            --password)
                DB_PASSWORD="$2"
                shift 2
                ;;
            --database)
                DB_NAME="$2"
                shift 2
                ;;
            --backup-dir)
                BACKUP_BASE_DIR="$2"
                BACKUP_FULL_DIR="$BACKUP_BASE_DIR/full"
                BACKUP_LOG_DIR="$BACKUP_BASE_DIR/logs"
                RESTORE_LOG_FILE="$BACKUP_LOG_DIR/restore_${DATE}.log"
                shift 2
                ;;
            *)
                # 保存非选项参数
                POSITIONAL_ARGS+=("$1")
                shift
                ;;
        esac
    done
    
    # 恢复位置参数
    set -- "${POSITIONAL_ARGS[@]}"
}

# ===========================================
# 主函数
# ===========================================

main() {
    local action=${1:-"interactive"}
    
    echo -e "${BLUE}TunnelManagement 数据库恢复工具${NC}"
    echo "====================================="
    echo ""
    
    # 初始化
    init_directories
    check_dependencies
    test_connection
    
    # 解析参数
    parse_arguments "$@"
    
    case "$action" in
        "interactive")
            restore_interactive
            ;;
        "latest")
            restore_latest
            ;;
        "file")
            restore_from_file "$2"
            ;;
        "list")
            list_backup_files
            ;;
        "test")
            log_info "数据库连接测试通过"
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
    
    log_info "恢复操作完成"
}

# 运行主函数
main "$@" 