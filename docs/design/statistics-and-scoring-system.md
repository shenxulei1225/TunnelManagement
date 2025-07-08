# 统计评分系统设计文档

## 1. 需求背景

系统需要一个完全可配置的评分体系，让用户能够自主定义评分维度、统计分类和计算规则。同时需要支持多维度的统计分析功能。关键是实现真正的用户自定义，而不是预先在代码中定义各种规则。

## 2. 核心概念

### 2.1 分类管理
- 用户可以自主创建和管理分类结构
- 支持多级分类的数据汇总
- 灵活配置汇总方式（求和、平均值、加权平均等）

### 2.2 数据源管理
- 用户可选择数据来源（数据表或视图）
- 系统自动展示可用的数据字段
- 支持数据预览功能

### 2.3 统计评分配置
- 用户自定义统计规则
- 用户自定义评分标准
- 支持多维度组合分析

## 3. 系统设计

### 3.1 数据结构

```sql
-- 用户定义的分类表
CREATE TABLE `user_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `parent_id` bigint COMMENT '父分类ID',
  `level` int NOT NULL COMMENT '层级',
  `path` varchar(255) NOT NULL COMMENT '分类路径',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `data_source` json COMMENT '数据来源配置',
  `rollup_config` json COMMENT '汇总配置',
  PRIMARY KEY (`id`)
) COMMENT='用户分类表';

-- 字段扩展属性表
CREATE TABLE `system_field_extension` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `field_def_id` bigint NOT NULL COMMENT '关联的字段定义ID',
  `business_type` varchar(32) NOT NULL COMMENT '业务类型：BASIC/STAT/SCORE',
  `stat_config` json COMMENT '统计配置',
  `score_config` json COMMENT '评分配置',
  `display_config` json COMMENT '展示配置',
  PRIMARY KEY (`id`),
  KEY `idx_field_def_id` (`field_def_id`)
) COMMENT='字段扩展属性表';

-- 字段分组表
CREATE TABLE `system_field_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '分组名称',
  `code` varchar(32) NOT NULL COMMENT '分组代码',
  `description` text COMMENT '分组描述',
  `order` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
  PRIMARY KEY (`id`)
) COMMENT='字段分组表';
```

### 3.2 用户界面设计

#### 3.2.1 分类管理界面
```typescript
interface CategoryManagementUI {
  // 用户操作界面
  operations: {
    // 创建分类
    createCategory: {
      name: string;          // 分类名称
      parentCategory?: string; // 可选择上级分类
      description?: string;   // 分类描述
    };
    
    // 设置汇总方式（通过下拉选择）
    setRollupMethod: {
      method: '求和' | '平均值' | '加权平均' | '自定义';
      weightField?: string;    // 如果选择加权平均，选择权重字段
    };
    
    // 拖拽排序
    dragToReorder: boolean;    // 支持拖拽调整顺序
    
    // 分类数据预览
    preview: {
      sampleData: any[];      // 显示样例数据
      rollupPreview: any;     // 显示汇总结果预览
    };
  };
}
```

#### 3.2.2 统计配置界面
```typescript
interface StatisticsConfigUI {
  step1: {
    // 选择数据范围
    categoryTree: {
      categories: Category[];
      searchBox: string;    // 搜索框
      expandAll: boolean;   // 展开/收起
    };
  };
  
  step2: {
    // 选择统计字段
    fieldSelector: {
      groups: FieldGroup[];
      searchBox: string;
      filter: {            // 字段筛选
        isStatistic?: boolean;
        dataType?: string[];
      };
    };
  };
  
  step3: {
    // 配置统计方式
    statisticsConfig: {
      method: string;
      dimensions: string[];
      metrics: string[];
      filters: {
        field: string;
        operator: string;
        value: any;
      }[];
    };
  };
}
```

#### 3.2.3 评分配置界面
```typescript
interface ScoringConfigUI {
  step1: {
    // 选择评分对象
    categorySelector: {
      selectedCategories: string[];
      preview: any[];
    };
  };
  
  step2: {
    // 设置评分规则
    scoringRules: {
      items: {
        field: string;
        weight: number;
        rules: {
          condition: string;
          score: number;
        }[];
      }[];
    };
  };
  
  step3: {
    // 配置展示方式
    displayConfig: {
      type: 'table' | 'chart';
      options: any;
    };
  };
}
```

## 4. 使用场景示例

### 4.1 创建设备分类

用户操作流程：
1. 点击"新建分类"
2. 输入名称"消防设备"
3. 选择上级分类"设备"
4. 选择数据来源"设备台账表"
5. 选择汇总方式"加权平均"
6. 选择权重字段"设备数量"

系统响应：
- 自动计算分类层级
- 生成分类路径
- 创建数据关联
- 显示数据预览

### 4.2 配置统计分析

用户操作流程：
1. 选择目标分类
2. 选择统计字段
3. 设置统计方式
4. 配置展示方式

系统响应：
- 动态生成统计查询
- 执行数据计算
- 展示统计结果

### 4.3 设置评分规则

用户操作流程：
1. 选择评分对象
2. 设置评分维度
3. 配置计算规则
4. 设置权重

系统响应：
- 保存评分配置
- 计算评分结果
- 展示评分详情

## 5. 设计优势

1. 完全可配置：
   - 用户自主创建分类
   - 灵活配置统计规则
   - 自定义评分标准

2. 操作便捷：
   - 可视化配置界面
   - 拖拽式操作
   - 实时数据预览

3. 数据完整：
   - 支持多级汇总
   - 保留计算过程
   - 数据可追溯

## 6. 后续优化方向

1. 用户体验优化：
   - 简化配置流程
   - 增加操作引导
   - 优化交互设计

2. 功能增强：
   - 添加配置模板
   - 支持批量操作
   - 增加数据导出

3. 性能优化：
   - 优化汇总计算
   - 添加结果缓存
   - 支持增量更新

## 7. 注意事项

1. 分类管理：
   - 分类变更时需要处理数据迁移
   - 注意分类层级深度限制
   - 考虑分类合并拆分场景

2. 数据处理：
   - 处理数据权限控制
   - 注意大数据量性能
   - 考虑数据一致性

3. 用户界面：
   - 提供充分的操作提示
   - 添加常见问题解答
   - 支持操作回退功能
