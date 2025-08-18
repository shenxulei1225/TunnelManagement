# 组件配置后端实现示例

## 1. 实体类定义

```java
@Entity
@Table(name = "component_configs")
@Data
@EqualsAndHashCode(callSuper = true)
public class ComponentConfig extends BaseEntity {
    
    @Column(name = "config_key", unique = true, nullable = false, length = 100)
    private String configKey;
    
    @Column(name = "component_type", nullable = false, length = 50)
    private String componentType;
    
    @Column(name = "page_path", nullable = false, length = 200)
    private String pagePath;
    
    @Column(name = "component_id", nullable = false, length = 50)
    private String componentId;
    
    @Column(name = "config_name", nullable = false, length = 100)
    private String configName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "config_data", columnDefinition = "JSON", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String configData;
    
    @Column(name = "version", length = 20)
    private String version = "1.0.0";
    
    @Column(name = "status")
    private Integer status = 1;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "updated_by")
    private Long updatedBy;
}

@Entity
@Table(name = "component_config_templates")
@Data
@EqualsAndHashCode(callSuper = true)
public class ComponentConfigTemplate extends BaseEntity {
    
    @Column(name = "template_key", unique = true, nullable = false, length = 100)
    private String templateKey;
    
    @Column(name = "component_type", nullable = false, length = 50)
    private String componentType;
    
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "config_data", columnDefinition = "JSON", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String configData;
    
    @Column(name = "is_system")
    private Boolean isSystem = false;
    
    @Column(name = "created_by")
    private Long createdBy;
}
```

## 2. Repository接口

```java
@Repository
public interface ComponentConfigRepository extends JpaRepository<ComponentConfig, Long> {
    
    Optional<ComponentConfig> findByConfigKey(String configKey);
    
    Optional<ComponentConfig> findByPagePathAndComponentId(String pagePath, String componentId);
    
    List<ComponentConfig> findByComponentTypeAndStatus(String componentType, Integer status);
    
    List<ComponentConfig> findByPagePathAndStatus(String pagePath, Integer status);
    
    @Query("SELECT c FROM ComponentConfig c WHERE " +
           "(:componentType IS NULL OR c.componentType = :componentType) AND " +
           "(:pagePath IS NULL OR c.pagePath = :pagePath) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:keyword IS NULL OR c.configName LIKE %:keyword% OR c.description LIKE %:keyword%)")
    Page<ComponentConfig> findByConditions(
        @Param("componentType") String componentType,
        @Param("pagePath") String pagePath,
        @Param("status") Integer status,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}

@Repository
public interface ComponentConfigTemplateRepository extends JpaRepository<ComponentConfigTemplate, Long> {
    
    Optional<ComponentConfigTemplate> findByTemplateKey(String templateKey);
    
    List<ComponentConfigTemplate> findByComponentType(String componentType);
    
    List<ComponentConfigTemplate> findByComponentTypeAndIsSystem(String componentType, Boolean isSystem);
}
```

## 3. Service层实现

