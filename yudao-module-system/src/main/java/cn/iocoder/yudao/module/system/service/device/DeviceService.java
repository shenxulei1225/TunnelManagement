package cn.iocoder.yudao.module.system.service.device;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.device.DeviceDO;

import java.util.List;

/**
 * 设备档案 Service 接口
 *
 * @author 系统管理员
 */
public interface DeviceService {

    /**
     * 创建设备档案
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDevice(DeviceSaveReqVO createReqVO);

    /**
     * 更新设备档案
     *
     * @param updateReqVO 更新信息
     */
    void updateDevice(DeviceSaveReqVO updateReqVO);

    /**
     * 删除设备档案
     *
     * @param id 编号
     */
    void deleteDevice(Long id);

    /**
     * 获得设备档案
     *
     * @param id 编号
     * @return 设备档案
     */
    DeviceDO getDevice(Long id);

    /**
     * 获得设备档案分页
     *
     * @param pageReqVO 分页查询
     * @return 设备档案分页
     */
    PageResult<DeviceDO> getDevicePage(DeviceListReqVO pageReqVO);

    /**
     * 获得设备档案列表, 用于 Excel 导出
     *
     * @param listReqVO 查询条件
     * @return 设备档案列表
     */
    List<DeviceDO> getDeviceList(DeviceListReqVO listReqVO);

    /**
     * 根据设备编号获取设备
     *
     * @param deviceCode 设备编号
     * @return 设备档案
     */
    DeviceDO getDeviceByCode(String deviceCode);

    /**
     * 获取子设备列表
     *
     * @param parentId 父设备ID
     * @return 子设备列表
     */
    List<DeviceDO> getChildDevices(Long parentId);

    /**
     * 根据区域ID获取设备列表
     *
     * @param regionId 区域ID
     * @return 设备列表
     */
    List<DeviceDO> getDevicesByRegion(Long regionId);

    /**
     * 校验设备编号是否存在
     *
     * @param deviceCode 设备编号
     * @param excludeId 排除的设备ID
     */
    void validateDeviceCodeUnique(String deviceCode, Long excludeId);

    /**
     * 校验父设备是否存在
     *
     * @param parentId 父设备ID
     */
    void validateParentDevice(Long parentId);
} 