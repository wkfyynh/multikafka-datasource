package com.yynhblog.kafka.config.kafkaconfig.beanconfig;

import com.yynhblog.kafka.config.kafkaconfig.creator.KafkaCreator;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: kafka 消费参数默认配置
 * @author: yynh
 * @create: 2022-05-16
 **/
@Data
@Slf4j
@Accessors(chain = true)
public class KafkaDataSourceConfig {


    /**
     * @Description: 如果要改默认参数的话需要改对应的判断 todo
     * @Param: {@link KafkaCreator#newKafkaConsumerInstance(KafkaDataSourceConfig, java.util.Map)}
     * @Author: yynh
     * @Date: 2022/5/19
     */
    private Map<String, Object> producerConf = new LinkedHashMap() {
        {
            put("key.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
            put("value.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
            put("acks", "all");
            put("batch.size", "16384");
            put("batch.size", "16384");
            put("buffer.memory", "34554432");
        }
    };//生产者配置,这里是一些默认配置，不能随便改，要改的话初始化判断条件也要改
    private Map<String, Object> consumerConf = new LinkedHashMap() {
        {
            put("key.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class);
            put("value.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class);
            put("auto.offset.reset", "latest");
        }
    };//消费者配置,这里是一些默认配置，不能随便改，要改的话初始化判断条件也要改

    private Map<String, KafkaExtraConfig> consumerExtra = new LinkedHashMap();//工厂配置
    private Map<String, KafkaExtraConfig> producerExtra = new LinkedHashMap();//工厂配置

    private String producerBeanName;//生产端需要单独设置beanname


}
