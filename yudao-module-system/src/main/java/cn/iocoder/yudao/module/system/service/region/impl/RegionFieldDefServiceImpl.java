package cn.iocoder.yudao.module.system.service.region.impl;

import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionFieldDefDO;
import cn.iocoder.yudao.module.system.dal.mysql.region.RegionFieldDefMapper;
import cn.iocoder.yudao.module.system.service.region.RegionFieldDefService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.List;

@Service
public class RegionFieldDefServiceImpl implements RegionFieldDefService {

    @Resource
    private RegionFieldDefMapper fieldDefMapper;

    @Override
    public List<RegionFieldDefDO> getFieldDefs(Integer regionType) {
        LambdaQueryWrapper<RegionFieldDefDO> wrapper = new LambdaQueryWrapper<>();
        if (regionType != null && regionType != 0) {
            wrapper.eq(RegionFieldDefDO::getRegionType, regionType).or();
        }
        wrapper.eq(RegionFieldDefDO::getRegionType, 0).orderByAsc(RegionFieldDefDO::getSort);
        return fieldDefMapper.selectList(wrapper);
    }

    @Override
    public Long createFieldDef(RegionFieldDefDO bean) {
        fieldDefMapper.insert(bean);
        return bean.getId();
    }

    @Override
    public Boolean updateFieldDef(RegionFieldDefDO bean) {
        return fieldDefMapper.updateById(bean) > 0;
    }

    @Override
    public Boolean deleteFieldDef(Long id) {
        return fieldDefMapper.deleteById(id) > 0;
    }
}
