package com.api.dtos;

import lombok.Data;

@Data
public class DeleteOneCardRequest {
    private Long category_id;
    private Long card_id;
}
