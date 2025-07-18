#!/bin/bash

# 重新加载 Cursor 项目的脚本
# 这个脚本会重新编译项目并提示用户重新加载 Cursor

set -e

echo "🔄 重新加载 Cursor 项目..."

# 清理并重新编译
echo "🔨 清理并重新编译项目..."
mvn clean compile -DskipTests -q

echo "✅ 编译完成！"
echo ""
echo "📋 现在请在 Cursor 中执行以下操作："
echo ""
echo "1. 重新加载 Java 项目："
echo "   - 按 Cmd+Shift+P (Mac) 或 Ctrl+Shift+P (Windows/Linux)"
echo "   - 输入并执行: 'Java: Reload Projects'"
echo ""
echo "2. 如果仍有问题，尝试："
echo "   - 'Java: Clean Java Language Server Workspace'"
echo "   - 'Java: Restart Language Server'"
echo ""
echo "3. 或者重启 Cursor"
echo ""
echo "💡 提示："
echo "- 确保安装了 'Extension Pack for Java' 扩展"
echo "- 确保安装了 'Maven for Java' 扩展"
echo "- Maven 项目应该会自动识别生成的源码目录" 