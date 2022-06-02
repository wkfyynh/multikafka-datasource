package com.yynhblog.kafka;

import com.yynhblog.kafka.utils.LogPortUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.yynhblog"})
@MapperScan(basePackages = "com.yynhblog.kafka.mapper")
public class KafkaApplication {

    public static void main(String[] args) throws InterruptedException {

        LogPortUtil.dealPort(args);
        SpringApplication.run(KafkaApplication.class, args);
    }

}
