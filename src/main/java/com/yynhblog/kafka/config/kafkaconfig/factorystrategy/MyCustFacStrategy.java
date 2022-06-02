package com.yynhblog.kafka.config.kafkaconfig.factorystrategy;

import com.alibaba.fastjson.JSONObject;
import com.yynhblog.kafka.entity.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

/**
 * @description: 自定义过滤工厂
 * @author: yynh
 * @create: 2022-05-17
 **/
@Slf4j
public class MyCustFacStrategy implements RecordFilterStrategy {
    @Override
    public boolean filter(ConsumerRecord consumerRecord) {
        try {
            TestDto testDto = JSONObject.parseObject(consumerRecord.value().toString(), TestDto.class);
            if (StringUtils.equals(testDto.getName(), "zhangsan")) {
                return false;
            }
        } catch (Exception e) {
            log.error("filter error ", consumerRecord.value());
            return true;
        }

        return true;
    }
}
