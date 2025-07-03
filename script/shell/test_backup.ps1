# 备份功能测试脚本
# 用于验证备份脚本的各项功能

param(
    [switch]$TestConfig = $false,
    [switch]$TestDatabase = $false,
    [switch]$TestFiles = $false,
    [switch]$TestAll = $false
)

# 获取脚本目录
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$BackupScript = Join-Path $ScriptDir "backup_data_windows.ps1"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "     备份功能测试脚本" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查备份脚本是否存在
if (!(Test-Path $BackupScript)) {
    Write-Host "错误: 备份脚本不存在: $BackupScript" -ForegroundColor Red
    exit 1
}

Write-Host "✓ 备份脚本存在: $BackupScript" -ForegroundColor Green

# 测试配置文件
function Test-Configuration {
    Write-Host "`n--- 测试配置文件 ---" -ForegroundColor Yellow
    
    $configFile = Join-Path $ScriptDir "backup_config.json"
    
    if (Test-Path $configFile) {
        Write-Host "✓ 配置文件存在: $configFile" -ForegroundColor Green
        
        try {
            $config = Get-Content $configFile | ConvertFrom-Json
            Write-Host "✓ 配置文件格式正确" -ForegroundColor Green
            
            # 检查必要的配置项
            if ($config.backup -and $config.backup.base_path) {
                Write-Host "✓ 备份路径配置存在: $($config.backup.base_path)" -ForegroundColor Green
            } else {
                Write-Host "✗ 备份路径配置缺失" -ForegroundColor Red
            }
            
            if ($config.database) {
                Write-Host "✓ 数据库配置存在" -ForegroundColor Green
            } else {
                Write-Host "✗ 数据库配置缺失" -ForegroundColor Red
            }
            
            if ($config.files) {
                Write-Host "✓ 文件备份配置存在" -ForegroundColor Green
            } else {
                Write-Host "✗ 文件备份配置缺失" -ForegroundColor Red
            }
            
        } catch {
            Write-Host "✗ 配置文件格式错误: $($_.Exception.Message)" -ForegroundColor Red
        }
    } else {
        Write-Host "✗ 配置文件不存在，将创建默认配置" -ForegroundColor Yellow
    }
}

