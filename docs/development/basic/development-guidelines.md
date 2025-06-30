# 🛠️ 项目开发常见问题指导建议

## 📋 概述

本文档整理了项目开发过程中的常见问题和最佳实践，帮助开发者在编写代码时就考虑到这些问题，减少后期反复修复的工作量。

---

## 🔧 1. TypeScript类型错误修复

### 1.1 接口类型定义规范

#### ✅ 正确的Props接口定义

```typescript
// ❌ 错误：属性可能为undefined但没有处理
interface ComponentProps {
  data: ApiResponse
  config: Settings
}

// ✅ 正确：明确可选属性和类型检查
interface ComponentProps {
  data?: ApiResponse | null
  config: Settings
  width?: number
  height?: number
}

const props = defineProps<ComponentProps>()

// 使用时进行类型检查
if (props.data) {
  // 安全使用data
}
```

#### ✅ ComponentInstance接口标准定义

```typescript
// 基于项目实际使用的标准接口
interface ComponentInstance {
  id: string
  name: string
  type: string
  x: number
  y: number
  width: number
  height: number
  zIndex: number
  parentId?: string
  
  // 组件状态
  state?: {
    isSelected?: boolean
    isVisible?: boolean
    isLocked?: boolean
  }
  
  // 组件属性
  props?: Record<string, any>
  
  // 样式属性
  style?: {
    backgroundColor?: string
    borderColor?: string
    borderWidth?: number
    borderRadius?: number
    opacity?: number
  }
  
  // 子组件
  children?: ComponentInstance[]
}
```

#### ✅ PageSettings接口完整定义

```typescript
interface PageSettings {
  // 基础属性
  title: string
  description?: string
  
  // 尺寸属性
  width: number
  height: number
  minWidth?: number
  minHeight?: number
  
  // 样式属性
  backgroundColor: string
  backgroundImage?: string
  
  // 网格设置
  showGrid?: boolean
  gridSize?: number
  snapToGrid?: boolean
  
  // 标尺设置
  showRulers?: boolean
  rulerUnit?: 'px' | 'rem' | 'em'
  
  // 其他设置
  enableZoom?: boolean
  maxZoom?: number
  minZoom?: number
}
```

### 1.2 常见类型错误处理

#### ✅ 可能为undefined的属性处理

```typescript
// ❌ 错误：直接使用可能为undefined的属性
const shadowKey = `${effect.offset.x},${effect.offset.y}`

// ✅ 正确：先检查再使用
if (effect.offset && effect.color) {
  const shadowKey = `${effect.offset.x},${effect.offset.y},${effect.radius},${this.rgbToHex(effect.color)}`
}

// ✅ 正确：使用可选链操作符
const shadowKey = effect.offset ? `${effect.offset.x},${effect.offset.y}` : null
```

#### ✅ 事件处理器类型定义

```typescript
// ✅ 正确的事件定义
const emit = defineEmits<{
  select: [id: string]
  'update:modelValue': [value: any]
  'component-drag-start': [event: { component: any; event: DragEvent }]
  'component-drag-end': []
}>()

// ✅ 正确的事件处理函数
function handleDragStart(component: ComponentType, event: DragEvent) {
  emit('component-drag-start', { component, event })
}
```

---

## 🎯 2. 菜单管理自动化配置

### 2.1 标准菜单创建参数

基于项目的菜单管理API，每个新增页面需要提供以下参数：

```typescript
interface MenuCreationParams {
  // 必填参数
  name: string           // 菜单名称
  type: number          // 菜单类型：1-目录, 2-菜单, 3-按钮
  sort: number          // 显示顺序
  parentId: number      // 父菜单ID
  status: number        // 状态：0-正常, 1-停用
  
  // 菜单和目录必填
  path?: string         // 路由地址
  icon?: string         // 菜单图标
  visible?: boolean     // 是否可见
  
  // 菜单必填
  component?: string    // 组件路径
  componentName?: string // 组件名
  keepAlive?: boolean   // 是否缓存
  alwaysShow?: boolean  // 是否总是显示
  
  // 按钮必填
  permission?: string   // 权限标识
}
```

