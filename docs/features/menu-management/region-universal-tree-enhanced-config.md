# 区域管理页面 - 增强版 UniversalTreeView 配置

## 概述

区域管理页面已经升级为增强版，支持拖拽、复制粘贴等高级功能。页面提供新旧组件切换功能，方便对比和测试。

## 新增功能

### 1. 拖拽功能
- **节点拖拽排序**：支持同级节点拖拽排序
- **跨级拖拽**：支持将节点拖拽到其他父节点下
- **拖拽动画**：300ms 平滑动画效果
- **拖拽样式**：半透明效果和视觉反馈

### 2. 复制粘贴功能
- **右键复制**：通过右键菜单复制节点
- **右键粘贴**：将复制的节点粘贴到目标位置
- **剪贴板管理**：支持跨节点复制粘贴操作
- **操作反馈**：复制和粘贴成功提示

### 3. 右键菜单
- **添加子项**：在选中节点下添加子区域
- **编辑节点**：编辑选中的区域信息
- **复制节点**：复制选中节点到剪贴板
- **粘贴节点**：粘贴剪贴板中的节点（需要先复制）
- **删除节点**：删除选中的区域

## 功能配置

### 拖拽配置
```typescript
dragConfig: {
  draggable: true,
  animation: 300,
  ghostClass: 'ghost-class',
  chosenClass: 'chosen-class',
  dragClass: 'drag-class'
}
```

### 交互配置
```typescript
interactionConfig: {
  editable: true,
  deletable: true,
  addable: true,
  copyable: true,
  showContextMenu: true,
  highlightCurrent: true
}
```

### 文本配置
```typescript
textConfig: {
  emptyText: '暂无区域数据',
  noResultsText: '没有找到匹配的区域',
  expandAllText: '展开全部',
  collapseAllText: '收起全部',
  addText: '添加子区域',
  editText: '编辑区域',
  deleteText: '删除区域',
  copyText: '复制区域',
  pasteText: '粘贴区域',
  clearSearchText: '清除搜索'
}
```

## 事件处理

### 基础事件
- `@node-click`: 节点点击事件，更新选中状态
- `@node-expand`: 节点展开事件
- `@node-collapse`: 节点折叠事件

### 操作事件
- `@node-edit`: 节点编辑事件，打开编辑表单
- `@node-delete`: 节点删除事件，确认后删除
- `@node-add`: 节点添加事件，打开新增表单

### 高级事件
- `@node-copy`: 节点复制事件，复制到剪贴板
- `@node-paste`: 节点粘贴事件，从剪贴板粘贴
- `@drag-end`: 拖拽结束事件，更新节点顺序

## 样式特性

### 拖拽样式
```scss
// 拖拽时的半透明效果
.ghost-class {
  opacity: 0.5;
  background-color: var(--el-color-primary-light-9);
}

// 被选中拖拽的节点样式
.chosen-class {
  background-color: var(--el-color-primary-light-8);
}

// 拖拽过程中的样式
.drag-class {
  transform: rotate(5deg);
  background-color: var(--el-color-primary-light-7);
}
```

### 表格样式
- 保持原有的表格外观
- 支持行悬停效果
- 支持行选中高亮
- 响应式布局适配

## 菜单配置参数

### 基础配置
- **路径**: `/system/region`
- **组件**: `SystemRegion`
- **权限**: `system:region:query`

### 扩展权限
- **新增**: `system:region:create`
- **编辑**: `system:region:update`
- **删除**: `system:region:delete`
- **导出**: `system:region:export`

### 菜单项配置
```javascript
{
  id: 'region-management',
  parentId: 'system',
  path: '/system/region',
  name: 'SystemRegion',
  component: 'system/region/index',
  meta: {
    title: '区域管理',
    icon: 'ep:location',
    permissions: ['system:region:query'],
    cache: true,
    affix: false
  }
}
```

## 使用说明

### 1. 基本操作
- **查看**: 点击节点查看详情
- **展开**: 点击展开图标或节点名称
- **搜索**: 使用搜索框快速定位

### 2. 编辑操作
- **新增**: 点击新增按钮或右键菜单
- **编辑**: 点击编辑按钮或右键菜单
- **删除**: 点击删除按钮或右键菜单

### 3. 高级操作
- **拖拽**: 直接拖拽节点到目标位置
- **复制**: 右键选择复制，然后在目标位置粘贴
- **批量操作**: 支持展开/折叠所有节点

## 兼容性

### 浏览器支持
- Chrome 80+
- Firefox 75+
- Safari 13+
- Edge 80+

### 移动端支持
- 响应式布局适配
- 触摸拖拽支持
- 移动端右键菜单

## 性能优化

### 虚拟滚动
- 大数据量时启用虚拟滚动
- 减少DOM节点数量
- 提升渲染性能

### 懒加载
- 支持节点懒加载
- 按需加载子节点数据
- 减少初始加载时间

## 测试要点

### 功能测试
- [x] 拖拽排序功能
- [x] 复制粘贴功能
- [x] 右键菜单功能
- [x] 新旧组件切换
- [x] 数据展示正确性

### 兼容性测试
- [x] 不同浏览器兼容性
- [x] 移动端适配
- [x] 权限控制正确性
- [x] 样式一致性

### 性能测试
- [x] 大数据量渲染
- [x] 拖拽操作流畅性
- [x] 内存使用情况
- [x] 网络请求优化

## 后续规划

1. **API 集成**: 将拖拽和复制操作与后端API集成
2. **批量操作**: 支持多选和批量操作
3. **导入导出**: 支持区域数据的导入导出
4. **历史记录**: 支持操作历史记录和撤销
5. **权限细化**: 更细粒度的权限控制 