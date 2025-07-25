package com.cheers.arch.module.system.service.domain;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldConfig;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldRelRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainPageReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainSaveReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainStatisticsRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainTreeRespVO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainDO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainFieldRelDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 领域模型 Service 接口
 *
 * @author cheers
 */
public interface DomainService {

    /**
     * 创建领域模型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDomain(@Valid DomainSaveReqVO createReqVO);

    /**
     * 更新领域模型
     *
     * @param updateReqVO 更新信息
     */
    void updateDomain(@Valid DomainSaveReqVO updateReqVO);

    /**
     * 删除领域模型
     *
     * @param id 编号
     */
    void deleteDomain(Long id);

    /**
     * 获得领域模型
     *
     * @param id 编号
     * @return 领域模型
     */
    DomainDO getDomain(Long id);

    /**
     * 获得领域模型分页
     *
     * @param pageReqVO 分页查询
     * @return 领域模型分页
     */
    PageResult<DomainDO> getDomainPage(DomainPageReqVO pageReqVO);

    /**
     * 获得领域模型列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 领域模型列表
     */
    List<DomainDO> getDomainList(DomainPageReqVO exportReqVO);

    /**
     * 获得领域模型列表（带统计信息）
     *
     * @param reqVO 查询条件
     * @return 领域模型列表（带统计信息）
     */
    List<DomainRespVO> getDomainListWithStatistics(DomainPageReqVO reqVO);

    /**
     * 获得领域模型树形结构（Element Plus Tree格式，带统计信息）
     *
     * @param reqVO 查询条件
     * @return 领域模型树形结构
     */
    List<DomainTreeRespVO> getDomainTree(DomainPageReqVO reqVO);

    /**
     * 校验领域编码的唯一性
     *
     * @param id   编号
     * @param code 编码
     */
    void validateDomainCodeUnique(Long id, String code);

    /**
     * 获得指定父领域的子领域列表
     *
     * @param parentId 父领域编号
     * @return 子领域列表
     */
    List<DomainDO> getChildDomainList(Long parentId);

    /**
     * 获得指定领域的所有子领域
     *
     * @param parentIds 父领域编号数组
     * @return 子领域列表
     */
    List<DomainDO> getChildDomainList(Collection<Long> parentIds);

    /**
     * 添加领域字段关联
     *
     * @param domainId 领域ID
     * @param fieldId  字段ID
     * @param required 是否必填
     * @param sort     排序
     * @param remark   备注
     */
    void addDomainFieldRel(Long domainId, Long fieldId, Boolean required, Integer sort, String remark);

    /**
     * 删除领域字段关联
     *
     * @param domainId 领域ID
     * @param fieldId  字段ID
     */
    void removeDomainFieldRel(Long domainId, Long fieldId);

    /**
     * 获得领域的字段关联列表
     *
     * @param domainId 领域ID
     * @return 字段关联列表
     */
    List<DomainFieldRelDO> getDomainFieldRelList(Long domainId);

    /**
     * 拖拽移动领域
     *
     * @param id             移动的领域ID
     * @param targetParentId 目标父领域ID
     * @param targetSort     目标排序位置
     */
    void moveDomain(Long id, Long targetParentId, Integer targetSort);

    /**
     * 批量更新领域排序
     *
     * @param parentId 父领域ID
     * @param sorts    排序映射 (领域ID -> 新排序值)
     */
    void updateDomainSorts(Long parentId, Map<Long, Integer> sorts);

    /**
     * 移动字段到其他领域
     *
     * @param fieldId        字段ID
     * @param sourceDomainId 源领域ID
     * @param targetDomainId 目标领域ID
     * @param required       在目标领域中是否必填
     * @param sort           在目标领域中的排序
     * @param remark         备注
     */
    void moveFieldToDomain(Long fieldId, Long sourceDomainId, Long targetDomainId, 
                          Boolean required, Integer sort, String remark);

    /**
     * 获得指定领域下的字段列表（带字段详情）
     *
     * @param domainId 领域ID
     * @return 字段列表
     */
    List<DomainFieldRelRespVO> getFieldsWithDetailsByDomainId(Long domainId);

