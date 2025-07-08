#!/bin/bash

# ===========================================
# TunnelManagement 数据库备份脚本
# 支持MySQL数据库的完整备份和增量备份
# ===========================================

set -e

# 获取当前时间戳
DATE=$(date +%Y%m%d_%H%M%S)
YEAR_MONTH=$(date +%Y%m)

# ===========================================
# 配置参数 - 请根据实际环境修改
# ===========================================

# 数据库连接配置
DB_HOST="127.0.0.1"
DB_PORT="3306"
DB_USER="root"
DB_PASSWORD="Coolhomer"
DB_NAME="tunnel_management"

# 备份路径配置
BACKUP_BASE_DIR="./backup/database"
BACKUP_FULL_DIR="$BACKUP_BASE_DIR/full"
BACKUP_INCREMENTAL_DIR="$BACKUP_BASE_DIR/incremental"
BACKUP_LOG_DIR="$BACKUP_BASE_DIR/logs"

# 备份文件名配置
BACKUP_FULL_FILE="$BACKUP_FULL_DIR/${DB_NAME}_full_${DATE}.sql"
BACKUP_INCREMENTAL_FILE="$BACKUP_INCREMENTAL_DIR/${DB_NAME}_incremental_${DATE}.sql"
LOG_FILE="$BACKUP_LOG_DIR/backup_${DATE}.log"

# 保留策略配置（天数）
FULL_BACKUP_RETENTION_DAYS=30      # 完整备份保留30天
INCREMENTAL_BACKUP_RETENTION_DAYS=7 # 增量备份保留7天
LOG_RETENTION_DAYS=30               # 日志保留30天

# MySQL配置文件路径（可选，用于更安全的密码管理）
MYSQL_CONFIG_FILE="/etc/mysql/backup.cnf"

# ===========================================
# 日志函数
# ===========================================

log() {
    local level=$1
    shift
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] [$level] $*" | tee -a "$LOG_FILE"
}

log_info() {
    log "INFO" "$@"
}

log_error() {
    log "ERROR" "$@"
}

log_warn() {
    log "WARN" "$@"
}

# ===========================================
# 初始化函数
# ===========================================

init_directories() {
    log_info "初始化备份目录..."
    
    # 创建必要的目录
    mkdir -p "$BACKUP_FULL_DIR"
    mkdir -p "$BACKUP_INCREMENTAL_DIR"
    mkdir -p "$BACKUP_LOG_DIR"
    
    # 按年月创建子目录，便于管理
    mkdir -p "$BACKUP_FULL_DIR/$YEAR_MONTH"
    mkdir -p "$BACKUP_INCREMENTAL_DIR/$YEAR_MONTH"
    
    log_info "备份目录初始化完成"
}

check_dependencies() {
    log_info "检查依赖..."
    
    # 检查mysqldump是否存在
    if ! command -v mysqldump &> /dev/null; then
        log_error "mysqldump 命令未找到，请安装MySQL客户端工具"
        exit 1
    fi
    
    # 检查mysql是否存在
    if ! command -v mysql &> /dev/null; then
        log_error "mysql 命令未找到，请安装MySQL客户端工具"
        exit 1
    fi
    
    log_info "依赖检查完成"
}

test_connection() {
    log_info "测试数据库连接..."
    
    if mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1" "$DB_NAME" &>/dev/null; then
        log_info "数据库连接测试成功"
    else
        log_error "数据库连接失败，请检查连接参数"
        exit 1
    fi
}

# ===========================================
# 备份函数
# ===========================================

backup_full() {
    log_info "开始完整备份..."
    
    local backup_file="$BACKUP_FULL_DIR/$YEAR_MONTH/${DB_NAME}_full_${DATE}.sql"
    local compressed_file="${backup_file}.gz"
    
    # 执行完整备份
    mysqldump -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        --single-transaction \
        --routines \
        --triggers \
        --events \
        --set-gtid-purged=OFF \
        --default-character-set=utf8mb4 \
        --lock-tables=false \
        --add-drop-database \
        --databases "$DB_NAME" > "$backup_file"
    
    if [ $? -eq 0 ]; then
        # 压缩备份文件
        gzip "$backup_file"
        
        local file_size=$(du -h "$compressed_file" | cut -f1)
        log_info "完整备份成功完成，文件: $compressed_file，大小: $file_size"
        
        # 创建最新备份的符号链接
        ln -sf "$compressed_file" "$BACKUP_FULL_DIR/latest_full_backup.sql.gz"
        
    else
        log_error "完整备份失败"
        exit 1
    fi
}

