# 操作日志控件开发文档

> 本文档提供操作日志控件(OperationLog)的技术实现方案、API接口文档和开发指导。

## 📋 技术概述

### 技术栈
- **框架**：Vue 3 + TypeScript
- **UI库**：Element Plus
- **状态管理**：Pinia (可选)
- **工具库**：dayjs (时间处理)、file-saver (文件导出)

### 核心特性
- ✅ 四种日志级别 (success, info, warning, error)
- ✅ 实时日志展示和搜索过滤
- ✅ 详细信息展开/折叠
- ✅ 数据导出 (JSON/TXT格式)
- ✅ 完整的TypeScript类型支持
- ✅ 响应式设计和深色模式支持

## 🏗️ 架构设计

### 组件层次结构
```
OperationLog (容器组件)
├── LogToolbar (工具栏组件)
│   ├── SearchInput (搜索输入框)
│   ├── ClearButton (清空按钮)
│   └── ExportButton (导出按钮)
└── LogList (日志列表组件)
    └── LogItem (日志条目组件)
        ├── LogIcon (级别图标)
        ├── LogTime (时间戳)
        ├── LogMessage (消息内容)
        └── LogDetail (详细信息)
```

### 数据流设计
```
Props (外部数据) → Component State (内部状态) → Events (事件发射)
                        ↓
                 Computed Values (计算属性)
                        ↓
                 Template Render (模板渲染)
```

## 🔧 API接口文档

### Props 属性

```typescript
interface OperationLogProps {
  /** 日志数据数组 */
  logs?: LogEntry[]
  /** 组件高度 */
  height?: string | number
  /** 最大显示日志数量 */
  maxLogs?: number
  /** 是否显示时间戳 */
  showTimestamp?: boolean
  /** 是否显示详细信息 */
  showDetails?: boolean
  /** 是否支持搜索 */
  searchable?: boolean
  /** 是否支持导出 */
  exportable?: boolean
  /** 自定义图标映射 */
  iconMap?: Record<LogLevel, string>
}
```

### LogEntry 类型定义

```typescript
interface LogEntry {
  /** 唯一标识符 */
  id: string
  /** 日志级别 */
  level: LogLevel
  /** 日志消息 */
  message: string
  /** 详细信息 */
  details?: string
  /** 时间戳 */
  timestamp: Date
  /** 自定义图标 */
  icon?: string
  /** 额外数据 */
  data?: any
}

type LogLevel = 'success' | 'info' | 'warning' | 'error'
```

### Events 事件

```typescript
interface OperationLogEvents {
  /** 日志条目点击事件 */
  'log-click': (log: LogEntry) => void
  /** 搜索事件 */
  'search': (keyword: string) => void
  /** 清空事件 */
  'clear': () => void
  /** 导出事件 */
  'export': (data: LogEntry[], format: 'json' | 'txt') => void
}
```

### Methods 方法

```typescript
interface OperationLogMethods {
  /** 添加日志 */
  addLog(log: Omit<LogEntry, 'id' | 'timestamp'>): void
  /** 清空所有日志 */
  clearLogs(): void
  /** 导出日志数据 */
  exportLogs(format: 'json' | 'txt'): void
  /** 搜索日志 */
  searchLogs(keyword: string): void
  /** 滚动到底部 */
  scrollToBottom(): void
}
```

## 💻 代码实现

### 基础使用示例

```vue
<template>
  <div class="log-demo">
    <h2>操作日志示例</h2>
    
    <!-- 基础用法 -->
    <OperationLog 
      :logs="logs"
      height="400px"
      :max-logs="100"
      searchable
      exportable
      @log-click="handleLogClick"
      @clear="handleClear"
    />
    
    <!-- 添加日志按钮 -->
    <div class="log-actions">
      <el-button @click="addSuccessLog" type="success">添加成功日志</el-button>
      <el-button @click="addInfoLog" type="info">添加信息日志</el-button>
      <el-button @click="addWarningLog" type="warning">添加警告日志</el-button>
      <el-button @click="addErrorLog" type="danger">添加错误日志</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import OperationLog from '@/components/Common/OperationLog.vue'
import type { LogEntry } from '@/types/log'

const logs = ref<LogEntry[]>([])

// 添加不同级别的日志
const addSuccessLog = () => {
  logs.value.push({
    id: Date.now().toString(),
    level: 'success',
    message: '操作成功完成',
    details: '数据保存成功，共处理了100条记录',
    timestamp: new Date()
  })
}

const addInfoLog = () => {
  logs.value.push({
    id: Date.now().toString(),
    level: 'info',
    message: '系统信息',
    details: '当前用户：admin，登录时间：2024-01-20 10:30:00',
    timestamp: new Date()
  })
}

const addWarningLog = () => {
  logs.value.push({
    id: Date.now().toString(),
    level: 'warning',
    message: '操作警告',
    details: '磁盘空间不足，建议清理缓存文件',
    timestamp: new Date()
  })
}

const addErrorLog = () => {
  logs.value.push({
    id: Date.now().toString(),
    level: 'error',
    message: '操作失败',
    details: '网络连接超时，请检查网络设置后重试',
    timestamp: new Date()
  })
}

// 事件处理
const handleLogClick = (log: LogEntry) => {
  console.log('点击日志:', log)
}

const handleClear = () => {
  logs.value = []
  console.log('日志已清空')
}
</script>
```

