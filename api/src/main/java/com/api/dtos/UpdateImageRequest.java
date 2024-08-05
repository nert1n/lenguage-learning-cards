package com.api.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateImageRequest {
    private final Long id;
    private final MultipartFile image;
}
