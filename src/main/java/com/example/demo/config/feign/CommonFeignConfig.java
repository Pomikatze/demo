package com.example.demo.config.feign;

import com.example.demo.config.feign.service.TestFeignApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Client configuration.
 */
@Configuration
@ComponentScan("com.example.demo")
@PropertySource(value = "classpath:/feign-pib-application-default.yml",
        factory = YamlPropertyLoaderFactory.class)
public class CommonFeignConfig {

    @Autowired
    private CommonFeignProperties serviceProperties;

    @Bean
    public TestFeignApi testFeignApi() {
        return FeignClientConfigBuilder
                .feignBuildJson(TestFeignApi.class, serviceProperties);
    }
}