### 高级配置示例

```vue
<template>
  <OperationLog 
    :logs="logs"
    height="500px"
    :max-logs="500"
    :show-timestamp="true"
    :show-details="true"
    :searchable="true"
    :exportable="true"
    :icon-map="customIconMap"
    @log-click="onLogClick"
    @search="onSearch"
    @export="onExport"
  />
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

// 自定义图标映射
const customIconMap = {
  success: 'ep:circle-check',
  info: 'ep:info-filled',
  warning: 'ep:warning-filled',
  error: 'ep:circle-close-filled'
}

// 计算属性示例
const filteredLogs = computed(() => {
  // 自定义过滤逻辑
  return logs.value.filter(log => {
    // 过滤条件
    return true
  })
})

// 事件处理函数
const onLogClick = (log: LogEntry) => {
  // 处理日志点击
}

const onSearch = (keyword: string) => {
  // 处理搜索
}

const onExport = (data: LogEntry[], format: string) => {
  // 处理导出
}
</script>
```

## 🎨 样式自定义

### CSS变量定义

```scss
.operation-log {
  // 基础变量
  --log-bg: #ffffff;
  --log-border: #e4e7ed;
  --log-text: #303133;
  --log-text-secondary: #909399;
  
  // 级别颜色变量
  --log-success: #67c23a;
  --log-success-bg: #f0f9ef;
  --log-success-border: #c2e7b0;
  
  --log-info: #909399;
  --log-info-bg: #f4f4f5;
  --log-info-border: #d3d4d6;
  
  --log-warning: #e6a23c;
  --log-warning-bg: #fdf6ec;
  --log-warning-border: #f5dab1;
  
  --log-error: #f56565;
  --log-error-bg: #fef0f0;
  --log-error-border: #fbc4c4;
  
  // 深色模式变量
  @media (prefers-color-scheme: dark) {
    --log-bg: #1e1e1e;
    --log-border: #333333;
    --log-text: #ffffff;
    --log-text-secondary: #cccccc;
  }
}
```

### 自定义样式覆盖

```scss
// 覆盖默认样式
.my-custom-log {
  .operation-log {
    --log-bg: #f8f9fa;
    --log-text: #495057;
    
    .log-item {
      border-radius: 8px;
      margin-bottom: 8px;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
    }
  }
}
```

## 🧪 测试指南

### 单元测试示例

```typescript
import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import OperationLog from '@/components/Common/OperationLog.vue'
import type { LogEntry } from '@/types/log'

describe('OperationLog', () => {
  const mockLogs: LogEntry[] = [
    {
      id: '1',
      level: 'success',
      message: '测试成功消息',
      timestamp: new Date('2024-01-20T10:00:00Z')
    },
    {
      id: '2',
      level: 'error',
      message: '测试错误消息',
      details: '详细错误信息',
      timestamp: new Date('2024-01-20T10:01:00Z')
    }
  ]

  it('应该正确渲染日志列表', () => {
    const wrapper = mount(OperationLog, {
      props: {
        logs: mockLogs
      }
    })
    
    expect(wrapper.findAll('.log-item')).toHaveLength(2)
    expect(wrapper.text()).toContain('测试成功消息')
    expect(wrapper.text()).toContain('测试错误消息')
  })

  it('应该支持搜索功能', async () => {
    const wrapper = mount(OperationLog, {
      props: {
        logs: mockLogs,
        searchable: true
      }
    })
    
    const searchInput = wrapper.find('input[type="search"]')
    await searchInput.setValue('成功')
    
    expect(wrapper.findAll('.log-item')).toHaveLength(1)
    expect(wrapper.text()).toContain('测试成功消息')
  })

  it('应该发射正确的事件', async () => {
    const wrapper = mount(OperationLog, {
      props: {
        logs: mockLogs
      }
    })
    
    await wrapper.find('.log-item').trigger('click')
    
    expect(wrapper.emitted('log-click')).toBeTruthy()
    expect(wrapper.emitted('log-click')?.[0]).toEqual([mockLogs[0]])
  })
})
```

### E2E测试示例

