package com.cheers.arch.module.system.controller.admin.item;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.item.vo.ItemCreateReqVO;
import com.cheers.arch.module.system.controller.admin.item.vo.ItemRespVO;
import com.cheers.arch.module.system.controller.admin.item.vo.ItemUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.item.ItemDO;
import com.cheers.arch.module.system.service.item.ItemService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数据项 Controller
 */
@Tag(name = "管理后台 - 数据项")
@RestController
@RequestMapping("/system/item")
@Validated
public class ItemController {

    @Resource
    private ItemService itemService;

    @PostMapping("/create")
    @Operation(summary = "创建数据项")
    @PreAuthorize("@ss.hasPermission('system:item:create')")
    public CommonResult<Long> createItem(@Valid @RequestBody ItemCreateReqVO createReqVO) {
        ItemDO itemDO = BeanUtils.toBean(createReqVO, ItemDO.class);
        Long id = itemService.createItem(itemDO);
        return success(id);
    }

    @PutMapping("/update")
    @Operation(summary = "更新数据项")
    @PreAuthorize("@ss.hasPermission('system:item:update')")
    public CommonResult<Boolean> updateItem(@Valid @RequestBody ItemUpdateReqVO updateReqVO) {
        ItemDO itemDO = BeanUtils.toBean(updateReqVO, ItemDO.class);
        itemService.updateItem(itemDO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除数据项")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:item:delete')")
    public CommonResult<Boolean> deleteItem(@RequestParam("id") Long id) {
        itemService.deleteItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得数据项")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<ItemRespVO> getItem(@RequestParam("id") Long id) {
        ItemDO item = itemService.getItem(id);
        return success(BeanUtils.toBean(item, ItemRespVO.class));
    }

    @GetMapping("/get-by-code")
    @Operation(summary = "根据编码获得数据项")
    @Parameter(name = "code", description = "编码", required = true)
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<ItemRespVO> getItemByCode(@RequestParam("code") String code) {
        ItemDO item = itemService.getItemByCode(code);
        return success(BeanUtils.toBean(item, ItemRespVO.class));
    }

    @GetMapping("/list-by-type")
    @Operation(summary = "根据类型获得数据项列表")
    @Parameter(name = "itemType", description = "数据项类型", required = true)
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<List<ItemRespVO>> getItemListByType(@RequestParam("itemType") String itemType) {
        List<ItemDO> list = itemService.getItemListByItemType(itemType);
        return success(BeanUtils.toBean(list, ItemRespVO.class));
    }

    @GetMapping("/list-by-ids")
    @Operation(summary = "根据ID列表获得数据项列表")
    @Parameter(name = "ids", description = "ID列表", required = true)
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<List<ItemRespVO>> getItemListByIds(@RequestParam("ids") List<Long> ids) {
        List<ItemDO> list = itemService.getItemListByIds(ids);
        return success(BeanUtils.toBean(list, ItemRespVO.class));
    }

    // ========== 自定义字段相关接口 ==========

    @PostMapping("/set-field")
    @Operation(summary = "设置自定义字段值")
    @PreAuthorize("@ss.hasPermission('system:item:update')")
    public CommonResult<Boolean> setCustomFieldValue(
            @RequestParam("itemId") Long itemId,
            @RequestParam("fieldCode") String fieldCode,
            @RequestParam("value") Object value) {
        itemService.setCustomFieldValue(itemId, fieldCode, value);
        return success(true);
    }

    @GetMapping("/get-field")
    @Operation(summary = "获取自定义字段值")
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<Object> getCustomFieldValue(
            @RequestParam("itemId") Long itemId,
            @RequestParam("fieldCode") String fieldCode) {
        Object value = itemService.getCustomFieldValue(itemId, fieldCode);
        return success(value);
    }

    @GetMapping("/get-all-fields")
    @Operation(summary = "获取所有自定义字段值")
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<Map<String, Object>> getAllCustomFieldValues(@RequestParam("itemId") Long itemId) {
        Map<String, Object> fieldValues = itemService.getAllCustomFieldValues(itemId);
        return success(fieldValues);
    }

    @PostMapping("/set-all-fields")
    @Operation(summary = "设置所有自定义字段值")
    @PreAuthorize("@ss.hasPermission('system:item:update')")
    public CommonResult<Boolean> setAllCustomFieldValues(
            @RequestParam("itemId") Long itemId,
            @RequestBody Map<String, Object> fieldValues) {
        itemService.setAllCustomFieldValues(itemId, fieldValues);
        return success(true);
    }

    @GetMapping("/list-by-field")
    @Operation(summary = "根据自定义字段查询数据项")
    @PreAuthorize("@ss.hasPermission('system:item:query')")
    public CommonResult<List<ItemRespVO>> getItemListByCustomField(
            @RequestParam("itemType") String itemType,
            @RequestParam("fieldCode") String fieldCode,
            @RequestParam("value") Object value) {
        List<ItemDO> list = itemService.getItemListByCustomField(itemType, fieldCode, value);
        return success(BeanUtils.toBean(list, ItemRespVO.class));
    }
} 