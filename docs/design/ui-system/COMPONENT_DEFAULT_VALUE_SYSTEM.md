# 组件默认值系统设计方案

## 📋 概述

参考Element UI和UMG的设计理念，实现组件自身携带默认配置的系统，大幅减少配置数据的存储量。只记录与默认值不同的修改配置，实现存储优化。

## 🎯 设计目标

- **存储优化**: 减少70-90%的配置数据存储量
- **性能提升**: 减少网络传输和加载时间
- **易于维护**: 默认值集中管理，版本化控制
- **向下兼容**: 不影响现有功能的平滑迁移

## 🏗️ 架构设计

### 1. 分层默认值体系

```typescript
// 全局默认值 (最低优先级)
interface GlobalDefaults {
  theme: 'light' | 'dark'
  size: 'small' | 'medium' | 'large'
  borderRadius: number
  spacing: number
}

// 组件类型默认值 (中优先级)
interface ComponentTypeDefaults {
  type: string // 如 'SuperTree', 'SuperList'
  defaultConfig: ComponentConfig
  version: string
  inheritance?: string[] // 继承其他类型的默认值
}

// 业务模板默认值 (较高优先级)
interface TemplateDefaults {
  templateId: string
  componentOverrides: Record<string, Partial<ComponentConfig>>
}

// 用户自定义配置 (最高优先级)
interface UserCustomConfig {
  instanceId: string
  customConfig: Partial<ComponentConfig>
  modifiedAt: Date
}
```

### 2. 配置合并策略

```typescript
export class ConfigMergeEngine {
  /**
   * 合并配置的优先级顺序：
   * 用户自定义 > 模板默认 > 组件类型默认 > 全局默认
   */
  mergeConfig(
    globalDefaults: GlobalDefaults,
    typeDefaults: ComponentTypeDefaults,
    templateDefaults?: TemplateDefaults,
    userConfig?: UserCustomConfig
  ): ComponentConfig {
    
    const baseConfig = {
      ...globalDefaults,
      ...typeDefaults.defaultConfig
    }
    
    if (templateDefaults?.componentOverrides[typeDefaults.type]) {
      Object.assign(baseConfig, templateDefaults.componentOverrides[typeDefaults.type])
    }
    
    if (userConfig?.customConfig) {
      Object.assign(baseConfig, userConfig.customConfig)
    }
    
    return baseConfig
  }
  
  /**
   * 计算差异配置 - 只保存与默认值不同的部分
   */
  calculateDiff(
    currentConfig: ComponentConfig,
    defaultConfig: ComponentConfig
  ): Partial<ComponentConfig> {
    const diff: Partial<ComponentConfig> = {}
    
    for (const [key, value] of Object.entries(currentConfig)) {
      if (JSON.stringify(value) !== JSON.stringify(defaultConfig[key])) {
        diff[key] = value
      }
    }
    
    return diff
  }
}
```

### 3. 组件默认值定义

```typescript
// SuperTree组件默认配置示例
export const SuperTreeDefaults: ComponentTypeDefaults = {
  type: 'SuperTree',
  version: '1.0.0',
  defaultConfig: {
    // 外观配置
    theme: 'modern',
    size: 'medium',
    showBorder: true,
    borderRadius: 6,
    
    // 功能配置
    showCheckbox: false,
    allowDrag: false,
    allowDrop: false,
    showIcon: true,
    
    // 工具栏配置
    showToolbar: true,
    toolbarPosition: 'top',
    showSearch: true,
    showRefresh: true,
    showExpandAll: true,
    
    // 数据配置
    lazy: false,
    defaultExpandAll: false,
    defaultExpandLevel: 1,
    filterNodeMethod: null,
    
    // 性能配置
    virtualScroll: false,
    itemHeight: 32,
    bufferSize: 10
  }
}

// SuperList组件默认配置示例
export const SuperListDefaults: ComponentTypeDefaults = {
  type: 'SuperList',
  version: '1.0.0',
  defaultConfig: {
    // 外观配置
    theme: 'modern',
    size: 'medium',
    stripe: true,
    border: true,
    
    // 功能配置
    selection: false,
    sortable: true,
    filterable: true,
    resizable: true,
    
    // 分页配置
    pagination: true,
    pageSize: 20,
    pageSizes: [10, 20, 50, 100],
    
    // 工具栏配置
    showToolbar: true,
    showAdd: true,
    showEdit: true,
    showDelete: true,
    showExport: true,
    
    // 性能配置
    lazy: false,
    virtualScroll: false,
    rowHeight: 48
  }
}
```

