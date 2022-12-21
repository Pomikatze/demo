package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    @Qualifier("jackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    /**
     * Добавление конверторов.
     *
     * @param converters    converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                converters.remove(converter);
                converters.add(jackson2HttpMessageConverter);
                break;
            }
        }
    }

    /**
     * JsonConverter для feign клиента из шареда.
     *
     * @return MappingJackson2HttpMessageConverter
     */
    @Bean("jsonConverter")
    public MappingJackson2HttpMessageConverter jsonConverter() {
        return jackson2HttpMessageConverter;
    }
}
