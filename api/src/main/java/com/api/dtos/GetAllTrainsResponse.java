package com.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllTrainsResponse {
    private Long id;
    private String name;
}