### 2.2 UX设计器页面菜单配置示例

```json
{
  "name": "UX设计器工作台",
  "type": 2,
  "sort": 210,
  "parentId": "UX设计器菜单的ID",
  "path": "workspace",
  "icon": "ep:brush-filled",
  "component": "ux-designer/workspace/index",
  "componentName": "UXDesignerWorkspace",
  "permission": "uxdesigner:workspace:design",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}
```

### 2.3 自动化菜单创建工具函数

```typescript
/**
 * 自动创建菜单配置
 */
export function generateMenuConfig(options: {
  pageName: string
  modulePrefix: string
  parentMenuId: number
  routePath: string
  componentPath: string
  icon?: string
  sort?: number
}): MenuCreationParams {
  const { pageName, modulePrefix, parentMenuId, routePath, componentPath, icon, sort } = options
  
  return {
    name: pageName,
    type: 2, // 菜单类型
    sort: sort || Date.now(),
    parentId: parentMenuId,
    path: routePath,
    icon: icon || 'ep:document',
    component: componentPath,
    componentName: toPascalCase(componentPath.replace(/\//g, '')),
    permission: `${modulePrefix}:${routePath}:query`,
    status: 0,
    visible: true,
    keepAlive: true,
    alwaysShow: false
  }
}

// 使用示例
const menuConfig = generateMenuConfig({
  pageName: "组件设计器",
  modulePrefix: "uxdesigner",
  parentMenuId: 5001,
  routePath: "component-designer",
  componentPath: "ux-designer/component/index",
  icon: "ep:grid",
  sort: 220
})
```

---

## 🚀 3. 功能完善最佳实践

### 3.1 数据持久化方案

#### ✅ 本地存储封装

```typescript
// utils/storage.ts
export class LocalStorage {
  static setItem<T>(key: string, value: T): void {
    try {
      localStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error('保存到本地存储失败:', error)
    }
  }
  
  static getItem<T>(key: string, defaultValue?: T): T | null {
    try {
      const item = localStorage.getItem(key)
      return item ? JSON.parse(item) : defaultValue || null
    } catch (error) {
      console.error('从本地存储读取失败:', error)
      return defaultValue || null
    }
  }
  
  static removeItem(key: string): void {
    localStorage.removeItem(key)
  }
}

// 使用示例
interface WorkspaceSettings {
  leftPanelWidth: number
  rightPanelWidth: number
  showGrid: boolean
  gridSize: number
}

// 保存工作台设置
LocalStorage.setItem('workspace-settings', {
  leftPanelWidth: 280,
  rightPanelWidth: 320,
  showGrid: true,
  gridSize: 20
})

// 读取工作台设置
const settings = LocalStorage.getItem<WorkspaceSettings>('workspace-settings', {
  leftPanelWidth: 280,
  rightPanelWidth: 320,
  showGrid: true,
  gridSize: 20
})
```

#### ✅ 用户偏好设置管理

```typescript
// composables/useUserPreferences.ts
export function useUserPreferences() {
  const preferences = ref({
    theme: 'light',
    language: 'zh-CN',
    autoSave: true,
    autoSaveInterval: 30000, // 30秒
    showMinimap: true,
    enableAnimations: true
  })
  
  // 加载偏好设置
  const loadPreferences = () => {
    const saved = LocalStorage.getItem('user-preferences')
    if (saved) {
      Object.assign(preferences.value, saved)
    }
  }
  
  // 保存偏好设置
  const savePreferences = () => {
    LocalStorage.setItem('user-preferences', preferences.value)
  }
  
  // 监听变化自动保存
  watch(preferences, savePreferences, { deep: true })
  
  onMounted(loadPreferences)
  
  return {
    preferences: readonly(preferences),
    updatePreference: (key: string, value: any) => {
      preferences.value[key] = value
    }
  }
}
```

