package com.cheers.arch.module.dynamic.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.service.directory.DirectoryService;
import com.cheers.arch.module.dynamic.dal.dataobject.field.DynamicFieldDefinitionDO;
import com.cheers.arch.module.dynamic.dal.dataobject.model.DynamicBusinessModelDO;
import com.cheers.arch.module.dynamic.dal.mysql.model.DynamicBusinessModelMapper;
import com.cheers.arch.module.dynamic.enums.ErrorCodeConstants;
import com.cheers.arch.module.dynamic.enums.BusinessModelTypeEnum;
import com.cheers.arch.module.dynamic.service.field.DynamicFieldDefinitionService;
import com.cheers.arch.module.dynamic.service.model.DynamicBusinessModelService;
import com.cheers.arch.module.dynamic.service.table.DynamicTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Map;

/**
 * 动态业务模型服务实现类
 */
@Service
@Slf4j
public class DynamicBusinessModelServiceImpl implements DynamicBusinessModelService {

    @Resource
    private DynamicBusinessModelMapper dynamicBusinessModelMapper;

    @Resource
    private DynamicTableService dynamicTableService;

    @Resource
    private DynamicFieldDefinitionService dynamicFieldDefinitionService;

    @Resource
    private DirectoryService directoryService;

    // @Resource
    // private RedisCache redisCache;

    private static final String CACHE_KEY_PREFIX = "dynamic_business:model:";

    /**
     * 自动生成表名
     * 命名规范：dynamic_{model_code}
     */
    private String generateTableName(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            // 如果编码为空，生成一个临时编码
            modelCode = "model_" + System.currentTimeMillis() % 10000;
        }
        
