#!/bin/bash

# 设置 Cursor 生成源码目录的脚本
# 这个脚本专门为 Cursor (VS Code) 配置 MapStruct 生成的源码目录

set -e

echo "🔧 正在为 Cursor 设置 MapStruct 生成源码目录..."

# 更新 .vscode/settings.json
echo "📝 更新 Cursor 配置..."
cat > .vscode/settings.json << 'EOF'
{
    "java.configuration.exclusions": [
        
    ],
    "java.import.maven.enabled": true,
    "java.compile.nullAnalysis.mode": "automatic",
    "maven.executable.path": "mvn",
    "maven.pomfile.autoUpdate": false,
    "java.configuration.maven.userSettings": null,
    "java.import.exclusions": [
        
    ],
    "java.project.sourcePaths": [
        "cheers-framework",
        "cheers-module-system",
        "cheers-module-uxdesigner", 
        "cheers-module-dynamic-business",
        "cheers-module-infra"
    ],
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ],
    "java.configuration.updateBuildConfiguration": "automatic",
    "java.import.gradle.enabled": false,
    "java.project.exportJar.targetPath": "${workspaceFolder}/libs",
    "java.project.exportJar.sourcePaths": [
        "src/main/java"
    ],
    "java.project.exportJar.resourcePaths": [
        "src/main/resources"
    ],
    "java.project.exportJar.outputPath": "target/classes",
    "java.project.exportJar.includeTestSources": false,
    "java.project.exportJar.includeTestResources": false,
    "java.project.exportJar.includeReferencedProjects": true,
    "java.project.exportJar.includeReferencedLibraries": true,
    "java.project.exportJar.includeReferencedSources": true,
    "java.project.exportJar.includeReferencedResources": true,
    "java.project.exportJar.includeReferencedTestSources": false,
    "java.project.exportJar.includeReferencedTestResources": false,
    "java.project.exportJar.includeReferencedGeneratedSources": true,
    "java.project.exportJar.includeReferencedGeneratedTestSources": false,
    "java.project.exportJar.includeReferencedGeneratedResources": true,
    "java.project.exportJar.includeReferencedGeneratedTestResources": false,
    "java.project.exportJar.includeReferencedGeneratedSourcesPath": "target/generated-sources/annotations",
    "java.project.exportJar.includeReferencedGeneratedTestSourcesPath": "target/generated-test-sources/test-annotations",
    "maven.view": "flat",
    "java.configuration.maven.globalSettings": null,
    "files.exclude": {
        
    }
}
EOF

echo "✅ Cursor 配置已更新"

# 重新编译所有模块以确保生成源码目录存在
echo "🔨 重新编译所有模块..."
mvn clean compile -DskipTests -q

echo "📋 检查生成的源码目录..."
find . -path "*/target/generated-sources/annotations" -type d | while read -r dir; do
    module_dir=$(dirname $(dirname $(dirname "$dir")))
    echo "✓ 模块 $(basename "$module_dir") 的生成源码目录: $dir"
done

echo ""
echo "🎉 Cursor 设置完成！"
echo ""
echo "📋 接下来的步骤："
echo "1. 重启 Cursor"
echo "2. 重新加载 Java 语言服务器："
echo "   - 按 Cmd+Shift+P (Mac) 或 Ctrl+Shift+P (Windows/Linux)"
echo "   - 输入 'Java: Reload Projects' 并执行"
echo "3. 如果仍有问题，可以尝试："
echo "   - 'Java: Clean Java Language Server Workspace'"
echo "   - 'Java: Restart Language Server'"
echo ""
echo "💡 提示："
echo "- 确保安装了 'Extension Pack for Java' 扩展"
echo "- 确保安装了 'Maven for Java' 扩展"
echo "- 如果问题持续，可以删除 .vscode 目录后重新运行此脚本" 