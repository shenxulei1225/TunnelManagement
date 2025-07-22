# 用户自定义页面设计器

## 📋 概述

用户自定义页面设计器是一个零代码页面开发平台，允许用户通过可视化界面设计页面，系统自动生成代码、打包并部署，实现真正的零代码开发。

## 🎯 核心功能

### 1. 可视化页面设计器
- **拖拽式设计**: 用户可以直接拖拽组件到设计区域
- **实时预览**: 设计过程中可以实时预览效果
- **属性配置**: 右侧面板可以配置组件的各种属性
- **组件库**: 提供丰富的组件库（基础组件、表单组件、数据组件、布局组件）

### 2. 自动代码生成
- **Vue组件代码**: 自动生成完整的Vue组件代码
- **路由配置**: 自动生成路由配置
- **菜单配置**: 自动生成菜单配置
- **样式代码**: 自动生成CSS样式

### 3. 自动打包上传
- **一键生成**: 点击"生成并上传"按钮
- **自动构建**: 自动执行前端构建
- **自动部署**: 可选择自动部署到服务器
- **构建状态**: 实时显示构建进度和状态

## 🏗️ 技术架构

### 整体架构图
```
┌─────────────────────────────────────────────────────────────┐
│                    用户界面层                               │
├─────────────────────────────────────────────────────────────┤
│  PageDesigner.vue    PageGenerator.vue    FrontendBuilder  │
├─────────────────────────────────────────────────────────────┤
│                    前端API层                               │
├─────────────────────────────────────────────────────────────┤
│  page-designer.ts    page-generator.ts    dynamic-router.ts│
├─────────────────────────────────────────────────────────────┤
│                    后端服务层                               │
├─────────────────────────────────────────────────────────────┤
│  PageDesignerController    PageDesignerService             │
├─────────────────────────────────────────────────────────────┤
│                    数据存储层                               │
├─────────────────────────────────────────────────────────────┤
│  页面设计数据    构建历史数据    文件系统存储               │
├─────────────────────────────────────────────────────────────┤
│                    构建系统                                 │
├─────────────────────────────────────────────────────────────┤
│  build-with-generated.js    npm scripts    vite build      │
└─────────────────────────────────────────────────────────────┘
```

## 🎨 前端设计器实现

### 1. 组件库系统

```typescript
// 组件库定义
const componentLibrary = {
  // 基础组件
  basic: [
    {
      type: 'el-input',
      name: '输入框',
      icon: 'Edit',
      defaultProps: {
        label: '输入框',
        placeholder: '请输入内容',
        required: false,
        rules: ''
      }
    },
    {
      type: 'el-button',
      name: '按钮',
      icon: 'Select',
      defaultProps: {
        label: '按钮',
        type: 'primary',
        text: '点击'
      }
    },
    {
      type: 'el-card',
      name: '卡片',
      icon: 'Document',
      defaultProps: {
        label: '卡片',
        header: '卡片标题',
        content: '卡片内容'
      }
    }
  ],
  
  // 表单组件
  form: [
    {
      type: 'el-form',
      name: '表单',
      icon: 'Document',
      defaultProps: {
        label: '表单',
        labelWidth: '120px',
        inline: false
      }
    },
    {
      type: 'el-select',
      name: '选择器',
      icon: 'Select',
      defaultProps: {
        label: '选择器',
        placeholder: '请选择',
        options: []
      }
    },
    {
      type: 'el-switch',
      name: '开关',
      icon: 'Switch',
      defaultProps: {
        label: '开关',
        activeText: '开启',
        inactiveText: '关闭'
      }
    }
  ],
  
  // 数据组件
  data: [
    {
      type: 'el-table',
      name: '表格',
      icon: 'Grid',
      defaultProps: {
        label: '表格',
        columns: [],
        data: []
      }
    },
    {
      type: 'el-pagination',
      name: '分页',
      icon: 'More',
      defaultProps: {
        label: '分页',
        total: 0,
        pageSize: 10,
        currentPage: 1
      }
    },
    {
      type: 'el-tree',
      name: '树形',
      icon: 'Folder',
      defaultProps: {
        label: '树形',
        data: [],
        props: {
          children: 'children',
          label: 'label'
        }
      }
    }
  ],
  
  // 布局组件
  layout: [
    {
      type: 'el-row',
      name: '行布局',
      icon: 'Grid',
      defaultProps: {
        gutter: 20,
        justify: 'start',
        align: 'top'
      }
    },
    {
      type: 'el-col',
      name: '列布局',
      icon: 'Grid',
      defaultProps: {
        span: 12,
        offset: 0,
        push: 0,
        pull: 0
      }
    },
    {
      type: 'el-container',
      name: '容器',
      icon: 'Box',
      defaultProps: {
        direction: 'horizontal'
      }
    }
  ]
}
```

