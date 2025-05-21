package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.ItemRequestDto;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item testItem;
    private ItemRequestDto testItemRequestDto;

    @BeforeEach
    void setUp() {
        testItem = Item.builder()
                .id(1L)
                .brand("TestBrand")
                .category("TestCategory")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        testItemRequestDto = new ItemRequestDto();
        testItemRequestDto.setBrand("TestBrand");
        testItemRequestDto.setCategory("TestCategory");
        testItemRequestDto.setPrice(10000);
    }

    @Test
    @DisplayName("상품 ID로 조회 테스트")
    void getItemByIdTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));

        Item result = itemService.getItemById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBrand()).isEqualTo("TestBrand");
        assertThat(result.getCategory()).isEqualTo("TestCategory");
        assertThat(result.getPrice()).isEqualTo(10000);

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회 시 예외 발생 테스트")
    void getItemByIdNotFoundTest() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            itemService.getItemById(999L);
        });

        verify(itemRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("상품 생성 테스트")
    void createItemTest() {
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        Item result = itemService.createItem(testItemRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBrand()).isEqualTo("TestBrand");
        assertThat(result.getCategory()).isEqualTo("TestCategory");
        assertThat(result.getPrice()).isEqualTo(10000);

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("상품 생성 시 유효성 검사 실패 테스트 - 가격 누락")
    void createItemValidationFailPriceTest() {
        ItemRequestDto invalidDto = new ItemRequestDto();
        invalidDto.setBrand("TestBrand");
        invalidDto.setCategory("TestCategory");

        assertThrows(IllegalArgumentException.class, () -> {
            itemService.createItem(invalidDto);
        });

        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @DisplayName("상품 생성 시 유효성 검사 실패 테스트 - 브랜드 누락")
    void createItemValidationFailBrandTest() {
        ItemRequestDto invalidDto = new ItemRequestDto();
        invalidDto.setCategory("TestCategory");
        invalidDto.setPrice(10000);

        assertThrows(IllegalArgumentException.class, () -> {
            itemService.createItem(invalidDto);
        });

        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @DisplayName("상품 생성 시 유효성 검사 실패 테스트 - 카테고리 누락")
    void createItemValidationFailCategoryTest() {
        ItemRequestDto invalidDto = new ItemRequestDto();
        invalidDto.setBrand("TestBrand");
        invalidDto.setPrice(10000);

        assertThrows(IllegalArgumentException.class, () -> {
            itemService.createItem(invalidDto);
        });

        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @DisplayName("상품 업데이트 테스트")
    void updateItemTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);

        ItemRequestDto updateDto = new ItemRequestDto();
        updateDto.setBrand("UpdatedBrand");
        updateDto.setCategory("UpdatedCategory");
        updateDto.setPrice(20000);

        Item result = itemService.updateItem(updateDto, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("UpdatedBrand");
        assertThat(result.getCategory()).isEqualTo("UpdatedCategory");
        assertThat(result.getPrice()).isEqualTo(20000);

        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품 업데이트 시 예외 발생 테스트")
    void updateItemNotFoundTest() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            itemService.updateItem(testItemRequestDto, 999L);
        });

        verify(itemRepository, times(1)).findById(999L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void deleteItemTest() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        doNothing().when(itemRepository).delete(any(Item.class));

        itemService.deleteItem(1L);

        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).delete(any(Item.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시 예외 발생 테스트")
    void deleteItemNotFoundTest() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            itemService.deleteItem(999L);
        });

        verify(itemRepository, times(1)).findById(999L);
        verify(itemRepository, never()).delete(any(Item.class));
    }
}
