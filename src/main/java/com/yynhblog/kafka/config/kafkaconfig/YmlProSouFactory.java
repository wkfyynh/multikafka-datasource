package com.yynhblog.kafka.config.kafkaconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: yynh
 * @create: 2022-05-15
 **/
@Slf4j
public class YmlProSouFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        PropertySource<?> propertySource = null;
        try {
            List<PropertySource<?>> load = new YamlPropertySourceLoader().load(name, encodedResource.getResource());
            propertySource = new YamlPropertySourceLoader().load(name, encodedResource.getResource()).get(0);
        }catch (Exception e){
            propertySource = new PropertySource<Object>("") {
                @Override
                public Object getProperty(String name) {
                    return null;
                }
            };
            log.error("load resource error "+ e);
        }
        return propertySource;
    }
}
