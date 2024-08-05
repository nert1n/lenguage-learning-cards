package com.api.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddToCategoryRequest {
    private String engtext;
    private String rustext;
    private MultipartFile image;
    private Long catid;
}
