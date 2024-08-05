package com.api.dtos;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private long[] card_ids;
}
