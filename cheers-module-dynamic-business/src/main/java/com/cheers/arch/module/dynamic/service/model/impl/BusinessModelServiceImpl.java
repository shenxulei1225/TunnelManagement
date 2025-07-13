package com.cheers.arch.module.dynamic.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.directory.constants.DirectoryConstants;
import com.cheers.arch.framework.directory.service.DirectoryService;
import com.cheers.arch.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import com.cheers.arch.module.dynamic.dal.dataobject.model.BusinessModelDO;
import com.cheers.arch.module.dynamic.dal.mysql.model.BusinessModelMapper;
import com.cheers.arch.module.dynamic.enums.ErrorCodeConstants;
import com.cheers.arch.module.dynamic.enums.BusinessModelTypeEnum;
import com.cheers.arch.module.dynamic.service.field.FieldDefinitionService;
import com.cheers.arch.module.dynamic.service.model.BusinessModelService;
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
 * 业务模型服务实现类
 */
@Service
@Slf4j
public class BusinessModelServiceImpl implements BusinessModelService {

    @Resource
    private BusinessModelMapper businessModelMapper;

    @Resource
    private DynamicTableService dynamicTableService;

    @Resource
    private FieldDefinitionService fieldDefinitionService;

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
    public Long createModel(BusinessModelDO model) {
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
        businessModelMapper.insert(model);

        // 初始化数据表
        initModelTable(model);

        // 更新缓存
        // updateCache(model);

        return model.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(BusinessModelDO model) {
        // 校验存在
        BusinessModelDO oldModel = validateModelExists(model.getId());

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
        businessModelMapper.updateById(model);

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
        BusinessModelDO model = validateModelExists(id);

        // 检查是否为只读模型，只读模型不允许删除
        if (Boolean.TRUE.equals(model.getReadonly())) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_READONLY_CANNOT_DELETE);
        }