backup_incremental() {
    log_info "开始增量备份..."
    
    local backup_file="$BACKUP_INCREMENTAL_DIR/$YEAR_MONTH/${DB_NAME}_incremental_${DATE}.sql"
    local compressed_file="${backup_file}.gz"
    
    # 获取最近一次完整备份的时间
    local last_backup_time
    if [ -f "$BACKUP_LOG_DIR/last_full_backup_time" ]; then
        last_backup_time=$(cat "$BACKUP_LOG_DIR/last_full_backup_time")
        log_info "上次完整备份时间: $last_backup_time"
    else
        log_warn "未找到上次完整备份时间，执行完整备份"
        backup_full
        return
    fi
    
    # 导出指定时间之后修改的数据
    # 注意：这里是简化的增量备份实现，实际生产环境建议使用binlog
    mysqldump -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        --single-transaction \
        --routines \
        --triggers \
        --events \
        --set-gtid-purged=OFF \
        --default-character-set=utf8mb4 \
        --lock-tables=false \
        --where="update_time >= '$last_backup_time' OR create_time >= '$last_backup_time'" \
        --databases "$DB_NAME" > "$backup_file" 2>/dev/null || true
    
    if [ -s "$backup_file" ]; then
        # 压缩备份文件
        gzip "$backup_file"
        
        local file_size=$(du -h "$compressed_file" | cut -f1)
        log_info "增量备份完成，文件: $compressed_file，大小: $file_size"
        
        # 创建最新增量备份的符号链接
        ln -sf "$compressed_file" "$BACKUP_INCREMENTAL_DIR/latest_incremental_backup.sql.gz"
    else
        log_info "没有新的数据需要增量备份"
        rm -f "$backup_file"
    fi
}

backup_schema_only() {
    log_info "开始结构备份..."
    
    local backup_file="$BACKUP_FULL_DIR/$YEAR_MONTH/${DB_NAME}_schema_${DATE}.sql"
    local compressed_file="${backup_file}.gz"
    
    # 只备份表结构
    mysqldump -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" \
        --no-data \
        --routines \
        --triggers \
        --events \
        --set-gtid-purged=OFF \
        --default-character-set=utf8mb4 \
        --add-drop-database \
        --databases "$DB_NAME" > "$backup_file"
    
    if [ $? -eq 0 ]; then
        gzip "$backup_file"
        local file_size=$(du -h "$compressed_file" | cut -f1)
        log_info "结构备份完成，文件: $compressed_file，大小: $file_size"
    else
        log_error "结构备份失败"
        exit 1
    fi
}

# ===========================================
# 清理函数
# ===========================================

cleanup_old_backups() {
    log_info "开始清理过期备份..."
    
    # 清理过期的完整备份
    find "$BACKUP_FULL_DIR" -name "*.sql.gz" -type f -mtime +$FULL_BACKUP_RETENTION_DAYS -delete 2>/dev/null || true
    
    # 清理过期的增量备份
    find "$BACKUP_INCREMENTAL_DIR" -name "*.sql.gz" -type f -mtime +$INCREMENTAL_BACKUP_RETENTION_DAYS -delete 2>/dev/null || true
    
    # 清理过期的日志
    find "$BACKUP_LOG_DIR" -name "backup_*.log" -type f -mtime +$LOG_RETENTION_DAYS -delete 2>/dev/null || true
    
    log_info "清理完成"
}

# ===========================================
# 信息函数
# ===========================================

show_backup_info() {
    log_info "=== 备份信息统计 ==="
    
    # 统计完整备份
    local full_count=$(find "$BACKUP_FULL_DIR" -name "*.sql.gz" -type f 2>/dev/null | wc -l)
    local full_size=$(du -sh "$BACKUP_FULL_DIR" 2>/dev/null | cut -f1 || echo "0")
    
    # 总大小
    local total_size=$(du -sh "$BACKUP_BASE_DIR" 2>/dev/null | cut -f1 || echo "0")
    
    log_info "完整备份文件数: $full_count, 占用空间: $full_size"
    log_info "总占用空间: $total_size"
}

# ===========================================
# 帮助函数
# ===========================================

show_help() {
    cat << EOF
TunnelManagement 数据库备份脚本

用法: $0 [选项]

选项:
    full        执行完整备份（默认）
    incremental 执行增量备份
    schema      仅备份数据库结构
    cleanup     清理过期备份
    info        显示备份信息
    test        测试数据库连接
    help        显示此帮助信息

示例:
    $0 full                     # 执行完整备份
    $0 incremental             # 执行增量备份
    $0 cleanup                 # 清理过期备份
    $0 info                    # 显示备份统计信息

配置:
    数据库: $DB_NAME
    备份路径: $BACKUP_BASE_DIR

注意事项:
1. 请确保MySQL客户端工具已安装
2. 请确保数据库连接参数正确
3. 请确保备份目录有足够的磁盘空间
4. 建议定期测试备份文件的可用性

EOF
}

# ===========================================
# 主函数
# ===========================================

main() {
    local action=${1:-"full"}
    
    # 先初始化目录（所有操作都需要）
    init_directories
    
    case "$action" in
        "full")
            check_dependencies
            test_connection
            backup_full
            echo "$DATE" > "$BACKUP_LOG_DIR/last_full_backup_time"
            show_backup_info
            ;;
        "incremental")
            check_dependencies
            test_connection
            backup_incremental
            show_backup_info
            ;;
        "schema")
            check_dependencies
            test_connection
            backup_schema_only
            show_backup_info
            ;;
        "cleanup")
            cleanup_old_backups
            show_backup_info
            ;;
        "info")
            show_backup_info
            ;;
        "test")
            check_dependencies
            test_connection
            log_info "数据库连接测试完成"
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
    
    log_info "备份操作完成"
}

# 运行主函数
main "$@" 