package com.example.demo.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * The type Feign client config builder.
 */
@Slf4j
public final class FeignClientConfigBuilder {

    public static <T> T feignBuildJson(Class<T> tClass, CommonFeignProperties properties) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(initJacksonEncoder())
                .decoder(initJacksonDecoder())
                .options(new Request.Options(
                        properties.getConnectTimeoutMillis(),
                        TimeUnit.MILLISECONDS,
                        properties.getReadTimeoutMillis(),
                        TimeUnit.MILLISECONDS, true)
                )
                .logger(new Slf4jLogger(CommonFeignConfig.class.getName()))
                .target(tClass, properties.getUrl());
    }

    private static Encoder initJacksonEncoder() {
        return new JacksonEncoder(createObjectMapper());
    }

    private static Decoder initJacksonDecoder() {
        return new JacksonDecoder(createObjectMapper());
    }

    private static ObjectMapper createObjectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();
    }
}
