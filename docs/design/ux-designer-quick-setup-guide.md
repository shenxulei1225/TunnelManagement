# 🚀 UX Designer UI转换器快速安装指南

> **📋 Quick Setup Guide for UX Designer UI Converter**  
> 快速搭建和配置UI转换器开发环境  
> 配套文档：[UX Designer UI转换器功能设计文档](ux-designer-ui-converter-design.md)

---

## ⚡ 一键快速安装

### 🎯 安装前准备

**环境要求：**
- Node.js >= 16.0.0
- MySQL >= 8.0 (或其他支持的数据库)
- 有效的菜单管理权限

**必要权限：**
- `system:menu:create` - 菜单创建权限
- `system:menu:update` - 菜单更新权限  
- `system:menu:query` - 菜单查询权限

---

## 📋 步骤1：菜单快速创建

### 方式一：SQL脚本执行 (推荐)

1. **执行菜单创建脚本**
```sql
-- 进入MySQL命令行
mysql -u [username] -p [database_name]

-- 执行UI转换器菜单脚本
source sql/mysql/ui_converter_menu.sql;

-- 验证菜单创建结果
SELECT name, type, path, component FROM system_menu 
WHERE name LIKE '%UI转换器%' OR path LIKE '%ui-converter%'
ORDER BY sort;
```

2. **验证菜单创建成功**
```sql
-- 应该看到以下菜单结构：
/*
UI转换器 (主菜单)
├── 资源导入
├── 组件树管理  
├── 可视化设计器
├── 属性编辑器
├── 代码导出
└── 预览中心
    (每个子菜单包含5个权限按钮)
*/
```

### 方式二：API自动创建

```typescript
// 在开发环境中运行
import { createUIConverterMenus } from '@/utils/menu-creator'

// 自动创建所有菜单
const result = await createUIConverterMenus()
console.log('菜单创建结果:', result)
```

---

## 🗂️ 步骤2：文件结构创建

### 创建核心目录结构

```bash
# 创建核心模块目录
mkdir -p tunnel-management-ui/src/core/converter/{types,importers,converters,utils,generators}

# 创建UI组件目录
mkdir -p tunnel-management-ui/src/components/UIConverter

# 创建页面视图目录  
mkdir -p tunnel-management-ui/src/views/ui-converter/{import,component-tree,design-canvas,property-panel,export,preview}

# 创建样式目录
mkdir -p tunnel-management-ui/src/styles/ui-converter

# 创建测试目录
mkdir -p tunnel-management-ui/src/tests/ui-converter
```

### 创建基础文件模板

