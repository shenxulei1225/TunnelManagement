# 通用API接口设计规范

> **零代码开发核心理念**：用户只需定义业务类型，系统自动生成标准CRUD接口

## 🎯 设计目标

### 核心原则
1. **用户友好**：业务人员只需要理解"业务类型"概念，无需了解技术细节
2. **标准化**：所有业务都遵循统一的API规范，确保一致性
3. **自动化**：系统根据业务类型自动生成完整的API接口
4. **可扩展**：支持业务特有功能的定制化扩展

### 用户体验流程
```
用户定义业务类型 → 系统生成API接口 → 前端动态调用 → 完整业务系统
```

## 📋 API规范设计

### 基础URL模式
```
/api/business/{businessType}
```

**示例**：
- 设备管理：`/api/business/device`
- 文档管理：`/api/business/document`  
- 用户管理：`/api/business/user`

### 标准CRUD接口

#### 1. 列表查询
```http
GET /api/business/{businessType}/list
```

**查询参数**：
```typescript
interface QueryParams {
  page?: number          // 页码，默认1
  pageSize?: number      // 每页数量，默认20
  search?: string        // 搜索关键词
  sortField?: string     // 排序字段
  sortOrder?: 'asc' | 'desc'  // 排序方向
  categoryId?: string    // 分类ID
  status?: string        // 状态
  [key: string]: any     // 其他过滤条件
}
```

**响应格式**：
```typescript
interface ApiResponse<T> {
  code: number
  message: string
  data: T[]
  pagination: {
    total: number
    page: number
    pageSize: number
    pages: number
  }
  timestamp: number
}
```

#### 2. 详情查询
```http
GET /api/business/{businessType}/get/{id}
```

#### 3. 新增数据
```http
POST /api/business/{businessType}/create
```

#### 4. 更新数据  
```http
PUT /api/business/{businessType}/update/{id}
```

#### 5. 删除数据
```http
DELETE /api/business/{businessType}/delete/{id}
```

#### 6. 批量删除
```http
DELETE /api/business/{businessType}/batch-delete
Body: { ids: string[] }
```

### 扩展功能接口

#### 分类管理
```http
GET    /api/business/{businessType}/category/tree      # 分类树
POST   /api/business/{businessType}/category/create    # 创建分类
PUT    /api/business/{businessType}/category/update/{id}  # 更新分类
DELETE /api/business/{businessType}/category/delete/{id}  # 删除分类
```

#### 数据导入导出
```http
POST /api/business/{businessType}/import    # 数据导入
GET  /api/business/{businessType}/export    # 数据导出
```

#### 字段配置
```http
GET /api/business/{businessType}/field-config      # 获取字段配置
PUT /api/business/{businessType}/field-config      # 更新字段配置
```

#### 业务统计
```http
GET /api/business/{businessType}/statistics   # 获取业务统计数据
```

## 🏗️ 后端实现架构

### 1. 通用控制器
```java
@RestController
@RequestMapping("/api/business/{businessType}")
public class UniversalBusinessController {
    
    @Autowired
    private UniversalBusinessService businessService;
    
    @GetMapping("/list")
    public Result<PageResult<Map<String, Object>>> list(
        @PathVariable String businessType,
        @RequestParam Map<String, Object> params) {
        return businessService.getList(businessType, params);
    }
    
    @PostMapping("/create")
    public Result<Map<String, Object>> create(
        @PathVariable String businessType,
        @RequestBody Map<String, Object> data) {
        return businessService.create(businessType, data);
    }
    
    // ... 其他CRUD方法
}
```

### 2. 通用服务层
```java
@Service
public class UniversalBusinessService {
    
    @Autowired
    private BusinessTypeConfigService configService;
    
    @Autowired
    private DynamicSqlExecutor sqlExecutor;
    
    public PageResult<Map<String, Object>> getList(String businessType, Map<String, Object> params) {
        // 1. 获取业务类型配置
        BusinessTypeConfig config = configService.getConfig(businessType);
        
        // 2. 构建动态SQL查询
        String sql = buildSelectSql(config, params);
        
        // 3. 执行查询并返回结果
        return sqlExecutor.selectPage(sql, params);
    }
    
    public Map<String, Object> create(String businessType, Map<String, Object> data) {
        // 1. 获取业务类型配置
        BusinessTypeConfig config = configService.getConfig(businessType);
        
        // 2. 数据验证
        validateData(config, data);
        
        // 3. 构建插入SQL
        String sql = buildInsertSql(config, data);
        
        // 4. 执行插入
        return sqlExecutor.insert(sql, data);
    }
}
```

### 3. 动态SQL构建器
```java
@Component
public class DynamicSqlBuilder {
    
    public String buildSelectSql(BusinessTypeConfig config, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        
        // 根据字段配置构建SELECT子句
        sql.append(config.getFields().stream()
            .filter(field -> field.isShowInList())
            .map(field -> field.getKey())
            .collect(Collectors.joining(", ")));
            
        sql.append(" FROM ").append(config.getTableName());
        
        // 构建WHERE条件
        appendWhereConditions(sql, config, params);
        
        // 构建ORDER BY
        appendOrderBy(sql, params);
        
        return sql.toString();
    }
}
```