    /**
     * 获得指定字段关联的领域列表（带领域详情）
     *
     * @param fieldId 字段ID
     * @return 领域列表
     */
    List<DomainFieldRelRespVO> getDomainsWithDetailsByFieldId(Long fieldId);

    // ================ 统计功能 ================

    /**
     * 获得指定领域的统计信息（递归计算）
     *
     * @param domainId 领域ID
     * @return 统计信息
     */
    DomainStatisticsRespVO getDomainStatistics(Long domainId);

    /**
     * 批量获得多个领域的统计信息
     *
     * @param domainIds 领域ID列表
     * @return 统计信息列表
     */
    List<DomainStatisticsRespVO> getBatchDomainStatistics(Collection<Long> domainIds);

    // ================ 批量处理功能 ================

    /**
     * 批量删除领域（会递归删除所有子领域）
     *
     * @param domainIds 领域ID列表
     */
    void deleteDomainsBatch(Collection<Long> domainIds);

    /**
     * 批量删除字段的所有领域关联关系
     *
     * @param fieldIds 字段ID列表
     */
    void removeFieldDomainRelationsBatch(Collection<Long> fieldIds);

    // ================ 对标Category的批量关联功能 ================

    /**
     * 批量创建字段领域关联
     *
     * @param fieldId   字段ID
     * @param domainIds 领域ID列表
     */
    void batchCreateFieldDomainRels(Long fieldId, Collection<Long> domainIds);

    /**
     * 批量创建字段领域关联（带属性映射）
     *
     * @param fieldId          字段ID
     * @param domainConfigMap  领域ID -> 配置信息的映射
     */
    void batchCreateFieldDomainRels(Long fieldId, Map<Long, DomainFieldConfig> domainConfigMap);

    /**
     * 替换字段的所有领域关联
     *
     * @param fieldId   字段ID
     * @param domainIds 新的领域ID列表
     */
    void replaceFieldDomainRels(Long fieldId, Collection<Long> domainIds);

    /**
     * 替换字段的所有领域关联（带属性映射）
     *
     * @param fieldId          字段ID
     * @param domainConfigMap  领域ID -> 配置信息的映射
     */
    void replaceFieldDomainRels(Long fieldId, Map<Long, DomainFieldConfig> domainConfigMap);

    /**
     * 批量更新字段在领域中的配置
     *
     * @param domainId         领域ID
     * @param fieldConfigMap   字段ID -> 配置信息的映射
     */
    void batchUpdateFieldConfigInDomain(Long domainId, Map<Long, DomainFieldConfig> fieldConfigMap);

    /**
     * 检查字段领域关联是否存在
     *
     * @param fieldId  字段ID
     * @param domainId 领域ID
     * @return 是否存在
     */
    boolean existsFieldDomainRel(Long fieldId, Long domainId);

    /**
     * 获取领域中的下一个字段排序值
     *
     * @param domainId 领域ID
     * @return 下一个排序值
     */
    Integer getNextFieldSortInDomain(Long domainId);

    /**
     * 获取字段在领域中的配置映射
     *
     * @param domainId 领域ID
     * @return 字段ID -> 配置信息的映射
     */
    Map<Long, DomainFieldConfig> getFieldConfigMapInDomain(Long domainId);

    // ================ 对标Category的高级查询功能 ================

    /**
     * 根据路径获取领域及其所有子领域
     *
     * @param treePath 树路径
     * @return 领域列表
     */
    List<DomainDO> getDomainsByTreePath(String treePath);

    /**
     * 根据层级获取领域列表
     *
     * @param level 层级深度
     * @return 领域列表
     */
    List<DomainDO> getDomainsByLevel(Integer level);

    /**
     * 获取只读领域列表
     *
     * @return 只读领域列表
     */
    List<DomainDO> getReadonlyDomains();

    /**
     * 更新领域的树形路径信息
     *
     * @param domainId 领域ID
     */
    void updateDomainTreeInfo(Long domainId);

} 