### 2. 拖拽系统实现

```typescript
// 拖拽开始
const handleDragStart = (event: DragEvent, component: ComponentDefinition) => {
  if (event.dataTransfer) {
    event.dataTransfer.setData('component', JSON.stringify(component))
    event.dataTransfer.effectAllowed = 'copy'
  }
}

// 拖拽结束
const handleDrop = (event: DragEvent) => {
  event.preventDefault()
  
  if (event.dataTransfer) {
    const componentData = JSON.parse(event.dataTransfer.getData('component'))
    
    const newComponent: Component = {
      id: `component_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
      type: componentData.type,
      name: componentData.name,
      props: { ...componentData.defaultProps }
    }
    
    pageComponents.value.push(newComponent)
    selectComponent(newComponent.id)
  }
}

// 拖拽悬停
const handleDragOver = (event: DragEvent) => {
  event.preventDefault()
  event.dataTransfer!.dropEffect = 'copy'
}

// 组件选择
const selectComponent = (componentId: string) => {
  selectedComponent.value = componentId
  const component = pageComponents.value.find(c => c.id === componentId)
  if (component) {
    selectedComponentProps.value = { ...component.props }
  }
}

// 更新组件属性
const updateComponentProp = (componentId: string, prop: string, value: any) => {
  const component = pageComponents.value.find(c => c.id === componentId)
  if (component) {
    component.props[prop] = value
  }
}
```

### 3. 属性配置系统

```typescript
// 属性面板组件
const PropertyPanel = defineComponent({
  props: {
    component: {
      type: Object as PropType<Component>,
      required: true
    }
  },
  
  setup(props) {
    const formData = reactive({ ...props.component.props })
    
    // 监听属性变化
    watch(formData, (newProps) => {
      Object.assign(props.component.props, newProps)
    }, { deep: true })
    
    return () => (
      <el-form model={formData} label-width="80px">
        <el-form-item label="组件ID">
          <el-input value={props.component.id} disabled />
        </el-form-item>
        
        <el-form-item label="组件类型">
          <el-input value={props.component.type} disabled />
        </el-form-item>
        
        <el-form-item label="标签文本">
          <el-input v-model={formData.label} />
        </el-form-item>
        
        <el-form-item label="占位符">
          <el-input v-model={formData.placeholder} />
        </el-form-item>
        
        <el-form-item label="是否必填">
          <el-switch v-model={formData.required} />
        </el-form-item>
        
        <el-form-item label="验证规则">
          <el-input v-model={formData.rules} type="textarea" />
        </el-form-item>
        
        <el-form-item label="样式">
          <el-input v-model={formData.style} type="textarea" />
        </el-form-item>
      </el-form>
    )
  }
})
```

### 4. 代码生成系统

```typescript
// 代码生成器
class CodeGenerator {
  // 生成页面代码
  generatePageCode(pageDesign: PageDesign): string {
    const { pageName, pagePath, components } = pageDesign
    
    return `<template>
  <div class="${pagePath}-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>${pageName}</span>
        </div>
      </template>
      
      <el-form :model="formData" label-width="120px">
        ${components.map(component => this.generateComponentCode(component)).join('\n        ')}
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const formData = reactive({
  ${components.map(component => `${component.props.label}: ''`).join(',\n  ')}
})

const handleSubmit = () => {
  ElMessage.success('提交成功')
}
</script>

<style scoped>
.${pagePath}-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>`
  }
  
