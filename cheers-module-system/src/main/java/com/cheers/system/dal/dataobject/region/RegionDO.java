package com.cheers.system.dal.dataobject.region;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 区域 DO
 *
 * @author 芋道源码
 */
@TableName(value = "system_region", autoResultMap = true) // 添加 autoResultMap = true，支持 TypeHandler
@Data
@EqualsAndHashCode(callSuper = true)
public class RegionDO extends TenantBaseDO {

    /**
     * 区域编号
     */
    private Long id;
    
    /**
     * 区域名称
     */
    private String name;
    
    /**
     * 父区域编号
     */
    private Long parentId;
    
    /**
     * 显示顺序
     */
    private Integer sort;
    
    /**
     * 所属分类ID
     */
    private Long categoryId;

    /**
     * 区域状态
     */
    private Integer status;
    
    /**
     * 扩展属性（JSON 格式）
     */
    @TableField(value = "extra_attrs", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraAttrs;

} 