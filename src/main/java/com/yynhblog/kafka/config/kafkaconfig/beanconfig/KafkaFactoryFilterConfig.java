package com.yynhblog.kafka.config.kafkaconfig.beanconfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: kafka工厂过滤配置
 * @author: yynh
 * @create: 2022-05-19
 **/
@Data
@Slf4j
public class KafkaFactoryFilterConfig {
    private Class<?> serializerClass;//要序列化成的对象
    private Class<? extends RecordFilterStrategy> custStrategy;//指定自定义策略，上面参数就会失效
    private Map<String, String> filtermap = new LinkedHashMap<>();//过滤的字段集合，key为要对比的字段，value为比较的值，支持逗号分隔

}