```bash
# 创建核心类型定义文件
cat > tunnel-management-ui/src/core/converter/types/index.ts << 'EOF'
/**
 * UI转换器核心类型定义
 * @author UX开发团队
 * @date 2024-12-28
 */

// 统一设计节点格式
export interface DesignNode {
  id: string
  name: string
  type: ComponentType
  styles: StyleProperties
  layout: LayoutProperties  
  properties: ComponentProperties
  children: DesignNode[]
  metadata: NodeMetadata
}

// 导入器插件接口
export interface ImporterPlugin {
  name: string
  version: string
  supportedFormats: string[]
  validate(source: ImportSource): Promise<boolean>
  parse(source: ImportSource): Promise<DesignNode[]>
}

// 转换器插件接口
export interface ConverterPlugin {
  name: string
  version: string
  targetPlatform: string
  convert(designTree: DesignNode[], options: ConvertOptions): Promise<ConvertResult>
}

// TODO: 补充完整的类型定义
EOF

# 创建主入口文件
cat > tunnel-management-ui/src/views/ui-converter/index.vue << 'EOF'
<!--
  UI转换器主界面
  @description 统一的UI设计转换工作台
  @author UX开发团队
  @date 2024-12-28
-->
<template>
  <div class="ui-converter-workspace">
    <div class="converter-toolbar">
      <el-button-group>
        <el-button @click="showImportDialog">
          <el-icon><Upload /></el-icon>
          导入设计
        </el-button>
        <el-button @click="saveDesign">
          <el-icon><Document /></el-icon>
          保存
        </el-button>
        <el-button @click="showExportDialog">
          <el-icon><Download /></el-icon>
          导出代码
        </el-button>
        <el-button @click="showPreviewDialog">
          <el-icon><View /></el-icon>
          预览
        </el-button>
      </el-button-group>
    </div>
    
    <div class="converter-main">
      <!-- TODO: 实现完整的三栏布局 -->
      <div class="placeholder-content">
        <el-result
          icon="info"
          title="UI转换器开发中"
          sub-title="请参考设计文档进行开发"
        >
          <template #extra>
            <el-button type="primary" @click="openDesignDoc">
              查看设计文档
            </el-button>
          </template>
        </el-result>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, Document, Download, View } from '@element-plus/icons-vue'

// TODO: 实现完整的转换器逻辑

const showImportDialog = () => {
  ElMessage.info('导入功能开发中...')
}

const saveDesign = () => {
  ElMessage.info('保存功能开发中...')
}

const showExportDialog = () => {
  ElMessage.info('导出功能开发中...')
}

const showPreviewDialog = () => {
  ElMessage.info('预览功能开发中...')
}

const openDesignDoc = () => {
  window.open('/docs/design/ux-designer-ui-converter-design.md', '_blank')
}
</script>

<style scoped lang="scss">
.ui-converter-workspace {
  height: 100vh;
  display: flex;
  flex-direction: column;
  
  .converter-toolbar {
    height: 60px;
    padding: 10px 20px;
    border-bottom: 1px solid var(--el-border-color);
    background: var(--el-bg-color);
  }
  
  .converter-main {
    flex: 1;
    display: flex;
    
    .placeholder-content {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}
</style>
EOF
```

---

## 🔗 步骤3：路由配置

### 添加到路由系统

```typescript
// tunnel-management-ui/src/router/modules/ui-converter.ts

export default [
  {
    path: '/ui-converter',
    component: () => import('@/layout/index.vue'),
    name: 'UIConverter',
    meta: {
      title: 'UI转换器',
      icon: 'ep:magic-stick',
      alwaysShow: true
    },
    children: [
      {
        path: '',
        component: () => import('@/views/ui-converter/index.vue'),
        name: 'UIConverterMain',
        meta: {
          title: '转换工作台',
          icon: 'ep:magic-stick',
          noCache: false
        }
      },
      {
        path: 'import',
        component: () => import('@/views/ui-converter/import/index.vue'),
        name: 'UIConverterImport',
        meta: {
          title: '资源导入',
          icon: 'ep:upload',
          noCache: false
        }
      },
      {
        path: 'component-tree',
        component: () => import('@/views/ui-converter/component-tree/index.vue'),
        name: 'UIConverterComponentTree',
        meta: {
          title: '组件树管理',
          icon: 'ep:tree',
          noCache: false
        }
      },
      {
        path: 'design-canvas',
        component: () => import('@/views/ui-converter/design-canvas/index.vue'),
        name: 'UIConverterDesignCanvas',
        meta: {
          title: '可视化设计器',
          icon: 'ep:edit',
          noCache: false
        }
      },
      {
        path: 'property-panel',
        component: () => import('@/views/ui-converter/property-panel/index.vue'),
        name: 'UIConverterPropertyPanel',
        meta: {
          title: '属性编辑器',
          icon: 'ep:setting',
          noCache: false
        }
      },
      {
        path: 'export',
        component: () => import('@/views/ui-converter/export/index.vue'),
        name: 'UIConverterExport',
        meta: {
          title: '代码导出',
          icon: 'ep:download',
          noCache: false
        }
      },
      {
        path: 'preview',
        component: () => import('@/views/ui-converter/preview/index.vue'),
        name: 'UIConverterPreview',
        meta: {
          title: '预览中心',
          icon: 'ep:view',
          noCache: false
        }
      }
    ]
  }
] as RouteRecordRaw[]
```

