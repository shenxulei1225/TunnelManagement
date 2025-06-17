package cn.iocoder.yudao.module.system.dal.dataobject.region;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 区域 DO
 *
 * @author 芋道源码
 */
@TableName("system_region")
@KeySequence("system_region_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 区域id
     */
    @TableId
    private Long id;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 父区域id
     */
    private Long parentId;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 区域负责人
     */
    private Long leaderUserId;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 区域状态（0正常 1停用）
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * 区域类型
     *
     * 枚举 {@link TODO region_type 对应的类}
     */
    private Integer type;
    /**
     * 所在路段
     */
    private String roadSection;
    /**
     * 起始位置
     */
    private String startPosition;
    /**
     * 结束位置
     */
    private String endPosition;

}