        // 验证模型编码格式（只允许字母、数字、下划线）
        if (!modelCode.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_CODE_INVALID);
        }
        
        // 生成表名：dynamic_{model_code}
        return "dynamic_" + modelCode.toLowerCase();
    }

    /**
     * 自动生成模型编码
     * 根据模型名称生成规范的编码
     */
    private String generateModelCode(String modelName) {
        if (StringUtils.isEmpty(modelName)) {
            // 如果模型名称为空，生成一个临时编码
            return "model_" + System.currentTimeMillis() % 10000;
        }
        
        // 移除特殊字符，只保留中文、英文、数字
        String code = modelName.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9]", "");
        
        // 如果是中文，转换为拼音
        if (code.matches(".*[\\u4e00-\\u9fa5].*")) {
            // 这里可以集成拼音转换库，暂时用简单的方式
            code = "model_" + System.currentTimeMillis() % 10000;
        } else {
            // 英文直接使用，转换为小写
            code = code.toLowerCase();
        }
        
        // 确保以字母开头
        if (!code.matches("^[a-zA-Z].*")) {
            code = "model_" + code;
        }
        
        // 限制长度
        if (code.length() > 50) {
            code = code.substring(0, 50);
        }
        
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createModel(DynamicBusinessModelDO model) {
        // 自动生成模型编码（如果为空）
        if (StringUtils.isEmpty(model.getCode())) {
            model.setCode(generateModelCode(model.getName()));
        }
        
        // 校验编码唯一性
        if (!isCodeUnique(model.getCode(), null)) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_CODE_DUPLICATE);
        }

        // 自动生成表名
        if (model.getTableName() == null || model.getTableName().isEmpty()) {
            model.setTableName(generateTableName(model.getCode()));
        } else {
            // 如果用户提供了表名，验证格式
            if (!model.getTableName().startsWith("dynamic_")) {
                throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_TABLE_NAME_INVALID);
            }
        }

        // 设置默认值
        if (model.getStructureType() == null) {
            model.setStructureType(1); // 默认树形结构
        }
        if (model.getStorageStrategy() == null) {
            model.setStorageStrategy(1); // 默认单表存储
        }
        if (model.getReadonly() == null) {
            model.setReadonly(false); // 默认可编辑
        }

        // 插入数据
        dynamicBusinessModelMapper.insert(model);

        // 初始化数据表
        initModelTable(model);

        // 更新缓存
        // updateCache(model);

        return model.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(DynamicBusinessModelDO model) {
        // 校验存在
        DynamicBusinessModelDO oldModel = validateModelExists(model.getId());

        // 模型类型可以修改，不再限制
        // 注意：修改模型类型时，系统模型自动设置为只读，自定义模型默认可编辑
        if (model.getModelType() != null && !Objects.equals(oldModel.getModelType(), model.getModelType())) {
            // 如果从自定义改为系统类型，自动设置为只读
            if (model.getModelType() == 0 && oldModel.getModelType() == 1) {
                model.setReadonly(true);
            }
            // 如果从系统改为自定义类型，保持只读状态不变（由用户手动控制）
        }

        // 自动生成模型编码（如果为空）
        if (StringUtils.isEmpty(model.getCode())) {
            model.setCode(generateModelCode(model.getName()));
        }

        // 校验编码唯一性
        if (!isCodeUnique(model.getCode(), model.getId())) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_CODE_DUPLICATE);
        }

        // 自动生成表名
        if (model.getTableName() == null || model.getTableName().isEmpty()) {
            model.setTableName(generateTableName(model.getCode()));
        } else {
            // 如果用户提供了表名，验证格式
            if (!model.getTableName().startsWith("dynamic_")) {
                throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_TABLE_NAME_INVALID);
            }
        }

        // 更新数据
        dynamicBusinessModelMapper.updateById(model);

        // 如果字段结构发生变化，需要更新数据表
        if (!Objects.equals(oldModel.getConfig(), model.getConfig())) {
            updateModelTable(model);
        }

        // 更新缓存
        // updateCache(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(Long id) {
        // 校验存在
        DynamicBusinessModelDO model = validateModelExists(id);

        // 删除数据
        dynamicBusinessModelMapper.deleteById(id);

        // 删除关联的字段定义
        // TODO: 实现删除关联字段定义的方法
        // dynamicFieldDefinitionService.deleteFieldDefinitionsByModelId(id);

        // 删除数据表（可选，根据业务需求决定）
        // dynamicTableService.dropTable(model.getTableName());

        // 清除缓存
        // clearCache(id);
    }

    @Override
    public DynamicBusinessModelDO getModel(Long id) {
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectById(id);
        if (model == null) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_NOT_EXISTS);
        }
        return model;
    }

    @Override
    public DynamicBusinessModelDO getModelByCode(String code) {
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectOne(
                new LambdaQueryWrapper<DynamicBusinessModelDO>()
                        .eq(DynamicBusinessModelDO::getCode, code));
        if (model == null) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_NOT_EXISTS);
        }
        return model;
    }

    @Override
    public List<DynamicBusinessModelDO> getModelList(List<Long> ids) {
        return dynamicBusinessModelMapper.selectList(
                new LambdaQueryWrapper<DynamicBusinessModelDO>()
                        .in(DynamicBusinessModelDO::getId, ids));
    }

    @Override
    public PageResult<DynamicBusinessModelDO> getModelPage(Integer pageNo, Integer pageSize, String name, Integer status) {
        Page<DynamicBusinessModelDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapperX<DynamicBusinessModelDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(DynamicBusinessModelDO::getName, name)
                .eqIfPresent(DynamicBusinessModelDO::getStatus, status)
                .orderByDesc(DynamicBusinessModelDO::getId);
        
        Page<DynamicBusinessModelDO> result = dynamicBusinessModelMapper.selectPage(page, queryWrapper);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public PageResult<DynamicBusinessModelDO> getModelPageByDirectory(Integer pageNo, Integer pageSize, String name, Integer status, Long directoryId) {
        Page<DynamicBusinessModelDO> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapperX<DynamicBusinessModelDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.likeIfPresent(DynamicBusinessModelDO::getName, name)
                .eqIfPresent(DynamicBusinessModelDO::getStatus, status)
                .eqIfPresent(DynamicBusinessModelDO::getDirectoryId, directoryId)
                .orderByDesc(DynamicBusinessModelDO::getId);
        
        Page<DynamicBusinessModelDO> result = dynamicBusinessModelMapper.selectPage(page, queryWrapper);
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    public boolean isCodeUnique(String code, Long excludeId) {
        LambdaQueryWrapper<DynamicBusinessModelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DynamicBusinessModelDO::getCode, code);
        if (excludeId != null) {
            queryWrapper.ne(DynamicBusinessModelDO::getId, excludeId);
        }
        return dynamicBusinessModelMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public List<DynamicBusinessModelDO> getModelListByTenant(Long tenantId) {
        return dynamicBusinessModelMapper.selectList(
                new LambdaQueryWrapper<DynamicBusinessModelDO>()
                        .eq(DynamicBusinessModelDO::getTenantId, tenantId));
    }

    @Override
    public void initModelTable(DynamicBusinessModelDO model) {
        // 这里可以调用动态表服务来创建表
        // dynamicTableService.createTable(model);
    }

    @Override
    public void updateModelTable(DynamicBusinessModelDO model) {
        // 这里可以调用动态表服务来更新表结构
        // dynamicTableService.updateTable(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateSort(List<Map<String, Object>> sortList) {
        for (Map<String, Object> sortItem : sortList) {
            Long id = Long.valueOf(sortItem.get("id").toString());
            Integer sort = Integer.valueOf(sortItem.get("sort").toString());
            
            DynamicBusinessModelDO model = new DynamicBusinessModelDO();
            model.setId(id);
            model.setSort(sort);
            
            dynamicBusinessModelMapper.updateById(model);
        }
    }

    private DynamicBusinessModelDO validateModelExists(Long id) {
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectById(id);
        if (model == null) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_NOT_EXISTS);
        }
        return model;
    }
} 