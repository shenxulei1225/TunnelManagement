package com.cheers.arch.module.system.service.tree;

import java.util.List;

import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigCreateReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigPageReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigRespVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigUpdateReqVO;

/**
 * 树形配置 Service 接口
 *
 * @author cheers
 */
public interface TreeConfigService {

    /**
     * 创建树形配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTreeConfig(@Valid TreeConfigCreateReqVO createReqVO);

    /**
     * 更新树形配置
     *
     * @param updateReqVO 更新信息
     */
    void updateTreeConfig(@Valid TreeConfigUpdateReqVO updateReqVO);

    /**
     * 删除树形配置
     *
     * @param id 编号
     */
    void deleteTreeConfig(Long id);

    /**
     * 获得树形配置
     *
     * @param id 编号
     * @return 树形配置
     */
    TreeConfigRespVO getTreeConfig(Long id);

    /**
     * 获得树形配置分页
     *
     * @param pageReqVO 分页查询
     * @return 树形配置分页
     */
    PageResult<TreeConfigRespVO> getTreeConfigPage(TreeConfigPageReqVO pageReqVO);

    /**
     * 根据场景获得树形配置
     *
     * @param scene 使用场景
     * @return 树形配置
     */
    TreeConfigRespVO getTreeConfigByScene(String scene);

    /**
     * 保存用户个人配置
     *
     * @param scene 场景标识
     * @param configName 配置名称
     * @param configJson 配置JSON
     */
    void saveUserTreeConfig(String scene, String configName, String configJson);

    /**
     * 加载用户个人配置
     *
     * @param scene 场景标识
     * @return 用户配置
     */
    TreeConfigRespVO loadUserTreeConfig(String scene);

    /**
     * 保存全局默认配置
     *
     * @param scene 场景标识
     * @param configName 配置名称
     * @param configJson 配置JSON
     */
    void saveGlobalTreeConfig(String scene, String configName, String configJson);

    /**
     * 获取场景下的所有配置
     *
     * @param scene 场景标识
     * @return 配置列表
     */
    List<TreeConfigRespVO> getTreeConfigListByScene(String scene);

    /**
     * 清除用户个人配置
     *
     * @param scene 场景标识
     */
    void clearUserTreeConfig(String scene);

} 