#!/bin/bash

echo "正在搜索重复的:value属性..."

# 搜索可能有重复:value属性的文件
echo "=== 搜索重复的:value属性 ==="
grep -rn ":value.*:value" tunnel-management-ui/src --include="*.vue" | head -20

echo ""
echo "=== 搜索重复的:label属性 ==="
grep -rn ":label.*:label" tunnel-management-ui/src --include="*.vue" | head -10

echo ""
echo "=== 搜索重复的:key属性 ==="  
grep -rn ":key.*:key" tunnel-management-ui/src --include="*.vue" | head -10

echo "搜索完成！" 