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
public class BrandLowestPriceDto {
    private String 브랜드;
    private List<CategoryPriceDto> 카테고리;
    private String 총액;
}