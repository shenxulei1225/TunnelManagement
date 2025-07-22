# 弹窗提示内容定义参考

## 📍 主要定义位置

### 1. **字段操作相关弹窗** 
**文件位置**：`tunnel-management-ui/src/utils/fieldActionConfirms.ts`

这是**字段管理模块**的核心弹窗定义文件，包含所有字段相关操作的确认弹窗。

#### **删除字段确认弹窗**
```typescript
static async confirmDeleteField(fieldName: string): Promise<void> {
  await ElMessageBox.confirm(
    `确定要彻底删除字段 "${fieldName}" 吗？

此操作将：
• 完全删除该字段定义
• 删除所有相关的分类关联
• 删除所有使用该字段的数据

⚠️ 此操作不可恢复！`,
    '删除字段确认',
    {
      confirmButtonText: '删除字段',
      cancelButtonText: '取消',
      type: 'error'
    }
  )
}
```

#### **解除字段关联确认弹窗**
```typescript
static async confirmUnlinkFields(fieldNames: string[], categoryName?: string): Promise<void> {
  await ElMessageBox.confirm(
    `确定要解除 ${fieldText} 与 ${categoryText} 的关联吗？

此操作将：
• 解除字段与分类的关联关系
• 字段定义本身不会被删除
• 可以重新关联到其他分类

✅ 此操作可以撤销`,
    '解除关联确认',
    {
      confirmButtonText: '解除关联',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
}
```

#### **批量删除字段确认弹窗**
```typescript
static async confirmBatchDeleteFields(count: number): Promise<void> {
  await ElMessageBox.confirm(
    `确定要彻底删除选中的 ${count} 个字段吗？

此操作将：
• 完全删除所有选中的字段定义
• 删除所有相关的分类关联
• 删除所有使用这些字段的数据

⚠️ 此操作不可恢复！`,
    '批量删除字段确认',
    {
      confirmButtonText: `删除 ${count} 个字段`,
      cancelButtonText: '取消',
      type: 'error'
    }
  )
}
```

#### **移动字段分组确认弹窗**
```typescript
static async confirmMoveField(fieldName: string, fromGroup: string, toGroup: string): Promise<void> {
  await ElMessageBox.confirm(
    `确定要将字段 "${fieldName}" 从 "${fromGroup}" 移动到 "${toGroup}" 吗？

此操作将：
• 解除与原分组的关联
• 建立与新分组的关联
• 保留字段的所有属性设置`,
    '移动字段确认',
    {
      confirmButtonText: '移动字段',
      cancelButtonText: '取消',
      type: 'info'
    }
  )
}
```

### 2. **字段分类管理弹窗**
**文件位置**：`tunnel-management-ui/src/views/system/field/FieldCategory/index.vue`

#### **删除分类确认弹窗**
```typescript
// 第414行
const handleDelete = async (row: FieldCategoryVO) => {
  await ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' })
  await deleteCategory(row.id!)
  ElMessage.success('删除成功')
  fetchTree()
}
```

### 3. **通用树组件弹窗**
**文件位置**：`tunnel-management-ui/src/components/extendTree/index.vue`

#### **删除节点确认弹窗**
```typescript
// 第282行
await ElMessageBox.confirm(
  // 具体内容需要查看完整实现
)
```

## 🎯 操作结果提示消息

### **成功/失败消息定义**
**文件位置**：`tunnel-management-ui/src/utils/fieldActionConfirms.ts`

```typescript
export class FieldActionMessages {
  
  // 删除操作消息
  static deleteSuccess(fieldName: string) {
    return `字段 "${fieldName}" 删除成功`
  }

  static deleteError(fieldName: string) {
    return `字段 "${fieldName}" 删除失败`
  }

  // 解除关联消息
  static unlinkSuccess(count: number = 1) {
    return count === 1 ? '关联解除成功' : `${count} 个字段关联解除成功`
  }

  static unlinkError(count: number = 1) {
    return count === 1 ? '关联解除失败' : `${count} 个字段关联解除失败`
  }

  // 批量删除消息
  static batchDeleteSuccess(count: number) {
    return `${count} 个字段删除成功`
  }

  static batchDeleteError(count: number) {
    return `${count} 个字段删除失败`
  }

  // 移动操作消息
  static moveSuccess(fieldName: string, toGroup: string) {
    return `字段 "${fieldName}" 已移动到 "${toGroup}"`
  }

  static moveError(fieldName: string) {
    return `字段 "${fieldName}" 移动失败`
  }
}
```

