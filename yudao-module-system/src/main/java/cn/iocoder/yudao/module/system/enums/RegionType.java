package cn.iocoder.yudao.module.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区域类型枚举
 */
@Getter
@AllArgsConstructor
public enum RegionType {
    TUNNEL_GALLERY(1, "管廊"),
    PIPELINE_SEGMENT(2, "管道段"),
    FIRE_ZONE(3, "防火区"),



    PERSONNEL_ENTRANCE_EXIT(7, "人员出入口"),
    HOISTING_PORT(8, "吊装口"),
    ESCAPE_HATCH(9, "逃生口"),
    VENTILATION_VENT(10, "通风口"),
    PIPELINE_BRANCH_PORT(11, "管线分支口"),
    SUPPORTS_HANGERS(12,"支吊架"),
    WATERPROOFING_DRAINAGE(13, "防排水设施"),
    INSPECTION_ROAD_AIR_DUCT(14, "检修道及风道等构筑物"),
    OTHER(20, "其他");

    private final int id;
    private final String description;
}
