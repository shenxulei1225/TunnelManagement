package cn.iocoder.yudao.module.system.service.region;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.RegionListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.RegionSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionDO;

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