#### ✅ 项目文件自动保存

```typescript
// composables/useAutoSave.ts
export function useAutoSave(
  getData: () => any,
  saveData: (data: any) => Promise<void>,
  interval: number = 30000
) {
  const isAutoSaving = ref(false)
  const lastSaveTime = ref<Date | null>(null)
  const hasUnsavedChanges = ref(false)
  
  let autoSaveTimer: NodeJS.Timeout | null = null
  
  const performAutoSave = async () => {
    if (isAutoSaving.value || !hasUnsavedChanges.value) return
    
    try {
      isAutoSaving.value = true
      const data = getData()
      await saveData(data)
      lastSaveTime.value = new Date()
      hasUnsavedChanges.value = false
      ElMessage.success('自动保存成功')
    } catch (error) {
      console.error('自动保存失败:', error)
      ElMessage.error('自动保存失败')
    } finally {
      isAutoSaving.value = false
    }
  }
  
  const startAutoSave = () => {
    if (autoSaveTimer) return
    autoSaveTimer = setInterval(performAutoSave, interval)
  }
  
  const stopAutoSave = () => {
    if (autoSaveTimer) {
      clearInterval(autoSaveTimer)
      autoSaveTimer = null
    }
  }
  
  const markAsChanged = () => {
    hasUnsavedChanges.value = true
  }
  
  onMounted(startAutoSave)
  onUnmounted(stopAutoSave)
  
  return {
    isAutoSaving: readonly(isAutoSaving),
    lastSaveTime: readonly(lastSaveTime),
    hasUnsavedChanges: readonly(hasUnsavedChanges),
    markAsChanged,
    performAutoSave
  }
}
```

### 3.2 性能优化方案

#### ✅ 大量组件渲染优化

```typescript
// 虚拟滚动组件渲染
import { VirtualList } from '@tanstack/vue-virtual'

// 组件列表虚拟化
const VirtualComponentList = defineComponent({
  setup(props) {
    const containerRef = ref<HTMLElement>()
    
    const virtualizer = useVirtualizer({
      count: props.items.length,
      getScrollElement: () => containerRef.value,
      estimateSize: () => 60,
      overscan: 5
    })
    
    return () => (
      <div ref={containerRef} style={{ height: '400px', overflow: 'auto' }}>
        <div style={{ 
          height: `${virtualizer.getTotalSize()}px`,
          position: 'relative'
        }}>
          {virtualizer.getVirtualItems().map(item => (
            <div
              key={item.key}
              style={{
                position: 'absolute',
                top: 0,
                left: 0,
                width: '100%',
                height: `${item.size}px`,
                transform: `translateY(${item.start}px)`
              }}
            >
              <ComponentItem data={props.items[item.index]} />
            </div>
          ))}
        </div>
      </div>
    )
  }
})
```

#### ✅ 拖拽操作性能优化

```typescript
// 拖拽防抖和性能优化
export function useDragPerformance() {
  const isDragging = ref(false)
  const dragThrottle = 16 // 60fps
  
  const createThrottledDragHandler = (handler: Function) => {
    let lastCall = 0
    return (...args: any[]) => {
      const now = Date.now()
      if (now - lastCall >= dragThrottle) {
        lastCall = now
        handler(...args)
      }
    }
  }
  
  const optimizedDragMove = createThrottledDragHandler((event: MouseEvent) => {
    // 拖拽移动处理逻辑
    requestAnimationFrame(() => {
      // 更新UI
    })
  })
  
  return {
    isDragging,
    optimizedDragMove
  }
}
```

#### ✅ 内存泄漏预防

```typescript
// 事件监听器管理
export function useEventManager() {
  const listeners = new Map<string, Function[]>()
  
  const addEventListener = (element: HTMLElement, event: string, handler: Function) => {
    element.addEventListener(event, handler as EventListener)
    
    const key = `${event}_${element.tagName}`
    if (!listeners.has(key)) {
      listeners.set(key, [])
    }
    listeners.get(key)!.push(handler)
  }
  
  const removeAllListeners = () => {
    listeners.forEach((handlers, key) => {
      handlers.forEach(handler => {
        // 移除事件监听器的逻辑
      })
    })
    listeners.clear()
  }
  
  onUnmounted(removeAllListeners)
  
  return {
    addEventListener,
    removeAllListeners
  }
}
```

