# 前端开发指导指南

## 一、项目结构规范

### 1. 目录结构
```
src/
├── api/              # API接口定义
├── assets/           # 静态资源
├── components/       # 公共组件
├── hooks/           # 自定义Hook
├── layouts/         # 布局组件
├── router/          # 路由配置
├── store/           # 状态管理
├── styles/          # 样式文件
├── utils/           # 工具函数
└── views/           # 页面组件
```

### 2. 命名规范

#### 2.1 文件命名规范
| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 组件文件 | PascalCase.vue | UserList.vue |
| API文件 | 模块名.ts | user.ts |
| 工具文件 | camelCase.ts | dateUtil.ts |
| 样式文件 | kebab-case.scss | user-list.scss |
| 类型文件 | PascalCase.d.ts | UserType.d.ts |

#### 2.2 变量命名规范
| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 组件名 | PascalCase | UserList |
| 变量 | camelCase | userName |
| 常量 | UPPER_CASE | MAX_COUNT |
| 私有变量 | _camelCase | _privateVar |
| 类型/接口 | PascalCase | UserInfo |

## 二、组件开发规范

### 1. 组件基本结构
```vue
<template>
  <div class="user-list">
    <!-- 模板内容 -->
  </div>
</template>

<script setup lang="ts">
// 类型导入
import type { UserInfo } from '@/types';

// 组件导入
import { ElTable } from 'element-plus';

// 属性定义
const props = defineProps<{
  list: UserInfo[];
}>();

// 事件定义
const emit = defineEmits<{
  (e: 'update', id: number): void;
}>();

// 响应式数据
const visible = ref(false);

// 计算属性
const computedValue = computed(() => {
  // 计算逻辑
});

// 方法定义
const handleClick = () => {
  // 处理逻辑
};
</script>

<style lang="scss" scoped>
.user-list {
  // 样式定义
}
</style>
```

### 2. 组件通信规范

#### 2.1 Props定义
```typescript
// 类型定义
interface Props {
  title: string;
  list?: UserInfo[];
  status: 'active' | 'inactive';
}

// Props声明
const props = withDefaults(defineProps<Props>(), {
  list: () => [],
  status: 'active'
});
```

#### 2.2 事件定义
```typescript
// 事件类型定义
interface Emits {
  (e: 'update', data: UserInfo): void;
  (e: 'delete', id: number): void;
}

// 事件声明
const emit = defineEmits<Emits>();
```

### 3. 状态管理规范

#### 3.1 Pinia Store定义
```typescript
export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: null as UserInfo | null,
    permissions: [] as string[]
  }),
  
  getters: {
    hasPermission: (state) => {
      return (permission: string) => state.permissions.includes(permission);
    }
  },
  
  actions: {
    async fetchUserInfo() {
      // 获取用户信息
    }
  }
});
```

## 三、样式开发规范

### 1. SCSS使用规范
```scss
// 变量定义
$primary-color: #409eff;
$border-radius: 4px;

// 混入定义
@mixin flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

// 组件样式
.user-card {
  @include flex-center;
  border-radius: $border-radius;
  
  &__header {
    font-weight: bold;
  }
  
  &__content {
    padding: 16px;
  }
}
```

### 2. 响应式设计规范
```scss
// 断点定义
$breakpoints: (
  'sm': 576px,
  'md': 768px,
  'lg': 992px,
  'xl': 1200px
);

// 混入使用
@mixin respond-to($breakpoint) {
  @media (min-width: map-get($breakpoints, $breakpoint)) {
    @content;
  }
}

// 使用示例
.container {
  width: 100%;
  
  @include respond-to('md') {
    width: 750px;
  }
  
  @include respond-to('lg') {
    width: 970px;
  }
}
```

## 四、API调用规范

### 1. API定义
```typescript
// api/user.ts
import request from '@/utils/request';
import type { UserInfo, CreateUserParams } from '@/types';

export const userApi = {
  getList(params: PageParams) {
    return request.get<PageResult<UserInfo>>('/system/user/list', { params });
  },
  
  create(data: CreateUserParams) {
    return request.post<number>('/system/user/create', data);
  },
  
  update(data: UpdateUserParams) {
    return request.put('/system/user/update', data);
  },
  
  delete(id: number) {
    return request.delete(`/system/user/delete?id=${id}`);
  }
};
```

### 2. API调用
```typescript
// 在组件中使用
const { data, loading, error } = await useRequest(() => userApi.getList(params));

// 错误处理
const handleSubmit = async () => {
  try {
    await userApi.create(form);
    ElMessage.success('创建成功');
  } catch (err) {
    console.error(err);
    ElMessage.error('创建失败');
  }
};
```

## 五、类型定义规范

### 1. 基础类型定义
```typescript
// types/user.d.ts
export interface UserInfo {
  id: number;
  username: string;
  nickname?: string;
  status: UserStatus;
  createTime: string;
}

export type UserStatus = 'active' | 'inactive' | 'locked';

export interface CreateUserParams {
  username: string;
  password: string;
  nickname?: string;
  email: string;
}
```

### 2. 工具类型使用
```typescript
// 分页参数类型
export interface PageParams {
  pageNo: number;
  pageSize: number;
  [key: string]: any;
}

// 分页结果类型
export interface PageResult<T> {
  list: T[];
  total: number;
}

// 请求响应类型
export interface ApiResponse<T> {
  code: number;
  data: T;
  msg: string;
}
```

## 六、开发流程指南

### 1. 功能开发流程
1. 需求分析
   - 理解UI/UX设计
   - 确认交互流程
   - 识别组件结构
   - 规划状态管理

2. 编码实现
   - 创建组件文件
   - 实现组件逻辑
   - 编写样式代码
   - 对接后端API

3. 测试验证
   - 功能测试
   - 兼容性测试
   - 性能测试
   - 交互体验测试

### 2. 开发注意事项

#### 2.1 性能优化
- 合理使用计算属性
- 避免不必要的渲染
- 使用异步组件
- 图片资源优化

#### 2.2 代码质量
- 组件职责单一
- 代码复用
- 类型完整性
- 注释完整性

#### 2.3 用户体验
- 添加加载状态
- 合理的错误提示
- 表单验证
- 响应式适配

## 七、常用工具说明

### 1. 请求工具
```typescript
// utils/request.ts
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

request.interceptors.request.use(
  config => {
    // 请求拦截处理
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  response => {
    // 响应拦截处理
    return response.data;
  },
  error => {
    return Promise.reject(error);
  }
);

export default request;
```

### 2. 工具函数
```typescript
// utils/date.ts
export const formatDate = (date: Date, format = 'YYYY-MM-DD'): string => {
  // 日期格式化逻辑
};

// utils/validate.ts
export const isEmail = (email: string): boolean => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};
```

### 3. 自定义Hook
```typescript
// hooks/useTable.ts
export function useTable<T>(api: (params: any) => Promise<PageResult<T>>) {
  const loading = ref(false);
  const list = ref<T[]>([]);
  const total = ref(0);
  
  const loadData = async (params: PageParams) => {
    loading.value = true;
    try {
      const { list: data, total: count } = await api(params);
      list.value = data;
      total.value = count;
    } finally {
      loading.value = false;
    }
  };
  
  return {
    loading,
    list,
    total,
    loadData
  };
}
``` 