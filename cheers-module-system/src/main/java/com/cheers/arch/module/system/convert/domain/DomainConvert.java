package com.cheers.arch.module.system.convert.domain;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainExcelVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainSaveReqVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainStatisticsRespVO;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainTreeRespVO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 领域模型 Convert
 *
 * @author cheers
 */
@Mapper
public interface DomainConvert {

    DomainConvert INSTANCE = Mappers.getMapper(DomainConvert.class);

    DomainDO convert(DomainSaveReqVO bean);

    DomainRespVO convert(DomainDO bean);

    List<DomainRespVO> convertList(List<DomainDO> list);

    PageResult<DomainRespVO> convertPage(PageResult<DomainDO> page);

    List<DomainExcelVO> convertList02(List<DomainDO> list);

    /**
     * 将DomainDO转换为带统计信息的DomainRespVO
     */
    default DomainRespVO convertWithStatistics(DomainDO domain, DomainStatisticsRespVO statistics) {
        if (domain == null) {
            return null;
        }
        
        DomainRespVO respVO = convert(domain);
        if (statistics != null) {
            respVO.setDirectFieldCount(statistics.getDirectFieldCount());
            respVO.setDirectChildCount(statistics.getDirectChildCount());
            respVO.setTotalFieldCount(statistics.getTotalFieldCount());
            respVO.setTotalChildCount(statistics.getTotalChildCount());
            respVO.setDepth(statistics.getDepth());
        }
        return respVO;
    }

    /**
     * 将DomainDO转换为树形结构VO（带统计信息）
     */
    default DomainTreeRespVO convertToTree(DomainDO domain, DomainStatisticsRespVO statistics) {
        if (domain == null) {
            return null;
        }
        
        DomainTreeRespVO treeVO = new DomainTreeRespVO();
        
        // 基本信息
        treeVO.setId(domain.getId());
        treeVO.setName(domain.getName());
        treeVO.setCode(domain.getCode());
        treeVO.setParentId(domain.getParentId());
        treeVO.setDescription(domain.getDescription());
        treeVO.setType(domain.getType());
        treeVO.setStatus(domain.getStatus());
        treeVO.setSort(domain.getSort());
        treeVO.setRemark(domain.getRemark());
        treeVO.setCreateTime(domain.getCreateTime());
        
        // Element Plus 树组件字段
        treeVO.setLabel(domain.getName());
        treeVO.setValue(String.valueOf(domain.getId()));
        treeVO.setDisabled(domain.getStatus() == 0); // 禁用状态的节点不可选
        treeVO.setNodeType("domain");
        
        // 统计信息
        if (statistics != null) {
            treeVO.setDirectFieldCount(statistics.getDirectFieldCount());
            treeVO.setDirectChildCount(statistics.getDirectChildCount());
            treeVO.setTotalFieldCount(statistics.getTotalFieldCount());
            treeVO.setTotalChildCount(statistics.getTotalChildCount());
            treeVO.setDepth(statistics.getDepth());
            
            // 根据统计信息设置 isLeaf
            treeVO.setIsLeaf(statistics.getDirectChildCount() == 0);
        } else {
            treeVO.setIsLeaf(false); // 默认不是叶子节点
        }
        
        // 扩展字段
        treeVO.setIcon(getIconByType(domain.getType()));
        treeVO.setExpanded(domain.getParentId() == 0); // 根节点默认展开
        
        return treeVO;
    }
    
    /**
     * 根据领域类型获取图标
     */
    default String getIconByType(String type) {
        if (type == null) {
            return "folder";
        }
        return switch (type.toLowerCase()) {
            case "business" -> "business";
            case "technical" -> "technical";
            case "data" -> "database";
            case "service" -> "service";
            default -> "folder";
        };
    }

} 