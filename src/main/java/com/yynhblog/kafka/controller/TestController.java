package com.yynhblog.kafka.controller;

import com.alibaba.fastjson.JSON;
import com.yynhblog.kafka.entity.TestDto;
import com.yynhblog.kafka.service.impl.TestService;
import com.yynhblog.kafka.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 生产端测试
 * @author: yynh
 * @create: 2022-05-14
 **/
@RestController
@RequestMapping(value = "/test")
@CrossOrigin
@Slf4j
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired(required = false)
    @Qualifier("produ")
    KafkaTemplate kafkaTemplate;

    @Autowired(required = false)
    @Qualifier("produ22")
    KafkaTemplate kafkaTemplate2;


    @GetMapping("/test")
    public Object batchQueryLastAV() {
        TestDto testDto = new TestDto();
        testDto.setMsg("今天");
        testDto.setName("lisi");
        ListenableFuture aaaaaaaaaa = kafkaTemplate.send("test", JSON.toJSONString(testDto));
        return ResultUtil.success(testService.test());
    }

    @GetMapping("/test2")
    public Object batchQueryLastAV2() {
        TestDto testDto = new TestDto();
        testDto.setMsg("999999999");
        testDto.setName("zhangsan");
        ListenableFuture aaaaaaaaaa = kafkaTemplate2.send("test2", JSON.toJSONString(testDto));
        return ResultUtil.success(testService.test());
    }
}
