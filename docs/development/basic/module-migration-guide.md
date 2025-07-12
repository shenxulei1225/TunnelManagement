# 模块迁移指南

本文档描述了从yudao框架迁移到cheers框架的标准流程。

## 目录结构对照

```
源模块结构：
yudao-framework/
  yudao-spring-boot-starter-{original-module}/
    src/main/
      java/cn/iocoder/yudao/framework/{submodule}/
      resources/META-INF/

目标模块结构：
cheers-framework/
  cheers-{module}/
    src/main/
      java/com/cheers/arch/framework/{submodule}/
      resources/META-INF/
```

## 迁移步骤

### 1. 创建目标模块目录并复制源代码

```bash
mkdir -p cheers-framework/{module}/src/main/java/com/cheers/arch/framework/{submodule} && \
cp -r yudao-framework/yudao-spring-boot-starter-{original-module}/src/main/java/cn/iocoder/yudao/framework/{submodule}/* \
cheers-framework/{module}/src/main/java/com/cheers/arch/framework/{submodule}/
```

### 2. 复制META-INF配置

```bash
mkdir -p cheers-framework/{module}/src/main/resources/META-INF && \
cp -r yudao-framework/yudao-spring-boot-starter-{original-module}/src/main/resources/META-INF/* \
cheers-framework/{module}/src/main/resources/META-INF/
```

### 3. 替换Java文件包名

```bash
find cheers-framework/{module}/src/main/java -name "*.java" \
-exec sed -i '' 's/cn.iocoder.yudao.framework/com.cheers.arch.framework/g' {} +
```

### 4. 替换类名前缀

```bash
# 重命名Yudao开头的文件
find cheers-framework/{module}/src/main/java -name "Yudao*.java" \
-exec sh -c 'mv "$1" "${1/Yudao/Cheers}"' _ {} \;

# 替换文件内容中的Yudao为Cheers
find cheers-framework/{module}/src/main/java -type f -name "*.java" \
-exec sed -i '' 's/Yudao/Cheers/g' {} +
```

### 5. 替换配置文件中的类名

```bash
find cheers-framework/{module}/src/main/resources -type f \
-exec sed -i '' 's/Yudao/Cheers/g' {} +
```

## pom.xml处理

在迁移模块时，需要对pom.xml进行以下修改：

1. 修改项目坐标
   ```xml
   <groupId>com.cheers.arch</groupId>
   <artifactId>cheers-{module}</artifactId>
   ```

2. 修改parent配置
   ```xml
   <parent>
       <groupId>com.cheers.arch</groupId>
       <artifactId>cheers-framework</artifactId>
       <version>${revision}</version>
   </parent>
   ```

3. 替换依赖
   - 将所有yudao相关的依赖更改为对应的cheers模块
   - 保持版本号与cheers-dependencies中定义的一致

## 注意事项

1. 确保目标目录不存在或为空，避免文件覆盖问题
2. 检查替换后的包名和类名是否正确
3. 验证META-INF配置文件中的类引用是否已更新
4. 确保pom.xml中的依赖关系正确且版本一致
5. 建议在替换完成后进行编译测试，确保代码可以正常编译

## 示例

以tenant模块为例：

```bash
# 1. 创建目录和复制源码
mkdir -p cheers-framework/cheers-tenant/src/main/java/com/cheers/arch/framework/tenant && \
cp -r yudao-framework/yudao-spring-boot-starter-biz-tenant/src/main/java/cn/iocoder/yudao/framework/tenant/* \
cheers-framework/cheers-tenant/src/main/java/com/cheers/arch/framework/tenant/

# 2. 复制META-INF
mkdir -p cheers-framework/cheers-tenant/src/main/resources/META-INF && \
cp -r yudao-framework/yudao-spring-boot-starter-biz-tenant/src/main/resources/META-INF/* \
cheers-framework/cheers-tenant/src/main/resources/META-INF/

# 3. 替换包名
find cheers-framework/cheers-tenant/src/main/java -name "*.java" \
-exec sed -i '' 's/cn.iocoder.yudao.framework/com.cheers.arch.framework/g' {} +

# 4. 替换类名
find cheers-framework/cheers-tenant/src/main/java -name "Yudao*.java" \
-exec sh -c 'mv "$1" "${1/Yudao/Cheers}"' _ {} \; && \
find cheers-framework/cheers-tenant/src/main/java -type f -name "*.java" \
-exec sed -i '' 's/Yudao/Cheers/g' {} +

# 5. 更新配置
find cheers-framework/cheers-tenant/src/main/resources -type f \
-exec sed -i '' 's/Yudao/Cheers/g' {} +
``` 