---

## 🎨 4. 用户体验增强

### 4.1 拖拽功能完善

#### ✅ 统一拖拽管理系统

```typescript
// composables/useDragManager.ts
export function useDragManager() {
  const dragState = ref<{
    isDragging: boolean
    dragType: 'component' | 'resize' | 'move'
    startPosition: { x: number; y: number }
    currentElement: HTMLElement | null
    dragData: any
  }>({
    isDragging: false,
    dragType: 'component',
    startPosition: { x: 0, y: 0 },
    currentElement: null,
    dragData: null
  })
  
  const startDrag = (
    element: HTMLElement,
    event: MouseEvent,
    type: 'component' | 'resize' | 'move',
    data?: any
  ) => {
    dragState.value = {
      isDragging: true,
      dragType: type,
      startPosition: { x: event.clientX, y: event.clientY },
      currentElement: element,
      dragData: data
    }
    
    document.addEventListener('mousemove', handleDragMove)
    document.addEventListener('mouseup', handleDragEnd)
    
    // 添加拖拽样式
    element.classList.add('dragging')
    document.body.style.cursor = getCursorForDragType(type)
  }
  
  const handleDragMove = (event: MouseEvent) => {
    if (!dragState.value.isDragging) return
    
    const deltaX = event.clientX - dragState.value.startPosition.x
    const deltaY = event.clientY - dragState.value.startPosition.y
    
    // 根据拖拽类型执行相应操作
    switch (dragState.value.dragType) {
      case 'component':
        handleComponentMove(deltaX, deltaY)
        break
      case 'resize':
        handleComponentResize(deltaX, deltaY)
        break
      case 'move':
        handleElementMove(deltaX, deltaY)
        break
    }
  }
  
  const handleDragEnd = () => {
    if (dragState.value.currentElement) {
      dragState.value.currentElement.classList.remove('dragging')
    }
    
    document.removeEventListener('mousemove', handleDragMove)
    document.removeEventListener('mouseup', handleDragEnd)
    document.body.style.cursor = ''
    
    dragState.value.isDragging = false
  }
  
  return {
    dragState: readonly(dragState),
    startDrag
  }
}
```

#### ✅ 智能吸附和对齐

```typescript
// utils/snapGuides.ts
export class SnapGuides {
  private guides: { x: number[]; y: number[] } = { x: [], y: [] }
  private threshold = 5 // 吸附阈值
  
  addGuide(x?: number, y?: number) {
    if (x !== undefined) this.guides.x.push(x)
    if (y !== undefined) this.guides.y.push(y)
  }
  
  snap(x: number, y: number): { x: number; y: number; snapped: boolean } {
    let snappedX = x
    let snappedY = y
    let snapped = false
    
    // X轴吸附
    for (const guideX of this.guides.x) {
      if (Math.abs(x - guideX) <= this.threshold) {
        snappedX = guideX
        snapped = true
        break
      }
    }
    
    // Y轴吸附
    for (const guideY of this.guides.y) {
      if (Math.abs(y - guideY) <= this.threshold) {
        snappedY = guideY
        snapped = true
        break
      }
    }
    
    return { x: snappedX, y: snappedY, snapped }
  }
  
  clearGuides() {
    this.guides = { x: [], y: [] }
  }
}
```

### 4.2 撤销重做系统

#### ✅ 操作历史管理

