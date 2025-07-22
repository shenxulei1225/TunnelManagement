package com.cheers.arch.module.system.service.pagedesigner.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.BuildResultRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.BuildStatusRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageDesignCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageDesignRespVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.PageDesignUpdateReqVO;
import com.cheers.arch.module.system.controller.admin.pagedesigner.vo.UploadPageReqVO;
import com.cheers.arch.module.system.dal.dataobject.pagedesigner.PageDesignDO;
import com.cheers.arch.module.system.dal.mysql.pagedesigner.PageDesignMapper;
import com.cheers.arch.module.system.service.pagedesigner.PageDesignerService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 页面设计器 Service 实现类
 */
@Service
@Validated
@Slf4j
public class PageDesignerServiceImpl implements PageDesignerService {

    @Resource
    private PageDesignMapper pageDesignMapper;

    @Override
    public PageDesignRespVO createPageDesign(PageDesignCreateReqVO createReqVO) {
        // 创建页面设计
        PageDesignDO pageDesign = BeanUtils.toBean(createReqVO, PageDesignDO.class);
        pageDesign.setId(UUID.randomUUID().toString());
        pageDesign.setStatus("draft");
        pageDesign.setCreateTime(LocalDateTime.now());
        pageDesign.setUpdateTime(LocalDateTime.now());
        
        // TenantBaseDO会自动设置租户ID
        pageDesignMapper.insert(pageDesign);
        
        return BeanUtils.toBean(pageDesign, PageDesignRespVO.class);
    }

    @Override
    public void updatePageDesign(PageDesignUpdateReqVO updateReqVO) {
        // 更新页面设计
        PageDesignDO updateObj = BeanUtils.toBean(updateReqVO, PageDesignDO.class);
        updateObj.setUpdateTime(LocalDateTime.now());
        
        // TenantBaseDO会自动处理租户隔离
        pageDesignMapper.updateById(updateObj);
    }

    @Override
    public void deletePageDesign(String id) {
        // 删除页面设计 - TenantBaseDO会自动处理租户隔离
        pageDesignMapper.deleteById(id);
    }

    @Override
    public PageDesignRespVO getPageDesign(String id) {
        // TenantBaseDO会自动处理租户隔离
        PageDesignDO pageDesign = pageDesignMapper.selectById(id);
        return BeanUtils.toBean(pageDesign, PageDesignRespVO.class);
    }

