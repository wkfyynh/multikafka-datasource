package com.yynhblog.kafka.config;

import com.yynhblog.kafka.utils.LogPortUtil;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @description: log4j2spring环境变量配置
 * @author: yynh
 * @create: 2021-10-10
 **/

@Plugin(name = "cust", category = StrLookup.CATEGORY)
public class Log4j2Config extends AbstractLookup implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static Environment environment;

    @Override
    public String lookup(LogEvent event, String key) {
        return LogPortUtil.port;

    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("-----------------init-----------------------");
        environment = configurableApplicationContext.getEnvironment();
    }
}
