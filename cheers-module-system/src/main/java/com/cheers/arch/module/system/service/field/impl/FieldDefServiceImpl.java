package com.cheers.arch.module.system.service.field.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;
import com.cheers.arch.framework.directory.service.DirectoryService;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldDefCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldDefUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefCategoryRelDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldDefCategoryRelMapper;
import com.cheers.arch.module.system.dal.mysql.field.FieldDefMapper;
import com.cheers.arch.module.system.service.field.FieldDefService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FieldDefServiceImpl extends ServiceImpl<FieldDefMapper, FieldDefDO> implements FieldDefService {

    private final FieldDefMapper fieldDefMapper;
    private final FieldDefCategoryRelMapper relMapper;
    private final com.cheers.arch.module.system.dal.mysql.region.FieldCategoryMapper categoryMapper;
    private final DirectoryService directoryService;

    @Override
    @Transactional
    public Long createFieldDef(FieldDefCreateReqVO reqVO) {
        log.info("Create FieldDef request: {}", reqVO);
        FieldDefDO def = BeanUtils.toBean(reqVO, FieldDefDO.class);
        def.setFieldKey(IdUtil.fastSimpleUUID());
        fieldDefMapper.insert(def);
        // 关联分类
        if (reqVO.getCategoryIds() != null) {
            List<FieldDefCategoryRelDO> relList = reqVO.getCategoryIds().stream()
                    .map(cid -> new FieldDefCategoryRelDO().setFieldDefId(def.getId()).setCategoryId(cid))
                    .collect(Collectors.toList());
            relList.forEach(relMapper::insert);
        }
        log.info("Create FieldDef success, id={}", def.getId());
        return def.getId();
    }

    @Override
    @Transactional
    public boolean updateFieldDef(FieldDefUpdateReqVO reqVO) {
        log.info("Update FieldDef request: {}", reqVO);
        FieldDefDO def = BeanUtils.toBean(reqVO, FieldDefDO.class);
        FieldDefDO origin = fieldDefMapper.selectById(reqVO.getId());
        if (origin != null) {
            def.setFieldKey(origin.getFieldKey());
        }
        int updated = fieldDefMapper.updateById(def);
        // 更新关联
        relMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().eq("field_def_id", reqVO.getId()));
        if (reqVO.getCategoryIds() != null) {
            reqVO.getCategoryIds().forEach(cid -> relMapper.insert(new FieldDefCategoryRelDO().setFieldDefId(reqVO.getId()).setCategoryId(cid)));
        }
        log.info("Update FieldDef [{}] success: {}", reqVO.getId(), updated > 0);
        return updated > 0;
    }

    @Override
    @Transactional
    public boolean deleteFieldDef(Long id) {
        int deleted = fieldDefMapper.deleteById(id);
        relMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().eq("field_def_id", id));
        return deleted > 0;
    }

    @Override
    public FieldDefDO getFieldDef(Long id) {
        return fieldDefMapper.selectById(id);
    }

    @Override
    public List<FieldDefDO> getFieldDefListByCategory(Long categoryId) {
        // 1. 取所有字段定义
        List<FieldDefDO> defs = fieldDefMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>());
        if (defs.isEmpty()) {
            return defs;
        }
        // 2. 查询关联
        List<Long> defIds = defs.stream().map(FieldDefDO::getId).toList();
        List<FieldDefCategoryRelDO> rels = relMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FieldDefCategoryRelDO>().in("field_def_id", defIds));
        // 3. 按字段分组收集全部分类
        java.util.Map<Long, java.util.List<Long>> catMap = rels.stream()
                .collect(java.util.stream.Collectors.groupingBy(FieldDefCategoryRelDO::getFieldDefId,
                        java.util.stream.Collectors.mapping(FieldDefCategoryRelDO::getCategoryId, java.util.stream.Collectors.toList())));
        // 4. 填充 categoryIds 字段
        defs.forEach(d -> d.setCategoryIds(catMap.getOrDefault(d.getId(), java.util.Collections.emptyList())));

        // 5. 按需过滤
        if (categoryId != null && categoryId > 0) {
            return defs.stream().filter(d -> d.getCategoryIds() != null && d.getCategoryIds().contains(categoryId)).toList();
        }
        return defs;
    }

    @Override
    public List<FieldDefDO> getFieldDefListByBizType(String bizType) {
        if (cn.hutool.core.util.StrUtil.isBlank(bizType)) {
            return getFieldDefListByCategory(null);
        }
        // 找到业务根分类
        com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO root = categoryMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO>()
                .eq(com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO::getParentId, 0L)
                .eq(com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO::getCode, bizType)
                .eq(com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO::getDeleted, false));
        if (root == null) {
            return java.util.Collections.emptyList();
        }
        // 获取该业务所有分类 id
        List<com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO> cats = categoryMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO>()
                .and(w -> w.eq(com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO::getId, root.getId())
                        .or().likeRight(com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO::getTreePath, root.getTreePath() + "/" + root.getId())));
        java.util.Set<Long> catIds = cats.stream().map(c -> c.getId()).collect(java.util.stream.Collectors.toSet());

        // 复用已有逻辑
        List<FieldDefDO> all = getFieldDefListByCategory(null);
        return all.stream().filter(d -> d.getCategoryIds() != null && d.getCategoryIds().stream().anyMatch(catIds::contains)).toList();
    }

    @Override
    public List<FieldDefDO> getFieldDefListBySemanticDirectory(Long semanticDirectoryId) {
        log.info("Query FieldDef list by semantic directory: [{}]", semanticDirectoryId);
        
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefDO> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        
        if (semanticDirectoryId != null) {
            // 递归查询该目录及所有子目录下的字段
            List<Long> allDirectoryIds = getAllChildDirectoryIds(semanticDirectoryId);
            allDirectoryIds.add(semanticDirectoryId); // 包含当前目录
            
            wrapper.in(FieldDefDO::getSemanticDirectoryId, allDirectoryIds);
        } else {
            // 查询未分类的字段（semanticDirectoryId为null）
            wrapper.isNull(FieldDefDO::getSemanticDirectoryId);
        }
        
        wrapper.orderByAsc(FieldDefDO::getSort, FieldDefDO::getId);
        
        List<FieldDefDO> result = fieldDefMapper.selectList(wrapper);
        log.info("Found {} fields for semantic directory [{}] and its children", result.size(), semanticDirectoryId);
        
        return result;
    }

    @Override
    @Transactional
    public boolean updateFieldSemanticDirectory(Long fieldId, Long semanticDirectoryId) {
        log.info("Update FieldDef [{}] semantic directory to [{}]", fieldId, semanticDirectoryId);
        
        FieldDefDO fieldDef = new FieldDefDO();
        fieldDef.setId(fieldId);
        fieldDef.setSemanticDirectoryId(semanticDirectoryId);
        
        int updated = fieldDefMapper.updateById(fieldDef);
        boolean success = updated > 0;
        log.info("Update FieldDef [{}] semantic directory success: {}", fieldId, success);
        
        return success;
    }

    @Override
    @Transactional
    public int batchUpdateFieldSemanticDirectory(List<Long> fieldIds, Long semanticDirectoryId) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return 0;
        }
        
        log.info("Batch update FieldDef semantic directory for {} fields to [{}]", fieldIds.size(), semanticDirectoryId);
        
        int totalUpdated = 0;
        for (Long fieldId : fieldIds) {
            FieldDefDO fieldDef = new FieldDefDO();
            fieldDef.setId(fieldId);
            fieldDef.setSemanticDirectoryId(semanticDirectoryId);
            
            int updated = fieldDefMapper.updateById(fieldDef);
            totalUpdated += updated;
        }
        
        log.info("Batch update FieldDef semantic directory completed: {}/{} success", totalUpdated, fieldIds.size());
        return totalUpdated;
    }

    @Override
    public List<Object> getSemanticDirectories() {
        // 方案1：优先尝试查询数据库中的真实数据
        try {
            // 先尝试查询数据库
            List<java.util.Map<String, Object>> dbResult = querySemanticDirectoriesFromDb();
            if (!dbResult.isEmpty()) {
                log.info("Successfully loaded {} semantic directories from database", dbResult.size());
                return new java.util.ArrayList<>(dbResult);
            }
        } catch (Exception e) {
            log.warn("Failed to query semantic directories from database, using fallback data", e);
        }
        
        // 方案2：数据库查询失败时，使用预定义数据作为备用
        log.info("Using predefined semantic directories as fallback");
        java.util.List<java.util.Map<String, Object>> directories = new java.util.ArrayList<>();
        
        // 创建根目录
        directories.add(createDirectoryMap(0L, "字段语义分类", "root", "字段语义分类根目录", 0, -1L));
        
        // 创建主要分类目录（作为根目录的子目录）
        directories.add(createDirectoryMap(1L, "物理测量", "physical", "物理相关的测量类字段", 1, 0L));
        directories.add(createDirectoryMap(2L, "业务管理", "business", "业务管理相关字段", 2, 0L));
        directories.add(createDirectoryMap(3L, "系统状态", "system", "系统状态相关字段", 3, 0L));
        
        // 创建物理测量的子分类
        directories.add(createDirectoryMap(11L, "几何测量", "measurement", "长度、面积、体积、重量等测量相关字段", 1, 1L));
        directories.add(createDirectoryMap(12L, "电气参数", "electrical", "电压、电流、功率、电阻等电气相关字段", 2, 1L));
        directories.add(createDirectoryMap(13L, "地理定位", "geographic", "坐标、海拔、方位、区域等地理相关字段", 3, 1L));
        
        // 创建业务管理的子分类
        directories.add(createDirectoryMap(21L, "合同管理", "contract", "合同编号、甲乙方、金额、期限等合同相关字段", 1, 2L));
        directories.add(createDirectoryMap(22L, "维护管理", "maintenance", "维护周期、最后维护时间、维护状态等维护相关字段", 2, 2L));
        directories.add(createDirectoryMap(23L, "检查管理", "inspection", "检查结果、检查时间、检查人员等检查相关字段", 3, 2L));
        
        // 创建系统状态的子分类
        directories.add(createDirectoryMap(31L, "运行状态", "status", "运行状态、健康状态、工作状态等状态相关字段", 1, 3L));
        directories.add(createDirectoryMap(32L, "时间信息", "temporal", "时间戳、日期、时刻等时间相关字段", 2, 3L));
        
        return new java.util.ArrayList<>(directories);
    }
    
    private List<java.util.Map<String, Object>> querySemanticDirectoriesFromDb() {
        try {
            // 使用DirectoryService查询FIELD_SEMANTIC业务类型的目录树
            List<DirectoryDO> directories = directoryService.getDirectoryTree("FIELD_SEMANTIC");
            
            if (directories == null || directories.isEmpty()) {
                log.info("No semantic directories found in database for business type: FIELD_SEMANTIC");
                return java.util.Collections.emptyList();
            }
            
            // 转换DirectoryDO为Map格式
            java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
            for (DirectoryDO directory : directories) {
                java.util.Map<String, Object> map = convertDirectoryToMap(directory);
                result.add(map);
            }
            
            log.info("Successfully loaded {} semantic directories from database", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("Failed to query semantic directories from database", e);
            return java.util.Collections.emptyList();
        }
    }
    
    private java.util.Map<String, Object> createDirectoryMap(Long id, String name, String code, String description, int sort, Long parentId) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("code", code);
        map.put("description", description);
        map.put("sort", sort);
        map.put("parentId", parentId != null && parentId >= 0 ? parentId : null);
        map.put("status", 1);
        map.put("children", new java.util.ArrayList<>());
        return map;
    }
    
    private java.util.Map<String, Object> convertDirectoryToMap(DirectoryDO directory) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", directory.getId());
        map.put("name", directory.getName());
        map.put("code", directory.getCode());
        map.put("description", directory.getDescription());
        map.put("sort", directory.getSort() != null ? directory.getSort() : 0);
        map.put("parentId", directory.getParentId() != null ? directory.getParentId() : 0L);
        map.put("status", directory.getStatus() != null ? directory.getStatus() : 1);
        map.put("icon", directory.getIcon());
        map.put("extData", directory.getExtData());
        map.put("children", new java.util.ArrayList<>());
        return map;
    }
    
    /**
     * 递归获取目录的所有子目录ID
     */
    private List<Long> getAllChildDirectoryIds(Long parentDirectoryId) {
        List<Long> allChildIds = new java.util.ArrayList<>();
        
        try {
            // 先尝试从数据库查询
            List<DirectoryDO> allDirectories = directoryService.getDirectoryTree("FIELD_SEMANTIC");
            if (allDirectories != null && !allDirectories.isEmpty()) {
                collectChildDirectoryIds(allDirectories, parentDirectoryId, allChildIds);
            } else {
                // 如果数据库查询失败，使用预定义数据
                List<java.util.Map<String, Object>> fallbackDirectories = createFallbackDirectories();
                collectChildDirectoryIdsFromMaps(fallbackDirectories, parentDirectoryId, allChildIds);
            }
        } catch (Exception e) {
            log.warn("Failed to query child directories from database, using fallback logic", e);
            List<java.util.Map<String, Object>> fallbackDirectories = createFallbackDirectories();
            collectChildDirectoryIdsFromMaps(fallbackDirectories, parentDirectoryId, allChildIds);
        }
        
        return allChildIds;
    }
    
    /**
     * 从DirectoryDO列表中递归收集子目录ID
     */
    private void collectChildDirectoryIds(List<DirectoryDO> directories, Long parentId, List<Long> result) {
        for (DirectoryDO directory : directories) {
            if (parentId.equals(directory.getParentId())) {
                result.add(directory.getId());
                // 递归查找子目录
                collectChildDirectoryIds(directories, directory.getId(), result);
            }
        }
    }
    
    /**
     * 从Map列表中递归收集子目录ID
     */
    private void collectChildDirectoryIdsFromMaps(List<java.util.Map<String, Object>> directories, Long parentId, List<Long> result) {
        for (java.util.Map<String, Object> directory : directories) {
            Long dirParentId = (Long) directory.get("parentId");
            if (parentId.equals(dirParentId)) {
                Long dirId = (Long) directory.get("id");
                result.add(dirId);
                // 递归查找子目录
                collectChildDirectoryIdsFromMaps(directories, dirId, result);
            }
        }
    }
    
    /**
     * 创建预定义目录数据（用于递归查询）
     */
    private List<java.util.Map<String, Object>> createFallbackDirectories() {
        java.util.List<java.util.Map<String, Object>> directories = new java.util.ArrayList<>();
        
        // 创建根目录
        directories.add(createDirectoryMap(0L, "字段语义分类", "root", "字段语义分类根目录", 0, -1L));
        
        // 创建主要分类目录（作为根目录的子目录）
        directories.add(createDirectoryMap(1L, "物理测量", "physical", "物理相关的测量类字段", 1, 0L));
        directories.add(createDirectoryMap(2L, "业务管理", "business", "业务管理相关字段", 2, 0L));
        directories.add(createDirectoryMap(3L, "系统状态", "system", "系统状态相关字段", 3, 0L));
        
        // 创建物理测量的子分类
        directories.add(createDirectoryMap(11L, "几何测量", "measurement", "长度、面积、体积、重量等测量相关字段", 1, 1L));
        directories.add(createDirectoryMap(12L, "电气参数", "electrical", "电压、电流、功率、电阻等电气相关字段", 2, 1L));
        directories.add(createDirectoryMap(13L, "地理定位", "geographic", "坐标、海拔、方位、区域等地理相关字段", 3, 1L));
        
        // 创建业务管理的子分类
        directories.add(createDirectoryMap(21L, "合同管理", "contract", "合同编号、甲乙方、金额、期限等合同相关字段", 1, 2L));
        directories.add(createDirectoryMap(22L, "维护管理", "maintenance", "维护周期、最后维护时间、维护状态等维护相关字段", 2, 2L));
        directories.add(createDirectoryMap(23L, "检查管理", "inspection", "检查结果、检查时间、检查人员等检查相关字段", 3, 2L));
        
        // 创建系统状态的子分类
        directories.add(createDirectoryMap(31L, "运行状态", "status", "运行状态、健康状态、工作状态等状态相关字段", 1, 3L));
        directories.add(createDirectoryMap(32L, "时间信息", "temporal", "时间戳、日期、时刻等时间相关字段", 2, 3L));
        
        return directories;
    }
}
