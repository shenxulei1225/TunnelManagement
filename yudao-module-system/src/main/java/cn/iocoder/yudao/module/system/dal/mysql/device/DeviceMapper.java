package cn.iocoder.yudao.module.system.dal.mysql.device;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceListReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.device.DeviceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备档案 Mapper
 *
 * @author 系统管理员
 */
@Mapper
public interface DeviceMapper extends BaseMapperX<DeviceDO> {

    default PageResult<DeviceDO> selectPage(DeviceListReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceDO>()
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .likeIfPresent(DeviceDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(DeviceDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DeviceDO::getDeviceTypeId, reqVO.getDeviceTypeId())
                .eqIfPresent(DeviceDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(DeviceDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(DeviceDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DeviceDO::getResponsibleUserId, reqVO.getResponsibleUserId())
                .likeIfPresent(DeviceDO::getManufacturer, reqVO.getManufacturer())
                .likeIfPresent(DeviceDO::getModel, reqVO.getModel())
                .likeIfPresent(DeviceDO::getSerialNumber, reqVO.getSerialNumber())
                .betweenIfPresent(DeviceDO::getPurchaseDate, reqVO.getPurchaseDate())
                .betweenIfPresent(DeviceDO::getInstallDate, reqVO.getInstallDate())
                .betweenIfPresent(DeviceDO::getWarrantyExpireDate, reqVO.getWarrantyExpireDate())
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceDO::getId));
    }

    default List<DeviceDO> selectList(DeviceListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceDO>()
                .likeIfPresent(DeviceDO::getName, reqVO.getName())
                .likeIfPresent(DeviceDO::getDeviceCode, reqVO.getDeviceCode())
                .eqIfPresent(DeviceDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DeviceDO::getDeviceTypeId, reqVO.getDeviceTypeId())
                .eqIfPresent(DeviceDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(DeviceDO::getRegionId, reqVO.getRegionId())
                .eqIfPresent(DeviceDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DeviceDO::getResponsibleUserId, reqVO.getResponsibleUserId())
                .likeIfPresent(DeviceDO::getManufacturer, reqVO.getManufacturer())
                .likeIfPresent(DeviceDO::getModel, reqVO.getModel())
                .likeIfPresent(DeviceDO::getSerialNumber, reqVO.getSerialNumber())
                .betweenIfPresent(DeviceDO::getPurchaseDate, reqVO.getPurchaseDate())
                .betweenIfPresent(DeviceDO::getInstallDate, reqVO.getInstallDate())
                .betweenIfPresent(DeviceDO::getWarrantyExpireDate, reqVO.getWarrantyExpireDate())
                .betweenIfPresent(DeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceDO::getId));
    }

    default DeviceDO selectByDeviceCode(String deviceCode) {
        return selectOne(DeviceDO::getDeviceCode, deviceCode);
    }

    default List<DeviceDO> selectByParentId(Long parentId) {
        return selectList(DeviceDO::getParentId, parentId);
    }

    default List<DeviceDO> selectByRegionId(Long regionId) {
        return selectList(DeviceDO::getRegionId, regionId);
    }
} 