### 4. 存储优化策略

```typescript
// 优化前的存储结构
interface OldComponentInstance {
  id: string
  type: string
  config: ComponentConfig // 完整配置，包含所有默认值
}

// 优化后的存储结构
interface OptimizedComponentInstance {
  id: string
  type: string
  configVersion: string // 默认配置版本号
  customConfig?: Partial<ComponentConfig> // 只存储修改的部分
  inheritanceChain?: string[] // 继承链，用于复杂场景
}

// 配置加载器
export class ConfigLoader {
  async loadComponentConfig(instance: OptimizedComponentInstance): Promise<ComponentConfig> {
    // 1. 获取默认配置
    const typeDefaults = await this.getTypeDefaults(instance.type, instance.configVersion)
    const globalDefaults = await this.getGlobalDefaults()
    
    // 2. 合并配置
    const mergedConfig = this.configMerger.mergeConfig(
      globalDefaults,
      typeDefaults,
      undefined, // 模板默认值
      { instanceId: instance.id, customConfig: instance.customConfig }
    )
    
    return mergedConfig
  }
  
  async saveComponentConfig(
    instanceId: string,
    newConfig: ComponentConfig
  ): Promise<void> {
    // 1. 获取默认配置
    const instance = await this.getInstance(instanceId)
    const typeDefaults = await this.getTypeDefaults(instance.type)
    
    // 2. 计算差异
    const diff = this.configMerger.calculateDiff(newConfig, typeDefaults.defaultConfig)
    
    // 3. 只保存差异部分
    await this.updateInstance(instanceId, {
      ...instance,
      customConfig: Object.keys(diff).length > 0 ? diff : undefined
    })
  }
}
```

## 🚀 实现策略

### 1. 渐进式迁移

```typescript
// 兼容性适配器 - 支持新旧格式平滑过渡
export class ConfigCompatibilityAdapter {
  async migrateToOptimizedFormat(oldInstance: OldComponentInstance): Promise<OptimizedComponentInstance> {
    const typeDefaults = await this.getTypeDefaults(oldInstance.type)
    const diff = this.configMerger.calculateDiff(oldInstance.config, typeDefaults.defaultConfig)
    
    return {
      id: oldInstance.id,
      type: oldInstance.type,
      configVersion: typeDefaults.version,
      customConfig: Object.keys(diff).length > 0 ? diff : undefined
    }
  }
  
  async loadLegacyConfig(instance: OptimizedComponentInstance | OldComponentInstance): Promise<ComponentConfig> {
    if ('config' in instance) {
      // 旧格式，直接返回
      return instance.config
    } else {
      // 新格式，动态合并
      return this.configLoader.loadComponentConfig(instance)
    }
  }
}
```

### 2. 版本管理

