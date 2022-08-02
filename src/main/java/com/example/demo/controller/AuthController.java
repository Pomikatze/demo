package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.service.RoleService;
import com.example.demo.service.SudisService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Tag(name = "Api для аутентификации")
@Controller
@RequiredArgsConstructor
public class AuthController {

    @Value("${vk.id}")
    private String vkSecret;

    @Value("${yandex.id}")
    private String yandexSecret;

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    private final SudisService sudisService;

    private final AuthenticationManager authenticationManager;

    private final RoleService roleService;

    private final ModelMapper mapper;

    @Operation(summary = "Аутентификация и присвоение токена")
    @PostMapping("/auth")
    public String createAuthToken(JwtRequest jwtRequest, HttpServletRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(),
                    jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(new BadCredentialsException("error of auth"), HttpStatus.UNAUTHORIZED);
            return "error of auth";
        }

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
                "response_type=token&" +
                "client_id=" + yandexSecret;
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
    public String yandexOAuth2(HttpServletRequest request) {
        createAuthToken(new JwtRequest("YandexUser", "123"), request);
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
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");
        roleService.save(role);
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(role);
        userEntity.setRole(roles);
        userEntity.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        userService.saveEntity(userEntity);
        return "redirect:http://localhost:8082/test/api/auth";
    }
}
