#!/bin/bash

# 解决cheers-module-dynamic-business依赖问题的脚本
# 使用方法: ./fix-dependencies.sh

echo "🔧 开始修复cheers-module-dynamic-business依赖问题..."

# 检测问题函数
detect_problem() {
    echo "🔍 检测依赖问题..."
    cd /Users/kevin/Documents/Github/TunnelManagement/cheers-module-dynamic-business
    
    # 尝试编译，检查是否有依赖问题
    if mvn compile -q 2>/dev/null; then
        echo "✅ 依赖正常，无需修复"
        return 0
    else
        echo "❌ 检测到依赖问题，开始修复..."
        return 1
    fi
}

# 修复函数
fix_dependencies() {
    echo "📦 清理当前模块..."
    mvn clean -q
    
    echo "🗑️  清理相关依赖的本地仓库缓存..."
    cd /Users/kevin/Documents/Github/TunnelManagement
    mvn dependency:purge-local-repository -DmanualInclude="com.cheers.arch" -q
    
    echo "📋 重新安装cheers-dependencies..."
    cd cheers-dependencies && mvn clean install -DskipTests -q && cd ..
    
    echo "🔨 按正确顺序重新编译整个项目..."
    mvn clean install -DskipTests -q
    
    echo "🔧 设置IDE生成源码目录..."
    cd /Users/kevin/Documents/Github/TunnelManagement
    ./script/shell/setup-ide-generated-sources.sh
    
    echo "✅ 验证编译结果..."
    cd cheers-module-dynamic-business
    mvn compile -q
    
    if [ $? -eq 0 ]; then
        echo "🎉 依赖问题修复成功！"
        echo "📋 修复内容："
        echo "   - 清理了所有cheers相关模块的本地仓库缓存"
        echo "   - 重新安装了cheers-dependencies模块"
        echo "   - 按正确顺序重新编译了整个项目"
        echo "   - 验证了dynamic-business模块的编译"
        echo ""
        echo "📁 生成的target目录："
        ls -la target/
        return 0
    else
        echo "❌ 修复失败，请检查错误信息"
        return 1
    fi
}

# 主执行逻辑
main() {
    # 检测是否需要修复
    if detect_problem; then
        echo "✅ 项目状态正常，无需修复"
        exit 0
    fi
    
    # 执行修复
    if fix_dependencies; then
        echo ""
        echo "💡 预防措施："
        echo "   1. 定期运行此脚本清理缓存"
        echo "   2. 避免单独编译dynamic-business模块"
        echo "   3. 使用 mvn clean install -DskipTests 编译整个项目"
        echo "   4. 如果IDE仍有错误提示，请重启IDE"
        echo ""
        echo "🚀 修复完成！现在可以正常开发了。"
    else
        echo "❌ 修复失败，请手动检查问题"
        exit 1
    fi
}

# 执行主函数
main 