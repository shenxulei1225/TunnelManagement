package com.cheers.arch.module.dynamic.service.field.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.redis.core.RedisCache;
import com.cheers.arch.module.dynamic.dal.dataobject.field.DynamicFieldDefinitionDO;
import com.cheers.arch.module.dynamic.dal.mysql.field.DynamicFieldDefinitionMapper;
import com.cheers.arch.module.dynamic.enums.ErrorCodeConstants;
import com.cheers.arch.module.dynamic.service.field.DynamicFieldDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 动态字段定义服务实现类
 */
@Service
@Slf4j
public class DynamicFieldDefinitionServiceImpl implements DynamicFieldDefinitionService {

    @Resource
    private DynamicFieldDefinitionMapper dynamicFieldDefinitionMapper;

    // @Resource
    // private RedisCache redisCache;

    private static final String CACHE_KEY_PREFIX = "dynamic_business:field:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createField(DynamicFieldDefinitionDO field) {
        // 校验编码唯一性
        if (!isCodeUnique(field.getModelCode(), field.getCode(), null)) {
            throw new ServiceException(ErrorCodeConstants.FIELD_DEFINITION_CODE_DUPLICATE);
        }

        // 插入数据
        dynamicFieldDefinitionMapper.insert(field);

        // 更新缓存
        // updateCache(field);

        return field.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateField(DynamicFieldDefinitionDO field) {
        // 校验存在
        DynamicFieldDefinitionDO oldField = validateFieldExists(field.getId());

        // 校验是否可以修改
        if (!validateFieldCanUpdate(field.getId())) {
            throw new ServiceException(ErrorCodeConstants.FIELD_DEFINITION_INVALID_CONFIG);
        }

        // 校验编码唯一性
        if (!isCodeUnique(field.getModelCode(), field.getCode(), field.getId())) {
            throw new ServiceException(ErrorCodeConstants.FIELD_DEFINITION_CODE_DUPLICATE);
        }

        // 更新数据
        dynamicFieldDefinitionMapper.updateById(field);

        // 更新缓存
        // updateCache(field);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteField(Long id) {
        // 校验存在
        DynamicFieldDefinitionDO field = validateFieldExists(id);

        // 校验是否可以删除
        if (!validateFieldCanDelete(id)) {
            throw new ServiceException(ErrorCodeConstants.FIELD_DEFINITION_INVALID_CONFIG);
        }

        // 删除数据
        dynamicFieldDefinitionMapper.deleteById(id);

        // 删除缓存
        // deleteCache(field);
    }

    @Override
    public DynamicFieldDefinitionDO getField(Long id) {
        // 先从缓存获取
        // DynamicFieldDefinitionDO field = getFieldFromCache(id);
        // if (field != null) {
        //     return field;
        // }

        // 从数据库获取
        DynamicFieldDefinitionDO field = dynamicFieldDefinitionMapper.selectById(id);

        // 更新缓存
        // if (field != null) {
        //     updateCache(field);
        // }

        return field;
    }

    @Override
    public DynamicFieldDefinitionDO getFieldByCode(String modelCode, String code) {
        // 先从缓存获取
        // DynamicFieldDefinitionDO field = getFieldFromCacheByCode(modelCode, code);
        // if (field != null) {
        //     return field;
        // }

        // 从数据库获取
        DynamicFieldDefinitionDO field = dynamicFieldDefinitionMapper.selectOne(new LambdaQueryWrapper<DynamicFieldDefinitionDO>()
                .eq(DynamicFieldDefinitionDO::getModelCode, modelCode)
                .eq(DynamicFieldDefinitionDO::getCode, code));

        // 更新缓存
        // if (field != null) {
        //     updateCache(field);
        // }

        return field;
    }