```java
@Service
@Transactional
@Slf4j
public class ComponentConfigService {
    
    @Autowired
    private ComponentConfigRepository configRepository;
    
    @Autowired
    private ComponentConfigTemplateRepository templateRepository;
    
    @Autowired
    private ComponentConfigHistoryRepository historyRepository;
    
    /**
     * 获取组件配置
     */
    public ComponentConfig getConfig(String pagePath, String componentId) {
        return configRepository.findByPagePathAndComponentId(pagePath, componentId)
                .orElse(null);
    }
    
    /**
     * 根据配置键获取配置
     */
    public ComponentConfig getConfigByKey(String configKey) {
        return configRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new BusinessException("配置不存在: " + configKey));
    }
    
    /**
     * 保存组件配置
     */
    public ComponentConfig saveConfig(ComponentConfigSaveRequest request) {
        // 验证配置数据
        validateConfigData(request.getComponentType(), request.getConfigData());
        
        ComponentConfig config = configRepository.findByConfigKey(request.getConfigKey())
                .orElse(new ComponentConfig());
        
        // 如果是更新，保存历史记录
        if (config.getId() != null) {
            saveConfigHistory(config);
        }
        
        // 更新配置
        BeanUtils.copyProperties(request, config);
        config.setUpdatedBy(getCurrentUserId());
        config.setUpdatedTime(LocalDateTime.now());
        
        if (config.getId() == null) {
            config.setCreatedBy(getCurrentUserId());
            config.setCreatedTime(LocalDateTime.now());
        }
        
        return configRepository.save(config);
    }
    
    /**
     * 验证配置数据
     */
    private void validateConfigData(String componentType, String configData) {
        try {
            JsonNode jsonNode = objectMapper.readTree(configData);
            
            switch (componentType) {
                case "SuperTree":
                    validateSuperTreeConfig(jsonNode);
                    break;
                case "SuperList":
                    validateSuperListConfig(jsonNode);
                    break;
                // 其他组件类型的验证
            }
        } catch (Exception e) {
            throw new BusinessException("配置数据格式错误: " + e.getMessage());
        }
    }
    
    private void validateSuperTreeConfig(JsonNode config) {
        // 验证必填字段
        if (!config.has("fieldMapping")) {
            throw new BusinessException("SuperTree配置缺少fieldMapping");
        }
        
        JsonNode fieldMapping = config.get("fieldMapping");
        if (!fieldMapping.has("label") || fieldMapping.get("label").asText().isEmpty()) {
            throw new BusinessException("fieldMapping.label不能为空");
        }
        if (!fieldMapping.has("children") || fieldMapping.get("children").asText().isEmpty()) {
            throw new BusinessException("fieldMapping.children不能为空");
        }
        if (!fieldMapping.has("id") || fieldMapping.get("id").asText().isEmpty()) {
            throw new BusinessException("fieldMapping.id不能为空");
        }
    }
    
    /**
     * 保存配置历史
     */
    private void saveConfigHistory(ComponentConfig config) {
        ComponentConfigHistory history = new ComponentConfigHistory();
        history.setConfigId(config.getId());
        history.setConfigData(config.getConfigData());
        history.setVersion(config.getVersion());
        history.setCreatedBy(getCurrentUserId());
        history.setCreatedTime(LocalDateTime.now());
        
        historyRepository.save(history);
    }
    
    /**
     * 复制配置
     */
    public ComponentConfig copyConfig(Long id, String newConfigKey, String newConfigName) {
        ComponentConfig originalConfig = configRepository.findById(id)
                .orElseThrow(() -> new BusinessException("原配置不存在"));
        
        // 检查新配置键是否已存在
        if (configRepository.findByConfigKey(newConfigKey).isPresent()) {
            throw new BusinessException("配置键已存在: " + newConfigKey);
        }
        
        ComponentConfig newConfig = new ComponentConfig();
        BeanUtils.copyProperties(originalConfig, newConfig);
        newConfig.setId(null);
        newConfig.setConfigKey(newConfigKey);
        newConfig.setConfigName(newConfigName);
        newConfig.setCreatedBy(getCurrentUserId());
        newConfig.setCreatedTime(LocalDateTime.now());
        newConfig.setUpdatedBy(null);
        newConfig.setUpdatedTime(null);
        
        return configRepository.save(newConfig);
    }
    
    /**
     * 基于模板创建配置
     */
    public ComponentConfig createFromTemplate(String templateKey, String configKey, String configName) {
        ComponentConfigTemplate template = templateRepository.findByTemplateKey(templateKey)
                .orElseThrow(() -> new BusinessException("模板不存在: " + templateKey));
        
        // 检查配置键是否已存在
        if (configRepository.findByConfigKey(configKey).isPresent()) {
            throw new BusinessException("配置键已存在: " + configKey);
        }
        
        // 解析配置键获取页面路径和组件ID
        String[] parts = configKey.split("_");
        String componentId = parts[parts.length - 1];
        String pagePath = String.join("_", Arrays.copyOf(parts, parts.length - 1))
                .replace("_", "/");
        
        ComponentConfig config = new ComponentConfig();
        config.setConfigKey(configKey);
        config.setComponentType(template.getComponentType());
        config.setPagePath(pagePath);
        config.setComponentId(componentId);
        config.setConfigName(configName);
        config.setConfigData(template.getConfigData());
        config.setCreatedBy(getCurrentUserId());
        config.setCreatedTime(LocalDateTime.now());
        
        return configRepository.save(config);
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        // 从SecurityContext或其他地方获取当前用户ID
        return 1L; // 示例
    }
}
```

