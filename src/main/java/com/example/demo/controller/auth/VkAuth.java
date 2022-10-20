package com.example.demo.controller.auth;

import com.example.demo.dto.auth.VkResponse;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Api для аутентификации через VK.
 */
@Tag(name = "Api для аутентификации через VK")
@Controller
@RequiredArgsConstructor
public class VkAuth {

    @Value("${vk.id}")
    private String vkId;

    @Value("${vk.key}")
    private String vkKey;

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

    @Operation(summary = "Отправить запрос на аутентификацию через ВК")
    @GetMapping("/vk/auth")
    public String vkAuth() {
        return "redirect:https://oauth.vk.com/authorize?" +
                "client_id=" + vkId +
                "&display=page&" +
                "redirect_uri=http://localhost:8082/test/oauth2&" +
                "scope=email&" +
                "response_type=code&" +
                "v=5.131";
    }

    @Operation(summary = "Получение кода от ВК")
    @GetMapping("/oauth2")
    public String VKoAuth2(@RequestParam String code) throws IOException {
        HttpPost post = new HttpPost("https://oauth.vk.com/access-token");

        post.addHeader("Content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<>(4);
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:8082/test/oauth2"));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", vkId));
        params.add(new BasicNameValuePair("client_secret", vkKey));

        HttpResponse response = client.execute(post);
        String result = authUtil.getResultOfResponse(response);

        VkResponse dto = objectMapper.readValue(result, VkResponse.class);
        return code;
    }
}
