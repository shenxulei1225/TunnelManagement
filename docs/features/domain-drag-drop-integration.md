# Domain领域模型拖拽功能集成指南

## 概述

Domain领域模型已支持完整的拖拽功能，包括树形结构的节点移动、排序调整等。本文档介绍如何在前端集成这些拖拽功能。

## 🎯 功能特性

### 1. 拖拽移动
- ✅ 支持跨父级移动
- ✅ 支持同级排序调整
- ✅ 自动维护树形结构完整性
- ✅ 防止循环依赖
- ✅ 排序自动重新计算

### 2. 批量排序
- ✅ 支持批量更新排序
- ✅ 支持拖拽后的排序优化

## 🔧 API 接口

### 拖拽移动接口

```http
POST /system/domain/move
```

**请求参数：**
```json
{
  "id": 3,                // 移动的领域ID
  "targetParentId": 2,    // 目标父领域ID（0表示根级）
  "targetSort": 1         // 目标排序位置（可选，不传则放到最后）
}
```

**响应：**
```json
{
  "code": 0,
  "data": true,
  "msg": "操作成功"
}
```

### 批量排序接口

```http
POST /system/domain/batch-sort?parentId=0
```

**请求参数：**
```json
{
  "1": 2,    // 领域ID: 新排序值
  "2": 1
}
```

## 🎨 前端集成示例

### Vue 3 + Element Plus 集成

```vue
<template>
  <el-tree
    :data="domainTree"
    node-key="id"
    draggable
    :allow-drop="allowDrop"
    :allow-drag="allowDrag"
    @node-drop="handleNodeDrop"
  >
    <template #default="{ node, data }">
      <span class="custom-tree-node">
        <span>{{ data.name }}</span>
      </span>
    </template>
  </el-tree>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DomainApi } from '@/api/system/domain'

const domainTree = ref([])

// 获取领域树数据
const loadDomainTree = async () => {
  const response = await DomainApi.getDomainList()
  domainTree.value = buildTree(response.data)
}

// 允许拖拽判断
const allowDrag = (draggingNode) => {
  // 可以添加自定义拖拽限制逻辑
  return true
}

// 允许放置判断
const allowDrop = (draggingNode, dropNode, type) => {
  // 防止拖拽到自己的子节点
  if (isDescendant(draggingNode.data, dropNode.data)) {
    return false
  }
  return true
}

// 拖拽完成处理
const handleNodeDrop = async (draggingNode, dropNode, dropType, ev) => {
  try {
    let targetParentId
    let targetSort
    
    if (dropType === 'before' || dropType === 'after') {
      // 同级排序
      targetParentId = dropNode.parent?.data?.id || 0
      const siblings = dropNode.parent?.childNodes || []
      targetSort = dropType === 'before' 
        ? dropNode.data.sort 
        : dropNode.data.sort + 1
    } else {
      // 移动到子级
      targetParentId = dropNode.data.id
      targetSort = 1
    }
    
    await DomainApi.moveDomain({
      id: draggingNode.data.id,
      targetParentId,
      targetSort
    })
    
    // 重新加载数据
    await loadDomainTree()
    
    ElMessage.success('移动成功')
  } catch (error) {
    ElMessage.error('移动失败：' + error.message)
    // 恢复原始状态
    await loadDomainTree()
  }
}

// 判断是否为子孙节点
const isDescendant = (ancestor, node) => {
  // 实现检查逻辑
  return false
}

// 构建树形结构
const buildTree = (list) => {
  // 实现树形结构构建逻辑
  return []
}

onMounted(() => {
  loadDomainTree()
})
</script>
```

### React + Ant Design 集成

```jsx
import React, { useState, useEffect } from 'react'
import { Tree, message } from 'antd'
import { DomainApi } from '@/api/system/domain'

const DomainTree = () => {
  const [treeData, setTreeData] = useState([])

  const loadData = async () => {
    try {
      const response = await DomainApi.getDomainList()
      setTreeData(buildTree(response.data))
    } catch (error) {
      message.error('加载失败')
    }
  }

  const onDrop = async (info) => {
    const { dragNode, node, dropPosition, dropToGap } = info
    
    try {
      let targetParentId
      let targetSort
      
      if (dropToGap) {
        // 同级排序
        targetParentId = node.parent?.id || 0
        targetSort = dropPosition
      } else {
        // 移动到子级
        targetParentId = node.id
        targetSort = 1
      }
      
      await DomainApi.moveDomain({
        id: dragNode.id,
        targetParentId,
        targetSort
      })
      
      await loadData()
      message.success('移动成功')
    } catch (error) {
      message.error('移动失败：' + error.message)
      await loadData()
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  return (
    <Tree
      draggable
      treeData={treeData}
      onDrop={onDrop}
      titleRender={(nodeData) => nodeData.name}
    />
  )
}

export default DomainTree
```

## 📝 注意事项

### 1. 拖拽限制
- 不能将节点拖拽到自己的子孙节点下
- 系统会自动校验循环依赖
- 根据权限控制拖拽功能

### 2. 排序处理
- 系统会自动重新计算排序值
- 拖拽后会消除排序间隙
- 支持同级内部调整和跨级移动

### 3. 错误处理
- 拖拽失败时应恢复原始状态
- 提供用户友好的错误提示
- 记录操作日志

### 4. 性能优化
- 大量节点时考虑虚拟滚动
- 拖拽时可以显示loading状态
- 批量操作时使用防抖

## 🔗 相关API

- `GET /system/domain/list` - 获取领域列表
- `POST /system/domain/move` - 拖拽移动
- `POST /system/domain/batch-sort` - 批量排序
- `POST /system/domain/create` - 创建领域
- `PUT /system/domain/update` - 更新领域
- `DELETE /system/domain/delete` - 删除领域

## 📋 菜单配置参数

创建Domain拖拽管理页面的菜单配置：

```json
{
  "name": "领域管理",
  "type": 2,
  "sort": 4010,
  "parentId": 4000,
  "path": "domain-tree",
  "icon": "ep:connection",
  "component": "system/domain/tree/index",
  "componentName": "SystemDomainTree",
  "permission": "system:domain:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

权限配置：
- `system:domain:query` - 查询权限
- `system:domain:update` - 拖拽移动权限
- `system:domain:create` - 新增权限  
- `system:domain:delete` - 删除权限 