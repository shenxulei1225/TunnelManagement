package com.cheers.uxdesigner.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * UX Designer 模块的配置类
 *
 * @author UX Designer
 */
@Configuration
@ComponentScan("com.cheers.uxdesigner")
public class UXDesignerConfiguration {
    // 这个配置类确保 Spring Boot 能够扫描到 com.cheers.uxdesigner 包下的所有组件
} 