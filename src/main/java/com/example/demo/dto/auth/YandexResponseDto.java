package com.example.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YandexResponseDto {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("expires_in")
    private String expired;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String type;
}
