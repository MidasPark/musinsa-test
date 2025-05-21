package com.musinsa.test.integration;

import com.musinsa.test.domain.Item;
import com.musinsa.test.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Implement2IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        Item shirtA = Item.builder()
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsA = Item.builder()
                .brand("A")
                .category("하의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoesA = Item.builder()
                .brand("A")
                .category("신발")
                .price(30000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shirtB = Item.builder()
                .brand("B")
                .category("상의")
                .price(15000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsB = Item.builder()
                .brand("B")
                .category("하의")
                .price(18000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoesB = Item.builder()
                .brand("B")
                .category("신발")
                .price(28000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirtA);
        itemRepository.save(pantsA);
        itemRepository.save(shoesA);
        itemRepository.save(shirtB);
        itemRepository.save(pantsB);
        itemRepository.save(shoesB);
    }

    @Test
    @DisplayName("단일 브랜드로 모든 카테고리 상품 구매 시 최저가격 브랜드 조회 API 통합 테스트")
    void lowestPriceBrandIntegrationTest() throws Exception {
        // 상품은 가나다 순으로 나열
        mockMvc.perform(get("/api/implement2/lowest_price_brand")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.최저가").exists())
                .andExpect(jsonPath("$.최저가.브랜드").value("A"))
                .andExpect(jsonPath("$.최저가.카테고리").isArray())
                .andExpect(jsonPath("$.최저가.카테고리.length()").value(3))
                .andExpect(jsonPath("$.최저가.카테고리[0].카테고리").value("상의"))
                .andExpect(jsonPath("$.최저가.카테고리[0].가격").value("10,000"))
                .andExpect(jsonPath("$.최저가.카테고리[1].카테고리").value("신발"))
                .andExpect(jsonPath("$.최저가.카테고리[1].가격").value("30,000"))
                .andExpect(jsonPath("$.최저가.카테고리[2].카테고리").value("하의"))
                .andExpect(jsonPath("$.최저가.카테고리[2].가격").value("20,000"))
                .andExpect(jsonPath("$.최저가.총액").value("60,000"));
    }

    @Test
    @DisplayName("데이터가 없을 때 빈 결과 반환 통합 테스트")
    void emptyDataIntegrationTest() throws Exception {
        itemRepository.deleteAll();

        mockMvc.perform(get("/api/implement2/lowest_price_brand")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.최저가").doesNotExist());
    }

    @Test
    @DisplayName("브랜드가 모든 카테고리를 가지고 있지 않을 때 테스트")
    void incompleteCategoriesIntegrationTest() throws Exception {
        itemRepository.deleteAll();

        Item shirtA = Item.builder()
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirtA);

        Item shirtB = Item.builder()
                .brand("B")
                .category("상의")
                .price(15000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsB = Item.builder()
                .brand("B")
                .category("하의")
                .price(18000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoesB = Item.builder()
                .brand("B")
                .category("신발")
                .price(28000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirtB);
        itemRepository.save(pantsB);
        itemRepository.save(shoesB);

        mockMvc.perform(get("/api/implement2/lowest_price_brand")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.최저가").exists())
                .andExpect(jsonPath("$.최저가.브랜드").value("B"))
                .andExpect(jsonPath("$.최저가.총액").value("61,000"));
    }
}