## 🎨 前端集成方案

### 1. 业务API工厂
```typescript
export class BusinessApiFactory {
  static getApi(businessType: string): UniversalBusinessApi {
    return new UniversalBusinessApi(businessType);
  }
}

// 使用示例
const deviceApi = BusinessApiFactory.getApi('device');
const devices = await deviceApi.getList({ page: 1, pageSize: 20 });
```

### 2. 动态组件渲染
```vue
<template>
  <StandardDataView :config="businessConfig" />
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { BusinessApiFactory } from '@/api/business/BusinessTypeApi';

const businessType = 'device'; // 从路由或配置获取
const businessApi = BusinessApiFactory.getApi(businessType);

const businessConfig = ref({
  api: {
    list: () => businessApi.getList(),
    create: (data) => businessApi.create(data),
    update: (id, data) => businessApi.update(id, data),
    delete: (id) => businessApi.delete(id)
  },
  // 其他配置...
});
</script>
```

## 📊 数据存储方案

### 1. 业务类型表
```sql
CREATE TABLE business_type (
  id VARCHAR(50) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  table_name VARCHAR(64) NOT NULL,
  menu_path JSON,
  icon VARCHAR(50),
  tags JSON,
  fields JSON NOT NULL,
  config JSON,
  status VARCHAR(20) DEFAULT 'active',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 2. 通用业务数据表
```sql
-- 每个业务类型对应一个数据表
CREATE TABLE business_{businessType} (
  id VARCHAR(50) PRIMARY KEY,
  business_type VARCHAR(50) NOT NULL,
  category_id VARCHAR(50),
  status VARCHAR(20) DEFAULT 'active',
  sort INT DEFAULT 0,
  create_by VARCHAR(50),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_by VARCHAR(50),
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  -- 动态字段根据业务类型配置生成
  ...
);
```

### 3. 分类管理表
```sql
CREATE TABLE business_category (
  id VARCHAR(50) PRIMARY KEY,
  business_type VARCHAR(50) NOT NULL,
  parent_id VARCHAR(50),
  name VARCHAR(100) NOT NULL,
  description TEXT,
  sort INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 实现步骤

### 阶段一：核心框架（2周）
1. **通用控制器开发**
   - 实现标准CRUD接口
   - 支持动态路由参数
   - 统一异常处理

2. **动态SQL引擎**
   - SQL构建器开发
   - 参数绑定机制
   - 查询优化

3. **业务类型配置管理**
   - 配置存储结构
   - 配置验证逻辑
   - 配置热更新

### 阶段二：功能扩展（2周）
1. **分类管理功能**
2. **数据导入导出**
3. **字段配置管理**
4. **权限控制集成**

### 阶段三：前端集成（1周）
1. **API工厂开发**
2. **动态组件集成**
3. **模板库完善**

## 📈 性能优化

### 1. 缓存策略
- **业务类型配置缓存**：Redis缓存，避免频繁数据库查询
- **字段配置缓存**：内存缓存，提高SQL构建效率
- **查询结果缓存**：按业务需求选择性缓存

### 2. SQL优化
- **索引策略**：为常用查询字段建立索引
- **分页优化**：使用LIMIT OFFSET优化
- **查询重写**：复杂查询的SQL优化

### 3. 并发控制
- **乐观锁**：使用版本号防止并发更新冲突
- **连接池**：合理配置数据库连接池
- **异步处理**：大批量操作使用异步处理

## 🛡️ 安全考虑

### 1. 输入验证
- **SQL注入防护**：使用参数化查询
- **数据验证**：严格的数据格式验证
- **业务规则检查**：业务逻辑层面的数据校验

### 2. 权限控制
- **接口级权限**：基于角色的访问控制
- **数据级权限**：行级和字段级权限控制
- **操作审计**：完整的操作日志记录

### 3. 配置安全
- **配置加密**：敏感配置信息加密存储
- **配置校验**：防止恶意配置注入
- **访问控制**：配置管理的严格权限控制

## 🎉 预期效果

### 用户体验提升
- **学习成本降低90%**：用户无需了解技术细节
- **开发效率提升80%**：从需求到上线缩短至1-2天
- **维护成本降低70%**：统一架构，便于维护

### 技术价值
- **代码复用率95%**：核心代码完全复用
- **系统一致性100%**：所有业务遵循统一规范
- **扩展性优秀**：新业务类型零成本接入

### 业务价值
- **快速响应业务需求**：业务变更即时生效
- **降低技术门槛**：业务人员可独立创建系统
- **提高数据质量**：统一的数据规范和验证

---

## 🚀 下一步计划

1. **后端API开发**：实现通用控制器和服务层
2. **数据库设计**：完善表结构和索引策略  
3. **前端集成测试**：验证API与前端组件的集成效果
4. **性能压力测试**：确保系统在高并发下的稳定性
5. **文档和培训**：编写用户手册和操作指南

通过这套通用API接口设计，我们将真正实现"零代码开发"的目标，让业务人员能够快速、独立地创建和管理各种业务系统。 