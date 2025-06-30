# Figma样式系统集成指南

> **🎨 从Figma设计中提取样式，生成统一风格的控件库**

---

## 🎯 功能概述

Figma样式系统集成功能允许您从Figma设计文件中自动提取颜色、字体、间距、阴影等样式信息，并将这些样式应用到整个控件系统中，确保设计与开发的完美一致性。

### 核心优势

- **🔄 自动化流程**: 一键从Figma提取样式并应用到控件
- **🎨 设计一致性**: 确保所有控件使用统一的设计语言
- **⚡ 快速应用**: 秒级完成样式提取和主题生成
- **📦 完整输出**: 生成CSS变量、UE主题、Element主题等
- **🔧 菜单集成**: 自动创建菜单管理中的样式系统入口

## 🚀 使用流程

### 第一步：准备Figma资源

1. **获取Figma访问令牌**
   ```
   1. 访问 https://www.figma.com/settings
   2. 滚动到 "Personal access tokens" 部分
   3. 点击 "Create new token"
   4. 输入描述并复制生成的令牌
   ```

2. **准备设计文件**
   - 确保设计文件包含完整的组件和样式
   - 使用一致的命名规范
   - 组织好颜色和字体样式

### 第二步：提取样式

1. **打开样式提取器**
   ```
   路径：系统管理 → 样式管理 → Figma样式提取器
   ```

2. **填写提取信息**
   ```javascript
   {
     figmaUrl: "https://www.figma.com/file/[FILE_ID]/...",
     accessToken: "你的Figma访问令牌",
     themeName: "自定义主题名称（可选）"
   }
   ```

3. **开始提取**
   - 点击"开始提取样式"按钮
   - 系统将自动分析设计文件
   - 提取颜色、字体、间距等样式信息

### 第三步：预览和确认

系统会展示提取结果：

- **📊 提取统计**: 显示提取到的样式数量
- **🎨 颜色预览**: 主要颜色的可视化展示
- **✏️ 字体预览**: 字体样式的实际效果
- **🧩 控件预览**: 应用样式后的控件效果

### 第四步：应用主题

配置应用选项：

```javascript
{
  applyTo: ['css', 'element', 'widgets', 'global'],
  themeName: "Figma主题",
  cssPrefix: "--theme",
  createMenu: true  // 是否创建菜单项
}
```

## 📋 菜单管理集成

### 自动创建的菜单参数

当启用"创建菜单"选项时，系统会自动生成以下菜单配置：

```javascript
{
  name: "Figma主题样式系统",
  type: 1,                    // 目录类型
  sort: Date.now(),
  parentId: 0,               // 根级菜单
  path: "/styles/figma-theme",
  icon: "ep:brush",          // 画刷图标
  component: "",
  componentName: "",
  status: 0,                 // 正常状态
  visible: true,
  keepAlive: true,
  alwaysShow: false,
  permission: "styles:figma-theme:view",
  description: "基于Figma设计的统一样式系统"
}
```

### 手动创建菜单步骤

如果需要手动创建菜单，按以下参数设置：

1. **基础信息**
   - 菜单名称: `{主题名称}样式系统`
   - 菜单类型: `目录`
   - 排序: `当前时间戳`
   - 上级菜单: `根目录`

2. **路由配置**
   - 路由地址: `/styles/{theme-name}`
   - 路由名称: `StyleSystem{ThemeName}`
   - 组件路径: `留空（目录类型）`

3. **权限设置**
   - 权限标识: `styles:{theme-name}:view`
   - 状态: `正常`
   - 显示: `是`
   - 缓存: `是`

4. **显示配置**
   - 图标: `ep:brush` 或 `ep:color-picker`
   - 始终显示: `否`
   - 可见性: `显示`

## 🔧 样式输出格式

### CSS变量输出

