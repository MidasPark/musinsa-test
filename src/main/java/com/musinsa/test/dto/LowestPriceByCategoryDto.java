package com.musinsa.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LowestPriceByCategoryDto {
    private String category;
    private String brand;
    private String price;
}
