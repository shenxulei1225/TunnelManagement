package com.cheers.uxdesigner.dal.dataobject.workspace;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 工作台文件 DO
 *
 * @author UX Designer
 */
@TableName("uxd_workspace_file")
@KeySequence("uxd_workspace_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceFileDO extends BaseDO {

    /**
     * 文件ID
     */
    @TableId
    private Long id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 缩略图URL
     */
    private String thumbnail;
    /**
     * 文件内容
     */
    private String content;
    /**
     * 文件分类
     */
    private String category;
    /**
     * 是否收藏
     */
    private Boolean starred;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 所属项目ID
     */
    private Long projectId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 团队ID
     */
    private Long teamId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

} 