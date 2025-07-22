package com.cheers.arch.module.system.service.pagegenerator;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.BuildFrontendReqVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.BuildResultRespVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.BuildStatusRespVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.GeneratedPageRespVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.MenuConfigReqVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.MenuConfigRespVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.PageGeneratorReqVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.PageGeneratorRespVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.PageStatisticsRespVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.RouteConfigReqVO;
import com.cheers.arch.module.system.controller.admin.pagegenerator.vo.RouteConfigRespVO;

import org.springframework.web.multipart.MultipartFile;

/**
 * 页面生成器 Service 接口
 *
 * @author cheers
 */
public interface PageGeneratorService {

    /**
     * 生成页面
     *
     * @param reqVO 请求参数
     * @return 生成的页面信息
     */
    PageGeneratorRespVO generatePage(PageGeneratorReqVO reqVO);

    /**
     * 保存页面到文件系统
     *
     * @param pagePath 页面路径
     * @param pageCode 页面代码
     */
    void savePageToFileSystem(String pagePath, String pageCode);

    /**
     * 从文件系统加载页面
     *
     * @param pagePath 页面路径
     * @return 页面代码
     */
    String loadPageFromFileSystem(String pagePath);

    /**
     * 检查页面文件是否存在
     *
     * @param pagePath 页面路径
     * @return 是否存在
     */
    boolean checkPageFileExists(String pagePath);

    /**
     * 删除页面文件
     *
     * @param pagePath 页面路径
     */
    void deletePageFile(String pagePath);

    /**
     * 获取所有生成的页面
     *
     * @return 页面列表
     */
    List<GeneratedPageRespVO> getAllGeneratedPages();

    /**
     * 更新路由配置
     *
     * @param reqVO 路由配置请求
     */
    void updateRouteConfig(RouteConfigReqVO reqVO);

    /**
     * 更新菜单配置
     *
     * @param reqVO 菜单配置请求
     */
    void updateMenuConfig(MenuConfigReqVO reqVO);

    /**
     * 重新加载路由
     */
    void reloadRoutes();

    /**
     * 获取路由配置
     *
     * @return 路由配置列表
     */
    List<RouteConfigRespVO> getRouteConfig();

    /**
     * 获取菜单配置
     *
     * @return 菜单配置列表
     */
    List<MenuConfigRespVO> getMenuConfig();

    /**
     * 导出页面配置
     *
     * @param pageId 页面ID
     * @return 配置内容
     */
    String exportPageConfig(String pageId);

    /**
     * 导入页面配置
     *
     * @param configFile 配置文件
     */
    void importPageConfig(MultipartFile configFile);

    /**
     * 激活页面
     *
     * @param pageId 页面ID
     */
    void activatePage(String pageId);

    /**
     * 停用页面
     *
     * @param pageId 页面ID
     */
    void deactivatePage(String pageId);

    /**
     * 获取页面统计信息
     *
     * @return 统计信息
     */
    PageStatisticsRespVO getPageStatistics();

    /**
     * 构建前端包
     *
     * @param reqVO 构建请求
     * @return 构建结果
     */
    BuildResultRespVO buildFrontend(BuildFrontendReqVO reqVO);

    /**
     * 获取构建状态
     *
     * @param buildId 构建ID
     * @return 构建状态
     */
    BuildStatusRespVO getBuildStatus(String buildId);

} 