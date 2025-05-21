package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.LowestPriceByCategoryDto;
import com.musinsa.test.dto.LowestPriceResponseDto;
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
class LowestPriceServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private LowestPriceService lowestPriceService;

    private List<String> categories;
    private Map<String, Item> lowestPriceItems;

    @BeforeEach
    void setUp() {
        categories = Arrays.asList("상의", "하의", "신발");
        
        lowestPriceItems = new HashMap<>();
        
        Item shirt = Item.builder()
                .id(1L)
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
                
        Item pants = Item.builder()
                .id(2L)
                .brand("B")
                .category("하의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
                
        Item shoes = Item.builder()
                .id(3L)
                .brand("C")
                .category("신발")
                .price(30000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
                
        lowestPriceItems.put("상의", shirt);
        lowestPriceItems.put("하의", pants);
        lowestPriceItems.put("신발", shoes);
    }

    @Test
    @DisplayName("카테고리별 최저가 상품 조회 및 총액 계산 테스트")
    void getLowestPriceTest() {
        when(itemRepository.findCategories()).thenReturn(categories);
        
        for (String category : categories) {
            when(itemRepository.findFirstByCategoryOrderByPriceAscUpdatedAtDesc(category))
                    .thenReturn(Optional.of(lowestPriceItems.get(category)));
        }

        LowestPriceResponseDto result = lowestPriceService.getLowestPrice();

        assertThat(result).isNotNull();
        assertThat(result.getLowestPriceItems()).hasSize(3);
        
        assertThat(result.getTotalPrice()).isEqualTo(PriceFormatter.KorFormat(60000));
        
        List<LowestPriceByCategoryDto> items = result.getLowestPriceItems();
        assertThat(items.get(0).getCategory()).isEqualTo("상의");
        assertThat(items.get(0).getBrand()).isEqualTo("A");
        assertThat(items.get(0).getPrice()).isEqualTo(PriceFormatter.KorFormat(10000));
        
        assertThat(items.get(1).getCategory()).isEqualTo("하의");
        assertThat(items.get(1).getBrand()).isEqualTo("B");
        assertThat(items.get(1).getPrice()).isEqualTo(PriceFormatter.KorFormat(20000));
        
        assertThat(items.get(2).getCategory()).isEqualTo("신발");
        assertThat(items.get(2).getBrand()).isEqualTo("C");
        assertThat(items.get(2).getPrice()).isEqualTo(PriceFormatter.KorFormat(30000));
        
        verify(itemRepository, times(1)).findCategories();
        for (String category : categories) {
            verify(itemRepository, times(1)).findFirstByCategoryOrderByPriceAscUpdatedAtDesc(category);
        }
    }
}
