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
public class LowestHighestResponseDto {
    private String 카테고리;
    private List<ItemBrandPriceDto> 최저가;
    private List<ItemBrandPriceDto> 최고가;
}