## 4. Controller层实现

```java
@RestController
@RequestMapping("/system/component-config")
@Slf4j
public class ComponentConfigController {
    
    @Autowired
    private ComponentConfigService configService;
    
    /**
     * 获取配置列表
     */
    @GetMapping("/list")
    public Result<PageResult<ComponentConfig>> getConfigList(
            @RequestParam(required = false) String componentType,
            @RequestParam(required = false) String pagePath,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        PageResult<ComponentConfig> result = configService.getConfigList(
            componentType, pagePath, status, keyword, page, size);
        return Result.success(result);
    }
    
    /**
     * 根据页面路径和组件ID获取配置
     */
    @GetMapping("/get")
    public Result<ComponentConfig> getConfig(
            @RequestParam String pagePath,
            @RequestParam String componentId) {
        
        ComponentConfig config = configService.getConfig(pagePath, componentId);
        return Result.success(config);
    }
    
    /**
     * 根据配置键获取配置
     */
    @GetMapping("/get/{configKey}")
    public Result<ComponentConfig> getConfigByKey(@PathVariable String configKey) {
        ComponentConfig config = configService.getConfigByKey(configKey);
        return Result.success(config);
    }
    
    /**
     * 保存配置
     */
    @PostMapping("/save")
    public Result<ComponentConfig> saveConfig(@RequestBody @Valid ComponentConfigSaveRequest request) {
        ComponentConfig config = configService.saveConfig(request);
        return Result.success(config);
    }
    
    /**
     * 更新配置
     */
    @PutMapping("/update/{id}")
    public Result<ComponentConfig> updateConfig(
            @PathVariable Long id,
            @RequestBody @Valid ComponentConfigUpdateRequest request) {
        
        ComponentConfig config = configService.updateConfig(id, request);
        return Result.success(config);
    }
    
    /**
     * 删除配置
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        configService.deleteConfig(id);
        return Result.success();
    }
    
    /**
     * 复制配置
     */
    @PostMapping("/copy/{id}")
    public Result<ComponentConfig> copyConfig(
            @PathVariable Long id,
            @RequestBody ComponentConfigCopyRequest request) {
        
        ComponentConfig config = configService.copyConfig(
            id, request.getConfigKey(), request.getConfigName());
        return Result.success(config);
    }
    
    /**
     * 导出配置
     */
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportConfig(@RequestBody List<Long> ids) {
        byte[] data = configService.exportConfigs(ids);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "component-configs.json");
        
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
    
    /**
     * 导入配置
     */
    @PostMapping("/import")
    public Result<Map<String, Object>> importConfig(@RequestParam MultipartFile file) {
        Map<String, Object> result = configService.importConfigs(file);
        return Result.success(result);
    }
}
```

## 5. 使用方式总结

### 配置键规则
- 格式：`{页面路径}_{组件ID}`
- 示例：`/system/user_userTree`、`/system/role_roleList`

### 典型使用场景

1. **用户管理页面的树形组件**
   - 配置键：`/system/user_userTree`
   - 组件类型：`SuperTree`
   - 配置内容：用户树的显示字段、操作按钮、搜索配置等

2. **角色管理页面的列表组件**
   - 配置键：`/system/role_roleList`
   - 组件类型：`SuperList`
   - 配置内容：列表字段、分页配置、操作按钮等

3. **字典管理页面的树形组件**
   - 配置键：`/system/dict_dictTree`
   - 组件类型：`SuperTree`
   - 配置内容：字典树的层级显示、编辑功能等

### 配置存储优势

1. **灵活性**：每个页面的同类组件可以有不同配置
2. **可维护性**：配置独立存储，便于管理和修改
3. **可扩展性**：支持版本控制、历史记录、模板等功能
4. **复用性**：通过模板可以快速创建标准配置
5. **权限控制**：可以基于用户角色控制配置的可见性和可编辑性