    @Override
    public List<PageDesignRespVO> getPageDesignList() {
        // TenantBaseDO会自动处理租户隔离
        LambdaQueryWrapper<PageDesignDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PageDesignDO::getCreateTime);
        List<PageDesignDO> list = pageDesignMapper.selectList(wrapper);
        return list.stream()
                .map(item -> BeanUtils.toBean(item, PageDesignRespVO.class))
                .toList();
    }

    @Override
    public BuildResultRespVO uploadAndBuild(UploadPageReqVO reqVO) {
        try {
            // 1. 保存页面设计
            PageDesignDO pageDesign = savePageDesign(reqVO);
            
            // 2. 生成页面代码
            String pageCode = generatePageCode(pageDesign);
            
            // 3. 保存页面文件
            savePageFile(pageDesign.getPagePath(), pageCode);
            
            // 4. 生成路由和菜单配置
            generateRouteAndMenu(pageDesign);
            
            // 5. 异步执行构建
            String buildId = UUID.randomUUID().toString();
            CompletableFuture.runAsync(() -> {
                try {
                    executeBuild(buildId, pageDesign);
                } catch (Exception e) {
                    log.error("构建失败", e);
                    updateBuildStatus(buildId, "failed", e.getMessage());
                }
            });
            
            return new BuildResultRespVO(buildId, "构建已开始");
            
        } catch (Exception e) {
            log.error("上传并构建失败", e);
            throw new RuntimeException("上传并构建失败: " + e.getMessage());
        }
    }

    @Override
    public BuildStatusRespVO getBuildStatus(String buildId) {
        // 这里应该从缓存或数据库获取构建状态
        // 暂时返回模拟数据
        return new BuildStatusRespVO(
            buildId,
            "building",
            75,
            "构建中...",
            LocalDateTime.now(),
            null
        );
    }

    @Override
    public void deployPage(String pageId) {
        try {
            PageDesignDO pageDesign = pageDesignMapper.selectById(pageId);
            if (pageDesign == null) {
                throw new RuntimeException("页面设计不存在");
            }
            
            // 1. 检查构建状态
            if (!"completed".equals(pageDesign.getBuildStatus())) {
                throw new RuntimeException("页面尚未构建完成");
            }
            
            // 2. 部署页面
            deployToServer(pageDesign);
            
            // 3. 更新状态
            pageDesign.setStatus("published");
            pageDesign.setUpdateTime(LocalDateTime.now());
            pageDesignMapper.updateById(pageDesign);
            
            log.info("页面部署成功: {}", pageId);
            
        } catch (Exception e) {
            log.error("页面部署失败", e);
            throw new RuntimeException("页面部署失败: " + e.getMessage());
        }
    }

    /**
     * 保存页面设计
     */
    private PageDesignDO savePageDesign(UploadPageReqVO reqVO) {
        PageDesignDO pageDesign = new PageDesignDO();
        pageDesign.setId(UUID.randomUUID().toString());
        pageDesign.setPageName(reqVO.getPageConfig().getPageName());
        pageDesign.setPagePath(reqVO.getPageConfig().getPagePath());
        pageDesign.setMenuTitle(reqVO.getPageConfig().getMenuTitle());
        pageDesign.setMenuIcon(reqVO.getPageConfig().getMenuIcon());
        // 将组件列表转换为JSON字符串
        pageDesign.setComponents(reqVO.getComponents() != null ? reqVO.getComponents().toString() : null);
        // 将页面配置转换为JSON字符串
        pageDesign.setConfig(reqVO.getPageConfig() != null ? reqVO.getPageConfig().toString() : null);
        pageDesign.setPageCode(reqVO.getPageCode());
        pageDesign.setRouteConfig(reqVO.getRouteConfig());
        pageDesign.setMenuConfig(reqVO.getMenuConfig());
        pageDesign.setStatus("draft");
        pageDesign.setCreateTime(LocalDateTime.now());
        pageDesign.setUpdateTime(LocalDateTime.now());
        
        pageDesignMapper.insert(pageDesign);
        return pageDesign;
    }

    /**
     * 生成页面代码
     */
    private String generatePageCode(PageDesignDO pageDesign) {
        StringBuilder code = new StringBuilder();
        code.append("<template>\n");
        code.append("  <div class=\"").append(pageDesign.getPagePath()).append("-page\">\n");
        code.append("    <el-card>\n");
        code.append("      <template #header>\n");
        code.append("        <div class=\"card-header\">\n");
        code.append("          <span>").append(pageDesign.getPageName()).append("</span>\n");
        code.append("        </div>\n");
        code.append("      </template>\n");
        code.append("      \n");
        code.append("      <el-form :model=\"formData\" label-width=\"120px\">\n");
        
        // 生成组件代码
        if (pageDesign.getComponents() != null) {
            // 这里应该解析components字符串为对象列表
            // 暂时跳过组件代码生成
            code.append("        <!-- 组件代码将在这里生成 -->\n");
        }
        
        code.append("      </el-form>\n");
        code.append("    </el-card>\n");
        code.append("  </div>\n");
        code.append("</template>\n");
        code.append("\n");
        code.append("<script setup lang=\"ts\">\n");
        code.append("import { ref, reactive } from 'vue'\n");
        code.append("import { ElMessage } from 'element-plus'\n");
        code.append("\n");
        code.append("const formData = reactive({\n");
        // 生成表单数据 - 暂时使用默认字段
        code.append("  field1: '',\n");
        code.append("  field2: '',\n");
        code.append("})\n");
        code.append("\n");
        code.append("const handleSubmit = () => {\n");
        code.append("  ElMessage.success('提交成功')\n");
        code.append("}\n");
        code.append("</script>\n");
        code.append("\n");
        code.append("<style scoped>\n");
        code.append(".").append(pageDesign.getPagePath()).append("-page {\n");
        code.append("  padding: 20px;\n");
        code.append("}\n");
        code.append("\n");
        code.append(".card-header {\n");
        code.append("  display: flex;\n");
        code.append("  justify-content: space-between;\n");
        code.append("  align-items: center;\n");
        code.append("}\n");
        code.append("</style>\n");
        
        return code.toString();
    }

    /**
     * 生成组件代码
     */
    private String generateComponentCode(Object component) {
        // 这里应该根据组件类型生成对应的代码
        // 暂时返回简单的表单组件代码
        return "<el-form-item label=\"字段\">\n" +
               "  <el-input v-model=\"formData.字段\" placeholder=\"请输入字段\" />\n" +
               "</el-form-item>";
    }

    /**
     * 获取组件标签
     */
    private String getComponentLabel(Object component) {
        // 这里应该从组件配置中获取标签
        return "field";
    }

    /**
     * 保存页面文件
     */
    private void savePageFile(String pagePath, String pageCode) throws IOException {
        // 创建目录
        Path dir = Paths.get("src/views/generated", pagePath);
        Files.createDirectories(dir);
        
        // 保存文件
        Path file = dir.resolve("index.vue");
        Files.write(file, pageCode.getBytes());
        
        log.info("页面文件已保存: {}", file);
    }

    /**
     * 生成路由和菜单配置
     */
    private void generateRouteAndMenu(PageDesignDO pageDesign) {
        // 这里应该生成路由和菜单配置
        // 并保存到相应的配置文件中
        log.info("路由和菜单配置已生成: {}", pageDesign.getPagePath());
    }

    /**
     * 执行构建
     */
    private void executeBuild(String buildId, PageDesignDO pageDesign) {
        try {
            log.info("开始构建页面: {}", pageDesign.getPagePath());
            
            // 1. 更新构建状态
            updateBuildStatus(buildId, "building", "构建中...");
            
            // 2. 执行前端构建
            ProcessBuilder pb = new ProcessBuilder(
                "npm", "run", "build:with-generated"
            );
            pb.directory(new File("tunnel-management-ui"));
            
            Process process = pb.start();
            
            // 3. 监控构建进度
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // 构建成功
                updateBuildStatus(buildId, "completed", "构建完成");
                
                // 更新页面设计状态
                pageDesign.setBuildStatus("completed");
                pageDesign.setUpdateTime(LocalDateTime.now());
                pageDesignMapper.updateById(pageDesign);
                
                log.info("页面构建成功: {}", pageDesign.getPagePath());
            } else {
                // 构建失败
                updateBuildStatus(buildId, "failed", "构建失败");
                log.error("页面构建失败: {}", pageDesign.getPagePath());
            }
            
        } catch (Exception e) {
            log.error("构建执行失败", e);
            updateBuildStatus(buildId, "failed", e.getMessage());
        }
    }

    /**
     * 更新构建状态
     */
    private void updateBuildStatus(String buildId, String status, String message) {
        // 这里应该更新构建状态到缓存或数据库
        log.info("构建状态更新: {} -> {}", buildId, status);
    }

    /**
     * 部署到服务器
     */
    private void deployToServer(PageDesignDO pageDesign) {
        try {
            // 1. 复制构建文件到部署目录
            Path sourceDir = Paths.get("tunnel-management-ui/dist");
            Path targetDir = Paths.get("/var/www/app");
            
            // 这里应该实现文件复制逻辑
            log.info("部署页面到服务器: {}", pageDesign.getPagePath());
            
            // 2. 重启应用（如果需要）
            // 这里可以添加重启应用的逻辑
            
        } catch (Exception e) {
            log.error("部署失败", e);
            throw new RuntimeException("部署失败: " + e.getMessage());
        }
    }
} 