    @Override
    public List<DynamicFieldDefinitionDO> getFieldListByModel(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return List.of();
        }
        return dynamicFieldDefinitionMapper.selectList(new LambdaQueryWrapper<DynamicFieldDefinitionDO>()
                .eq(DynamicFieldDefinitionDO::getModelCode, modelCode)
                .orderByAsc(DynamicFieldDefinitionDO::getSort));
    }

    @Override
    public PageResult<DynamicFieldDefinitionDO> getFieldPage(Integer pageNo, Integer pageSize, String name, String code, String type, Long modelId) {
        // 分页查询
        Page<DynamicFieldDefinitionDO> page = dynamicFieldDefinitionMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapperX<DynamicFieldDefinitionDO>()
                        .likeIfPresent(DynamicFieldDefinitionDO::getName, name)
                        .likeIfPresent(DynamicFieldDefinitionDO::getCode, code)
                        .eqIfPresent(DynamicFieldDefinitionDO::getType, type)
                        .eqIfPresent(DynamicFieldDefinitionDO::getModelCode, modelId != null ? modelId.toString() : null)
                        .orderByAsc(DynamicFieldDefinitionDO::getSort));

        // 返回分页结果
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean isCodeUnique(String modelCode, String code, Long excludeId) {
        if (StringUtils.isEmpty(modelCode) || StringUtils.isEmpty(code)) {
            return true;
        }

        // 查询编码是否存在
        DynamicFieldDefinitionDO field = dynamicFieldDefinitionMapper.selectOne(new LambdaQueryWrapper<DynamicFieldDefinitionDO>()
                .eq(DynamicFieldDefinitionDO::getModelCode, modelCode)
                .eq(DynamicFieldDefinitionDO::getCode, code)
                .last("LIMIT 1"));

        // 如果不存在，说明不重复
        if (field == null) {
            return true;
        }

        // 如果是更新操作，且查询到的是当前记录，说明不重复
        return excludeId != null && Objects.equals(field.getId(), excludeId);
    }

    @Override
    public List<DynamicFieldDefinitionDO> getFieldListByTenant(Long tenantId) {
        if (tenantId == null) {
            return List.of();
        }
        return dynamicFieldDefinitionMapper.selectList(new LambdaQueryWrapper<DynamicFieldDefinitionDO>()
                .eq(DynamicFieldDefinitionDO::getTenantId, tenantId));
    }

    @Override
    public boolean validateFieldCanDelete(Long id) {
        // TODO: 检查字段是否被使用
        return true;
    }

    @Override
    public boolean validateFieldCanUpdate(Long id) {
        // TODO: 检查字段是否被使用
        return true;
    }

    @Override
    public List<DynamicFieldDefinitionDO> getFieldList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return dynamicFieldDefinitionMapper.selectBatchIds(ids);
    }

    /**
     * 校验动态字段定义是否存在
     */
    private DynamicFieldDefinitionDO validateFieldExists(Long id) {
        DynamicFieldDefinitionDO field = dynamicFieldDefinitionMapper.selectById(id);
        if (field == null) {
            throw new ServiceException(ErrorCodeConstants.FIELD_DEFINITION_NOT_EXISTS);
        }
        return field;
    }

    // TODO: 实现缓存相关方法
    // private DynamicFieldDefinitionDO getFieldFromCache(Long id) {
    //     return redisCache.get(CACHE_KEY_PREFIX + id);
    // }
    //
    // private DynamicFieldDefinitionDO getFieldFromCacheByCode(String modelCode, String code) {
    //     return redisCache.get(CACHE_KEY_PREFIX + "code:" + modelCode + ":" + code);
    // }
    //
    // private void updateCache(DynamicFieldDefinitionDO field) {
    //     redisCache.set(CACHE_KEY_PREFIX + field.getId(), field);
    //     redisCache.set(CACHE_KEY_PREFIX + "code:" + field.getModelCode() + ":" + field.getCode(), field);
    // }
    //
    // private void deleteCache(DynamicFieldDefinitionDO field) {
    //     redisCache.delete(CACHE_KEY_PREFIX + field.getId());
    //     redisCache.delete(CACHE_KEY_PREFIX + "code:" + field.getModelCode() + ":" + field.getCode());
    // }
} 