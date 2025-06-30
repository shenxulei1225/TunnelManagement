# Element Plus 组件属性参考文档

> **目标**: 提供Element Plus组件的完整API参考，减少开发中的属性使用错误
> **版本**: Element Plus 2.10.2
> **更新日期**: 2025-01-26

## 📋 目录

- [版本兼容性](#版本兼容性)
- [Button 按钮](#button-按钮)
- [Link 链接](#link-链接)
- [Form 表单](#form-表单)
- [Table 表格](#table-表格)
- [Dialog 对话框](#dialog-对话框)
- [Message 消息提示](#message-消息提示)
- [常见错误与解决方案](#常见错误与解决方案)
- [最佳实践](#最佳实践)

## 🔄 版本兼容性

### 当前项目版本
- **Element Plus**: 2.10.2 (2025-06-13)
- **Vue**: 3.x
- **兼容性**: 支持最新的2.10.x系列所有特性

### 重要版本变更记录
| 版本 | 重要变更 | 影响 |
|------|---------|------|
| 2.2.0 | Text Button 重新设计 | 建议使用新的 `text` 属性 |
| 2.2.1 | 新增 Link Button | 新增 `link` 属性 |
| 2.3.0 | 新增 Text 组件 | - |
| 2.8.0 | 新增 Mention 组件 | - |
| 2.10.0 | 新增 Splitter 组件 | - |
| 3.0.0 | 将移除 `type="text"` | 计划中的重大变更 |

## 🔘 Button 按钮

### 基本属性 (Attributes)

| 属性 | 说明 | 类型 | 可选值 | 默认值 | 版本 |
|------|------|------|--------|--------|------|
| size | 尺寸 | string | large / default / small | - | - |
| type | 类型 | string | primary / success / warning / danger / info | - | - |
| plain | 是否朴素按钮 | boolean | - | false | - |
| text | 是否文字按钮 | boolean | - | false | 2.2.0+ |
| bg | 文字按钮背景色是否常显 | boolean | - | false | 2.2.0+ |
| link | 是否链接按钮 | boolean | - | false | 2.2.1+ |
| round | 是否圆角按钮 | boolean | - | false | - |
| circle | 是否圆形按钮 | boolean | - | false | - |
| loading | 是否加载中状态 | boolean | - | false | - |
| disabled | 是否禁用状态 | boolean | - | false | - |
| icon | 图标组件 | string / Component | - | - | - |
| autofocus | 是否默认聚焦 | boolean | - | false | - |
| native-type | 原生type属性 | string | button / submit / reset | button | - |
| color | 自定义颜色 | string | - | - | - |
| dark | 暗色模式 | boolean | - | false | - |
| tag | 自定义元素标签 | string / Component | - | button | 2.3.4+ |

### 事件 (Events)

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| click | 点击时触发 | (event: Event) |

### 插槽 (Slots)

| 插槽名 | 说明 |
|--------|------|
| default | 默认内容 |
| loading | 自定义加载图标 |
| icon | 自定义图标 |

### 使用示例

```vue
<template>
  <!-- 基本用法 -->
  <el-button>默认按钮</el-button>
  <el-button type="primary">主要按钮</el-button>
  
  <!-- 链接按钮 (2.2.1+) -->
  <el-button link>链接按钮</el-button>
  <el-button type="primary" link>主要链接按钮</el-button>
  
  <!-- 文字按钮 (2.2.0+) -->
  <el-button text>文字按钮</el-button>
  <el-button text bg>带背景文字按钮</el-button>
  
  <!-- 图标按钮 -->
  <el-button :icon="Edit">编辑</el-button>
  <el-button type="primary" :icon="Search" />
  
  <!-- 加载状态 -->
  <el-button :loading="loading" @click="handleClick">提交</el-button>
</template>

<script setup>
import { Edit, Search } from '@element-plus/icons-vue'

const loading = ref(false)
const handleClick = () => {
  loading.value = true
  // 异步操作...
}
</script>
```

## 🔗 Link 链接

### 基本属性 (Attributes)

| 属性 | 说明 | 类型 | 可选值 | 默认值 | 版本 |
|------|------|------|--------|--------|------|
| type | 类型 | string | primary / success / warning / danger / info | default | - |
| underline | 下划线显示时机 | string | always / hover / never | hover | 2.9.9+ |
| disabled | 是否禁用 | boolean | - | false | - |
| href | 链接地址 | string | - | - | - |
| target | 链接打开方式 | string | _blank / _self / _parent / _top | _self | - |
| icon | 图标组件 | string / Component | - | - | - |

### 使用示例

```vue
<template>
  <!-- 基本用法 -->
  <el-link href="https://element-plus.org" target="_blank">
    Element Plus
  </el-link>
  
  <!-- 不同类型 -->
  <el-link type="primary">主要链接</el-link>
  <el-link type="success">成功链接</el-link>
  
  <!-- 下划线控制 (2.9.9+) -->
  <el-link underline="always">始终显示下划线</el-link>
  <el-link underline="hover">悬停显示下划线</el-link>
  <el-link underline="never">从不显示下划线</el-link>
  
  <!-- 带图标 -->
  <el-link :icon="Edit">编辑链接</el-link>
</template>
```

## 📝 Form 表单

### Form 属性

| 属性 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| model | 表单数据对象 | object | - |
| rules | 表单验证规则 | object | - |
| inline | 行内表单模式 | boolean | false |
| label-position | 标签位置 | string (left/right/top) | right |
| label-width | 标签宽度 | string / number | - |
| size | 组件尺寸 | string | - |
| disabled | 是否禁用 | boolean | false |

### FormItem 属性

| 属性 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| prop | 表单域model字段 | string | - |
| label | 标签文本 | string | - |
| label-width | 标签宽度 | string / number | - |
| required | 是否必填 | boolean | false |
| rules | 表单验证规则 | object / array | - |
| error | 表单域验证错误信息 | string | - |
| size | 组件尺寸 | string | - |

### 使用示例

```vue
<template>
  <el-form 
    ref="formRef" 
    :model="form" 
    :rules="rules" 
    label-width="120px"
  >
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" />
    </el-form-item>
    
    <el-form-item label="密码" prop="password">
      <el-input v-model="form.password" type="password" />
    </el-form-item>
    
    <el-form-item>
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
const formRef = ref()
const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      console.log('表单验证通过')
    }
  })
}

const resetForm = () => {
  formRef.value.resetFields()
}
</script>
```

## 📊 Table 表格

### 基本属性

| 属性 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| data | 显示的数据 | array | - |
| height | 表格高度 | string / number | - |
| max-height | 表格最大高度 | string / number | - |
| stripe | 是否为斑马纹 | boolean | false |
| border | 是否带有纵向边框 | boolean | false |
| size | 表格尺寸 | string | - |
| fit | 列的宽度是否自撑开 | boolean | true |
| show-header | 是否显示表头 | boolean | true |
| row-key | 行数据的Key | string / function | - |

### TableColumn 属性

| 属性 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| type | 列类型 | string | - |
| prop | 字段名称 | string | - |
| label | 显示的标题 | string | - |
| width | 列宽度 | string / number | - |
| min-width | 列最小宽度 | string / number | - |
| fixed | 列是否固定 | string / boolean | - |
| align | 对齐方式 | string | left |
| sortable | 是否可排序 | boolean / string | false |
| formatter | 格式化函数 | function | - |

### 使用示例

```vue
<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="date" label="日期" width="180" />
    <el-table-column prop="name" label="姓名" width="180" />
    <el-table-column prop="address" label="地址" />
    <el-table-column label="操作" width="200">
      <template #default="scope">
        <el-button link @click="handleEdit(scope.row)">编辑</el-button>
        <el-button link type="danger" @click="handleDelete(scope.row)">
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
```

## 📱 Dialog 对话框

### 基本属性

| 属性 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| v-model | 是否显示 | boolean | false |
| title | 标题 | string | - |
| width | 宽度 | string / number | 50% |
| top | 距离顶部位置 | string | 15vh |
| modal | 是否需要遮罩层 | boolean | true |
| close-on-click-modal | 点击遮罩是否关闭 | boolean | true |
| close-on-press-escape | 按ESC是否关闭 | boolean | true |
| show-close | 是否显示关闭按钮 | boolean | true |
| before-close | 关闭前的回调 | function | - |
| draggable | 是否可拖拽 | boolean | false |

### 事件

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| open | 打开时触发 | - |
| opened | 打开动画结束时触发 | - |
| close | 关闭时触发 | - |
| closed | 关闭动画结束时触发 | - |

### 使用示例

```vue
<template>
  <el-button @click="dialogVisible = true">打开对话框</el-button>
  
  <el-dialog
    v-model="dialogVisible"
    title="提示"
    width="500px"
    :before-close="handleClose"
  >
    <span>这是一段信息</span>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="dialogVisible = false">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
const dialogVisible = ref(false)

const handleClose = (done) => {
  ElMessageBox.confirm('确认关闭？')
    .then(() => {
      done()
    })
    .catch(() => {})
}
</script>
```

## 💬 Message 消息提示

### 基本用法

```javascript
// 基本消息
ElMessage('这是一条消息提示')

// 不同类型
ElMessage.success('成功提示')
ElMessage.warning('警告提示')
ElMessage.info('消息提示')
ElMessage.error('错误提示')

// 配置选项
ElMessage({
  message: '恭喜你，这是一条成功消息',
  type: 'success',
  duration: 3000,
  showClose: true,
  center: true
})
```

### 配置选项

| 选项 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| message | 消息文字 | string / VNode | - |
| type | 消息类型 | string | info |
| duration | 显示时间(毫秒) | number | 3000 |
| showClose | 是否显示关闭按钮 | boolean | false |
| center | 文字是否居中 | boolean | false |
| onClose | 关闭时的回调函数 | function | - |

## ❌ 常见错误与解决方案

### 1. Button 类型错误

```vue
<!-- ❌ 错误：不存在 type="link" -->
<el-button type="link">链接按钮</el-button>

<!-- ✅ 正确：使用 link 属性 -->
<el-button link>链接按钮</el-button>
<el-button type="primary" link>主要链接按钮</el-button>
```

### 2. Text Button 版本兼容

```vue
<!-- ⚠️ 旧版本用法 (将在3.0.0移除) -->
<el-button type="text">文字按钮</el-button>

<!-- ✅ 新版本用法 (2.2.0+) -->
<el-button text>文字按钮</el-button>
<el-button text bg>带背景文字按钮</el-button>
```

### 3. Link 下划线属性

```vue
<!-- ⚠️ 旧版本用法 (布尔值，3.0.0将移除) -->
<el-link :underline="false">无下划线</el-link>

<!-- ✅ 新版本用法 (2.9.9+) -->
<el-link underline="never">无下划线</el-link>
<el-link underline="hover">悬停显示</el-link>
<el-link underline="always">始终显示</el-link>
```

### 4. Form 验证错误

```vue
<!-- ❌ 错误：缺少 prop 属性 -->
<el-form-item label="用户名">
  <el-input v-model="form.username" />
</el-form-item>

<!-- ✅ 正确：必须添加 prop 属性进行验证 -->
<el-form-item label="用户名" prop="username">
  <el-input v-model="form.username" />
</el-form-item>
```

### 5. Table 插槽使用

```vue
<!-- ❌ 错误：旧版本插槽语法 -->
<el-table-column label="操作">
  <template slot-scope="scope">
    <el-button @click="edit(scope.row)">编辑</el-button>
  </template>
</el-table-column>

<!-- ✅ 正确：Vue 3 插槽语法 -->
<el-table-column label="操作">
  <template #default="scope">
    <el-button @click="edit(scope.row)">编辑</el-button>
  </template>
</el-table-column>
```

## 💡 最佳实践

### 1. 组件导入

```javascript
// ✅ 推荐：按需导入
import { ElButton, ElMessage } from 'element-plus'

// ✅ 全局注册（在main.js中）
import ElementPlus from 'element-plus'
app.use(ElementPlus)
```

### 2. 图标使用

```vue
<template>
  <!-- ✅ 推荐：使用 Element Plus 图标 -->
  <el-button :icon="Edit">编辑</el-button>
  
  <!-- ✅ 也可以使用插槽 -->
  <el-button>
    <template #icon>
      <Edit />
    </template>
    编辑
  </el-button>
</template>

<script setup>
import { Edit } from '@element-plus/icons-vue'
</script>
```

### 3. 主题定制

```css
/* 自定义CSS变量 */
:root {
  --el-color-primary: #409eff;
  --el-color-primary-light-3: #79bbff;
  --el-color-primary-light-5: #a0cfff;
  --el-color-primary-light-7: #c6e2ff;
  --el-color-primary-light-8: #d9ecff;
  --el-color-primary-light-9: #ecf5ff;
  --el-color-primary-dark-2: #337ecc;
}
```

### 4. 响应式设计

```vue
<template>
  <!-- 响应式Button尺寸 -->
  <el-button :size="buttonSize">响应式按钮</el-button>
</template>

<script setup>
import { computed } from 'vue'

const buttonSize = computed(() => {
  if (window.innerWidth < 768) return 'small'
  if (window.innerWidth < 1024) return 'default'
  return 'large'
})
</script>
```

### 5. 性能优化

```vue
<template>
  <!-- 大数据表格使用虚拟滚动 -->
  <el-table-v2
    :columns="columns"
    :data="data"
    :width="700"
    :height="400"
    fixed
  />
</template>
```

## 📚 参考资源

- [Element Plus 官方文档](https://element-plus.org/)
- [Element Plus GitHub](https://github.com/element-plus/element-plus)
- [Element Plus 图标库](https://element-plus.org/zh-CN/component/icon.html)
- [Element Plus 更新日志](https://github.com/element-plus/element-plus/releases)

## 🔄 文档维护

**更新频率**: 每个Element Plus大版本更新后及时同步
**维护责任人**: 前端开发团队
**反馈渠道**: 开发群组或Issue创建

---

> 💡 **提示**: 这个文档会持续更新，建议收藏并定期查看最新版本的API变更。
> 
> 🚨 **重要**: 在使用任何组件前，请先查看对应的版本兼容性信息！ 