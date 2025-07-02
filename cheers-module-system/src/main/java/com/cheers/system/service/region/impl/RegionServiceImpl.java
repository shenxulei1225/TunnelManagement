package com.cheers.system.service.region.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.system.controller.admin.region.vo.RegionListReqVO;
import com.cheers.system.controller.admin.region.vo.RegionPageReqVO;
import com.cheers.system.controller.admin.region.vo.RegionSaveReqVO;
import com.cheers.system.controller.admin.region.vo.RegionImportResultVO;
import com.cheers.system.dal.dataobject.region.RegionDO;
import com.cheers.system.dal.mysql.region.RegionMapper;
import com.cheers.system.service.region.RegionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * 区域 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service("cheersRegionService")
@Validated
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Override
    public Long createRegion(RegionSaveReqVO createReqVO) {
        // 调试信息：打印接收到的数据
        log.info("[CHEERS-CREATE] 接收到的创建请求数据: {}", createReqVO);
        log.info("[CHEERS-CREATE] categoryId: {}", createReqVO.getCategoryId());
        
        // 插入
        RegionDO region = BeanUtils.toBean(createReqVO, RegionDO.class);
        log.info("[CHEERS-CREATE] 转换后的DO对象: {}", region);
        log.info("[CHEERS-CREATE] DO对象的categoryId: {}", region.getCategoryId());
        
        regionMapper.insert(region);
        // 返回
        return region.getId();
    }

    @Override
    public void updateRegion(RegionSaveReqVO updateReqVO) {
        // 调试信息：打印接收到的数据
        log.info("[CHEERS-UPDATE] 接收到的更新请求数据: {}", updateReqVO);
        log.info("[CHEERS-UPDATE] categoryId: {}", updateReqVO.getCategoryId());
        
        // 校验存在
        validateRegionExists(updateReqVO.getId());
        
        // 更新前先查询当前数据
        RegionDO beforeUpdate = regionMapper.selectById(updateReqVO.getId());
        log.info("[CHEERS-UPDATE] 更新前的数据: {}", beforeUpdate);
        
        // 更新
        RegionDO updateObj = BeanUtils.toBean(updateReqVO, RegionDO.class);
        log.info("[CHEERS-UPDATE] 转换后的DO对象: {}", updateObj);
        log.info("[CHEERS-UPDATE] DO对象的categoryId: {}", updateObj.getCategoryId());
        
        int updateResult = regionMapper.updateById(updateObj);
        log.info("[CHEERS-UPDATE] 数据库更新结果: {} 行受影响", updateResult);
        
        // 更新后再次查询验证
        RegionDO afterUpdate = regionMapper.selectById(updateReqVO.getId());
        log.info("[CHEERS-UPDATE] 更新后的数据: {}", afterUpdate);
        log.info("[CHEERS-UPDATE] 更新后的categoryId: {}", afterUpdate != null ? afterUpdate.getCategoryId() : "null");
        log.info("[CHEERS-UPDATE] 更新后的extraAttrs: {}", afterUpdate != null ? afterUpdate.getExtraAttrs() : "null");
    }

    @Override
    public void deleteRegion(Long id) {
        // 校验存在
        validateRegionExists(id);
        // 删除
        regionMapper.deleteById(id);
    }

    private void validateRegionExists(Long id) {
        if (regionMapper.selectById(id) == null) {
            throw new RuntimeException("区域不存在");
        }
    }

    @Override
    public RegionDO getRegion(Long id) {
        return regionMapper.selectById(id);
    }

    @Override
    public PageResult<RegionDO> getRegionPage(RegionPageReqVO pageReqVO) {
        return regionMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<RegionDO>()
                .likeIfPresent(RegionDO::getName, pageReqVO.getName())
                .eqIfPresent(RegionDO::getParentId, pageReqVO.getParentId())
                .eqIfPresent(RegionDO::getSort, pageReqVO.getSort())
                .eqIfPresent(RegionDO::getStatus, pageReqVO.getStatus())
                .betweenIfPresent(RegionDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByAsc(RegionDO::getSort));
    }

    @Override
    public List<RegionDO> getRegionList(RegionListReqVO listReqVO) {
        return regionMapper.selectListByConditions(listReqVO.getName(), listReqVO.getParentId(), 
                listReqVO.getSort(), listReqVO.getStatus());
    }

    // ==================== 批量导入相关方法实现 ====================

    @Override
    public void downloadImportTemplate(Long categoryId, HttpServletResponse response) throws IOException {
        log.info("[CHEERS-IMPORT] 下载导入模板, categoryId: {}", categoryId);
        // TODO: 实现导入模板下载功能
        throw new UnsupportedOperationException("导入模板下载功能待实现");
    }

    @Override
    public RegionImportResultVO importRegions(Long categoryId, MultipartFile file) throws IOException {
        log.info("[CHEERS-IMPORT] 批量导入区域数据, categoryId: {}, fileName: {}", categoryId, file.getOriginalFilename());
        // TODO: 实现批量导入功能
        throw new UnsupportedOperationException("批量导入功能待实现");
    }

    @Override
    public RegionImportResultVO getImportResult(String batchId) {
        log.info("[CHEERS-IMPORT] 获取导入结果, batchId: {}", batchId);
        // TODO: 实现获取导入结果功能
        throw new UnsupportedOperationException("获取导入结果功能待实现");
    }

} 