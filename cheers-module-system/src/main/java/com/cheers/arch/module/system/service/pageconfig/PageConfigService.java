package com.cheers.arch.module.system.service.pageconfig;

import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigPageReqVO;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.pageconfig.PageConfigDO;

/**
 * 页面配置 Service 接口
 *
 * @author cheers
 */
public interface PageConfigService {

    /**
     * 创建页面配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPageConfig(@Valid PageConfigCreateReqVO createReqVO);

    /**
     * 更新页面配置
     *
     * @param updateReqVO 更新信息
     */
    void updatePageConfig(@Valid PageConfigUpdateReqVO updateReqVO);

    /**
     * 删除页面配置
     *
     * @param id 编号
     */
    void deletePageConfig(Long id);

    /**
     * 获得页面配置
     *
     * @param id 编号
     * @return 页面配置
     */
    PageConfigDO getPageConfig(Long id);

    /**
     * 根据配置键获得页面配置
     *
     * @param configKey 配置键
     * @return 页面配置
     */
    PageConfigDO getPageConfigByKey(String configKey);

    /**
     * 根据配置键和租户ID获得页面配置
     *
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 页面配置
     */
    PageConfigDO getPageConfigByKeyAndTenantId(String configKey, Long tenantId);

    /**
     * 获得页面配置分页
     *
     * @param pageReqVO 分页查询
     * @return 页面配置分页
     */
    PageResult<PageConfigDO> getPageConfigPage(PageConfigPageReqVO pageReqVO);

} 