```typescript
// 默认配置版本管理
export class DefaultConfigVersionManager {
  async updateDefaults(type: string, newDefaults: ComponentConfig): Promise<void> {
    const currentVersion = await this.getCurrentVersion(type)
    const newVersion = this.generateVersion(currentVersion)
    
    // 保存新版本的默认配置
    await this.saveDefaults({
      type,
      version: newVersion,
      defaultConfig: newDefaults,
      migrationType: 'backward-compatible' // 或 'breaking-change'
    })
    
    // 触发实例迁移（如果需要）
    if (this.isBreakingChange(currentVersion, newVersion)) {
      await this.scheduleMigration(type, currentVersion, newVersion)
    }
  }
  
  async migrateInstances(type: string, fromVersion: string, toVersion: string): Promise<void> {
    const instances = await this.getInstancesByType(type, fromVersion)
    
    for (const instance of instances) {
      const oldDefaults = await this.getDefaults(type, fromVersion)
      const newDefaults = await this.getDefaults(type, toVersion)
      
      // 重新计算差异配置
      const fullConfig = this.configMerger.mergeConfig(
        await this.getGlobalDefaults(),
        oldDefaults,
        undefined,
        { instanceId: instance.id, customConfig: instance.customConfig }
      )
      
      const newDiff = this.configMerger.calculateDiff(fullConfig, newDefaults.defaultConfig)
      
      // 更新实例
      await this.updateInstance(instance.id, {
        ...instance,
        configVersion: toVersion,
        customConfig: Object.keys(newDiff).length > 0 ? newDiff : undefined
      })
    }
  }
}
```

### 3. 性能优化

```typescript
// 配置缓存管理
export class ConfigCacheManager {
  private configCache = new Map<string, ComponentConfig>()
  private defaultsCache = new Map<string, ComponentTypeDefaults>()
  
  async getCachedConfig(instanceId: string): Promise<ComponentConfig | null> {
    return this.configCache.get(instanceId) || null
  }
  
  setCachedConfig(instanceId: string, config: ComponentConfig): void {
    this.configCache.set(instanceId, config)
    
    // 设置过期时间
    setTimeout(() => {
      this.configCache.delete(instanceId)
    }, 5 * 60 * 1000) // 5分钟过期
  }
  
  async preloadDefaults(types: string[]): Promise<void> {
    const promises = types.map(async type => {
      const defaults = await this.getTypeDefaults(type)
      this.defaultsCache.set(type, defaults)
    })
    
    await Promise.all(promises)
  }
}
```

## 📊 效果评估

### 存储空间节省
```typescript
// 计算存储优化效果
interface StorageOptimizationReport {
  before: {
    totalInstances: number
    averageConfigSize: number // KB
    totalStorageSize: number // KB
  }
  after: {
    totalInstances: number
    averageCustomConfigSize: number // KB
    defaultConfigSize: number // KB
    totalStorageSize: number // KB
  }
  savings: {
    percentage: number
    absoluteSize: number // KB
  }
}

// 示例优化效果
const optimizationExample: StorageOptimizationReport = {
  before: {
    totalInstances: 1000,
    averageConfigSize: 2.5, // 每个实例2.5KB配置
    totalStorageSize: 2500 // 总计2.5MB
  },
  after: {
    totalInstances: 1000,
    averageCustomConfigSize: 0.3, // 平均只有0.3KB差异配置
    defaultConfigSize: 50, // 所有默认配置50KB
    totalStorageSize: 350 // 300KB差异配置 + 50KB默认配置
  },
  savings: {
    percentage: 86, // 节省86%存储空间
    absoluteSize: 2150 // 节省2.15MB
  }
}
```

### 性能提升
- **加载速度**: 减少60-80%的配置数据传输
- **内存占用**: 减少70-85%的配置数据内存占用
- **缓存效率**: 默认配置可以全局缓存，提高命中率

## 🔧 实施建议

### 1. 分阶段实施
1. **第一阶段**: 为核心组件定义默认配置
2. **第二阶段**: 实现配置合并引擎和存储优化
3. **第三阶段**: 渐进式迁移现有数据
4. **第四阶段**: 完善版本管理和缓存优化

### 2. 最佳实践
- 默认配置应该覆盖80%以上的常用场景
- 设计时考虑向下兼容性
- 实现配置可视化对比工具
- 建立默认配置的治理流程

### 3. 监控指标
- 存储空间使用率
- 配置加载性能
- 用户配置修改频率
- 默认配置使用率

通过这套默认值系统，可以显著减少配置数据的存储量，提升系统性能，同时保持良好的用户体验和维护性。