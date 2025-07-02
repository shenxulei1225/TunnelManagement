package cn.iocoder.yudao.module.system.controller.admin.device;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceListReqVO;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceRespVO;
import cn.iocoder.yudao.module.system.controller.admin.device.vo.DeviceSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.module.system.service.device.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 设备档案")
@RestController
@RequestMapping("/system/device")
@Validated
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @PostMapping("/create")
    @Operation(summary = "创建设备档案")
    @PreAuthorize("@ss.hasPermission('system:device:create')")
    public CommonResult<Long> createDevice(@Valid @RequestBody DeviceSaveReqVO createReqVO) {
        Long id = deviceService.createDevice(createReqVO);
        return success(id);
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备档案")
    @PreAuthorize("@ss.hasPermission('system:device:update')")
    public CommonResult<Boolean> updateDevice(@Valid @RequestBody DeviceSaveReqVO updateReqVO) {
        deviceService.updateDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备档案")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:device:delete')")
    public CommonResult<Boolean> deleteDevice(@RequestParam("id") Long id) {
        deviceService.deleteDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备档案")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:device:query')")
    public CommonResult<DeviceRespVO> getDevice(@RequestParam("id") Long id) {
        DeviceDO device = deviceService.getDevice(id);
        DeviceRespVO vo = BeanUtils.toBean(device, DeviceRespVO.class);
        // 手动拷贝 Map，BeanUtils 对泛型 Map 可能不生效
        if (device != null) {
            vo.setExtraAttrs(device.getExtraAttrs());
        }
        return success(vo);
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备档案分页")
    @PreAuthorize("@ss.hasPermission('system:device:query')")
    public CommonResult<PageResult<DeviceRespVO>> getDevicePage(@Valid DeviceListReqVO pageReqVO) {
        PageResult<DeviceDO> pageResult = deviceService.getDevicePage(pageReqVO);
        PageResult<DeviceRespVO> result = BeanUtils.toBean(pageResult, DeviceRespVO.class);
        // 手动处理 extraAttrs 字段
        if (result.getList() != null) {
            for (int i = 0; i < result.getList().size(); i++) {
                DeviceRespVO vo = result.getList().get(i);
                DeviceDO original = pageResult.getList().get(i);
                vo.setExtraAttrs(original.getExtraAttrs());
            }
        }
        return success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "获得设备档案列表")
    @PreAuthorize("@ss.hasPermission('system:device:query')")
    public CommonResult<List<DeviceRespVO>> getDeviceList(@Valid DeviceListReqVO listReqVO) {
        List<DeviceDO> list = deviceService.getDeviceList(listReqVO);
        List<DeviceRespVO> result = BeanUtils.toBean(list, DeviceRespVO.class);
        // 手动处理 extraAttrs 字段
        if (result != null && list != null) {
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setExtraAttrs(list.get(i).getExtraAttrs());
            }
        }
        return success(result);
    }

    @GetMapping("/export")
    @Operation(summary = "导出设备档案")
    @PreAuthorize("@ss.hasPermission('system:device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDevice(@Valid DeviceListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<DeviceDO> list = deviceService.getDeviceList(listReqVO);
        List<DeviceRespVO> result = BeanUtils.toBean(list, DeviceRespVO.class);
        // 手动处理 extraAttrs 字段
        if (result != null && list != null) {
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setExtraAttrs(list.get(i).getExtraAttrs());
            }
        }
        // 导出 Excel
        ExcelUtils.write(response, "设备档案.xls", "数据", DeviceRespVO.class, result);
    }

    @GetMapping("/children")
    @Operation(summary = "获得子设备列表")
    @Parameter(name = "parentId", description = "父设备ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:device:query')")
    public CommonResult<List<DeviceRespVO>> getChildDevices(@RequestParam("parentId") Long parentId) {
        List<DeviceDO> list = deviceService.getChildDevices(parentId);
        List<DeviceRespVO> result = BeanUtils.toBean(list, DeviceRespVO.class);
        // 手动处理 extraAttrs 字段
        if (result != null && list != null) {
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setExtraAttrs(list.get(i).getExtraAttrs());
            }
        }
        return success(result);
    }

    @GetMapping("/by-region")
    @Operation(summary = "根据区域获得设备列表")
    @Parameter(name = "regionId", description = "区域ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:device:query')")
    public CommonResult<List<DeviceRespVO>> getDevicesByRegion(@RequestParam("regionId") Long regionId) {
        List<DeviceDO> list = deviceService.getDevicesByRegion(regionId);
        List<DeviceRespVO> result = BeanUtils.toBean(list, DeviceRespVO.class);
        // 手动处理 extraAttrs 字段
        if (result != null && list != null) {
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setExtraAttrs(list.get(i).getExtraAttrs());
            }
        }
        return success(result);
    }
} 