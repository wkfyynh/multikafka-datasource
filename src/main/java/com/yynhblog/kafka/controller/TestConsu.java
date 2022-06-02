package com.yynhblog.kafka.controller;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @description:  测试类
 * @author: yynh
 * @create: 2022-05-17
 **/
@EnableKafka
@Component
public class TestConsu {


    @KafkaListener(topics = {"test"}, containerFactory = "one",groupId = "custgroup")
    public void listenerone (String data) {
        System.out.println("msg is=================="+data);
    }

}
