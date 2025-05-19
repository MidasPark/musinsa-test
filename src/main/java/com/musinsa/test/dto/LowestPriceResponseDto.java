package com.musinsa.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LowestPriceResponseDto {
    private List<LowestPriceByCategoryDto> lowestPriceItems;
    private String totalPrice;
}
