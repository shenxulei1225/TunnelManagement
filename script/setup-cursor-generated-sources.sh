#!/bin/bash

# 设置Cursor的生成源码目录配置脚本
# 确保MapStruct生成的实现类能被Cursor正确识别

echo "🔧 设置Cursor的生成源码目录配置..."

# 创建.vscode目录（如果不存在）
mkdir -p .vscode

# 更新settings.json配置
cat > .vscode/settings.json << 'EOF'
{
    "java.configuration.exclusions": [],
    "java.import.maven.enabled": true,
    "java.compile.nullAnalysis.mode": "automatic",
    "maven.executable.path": "mvn",
    "maven.pomfile.autoUpdate": false,
    "java.configuration.maven.userSettings": null,
    "java.import.exclusions": [],
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
    "java.project.outputPath": "target/classes",
    "java.project.generatedSourcePaths": [
        "target/generated-sources/annotations"
    ],
    "maven.view": "flat",
    "java.configuration.maven.globalSettings": null,
    "files.exclude": {},
    "java.autobuild.enabled": true,
    "java.saveActions.organizeImports": true,
    "java.completion.importOrder": [
        "java",
        "javax", 
        "jakarta",
        "com",
        "org"
    ]
}
EOF

echo "✅ Cursor配置已更新"
echo "📝 主要改进："
echo "   - 添加了 java.project.generatedSourcePaths 配置"
echo "   - 启用了 java.autobuild.enabled"
echo "   - 优化了Java项目路径配置"
echo ""
echo "🔄 请重启Cursor或重新加载Java项目"
echo "💡 提示：如果仍有问题，请检查根pom.xml中的build-helper-maven-plugin配置" 