package com.cheers.arch.module.system.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * System 模块的 MyBatis 配置
 *
 * @author 芋道源码
 */
@Configuration
@MapperScan(value = "com.cheers.arch.module.system.dal.mysql", annotationClass = Mapper.class)
public class SystemMybatisConfig {
} 