```typescript
import { test, expect } from '@playwright/test'

test('操作日志功能测试', async ({ page }) => {
  await page.goto('/test/operation-log')
  
  // 测试日志添加
  await page.click('button:has-text("添加成功日志")')
  await expect(page.locator('.log-item')).toHaveCount(1)
  
  // 测试搜索功能
  await page.fill('input[placeholder="搜索日志..."]', '成功')
  await expect(page.locator('.log-item')).toHaveCount(1)
  
  // 测试清空功能
  await page.click('button:has-text("清空")')
  await expect(page.locator('.log-item')).toHaveCount(0)
})
```

## 📦 构建和部署

### 组件导出配置

```typescript
// components/Common/index.ts
export { default as OperationLog } from './OperationLog.vue'
export type { LogEntry, LogLevel } from './types'
```

### 全局注册

```typescript
// main.ts
import { createApp } from 'vue'
import OperationLog from '@/components/Common/OperationLog.vue'

const app = createApp(App)

// 全局注册组件
app.component('OperationLog', OperationLog)
```

### 按需导入配置

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import { resolve } from 'path'

export default defineConfig({
  build: {
    lib: {
      entry: resolve(__dirname, 'src/components/Common/index.ts'),
      name: 'OperationLog',
      fileName: 'operation-log'
    },
    rollupOptions: {
      external: ['vue', 'element-plus'],
      output: {
        globals: {
          vue: 'Vue',
          'element-plus': 'ElementPlus'
        }
      }
    }
  }
})
```

## 🔧 调试和故障排查

### 调试工具

```typescript
// 开发环境调试功能
const debugMode = import.meta.env.DEV

const logDebug = (message: string, data?: any) => {
  if (debugMode) {
    console.log(`[OperationLog Debug] ${message}`, data)
  }
}

// 在组件中使用
logDebug('日志添加', newLog)
logDebug('搜索执行', { keyword, results: filteredLogs.value.length })
```

### 常见问题解决

#### 1. 日志不显示问题
```typescript
// 检查数据格式
const validateLogEntry = (log: any): log is LogEntry => {
  return (
    typeof log.id === 'string' &&
    ['success', 'info', 'warning', 'error'].includes(log.level) &&
    typeof log.message === 'string' &&
    log.timestamp instanceof Date
  )
}
```

#### 2. 性能问题优化
```vue
<template>
  <!-- 使用虚拟滚动优化大量数据 -->
  <el-virtual-list
    :data="filteredLogs"
    :height="400"
    :item-size="60"
    class="log-list"
  >
    <template #default="{ item, index }">
      <LogItem :log="item" :index="index" />
    </template>
  </el-virtual-list>
</template>
```

#### 3. 内存泄漏预防
```typescript
// 清理定时器和事件监听器
onUnmounted(() => {
  // 清理资源
  if (autoScrollTimer) {
    clearInterval(autoScrollTimer)
  }
  
  // 移除事件监听器
  window.removeEventListener('resize', handleResize)
})
```

## 📈 性能优化

### 虚拟滚动实现

```vue
<script setup lang="ts">
import { computed, ref } from 'vue'

const containerHeight = 400
const itemHeight = 60
const visibleCount = Math.ceil(containerHeight / itemHeight)
const scrollTop = ref(0)

const visibleLogs = computed(() => {
  const start = Math.floor(scrollTop.value / itemHeight)
  const end = start + visibleCount + 1
  return filteredLogs.value.slice(start, end)
})

const handleScroll = (e: Event) => {
  scrollTop.value = (e.target as HTMLElement).scrollTop
}
</script>
```

### 防抖优化

```typescript
import { debounce } from 'lodash-es'

const debouncedSearch = debounce((keyword: string) => {
  searchKeyword.value = keyword
}, 300)
```

## 🔄 版本迁移指南

### v1.0 → v1.1 迁移

```typescript
// 旧版本用法
<OperationLog :data="logs" />

// 新版本用法
<OperationLog :logs="logs" />
```

### 破坏性变更说明

1. **Props名称变更**：`data` → `logs`
2. **事件名称标准化**：`item-click` → `log-click`
3. **类型定义更新**：增加了更严格的类型约束

---

## 🎯 菜单配置参数

当集成操作日志控件到页面时，使用以下菜单配置参数：

```json
{
  "name": "操作日志控件",
  "type": 2,
  "sort": 2100,
  "parentId": 1,
  "path": "operation-log-dev",
  "icon": "ep:document-copy",
  "component": "development/operation-log/index",
  "componentName": "OperationLogDev",
  "permission": "dev:log:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

---

## 📚 相关文档

- [操作日志控件设计文档](../design/operation-log-design.md)
- [操作日志控件使用说明](../../tunnel-management-ui/src/components/Common/OperationLogUsage.md)
- [标签颜色系统开发文档](./tag-color-system-development.md)
- [核心开发指南](./core-development-guide.md)

---

*本开发文档将随功能迭代持续更新，如有技术问题请联系开发团队。* 