```typescript
// composables/useUndoRedo.ts
export function useUndoRedo<T>(maxHistory = 50) {
  const undoStack = ref<T[]>([])
  const redoStack = ref<T[]>([])
  const currentState = ref<T | null>(null)
  
  const canUndo = computed(() => undoStack.value.length > 0)
  const canRedo = computed(() => redoStack.value.length > 0)
  
  const saveState = (state: T) => {
    // 保存当前状态到撤销栈
    if (currentState.value) {
      undoStack.value.push(cloneDeep(currentState.value))
      
      // 限制历史记录数量
      if (undoStack.value.length > maxHistory) {
        undoStack.value.shift()
      }
    }
    
    // 清空重做栈
    redoStack.value = []
    
    // 更新当前状态
    currentState.value = cloneDeep(state)
  }
  
  const undo = (): T | null => {
    if (!canUndo.value) return null
    
    const previousState = undoStack.value.pop()!
    
    if (currentState.value) {
      redoStack.value.push(cloneDeep(currentState.value))
    }
    
    currentState.value = cloneDeep(previousState)
    return currentState.value
  }
  
  const redo = (): T | null => {
    if (!canRedo.value) return null
    
    const nextState = redoStack.value.pop()!
    
    if (currentState.value) {
      undoStack.value.push(cloneDeep(currentState.value))
    }
    
    currentState.value = cloneDeep(nextState)
    return currentState.value
  }
  
  const clear = () => {
    undoStack.value = []
    redoStack.value = []
    currentState.value = null
  }
  
  return {
    canUndo,
    canRedo,
    saveState,
    undo,
    redo,
    clear
  }
}
```

---

## 📱 5. 响应式设计优化

### 5.1 移动端适配方案

#### ✅ 响应式布局Hook

```typescript
// composables/useResponsive.ts
export function useResponsive() {
  const windowWidth = ref(window.innerWidth)
  const windowHeight = ref(window.innerHeight)
  
  const isMobile = computed(() => windowWidth.value < 768)
  const isTablet = computed(() => windowWidth.value >= 768 && windowWidth.value < 1024)
  const isDesktop = computed(() => windowWidth.value >= 1024)
  
  const breakpoint = computed(() => {
    if (isMobile.value) return 'mobile'
    if (isTablet.value) return 'tablet'
    return 'desktop'
  })
  
  const updateSize = () => {
    windowWidth.value = window.innerWidth
    windowHeight.value = window.innerHeight
  }
  
  onMounted(() => {
    window.addEventListener('resize', updateSize)
  })
  
  onUnmounted(() => {
    window.removeEventListener('resize', updateSize)
  })
  
  return {
    windowWidth: readonly(windowWidth),
    windowHeight: readonly(windowHeight),
    isMobile,
    isTablet,
    isDesktop,
    breakpoint
  }
}
```

#### ✅ 触摸手势支持

```typescript
// composables/useTouchGestures.ts
export function useTouchGestures() {
  const touches = ref<TouchList | null>(null)
  const isMultiTouch = computed(() => touches.value && touches.value.length > 1)
  
  const handleTouchStart = (event: TouchEvent) => {
    touches.value = event.touches
  }
  
  const handleTouchMove = (event: TouchEvent) => {
    if (!touches.value) return
    
    if (isMultiTouch.value) {
      // 处理缩放手势
      handlePinchZoom(event)
    } else {
      // 处理单指拖拽
      handleSingleTouchMove(event)
    }
  }
  
  const handlePinchZoom = (event: TouchEvent) => {
    if (event.touches.length !== 2) return
    
    const touch1 = event.touches[0]
    const touch2 = event.touches[1]
    
    const distance = Math.sqrt(
      Math.pow(touch2.clientX - touch1.clientX, 2) +
      Math.pow(touch2.clientY - touch1.clientY, 2)
    )
    
    // 缩放逻辑
  }
  
  return {
    handleTouchStart,
    handleTouchMove,
    isMultiTouch
  }
}
```

---

## 🔍 6. 测试和质量保证

### 6.1 单元测试规范

#### ✅ 组件测试模板

