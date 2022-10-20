package com.example.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VkResponse {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("expires_in")
    private Long expired;

    @JsonProperty("user_id")
    private Long userId;

    private String error;

    @JsonProperty("error_description")
    private String description;
}
