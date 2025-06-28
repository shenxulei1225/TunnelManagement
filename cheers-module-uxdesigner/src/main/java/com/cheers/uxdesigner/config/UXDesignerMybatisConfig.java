package com.cheers.uxdesigner.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * UX Designer 模块的 MyBatis 配置
 *
 * @author UX Designer
 */
@Configuration
@MapperScan(value = "com.cheers.uxdesigner.dal.mysql", annotationClass = Mapper.class)
public class UXDesignerMybatisConfig {
} 