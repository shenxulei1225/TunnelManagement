package com.cheers.module.dynamic.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.framework.common.exception.ServiceException;
import com.cheers.framework.common.pojo.PageResult;
import com.cheers.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.framework.redis.core.RedisCache;
import com.cheers.module.dynamic.dal.dataobject.model.BusinessModelDO;
import com.cheers.module.dynamic.dal.mysql.model.BusinessModelMapper;
import com.cheers.module.dynamic.service.model.BusinessModelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 业务模型服务实现类
 */
@Service
@Slf4j
public class BusinessModelServiceImpl implements BusinessModelService {

    @Resource
    private BusinessModelMapper businessModelMapper;

    @Resource
    private RedisCache redisCache;

    private static final String CACHE_KEY_PREFIX = "dynamic_business:model:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createModel(BusinessModelDO model) {
        // 校验编码唯一性
        if (!isCodeUnique(model.getCode(), null)) {
            throw new ServiceException("业务模型编码已存在");
        }

        // 插入数据
        businessModelMapper.insert(model);

        // 初始化数据表
        initModelTable(model);

        // 更新缓存
        updateCache(model);

        return model.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(BusinessModelDO model) {
        // 校验存在
        BusinessModelDO oldModel = validateModelExists(model.getId());

        // 校验编码唯一性
        if (!isCodeUnique(model.getCode(), model.getId())) {
            throw new ServiceException("业务模型编码已存在");
        }

        // 更新数据
        businessModelMapper.updateById(model);

        // 如果字段结构发生变化，需要更新数据表
        if (!Objects.equals(oldModel.getConfig(), model.getConfig())) {
            updateModelTable(model);
        }

        // 更新缓存
        updateCache(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(Long id) {
        // 校验存在
        BusinessModelDO model = validateModelExists(id);

        // 删除数据
        businessModelMapper.deleteById(id);

        // 删除缓存
        deleteCache(model);
    }

    @Override
    public BusinessModelDO getModel(Long id) {
        // 先从缓存获取
        BusinessModelDO model = getModelFromCache(id);
        if (model != null) {
            return model;
        }

        // 从数据库获取
        model = businessModelMapper.selectById(id);
        
        // 更新缓存
        if (model != null) {
            updateCache(model);
        }

        return model;
    }

    @Override
    public BusinessModelDO getModelByCode(String code) {
        // 先从缓存获取
        BusinessModelDO model = getModelFromCacheByCode(code);
        if (model != null) {
            return model;
        }

        // 从数据库获取
        model = businessModelMapper.selectOne(new LambdaQueryWrapper<BusinessModelDO>()
                .eq(BusinessModelDO::getCode, code));

        // 更新缓存
        if (model != null) {
            updateCache(model);
        }

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
        // 分页查询
        Page<BusinessModelDO> page = businessModelMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapperX<BusinessModelDO>()
                        .likeIfPresent(BusinessModelDO::getName, name)
                        .eqIfPresent(BusinessModelDO::getStatus, status)
                        .orderByDesc(BusinessModelDO::getId));

        // 返回分页结果
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
        // TODO: 根据业务模型配置，动态创建数据表
        log.info("[initModelTable][开始初始化业务模型({})的数据表]", model.getCode());
    }

    @Override
    public void updateModelTable(BusinessModelDO model) {
        // TODO: 根据业务模型配置，动态更新数据表结构
        log.info("[updateModelTable][开始更新业务模型({})的数据表结构]", model.getCode());
    }

    private BusinessModelDO validateModelExists(Long id) {
        BusinessModelDO model = getModel(id);
        if (model == null) {
            throw new ServiceException("业务模型不存在");
        }
        return model;
    }

    private BusinessModelDO getModelFromCache(Long id) {
        return redisCache.getCacheObject(CACHE_KEY_PREFIX + "id:" + id);
    }

    private BusinessModelDO getModelFromCacheByCode(String code) {
        return redisCache.getCacheObject(CACHE_KEY_PREFIX + "code:" + code);
    }

    private void updateCache(BusinessModelDO model) {
        // 更新ID缓存
        redisCache.setCacheObject(CACHE_KEY_PREFIX + "id:" + model.getId(), model);
        // 更新编码缓存
        redisCache.setCacheObject(CACHE_KEY_PREFIX + "code:" + model.getCode(), model);
    }

    private void deleteCache(BusinessModelDO model) {
        // 删除ID缓存
        redisCache.deleteObject(CACHE_KEY_PREFIX + "id:" + model.getId());
        // 删除编码缓存
        redisCache.deleteObject(CACHE_KEY_PREFIX + "code:" + model.getCode());
    }
} 