## 🔧 后端API返回的提示消息

### **字段关联操作消息**
**文件位置**：`cheers-module-system/src/main/java/com/cheers/arch/module/system/controller/admin/field/FieldController.java`

```java
// 关联字段到分级组
@PutMapping("/link-hierarchy-group")
public CommonResult<String> linkFieldHierarchyGroup(@Valid @RequestBody FieldUpdateHierarchyReqVO reqVO) {
    if (currentGroupIds.contains(reqVO.getHierarchyGroupId())) {
        return CommonResult.success("字段已在此分组中");  // 重复关联提示
    }
    
    fieldHierarchyRelService.createFieldHierarchyRel(reqVO.getFieldId(), reqVO.getHierarchyGroupId());
    return CommonResult.success("关联成功");  // 成功关联提示
}

// 更新字段分级组
@PutMapping("/update-hierarchy-group") 
public CommonResult<String> updateFieldHierarchy(@Valid @RequestBody FieldUpdateHierarchyReqVO reqVO) {
    if (reqVO.getHierarchyGroupId() == null) {
        return CommonResult.success("分组关联已移除");
    } else if (currentGroupIds.contains(reqVO.getHierarchyGroupId())) {
        return CommonResult.success("字段已在此分组中");
    } else if (currentGroupIds.size() <= 1) {
        return CommonResult.success("分组更新成功");
    } else {
        return CommonResult.success("分组关联已添加");
    }
}
```

## 📝 弹窗类型和样式

### **弹窗类型分类**
- **`type: 'error'`** - 危险操作（删除字段）- 红色
- **`type: 'warning'`** - 警告操作（解除关联）- 橙色  
- **`type: 'info'`** - 信息操作（移动字段）- 蓝色
- **`type: 'success'`** - 成功操作 - 绿色

### **自定义样式类**
- `field-delete-confirm-dialog` - 删除字段弹窗样式
- `field-unlink-confirm-dialog` - 解除关联弹窗样式
- `field-batch-delete-confirm-dialog` - 批量删除弹窗样式
- `field-move-confirm-dialog` - 移动字段弹窗样式

## 🛠️ 如何修改弹窗内容

### **1. 修改字段操作弹窗**
编辑文件：`tunnel-management-ui/src/utils/fieldActionConfirms.ts`

```typescript
// 修改删除确认弹窗的内容
static async confirmDeleteField(fieldName: string): Promise<void> {
  await ElMessageBox.confirm(
    `这里是新的提示内容...`,  // 🔧 修改这里
    '新的标题',                // 🔧 修改标题
    {
      confirmButtonText: '新的按钮文案',  // 🔧 修改按钮文案
      cancelButtonText: '取消',
      type: 'error'
    }
  )
}
```

### **2. 修改API返回消息**
编辑文件：`cheers-module-system/src/main/java/com/cheers/arch/module/system/controller/admin/field/FieldController.java`

```java
// 修改后端返回的提示消息
return CommonResult.success("新的提示消息");  // 🔧 修改这里
```

### **3. 添加新的弹窗类型**
在 `fieldActionConfirms.ts` 中添加新方法：

```typescript
static async confirmNewAction(param: string): Promise<void> {
  await ElMessageBox.confirm(
    '新操作的确认内容...',
    '新操作确认',
    {
      confirmButtonText: '确认操作',
      cancelButtonText: '取消',
      type: 'info'
    }
  )
}
```

## 📍 使用位置汇总

| 弹窗类型 | 定义位置 | 使用位置 | 触发条件 |
|---------|----------|----------|----------|
| 删除字段 | `fieldActionConfirms.ts` | `useFieldManagement.ts` | 点击删除字段按钮 |
| 解除关联 | `fieldActionConfirms.ts` | `FieldCategory/index.vue` | 点击解除关联按钮 |
| 删除分类 | `FieldCategory/index.vue` | 同文件 | 点击删除分类按钮 |
| 移动字段 | `fieldActionConfirms.ts` | `FieldManagement.vue` | 拖拽字段到新分组 |
| 批量删除 | `fieldActionConfirms.ts` | 暂未使用 | 批量选择删除 |

## 💡 最佳实践

1. **集中管理**：字段相关的弹窗统一在 `fieldActionConfirms.ts` 中定义
2. **类型区分**：根据操作危险程度选择合适的 `type`
3. **内容详细**：提供操作影响的详细说明
4. **可撤销性**：明确标注操作是否可撤销
5. **一致性**：保持相同类型操作的弹窗风格一致 