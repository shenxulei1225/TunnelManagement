# Directory 服务测试指南

## 目标
验证**保留 Directory 服务、移除 Trees 抽象层**的实施效果。

## 测试环境准备

### 1. 启动后端服务
```bash
# 确保后端 DirectoryService 已实现并启动
cd cheers-server
mvn spring-boot:run
```

### 2. 启动前端服务
```bash
cd tunnel-management-ui
npm run dev
```

## 测试页面访问

### 方式一：专用测试页面
访问：`http://localhost:3000/test/directory`

### 方式二：系统管理页面
访问：`http://localhost:3000/system/directory`

## 测试功能清单

### ✅ 基础功能测试

**1. 目录数据加载**
- [ ] 切换不同业务类型（test_directory、dynamic_model、file_management）
- [ ] 验证数据正确加载
- [ ] 检查树形结构正确显示

**2. CRUD 操作**
- [ ] 创建根目录
- [ ] 创建子目录
- [ ] 编辑目录信息
- [ ] 删除目录（验证子目录检查）

**3. 树形操作**
- [ ] 展开/折叠节点
- [ ] 搜索过滤功能
- [ ] 节点选中高亮

### ✅ 拖拽功能测试

**1. 基本拖拽**
- [ ] 拖拽节点到其他节点内部（inner）
- [ ] 拖拽节点到其他节点前面（before）
- [ ] 拖拽节点到其他节点后面（after）

**2. 拖拽约束**
- [ ] 不能拖拽到自己
- [ ] 不能拖拽到自己的子节点
- [ ] 拖拽后数据结构正确更新

**3. 拖拽反馈**
- [ ] 拖拽过程中的视觉反馈
- [ ] 拖拽成功/失败的消息提示
- [ ] 拖拽后自动刷新数据

### ✅ API 接口测试

**验证以下 API 是否正常工作：**
- [ ] `GET /directory/tree?businessType=xxx` - 获取目录树
- [ ] `POST /directory/create` - 创建目录
- [ ] `PUT /directory/update` - 更新目录
- [ ] `DELETE /directory/delete?id=xxx` - 删除目录
- [ ] `PUT /directory/move` - 移动目录
- [ ] `GET /directory/children?businessType=xxx&parentId=xxx` - 获取子目录

## 测试步骤详解

### Step 1: 创建测试数据
1. 访问测试页面
2. 选择 "test_directory" 业务类型
3. 点击 **"创建测试数据"** 按钮
4. 观察日志输出是否显示创建成功
5. 验证右侧树形结构是否正确显示

### Step 2: 测试拖拽功能
1. 将 "子目录1" 拖拽到 "子目录2" 内部
2. 观察日志显示拖拽操作
3. 验证数据结构更新是否正确
4. 尝试不同的拖拽组合

### Step 3: 测试 CRUD 操作
1. 点击 **"新增目录"** 按钮
2. 填写目录信息并保存
3. 尝试编辑现有目录
4. 删除测试目录

### Step 4: 测试业务类型切换
1. 切换到 "dynamic_model" 业务类型
2. 验证加载对应的业务数据
3. 测试不同业务类型的隔离性

## 验证要点

### ✅ 技术架构验证
- [ ] **已移除 Trees 抽象层** - 代码中不再引用 `cheers-trees` 模块
- [ ] **使用 Element Plus 标准拖拽** - 直接使用 `el-tree` 的 `draggable` 属性
- [ ] **使用 MyBatis Plus 标准查询** - 后端使用 `LambdaQueryWrapper`
- [ ] **保留 Directory 服务价值** - 多业务类型统一管理

### ✅ 用户体验验证
- [ ] **拖拽操作流畅** - 无卡顿，反馈及时
- [ ] **树形展示美观** - 图标、缩进、样式正确
- [ ] **错误处理友好** - 操作失败有明确提示
- [ ] **数据实时更新** - 操作后立即反映变化

### ✅ 性能验证
- [ ] **加载速度** - 大量数据时加载是否及时
- [ ] **内存占用** - 长时间操作后无内存泄漏
- [ ] **网络请求** - API 调用次数合理

## 问题排查

### 常见问题及解决方案

**1. 页面无法访问**
```bash
# 检查前端服务是否启动
npm run dev
# 检查端口是否被占用
lsof -i :3000
```

**2. API 请求失败**
```bash
# 检查后端服务状态
curl http://localhost:8080/directory/tree?businessType=test_directory
# 检查控制台网络面板
```

**3. 拖拽功能异常**
- 检查浏览器控制台是否有 JavaScript 错误
- 验证 Element Plus 版本是否支持拖拽功能
- 确认 `allow-drag` 和 `allow-drop` 函数返回值

**4. 类型错误**
```typescript
// 确保类型定义正确
interface DirectoryNode {
  id?: number  // 注意可选类型
  name: string
  // ...
}
```

## 测试报告模板

### 测试结果记录
```markdown
## Directory 服务测试报告

**测试时间：** 2024-XX-XX
**测试人员：** XXX
**测试环境：** 开发环境

### 功能测试结果
- [x] 基础 CRUD 操作 ✅
- [x] 拖拽功能 ✅ 
- [x] 多业务类型支持 ✅
- [x] API 接口调用 ✅

### 技术架构验证
- [x] 已移除 Trees 抽象层 ✅
- [x] 使用标准 Element Plus Tree ✅
- [x] 使用标准 MyBatis Plus 查询 ✅

### 发现问题
1. 问题描述...
2. 解决方案...

### 总体评价
功能正常，符合设计要求。
```

## 菜单配置参数

**测试完成后，可以使用以下参数创建菜单：**
```json
{
  "name": "目录管理",
  "parentId": 0,
  "type": 2,
  "path": "/system/directory",
  "component": "system/directory/index",
  "componentName": "SystemDirectory",
  "permission": "system:directory:view",
  "icon": "ep:folder",
  "sort": 1,
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

## 总结

通过本次测试，验证了**保留 Directory 服务、移除 Trees 抽象层**的实施效果：

✅ **保持了实用性** - Directory 服务继续提供多业务类型统一管理  
✅ **提升了标准化** - 使用行业标准的 Element Plus 和 MyBatis Plus  
✅ **降低了复杂度** - 移除了不必要的 Trees 抽象层  
✅ **改善了维护性** - 代码更简洁，更易于理解和维护 