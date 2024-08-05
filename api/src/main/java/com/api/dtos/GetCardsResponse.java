package com.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetCardsResponse {
    private Long id;
    private String engtext;
    private String rustext;
    private byte[] image;
    private boolean isInFavorites;
}
