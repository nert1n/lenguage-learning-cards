package com.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private float level;
    private int swiped;
    private int swipedCategories;
    private int createdCards;
    private int createdCategories;
}
