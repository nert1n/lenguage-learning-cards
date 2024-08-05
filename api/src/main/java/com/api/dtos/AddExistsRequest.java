package com.api.dtos;

import lombok.Data;

@Data
public class AddExistsRequest {
    private long cat_id;
    private long card_id;
}