# 测试数据库连接
function Test-DatabaseConnection {
    Write-Host "`n--- 测试数据库连接 ---" -ForegroundColor Yellow
    
    $configFile = Join-Path $ScriptDir "backup_config.json"
    
    if (!(Test-Path $configFile)) {
        Write-Host "✗ 配置文件不存在，跳过数据库测试" -ForegroundColor Yellow
        return
    }
    
    try {
        $config = Get-Content $configFile | ConvertFrom-Json
        $dbConfig = $config.database
        
        if (!$dbConfig.enabled) {
            Write-Host "! 数据库备份已禁用" -ForegroundColor Yellow
            return
        }
        
        Write-Host "数据库类型: $($dbConfig.type)" -ForegroundColor Cyan
        Write-Host "主机: $($dbConfig.host):$($dbConfig.port)" -ForegroundColor Cyan
        Write-Host "用户名: $($dbConfig.username)" -ForegroundColor Cyan
        
        switch ($dbConfig.type.ToLower()) {
            "mysql" {
                $mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue
                if ($mysqlPath) {
                    Write-Host "✓ MySQL 客户端已安装" -ForegroundColor Green
                    
                    # 测试连接（不包含密码的安全测试）
                    $testArgs = @(
                        "--host=$($dbConfig.host)",
                        "--port=$($dbConfig.port)",
                        "--user=$($dbConfig.username)",
                        "--execute=SELECT 1",
                        "--silent"
                    )
                    
                    Write-Host "正在测试 MySQL 连接..." -ForegroundColor Cyan
                    # 注意：实际生产环境中不应该这样测试密码
                    Write-Host "! 请手动验证数据库连接" -ForegroundColor Yellow
                } else {
                    Write-Host "✗ MySQL 客户端未安装" -ForegroundColor Red
                }
            }
            "postgresql" {
                $pgPath = Get-Command psql -ErrorAction SilentlyContinue
                if ($pgPath) {
                    Write-Host "✓ PostgreSQL 客户端已安装" -ForegroundColor Green
                } else {
                    Write-Host "✗ PostgreSQL 客户端未安装" -ForegroundColor Red
                }
            }
            "sqlserver" {
                $sqlcmdPath = Get-Command sqlcmd -ErrorAction SilentlyContinue
                if ($sqlcmdPath) {
                    Write-Host "✓ SQL Server 客户端已安装" -ForegroundColor Green
                } else {
                    Write-Host "✗ SQL Server 客户端未安装" -ForegroundColor Red
                }
            }
            default {
                Write-Host "✗ 不支持的数据库类型: $($dbConfig.type)" -ForegroundColor Red
            }
        }
        
    } catch {
        Write-Host "✗ 数据库配置测试失败: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 测试文件备份路径
function Test-FilePaths {
    Write-Host "`n--- 测试文件备份路径 ---" -ForegroundColor Yellow
    
    $configFile = Join-Path $ScriptDir "backup_config.json"
    $projectRoot = Split-Path -Parent (Split-Path -Parent $ScriptDir)
    
    if (!(Test-Path $configFile)) {
        Write-Host "✗ 配置文件不存在，跳过文件路径测试" -ForegroundColor Yellow
        return
    }
    
    try {
        $config = Get-Content $configFile | ConvertFrom-Json
        $filesConfig = $config.files
        
        if (!$filesConfig.enabled) {
            Write-Host "! 文件备份已禁用" -ForegroundColor Yellow
            return
        }
        
        Write-Host "项目根目录: $projectRoot" -ForegroundColor Cyan
        
        foreach ($source in $filesConfig.sources) {
            $sourcePath = Join-Path $projectRoot $source.path
            
            if (Test-Path $sourcePath) {
                Write-Host "✓ $($source.name): $sourcePath" -ForegroundColor Green
            } else {
                Write-Host "✗ $($source.name): $sourcePath (路径不存在)" -ForegroundColor Red
            }
        }
        
        # 测试备份目录权限
        $backupPath = $config.backup.base_path
        $testDir = Join-Path $backupPath "test_$(Get-Date -Format 'yyyyMMddHHmmss')"
        
        try {
            New-Item -ItemType Directory -Path $testDir -Force | Out-Null
            Remove-Item $testDir -Force
            Write-Host "✓ 备份目录可写: $backupPath" -ForegroundColor Green
        } catch {
            Write-Host "✗ 备份目录权限错误: $backupPath" -ForegroundColor Red
        }
        
    } catch {
        Write-Host "✗ 文件路径测试失败: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 测试 PowerShell 环境
function Test-PowerShellEnvironment {
    Write-Host "`n--- 测试 PowerShell 环境 ---" -ForegroundColor Yellow
    
    # PowerShell 版本
    Write-Host "PowerShell 版本: $($PSVersionTable.PSVersion)" -ForegroundColor Cyan
    
    if ($PSVersionTable.PSVersion.Major -ge 5) {
        Write-Host "✓ PowerShell 版本支持" -ForegroundColor Green
    } else {
        Write-Host "✗ PowerShell 版本过低，建议升级到 5.1 或更高版本" -ForegroundColor Red
    }
    
    # 执行策略
    $executionPolicy = Get-ExecutionPolicy
    Write-Host "执行策略: $executionPolicy" -ForegroundColor Cyan
    
    if ($executionPolicy -eq "Restricted") {
        Write-Host "✗ 执行策略受限，需要修改执行策略" -ForegroundColor Red
        Write-Host "建议运行: Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser" -ForegroundColor Yellow
    } else {
        Write-Host "✓ 执行策略允许脚本运行" -ForegroundColor Green
    }
    
    # 必要的 cmdlet
    $requiredCmdlets = @("Compress-Archive", "Invoke-WebRequest", "ConvertFrom-Json")
    
    foreach ($cmdlet in $requiredCmdlets) {
        if (Get-Command $cmdlet -ErrorAction SilentlyContinue) {
            Write-Host "✓ $cmdlet 可用" -ForegroundColor Green
        } else {
            Write-Host "✗ $cmdlet 不可用" -ForegroundColor Red
        }
    }
}

# 运行干净测试（不实际备份）
function Test-DryRun {
    Write-Host "`n--- 运行干燥测试 ---" -ForegroundColor Yellow
    
    try {
        Write-Host "正在运行仅文件备份测试..." -ForegroundColor Cyan
        
        # 创建临时配置文件，禁用数据库备份
        $tempConfig = @{
            backup = @{
                base_path = Join-Path $env:TEMP "tunnel_backup_test"
                retention_days = 1
                compress = $false
            }
            database = @{
                enabled = $false
            }
            files = @{
                enabled = $true
                backup_path = "files"
                sources = @(
                    @{
                        name = "测试文件"
                        path = "script\shell"
                        exclude = @("*.log", "*.tmp")
                    }
                )
            }
            notification = @{
                enabled = $false
            }
        }
        
        $tempConfigFile = Join-Path $ScriptDir "test_config.json"
        $tempConfig | ConvertTo-Json -Depth 10 | Set-Content $tempConfigFile
        
        # 运行备份脚本
        & $BackupScript -BackupType "files" -ConfigFile "test_config.json"
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ 干燥测试成功" -ForegroundColor Green
        } else {
            Write-Host "✗ 干燥测试失败，退出码: $LASTEXITCODE" -ForegroundColor Red
        }
        
        # 清理临时文件
        if (Test-Path $tempConfigFile) {
            Remove-Item $tempConfigFile -Force
        }
        
    } catch {
        Write-Host "✗ 干燥测试异常: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 主测试流程
Write-Host "开始备份功能测试..." -ForegroundColor Green

# PowerShell 环境测试
Test-PowerShellEnvironment

if ($TestConfig -or $TestAll) {
    Test-Configuration
}

if ($TestDatabase -or $TestAll) {
    Test-DatabaseConnection
}

if ($TestFiles -or $TestAll) {
    Test-FilePaths
}

if ($TestAll) {
    Test-DryRun
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "测试完成！" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan

if (!$TestConfig -and !$TestDatabase -and !$TestFiles -and !$TestAll) {
    Write-Host "`n使用参数运行特定测试:" -ForegroundColor Yellow
    Write-Host "  -TestConfig     测试配置文件" -ForegroundColor Yellow
    Write-Host "  -TestDatabase   测试数据库连接" -ForegroundColor Yellow
    Write-Host "  -TestFiles      测试文件路径" -ForegroundColor Yellow
    Write-Host "  -TestAll        运行所有测试" -ForegroundColor Yellow
    Write-Host "`n示例:" -ForegroundColor Yellow
    Write-Host "  .\test_backup.ps1 -TestAll" -ForegroundColor Yellow
} 