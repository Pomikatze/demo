package com.example.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class YandexUserDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String login;

    @JsonProperty("real_name")
    private String fullName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String sex;

    @JsonProperty("default_email")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate birthday;
}
