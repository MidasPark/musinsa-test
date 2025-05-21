package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.BrandLowestPriceResponseDto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandPriceServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BrandPriceService brandPriceService;

    private List<Item> items;
    private List<String> categories;

    @BeforeEach
    void setUp() {
        categories = Arrays.asList("상의", "하의", "신발");
        items = new ArrayList<>();

        Item shirtA = Item.builder()
                .id(1L)
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsA = Item.builder()
                .id(2L)
                .brand("A")
                .category("하의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoesA = Item.builder()
                .id(3L)
                .brand("A")
                .category("신발")
                .price(30000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shirtB = Item.builder()
                .id(4L)
                .brand("B")
                .category("상의")
                .price(15000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsB = Item.builder()
                .id(5L)
                .brand("B")
                .category("하의")
                .price(18000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoesB = Item.builder()
                .id(6L)
                .brand("B")
                .category("신발")
                .price(28000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        items.add(shirtA);
        items.add(pantsA);
        items.add(shoesA);
        items.add(shirtB);
        items.add(pantsB);
        items.add(shoesB);
    }

    @Test
    @DisplayName("브랜드별 최저가 상품 조회 및 총액 계산 테스트")
    void getLowestPriceBrandTest() {
        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findCategories()).thenReturn(categories);

        BrandLowestPriceResponseDto result = brandPriceService.getLowestPriceBrand();

        assertThat(result).isNotNull();
        assertThat(result.get최저가()).isNotNull();

        assertThat(result.get최저가().get브랜드()).isEqualTo("A");
        assertThat(result.get최저가().get총액()).isEqualTo(PriceFormatter.KorFormat(60000));

        assertThat(result.get최저가().get카테고리()).hasSize(3);
        assertThat(result.get최저가().get카테고리().get(0).get카테고리()).isEqualTo("상의");
        assertThat(result.get최저가().get카테고리().get(0).get가격()).isEqualTo(PriceFormatter.KorFormat(10000));

        assertThat(result.get최저가().get카테고리().get(1).get카테고리()).isEqualTo("하의");
        assertThat(result.get최저가().get카테고리().get(1).get가격()).isEqualTo(PriceFormatter.KorFormat(20000));

        assertThat(result.get최저가().get카테고리().get(2).get카테고리()).isEqualTo("신발");
        assertThat(result.get최저가().get카테고리().get(2).get가격()).isEqualTo(PriceFormatter.KorFormat(30000));

        verify(itemRepository, times(1)).findAll();
        verify(itemRepository, times(1)).findCategories();
    }
}
