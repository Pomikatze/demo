package com.example.demo.dto;

import lombok.*;

import java.io.Serializable;
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Page implements Serializable {

    private int totalElement = 5;

    @NonNull
    private Object content;
}
