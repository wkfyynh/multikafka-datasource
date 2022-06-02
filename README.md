### 项目简介

是一个非常简陋的集成了多数据源数据库（[Dynamic-Datasource](https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter)）、多数据源mongodb、多数据源kafka的一个项目模板。

主要实现了通过配置精简化kafka消费端和生产端的实例化流程，自动生成注入kafkaConsumer和kafkaTemplate对象，支持多个kafkaConsumer和kafkaTemplate对象的实例化。

根据自己当前业务需求开发的，有局限性。适用对接大量第三方数据的简单项目，代码很简单，功能也很不完善，没有进行大量测试，业务场景也不一定通用，不要直接用，核心代码就一个类 ``com.yynhblog.kafka.config.kafkaconfig.creator.KafkaProducerCreator`` ，可以根据需要进行修改完善。

