package com.example.demo.config;

import com.fasterxml.jackson.databind.MapperFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JacksonConfig {

    /**
     * Конфигурация Jackson2HttpMessageConverter для нормального отображения
     * объекта типа Page при использовании @JsonView.
     *
     * @return MappingJackson2HttpMessageConverter
     */
    @Bean("jackson2HttpMessageConverter")
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder()
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .featuresToEnable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .defaultViewInclusion(true);
        return new MappingJackson2HttpMessageConverter(mapperBuilder.build());
    }
}
