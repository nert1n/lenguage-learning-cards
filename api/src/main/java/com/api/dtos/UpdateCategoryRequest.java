package com.api.dtos;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private Long category_id;
    private Long[] card_ids;
}
