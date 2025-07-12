# Maven 依赖管理和编译规则

## 1. 项目结构

项目采用 Maven 多模块结构，主要包含以下模块：

```
tunnel-management (根项目)
├── cheers-dependencies (依赖版本管理)
├── cheers-framework (框架模块)
│   ├── cheers-common (公共模块)
│   ├── cheers-web (Web模块)
│   ├── cheers-redis (Redis模块)
│   ├── cheers-security-core (安全模块)
│   └── cheers-translate (翻译模块)
└── 其他业务模块
```

## 2. 依赖管理规则

### 2.1 版本统一管理

1. 使用 `${revision}` 变量统一管理项目版本：
   - 在根 pom.xml 中定义项目整体版本
   - 在 cheers-dependencies 中定义 BOM 版本
   ```xml
   <properties>
       <revision>1.0.0-snapshot</revision>
   </properties>
   ```

2. 使用 flatten-maven-plugin 处理版本变量：
   ```xml
   <plugin>
       <groupId>org.codehaus.mojo</groupId>
       <artifactId>flatten-maven-plugin</artifactId>
       <configuration>
           <flattenMode>bom</flattenMode>
           <updatePomFile>true</updatePomFile>
       </configuration>
       <executions>
           <execution>
               <id>flatten</id>
               <phase>process-resources</phase>
               <goals>
                   <goal>flatten</goal>
               </goals>
           </execution>
           <execution>
               <id>flatten.clean</id>
               <phase>clean</phase>
               <goals>
                   <goal>clean</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```

### 2.2 依赖版本管理

1. cheers-dependencies 作为 BOM（Bill of Materials）：
   - 统一管理所有第三方依赖版本
   - 不包含具体依赖，只包含版本定义
   - 独立维护，不依赖父 POM

2. 根 pom.xml 通过 import scope 引入 BOM：
   ```xml
   <dependencyManagement>
       <dependencies>
           <dependency>
               <groupId>com.cheers.arch</groupId>
               <artifactId>cheers-dependencies</artifactId>
               <version>${revision}</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
       </dependencies>
   </dependencyManagement>
   ```

3. 子模块依赖规则：
   - 继承根 pom.xml 的版本管理
   - 声明依赖时不指定版本号
   - 如需覆盖版本，在模块的 dependencyManagement 中定义

## 3. 编译顺序

### 3.1 完整项目编译

直接在根目录执行：
```bash
mvn clean install -DskipTests
```

Maven 会自动按照依赖顺序编译：
1. cheers-dependencies
2. 根 pom
3. framework 及其子模块
4. 其他业务模块

### 3.2 单独编译 framework

需要按照以下顺序编译：

1. 先编译安装 cheers-dependencies：
```bash
cd cheers-dependencies
mvn clean install
```

2. 安装根 pom（不编译子模块）：
```bash
cd ..
mvn clean install -N
```

3. 编译 framework：
```bash
cd cheers-framework
mvn clean install -DskipTests
```

### 3.3 编译参数说明

- `-N`：不递归编译子模块
- `-DskipTests`：跳过测试
- `clean`：清理之前的编译结果
- `install`：安装到本地仓库

## 4. 最佳实践

1. 版本管理：
   - 所有版本号统一在 cheers-dependencies 中定义
   - 避免在子模块中直接指定版本号
   - 使用 `${revision}` 统一管理项目版本

2. 依赖引入：
   - 优先使用 cheers-dependencies 中定义的版本
   - 必要时在子模块的 dependencyManagement 中覆盖版本
   - 显式声明所有直接依赖，不依赖传递依赖

3. 编译构建：
   - 优先使用根目录的完整编译
   - 单独编译模块时注意依赖顺序
   - 善用 Maven 命令参数控制编译行为 