```typescript
// ComponentName.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ComponentName from '@/components/ComponentName.vue'

describe('ComponentName', () => {
  it('应该正确渲染', () => {
    const wrapper = mount(ComponentName, {
      props: {
        title: 'Test Title'
      }
    })
    
    expect(wrapper.text()).toContain('Test Title')
  })
  
  it('应该正确处理事件', async () => {
    const wrapper = mount(ComponentName)
    
    await wrapper.find('button').trigger('click')
    
    expect(wrapper.emitted('click')).toBeTruthy()
  })
})
```

#### ✅ 工具函数测试

```typescript
// utils.test.ts
import { describe, it, expect } from 'vitest'
import { generateMenuConfig, LocalStorage } from '@/utils'

describe('generateMenuConfig', () => {
  it('应该生成正确的菜单配置', () => {
    const config = generateMenuConfig({
      pageName: '测试页面',
      modulePrefix: 'test',
      parentMenuId: 1,
      routePath: 'test-page',
      componentPath: 'test/page/index'
    })
    
    expect(config.name).toBe('测试页面')
    expect(config.type).toBe(2)
    expect(config.permission).toBe('test:test-page:query')
  })
})

describe('LocalStorage', () => {
  it('应该正确保存和读取数据', () => {
    const testData = { key: 'value' }
    
    LocalStorage.setItem('test', testData)
    const retrieved = LocalStorage.getItem('test')
    
    expect(retrieved).toEqual(testData)
  })
})
```

---

## 📚 7. 代码规范和最佳实践

### 7.1 命名规范

```typescript
// ✅ 文件命名
// 组件: PascalCase
ComponentName.vue
// Hook: camelCase with use prefix
useComponentLogic.ts
// 工具: camelCase
formatUtils.ts

// ✅ 变量命名
const componentInstances = ref<ComponentInstance[]>([])  // 复数形式表示数组
const selectedComponent = ref<ComponentInstance | null>(null)  // 单数形式表示单个对象
const isLoading = ref(false)  // 布尔值使用is/has/can前缀

// ✅ 函数命名
function handleComponentSelect() {}  // 事件处理器使用handle前缀
function validateFormData() {}  // 验证函数使用validate前缀
function formatDateTime() {}  // 格式化函数使用format前缀
```

### 7.2 错误处理规范

```typescript
// ✅ 统一错误处理
export class ApiError extends Error {
  constructor(
    message: string,
    public code: string,
    public statusCode: number
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

// ✅ 异步操作错误处理
async function saveWorkspaceFile(file: WorkspaceFile) {
  try {
    loading.value = true
    const result = await fileApi.save(file)
    ElMessage.success('保存成功')
    return result
  } catch (error) {
    console.error('保存文件失败:', error)
    
    if (error instanceof ApiError) {
      ElMessage.error(`保存失败: ${error.message}`)
    } else {
      ElMessage.error('保存失败，请重试')
    }
    
    throw error
  } finally {
    loading.value = false
  }
}
```

---

## 🎯 8. 检查清单

### 8.1 开发前检查清单

- [ ] **TypeScript类型**: 所有接口都已正确定义，属性可空性已明确标注
- [ ] **Props定义**: 组件Props使用TypeScript接口，包含所有必要属性
- [ ] **事件定义**: emit事件已正确类型化
- [ ] **错误处理**: 异步操作包含完整的错误处理逻辑
- [ ] **菜单配置**: 新页面的菜单参数已准备完整

### 8.2 开发中检查清单

- [ ] **数据持久化**: 用户设置和工作状态已保存到本地存储
- [ ] **性能优化**: 大量数据渲染使用虚拟化，拖拽操作已优化
- [ ] **用户体验**: 拖拽功能流畅，支持吸附对齐
- [ ] **撤销重做**: 重要操作已记录到历史栈
- [ ] **响应式设计**: 移动端和平板端适配良好

### 8.3 发布前检查清单

- [ ] **单元测试**: 关键组件和工具函数已测试覆盖
- [ ] **类型检查**: 运行`npm run type-check`无错误
- [ ] **代码规范**: 通过ESLint和Prettier检查
- [ ] **性能测试**: 大量组件场景下性能表现良好
- [ ] **兼容性测试**: 在目标浏览器中功能正常

