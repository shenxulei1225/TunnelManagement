# 业务建模工作台字段需求分析

## 📋 **前端接口调整总结**

### **1. API接口变更**
- ✅ **从动态模型API切换到Domain API**
  - 原接口：`/dynamic/model/*` 
  - 新接口：`/system/domain/*`
- ✅ **创建Domain API接口文件**：`src/api/system/domain.ts`
- ✅ **更新业务建模工作台**：使用`DomainApi`替代原有的`listModel`、`addModel`、`updateModel`、`delModel`

### **2. 字段映射调整**

#### **Domain模型字段结构**
```typescript
interface DomainVO {
  id?: number           // 主键ID
  name: string          // 领域名称 ✅ 必填
  code: string          // 领域编码 ✅ 必填  
  parentId: number      // 父领域ID（支持层级结构）
  description?: string  // 领域描述
  type?: string         // 领域类型（business/system等）✅ 必填
  status: number        // 状态（0禁用/1启用）✅ 必填
  sort?: number         // 排序
  remark?: string       // 备注
  readonly?: number     // 只读标识（0普通/1只读）
  createTime?: string   // 创建时间
  updateTime?: string   // 更新时间
}
```

#### **字段映射关系**
| 前端表单字段 | Domain字段 | 说明 | 是否必填 |
|-------------|-----------|------|---------|
| `name` | `name` | 模型名称 | ✅ 是 |
| `code` | `code` | 模型编码 | ✅ 是 |
| `type` | `type` | 领域类型 | ✅ 是 |
| `description` | `description` | 模型描述 | ❌ 否 |
| `status` | `status` | 模型状态 | ✅ 是 |
| `modelType` | `readonly` | 模型类型映射为只读标识 | ❌ 否 |
| `sort` | `sort` | 排序 | ❌ 否 |
| `remark` | `remark` | 备注 | ❌ 否 |
| - | `parentId` | 父领域ID（固定为0，顶级） | ✅ 是 |

### **3. 建模核心字段需求**

#### **基础模型信息**
1. **模型名称** (`name`) - 必填
   - 用户友好的显示名称
   - 例如："用户管理"、"订单系统"

2. **模型编码** (`code`) - 必填
   - 系统内唯一标识
   - 建议格式：字母、数字、下划线
   - 例如："user_management"、"order_system"

3. **领域类型** (`type`) - 必填
   - 用于分类管理
   - 常用值：`business`（业务）、`system`（系统）
   - 默认：`business`

4. **模型状态** (`status`) - 必填
   - 0：禁用
   - 1：启用
   - 默认：1

#### **扩展配置信息**
5. **模型描述** (`description`) - 可选
   - 详细的业务说明
   - 帮助理解模型用途

6. **只读标识** (`readonly`) - 可选
   - 0：普通模型（可编辑）
   - 1：系统模型（只读）
   - 映射自前端的`modelType`字段

7. **排序** (`sort`) - 可选
   - 控制显示顺序
   - 默认：0

8. **备注** (`remark`) - 可选
   - 额外的说明信息

#### **领域建模扩展字段**
9. **字段配置** (`fields`) - 业务字段
   - 字段名称、类型、是否必填等
   - 支持动态添加/删除字段

10. **页面配置** (`pageConfig`) - 页面设置
    - 列表页面、详情页面开关
    - 编辑、删除功能权限

11. **权限配置** (`permissions`) - 访问控制
    - 可见角色（admin/user/guest）
    - 操作权限（create/read/update/delete）

#### **建模过程数据**
12. **业务场景** - 建模选择
    - 组织架构管理
    - 电商订单系统  
    - 设备监控系统

13. **需求分析** - 建模内容
    - 业务需求描述
    - 核心概念识别（实体/值对象/聚合根/服务）

14. **聚合设计** - 领域模型
    - 聚合根、实体列表、值对象列表

15. **业务规则** - 规则定义
    - 规则名称、规则描述

### **4. 后端接口需求**

#### **必需的Domain API接口**
- ✅ `POST /system/domain/create` - 创建领域模型
- ✅ `PUT /system/domain/update` - 更新领域模型  
- ✅ `DELETE /system/domain/delete` - 删除领域模型
- ✅ `GET /system/domain/get` - 获取单个领域模型
- ✅ `GET /system/domain/page` - 获取领域模型分页列表

#### **数据验证要求**
- **name**: 非空，长度≤100字符
- **code**: 非空，长度≤50字符，唯一性
- **type**: 非空，长度≤50字符
- **status**: 必须为0或1
- **parentId**: 非空，默认为0（顶级）

#### **菜单配置信息**
根据[[memory:3096140]]，每生成一个页面需要提供菜单创建的配置参数：

```json
{
  "menuInfo": {
    "name": "业务建模工作台",
    "path": "/domain-model/modeling", 
    "component": "/domain-model/workspace",
    "icon": "ep:magic-stick",
    "sort": 20,
    "visible": true,
    "status": 1,
    "type": 2,
    "parentId": "领域建模菜单的ID",
    "keepAlive": true
  }
}
```

### **5. 下一步工作**

1. **后端验证**：确认Domain API接口是否正常工作
2. **字段扩展**：根据业务需求添加更多Domain字段
3. **权限集成**：实现细粒度的权限控制
4. **数据持久化**：保存建模过程中的聚合设计和业务规则
5. **接口优化**：根据前端使用情况优化API响应结构

## 🎯 **核心改进**

- ✅ **统一数据源**：使用Domain作为业务模型的统一管理
- ✅ **标准化接口**：遵循系统统一的API规范
- ✅ **层级支持**：支持领域模型的层级结构管理  
- ✅ **类型分类**：通过type字段支持不同类型的领域模型
- ✅ **权限控制**：通过readonly字段控制模型的可编辑性 