package com.api.dtos;

import lombok.Data;

@Data
public class ChangePasswordCodeRequest {
    private String email;
    private String code;
}
