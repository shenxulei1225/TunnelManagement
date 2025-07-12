package com.cheers.arch.module.uxdesigner.dal.dataobject.workspace;

import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 工作台项目 DO
 *
 * @author UX Designer
 */
@TableName("uxd_workspace_project")
@KeySequence("uxd_workspace_project_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceProjectDO extends BaseDO {

    /**
     * 项目ID
     */
    @TableId
    private Long id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 项目封面
     */
    private String cover;
    /**
     * 项目类型
     */
    private String type;
    /**
     * 项目状态
     */
    private Integer status;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 团队ID
     */
    private Long teamId;
    /**
     * 是否收藏
     */
    private Boolean starred;
    /**
     * 备注
     */
    private String remark;

} 