---

## 🚀 9. 每生成一个页面的菜单提示

### 9.1 自动生成菜单提示函数

```typescript
/**
 * 生成菜单创建提示信息
 */
export function generateMenuPrompt(pageName: string, routePath: string, componentPath: string) {
  console.log(`
🎯 菜单管理配置参数：
{
  "name": "${pageName}",
  "type": 2,
  "sort": ${Date.now()},
  "parentId": "[请填写上级菜单ID]",
  "path": "${routePath}",
  "icon": "[请选择合适图标]",
  "component": "${componentPath}",
  "componentName": "${toPascalCase(componentPath.replace(/\//g, ''))}",
  "permission": "[模块前缀]:${routePath.replace(/-/g, ':')}:query",
  "status": 0,
  "visible": true,
  "keepAlive": true,
  "alwaysShow": false
}

📝 请复制以上配置到菜单管理系统中创建菜单
  `)
}

// 在页面创建时自动调用
generateMenuPrompt("UX设计器", "ux-designer", "ux-designer/workspace/index")
```

---

## 📋 开发检查清单

### 开发前检查清单

**基础准备：**
- [ ] TypeScript类型定义完整
- [ ] Props接口定义正确
- [ ] 事件处理器类型正确
- [ ] 错误处理逻辑完善

**多租户检查：**
- [ ] 数据库表包含`tenant_id`字段
- [ ] 实体类继承`TenantBaseDO`
- [ ] 确认租户隔离策略

**菜单配置：**
- [ ] 菜单配置参数准备完整
- [ ] 权限标识符定义正确
- [ ] 路由配置匹配

### 开发中检查清单

**功能实现：**
- [ ] 实现数据持久化
- [ ] 性能优化（虚拟滚动、防抖等）
- [ ] 用户体验优化（加载状态、错误提示）
- [ ] 撤销重做功能实现
- [ ] 响应式设计适配

**多租户功能：**
- [ ] API请求包含`tenant-id`头
- [ ] Mock数据设置正确租户ID
- [ ] 租户上下文正确传递
- [ ] 数据隔离功能验证

### 发布前检查清单

**质量保证：**
- [ ] 单元测试覆盖
- [ ] TypeScript类型检查通过
- [ ] 代码规范检查通过
- [ ] 性能测试通过
- [ ] 兼容性测试通过

**多租户验证：**
- [ ] 租户隔离功能验证
- [ ] 跨租户数据访问测试
- [ ] 租户切换功能正常
- [ ] 数据查询结果正确

## 📋 结语

这份指导建议涵盖了项目开发中的主要问题点，特别是多租户架构的重要注意事项。按照这些规范开发可以显著减少后期维护成本。

**重要提醒：**
每次创建新页面/功能时，都应该：
1. 使用标准的TypeScript接口定义
2. 🔴 **确保数据库表包含`tenant_id`字段**
3. 🔴 **Mock数据设置正确的租户ID（避免默认0值）**
4. 提供完整的菜单配置参数
5. 考虑数据持久化需求
6. 实现必要的性能优化
7. 确保响应式设计兼容性
8. 编写相应的单元测试

通过遵循这些最佳实践，可以大幅提升开发效率和代码质量，避免常见的多租户相关问题。 

## 多租户开发规范

**⚠️ 重要：本项目采用多租户架构，以下规范必须严格遵守**

### 数据库表设计规范

```sql
-- ✅ 正确：所有业务表都必须包含tenant_id字段
CREATE TABLE `your_business_table` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint NOT NULL DEFAULT '1' COMMENT '租户编号',
  -- 其他业务字段...
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者', 
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务表';

-- ❌ 错误：缺少tenant_id字段导致多租户功能异常
CREATE TABLE `wrong_table` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  -- 缺少tenant_id字段！
  `name` varchar(100),
  PRIMARY KEY (`id`)
);
```

