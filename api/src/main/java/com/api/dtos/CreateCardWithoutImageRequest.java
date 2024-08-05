package com.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCardWithoutImageRequest {
    private String engtext;
    private String rustext;
}
