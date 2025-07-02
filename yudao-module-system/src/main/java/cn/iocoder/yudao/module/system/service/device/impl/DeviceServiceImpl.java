package cn.iocoder.yudao.module.system.service.device.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.system.dal.mysql.device.DeviceMapper;
import cn.iocoder.yudao.module.system.service.device.DeviceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 设备档案 Service 实现类
 *
 * @author 系统管理员
 */
@Service
@Validated
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public Long createDevice(DeviceSaveReqVO createReqVO) {
        // 校验设备编号唯一性
        validateDeviceCodeUnique(createReqVO.getDeviceCode(), null);
        
        // 校验父设备
        if (createReqVO.getParentId() != null && !createReqVO.getParentId().equals(DeviceDO.PARENT_ID_ROOT)) {
            validateParentDevice(createReqVO.getParentId());
        }

        // 插入
        DeviceDO device = BeanUtils.toBean(createReqVO, DeviceDO.class);
        deviceMapper.insert(device);
        // 返回
        return device.getId();
    }

    @Override
    public void updateDevice(DeviceSaveReqVO updateReqVO) {
        // 校验存在
        validateDeviceExists(updateReqVO.getId());
        
        // 校验设备编号唯一性
        validateDeviceCodeUnique(updateReqVO.getDeviceCode(), updateReqVO.getId());
        
        // 校验父设备
        if (updateReqVO.getParentId() != null && !updateReqVO.getParentId().equals(DeviceDO.PARENT_ID_ROOT)) {
            validateParentDevice(updateReqVO.getParentId());
            // 不能设置自己为父设备
            if (updateReqVO.getParentId().equals(updateReqVO.getId())) {
                throw exception(DEVICE_PARENT_ERROR);
            }
        }

        // 更新
        DeviceDO updateObj = BeanUtils.toBean(updateReqVO, DeviceDO.class);
        deviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevice(Long id) {
        // 校验存在
        validateDeviceExists(id);
        
        // 校验是否有子设备
        List<DeviceDO> childDevices = deviceMapper.selectByParentId(id);
        if (!childDevices.isEmpty()) {
            throw exception(DEVICE_EXITS_CHILDREN);
        }
        
        // 删除
        deviceMapper.deleteById(id);
    }

    private void validateDeviceExists(Long id) {
        if (deviceMapper.selectById(id) == null) {
            throw exception(DEVICE_NOT_EXISTS);
        }
    }

    @Override
    public DeviceDO getDevice(Long id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public PageResult<DeviceDO> getDevicePage(DeviceListReqVO pageReqVO) {
        return deviceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceDO> getDeviceList(DeviceListReqVO listReqVO) {
        return deviceMapper.selectList(listReqVO);
    }

    @Override
    public DeviceDO getDeviceByCode(String deviceCode) {
        return deviceMapper.selectByDeviceCode(deviceCode);
    }

    @Override
    public List<DeviceDO> getChildDevices(Long parentId) {
        return deviceMapper.selectByParentId(parentId);
    }

    @Override
    public List<DeviceDO> getDevicesByRegion(Long regionId) {
        return deviceMapper.selectByRegionId(regionId);
    }

    @Override
    public void validateDeviceCodeUnique(String deviceCode, Long excludeId) {
        DeviceDO device = deviceMapper.selectByDeviceCode(deviceCode);
        if (device == null) {
            return;
        }
        // 如果 excludeId 为空，说明不需要排除任何编号，那么只要存在就是重复
        if (excludeId == null) {
            throw exception(DEVICE_CODE_DUPLICATE, deviceCode);
        }
        // 如果有设备，但是不是排除的 ID 对应的设备，说明重复
        if (!device.getId().equals(excludeId)) {
            throw exception(DEVICE_CODE_DUPLICATE, deviceCode);
        }
    }

    @Override
    public void validateParentDevice(Long parentId) {
        if (parentId == null || parentId.equals(DeviceDO.PARENT_ID_ROOT)) {
            return;
        }
        DeviceDO parentDevice = deviceMapper.selectById(parentId);
        if (parentDevice == null) {
            throw exception(DEVICE_PARENT_NOT_EXISTS);
        }
    }
} 