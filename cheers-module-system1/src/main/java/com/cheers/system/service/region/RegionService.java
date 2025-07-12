package com.cheers.arch.module.system.service.region;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionListReqVO;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionPageReqVO;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionSaveReqVO;
import com.cheers.arch.module.system.controller.admin.region.vo.RegionImportResultVO;
import com.cheers.arch.module.system.controller.admin.region.vo.TreeRegionVO;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 区域 Service 接口
 *
 * @author 芋道源码
 */
public interface RegionService {

    /**
     * 创建区域
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRegion(RegionSaveReqVO createReqVO);

    /**
     * 更新区域
     *
     * @param updateReqVO 更新信息
     */
    void updateRegion(RegionSaveReqVO updateReqVO);

    /**
     * 删除区域
     *
     * @param id 编号
     */
    void deleteRegion(Long id);

    /**
     * 获得区域
     *
     * @param id 编号
     * @return 区域
     */
    RegionDO getRegion(Long id);

    /**
     * 获得区域分页
     *
     * @param pageReqVO 分页查询
     * @return 区域分页
     */
    PageResult<RegionDO> getRegionPage(RegionPageReqVO pageReqVO);

    /**
     * 获得区域列表, 用于 Excel 导出
     *
     * @param listReqVO 查询条件
     * @return 区域列表
     */
    List<RegionDO> getRegionList(RegionListReqVO listReqVO);

    /**
     * 获取区域树形结构
     *
     * @param status 状态
     * @return 区域树形列表
     */
    List<TreeRegionVO> getRegionTree(Integer status);

    // ==================== 批量导入相关方法 ====================

    /**
     * 下载导入模板
     *
     * @param categoryId 分类ID
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    void downloadImportTemplate(Long categoryId, HttpServletResponse response) throws IOException;

    /**
     * 批量导入区域数据
     *
     * @param categoryId 分类ID
     * @param file 上传的Excel文件
     * @return 导入结果
     * @throws IOException IO异常
     */
    RegionImportResultVO importRegions(Long categoryId, MultipartFile file) throws IOException;

    /**
     * 获取导入结果
     *
     * @param batchId 批次ID
     * @return 导入结果
     */
    RegionImportResultVO getImportResult(String batchId);

}