package com.api.dtos;

import lombok.Data;

@Data
public class AddToCatWithoutImageRequest {
    private String engtext;
    private String rustext;
    private Long catid;
}
