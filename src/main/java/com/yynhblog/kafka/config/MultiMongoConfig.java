package com.yynhblog.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;


/**
 * @description: mongodb 多数据源配置
 * @author: yynh
 * @create: 2020-11-15
 **/
//@Configuration
public class MultiMongoConfig {
    @Value("${spring.data.mongodb.primary.uri}")
    private String uri;
    @Value("${spring.data.mongodb.secondary.uri}")
    private String uri2;


    @Bean
    @Primary
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }

    /**
     * @Description: 去除 _class
     * @Param: []
     * @return: org.springframework.data.mongodb.core.convert.MappingMongoConverter
     * @Author: yynh
     * @Date: 2020/11/13
     */
    @Bean
    @Primary
    public MappingMongoConverter mappingMongoConverter() {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    @Primary
    public MongoDatabaseFactory dbFactory() {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

    @Bean(name = "primaryMongoTemplate")
    @Primary
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(dbFactory(), this.mappingMongoConverter());
    }

    // ==================== 连接到 mongodb2 服务器 ======================================
    @Bean
    public MongoMappingContext mongoMappingContext2() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }

    /**
     * @Description: 去除 _class
     * @Param: []
     * @return: org.springframework.data.mongodb.core.convert.MappingMongoConverter
     * @Author: yynh
     * @Date: 2020/11/15
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter2() {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory2());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext2());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    public MongoDatabaseFactory dbFactory2() {
        return new SimpleMongoClientDatabaseFactory(uri2);
    }

    @Bean(name = "secondaryMongoTemplate")
    public MongoTemplate mongoTemplate2() {
        return new MongoTemplate(this.dbFactory2(), this.mappingMongoConverter2());
    }

}
