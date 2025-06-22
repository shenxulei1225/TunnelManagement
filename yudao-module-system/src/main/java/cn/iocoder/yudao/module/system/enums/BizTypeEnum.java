package cn.iocoder.yudao.module.system.enums;

/**
 * 动态字段业务类型枚举
 * 如需新增业务，只需在此处补充常量即可。
 */
public enum BizTypeEnum {
    REGION("区域"),
    DEVICE("设备"),
    MAINTAIN("维护"),
    ENV_MONITOR("环境")
    ;

    private final String label;

    BizTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
