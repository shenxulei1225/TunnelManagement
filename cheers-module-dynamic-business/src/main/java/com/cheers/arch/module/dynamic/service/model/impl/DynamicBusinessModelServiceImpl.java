package com.cheers.arch.module.dynamic.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.directory.constants.DirectoryConstants;
import com.cheers.arch.framework.directory.service.DirectoryService;
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

        // 检查是否有业务记录
        // TODO: 检查是否有业务记录使用此模型

        // 删除数据
        dynamicBusinessModelMapper.deleteById(id);

        // 删除缓存
        // deleteCache(model);
    }

    @Override
    public DynamicBusinessModelDO getModel(Long id) {
        // 先从缓存获取
        // DynamicBusinessModelDO model = getModelFromCache(id);
        // if (model != null) {
        //     return model;
        // }

        // 从数据库获取
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectById(id);

        // 更新缓存
        // if (model != null) {
        //     updateCache(model);
        // }

        return model;
    }

    @Override
    public DynamicBusinessModelDO getModelByCode(String code) {
        // 先从缓存获取
        // DynamicBusinessModelDO model = getModelFromCacheByCode(code);
        // if (model != null) {
        //     return model;
        // }

        // 从数据库获取
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectOne(new LambdaQueryWrapper<DynamicBusinessModelDO>()
                .eq(DynamicBusinessModelDO::getCode, code));

        // 更新缓存
        // if (model != null) {
        //     updateCache(model);
        // }

        return model;
    }

    @Override
    public List<DynamicBusinessModelDO> getModelList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return dynamicBusinessModelMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DynamicBusinessModelDO> getModelPage(Integer pageNo, Integer pageSize, String name, Integer status) {
        // 分页查询
        Page<DynamicBusinessModelDO> page = dynamicBusinessModelMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapperX<DynamicBusinessModelDO>()
                        .likeIfPresent(DynamicBusinessModelDO::getName, name)
                        .eqIfPresent(DynamicBusinessModelDO::getStatus, status)
                        .orderByAsc(DynamicBusinessModelDO::getSort));

        // 返回分页结果
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PageResult<DynamicBusinessModelDO> getModelPageByDirectory(Integer pageNo, Integer pageSize, String name, Integer status, Long directoryId) {
        // 分页查询
        Page<DynamicBusinessModelDO> page = dynamicBusinessModelMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapperX<DynamicBusinessModelDO>()
                        .likeIfPresent(DynamicBusinessModelDO::getName, name)
                        .eqIfPresent(DynamicBusinessModelDO::getStatus, status)
                        .eqIfPresent(DynamicBusinessModelDO::getDirectoryId, directoryId)
                        .orderByAsc(DynamicBusinessModelDO::getSort));

        // 返回分页结果
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean isCodeUnique(String code, Long excludeId) {
        if (StringUtils.isEmpty(code)) {
            return true;
        }

        // 查询编码是否存在
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectOne(new LambdaQueryWrapper<DynamicBusinessModelDO>()
                .eq(DynamicBusinessModelDO::getCode, code)
                .last("LIMIT 1"));

        // 如果不存在，说明不重复
        if (model == null) {
            return true;
        }

        // 如果是更新操作，且查询到的是当前记录，说明不重复
        return excludeId != null && Objects.equals(model.getId(), excludeId);
    }

    @Override
    public List<DynamicBusinessModelDO> getModelListByTenant(Long tenantId) {
        if (tenantId == null) {
            return List.of();
        }
        return dynamicBusinessModelMapper.selectList(new LambdaQueryWrapper<DynamicBusinessModelDO>()
                .eq(DynamicBusinessModelDO::getTenantId, tenantId));
    }

    @Override
    public void initModelTable(DynamicBusinessModelDO model) {
        // TODO: 实现动态表初始化
        log.info("初始化模型表: {}", model.getTableName());
    }

    @Override
    public void updateModelTable(DynamicBusinessModelDO model) {
        // TODO: 实现动态表更新
        log.info("更新模型表: {}", model.getTableName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateSort(List<Map<String, Object>> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return;
        }

        // 批量更新排序
        for (Map<String, Object> sortItem : sortList) {
            Long id = (Long) sortItem.get("id");
            Integer sort = (Integer) sortItem.get("sort");
            if (id != null && sort != null) {
                DynamicBusinessModelDO model = new DynamicBusinessModelDO();
                model.setId(id);
                model.setSort(sort);
                dynamicBusinessModelMapper.updateById(model);
            }
        }
    }

    /**
     * 校验模型是否存在
     */
    private DynamicBusinessModelDO validateModelExists(Long id) {
        DynamicBusinessModelDO model = dynamicBusinessModelMapper.selectById(id);
        if (model == null) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_NOT_EXISTS);
        }
        return model;
    }

    // TODO: 实现缓存相关方法
    // private DynamicBusinessModelDO getModelFromCache(Long id) {
    //     return redisCache.get(CACHE_KEY_PREFIX + id);
    // }
    //
    // private DynamicBusinessModelDO getModelFromCacheByCode(String code) {
    //     return redisCache.get(CACHE_KEY_PREFIX + "code:" + code);
    // }
    //
    // private void updateCache(DynamicBusinessModelDO model) {
    //     redisCache.set(CACHE_KEY_PREFIX + model.getId(), model);
    //     redisCache.set(CACHE_KEY_PREFIX + "code:" + model.getCode(), model);
    // }
    //
    // private void deleteCache(DynamicBusinessModelDO model) {
    //     redisCache.delete(CACHE_KEY_PREFIX + model.getId());
    //     redisCache.delete(CACHE_KEY_PREFIX + "code:" + model.getCode());
    // }
} 