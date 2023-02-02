package com.example.demo.controller;

import com.example.demo.config.JacksonConfig;
import com.example.demo.config.SecurityConfig;
import com.example.demo.config.WebMvcConfig;
import com.example.demo.dto.UserDto;
import com.example.demo.jwt.JwtRequestFilter;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.example.demo.ResponseBodyMatchers.responseBody;
import static com.example.demo.maker.dto.UserDtoMaker.USER_DTO_INSTANTIATOR;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
        })
@AutoConfigureMockMvc
@Import({WebMvcConfig.class, JacksonConfig.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void getAll() {
        List<UserDto> response = Collections.singletonList(make(a(USER_DTO_INSTANTIATOR)));

        when(userService.findAllDto()).thenReturn(response);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(response, new TypeReference<List<UserDto>>() {
                }));

        verify(userService, times(1)).findAllDto();
    }
}
