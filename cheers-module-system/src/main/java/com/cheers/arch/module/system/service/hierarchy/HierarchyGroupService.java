package com.cheers.arch.module.system.service.hierarchy;

import java.util.List;

import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupCreateReqVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupFlatVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupPageReqVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupTreeVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupDO;

/**
 * 系统分级组 Service 接口
 *
 * @author cheers
 */
public interface HierarchyGroupService {

    /**
     * 创建系统分级组
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createHierarchyGroup(@Valid HierarchyGroupCreateReqVO createReqVO);

    /**
     * 更新系统分级组
     *
     * @param updateReqVO 更新信息
     */
    void updateHierarchyGroup(@Valid HierarchyGroupUpdateReqVO updateReqVO);

    /**
     * 删除系统分级组
     *
     * @param id 编号
     */
    void deleteHierarchyGroup(Long id);

    /**
     * 获得系统分级组
     *
     * @param id 编号
     * @return 系统分级组
     */
    HierarchyGroupDO getHierarchyGroup(Long id);

    /**
     * 获得系统分级组列表
     *
     * @param ids 编号
     * @return 系统分级组列表
     */
    List<HierarchyGroupDO> getHierarchyGroupList(List<Long> ids);

    /**
     * 获得系统分级组分页
     *
     * @param pageReqVO 分页查询
     * @return 系统分级组分页
     */
    PageResult<HierarchyGroupDO> getHierarchyGroupPage(HierarchyGroupPageReqVO pageReqVO);

    /**
     * 根据编码获取系统分级组
     *
     * @param code 编码
     * @return 系统分级组
     */
    HierarchyGroupDO getHierarchyGroupByCode(String code);

    /**
     * 根据父ID获取子分级组列表
     *
     * @param parentId 父ID
     * @return 子分级组列表
     */
    List<HierarchyGroupDO> getHierarchyGroupListByParentId(Long parentId);

    /**
     * 根据路径获取分级组列表
     *
     * @param path 路径
     * @return 分级组列表
     */
    List<HierarchyGroupDO> getHierarchyGroupListByPath(String path);

    /**
     * 获得系统分级组树形数据
     * 返回单个根节点，避免数据冗余，优化 Element Plus Tree 组件性能
     *
     * @return 系统分级组树形数据
     */
    HierarchyGroupTreeVO getHierarchyGroupTree();

    /**
     * 获得系统分级组扁平化数据（避免数据冗余）
     * 返回扁平化的数据，前端可以自行构建树形结构
     *
     * @return 系统分级组扁平化数据
     */
    List<HierarchyGroupFlatVO> getHierarchyGroupFlatList();

    /**
     * 根据用途类型获取分级组列表
     *
     * @param usageType 用途类型：FIELD(字段分组), BUSINESS(业务分组)
     * @return 分级组列表
     */
    List<HierarchyGroupDO> getHierarchyGroupListByUsageType(String usageType);

    /**
     * 根据用途类型和分组类型获取分级组列表
     *
     * @param usageType 用途类型：FIELD(字段分组), BUSINESS(业务分组)
     * @param groupType 分组类型：EQUIPMENT(设备), PERSONNEL(人员)等
     * @return 分级组列表
     */
    List<HierarchyGroupDO> getHierarchyGroupListByUsageTypeAndGroupType(String usageType, String groupType);

    /**
     * 根据用途类型获取分级组树形数据
     *
     * @param usageType 用途类型：FIELD(字段分组), BUSINESS(业务分组)
     * @return 分级组树形数据
     */
    HierarchyGroupTreeVO getHierarchyGroupTreeByUsageType(String usageType);

} 