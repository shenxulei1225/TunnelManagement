package cn.iocoder.yudao.module.system.service.region.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.RegionListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.RegionSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionDO;
import cn.iocoder.yudao.module.system.dal.dataobject.field.FieldDefDO;
import cn.iocoder.yudao.module.system.service.field.FieldDefService;
import cn.iocoder.yudao.module.system.util.FieldFormulaUtils;
import cn.iocoder.yudao.module.system.dal.mysql.region.RegionMapper;
import cn.iocoder.yudao.module.system.service.region.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "system.region.service", name = "enabled", havingValue = "true", matchIfMissing = false)
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Resource
    private FieldDefService fieldDefService;

    private static final String BIZ_TYPE = "REGION";



    @Override
    public Long createRegion(RegionSaveReqVO createReqVO) {
        log.info("[createRegion] request.extraAttrs = {}", createReqVO.getExtraAttrs());
        // 计算派生字段
        List<FieldDefDO> defs = fieldDefService.getFieldDefListByBizType(BIZ_TYPE);
        FieldFormulaUtils.applyComputedFields(createReqVO.getExtraAttrs(), defs);
    
        RegionDO region = BeanUtils.toBean(createReqVO, RegionDO.class);
        log.info("[createRegion] converted RegionDO = {}", region);
        
        regionMapper.insert(region);
        return region.getId();
    }

    @Override
    public Boolean updateRegion(RegionSaveReqVO updateReqVO) {
        log.info("[updateRegion] request.extraAttrs = {}", updateReqVO.getExtraAttrs());
        // 计算派生字段
        List<FieldDefDO> defs = fieldDefService.getFieldDefListByBizType(BIZ_TYPE);
        FieldFormulaUtils.applyComputedFields(updateReqVO.getExtraAttrs(), defs);
    
        RegionDO region = BeanUtils.toBean(updateReqVO, RegionDO.class);
        log.info("[updateRegion] converted RegionDO = {}", region);
        
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