  // 生成组件代码
  generateComponentCode(component: Component): string {
    const { type, props } = component
    
    switch (type) {
      case 'el-input':
        return `<el-form-item label="${props.label}">
  <el-input v-model="formData.${props.label}" placeholder="${props.placeholder}" />
</el-form-item>`
      
      case 'el-button':
        return `<el-form-item>
  <el-button type="${props.type}" @click="handleSubmit">${props.text}</el-button>
</el-form-item>`
      
      case 'el-card':
        return `<el-card>
  <template #header>${props.header}</template>
  <div>${props.content}</div>
</el-card>`
      
      case 'el-select':
        return `<el-form-item label="${props.label}">
  <el-select v-model="formData.${props.label}" placeholder="${props.placeholder}">
    ${props.options.map(option => 
      `<el-option label="${option.label}" value="${option.value}" />`
    ).join('\n    ')}
  </el-select>
</el-form-item>`
      
      case 'el-table':
        return `<el-table :data="tableData" style="width: 100%">
  ${props.columns.map(column => 
    `<el-table-column prop="${column.prop}" label="${column.label}" />`
  ).join('\n  ')}
</el-table>`
      
      default:
        return `<el-form-item label="${props.label}">
  <${type} v-model="formData.${props.label}" />
</el-form-item>`
    }
  }
  
  // 生成路由配置
  generateRouteConfig(pageDesign: PageDesign): string {
    const { pagePath, menuTitle, menuIcon } = pageDesign
    
    return `{
  path: '/${pagePath}',
  name: '${menuTitle.replace(/\s+/g, '')}',
  component: () => import('@/views/generated/${pagePath}/index.vue'),
  meta: {
    title: '${menuTitle}',
    icon: '${menuIcon}',
    noCache: true
  }
}`
  }
  
  // 生成菜单配置
  generateMenuConfig(pageDesign: PageDesign): string {
    const { menuTitle, menuIcon, pagePath } = pageDesign
    
    return `{
  name: '${menuTitle.replace(/\s+/g, '')}',
  path: '/${pagePath}',
  component: '${menuTitle.replace(/\s+/g, '')}',
  meta: {
    title: '${menuTitle}',
    icon: '${menuIcon}',
    noCache: true
  }
}`
  }
}
```

## 🔧 后端服务实现

### 1. 数据模型

```java
// 页面设计实体
@Data
@TableName("page_design")
public class PageDesignDO {
    
    @TableId
    private String id;
    
    /**
     * 页面名称
     */
    private String pageName;
    
    /**
     * 页面路径
     */
    private String pagePath;
    
    /**
     * 菜单标题
     */
    private String menuTitle;
    
    /**
     * 菜单图标
     */
    private String menuIcon;
    
    /**
     * 组件列表 (JSON)
     */
    private String components;
    
    /**
     * 页面配置 (JSON)
     */
    private String config;
    
    /**
     * 生成的页面代码
     */
    private String pageCode;
    
    /**
     * 路由配置
     */
    private String routeConfig;
    
    /**
     * 菜单配置
     */
    private String menuConfig;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 构建状态
     */
    private String buildStatus;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

// 构建历史实体
@Data
@TableName("build_history")
public class BuildHistoryDO {
    
    @TableId
    private String id;
    
    /**
     * 页面设计ID
     */
    private String pageDesignId;
    
    /**
     * 构建状态
     */
    private String status;
    
    /**
     * 构建进度
     */
    private Integer progress;
    
    /**
     * 构建消息
     */
    private String message;
    
