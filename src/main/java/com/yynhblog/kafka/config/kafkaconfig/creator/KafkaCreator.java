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
package com.yynhblog.kafka.config.kafkaconfig.creator;

import com.alibaba.fastjson.JSONObject;
import com.yynhblog.kafka.config.kafkaconfig.beanconfig.KafkaConfigProperies;
import com.yynhblog.kafka.config.kafkaconfig.beanconfig.KafkaFactoryFilterConfig;
import com.yynhblog.kafka.config.kafkaconfig.beanconfig.KafkaDataSourceConfig;
import com.yynhblog.kafka.config.kafkaconfig.beanconfig.KafkaExtraConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KafkaCreator implements InitializingBean {
    @Autowired
    private KafkaConfigProperies properties;//kafka核心配置文件
    @Autowired
    private DefaultListableBeanFactory beanFactory;//springbean 工厂

    private Map<String, KafkaDataSourceConfig> kafkaDataSourceConfig; //

    /**
     * @Description: 初始化kafka多数据源配置
     * @Param: []
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/18
     */
    private void createKafkaDataSourceBean() {
        try {
            createKafkaInstance();
        } catch (Exception e) {
            log.error("初始化kafka config失败 " + e);
        }
    }


    /**
     * @Description: 初始化生产端和消费端
     * @Param: []
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/16
     */
    private void createKafkaInstance() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (!kafkaDataSourceConfig.isEmpty()) {
            for (String server : kafkaDataSourceConfig.keySet()) {
                KafkaDataSourceConfig kafkaDataSourceConfig = this.kafkaDataSourceConfig.get(server);
                Map<String, Object> consumerConf = kafkaDataSourceConfig.getConsumerConf();//核心配置
                Map<String, Object> produceConf = kafkaDataSourceConfig.getProducerConf();
                newKafkaConsumerInstance(kafkaDataSourceConfig, consumerConf);
                newKafkaProducerInstance(kafkaDataSourceConfig, produceConf);
            }
        }
    }

    /**
     * @Description: 实例化生产者，以配置的server下面的produceeConf的数量为基准
     * @Param: [kafkaDataSourceConfig, produceConf]
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/19
     */
    private void newKafkaProducerInstance(KafkaDataSourceConfig kafkaDataSourceConfig, Map<String, Object> produceConf) throws ClassNotFoundException {
        if (!produceConf.isEmpty() && StringUtils.isNoneBlank(kafkaDataSourceConfig.getProducerBeanName()) && produceConf.size() > 6) {
//          暂时不考虑producerExtra中的配置 todo
            custStringToClass(produceConf);
            DefaultKafkaProducerFactory defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(produceConf);
            String producerBeanName = kafkaDataSourceConfig.getProducerBeanName();
            KafkaTemplate<String, String> stringStringKafkaTemplate = new KafkaTemplate<String, String>(defaultKafkaProducerFactory);
            stringStringKafkaTemplate.setBeanName(producerBeanName);
            beanFactory.registerSingleton(producerBeanName, stringStringKafkaTemplate);
            log.info("create kafka kafkatemplate  ,bean id is {} , factory config  is {}", producerBeanName, stringStringKafkaTemplate.getProducerFactory().getConfigurationProperties().toString());
        }

    }

    /**
     * @Description: 实例化消费端， 以配置的consumerExtra为基准
     * @Param: [kafkaDataSourceConfig, consumerConf]
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/14
     */
    private void newKafkaConsumerInstance(KafkaDataSourceConfig kafkaDataSourceConfig, Map<String, Object> consumerConf) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (!consumerConf.isEmpty() && consumerConf.size() > 3) {
            Map<String, KafkaExtraConfig> extra = kafkaDataSourceConfig.getConsumerExtra();
            for (String factory : extra.keySet()) {
//                   代表配置了多个工厂相关
                ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaFac = new ConcurrentKafkaListenerContainerFactory<>();
                KafkaExtraConfig kafkaExtraConfig = extra.get(factory);
                if (kafkaExtraConfig.getConsumerFactory() != null) {
//                       代表使用了自定义工厂
//                    todo 或许可以更细化点，把消费工厂和过滤工厂整合到一起
                    Class<? extends KafkaListenerContainerFactory> consumerFactory = kafkaExtraConfig.getConsumerFactory();
                    kafkaFac = (ConcurrentKafkaListenerContainerFactory<Integer, String>) consumerFactory.newInstance();
                } else {
//                   默认 常用配置
                    kafkaFac.setAckDiscarded(kafkaExtraConfig.getAckDiscarded());//是否开始消息过滤
                    kafkaFac.setConcurrency(kafkaExtraConfig.getConcurrency());
                    kafkaFac.setBatchListener(kafkaExtraConfig.getBatchListener());
                    kafkaFac.getContainerProperties().setPollTimeout(kafkaExtraConfig.getPolltimeout());//拉取超时时间，默认3000
                }
                custStringToClass(consumerConf);
                DefaultKafkaConsumerFactory<Object, Object> defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(consumerConf);
                kafkaFac.setConsumerFactory(defaultKafkaConsumerFactory);
                KafkaFactoryFilterConfig factoryFilter = kafkaExtraConfig.getFactoryFilter();
                if (kafkaExtraConfig.getAckDiscarded()) {
                    setFilterFactory(kafkaFac, factoryFilter);
                }
                beanFactory.registerSingleton(factory, kafkaFac);
                log.info("create kafka consumer ,bean id is {} , properties is {} , factory config is {}", factory, kafkaFac.getContainerProperties().toString(), kafkaFac.getConsumerFactory().getConfigurationProperties().toString());
            }

        }
    }

    /**
     * @Description: 设置工厂过滤策略
     * @Param: [defaultKafkaConsumerFactory]
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/14
     */
    private void setFilterFactory(ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaFac, KafkaFactoryFilterConfig factoryFilter) throws IllegalAccessException, InstantiationException {
        if (factoryFilter != null) {
//           代表配置了过滤策略
            if (factoryFilter.getCustStrategy() != null) {
//               代表使用了自定义策略
                kafkaFac.setRecordFilterStrategy(factoryFilter.getCustStrategy().newInstance());
            } else if (!factoryFilter.getFiltermap().isEmpty()) {
                kafkaFac.setRecordFilterStrategy((ConsumerRecord<Integer, String> consumerRecord) -> {
                    try {
                        Object o = JSONObject.parseObject(consumerRecord.value().toString(), factoryFilter.getSerializerClass());
//                        log.info("序列化的对象为:::" + o);
                        return judgeFiledMap(o, factoryFilter.getFiltermap());
                    } catch (Exception e) {
                        log.error("filter error topic is {},value is {}", consumerRecord.topic(), consumerRecord.value());
                        return true;
                    }
                });
            }
        }
    }


    /**
     * @Description: 计算多条件下的过滤规则
     * @Param: [o, filterMap]
     * @return: boolean
     * @Author: yynh
     * @Date: 2022/5/19
     */
    private boolean judgeFiledMap(Object o, Map<String, String> filterMap) {
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        List<Boolean> execValue = new ArrayList<>();
        for (String filterKey : filterMap.keySet()) {
            boolean execBoo = false;
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                execBoo = getaBoolean(o, filterMap, declaredField, filterKey);
                if (execBoo) {
                    break;//代表这个key满足条件了，不需要再循环了
                }
            }
            if (execBoo) {
                execValue.add(execBoo);//用来计算所有的key对应的条件是否满足，少一个都代表有问题
            } else {
                return true;//这个true代表丢弃消息
            }
        }

        return !(execValue.size() == filterMap.size());//要取反，不然逻辑不对
    }

    /**
     * @Description: 计算每个key是否满足条件，满足就返回true ,否则返回false
     * @Param: [o, filterMap, declaredField, filterKey]
     * @return: boolean
     * @Author: yynh
     * @Date: 2022/5/19
     */
    private boolean getaBoolean(Object o, Map<String, String> filterMap, Field declaredField, String filterKey) {
        if (declaredField.getName().equals(filterKey)) {
            try {
                Object o1 = declaredField.get(o); //value值
                String[] filtervalues = StringUtils.split(filterMap.get(filterKey), ",");
                for (String filtervalue : filtervalues) {
                    if (o1 instanceof String) {
                        if (filtervalue.equals(o1.toString())) {//只要配置的值中有一个满足条件就保留
                            return true;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("反射获取字段失败, 对象为 " + o + "  字段为" + declaredField.getName() + " 过滤的值为 " + filterKey);
                return false;
            }
        }
        return false;
    }

    /**
     * @Description: 消费过滤工厂 简单化配置，需要反射获取字段值和对应的设置的过滤值进行比较
     * @Param: [o, filterFiled, filterString]
     * @return: boolean
     * @Author: yynh
     * @Date: 2022/5/19
     */
    private boolean judgeFiled(Object o, String filterFiled, String filterString) {
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            if (declaredField.getName().equals(filterFiled)) {
                try {
                    Object o1 = declaredField.get(o);
                    if (o1 instanceof String) {
                        if (filterString.equals(o1.toString())) {
                            return false;
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error("反射获取字段失败, 对象为 " + o + "  字段为" + declaredField.getName() + " 过滤的值为 " + filterString);
                    return true;
                }
            }
        }
        return true;
    }


    /**
     * @Description: 由于配置在yml map中的对象默认识别出字符串，需要做下转换
     * @Param: [map]
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/19
     */
    private void custStringToClass(Map<String, Object> map) throws ClassNotFoundException {
        for (String key : map.keySet()) {
//            "org.apache.kafka.common"
            if (map.get(key) instanceof String && StringUtils.containsIgnoreCase(map.get(key).toString(), "org.apache.kafka.common")) {
                map.put(key, Class.forName(map.get(key).toString()));
            }
        }
    }


    /**
     * @Description: 初始化加载配置
     * @Param: []
     * @return: void
     * @Author: yynh
     * @Date: 2022/5/17
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        kafkaDataSourceConfig = properties.getServer();
        createKafkaDataSourceBean();
    }


}
