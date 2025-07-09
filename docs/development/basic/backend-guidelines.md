# 后端开发指导指南

## 一、项目结构规范

### 1. 分层架构
```
src/main/java/com/cheers/system/
├── controller        # 控制层：处理请求响应
│   └── admin         # 后台管理接口
│       └── vo        # 视图对象：请求/响应对象
├── service          # 业务层：处理业务逻辑
│   └── impl         # 接口实现
├── dal              # 数据访问层
│   ├── dataobject   # 数据对象：与数据库表映射
│   └── mysql        # 数据库访问：Mapper接口
├── convert          # 对象转换层：DO/VO转换
└── enums            # 枚举定义：常量和状态
```

### 2. 命名规范

#### 2.1 文件命名规范
| 类型 | 命名规则 | 示例 |
|------|----------|------|
| Controller | 模块名Controller | UserController |
| Service接口 | 模块名Service | UserService |
| Service实现 | 模块名ServiceImpl | UserServiceImpl |
| Mapper接口 | 模块名Mapper | UserMapper |
| 数据对象 | 模块名DO | UserDO |
| 请求对象 | 模块名XxxReqVO | UserCreateReqVO |
| 响应对象 | 模块名RespVO | UserRespVO |
| 转换类 | 模块名Convert | UserConvert |

#### 2.2 方法命名规范
| 操作类型 | 命名规则 | 示例 |
|----------|----------|------|
| 创建 | create模块名 | createUser |
| 更新 | update模块名 | updateUser |
| 删除 | delete模块名 | deleteUser |
| 获取单个 | get模块名 | getUser |
| 获取列表 | get模块名List | getUserList |
| 获取分页 | get模块名Page | getUserPage |

## 二、编码规范

### 1. Controller层规范

```java
@Tag(name = "管理后台 - 用户")
@RestController
@RequestMapping("/system/user")
@Validated
public class UserController {
    
    @Resource
    private UserService userService;

    @PostMapping("/create")
    @Operation(summary = "创建用户")
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public Long createUser(@Valid @RequestBody UserCreateReqVO createReqVO) {
        return userService.createUser(UserConvert.INSTANCE.convert(createReqVO));
    }
}
```

关键点：
- 使用@Tag标注接口分类
- 使用@Operation标注接口说明
- 使用@PreAuthorize进行权限控制
- 使用@Valid进行参数校验
- 统一的URL命名规范

### 2. Service层规范

```java
@Service
@Validated
public class UserServiceImpl implements UserService {
    
    @Resource
    private UserMapper userMapper;
    
    @Override
    public Long createUser(UserDO user) {
        // 1. 业务校验
        validateUserNameUnique(user.getUsername(), null);
        
        // 2. 设置默认值
        initUserDefaults(user);
        
        // 3. 插入数据
        userMapper.insert(user);
        return user.getId();
    }
    
    private void validateUserNameUnique(String username, Long id) {
        UserDO user = userMapper.selectOne(new LambdaQueryWrapperX<UserDO>()
                .eq(UserDO::getUsername, username)
                .neIfPresent(UserDO::getId, id));
        if (user != null) {
            throw new ServiceException(USER_NAME_DUPLICATE);
        }
    }
}
```

关键点：
- 实现接口定义的方法
- 添加必要的业务校验
- 使用LambdaQueryWrapperX进行查询
- 清晰的方法职责划分

### 3. Mapper层规范

```java
@Mapper
public interface UserMapper extends BaseMapperX<UserDO> {
    // 继承BaseMapperX获取增强功能
    // 只需要定义特殊的查询方法
}
```

## 三、业务校验规范

### 1. 校验时机
| 操作类型 | 校验项 |
|----------|--------|
| 创建操作 | - 必填字段非空校验<br>- 数据唯一性校验<br>- 关联数据存在性校验 |
| 更新操作 | - 记录存在性校验<br>- 必填字段非空校验<br>- 数据唯一性校验(排除自身)<br>- 关联数据存在性校验 |
| 删除操作 | - 记录存在性校验<br>- 关联数据检查 |

### 2. 校验方法示例

```java
// 1. 存在性校验
private void validateUserExists(Long id) {
    if (userMapper.selectById(id) == null) {
        throw new ServiceException(USER_NOT_EXISTS);
    }
}

// 2. 唯一性校验
private void validateUserNameUnique(String username, Long id) {
    UserDO user = userMapper.selectOne(new LambdaQueryWrapperX<UserDO>()
            .eq(UserDO::getUsername, username)
            .neIfPresent(UserDO::getId, id));
    if (user != null) {
        throw new ServiceException(USER_NAME_DUPLICATE);
    }
}

// 3. 关联性校验
private void validateDeptExists(Long deptId) {
    if (deptMapper.selectById(deptId) == null) {
        throw new ServiceException(DEPT_NOT_EXISTS);
    }
}
```

## 四、错误码规范

### 1. 错误码设计
- 按模块分类，每个模块预留1000个错误码
- 错误码格式：模块编码(3位) + 子模块编码(3位) + 序号(3位)

```java
public interface ErrorCodeConstants {
    // ========== 用户模块 1002000000 ==========
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1002000001, "用户不存在");
    ErrorCode USER_NAME_DUPLICATE = new ErrorCode(1002000002, "用户名已存在");
    ErrorCode USER_MOBILE_DUPLICATE = new ErrorCode(1002000003, "手机号已存在");
}
```

### 2. 错误提示规范
- 使用中文，简洁清晰
- 说明具体错误原因
- 适当给出处理建议

## 五、开发流程指南

### 1. 功能开发流程
1. 需求分析
   - 理解业务需求
   - 设计数据结构
   - 确定接口规范
   - 识别业务校验点

2. 编码实现
   - 创建数据库表
   - 生成基础代码
   - 完善业务逻辑
   - 添加业务校验
   - 补充错误码定义
   - 完善接口文档

3. 测试验证
   - 编写单元测试
   - 接口功能测试
   - 业务流程测试
   - 异常场景测试

4. 代码优化
   - 检查代码规范
   - 优化执行性能
   - 完善错误处理
   - 补充注释文档

### 2. 开发注意事项

#### 2.1 安全规范
- 所有接口必须添加权限控制
- 敏感数据必须加密处理
- 防止SQL注入
- 防止XSS攻击

#### 2.2 性能优化
- 合理使用索引
- 避免大事务
- 使用缓存
- 分页查询限制

#### 2.3 代码质量
- 遵循DRY原则
- 保持代码简洁
- 及时处理TODO
- 删除无用代码

## 六、常用工具说明

### 1. MyBatis增强工具
```java
// 条件构造示例
new LambdaQueryWrapperX<UserDO>()
    .likeIfPresent(UserDO::getUsername, username)    // 模糊查询
    .eqIfPresent(UserDO::getStatus, status)          // 精确匹配
    .betweenIfPresent(UserDO::getCreateTime, startTime, endTime)  // 范围查询
    .orderByDesc(UserDO::getId);                     // 排序
```

### 2. 接口文档工具
必须添加的Swagger注解：
- @Tag: 接口分类
- @Operation: 接口说明
- @Parameter: 参数说明
- @Schema: 字段说明

### 3. 单元测试工具
```java
@SpringBootTest
public class UserServiceTest {
    
    @Resource
    private UserService userService;
    
    @Test
    public void testCreateUser() {
        // 准备测试数据
        UserDO user = buildUser();
        
        // 调用测试方法
        Long userId = userService.createUser(user);
        
        // 验证结果
        assertNotNull(userId);
        UserDO dbUser = userService.getUser(userId);
        assertEquals(user.getUsername(), dbUser.getUsername());
    }
}
``` 