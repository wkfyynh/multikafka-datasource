package com.yynhblog.kafka.config.kafkaconfig.beanconfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;

/**
 * @description: kafka 工厂配置
 * @author: yynh
 * @create: 2022-05-13
 **/
@Data
@Slf4j
public class KafkaExtraConfig {

    private Boolean ackDiscarded = false;//是否可以丢弃消息，默认false

    private Class<? extends KafkaListenerContainerFactory> consumerFactory;//自定义消费工厂

    private long polltimeout = 3000;//消息拉取超时时间

    private Boolean batchListener = false;//批量监听

    private Integer concurrency = 1;//KafkaMessageListenerContainer 实例数量

    private KafkaFactoryFilterConfig factoryFilter;//消费过滤工厂配置

    private Class<? extends ProducerFactory> producerFactory;//自定义生产工厂
}
