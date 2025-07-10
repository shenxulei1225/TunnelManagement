package com.cheers.module.dynamic.service.field.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.framework.common.exception.ServiceException;
import com.cheers.framework.common.pojo.PageResult;
import com.cheers.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.framework.redis.core.RedisCache;
import com.cheers.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import com.cheers.module.dynamic.dal.mysql.field.FieldDefinitionMapper;
import com.cheers.module.dynamic.service.field.FieldDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 字段定义服务实现类
 */
@Service
@Slf4j
public class FieldDefinitionServiceImpl implements FieldDefinitionService {

    @Resource
    private FieldDefinitionMapper fieldDefinitionMapper;

    @Resource
    private RedisCache redisCache;

    private static final String CACHE_KEY_PREFIX = "dynamic_business:field:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createField(FieldDefinitionDO field) {
        // 校验编码唯一性
        if (!isCodeUnique(field.getModelCode(), field.getCode(), null)) {
            throw new ServiceException("字段编码已存在");
        }

        // 插入数据
        fieldDefinitionMapper.insert(field);

        // 更新缓存
        updateCache(field);

        return field.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateField(FieldDefinitionDO field) {
        // 校验存在
        FieldDefinitionDO oldField = validateFieldExists(field.getId());

        // 校验是否可以修改
        if (!validateFieldCanUpdate(field.getId())) {
            throw new ServiceException("字段已被使用，不能修改");
        }

        // 校验编码唯一性
        if (!isCodeUnique(field.getModelCode(), field.getCode(), field.getId())) {
            throw new ServiceException("字段编码已存在");
        }

        // 更新数据
        fieldDefinitionMapper.updateById(field);

        // 更新缓存
        updateCache(field);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteField(Long id) {
        // 校验存在
        FieldDefinitionDO field = validateFieldExists(id);

        // 校验是否可以删除
        if (!validateFieldCanDelete(id)) {
            throw new ServiceException("字段已被使用，不能删除");
        }

        // 删除数据
        fieldDefinitionMapper.deleteById(id);

        // 删除缓存
        deleteCache(field);
    }

    @Override
    public FieldDefinitionDO getField(Long id) {
        // 先从缓存获取
        FieldDefinitionDO field = getFieldFromCache(id);
        if (field != null) {
            return field;
        }

        // 从数据库获取
        field = fieldDefinitionMapper.selectById(id);

        // 更新缓存
        if (field != null) {
            updateCache(field);
        }

        return field;
    }

    @Override
    public FieldDefinitionDO getFieldByCode(String modelCode, String code) {
        // 先从缓存获取
        FieldDefinitionDO field = getFieldFromCacheByCode(modelCode, code);
        if (field != null) {
            return field;
        }

        // 从数据库获取
        field = fieldDefinitionMapper.selectOne(new LambdaQueryWrapper<FieldDefinitionDO>()
                .eq(FieldDefinitionDO::getModelCode, modelCode)
                .eq(FieldDefinitionDO::getCode, code));

        // 更新缓存
        if (field != null) {
            updateCache(field);
        }

        return field;
    }

    @Override
    public List<FieldDefinitionDO> getFieldListByModel(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return List.of();
        }
        return fieldDefinitionMapper.selectList(new LambdaQueryWrapper<FieldDefinitionDO>()
                .eq(FieldDefinitionDO::getModelCode, modelCode)
                .orderByAsc(FieldDefinitionDO::getSort));
    }

    @Override
    public PageResult<FieldDefinitionDO> getFieldPage(Integer pageNo, Integer pageSize, String modelCode, String name, Integer status) {
        // 分页查询
        Page<FieldDefinitionDO> page = fieldDefinitionMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapperX<FieldDefinitionDO>()
                        .eq(FieldDefinitionDO::getModelCode, modelCode)
                        .likeIfPresent(FieldDefinitionDO::getName, name)
                        .eqIfPresent(FieldDefinitionDO::getStatus, status)
                        .orderByAsc(FieldDefinitionDO::getSort));

        // 返回分页结果
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public boolean isCodeUnique(String modelCode, String code, Long excludeId) {
        if (StringUtils.isEmpty(modelCode) || StringUtils.isEmpty(code)) {
            return true;
        }

        // 查询编码是否存在
        FieldDefinitionDO field = fieldDefinitionMapper.selectOne(new LambdaQueryWrapper<FieldDefinitionDO>()
                .eq(FieldDefinitionDO::getModelCode, modelCode)
                .eq(FieldDefinitionDO::getCode, code)
                .last("LIMIT 1"));

        // 如果不存在，说明不重复
        if (field == null) {
            return true;
        }

        // 如果是更新操作，且查询到的是当前记录，说明不重复
        return excludeId != null && Objects.equals(field.getId(), excludeId);
    }

    @Override
    public List<FieldDefinitionDO> getFieldListByTenant(Long tenantId) {
        if (tenantId == null) {
            return List.of();
        }
        return fieldDefinitionMapper.selectList(new LambdaQueryWrapper<FieldDefinitionDO>()
                .eq(FieldDefinitionDO::getTenantId, tenantId));
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

    private FieldDefinitionDO validateFieldExists(Long id) {
        FieldDefinitionDO field = getField(id);
        if (field == null) {
            throw new ServiceException("字段不存在");
        }
        return field;
    }

    private FieldDefinitionDO getFieldFromCache(Long id) {
        return redisCache.getCacheObject(CACHE_KEY_PREFIX + "id:" + id);
    }

    private FieldDefinitionDO getFieldFromCacheByCode(String modelCode, String code) {
        return redisCache.getCacheObject(CACHE_KEY_PREFIX + "code:" + modelCode + ":" + code);
    }

    private void updateCache(FieldDefinitionDO field) {
        // 更新ID缓存
        redisCache.setCacheObject(CACHE_KEY_PREFIX + "id:" + field.getId(), field);
        // 更新编码缓存
        redisCache.setCacheObject(CACHE_KEY_PREFIX + "code:" + field.getModelCode() + ":" + field.getCode(), field);
    }

    private void deleteCache(FieldDefinitionDO field) {
        // 删除ID缓存
        redisCache.deleteObject(CACHE_KEY_PREFIX + "id:" + field.getId());
        // 删除编码缓存
        redisCache.deleteObject(CACHE_KEY_PREFIX + "code:" + field.getModelCode() + ":" + field.getCode());
    }
} 