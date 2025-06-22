package cn.iocoder.yudao.module.system.enums;

/**
 * 动态字段业务类型枚举
 * 如需新增业务，只需在此处补充常量即可。
 */
public enum BizTypeEnum {

    TUNNEL("管廊管理"),
    DEVICE("设备管理"),
    MAINTAIN("维护管理"),
    ENV_MONITOR("环境监测");

    private final String label;

    BizTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
