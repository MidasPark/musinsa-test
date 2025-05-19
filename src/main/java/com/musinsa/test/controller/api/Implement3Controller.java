package com.musinsa.test.controller.api;

import com.musinsa.test.dto.ErrorResponseDto;
import com.musinsa.test.dto.LowestHighestResponseDto;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.service.LowestHighestService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 3. 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다.
 * API 실패 시, 실패값과 실패 사유를 전달해야 합니다.
 *
 * 핵심 조건:
 * - 주어진 카테고리명으로 검색 범위를 제한
 *   - 카테고리가 존재하지 않으면 에러 발생
 * - 해당 카테고리의 최저가 브랜드와 가격 및 최고가 브랜드와 가격 찾기
 *   - 최저/최고가 상품은 결과가 여러 개일 수 있는 구조로 보였으나 명확한 기준이 없어 최근 업데이트된 상품 1개로 응답을 고정
 */
@RestController
@RequestMapping("/api/implement3")
@Tag(name = "카테고리", description = "")
@RequiredArgsConstructor
public class Implement3Controller {

    private final LowestHighestService lowestHighestService;

    @Operation(summary = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LowestHighestResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리 또는 상품",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping("/lowest_highest")
    public ResponseEntity<?> lowestHighest(@RequestParam String category) {
        Boolean isValid = lowestHighestService.isValidCategory(category);

        if (!isValid) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Not Found",
                    "존재하지 않는 카테고리 입니다."
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
        }

        try {
            LowestHighestResponseDto response = lowestHighestService.findLowestHighestPrice(category);
            return ResponseEntity.ok(response);
        } catch ( RecordNotFoundException e ) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Not Found",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
        } catch ( Exception e ) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Error",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
        }
    }
}
