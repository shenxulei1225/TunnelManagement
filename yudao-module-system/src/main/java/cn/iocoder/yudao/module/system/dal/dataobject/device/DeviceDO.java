package cn.iocoder.yudao.module.system.dal.dataobject.device;

import lombok.*;
import java.util.Map;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

/**
 * 设备档案 DO
 *
 * @author 系统管理员
 */
@TableName(value = "system_device", autoResultMap = true)
@KeySequence("system_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDO extends TenantBaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 设备ID
     */
    @TableId
    private Long id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 父设备ID（支持多级设备管理）
     */
    private Long parentId;

    /**
     * 设备类型ID
     */
    private Long deviceTypeId;

    /**
     * 分类ID (关联字段分类)
     */
    private Long categoryId;

    /**
     * 所属区域ID
     */
    private Long regionId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 设备状态（0正常 1停用 2维修中 3报废）
     */
    private Integer status;

    /**
     * 设备负责人
     */
    private Long responsibleUserId;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 购买日期
     */
    private LocalDateTime purchaseDate;

    /**
     * 安装日期
     */
    private LocalDateTime installDate;

    /**
     * 保修到期日期
     */
    private LocalDateTime warrantyExpireDate;

    /**
     * 设备描述
     */
    private String description;

    /**
     * 动态扩展属性(JSON) - 存储自定义字段数据
     */
    @TableField(value = "extra_attrs", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraAttrs;

    /**
     * 备注
     */
    private String remark;
} 