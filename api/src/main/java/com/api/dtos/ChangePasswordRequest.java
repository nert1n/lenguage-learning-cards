package com.api.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;
    private String newPassword;
}
