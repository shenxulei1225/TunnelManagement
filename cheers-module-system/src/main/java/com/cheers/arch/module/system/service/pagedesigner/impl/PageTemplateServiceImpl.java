package com.cheers.arch.module.system.service.pagedesigner.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageTemplateCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageTemplateRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageTemplateUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.pagedesigner.PageTemplateDO;
import com.cheers.arch.module.system.dal.mysql.pagedesigner.PageTemplateMapper;
import com.cheers.arch.module.system.service.pagedesigner.PageTemplateService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 页面模板 Service 实现类
 */
@Service
@Validated
@Slf4j
public class PageTemplateServiceImpl implements PageTemplateService {

    @Resource
    private PageTemplateMapper pageTemplateMapper;

    @Override
    public String createPageTemplate(PageTemplateCreateReqVO createReqVO) {
        // 创建页面模板
        PageTemplateDO pageTemplate = BeanUtils.toBean(createReqVO, PageTemplateDO.class);
        pageTemplate.setId(UUID.randomUUID().toString());
        pageTemplate.setCreateTime(LocalDateTime.now());
        pageTemplate.setUpdateTime(LocalDateTime.now());
        
        // TenantBaseDO会自动设置租户ID
        pageTemplateMapper.insert(pageTemplate);
        
        return pageTemplate.getId();
    }

    @Override
    public void updatePageTemplate(PageTemplateUpdateReqVO updateReqVO) {
        // 更新页面模板
        PageTemplateDO updateObj = BeanUtils.toBean(updateReqVO, PageTemplateDO.class);
        updateObj.setUpdateTime(LocalDateTime.now());
        
        // TenantBaseDO会自动处理租户隔离
        pageTemplateMapper.updateById(updateObj);
    }

    @Override
    public void deletePageTemplate(String id) {
        // 删除页面模板 - TenantBaseDO会自动处理租户隔离
        pageTemplateMapper.deleteById(id);
    }

    @Override
    public PageTemplateRespVO getPageTemplate(String id) {
        // TenantBaseDO会自动处理租户隔离
        PageTemplateDO pageTemplate = pageTemplateMapper.selectById(id);
        return BeanUtils.toBean(pageTemplate, PageTemplateRespVO.class);
    }

    @Override
    public List<PageTemplateRespVO> getPageTemplateList() {
        // TenantBaseDO会自动处理租户隔离
        LambdaQueryWrapper<PageTemplateDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PageTemplateDO::getSort)
               .orderByDesc(PageTemplateDO::getCreateTime);
        List<PageTemplateDO> list = pageTemplateMapper.selectList(wrapper);
        return list.stream()
                .map(item -> BeanUtils.toBean(item, PageTemplateRespVO.class))
                .toList();
    }

    @Override
    public List<PageTemplateRespVO> getPageTemplateListByType(String templateType) {
        // TenantBaseDO会自动处理租户隔离
        LambdaQueryWrapper<PageTemplateDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PageTemplateDO::getTemplateType, templateType)
               .eq(PageTemplateDO::getStatus, "active")
               .orderByAsc(PageTemplateDO::getSort)
               .orderByDesc(PageTemplateDO::getCreateTime);
        List<PageTemplateDO> list = pageTemplateMapper.selectList(wrapper);
        return list.stream()
                .map(item -> BeanUtils.toBean(item, PageTemplateRespVO.class))
                .toList();
    }
} 