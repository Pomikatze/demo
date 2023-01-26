package com.example.demo.responseAdvice.advice;

import com.example.demo.responseAdvice.response.ResponseWrapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Авто-конфигурация для обертки REST response в {@link ResponseWrapper}.
 */
//@Configuration
//@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class PILResponseWrapperConfiguration {

    /**
     * Создает бин-обработчик для REST response.
     *
     * @return {@link PILResponseWrapperAdvice}
     */
//    @Bean
//    @ConditionalOnProperty(prefix = "application", name = "baseControllerPackage")
//    @ConditionalOnMissingBean
//    public PILResponseWrapperAdvice pilResponseWrapperAdvice() {
//        return new PILResponseWrapperAdvice();
//    }
}
