package com.musinsa.test.controller.api;

import com.musinsa.test.dto.ErrorResponseDto;
import com.musinsa.test.dto.LowestPriceResponseDto;
import com.musinsa.test.service.LowestPriceService;
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
 * 1. 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다.
 * API 실패 시, 실패값과 실패 사유를 전달해야 합니다.
 *
 * 핵심 조건:
 * - 각 카테고리마다 가장 저렴한 최저가 상품 찾기
 *   - 만약 동일한 최저가 상품이 존재한다면 최근에 업데이트된 상품으로 리턴
 * - 최저가 상품들의 가격 합산
 */
@RestController
@RequestMapping("/api/implement1")
@Tag(name = "카테고리", description = "")
@RequiredArgsConstructor
public class Implement1Controller {

    private final LowestPriceService lowestPriceService;

    @Operation(summary = "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LowestPriceResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping("/category_lowest_price")
    public ResponseEntity<?> categoryLowestPrice() {
        try {
            LowestPriceResponseDto response = lowestPriceService.getLowestPrice();
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
