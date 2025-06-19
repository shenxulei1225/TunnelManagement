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
    public List<RegionFieldDefDO> getFieldDefs(String ownerType, Long categoryId) {
        LambdaQueryWrapper<RegionFieldDefDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionFieldDefDO::getOwnerType, ownerType);
        if (categoryId != null && categoryId != 0) {
            wrapper.eq(RegionFieldDefDO::getCategoryId, categoryId).or();
        }
        wrapper.eq(RegionFieldDefDO::getCategoryId, 0L).orderByAsc(RegionFieldDefDO::getSort);
        return fieldDefMapper.selectList(wrapper);
    }

    @Override
    public Long createFieldDef(String ownerType, RegionFieldDefDO bean) {
        bean.setOwnerType(ownerType);
        fieldDefMapper.insert(bean);
        return bean.getId();
    }

    @Override
    public Boolean updateFieldDef(String ownerType, RegionFieldDefDO bean) {
        bean.setOwnerType(ownerType);
        return fieldDefMapper.updateById(bean) > 0;
    }

    @Override
    public Boolean deleteFieldDef(String ownerType, Long id) {
        return fieldDefMapper.deleteById(id) > 0;
    }
}
