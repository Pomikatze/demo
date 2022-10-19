package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.auth.YandexResponseDto;
import com.example.demo.dto.auth.YandexUserDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.RoleService;
import com.example.demo.service.SudisService;
import com.example.demo.service.UserService;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Tag(name = "Api для аутентификации")
@Controller
@RequiredArgsConstructor
public class AuthController {

    @Value("${vk.id}")
    private String vkSecret;

    @Value("${yandex.id}")
    private String yandexId;

    @Value("${yandex.password}")
    private String yandexPassword;

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    private final SudisService sudisService;

    private final AuthenticationManager authenticationManager;

    private final RoleService roleService;

    private final ModelMapper mapper;

    private ObjectMapper objectMapper;

    private HttpClient client;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        client = HttpClientBuilder.create().build();
    }

    @Operation(summary = "Аутентификация и присвоение токена")
    @PostMapping("/auth")
    public String createAuthToken(JwtRequest jwtRequest, HttpServletRequest request) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(),
//                    jwtRequest.getPassword()));
//        } catch (BadCredentialsException e) {
////            return new ResponseEntity<>(new BadCredentialsException("error of auth"), HttpStatus.UNAUTHORIZED);
//            return "error of auth";
//        }

        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUserName());

        String token = jwtTokenUtil.generateToken(userDetails);

        sudisService.saveSudis(jwtRequest.getUserName(), token, request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + token);

        return "redirect:http://localhost:8082/test/api/base";
    }

    @Operation(summary = "Отправить запрос на аутентификацию через ВК")
    @GetMapping("/vk/auth")
    public String vkAuth() {
        return "redirect:https://oauth.vk.com/authorize?" +
                "client_id=" + vkSecret + "&" +
                "display=page&" +
                "redirect_uri=http://localhost:8082/test/oauth2&" +
                "scope=email&" +
                "response_type=code&" +
                "v=5.131";
    }

    @Operation(summary = "Отправить запрос на аутентификацию через Яндекс")
    @GetMapping("/yandex/auth")
    public String yandexAuth() {
        return "redirect:https://oauth.yandex.ru/authorize?" +
                "response_type=code&" +
                "client_id=" + yandexId;
    }

    @Operation(summary = "Отправить запрос на аутентификацию через Гугл")
    @GetMapping("/google/auth")
    public String googleAuth() {
        return "redirect:https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?" +
                "response_type=code" +
                "&client_id=690893922086-4l19on1j5ltldr2541ioa05p8v2qngqc.apps.googleusercontent.com&" +
                "scope=openid%20profile%20email&" +
                "state=1xMqu3wuXKpZvwjwWYnaTlBZ1HFW7lJNpsr3WNBrlQc%3D&" +
                "redirect_uri=http%3A%2F%2Flocalhost%3A8082%2Ftest%2Fgoogle&" +
                "nonce=cP2bBHnF8m5EQkzuNT92ab0tEqrINiXMWaGK0_KSu5k&" +
                "flowName=GeneralOAuthFlow";
    }

    @Operation(summary = "Получение кода от ВК")
    @GetMapping("/oauth2")
    public String VKoAuth2(@RequestParam String code) {
        return code;
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
        String result = getResultOfResponse(response);

        YandexResponseDto responseDto = objectMapper.readValue(result, YandexResponseDto.class);
        UserEntity user = saveYandexUser(responseDto);
        createAuthToken(new JwtRequest(user.getName(), user.getPassword()), request);
        return "redirect:http://localhost:8082/test/api/base";
    }

    @Operation(summary = "Получение кода от Гугл")
    @GetMapping("/google")
    public String googleOAuth2(HttpServletRequest request) {
        createAuthToken(new JwtRequest("GoogleUser", "123"), request);
        return "redirect:http://localhost:8082/test/api/base";
    }

    @Operation(summary = "Открыть форму для регистрации")
    @GetMapping("/reg")
    public String registration(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "registration";
    }

    @Operation(summary = "Сохранить нового пользователя")
    @PostMapping("/save")
    public String saveUser(UserDto user) {

        if (userService.existsByName(user.getName())) {
            throw new RuntimeException("Такой пользователь уже сущестует");
        }

        saveUserToBase(user);
        return "redirect:http://localhost:8082/test/api/auth";
    }

    private String getResultOfResponse(HttpResponse response) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = response.getEntity().getContent().read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    private UserEntity saveYandexUser(YandexResponseDto dto) throws IOException {

        HttpGet get = new HttpGet("https://login.yandex.ru/info?format=json");
        get.addHeader("Authorization", "OAuth " + dto.getToken());
        HttpResponse response = client.execute(get);
        String result = getResultOfResponse(response);
        YandexUserDto yandexUserDto = objectMapper.readValue(result, YandexUserDto.class);

        if (!userService.existsByName(yandexUserDto.getFullName())) {

            UserDto userDto = new UserDto();
            userDto.setName(yandexUserDto.getFullName());
            userDto.setPassword(yandexUserDto.getLogin());
            return saveUserToBase(userDto);

        } else {
            return userService.findByUsername(yandexUserDto.getFullName())
                    .orElseThrow(() -> new DemoException("Пользователь не найден"));
        }
    }

    private UserEntity saveUserToBase(UserDto user) {
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");
        roleService.save(role);

        UserEntity userEntity = mapper.map(user, UserEntity.class);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(role);
        userEntity.setRole(roles);
        userEntity.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        userService.saveEntity(userEntity);

        return userEntity;
    }
}