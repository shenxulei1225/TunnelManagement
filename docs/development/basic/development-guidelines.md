# 开发指导指南

本文档作为开发指导的入口，提供项目整体开发规范概述，并引导您查看具体的前后端开发指南。

## 一、项目概述

### 1. 技术栈
- 后端：Spring Boot + MyBatis-Plus + MySQL
- 前端：Vue3 + TypeScript + Element Plus
- 文档：Swagger + Markdown

### 2. 项目结构
```
TunnelManagement/
├── docs/                    # 项目文档
│   └── development/         # 开发指南
│       └── basic/          
│           ├── development-guidelines.md  # 本文档
│           ├── backend-guidelines.md      # 后端开发指南
│           └── frontend-guidelines.md     # 前端开发指南
├── cheers-module-*/         # 后端业务模块
├── tunnel-management-ui/    # 前端项目
└── script/                  # 部署脚本
```

## 二、开发指南

### 1. [后端开发指南](./backend-guidelines.md)
后端开发指南包含以下主要内容：
- Java项目分层架构
- 代码规范和命名规范
- 业务校验规范
- 错误码规范
- 开发流程指南
- 常用工具说明

### 2. [前端开发指南](./frontend-guidelines.md)
前端开发指南包含以下主要内容：
- Vue3项目结构规范
- 组件开发规范
- 样式开发规范
- API调用规范
- TypeScript类型规范
- 开发流程指南

## 三、开发流程概述

### 1. 需求分析
- 理解业务需求
- 确定技术方案
- 评估开发周期
- 识别技术难点

### 2. 开发阶段
- 遵循对应技术栈的开发指南
- 按照规范编写代码
- 编写必要的测试
- 完善相关文档

### 3. 提交规范
- 遵循Git Flow工作流
- 提交信息需要清晰描述改动
- 重要改动需要添加注释
- 提交前进行代码审查

### 4. 部署发布
- 遵循CI/CD流程
- 按环境分步骤发布
- 进行必要的测试
- 记录部署日志

## 四、注意事项

### 1. 代码质量
- 遵循开发规范
- 保持代码简洁
- 注重代码复用
- 及时重构优化

### 2. 安全规范
- 遵循安全开发规范
- 注意敏感数据处理
- 防范常见安全漏洞
- 定期安全审计

### 3. 文档维护
- 及时更新文档
- 保持文档准确性
- 注重文档可读性
- 添加必要的示例

## 五、相关资源

### 1. 项目文档
- [项目结构说明](../structure/project-structure.md)
- [完整工作流指南](../development/complete-workflow-guide.md)
- [通用问题指南](../features/common-issues-guide.md)

### 2. 外部资源
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Vue3官方文档](https://v3.vuejs.org/)
- [Element Plus组件库](https://element-plus.org/)
- [TypeScript官方文档](https://www.typescriptlang.org/) 