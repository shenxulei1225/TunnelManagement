package com.cheers.framework.monitor.config;

import com.cheers.framework.monitor.core.aop.BizTraceAspect;
import com.cheers.framework.monitor.core.filter.TraceFilter;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * 链路追踪配置类
 */
@AutoConfiguration
@ConditionalOnClass({BizTraceAspect.class})
@EnableConfigurationProperties(MonitorProperties.class)
@ConditionalOnProperty(prefix = "cheers.monitor", name = "tracer-enable", matchIfMissing = true)
public class TracerAutoConfiguration {

    @Bean
    public BizTraceAspect bizTracingAop() {
        return new BizTraceAspect(tracer());
    }

    @Bean
    public Tracer tracer() {
        // 创建 SkywalkingTracer 对象
        SkywalkingTracer tracer = new SkywalkingTracer();
        // 设置为 GlobalTracer 的追踪器
        GlobalTracer.register(tracer);
        return tracer;
    }

    /**
     * 创建 TraceFilter 过滤器，响应 header 设置 traceId
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilter() {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }

} 