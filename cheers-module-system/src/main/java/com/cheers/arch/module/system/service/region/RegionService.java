package com.cheers.arch.module.system.service.region;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionListReqVO;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;

import java.util.List;

/**
 * 区域 Service
 */
public interface RegionService {

    Long createRegion(RegionSaveReqVO createReqVO);

    Boolean updateRegion(RegionSaveReqVO updateReqVO);

    Boolean deleteRegion(Long id);

    RegionDO getRegion(Long id);

    PageResult<RegionDO> getRegionPage(RegionListReqVO pageReqVO);

    List<RegionDO> getRegionList(RegionListReqVO listReqVO);
}
