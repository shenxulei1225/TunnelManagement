package com.cheers.framework.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * WebSocket 配置项
 */
@ConfigurationProperties("cheers.websocket")
@Data
@Validated
public class WebSocketProperties {

    /**
     * WebSocket 的连接路径
     */
    @NotEmpty(message = "WebSocket 的连接路径不能为空")
    private String path = "/ws";

    /**
     * 消息发送器的类型
     *
     * 可选值：local、redis、rocketmq、kafka、rabbitmq
     */
    @NotNull(message = "WebSocket 的消息发送者不能为空")
    private String senderType = "local";

    /**
     * Redis 配置
     */
    private Redis redis = new Redis();

    /**
     * RocketMQ 配置
     */
    private RocketMQ rocketmq = new RocketMQ();

    /**
     * RabbitMQ 配置
     */
    private RabbitMQ rabbitmq = new RabbitMQ();

    /**
     * Kafka 配置
     */
    private Kafka kafka = new Kafka();

    @Data
    public static class Redis {
        /**
         * Channel
         */
        @NotEmpty(message = "Redis Channel不能为空")
        private String channel = "websocket.channel";
    }

    @Data
    public static class RocketMQ {
        /**
         * Topic
         */
        @NotEmpty(message = "RocketMQ Topic不能为空")
        private String topic = "websocket";

        /**
         * Consumer Group
         */
        @NotEmpty(message = "RocketMQ Consumer Group不能为空")
        private String consumerGroup = "websocket-consumer";
    }

    @Data
    public static class RabbitMQ {
        /**
         * Exchange
         */
        @NotEmpty(message = "RabbitMQ Exchange不能为空")
        private String exchange = "websocket.exchange";

        /**
         * Queue
         */
        @NotEmpty(message = "RabbitMQ Queue不能为空")
        private String queue = "websocket.queue";
    }

    @Data
    public static class Kafka {
        /**
         * Topic
         */
        @NotEmpty(message = "Kafka Topic不能为空")
        private String topic = "websocket";

        /**
         * Consumer Group
         */
        @NotEmpty(message = "Kafka Consumer Group不能为空")
        private String consumerGroup = "websocket-consumer";
    }
} 