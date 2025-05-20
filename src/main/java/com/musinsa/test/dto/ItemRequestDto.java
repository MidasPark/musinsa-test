package com.musinsa.test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemRequestDto {
    @Schema(description = "브랜드 이름", example = "A", maxLength = 100)
    @NotBlank(message = "브랜드 이름은 필수입니다.")
    @Size(max = 100, message = "브랜드 이름은 최대 100자까지 입력 가능합니다.")
    private String brand;

    @Schema(description = "카테고리 이름", example = "신발")
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    @Size(max = 50, message = "브랜드 이름은 최대 50자까지 입력 가능합니다.")
    private String category;

    @Schema(description = "가격", example = "10000")
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "최소 가격은 0 이상이어야 합니다.")
    private Integer price;
}