```css
:root {
  /* 颜色变量 */
  --theme-primary: #409EFF;
  --theme-secondary: #6C757D;
  --theme-background: #FFFFFF;
  
  /* 字体变量 */
  --theme-heading-family: 'Inter';
  --theme-heading-size: 24px;
  --theme-heading-weight: 600;
  
  /* 间距变量 */
  --theme-spacing-xs: 4px;
  --theme-spacing-sm: 8px;
  --theme-spacing-md: 16px;
  
  /* 阴影变量 */
  --theme-shadow-sm: 0 1px 3px rgba(0,0,0,0.12);
  --theme-shadow-md: 0 3px 6px rgba(0,0,0,0.16);
  
  /* 边框变量 */
  --theme-radius-sm: 4px;
  --theme-radius-md: 8px;
}
```

### UE主题配置

```typescript
interface UETheme {
  name: "figma-extracted-theme";
  colors: {
    primary: { r: 0.25, g: 0.62, b: 0.98, a: 1 };
    secondary: { r: 0.42, g: 0.45, b: 0.50, a: 1 };
    // ... 更多颜色
  };
  fonts: {
    default: {
      fontFamilyName: "Inter";
      size: 14;
      fontWeight: 400;
    };
    // ... 更多字体
  };
  spacing: {
    xs: 4, sm: 8, md: 16, lg: 24, xl: 32
  };
}
```

## 🎨 控件样式应用

### 按钮样式

```vue
<template>
  <button class="theme-button">
    使用主题样式的按钮
  </button>
</template>

<style scoped>
.theme-button {
  background-color: var(--theme-primary);
  color: white;
  border: none;
  padding: var(--theme-spacing-sm) var(--theme-spacing-md);
  border-radius: var(--theme-radius-md);
  font-family: var(--theme-button-family);
  font-size: var(--theme-button-size);
  font-weight: var(--theme-button-weight);
  cursor: pointer;
  transition: all 0.2s ease;
}

.theme-button:hover {
  opacity: 0.8;
  transform: translateY(-1px);
  box-shadow: var(--theme-shadow-md);
}
</style>
```

### 输入框样式

```vue
<template>
  <input class="theme-input" placeholder="主题样式输入框">
</template>

<style scoped>
.theme-input {
  border: 1px solid var(--theme-border, #e4e7ed);
  padding: var(--theme-spacing-sm) var(--theme-spacing-md);
  border-radius: var(--theme-radius-sm);
  font-family: var(--theme-body-family);
  font-size: var(--theme-body-size);
  background: var(--theme-background);
  color: var(--theme-text-primary);
}

.theme-input:focus {
  outline: none;
  border-color: var(--theme-primary);
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
</style>
```

## 📦 API接口集成

### 样式提取API

```typescript
import StyleSystemIntegrator from '@/core/style-extractor/StyleSystemIntegrator'

// 从URL创建样式系统
const integrator = new StyleSystemIntegrator({
  extraction: {
    extractColors: true,
    extractTypography: true,
    extractSpacing: true,
    minUsageCount: 2
  },
  application: {
    themeName: 'my-figma-theme',
    generateCssVariables: true,
    generateUeTheme: true,
    autoApplyToDom: true
  }
})

try {
  const result = await integrator.createStyleSystemFromUrl(
    'https://www.figma.com/file/ABC123/My-Design',
    'figma-access-token',
    {
      // 组件样式覆盖
      button: {
        baseStyles: {
          'border-radius': '8px',
          'padding': '12px 24px'
        },
        stateStyles: {
          hover: { 'transform': 'translateY(-2px)' },
          active: { 'transform': 'translateY(0px)' }
        }
      }
    }
  )
  
  console.log('样式系统创建成功:', result)
} catch (error) {
  console.error('创建失败:', error)
}
```

### 菜单管理API调用

```typescript
import { MenuApi } from '@/api/system/menu'

// 自动创建菜单项
const menuParams = {
  name: "Figma主题样式系统",
  type: 1,
  sort: Date.now(),
  parentId: 0,
  path: "/styles/figma-theme",
  icon: "ep:brush",
  component: "",
  componentName: "",
  status: 0,
  visible: true,
  keepAlive: true,
  alwaysShow: false,
  permission: "styles:figma-theme:view",
  description: "基于Figma设计的统一样式系统"
}

try {
  const menuResult = await MenuApi.createMenu(menuParams)
  console.log('菜单创建成功:', menuResult)
} catch (error) {
  console.error('菜单创建失败:', error)
}
```

