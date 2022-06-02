/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yynhblog.kafka.config.kafkaconfig.beanconfig;

import com.yynhblog.kafka.config.kafkaconfig.YmlProSouFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 从yml文件中计息kafka属性
 * @Param:
 * @return:
 * @Author: yynh
 * @Date: 2022/5/17
 */
@Slf4j
@Getter
@Setter
@PropertySource(value = {"classpath:application-kafkaconfig.yml"}, factory = YmlProSouFactory.class)
@ConfigurationProperties(prefix = "kafka.cust")
@Configuration
public class KafkaConfigProperies {
    @NestedConfigurationProperty
    private Map<String, KafkaDataSourceConfig> server = new LinkedHashMap<>();
}
