package com.musinsa.test.controller.api;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.*;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 4. 운영자는 새로운 브랜드를 등록하고, 모든 브랜드의 상품을 추가, 변경, 삭제할 수 있어야 합니다.
 *
 * 핵심 조건:
 * - 생성
 *   - 중복에 대한 처리는 고려하지 않음. 실제로 구현시에는 꼭 확인이 필요함
 * - 갱신
 *   - ID 존재 유무만 체크
 * - 삭제
 *   - ID 존재 유무만 체크
 */
@RestController
@RequestMapping("/api/implement4")
@Tag(name = "상품", description = "")
@RequiredArgsConstructor
public class Implement4Controller {

    private final ItemService itemService;

    @Operation(summary = "새로운 상품을 추가한다.", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LowestHighestResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PostMapping("/item")
    public ResponseEntity<?> insert(
            @Valid @RequestBody ItemRequestDto requestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("입력 값에 오류가 있습니다.");

            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Validation Error",
                    errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            Item item = itemService.createItem(requestDto);

            SimpleSuccessResponseDto response = new SimpleSuccessResponseDto("정상적으로 저장되었습니다. ID: " + item.getId());
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        } catch ( Exception e ) {
            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Error",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
        }
    }

    @Operation(summary = "기존의 상품을 갱신 한다.", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LowestHighestResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상품",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PutMapping("/item/{itemId}")
    public ResponseEntity<?> update(
            @Valid @RequestBody ItemRequestDto requestDto,
            BindingResult bindingResult,
            @PathVariable long itemId
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                    .orElse("입력 값에 오류가 있습니다.");

            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    "Validation Error",
                    errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            itemService.updateItem(requestDto, itemId);

            SimpleSuccessResponseDto response = new SimpleSuccessResponseDto("정상적으로 갱신되었습니다.");
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
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

    @Operation(summary = "기존의 상품을 삭제한다.", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = LowestHighestResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 상품",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<?> delete(@PathVariable long itemId) {
        try {
            itemService.deleteItem(itemId);
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

        SimpleSuccessResponseDto response = new SimpleSuccessResponseDto("정상적으로 삭제되었습니다.");
        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