---

## ✅ 步骤4：验证安装结果

### 检查菜单是否正确创建

1. **登录管理后台**
2. **访问系统管理 → 菜单管理**
3. **查看是否存在"UI转换器"菜单及其子菜单**
4. **验证权限配置是否完整**

### 检查页面是否可以访问

1. **访问主工作台**: `http://localhost/ui-converter`
2. **检查各子模块页面是否可以正常路由**
3. **验证界面布局是否正确显示**

### 开发环境测试

```bash
# 启动开发服务器
cd tunnel-management-ui
npm run dev

# 访问UI转换器模块
open http://localhost:3000/ui-converter
```

---

## 🐛 常见问题解决

### 问题1：菜单创建失败

**现象：** SQL脚本执行报错或菜单未显示

**解决方案：**
```sql
-- 检查system_menu表结构
DESC system_menu;

-- 检查是否有权限创建菜单
SELECT permission FROM system_menu WHERE name LIKE '%菜单%';

-- 手动创建主菜单（如果脚本失败）
INSERT INTO system_menu (name, type, sort, parent_id, path, icon, status, visible, keep_alive, always_show) 
VALUES ('UI转换器', 1, 6000, 0, 'ui-converter', 'ep:magic-stick', 0, 1, 0, 1);
```

### 问题2：路由访问404

**现象：** 访问ui-converter页面显示404

**解决方案：**
```typescript
// 检查路由是否正确注册
// 在 tunnel-management-ui/src/router/index.ts 中添加：

import uiConverterRoutes from './modules/ui-converter'

// 确保路由被正确导入和注册
const routes = [
  // ... 其他路由
  ...uiConverterRoutes
]
```

### 问题3：组件文件不存在

**现象：** 路由配置的组件文件找不到

**解决方案：**
```bash
# 创建缺失的组件文件
touch tunnel-management-ui/src/views/ui-converter/import/index.vue
touch tunnel-management-ui/src/views/ui-converter/component-tree/index.vue
# ... 创建其他缺失的文件

# 或使用脚本批量创建
for page in import component-tree design-canvas property-panel export preview; do
  mkdir -p "tunnel-management-ui/src/views/ui-converter/$page"
  echo '<template><div>{{ $route.meta.title }}页面开发中...</div></template>' > "tunnel-management-ui/src/views/ui-converter/$page/index.vue"
done
```

---

## 📋 下一步开发计划

### 立即开始的任务

1. **完善类型定义** (优先级：高)
   - 补充完整的TypeScript接口定义
   - 建立严格的类型检查

2. **实现核心转换引擎** (优先级：高)
   - 创建UniversalConverter类
   - 实现插件注册机制

3. **开发基础UI组件** (优先级：中)
   - 实现三栏布局结构
   - 创建基础的交互组件

### 开发顺序建议

1. **Week 1**: 核心架构和类型定义
2. **Week 2**: 基础UI框架和布局
3. **Week 3**: 导入功能开发
4. **Week 4**: 编辑功能开发
5. **Week 5**: 导出功能开发
6. **Week 6**: 测试和优化

---

## 🎯 成功标准

### 安装成功标准
- [ ] 所有菜单成功创建并显示
- [ ] 路由配置正确，页面可以访问
- [ ] 基础文件结构创建完成
- [ ] 开发环境正常启动

### 开发完成标准
- [ ] 所有模块状态更新为"✅ 已完成"
- [ ] 单元测试覆盖率 > 90%
- [ ] 所有页面功能正常工作
- [ ] 代码生成功能验证通过

---

**📋 文档状态：** ✅ 已发布  
**🔄 维护责任：** UX开发团队  
**📅 下次更新：** 随开发进展持续更新

**🔗 相关文档：**
- [UX Designer UI转换器功能设计文档](ux-designer-ui-converter-design.md)
- [UI转换器菜单SQL脚本](../../sql/mysql/ui_converter_menu.sql)
- [核心开发指南](../development/core-development-guide.md) 