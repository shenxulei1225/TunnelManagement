# 业务建模菜单配置指南

## 概述

业务建模模块现在包含了从动态业务迁移过来的智能推荐功能。本文档提供了完整的菜单管理配置参数。

## 菜单结构

```
业务建模 (/business-modeling 或 /system/business-modeling)
├── 业务建模工作台 (默认页面)
├── 页面生成器
├── 页面配置 (隐藏)
├── 智能推荐 (新增)
└── 推荐配置 (隐藏，新增)
```

## 手工配置参数

### 主菜单：业务建模
- **菜单名称**：业务建模
- **权限标识**：business:modeling:view
- **菜单类型**：目录(1)
- **排序**：900
- **父菜单ID**：0 (顶级菜单) 或 1 (系统管理)
- **路由地址**：/business-modeling
- **菜单图标**：ep:setting
- **组件路径**：Layout
- **组件名称**：(空)
- **状态**：启用(0)
- **显示状态**：显示(1)
- **缓存**：缓存(1)
- **总是显示**：是(1)

### 子菜单1：业务建模工作台
- **菜单名称**：业务建模工作台
- **权限标识**：business:modeling:workspace
- **菜单类型**：菜单(2)
- **排序**：1
- **父菜单ID**：{业务建模主菜单ID}
- **路由地址**：(空)
- **菜单图标**：ep:setting
- **组件路径**：business-module/BusinessModeling
- **组件名称**：BusinessModelingHome
- **状态**：启用(0)
- **显示状态**：显示(1)
- **缓存**：缓存(1)
- **总是显示**：否(0)

### 子菜单2：页面生成器
- **菜单名称**：页面生成器
- **权限标识**：business:modeling:generator
- **菜单类型**：菜单(2)
- **排序**：2
- **父菜单ID**：{业务建模主菜单ID}
- **路由地址**：page-generator
- **菜单图标**：ep:magic-stick
- **组件路径**：business-module/page-generator/index
- **组件名称**：PageGenerator
- **状态**：启用(0)
- **显示状态**：显示(1)
- **缓存**：缓存(1)
- **总是显示**：否(0)

### 子菜单3：页面配置（隐藏）
- **菜单名称**：页面配置
- **权限标识**：business:modeling:configure
- **菜单类型**：菜单(2)
- **排序**：3
- **父菜单ID**：{业务建模主菜单ID}
- **路由地址**：page-generator/configure
- **菜单图标**：ep:setting
- **组件路径**：business-module/page-generator/configure
- **组件名称**：PageGeneratorConfigure
- **状态**：启用(0)
- **显示状态**：隐藏(0)
- **缓存**：缓存(1)
- **总是显示**：否(0)

### 子菜单4：智能推荐（新增）
- **菜单名称**：智能推荐
- **权限标识**：business:modeling:recommendation
- **菜单类型**：菜单(2)
- **排序**：4
- **父菜单ID**：{业务建模主菜单ID}
- **路由地址**：recommendation
- **菜单图标**：ep:star
- **组件路径**：dynamic/recommendation/index
- **组件名称**：BusinessRecommendation
- **状态**：启用(0)
- **显示状态**：显示(1)
- **缓存**：缓存(1)
- **总是显示**：否(0)

### 子菜单5：推荐配置（隐藏，新增）
- **菜单名称**：推荐配置
- **权限标识**：business:modeling:recommendation:configure
- **菜单类型**：菜单(2)
- **排序**：5
- **父菜单ID**：{业务建模主菜单ID}
- **路由地址**：recommendation/configure
- **菜单图标**：ep:setting
- **组件路径**：dynamic/recommendation/configure
- **组件名称**：BusinessRecommendationConfigure
- **状态**：启用(0)
- **显示状态**：隐藏(0)
- **缓存**：缓存(1)
- **总是显示**：否(0)

## 执行迁移

### 方法1：执行SQL脚本（推荐）
```bash
# 执行菜单迁移脚本
mysql -u your_username -p your_database < sql/mysql/move_recommendation_to_business_modeling.sql
```

### 方法2：手动在菜单管理中操作
1. 登录系统管理后台
2. 进入【系统管理】→【菜单管理】
3. 找到【动态业务】下的【智能推荐】菜单
4. 编辑菜单，修改父菜单为【业务建模】
5. 同样处理【推荐配置】菜单

## 访问路径

迁移完成后，智能推荐的访问路径：
- **智能推荐**：`http://localhost/system/business-modeling/recommendation`
- **推荐配置**：`http://localhost/system/business-modeling/recommendation/configure`

如果业务建模是顶级菜单，则路径为：
- **智能推荐**：`http://localhost/business-modeling/recommendation`
- **推荐配置**：`http://localhost/business-modeling/recommendation/configure`

## 权限配置

确保相关角色具有以下权限：
- `business:modeling:view` - 访问业务建模模块
- `business:modeling:recommendation` - 访问智能推荐
- `business:modeling:recommendation:configure` - 配置智能推荐

## 注意事项

1. **组件路径不变**：智能推荐组件仍然位于 `dynamic/recommendation/` 目录，只是在菜单中重新组织
2. **权限更新**：权限标识从 `dynamic:recommendation` 改为 `business:modeling:recommendation`
3. **路由名称**：为避免冲突，路由名称从 `DynamicRecommendation` 改为 `BusinessRecommendation`
4. **缓存清理**：迁移后可能需要清理浏览器缓存和后端权限缓存 