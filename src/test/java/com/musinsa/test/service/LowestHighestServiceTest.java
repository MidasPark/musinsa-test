package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.LowestHighestResponseDto;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.repository.ItemRepository;
import com.musinsa.test.util.PriceFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LowestHighestServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private LowestHighestService lowestHighestService;

    private List<String> categories;
    private Item lowestPriceItem;
    private Item highestPriceItem;

    @BeforeEach
    void setUp() {
        categories = Arrays.asList("상의", "하의", "신발");

        lowestPriceItem = Item.builder()
                .id(1L)
                .brand("A")
                .category("상의")
                .price(10000)
                .build();

        highestPriceItem = Item.builder()
                .id(2L)
                .brand("B")
                .category("상의")
                .price(20000)
                .build();
    }

    @Test
    @DisplayName("유효한 카테고리 확인 테스트")
    void isValidCategoryTest() {
        when(itemRepository.findCategories()).thenReturn(categories);

        Boolean result = lowestHighestService.isValidCategory("상의");

        assertThat(result).isTrue();

        verify(itemRepository, times(1)).findCategories();
    }

    @Test
    @DisplayName("유효하지 않은 카테고리 확인 테스트")
    void isInvalidCategoryTest() {
        when(itemRepository.findCategories()).thenReturn(categories);

        Boolean result = lowestHighestService.isValidCategory("미존재카테고리");

        assertThat(result).isFalse();

        verify(itemRepository, times(1)).findCategories();
    }

    @Test
    @DisplayName("빈 카테고리 확인 테스트")
    void isEmptyCategoryTest() {
        Boolean result = lowestHighestService.isValidCategory("");

        assertThat(result).isFalse();

        verify(itemRepository, never()).findCategories();
    }

    @Test
    @DisplayName("카테고리 목록이 비어있을 때 테스트")
    void isValidCategoryWithEmptyCategoriesTest() {
        when(itemRepository.findCategories()).thenReturn(Collections.emptyList());

        Boolean result = lowestHighestService.isValidCategory("상의");

        assertThat(result).isFalse();

        verify(itemRepository, times(1)).findCategories();
    }

    @Test
    @DisplayName("카테고리별 최저가, 최고가 상품 조회 테스트")
    void findLowestHighestPriceTest() {
        when(itemRepository.findFirstByCategoryOrderByPriceAscUpdatedAtDesc("상의"))
                .thenReturn(Optional.of(lowestPriceItem));
        when(itemRepository.findFirstByCategoryOrderByPriceDescUpdatedAtAsc("상의"))
                .thenReturn(Optional.of(highestPriceItem));

        LowestHighestResponseDto result = lowestHighestService.findLowestHighestPrice("상의");

        assertThat(result).isNotNull();
        assertThat(result.get카테고리()).isEqualTo("상의");

        assertThat(result.get최저가()).hasSize(1);
        assertThat(result.get최저가().get(0).getBrand()).isEqualTo("A");
        assertThat(result.get최저가().get(0).getPrice()).isEqualTo(PriceFormatter.KorFormat(10000));

        assertThat(result.get최고가()).hasSize(1);
        assertThat(result.get최고가().get(0).getBrand()).isEqualTo("B");
        assertThat(result.get최고가().get(0).getPrice()).isEqualTo(PriceFormatter.KorFormat(20000));

        verify(itemRepository, times(1)).findFirstByCategoryOrderByPriceAscUpdatedAtDesc("상의");
        verify(itemRepository, times(1)).findFirstByCategoryOrderByPriceDescUpdatedAtAsc("상의");
    }

    @Test
    @DisplayName("상품이 없을 때 예외 발생 테스트")
    void findLowestHighestPriceWithNoItemsTest() {
        when(itemRepository.findFirstByCategoryOrderByPriceAscUpdatedAtDesc("상의"))
                .thenReturn(Optional.empty());
        when(itemRepository.findFirstByCategoryOrderByPriceDescUpdatedAtAsc("상의"))
                .thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            lowestHighestService.findLowestHighestPrice("상의");
        });

        verify(itemRepository, times(1)).findFirstByCategoryOrderByPriceAscUpdatedAtDesc("상의");
        verify(itemRepository, times(1)).findFirstByCategoryOrderByPriceDescUpdatedAtAsc("상의");
    }
}
