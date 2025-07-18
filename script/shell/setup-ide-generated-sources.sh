#!/bin/bash

# 设置 IDE 生成源码目录的脚本
# 这个脚本会自动将 MapStruct 生成的源码目录标记为 IDE 的源码目录

set -e

echo "正在设置 IDE 生成源码目录..."

# 查找所有包含 MapStruct 生成源码的模块
find . -path "*/target/generated-sources/annotations" -type d | while read -r dir; do
    module_dir=$(dirname $(dirname $(dirname "$dir")))
    echo "处理模块: $module_dir"
    
    # 创建 .idea 目录（如果不存在）
    mkdir -p "$module_dir/.idea"
    
    # 创建或更新 .idea/compiler.xml 文件
    cat > "$module_dir/.idea/compiler.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="CompilerConfiguration">
    <annotationProcessing>
      <profile name="Maven default annotation processors profile" enabled="true">
        <sourceOutputDir name="target/generated-sources/annotations" />
        <sourceTestOutputDir name="target/generated-test-sources/test-annotations" />
        <outputRelativeToContentRoot value="true" />
        <module name="$(basename "$module_dir")" />
      </profile>
    </annotationProcessing>
  </component>
</project>
EOF

    # 创建或更新 .idea/modules.xml 文件
    cat > "$module_dir/.idea/modules.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectModuleManager">
    <modules>
      <module fileurl="file://\$PROJECT_DIR\$/pom.xml" groupId="com.cheers.arch" artifactId="$(basename "$module_dir")" />
    </modules>
  </component>
</project>
EOF

    echo "✓ 已为 $module_dir 设置生成源码目录"
done

echo "完成！现在 IDE 应该能正确识别 MapStruct 生成的源码了。"
echo ""
echo "如果使用 IntelliJ IDEA，请："
echo "1. 重新导入项目"
echo "2. 或者手动右键点击 target/generated-sources/annotations 目录"
echo "3. 选择 'Mark Directory as' → 'Generated Sources Root'"
echo ""
echo "如果使用 VS Code，请："
echo "1. 重新加载窗口"
echo "2. 确保 Java Extension Pack 已安装" 