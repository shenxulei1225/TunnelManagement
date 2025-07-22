package com.cheers.arch.module.system.service.pagedesigner;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.BuildResultRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.BuildStatusRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageDesignCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageDesignRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageDesignUpdateReqVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.UploadPageReqVO;

/**
 * 页面设计器 Service 接口
 *
 * @author cheers
 */
public interface PageDesignerService {

    /**
     * 创建页面设计
     *
     * @param createReqVO 创建请求
     * @return 页面设计响应
     */
    PageDesignRespVO createPageDesign(PageDesignCreateReqVO createReqVO);

    /**
     * 更新页面设计
     *
     * @param updateReqVO 更新请求
     */
    void updatePageDesign(PageDesignUpdateReqVO updateReqVO);

    /**
     * 删除页面设计
     *
     * @param id 页面设计ID
     */
    void deletePageDesign(String id);

    /**
     * 获取页面设计
     *
     * @param id 页面设计ID
     * @return 页面设计响应
     */
    PageDesignRespVO getPageDesign(String id);

    /**
     * 获取页面设计列表
     *
     * @return 页面设计列表
     */
    List<PageDesignRespVO> getPageDesignList();

    /**
     * 上传并构建页面
     *
     * @param reqVO 上传请求
     * @return 构建结果
     */
    BuildResultRespVO uploadAndBuild(UploadPageReqVO reqVO);

    /**
     * 获取构建状态
     *
     * @param buildId 构建ID
     * @return 构建状态
     */
    BuildStatusRespVO getBuildStatus(String buildId);

    /**
     * 部署页面
     *
     * @param pageId 页面ID
     */
    void deployPage(String pageId);

} 