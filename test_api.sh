#!/bin/bash

echo "测试API: /admin-api/system/tree-data/data/field_category"

# 测试API是否可访问
curl -X GET "http://localhost:48080/admin-api/system/tree-data/data/field_category" \
  -H "Content-Type: application/json" \
  -v

echo ""
echo "测试完成" 