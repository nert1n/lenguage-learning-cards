package com.api.dtos;

import lombok.Data;

@Data
public class UpdateCardRequest {
    private Long id;
    private String engtext;
    private String rustext;
}
