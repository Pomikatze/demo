package com.example.demo.utils;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.auth.YandexResponseDto;
import com.example.demo.dto.auth.YandexUserDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.service.RoleService;
import com.example.demo.service.SudisService;
import com.example.demo.service.UserService;
import com.example.demo.utils.api.AuthUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Утильный класс для авторизации.
 */
@Service
@AllArgsConstructor
public class AuthUtilImpl implements AuthUtil {

    private final RoleService roleService;

    private final ModelMapper mapper;

    private ObjectMapper objectMapper;

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    private final SudisService sudisService;

    @Override
    public String getResultOfResponse(HttpResponse response) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = response.getEntity().getContent().read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    @Override
    public UserEntity saveYandexUser(YandexResponseDto dto, HttpClient client) throws IOException {
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

    @Override
    public void createAuthToken(JwtRequest jwtRequest, HttpServletRequest request) {
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUserName());

        String token = jwtTokenUtil.generateToken(userDetails);

        sudisService.saveSudis(jwtRequest.getUserName(), token, request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + token);
    }

    /**
     * Сохранить пользователя в БД.
     *
     * @param user {@link UserDto}
     * @return {@link UserEntity}
     */
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
