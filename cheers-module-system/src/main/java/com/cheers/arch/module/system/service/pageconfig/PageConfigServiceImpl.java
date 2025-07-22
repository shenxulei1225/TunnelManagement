package com.cheers.arch.module.system.service.pageconfig;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.PAGE_CONFIG_NOT_EXISTS;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigPageReqVO;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigUpdateReqVO;
import com.cheers.arch.module.system.convert.pageconfig.PageConfigConvert;
import com.cheers.arch.module.system.dal.dataobject.pageconfig.PageConfigDO;
import com.cheers.arch.module.system.dal.mysql.pageconfig.PageConfigMapper;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 页面配置 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
public class PageConfigServiceImpl implements PageConfigService {

    @Resource
    private PageConfigMapper pageConfigMapper;

    @Override
    public Long createPageConfig(PageConfigCreateReqVO createReqVO) {
        // 插入
        PageConfigDO pageConfig = PageConfigConvert.INSTANCE.convert(createReqVO);
        // 设置租户ID
        pageConfig.setTenantId(TenantContextHolder.getTenantId());
        pageConfigMapper.insert(pageConfig);
        // 返回
        return pageConfig.getId();
    }

    @Override
    public void updatePageConfig(PageConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validatePageConfigExists(updateReqVO.getId());
        // 更新
        PageConfigDO updateObj = PageConfigConvert.INSTANCE.convert(updateReqVO);
        pageConfigMapper.updateById(updateObj);
    }

    @Override
    public void deletePageConfig(Long id) {
        // 校验存在
        validatePageConfigExists(id);
        // 删除
        pageConfigMapper.deleteById(id);
    }

    private void validatePageConfigExists(Long id) {
        if (pageConfigMapper.selectById(id) == null) {
            throw exception(PAGE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public PageConfigDO getPageConfig(Long id) {
        return pageConfigMapper.selectById(id);
    }

    @Override
    public PageConfigDO getPageConfigByKey(String configKey) {
        return pageConfigMapper.selectByConfigKeyAndTenantId(configKey, TenantContextHolder.getTenantId());
    }

    @Override
    public PageConfigDO getPageConfigByKeyAndTenantId(String configKey, Long tenantId) {
        return pageConfigMapper.selectByConfigKeyAndTenantId(configKey, tenantId);
    }

    @Override
    public PageResult<PageConfigDO> getPageConfigPage(PageConfigPageReqVO pageReqVO) {
        return pageConfigMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<PageConfigDO>()
                .likeIfPresent(PageConfigDO::getConfigKey, pageReqVO.getConfigKey())
                .likeIfPresent(PageConfigDO::getBusinessName, pageReqVO.getBusinessName())
                .likeIfPresent(PageConfigDO::getPageTitle, pageReqVO.getPageTitle())
                .eqIfPresent(PageConfigDO::getTemplateType, pageReqVO.getTemplateType())
                .eqIfPresent(PageConfigDO::getStatus, pageReqVO.getStatus())
                .betweenIfPresent(PageConfigDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByDesc(PageConfigDO::getId));
    }

} 