    /**
     * 构建日志
     */
    private String logs;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
```

### 2. 服务实现

```java
@Service
@Validated
@Slf4j
public class PageDesignerServiceImpl implements PageDesignerService {
    
    @Resource
    private PageDesignMapper pageDesignMapper;
    
    @Resource
    private BuildHistoryMapper buildHistoryMapper;
    
    @Resource
    private FileService fileService;
    
    @Override
    public PageDesignRespVO createPageDesign(PageDesignCreateReqVO createReqVO) {
        // 创建页面设计
        PageDesignDO pageDesign = BeanUtils.toBean(createReqVO, PageDesignDO.class);
        pageDesign.setId(UUID.randomUUID().toString());
        pageDesign.setStatus("draft");
        pageDesign.setCreateTime(LocalDateTime.now());
        pageDesign.setUpdateTime(LocalDateTime.now());
        
        // 序列化组件和配置
        pageDesign.setComponents(JsonUtils.toJsonString(createReqVO.getComponents()));
        pageDesign.setConfig(JsonUtils.toJsonString(createReqVO.getConfig()));
        
        pageDesignMapper.insert(pageDesign);
        
        return BeanUtils.toBean(pageDesign, PageDesignRespVO.class);
    }
    
    @Override
    public BuildResultRespVO uploadAndBuild(UploadPageReqVO reqVO) {
        try {
            // 1. 保存页面设计
            PageDesignDO pageDesign = savePageDesign(reqVO);
            
            // 2. 生成页面代码
            String pageCode = generatePageCode(pageDesign);
            pageDesign.setPageCode(pageCode);
            
            // 3. 保存页面文件
            savePageFile(pageDesign.getPagePath(), pageCode);
            
            // 4. 生成路由和菜单配置
            String routeConfig = generateRouteConfig(pageDesign);
            String menuConfig = generateMenuConfig(pageDesign);
            pageDesign.setRouteConfig(routeConfig);
            pageDesign.setMenuConfig(menuConfig);
            
            // 5. 更新页面设计
            pageDesign.setUpdateTime(LocalDateTime.now());
            pageDesignMapper.updateById(pageDesign);
            
            // 6. 异步执行构建
            String buildId = UUID.randomUUID().toString();
            CompletableFuture.runAsync(() -> {
                try {
                    executeBuild(buildId, pageDesign);
                } catch (Exception e) {
                    log.error("构建失败", e);
                    updateBuildStatus(buildId, "failed", e.getMessage());
                }
            });
            
            return new BuildResultRespVO(buildId, "构建已开始");
            
        } catch (Exception e) {
            log.error("上传并构建失败", e);
            throw new RuntimeException("上传并构建失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成页面代码
     */
    private String generatePageCode(PageDesignDO pageDesign) {
        CodeGenerator generator = new CodeGenerator();
        return generator.generatePageCode(pageDesign);
    }
    
    /**
     * 保存页面文件
     */
    private void savePageFile(String pagePath, String pageCode) throws IOException {
        // 创建目录
        Path dir = Paths.get("tunnel-management-ui/src/views/generated", pagePath);
        Files.createDirectories(dir);
        
        // 保存文件
        Path file = dir.resolve("index.vue");
        Files.write(file, pageCode.getBytes(StandardCharsets.UTF_8));
        
        log.info("页面文件已保存: {}", file);
    }
    
    /**
     * 执行构建
     */
    private void executeBuild(String buildId, PageDesignDO pageDesign) {
        try {
            log.info("开始构建页面: {}", pageDesign.getPagePath());
            
            // 1. 创建构建历史
            BuildHistoryDO buildHistory = new BuildHistoryDO();
            buildHistory.setId(buildId);
            buildHistory.setPageDesignId(pageDesign.getId());
            buildHistory.setStatus("building");
            buildHistory.setProgress(0);
            buildHistory.setStartTime(LocalDateTime.now());
            buildHistoryMapper.insert(buildHistory);
            
            // 2. 更新构建状态
            updateBuildStatus(buildId, "building", "构建中...");
            
            // 3. 执行前端构建
            ProcessBuilder pb = new ProcessBuilder(
                "node", "scripts/build-with-generated.js"
            );
            pb.directory(new File("tunnel-management-ui"));
            pb.environment().put("NODE_ENV", "production");
            
            Process process = pb.start();
            
            // 4. 监控构建进度
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("构建日志: {}", line);
                updateBuildLog(buildId, line);
            }
            
            // 5. 等待构建完成
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // 构建成功
                updateBuildStatus(buildId, "completed", "构建完成");
                
                // 更新页面设计状态
                pageDesign.setBuildStatus("completed");
                pageDesign.setUpdateTime(LocalDateTime.now());
                pageDesignMapper.updateById(pageDesign);
                
                log.info("页面构建成功: {}", pageDesign.getPagePath());
            } else {
                // 构建失败
                updateBuildStatus(buildId, "failed", "构建失败");
                log.error("页面构建失败: {}", pageDesign.getPagePath());
            }
            
        } catch (Exception e) {
            log.error("构建执行失败", e);
            updateBuildStatus(buildId, "failed", e.getMessage());
        }
    }
    
    /**
     * 更新构建状态
     */
    private void updateBuildStatus(String buildId, String status, String message) {
        BuildHistoryDO buildHistory = buildHistoryMapper.selectById(buildId);
        if (buildHistory != null) {
            buildHistory.setStatus(status);
            buildHistory.setMessage(message);
            buildHistory.setUpdateTime(LocalDateTime.now());
            
            if ("completed".equals(status) || "failed".equals(status)) {
                buildHistory.setEndTime(LocalDateTime.now());
            }
            
            buildHistoryMapper.updateById(buildHistory);
        }
    }
    
    /**
     * 更新构建日志
     */
    private void updateBuildLog(String buildId, String log) {
        BuildHistoryDO buildHistory = buildHistoryMapper.selectById(buildId);
        if (buildHistory != null) {
            String logs = buildHistory.getLogs();
            if (logs == null) {
                logs = "";
            }
            logs += log + "\n";
            buildHistory.setLogs(logs);
            buildHistoryMapper.updateById(buildHistory);
        }
    }
}
```

## 🏗️ 构建系统实现

### 1. 构建脚本

```javascript
#!/usr/bin/env node

/**
 * 包含生成页面的构建脚本
 * 自动收集用户设计的页面并打包到主应用中
 */

const fs = require('fs')
const path = require('path')
const { execSync } = require('child_process')

// 配置
const config = {
  generatedPagesDir: 'src/views/generated',
  routerConfigFile: 'src/router/modules/generated.ts',
  menuConfigFile: 'src/config/generated-menus.ts',
  buildOutputDir: 'dist',
  buildScript: 'npm run build'
}

/**
 * 收集生成的页面
 */
function collectGeneratedPages() {
  console.log('🔍 收集生成的页面...')
  
  const pages = []
  const generatedDir = path.resolve(config.generatedPagesDir)
  
  if (!fs.existsSync(generatedDir)) {
    console.log('📁 生成的页面目录不存在，跳过')
    return pages
  }
  
  const pageDirs = fs.readdirSync(generatedDir, { withFileTypes: true })
    .filter(dirent => dirent.isDirectory())
    .map(dirent => dirent.name)
  
  for (const pageDir of pageDirs) {
    const pagePath = path.join(generatedDir, pageDir)
    const indexPath = path.join(pagePath, 'index.vue')
    
    if (fs.existsSync(indexPath)) {
      pages.push({
        name: pageDir,
        path: pageDir,
        component: `@/views/generated/${pageDir}/index.vue`
      })
      console.log(`✅ 发现页面: ${pageDir}`)
    }
  }
  
  console.log(`📊 共收集到 ${pages.length} 个页面`)
  return pages
}

/**
 * 生成路由配置
 */
function generateRouterConfig(pages) {
  console.log('🛣️ 生成路由配置...')
  
  let routerCode = `// 自动生成的页面路由配置
// 此文件由构建脚本自动生成，请勿手动修改

import { RouteRecordRaw } from 'vue-router'

export const generatedRoutes: RouteRecordRaw[] = [
`
  
  for (const page of pages) {
    routerCode += `  {
    path: '/${page.path}',
    name: '${page.name.replace(/-/g, '')}',
    component: () => import('${page.component}'),
    meta: {
      title: '${page.name.replace(/-/g, ' ')}',
      icon: 'ep:document',
      noCache: true
    }
  },
`
  }
  
  routerCode += `]

export default generatedRoutes
`
  
  // 写入路由配置文件
  const routerConfigPath = path.resolve(config.routerConfigFile)
  const routerConfigDir = path.dirname(routerConfigPath)
  
  if (!fs.existsSync(routerConfigDir)) {
    fs.mkdirSync(routerConfigDir, { recursive: true })
  }
  
  fs.writeFileSync(routerConfigPath, routerCode)
  console.log(`✅ 路由配置已生成: ${config.routerConfigFile}`)
}

/**
 * 生成菜单配置
 */
function generateMenuConfig(pages) {
  console.log('📋 生成菜单配置...')
  
  let menuCode = `// 自动生成的菜单配置
// 此文件由构建脚本自动生成，请勿手动修改

export const generatedMenus = [
`
  
  for (const page of pages) {
    menuCode += `  {
    name: '${page.name.replace(/-/g, '')}',
    path: '/${page.path}',
    component: '${page.name.replace(/-/g, '')}',
    meta: {
      title: '${page.name.replace(/-/g, ' ')}',
      icon: 'ep:document',
      noCache: true
    }
  },
`
  }
  
  menuCode += `]

export default generatedMenus
`
  
  // 写入菜单配置文件
  const menuConfigPath = path.resolve(config.menuConfigFile)
  const menuConfigDir = path.dirname(menuConfigPath)
  
  if (!fs.existsSync(menuConfigDir)) {
    fs.mkdirSync(menuConfigDir, { recursive: true })
  }
  
  fs.writeFileSync(menuConfigPath, menuCode)
  console.log(`✅ 菜单配置已生成: ${config.menuConfigFile}`)
}

/**
 * 更新主路由文件
 */
function updateMainRouter() {
  console.log('🔗 更新主路由文件...')
  
  const mainRouterFile = 'src/router/index.ts'
  const mainRouterPath = path.resolve(mainRouterFile)
  
  if (!fs.existsSync(mainRouterPath)) {
    console.log('⚠️ 主路由文件不存在，跳过')
    return
  }
  
  let routerContent = fs.readFileSync(mainRouterPath, 'utf8')
  
  // 检查是否已经导入了生成的路由
  if (!routerContent.includes('generatedRoutes')) {
    // 添加导入语句
    const importStatement = `import { generatedRoutes } from './modules/generated'
`
    
    // 找到合适的位置插入导入
    const importIndex = routerContent.lastIndexOf('import')
    const nextLineIndex = routerContent.indexOf('\n', importIndex) + 1
    
    routerContent = routerContent.slice(0, nextLineIndex) + importStatement + routerContent.slice(nextLineIndex)
    
    // 添加生成的路由到路由数组
    const routesIndex = routerContent.indexOf('const routes')
    if (routesIndex !== -1) {
      const routesEndIndex = routerContent.indexOf(']', routesIndex)
      if (routesEndIndex !== -1) {
        routerContent = routerContent.slice(0, routesEndIndex) + ',\n  ...generatedRoutes' + routerContent.slice(routesEndIndex)
      }
    }
    
    fs.writeFileSync(mainRouterPath, routerContent)
    console.log(`✅ 主路由文件已更新: ${mainRouterFile}`)
  } else {
    console.log('ℹ️ 主路由文件已包含生成的路由，跳过')
  }
}

/**
 * 执行构建
 */
function executeBuild() {
  console.log('🏗️ 开始构建...')
  
  try {
    execSync(config.buildScript, { 
      stdio: 'inherit',
      cwd: process.cwd()
    })
    console.log('✅ 构建完成')
  } catch (error) {
    console.error('❌ 构建失败:', error.message)
    process.exit(1)
  }
}

/**
 * 主函数
 */
function main() {
  console.log('🚀 开始构建包含生成页面的应用...')
  
  try {
    // 1. 收集生成的页面
    const pages = collectGeneratedPages()
    
    if (pages.length === 0) {
      console.log('ℹ️ 没有发现生成的页面，执行标准构建')
      executeBuild()
      return
    }
    
    // 2. 生成路由配置
    generateRouterConfig(pages)
    
    // 3. 生成菜单配置
    generateMenuConfig(pages)
    
    // 4. 更新主路由文件
    updateMainRouter()
    
    // 5. 执行构建
    executeBuild()
    
    console.log('🎉 构建完成！生成的页面已包含在应用中')
    
  } catch (error) {
    console.error('❌ 构建过程出错:', error.message)
    process.exit(1)
  }
}

// 执行主函数
if (require.main === module) {
  main()
}

module.exports = {
  collectGeneratedPages,
  generateRouterConfig,
  generateMenuConfig,
  updateMainRouter,
  executeBuild
}
```

## 🚀 使用流程

### 1. 设计页面
```
1. 访问页面设计器: http://localhost:3000/examples/page-designer
2. 从左侧组件库拖拽组件到设计区域
3. 在右侧属性面板配置组件属性
4. 实时预览页面效果
```

### 2. 生成页面
```
1. 点击"生成并上传"按钮
2. 填写页面配置（页面名称、路径、菜单等）
3. 系统自动生成代码并上传
4. 自动执行构建流程
```

### 3. 部署页面
```
1. 构建完成后可选择自动部署
2. 页面自动添加到菜单中
3. 可以立即访问新页面
```

## 🎯 优势特点

### ✅ 真正的可视化设计
- 拖拽式设计，无需编写代码
- 实时预览，所见即所得
- 丰富的组件库，满足各种需求

### ✅ 自动代码生成
- 生成完整的Vue组件代码
- 自动生成路由和菜单配置
- 支持自定义样式和逻辑

### ✅ 自动打包上传
- 一键生成并上传
- 自动执行构建流程
- 支持增量构建和全量构建

### ✅ 完整的生命周期管理
- 设计 → 生成 → 构建 → 部署
- 支持版本管理和模板管理
- 支持页面激活/停用

## 🔧 技术架构

### 1. 前端设计器
```
PageDesigner.vue          # 主设计器组件
├── ComponentPanel        # 组件面板
├── DesignCanvas         # 设计画布
├── PropertyPanel        # 属性面板
└── PreviewDialog        # 预览对话框
```

### 2. 后端服务
```
PageDesignerService       # 页面设计器服务
├── 保存页面设计
├── 生成页面代码
├── 执行构建流程
└── 部署页面
```

### 3. 构建系统
```
build-with-generated.js   # 构建脚本
├── 收集生成的页面
├── 生成路由配置
├── 生成菜单配置
└── 执行构建
```

## 访问地址

**页面设计器:**
```
http://localhost:3000/examples/page-designer
```

**页面生成器:**
```
http://localhost:3000/examples/page-generator
```

**前端构建管理:**
```
http://localhost:3000/examples/frontend-builder
```

## 📊 构建脚本使用

```bash
# 手动执行构建
npm run build:with-generated

# 或者直接运行脚本
node scripts/build-with-generated.js
```

这样用户就可以通过可视化界面设计自己的页面，系统会自动生成代码、打包并上传，实现真正的0代码页面开发！ 