package com.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CreateCardRequest {
    private String engtext;
    private String rustext;
    private MultipartFile image;
}