## 🔄 工作流集成

### 设计师工作流

1. **在Figma中完成设计**
   - 使用统一的设计系统
   - 定义完整的颜色和字体规范
   - 标准化组件样式

2. **导出设计令牌**
   - 使用样式提取器从Figma获取样式
   - 预览提取结果
   - 确认样式覆盖范围

3. **发布到开发环境**
   - 应用主题到控件系统
   - 创建样式系统菜单
   - 通知开发团队使用新样式

### 开发者工作流

1. **获取样式系统**
   - 从菜单管理访问样式系统
   - 下载CSS变量文件
   - 查看控件样式指南

2. **应用到页面开发**
   - 使用CSS变量定义样式
   - 引用主题化的控件组件
   - 确保样式一致性

3. **样式维护更新**
   - 当设计更新时重新提取样式
   - 更新现有页面样式引用
   - 测试样式兼容性

## 🛠️ 高级配置

### 自定义样式提取规则

```typescript
const customOptions = {
  extraction: {
    // 颜色提取选项
    extractColors: true,
    colorSimilarityThreshold: 15,  // 颜色相似度阈值
    
    // 字体提取选项
    extractTypography: true,
    
    // 间距提取选项
    extractSpacing: true,
    spacingSimilarityThreshold: 4,  // 间距相似度阈值
    
    // 使用频次过滤
    minUsageCount: 3,  // 最小使用次数
    
    // 阴影和边框
    extractShadows: true,
    extractBorders: true
  },
  
  application: {
    // 主题名称
    themeName: 'custom-figma-theme',
    
    // 生成选项
    generateCssVariables: true,
    generateUeTheme: true,
    generateElementTheme: true,
    
    // CSS前缀
    cssPrefix: '--custom-theme',
    
    // 自动应用
    autoApplyToDom: false
  }
}
```

### 组件样式覆盖

```typescript
const componentOverrides = {
  // 按钮组件覆盖
  button: {
    baseStyles: {
      'font-weight': 'bold',
      'letter-spacing': '0.5px',
      'text-transform': 'uppercase'
    },
    stateStyles: {
      normal: { 'box-shadow': 'none' },
      hover: { 
        'box-shadow': '0 4px 8px rgba(0,0,0,0.1)',
        'transform': 'translateY(-1px)'
      },
      active: { 'transform': 'translateY(0)' },
      disabled: { 'opacity': '0.5' }
    }
  },
  
  // 输入框组件覆盖
  input: {
    baseStyles: {
      'border-width': '2px',
      'transition': 'all 0.3s ease'
    },
    stateStyles: {
      focus: {
        'border-color': 'var(--theme-primary)',
        'box-shadow': '0 0 0 3px rgba(64, 158, 255, 0.1)'
      }
    }
  }
}
```

## 📊 性能优化

### 样式提取优化

- **并行处理**: 同时分析多个设计元素
- **缓存机制**: 缓存已分析的节点信息
- **增量更新**: 只处理变更的设计部分
- **智能过滤**: 自动过滤无用或重复的样式

### 主题应用优化

- **按需加载**: 只加载使用的样式变量
- **CSS优化**: 压缩生成的CSS代码
- **浏览器兼容**: 提供降级方案
- **性能监控**: 跟踪样式应用性能

## 🔍 故障排除

### 常见问题

1. **提取失败**
   - 检查Figma访问令牌是否有效
   - 确认文件ID是否正确
   - 验证网络连接状态

2. **样式不一致**
   - 检查CSS变量是否正确应用
   - 确认组件使用了主题样式
   - 验证样式优先级

3. **菜单创建失败**
   - 检查菜单创建权限
   - 确认路径不冲突
   - 验证菜单参数格式

### 调试工具

```javascript
// 开启调试模式
localStorage.setItem('figma-extractor-debug', 'true')

// 查看提取过程
console.log('样式提取详情:', result.extractedStyles)

// 验证CSS变量
console.log('CSS变量:', result.appliedTheme.cssVariables)
```

---

**💡 小贴士**: 建议在正式环境使用前，先在测试环境验证样式提取和应用效果，确保与现有系统的兼容性。 