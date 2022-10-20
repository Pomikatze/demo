package com.example.demo.controller.auth;

import com.example.demo.jwt.JwtRequest;
import com.example.demo.service.api.TokenCacheService;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Api для аутентификации через Google.
 */
@Tag(name = "Api для аутентификации через Google")
@Controller
@RequiredArgsConstructor
public class GoogleAuth {

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleSecret;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleUri;

    @Value("spring.security.oauth2.client.registration.google.code-verifier")
    private String codeVerifier;

    private final TokenCacheService cacheService;

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

    @Operation(summary = "Отправить запрос на аутентификацию через Гугл")
    @GetMapping("/google/auth")
    public String googleAuth() throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(bytes);
        String secret = Base64.getEncoder().withoutPadding().encodeToString(hash);
        cacheService.put("secret", secret);
        return "redirect:https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?" +
                "response_type=code" +
                "&client_id=" + googleId +
                "&scope=openid%20profile%20email&" +
                "state=1xMqu3wuXKpZvwjwWYnaTlBZ1HFW7lJNpsr3WNBrlQc%3D&" +
                "redirect_uri=" + googleUri +
                "&code_challenge=" + secret +
                "&code_challenge_method=S256";
    }

    @Operation(summary = "Получение кода от Гугл")
    @GetMapping("/google")
    public String googleOAuth2(@RequestParam String code, HttpServletRequest request) throws IOException {
        HttpPost post = new HttpPost("https://oauth2.googleapis.com/token");
        post.addHeader("Content-type", "application/x-www-form-urlencoded");
        String secret = cacheService.getIfPresent("secret")
                .orElseThrow(() -> new RuntimeException("Ошибка кэша"));
        cacheService.invalidate("secret");

        List<NameValuePair> params = new ArrayList<>(6);
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", googleId));
        params.add(new BasicNameValuePair("client_secret", googleSecret));
        params.add(new BasicNameValuePair("redirect_uri", googleSecret));
        params.add(new BasicNameValuePair("code_verifier", secret));
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = client.execute(post);
        String result = authUtil.getResultOfResponse(response);

        // TODO заглушка
        authUtil.createAuthToken(new JwtRequest("GoogleUser", "123"), request);
        return "redirect:http://localhost:8082/test/api/base";
    }
}