### Java实体类规范

```java
// ✅ 正确：业务实体继承TenantBaseDO
@TableName(value = "your_business_table", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YourBusinessDO extends TenantBaseDO {
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 业务字段
     */
    private String name;
    
    // 其他业务字段...
}

// ❌ 错误：不继承TenantBaseDO导致租户隔离失效
@TableName(value = "wrong_table")
@Data
public class WrongDO extends BaseDO {
    private Long id;
    private String name;
    // 缺少租户字段！
}
```

### Mock数据和测试数据规范

```java
// ✅ 正确：Mock数据必须设置正确的租户ID
@Test
public void testCreateData() {
    // 设置当前租户上下文
    TenantContextHolder.setTenantId(1L);
    
    // 创建mock数据，明确设置租户ID
    YourBusinessDO mockData = randomPojo(YourBusinessDO.class, o -> {
        o.setTenantId(1L); // 明确设置租户ID为1（默认租户）
        o.setName("测试数据");
    });
    
    // 执行测试...
}

// ❌ 错误：Mock数据租户ID为0导致数据不可见
@Test
public void wrongTest() {
    // 没有设置租户上下文或设置错误的租户ID
    YourBusinessDO mockData = randomPojo(YourBusinessDO.class, o -> {
        // o.setTenantId(0L); // 默认为0，导致数据不可见！
        o.setName("测试数据");
    });
}
```

### 前端API调用规范

```typescript
// ✅ 正确：API请求必须包含tenant-id头
import { getTenantId } from '@/utils/auth'

const apiRequest = async () => {
  const tenantId = getTenantId() || 1 // 默认租户ID为1
  
  return request({
    url: '/api/your-endpoint',
    method: 'GET',
    headers: {
      'tenant-id': tenantId
    }
  })
}

// ❌ 错误：缺少tenant-id头导致请求失败
const wrongRequest = async () => {
  return request({
    url: '/api/your-endpoint',  
    method: 'GET'
    // 缺少tenant-id头！
  })
}
```

### 租户配置检查清单

**数据库表检查：**
- [ ] 所有业务表都包含`tenant_id`字段
- [ ] `tenant_id`字段设置了正确的默认值（通常为1）
- [ ] 添加了`tenant_id`的索引
- [ ] 在`TenantProperties.ignoreTables`中配置了不需要多租户的表

**Java代码检查：**
- [ ] 业务实体类继承`TenantBaseDO`
- [ ] 测试数据明确设置了`tenantId`
- [ ] 没有在代码中硬编码租户ID（除了测试）
- [ ] 使用`TenantContextHolder`获取当前租户

**前端代码检查：**
- [ ] API请求包含`tenant-id`请求头
- [ ] 登录后正确保存租户信息
- [ ] 页面刷新后能正确获取租户信息

### 常见租户问题解决方案

**问题1：数据查询结果为空**
```java
// 原因：tenant_id字段缺失或值为0
// 解决：检查表结构和数据中的tenant_id字段

// 临时修复SQL（仅用于数据修复）
UPDATE your_table SET tenant_id = 1 WHERE tenant_id = 0;
```

**问题2：Mock数据在测试中不可见**
```java
// 原因：测试数据tenant_id为0，但查询时租户上下文为1
// 解决：统一设置测试租户
@BeforeEach
public void setUp() {
    TenantContextHolder.setTenantId(1L);
}

@Test
public void testWithCorrectTenant() {
    YourBusinessDO testData = randomPojo(YourBusinessDO.class, o -> {
        o.setTenantId(1L); // 与上下文保持一致
    });
    // 测试逻辑...
}
```

**问题3：API请求被拒绝**
```typescript
// 原因：缺少tenant-id请求头
// 解决：配置axios拦截器统一添加
axios.interceptors.request.use(config => {
  const tenantId = getTenantId()
  if (tenantId) {
    config.headers['tenant-id'] = tenantId
  }
  return config
})
```

## 菜单管理自动化

// ... existing code ... 