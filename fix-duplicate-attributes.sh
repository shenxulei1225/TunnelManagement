#!/bin/bash

echo "开始修复重复的value属性问题..."

# 修复 el-option 中重复的 :value 属性
# 模式: :value="xxx" :value="yyy" -> :label="xxx" :value="yyy"
find tunnel-management-ui/src -name "*.vue" -type f -exec sed -i '' 's/:value="\([^"]*\)" :value="\([^"]*\)"/:label="\1" :value="\2"/g' {} +

# 修复 el-radio 中重复的 :value 属性 (通常第二个应该是label)
# 但需要更小心，因为有些可能是相同的值
echo "修复 el-radio 重复属性..."

# 特殊情况：:value="item.value" :value="item.label" -> :value="item.value" :label="item.label"
find tunnel-management-ui/src -name "*.vue" -type f -exec sed -i '' 's/:value="\([^"]*\)" :value="\([^"]*\.label\)"/:value="\1" :label="\2"/g' {} +

# 修复相同值的重复：:value="xxx" :value="xxx" -> :value="xxx"
find tunnel-management-ui/src -name "*.vue" -type f -exec sed -i '' 's/:value="\([^"]*\)" :value="\1"/:value="\1"/g' {} +

echo "修复完成！"

# 检查是否还有重复属性
echo "检查剩余的重复属性："
grep -c ":value.*:value" tunnel-management-ui/src --include="*.vue" | grep -v ":0" | head -10 