        // 检查是否为只读模型，只读模型不允许删除
        if (Boolean.TRUE.equals(model.getReadonly())) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_READONLY_CANNOT_DELETE);
        }

        try {
            // 先删除对应的数据库表
            if (StringUtils.isNotEmpty(model.getTableName())) {
                dynamicTableService.dropTable(model);
                log.info("[deleteModel][业务模型({})的数据库表({})删除成功]", model.getCode(), model.getTableName());
            }
        } catch (Exception e) {
            log.warn("[deleteModel][删除业务模型({})的数据库表({})失败，继续删除业务模型记录]", model.getCode(), model.getTableName(), e);
            // 即使删除表失败，也继续删除业务模型记录
        }

        // 删除业务模型记录
        businessModelMapper.deleteById(id);

        // 删除缓存
        // deleteCache(model);
    }

    @Override
    public BusinessModelDO getModel(Long id) {
        // 先从缓存获取
        // BusinessModelDO model = getModelFromCache(id);
        // if (model != null) {
        //     return model;
        // }

        // 从数据库获取
        BusinessModelDO model = businessModelMapper.selectById(id);
        
        // 更新缓存
        // if (model != null) {
        //     updateCache(model);
        // }

        return model;
    }

    @Override
    public BusinessModelDO getModelByCode(String code) {
        // 先从缓存获取
        // BusinessModelDO model = getModelFromCacheByCode(code);
        // if (model != null) {
        //     return model;
        // }

        // 从数据库获取
        BusinessModelDO model = businessModelMapper.selectOne(new LambdaQueryWrapper<BusinessModelDO>()
                .eq(BusinessModelDO::getCode, code));

        // 更新缓存
        // if (model != null) {
        //     updateCache(model);
        // }

        return model;
    }

    @Override
    public List<BusinessModelDO> getModelList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return businessModelMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BusinessModelDO> getModelPage(Integer pageNo, Integer pageSize, String name, Integer status) {
        // 构建查询条件
        LambdaQueryWrapperX<BusinessModelDO> queryWrapper = new LambdaQueryWrapperX<BusinessModelDO>()
                .likeIfPresent(BusinessModelDO::getName, name)
                .eqIfPresent(BusinessModelDO::getStatus, status)
                .orderByDesc(BusinessModelDO::getSort)
                .orderByDesc(BusinessModelDO::getId);

        // 执行查询
        Page<BusinessModelDO> page = businessModelMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PageResult<BusinessModelDO> getModelPageByDirectory(Integer pageNo, Integer pageSize, String name, Integer status, Long directoryId) {
        // 构建查询条件
        LambdaQueryWrapperX<BusinessModelDO> queryWrapper = new LambdaQueryWrapperX<BusinessModelDO>()
                .likeIfPresent(BusinessModelDO::getName, name)
                .eqIfPresent(BusinessModelDO::getStatus, status)
                .eqIfPresent(BusinessModelDO::getDirectoryId, directoryId)
                .orderByDesc(BusinessModelDO::getSort)
                .orderByDesc(BusinessModelDO::getId);

        // 执行查询
        Page<BusinessModelDO> page = businessModelMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean isCodeUnique(String code, Long excludeId) {
        if (StringUtils.isEmpty(code)) {
            return true;
        }

        // 查询编码是否存在
        BusinessModelDO model = businessModelMapper.selectOne(new LambdaQueryWrapper<BusinessModelDO>()
                .eq(BusinessModelDO::getCode, code)
                .last("LIMIT 1"));

        // 如果不存在，说明不重复
        if (model == null) {
            return true;
        }

        // 如果是更新操作，且查询到的是当前记录，说明不重复
        return excludeId != null && Objects.equals(model.getId(), excludeId);
    }

    @Override
    public List<BusinessModelDO> getModelListByTenant(Long tenantId) {
        if (tenantId == null) {
            return List.of();
        }
        return businessModelMapper.selectList(new LambdaQueryWrapper<BusinessModelDO>()
                .eq(BusinessModelDO::getTenantId, tenantId));
    }

    @Override
    public void initModelTable(BusinessModelDO model) {
        // 获取模型的字段定义列表
        List<FieldDefinitionDO> fields = fieldDefinitionService.getFieldListByModel(model.getCode());
        
        // 即使没有字段定义，也创建基础表结构
        // 这样后续添加字段时可以直接使用这个表
        dynamicTableService.createTable(model, fields);
        
        log.info("[initModelTable][业务模型({})的数据表初始化成功，字段数量: {}]", model.getCode(), fields.size());
    }

    @Override
    public void updateModelTable(BusinessModelDO model) {
        // 获取模型的字段定义列表
        List<FieldDefinitionDO> fields = fieldDefinitionService.getFieldListByModel(model.getCode());
        
        // 更新数据表结构
        // TODO: 需要获取旧的字段列表进行比较
        // 暂时传入空的旧字段列表
        List<FieldDefinitionDO> oldFields = List.of();
        dynamicTableService.updateTable(model, fields, oldFields);
        log.info("[updateModelTable][业务模型({})的数据表结构更新成功]", model.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateSort(List<Map<String, Object>> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return;
        }

        for (Map<String, Object> sortItem : sortList) {
            Long id = (Long) sortItem.get("id");
            Integer sort = (Integer) sortItem.get("sort");
            
            if (id != null && sort != null) {
                BusinessModelDO model = new BusinessModelDO();
                model.setId(id);
                model.setSort(sort);
                businessModelMapper.updateById(model);
            }
        }
        
        log.info("[batchUpdateSort][批量更新排序成功，共更新{}条记录]", sortList.size());
    }


    private BusinessModelDO validateModelExists(Long id) {
        BusinessModelDO model = getModel(id);
        if (model == null) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_NOT_EXISTS);
        }
        return model;
    }

    // private BusinessModelDO getModelFromCache(Long id) {
    //     return redisCache.getCacheObject(CACHE_KEY_PREFIX + "id:" + id);
    // }

    // private BusinessModelDO getModelFromCacheByCode(String code) {
    //     return redisCache.getCacheObject(CACHE_KEY_PREFIX + "code:" + code);
    // }

    // private void updateCache(BusinessModelDO model) {
    //     // 更新ID缓存
    //     redisCache.setCacheObject(CACHE_KEY_PREFIX + "id:" + model.getId(), model);
    //     // 更新编码缓存
    //     redisCache.setCacheObject(CACHE_KEY_PREFIX + "code:" + model.getCode(), model);
    // }

    // private void deleteCache(BusinessModelDO model) {
    //     // 删除ID缓存
    //     redisCache.deleteObject(CACHE_KEY_PREFIX + "id:" + model.getId());
    //     // 删除编码缓存
    //     redisCache.deleteObject(CACHE_KEY_PREFIX + "code:" + model.getCode());
    // }
} 