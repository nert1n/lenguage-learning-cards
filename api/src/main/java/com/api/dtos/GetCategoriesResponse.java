package com.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetCategoriesResponse {
    private Long id;
    private String name;
    private List<GetCardsResponse> cards;
}
