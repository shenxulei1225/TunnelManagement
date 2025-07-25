package com.cheers.arch.module.system.service.domain;

import cn.hutool.core.util.StrUtil;
import com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldConfig;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldRelRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainPageReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainSaveReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainStatisticsRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainTreeRespVO;
import com.cheers.arch.module.system.convert.domain.DomainConvert;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainDO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainFieldRelDO;
import com.cheers.arch.module.system.dal.mysql.domain.DomainMapper;
import com.cheers.arch.module.system.dal.mysql.domain.DomainFieldRelMapper;
import com.cheers.arch.module.system.enums.ErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.framework.common.util.collection.CollectionUtils.convertList;

/**
 * 领域模型 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
@lombok.extern.slf4j.Slf4j
public class DomainServiceImpl implements DomainService {

    @Resource
    private DomainMapper domainMapper;
    @Resource
    private DomainFieldRelMapper domainFieldRelMapper;

    @Override
    public Long createDomain(DomainSaveReqVO createReqVO) {
        // 设置默认父ID
        if (createReqVO.getParentId() == null) {
            createReqVO.setParentId(DomainDO.PARENT_ID_ROOT);
        }
        // 校验父领域的有效性
        validateParentDomain(null, createReqVO.getParentId());
        // 校验编码唯一性
        validateDomainCodeUnique(null, createReqVO.getCode());
        // 校验领域名的唯一性
        validateDomainNameUnique(null, createReqVO.getParentId(), createReqVO.getName());
        
        // 插入
        DomainDO domain = DomainConvert.INSTANCE.convert(createReqVO);
        domainMapper.insert(domain);
        
        // 返回
        return domain.getId();
    }

    @Override
    public void updateDomain(DomainSaveReqVO updateReqVO) {
        // 设置默认父ID
        if (updateReqVO.getParentId() == null) {
            updateReqVO.setParentId(DomainDO.PARENT_ID_ROOT);
        }
        // 校验存在
        validateDomainExists(updateReqVO.getId());
        // 校验父领域的有效性
        validateParentDomain(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验编码唯一性
        validateDomainCodeUnique(updateReqVO.getId(), updateReqVO.getCode());
        // 校验领域名的唯一性
        validateDomainNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());
        
        // 更新
        DomainDO updateObj = DomainConvert.INSTANCE.convert(updateReqVO);
        domainMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDomain(Long id) {
        // 校验存在
        validateDomainExists(id);
        // 校验是否有子领域
        List<DomainDO> childDomains = domainMapper.selectByParentId(id);
        if (!childDomains.isEmpty()) {
            throw exception(ErrorCodeConstants.DOMAIN_EXISTS_CHILDREN);
        }
        // 使用批量删除优化性能
        domainFieldRelMapper.deleteByDomainIds(List.of(id));
        domainMapper.deleteBatchByIds(List.of(id));
    }

    private void validateDomainExists(Long id) {
        if (domainMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.DOMAIN_NOT_EXISTS);
        }
    }

    @Override
    public DomainDO getDomain(Long id) {
        return domainMapper.selectById(id);
    }

    @Override
    public PageResult<DomainDO> getDomainPage(DomainPageReqVO pageReqVO) {
        return domainMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DomainDO> getDomainList(DomainPageReqVO exportReqVO) {
        return domainMapper.selectList(exportReqVO);
    }

    @Override
    public List<DomainRespVO> getDomainListWithStatistics(DomainPageReqVO reqVO) {
        // 1. 获取领域列表
        List<DomainDO> domains = domainMapper.selectList(reqVO);
        if (domains.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 批量获取统计信息
        List<Long> domainIds = domains.stream().map(DomainDO::getId).toList();
        List<DomainStatisticsRespVO> statisticsList = getBatchDomainStatistics(domainIds);
        
        // 3. 创建统计信息映射
        Map<Long, DomainStatisticsRespVO> statisticsMap = statisticsList.stream()
                .collect(Collectors.toMap(DomainStatisticsRespVO::getDomainId, s -> s));

        // 4. 转换为带统计信息的响应VO
        return domains.stream()
                .map(domain -> DomainConvert.INSTANCE.convertWithStatistics(
                        domain, 
                        statisticsMap.get(domain.getId())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DomainTreeRespVO> getDomainTree(DomainPageReqVO reqVO) {
        // 1. 获取所有领域数据
        List<DomainDO> allDomains = domainMapper.selectList(reqVO);
        if (allDomains.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 批量获取统计信息
        List<Long> domainIds = allDomains.stream().map(DomainDO::getId).toList();
        List<DomainStatisticsRespVO> statisticsList = getBatchDomainStatistics(domainIds);
        
        // 3. 创建统计信息映射
        Map<Long, DomainStatisticsRespVO> statisticsMap = statisticsList.stream()
                .collect(Collectors.toMap(DomainStatisticsRespVO::getDomainId, s -> s));

        // 4. 转换为树形VO并创建映射
        Map<Long, DomainTreeRespVO> domainMap = allDomains.stream()
                .collect(Collectors.toMap(
                        DomainDO::getId,
                        domain -> DomainConvert.INSTANCE.convertToTree(domain, statisticsMap.get(domain.getId()))
                ));

        // 5. 构建树形结构
        List<DomainTreeRespVO> rootNodes = new ArrayList<>();
        
        for (DomainTreeRespVO domain : domainMap.values()) {
            if (domain.getParentId() == null || domain.getParentId().equals(DomainDO.PARENT_ID_ROOT)) {
                // 根节点
                rootNodes.add(domain);
            } else {
                // 子节点，添加到父节点的children中
                DomainTreeRespVO parent = domainMap.get(domain.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(domain);
                }
            }
        }

        // 6. 对每个级别的节点按sort排序
        sortTreeNodes(rootNodes);
        
        return rootNodes;
    }

    /**
     * 递归排序树节点
     */
    private void sortTreeNodes(List<DomainTreeRespVO> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        
        // 当前级别排序
        nodes.sort((a, b) -> {
            if (a.getSort() == null && b.getSort() == null) {
                return 0;
            }
            if (a.getSort() == null) {
                return 1;
            }
            if (b.getSort() == null) {
                return -1;
            }
            return a.getSort().compareTo(b.getSort());
        });
        
        // 递归排序子节点
        for (DomainTreeRespVO node : nodes) {
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                sortTreeNodes(node.getChildren());
            }
        }
    }

    @Override
    public void validateDomainCodeUnique(Long id, String code) {
        if (StrUtil.isBlank(code)) {
            return;
        }
        DomainDO domain = domainMapper.selectByCode(code);
        if (domain == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的编码
        if (id == null) {
            throw exception(ErrorCodeConstants.DOMAIN_CODE_DUPLICATE, code);
        }
        if (!domain.getId().equals(id)) {
            throw exception(ErrorCodeConstants.DOMAIN_CODE_DUPLICATE, code);
        }
    }

    @Override
    public List<DomainDO> getChildDomainList(Long parentId) {
        return domainMapper.selectByParentId(parentId);
    }

    @Override
    public List<DomainDO> getChildDomainList(Collection<Long> parentIds) {
        List<DomainDO> result = new ArrayList<>();
        for (Long parentId : parentIds) {
            result.addAll(getChildDomainList(parentId));
        }
        return result;
    }

    @Override
    public void addDomainFieldRel(Long domainId, Long fieldId, Boolean required, Integer sort, String remark) {
        // 校验领域存在
        validateDomainExists(domainId);
        
        // 检查关联是否已存在，如果存在则更新，不存在则新增
        DomainFieldRelDO existRel = domainFieldRelMapper.selectByDomainIdAndFieldId(domainId, fieldId);
        if (existRel != null) {
            // 关联已存在，更新相关属性
            DomainFieldRelDO updateRel = new DomainFieldRelDO();
            updateRel.setId(existRel.getId());
            updateRel.setRequired(required != null ? required : existRel.getRequired());
            updateRel.setSort(sort != null ? sort : existRel.getSort());
            updateRel.setRemark(remark != null ? remark : existRel.getRemark());
            domainFieldRelMapper.updateById(updateRel);
            log.info("更新领域{}与字段{}的关联关系", domainId, fieldId);
        } else {
            // 关联不存在，新增关联
            DomainFieldRelDO rel = DomainFieldRelDO.builder()
                    .domainId(domainId)
                    .fieldId(fieldId)
                    .required(required != null ? required : false)
                    .sort(sort != null ? sort : 0)
                    .remark(remark)
                    .build();
            domainFieldRelMapper.insert(rel);
            log.info("新增领域{}与字段{}的关联关系", domainId, fieldId);
        }
    }

    @Override
    public void removeDomainFieldRel(Long domainId, Long fieldId) {
        DomainFieldRelDO rel = domainFieldRelMapper.selectByDomainIdAndFieldId(domainId, fieldId);
        if (rel == null) {
            throw exception(ErrorCodeConstants.FIELD_CATEGORY_REL_NOT_EXISTS);
        }
        domainFieldRelMapper.deleteById(rel.getId());
    }

    @Override
    public List<DomainFieldRelDO> getDomainFieldRelList(Long domainId) {
        return domainFieldRelMapper.selectByDomainId(domainId);
    }

    private void validateParentDomain(Long id, Long parentId) {
        if (parentId == null || DomainDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父领域
        if (Objects.equals(id, parentId)) {
            throw exception(ErrorCodeConstants.DOMAIN_PARENT_ERROR);
        }
        // 2. 父领域不存在
        DomainDO parentDomain = domainMapper.selectById(parentId);
        if (parentDomain == null) {
            throw exception(ErrorCodeConstants.DOMAIN_PARENT_NOT_EXISTS);
        }
        // 3. 递归校验，避免设置自己的子领域为父领域
        if (id == null) { // 新增时，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentDomain.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(ErrorCodeConstants.DOMAIN_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父领域
            if (parentId == null || DomainDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentDomain = domainMapper.selectById(parentId);
            if (parentDomain == null) {
                break;
            }
        }
    }

    private void validateDomainNameUnique(Long id, Long parentId, String name) {
        DomainDO domain = domainMapper.selectByParentIdAndName(parentId, name);
        if (domain == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的领域
        if (id == null) {
            throw exception(ErrorCodeConstants.DOMAIN_NAME_DUPLICATE);
        }
        if (!Objects.equals(domain.getId(), id)) {
            throw exception(ErrorCodeConstants.DOMAIN_NAME_DUPLICATE);
        }
    }

    @Override
    public void moveDomain(Long id, Long targetParentId, Integer targetSort) {
        // 校验移动的领域存在
        DomainDO domain = domainMapper.selectById(id);
        if (domain == null) {
            throw exception(ErrorCodeConstants.DOMAIN_NOT_EXISTS);
        }

        // 校验目标父领域的有效性
        validateParentDomain(id, targetParentId);

        // 如果目标父领域和当前父领域相同，只需要调整排序
        if (Objects.equals(domain.getParentId(), targetParentId)) {
            // 同父级内部调整排序
            adjustSortWithinParent(targetParentId, id, targetSort);
        } else {
            // 移动到不同父级
            moveToNewParent(id, targetParentId, targetSort);
        }
    }

    @Override
    public void updateDomainSorts(Long parentId, Map<Long, Integer> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return;
        }

        // 批量更新排序
        sorts.forEach((domainId, sort) -> {
            DomainDO updateObj = new DomainDO();
            updateObj.setId(domainId);
            updateObj.setSort(sort);
            domainMapper.updateById(updateObj);
        });
    }

    /**
     * 在同一父级内调整排序
     */
    private void adjustSortWithinParent(Long parentId, Long moveDomainId, Integer targetSort) {
        List<DomainDO> siblings = domainMapper.selectByParentId(parentId);
        
        if (targetSort == null) {
            targetSort = siblings.size();
        }

        // 重新计算所有兄弟节点的排序
        int newSort = 1;
        for (DomainDO sibling : siblings) {
            if (Objects.equals(sibling.getId(), moveDomainId)) {
                continue; // 跳过移动的节点
            }
            
            if (newSort == targetSort) {
                newSort++; // 为移动的节点预留位置
            }
            
            if (!Objects.equals(sibling.getSort(), newSort)) {
                DomainDO updateObj = new DomainDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(newSort);
                domainMapper.updateById(updateObj);
            }
            newSort++;
        }

        // 更新移动节点的排序
        DomainDO updateObj = new DomainDO();
        updateObj.setId(moveDomainId);
        updateObj.setSort(targetSort);
        domainMapper.updateById(updateObj);
    }

    /**
     * 移动到新的父级
     */
    private void moveToNewParent(Long domainId, Long targetParentId, Integer targetSort) {
        // 1. 获取目标父级下的所有子节点
        List<DomainDO> targetSiblings = domainMapper.selectByParentId(targetParentId);
        
        if (targetSort == null || targetSort > targetSiblings.size() + 1) {
            targetSort = targetSiblings.size() + 1;
        }

        // 2. 调整目标父级下其他节点的排序
        for (DomainDO sibling : targetSiblings) {
            if (sibling.getSort() >= targetSort) {
                DomainDO updateObj = new DomainDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(sibling.getSort() + 1);
                domainMapper.updateById(updateObj);
            }
        }

        // 3. 更新移动节点的父级和排序
        DomainDO updateObj = new DomainDO();
        updateObj.setId(domainId);
        updateObj.setParentId(targetParentId);
        updateObj.setSort(targetSort);
        domainMapper.updateById(updateObj);

        // 4. 重新整理原父级下的排序
        DomainDO originalDomain = domainMapper.selectById(domainId);
        if (originalDomain != null && originalDomain.getParentId() != null) {
            reorderSiblings(originalDomain.getParentId());
        }
    }

    /**
     * 重新整理兄弟节点的排序，消除空隙
     */
    private void reorderSiblings(Long parentId) {
        List<DomainDO> siblings = domainMapper.selectByParentId(parentId);
        
        int sort = 1;
        for (DomainDO sibling : siblings) {
            if (!Objects.equals(sibling.getSort(), sort)) {
                DomainDO updateObj = new DomainDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(sort);
                domainMapper.updateById(updateObj);
            }
            sort++;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveFieldToDomain(Long fieldId, Long sourceDomainId, Long targetDomainId, 
                                 Boolean required, Integer sort, String remark) {
        // 1. 校验字段存在
        // TODO: 可以添加字段存在性校验
        
        // 2. 校验源领域和目标领域存在
        validateDomainExists(sourceDomainId);
        validateDomainExists(targetDomainId);
        
        // 3. 校验源领域和目标领域不相同
        if (Objects.equals(sourceDomainId, targetDomainId)) {
            throw exception(ErrorCodeConstants.DOMAIN_FIELD_MOVE_SAME_DOMAIN);
        }
        
        // 4. 校验源关联关系存在
        DomainFieldRelDO sourceRel = domainFieldRelMapper.selectByDomainIdAndFieldId(sourceDomainId, fieldId);
        if (sourceRel == null) {
            throw exception(ErrorCodeConstants.FIELD_CATEGORY_REL_NOT_EXISTS);
        }
        
        // 5. 检查目标关联关系，如果已存在则先删除，再创建新的
        DomainFieldRelDO targetRel = domainFieldRelMapper.selectByDomainIdAndFieldId(targetDomainId, fieldId);
        if (targetRel != null) {
            // 目标关联已存在，先删除旧的关联
            domainFieldRelMapper.deleteById(targetRel.getId());
            log.info("删除目标领域{}与字段{}的已有关联关系", targetDomainId, fieldId);
        }
        
        // 6. 删除源领域的关联关系
        domainFieldRelMapper.deleteById(sourceRel.getId());
        
        // 7. 创建目标领域的关联关系
        DomainFieldRelDO newRel = DomainFieldRelDO.builder()
                .domainId(targetDomainId)
                .fieldId(fieldId)
                .required(required != null ? required : sourceRel.getRequired()) // 保持原有设置或使用新设置
                .sort(sort != null ? sort : getNextSortInDomain(targetDomainId)) // 指定排序或自动分配
                .remark(remark != null ? remark : "从领域" + sourceDomainId + "移动而来")
                .build();
        domainFieldRelMapper.insert(newRel);
        
        // 8. 重新整理源领域的字段排序
        reorderFieldsInDomain(sourceDomainId);
    }

    /**
     * 获取领域中下一个排序号
     */
    private Integer getNextSortInDomain(Long domainId) {
        List<DomainFieldRelDO> existingRels = domainFieldRelMapper.selectByDomainId(domainId);
        return existingRels.size() + 1;
    }

    /**
     * 重新整理领域中字段的排序
     */
    private void reorderFieldsInDomain(Long domainId) {
        List<DomainFieldRelDO> fieldRels = domainFieldRelMapper.selectByDomainId(domainId);
        
        int sort = 1;
        for (DomainFieldRelDO fieldRel : fieldRels) {
            if (!Objects.equals(fieldRel.getSort(), sort)) {
                DomainFieldRelDO updateObj = new DomainFieldRelDO();
                updateObj.setId(fieldRel.getId());
                updateObj.setSort(sort);
                domainFieldRelMapper.updateById(updateObj);
            }
            sort++;
        }
    }

    @Override
    public List<DomainFieldRelRespVO> getFieldsWithDetailsByDomainId(Long domainId) {
        // 校验领域存在
        validateDomainExists(domainId);
        
        // 查询领域下的字段列表（带字段详情）
        return domainFieldRelMapper.selectFieldsByDomainId(domainId);
    }

    @Override
    public List<DomainFieldRelRespVO> getDomainsWithDetailsByFieldId(Long fieldId) {
        // TODO: 可以添加字段存在性校验
        
        // 查询字段关联的领域列表（带领域详情）
        return domainFieldRelMapper.selectDomainsByFieldId(fieldId);
    }

    // ================ 统计功能实现 ================

    @Override
    public DomainStatisticsRespVO getDomainStatistics(Long domainId) {
        // 校验领域存在
        validateDomainExists(domainId);
        DomainDO domain = domainMapper.selectById(domainId);
        
        // 计算直接关联的字段数量
        Integer directFieldCount = domainFieldRelMapper.countFieldsByDomainId(domainId);
        
        // 计算直接子领域数量
        Integer directChildCount = domainMapper.countChildrenByParentId(domainId);
        
        // 获取所有后代领域ID（递归）
        List<Long> allDescendantIds = domainMapper.selectAllDescendantIds(domainId);
        
        // 计算总子领域数量
        Integer totalChildCount = allDescendantIds.size();
        
        // 计算总字段数量（包含所有子领域）
        Integer totalFieldCount = directFieldCount;
        if (!allDescendantIds.isEmpty()) {
            List<Map<String, Object>> descendantFieldCounts = domainFieldRelMapper.countFieldsByDomainIds(allDescendantIds);
            for (Map<String, Object> count : descendantFieldCounts) {
                totalFieldCount += ((Number) count.get("field_count")).intValue();
            }
        }
        
        // 计算层级深度
        Integer depth = calculateDomainDepth(domainId);
        
        return new DomainStatisticsRespVO(
            domainId,
            domain.getName(),
            domain.getCode(),
            directFieldCount,
            directChildCount,
            totalFieldCount,
            totalChildCount,
            depth
        );
    }

    @Override
    public List<DomainStatisticsRespVO> getBatchDomainStatistics(Collection<Long> domainIds) {
        if (domainIds == null || domainIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<DomainStatisticsRespVO> result = new ArrayList<>();
        for (Long domainId : domainIds) {
            try {
                result.add(getDomainStatistics(domainId));
            } catch (Exception e) {
                // 如果某个领域统计失败，继续处理其他领域
                log.warn("获取领域{}统计信息失败: {}", domainId, e.getMessage());
            }
        }
        return result;
    }

    // ================ 批量处理功能实现 ================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDomainsBatch(Collection<Long> domainIds) {
        if (domainIds == null || domainIds.isEmpty()) {
            return;
        }
        
        // 收集所有需要删除的领域ID（包括所有后代）
        Set<Long> allDomainIdsToDelete = new HashSet<>();
        for (Long domainId : domainIds) {
            // 校验领域存在
            validateDomainExists(domainId);
            
            // 添加当前领域
            allDomainIdsToDelete.add(domainId);
            
            // 添加所有后代领域
            List<Long> descendantIds = domainMapper.selectAllDescendantIds(domainId);
            allDomainIdsToDelete.addAll(descendantIds);
        }
        
        // 批量删除所有领域的字段关联关系
        domainFieldRelMapper.deleteByDomainIds(allDomainIdsToDelete);
        
        // 批量删除领域
        domainMapper.deleteBatchByIds(allDomainIdsToDelete);
        
        log.info("批量删除领域成功，删除领域数量: {}", allDomainIdsToDelete.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFieldDomainRelationsBatch(Collection<Long> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return;
        }
        
        // 批量删除字段的所有领域关联关系
        domainFieldRelMapper.deleteByFieldIds(fieldIds);
        
        log.info("批量删除字段关联关系成功，字段数量: {}", fieldIds.size());
    }

    // ================ 私有辅助方法 ================

    /**
     * 计算领域的层级深度
     */
    private Integer calculateDomainDepth(Long domainId) {
        int depth = 0;
        Long currentId = domainId;
        
        while (currentId != null && !Objects.equals(currentId, DomainDO.PARENT_ID_ROOT)) {
            DomainDO domain = domainMapper.selectById(currentId);
            if (domain == null) {
                break;
            }
            depth++;
            currentId = domain.getParentId();
        }
        
        return depth;
    }

    // ================ 对标Category的批量关联功能实现 ================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateFieldDomainRels(Long fieldId, Collection<Long> domainIds) {
        if (domainIds == null || domainIds.isEmpty()) {
            return;
        }

        List<DomainFieldRelDO> relations = new ArrayList<>();
        for (Long domainId : domainIds) {
            // 校验领域存在
            validateDomainExists(domainId);
            
            // 检查是否已存在关联
            if (!domainFieldRelMapper.exists(fieldId, domainId)) {
                DomainFieldRelDO rel = DomainFieldRelDO.builder()
                        .fieldId(fieldId)
                        .domainId(domainId)
                        .required(false)
                        .sort(domainFieldRelMapper.selectNextSortInDomain(domainId))
                        .remark("批量创建")
                        .build();
                relations.add(rel);
            }
        }

        if (!relations.isEmpty()) {
            domainFieldRelMapper.batchInsert(relations);
            log.info("批量创建字段{}与{}个领域的关联关系", fieldId, relations.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateFieldDomainRels(Long fieldId, Map<Long, DomainFieldConfig> domainConfigMap) {
        if (domainConfigMap == null || domainConfigMap.isEmpty()) {
            return;
        }

        List<DomainFieldRelDO> relations = new ArrayList<>();
        for (Map.Entry<Long, DomainFieldConfig> entry : domainConfigMap.entrySet()) {
            Long domainId = entry.getKey();
            DomainFieldConfig config = entry.getValue();
            
            // 校验领域存在
            validateDomainExists(domainId);
            
            // 检查是否已存在关联
            if (!domainFieldRelMapper.exists(fieldId, domainId)) {
                DomainFieldRelDO rel = DomainFieldRelDO.builder()
                        .fieldId(fieldId)
                        .domainId(domainId)
                        .required(config.getRequired() != null ? config.getRequired() : false)
                        .sort(config.getSort() != null ? config.getSort() : domainFieldRelMapper.selectNextSortInDomain(domainId))
                        .remark(config.getRemark() != null ? config.getRemark() : "批量创建")
                        .build();
                relations.add(rel);
            }
        }

        if (!relations.isEmpty()) {
            domainFieldRelMapper.batchInsert(relations);
            log.info("批量创建字段{}与{}个领域的关联关系（带配置）", fieldId, relations.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceFieldDomainRels(Long fieldId, Collection<Long> domainIds) {
        // 1. 删除现有的所有关联
        domainFieldRelMapper.deleteByFieldId(fieldId);
        
        // 2. 创建新的关联
        if (domainIds != null && !domainIds.isEmpty()) {
            batchCreateFieldDomainRels(fieldId, domainIds);
        }
        
        log.info("替换字段{}的所有领域关联关系，新关联数量: {}", fieldId, domainIds != null ? domainIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceFieldDomainRels(Long fieldId, Map<Long, DomainFieldConfig> domainConfigMap) {
        // 1. 删除现有的所有关联
        domainFieldRelMapper.deleteByFieldId(fieldId);
        
        // 2. 创建新的关联
        if (domainConfigMap != null && !domainConfigMap.isEmpty()) {
            batchCreateFieldDomainRels(fieldId, domainConfigMap);
        }
        
        log.info("替换字段{}的所有领域关联关系（带配置），新关联数量: {}", fieldId, domainConfigMap != null ? domainConfigMap.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateFieldConfigInDomain(Long domainId, Map<Long, DomainFieldConfig> fieldConfigMap) {
        if (fieldConfigMap == null || fieldConfigMap.isEmpty()) {
            return;
        }

        // 校验领域存在
        validateDomainExists(domainId);

        for (Map.Entry<Long, DomainFieldConfig> entry : fieldConfigMap.entrySet()) {
            Long fieldId = entry.getKey();
            DomainFieldConfig config = entry.getValue();
            
            // 检查关联是否存在
            DomainFieldRelDO existRel = domainFieldRelMapper.selectByDomainIdAndFieldId(domainId, fieldId);
            if (existRel != null) {
                DomainFieldRelDO updateRel = new DomainFieldRelDO();
                updateRel.setId(existRel.getId());
                updateRel.setRequired(config.getRequired() != null ? config.getRequired() : existRel.getRequired());
                updateRel.setSort(config.getSort() != null ? config.getSort() : existRel.getSort());
                updateRel.setRemark(config.getRemark() != null ? config.getRemark() : existRel.getRemark());
                domainFieldRelMapper.updateById(updateRel);
            }
        }
        
        log.info("批量更新领域{}中{}个字段的配置", domainId, fieldConfigMap.size());
    }

    @Override
    public boolean existsFieldDomainRel(Long fieldId, Long domainId) {
        return domainFieldRelMapper.exists(fieldId, domainId);
    }

    @Override
    public Integer getNextFieldSortInDomain(Long domainId) {
        return domainFieldRelMapper.selectNextSortInDomain(domainId);
    }

    @Override
    public Map<Long, DomainFieldConfig> getFieldConfigMapInDomain(Long domainId) {
        List<Map<String, Object>> configs = domainFieldRelMapper.selectFieldConfigByDomainId(domainId);
        return configs.stream().collect(Collectors.toMap(
                config -> ((Number) config.get("field_id")).longValue(),
                config -> DomainFieldConfig.of(
                        (Boolean) config.get("required"),
                        ((Number) config.get("sort")).intValue(),
                        (String) config.get("remark")
                )
        ));
    }

    // ================ 对标Category的高级查询功能实现 ================

    @Override
    public List<DomainDO> getDomainsByTreePath(String treePath) {
        return domainMapper.selectByTreePath(treePath);
    }

    @Override
    public List<DomainDO> getDomainsByLevel(Integer level) {
        return domainMapper.selectByLevel(level);
    }

    @Override
    public List<DomainDO> getReadonlyDomains() {
        return domainMapper.selectReadonlyDomains();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDomainTreeInfo(Long domainId) {
        DomainDO domain = domainMapper.selectById(domainId);
        if (domain == null) {
            return;
        }

        // 计算层级深度和树路径
        String treePath = buildTreePath(domainId);
        Integer level = calculateLevel(treePath);

        // 更新树信息
        DomainDO updateDomain = new DomainDO();
        updateDomain.setId(domainId);
        updateDomain.setTreePath(treePath);
        updateDomain.setLevel(level);
        domainMapper.updateById(updateDomain);

        // 递归更新所有子领域的树信息
        List<DomainDO> children = domainMapper.selectByParentId(domainId);
        for (DomainDO child : children) {
            updateDomainTreeInfo(child.getId());
        }
    }

    /**
     * 构建树路径
     */
    private String buildTreePath(Long domainId) {
        List<String> pathParts = new ArrayList<>();
        Long currentId = domainId;
        
        while (currentId != null && !Objects.equals(currentId, DomainDO.PARENT_ID_ROOT)) {
            pathParts.add(0, currentId.toString());
            DomainDO domain = domainMapper.selectById(currentId);
            if (domain == null) {
                break;
            }
            currentId = domain.getParentId();
        }
        
        return String.join("/", pathParts);
    }

    /**
     * 根据路径计算层级
     */
    private Integer calculateLevel(String treePath) {
        if (treePath == null || treePath.isEmpty()) {
            return 1;
        }
        return treePath.split("/").length;
    }

} 