package cn.iocoder.yudao.module.system.service.region.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.RegionListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.RegionSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionDO;
import cn.iocoder.yudao.module.system.dal.mysql.region.RegionMapper;
import cn.iocoder.yudao.module.system.service.region.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Override
    public Long createRegion(RegionSaveReqVO createReqVO) {
        RegionDO region = BeanUtils.toBean(createReqVO, RegionDO.class);
        regionMapper.insert(region);
        return region.getId();
    }

    @Override
    public Boolean updateRegion(RegionSaveReqVO updateReqVO) {
        RegionDO region = BeanUtils.toBean(updateReqVO, RegionDO.class);
        return regionMapper.updateById(region) > 0;
    }

    @Override
    public Boolean deleteRegion(Long id) {
        return regionMapper.deleteById(id) > 0;
    }

    @Override
    public RegionDO getRegion(Long id) {
        return regionMapper.selectById(id);
    }

    @Override
    public PageResult<RegionDO> getRegionPage(RegionListReqVO pageReqVO) {
        Page<RegionDO> mpPage = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        LambdaQueryWrapper<RegionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(pageReqVO.getName() != null, RegionDO::getName, pageReqVO.getName());
        regionMapper.selectPage(mpPage, wrapper);
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

    @Override
    public List<RegionDO> getRegionList(RegionListReqVO listReqVO) {
        return regionMapper.selectList(listReqVO);
    }
}
