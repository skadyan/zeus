package com.cumulativeminds.zeus.infra;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cumulativeminds.zeus.YamlUtil;
import com.hazelcast.config.Config;

@Configuration
@ComponentScan
public class HazelcastTestConfig {
    @Bean
    @ConditionalOnMissingBean()
    public static Config newTestConfig() {
        Properties properties = YamlUtil.loadYaml("hazelcast-instance-config.yaml");
        Config config = new Config("test-node-1");
        config.setProperties(properties);
        return config;
    }
}
