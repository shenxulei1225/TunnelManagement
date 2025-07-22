package com.cheers.arch.module.system.service.pagedesigner;

import java.util.List;

import jakarta.validation.Valid;

import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageTemplateCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageTemplateRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageTemplateUpdateReqVO;

/**
 * 页面模板 Service 接口
 *
 * @author cheers
 */
public interface PageTemplateService {

    /**
     * 创建页面模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createPageTemplate(@Valid PageTemplateCreateReqVO createReqVO);

    /**
     * 更新页面模板
     *
     * @param updateReqVO 更新信息
     */
    void updatePageTemplate(@Valid PageTemplateUpdateReqVO updateReqVO);

    /**
     * 删除页面模板
     *
     * @param id 编号
     */
    void deletePageTemplate(String id);

    /**
     * 获得页面模板
     *
     * @param id 编号
     * @return 页面模板
     */
    PageTemplateRespVO getPageTemplate(String id);

    /**
     * 获得页面模板列表
     *
     * @return 页面模板列表
     */
    List<PageTemplateRespVO> getPageTemplateList();

    /**
     * 根据模板类型获取模板列表
     *
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<PageTemplateRespVO> getPageTemplateListByType(String templateType);

} 