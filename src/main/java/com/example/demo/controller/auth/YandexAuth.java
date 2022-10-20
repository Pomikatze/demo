package com.example.demo.controller.auth;

import com.example.demo.dto.auth.YandexResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.utils.api.AuthUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Api для аутентификации через Яндекс.
 */
@Tag(name = "Api для аутентификации через Яндекс")
@Controller
@RequiredArgsConstructor
public class YandexAuth {

    @Value("${yandex.id}")
    private String yandexId;

    @Value("${yandex.password}")
    private String yandexPassword;

    private ObjectMapper objectMapper;

    private HttpClient client;

    private final AuthUtil authUtil;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        client = HttpClientBuilder.create().build();
    }

    @Operation(summary = "Отправить запрос на аутентификацию через Яндекс")
    @GetMapping("/yandex/auth")
    public String yandexAuth() {
        return "redirect:https://oauth.yandex.ru/authorize?" +
                "response_type=code&" +
                "client_id=" + yandexId;
    }

    @Operation(summary = "Получение кода от Яндекса")
    @GetMapping("/yan")
    public String yandexOAuth2(@RequestParam String code, HttpServletRequest request) throws IOException {
        HttpPost post = new HttpPost("https://oauth.yandex.ru/token");

        post.addHeader("Content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<>(4);
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", yandexId));
        params.add(new BasicNameValuePair("client_secret", yandexPassword));
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = client.execute(post);
        String result = authUtil.getResultOfResponse(response);

        YandexResponseDto responseDto = objectMapper.readValue(result, YandexResponseDto.class);
        UserEntity user = authUtil.saveYandexUser(responseDto, client);
        authUtil.createAuthToken(new JwtRequest(user.getName(), user.getPassword()), request);
        return "redirect:http://localhost:8082/test/api/base";
    }
}
