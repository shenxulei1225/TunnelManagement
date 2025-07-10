package com.cheers.framework.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 监控配置类
 */
@ConfigurationProperties("cheers.monitor")
@Data
public class MonitorProperties {
    
    /**
     * 是否启用监控，默认为 true
     */
    private boolean enable = true;
    
    /**
     * 是否启用指标收集，默认为 true
     */
    private boolean metricsEnable = true;
    
    /**
     * 是否启用链路追踪，默认为 true
     */
    private boolean tracerEnable = true;
    
} 