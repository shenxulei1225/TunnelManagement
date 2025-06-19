package cn.iocoder.yudao.module.system.service.region;

import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionFieldDefDO;

import java.util.List;

public interface RegionFieldDefService {
    List<RegionFieldDefDO> getFieldDefs(String ownerType, Long categoryId);

    Long createFieldDef(String ownerType, RegionFieldDefDO bean);

    Boolean updateFieldDef(String ownerType, RegionFieldDefDO bean);

    Boolean deleteFieldDef(String ownerType, Long id);
}
