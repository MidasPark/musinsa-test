package com.musinsa.test.controller.api;

import com.musinsa.test.dto.BrandLowestPriceResponseDto;
import com.musinsa.test.dto.ErrorResponseDto;
import com.musinsa.test.service.BrandPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2. 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다.
 * API 실패 시, 실패값과 실패 사유를 전달해야 합니다.
 *
 * 핵심 조건:
 * - 하나의 브랜드에서 모든 카테고리 상품을 구매
 * - 선택된 브랜드는 시스템 내 모든 카테고리의 상품을 보유해야됨
 * - 각 카테고리에서는 해당 브랜드의 가장 저렴한 상품을 기준으로 총액 계산
 */
@RestController
@RequestMapping("/api/implement2")
@Tag(name = "카테고리", description = "")
@RequiredArgsConstructor
public class Implement2Controller {

    private final BrandPriceService brandPriceService;

    @Operation(summary = "단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = BrandLowestPriceResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping("/lowest_price_brand")
    public ResponseEntity<?> lowestPriceBrand() {
        try {
            BrandLowestPriceResponseDto response = brandPriceService.getLowestPriceBrand();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            int status = HttpStatus.BAD_REQUEST.value();
            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Error",
                    "데이터 조회에 실패했습니다. 잠시 후 다시 시도해주세요."
            );
            return ResponseEntity.status(status).body(errorResponse);
        }
    }
}
