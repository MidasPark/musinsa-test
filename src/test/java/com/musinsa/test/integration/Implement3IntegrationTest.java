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
class Implement3IntegrationTest {

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

        Item shirtB = Item.builder()
                .brand("B")
                .category("상의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shirtC = Item.builder()
                .brand("C")
                .category("상의")
                .price(15000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsA = Item.builder()
                .brand("A")
                .category("하의")
                .price(18000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pantsB = Item.builder()
                .brand("B")
                .category("하의")
                .price(25000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirtA);
        itemRepository.save(shirtB);
        itemRepository.save(shirtC);
        itemRepository.save(pantsA);
        itemRepository.save(pantsB);
    }

    @Test
    @DisplayName("카테고리별 최저가, 최고가 브랜드 조회 API 통합 테스트 - 상의")
    void lowestHighestPriceShirtIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/implement3/lowest_highest")
                .param("category", "상의")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.카테고리").value("상의"))
                .andExpect(jsonPath("$.최저가").isArray())
                .andExpect(jsonPath("$.최저가.length()").value(1))
                .andExpect(jsonPath("$.최저가[0].brand").value("A"))
                .andExpect(jsonPath("$.최저가[0].price").value("10,000"))
                .andExpect(jsonPath("$.최고가").isArray())
                .andExpect(jsonPath("$.최고가.length()").value(1))
                .andExpect(jsonPath("$.최고가[0].brand").value("B"))
                .andExpect(jsonPath("$.최고가[0].price").value("20,000"));
    }

    @Test
    @DisplayName("카테고리별 최저가, 최고가 브랜드 조회 API 통합 테스트 - 하의")
    void lowestHighestPricePantsIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/implement3/lowest_highest")
                .param("category", "하의")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.카테고리").value("하의"))
                .andExpect(jsonPath("$.최저가").isArray())
                .andExpect(jsonPath("$.최저가.length()").value(1))
                .andExpect(jsonPath("$.최저가[0].brand").value("A"))
                .andExpect(jsonPath("$.최저가[0].price").value("18,000"))
                .andExpect(jsonPath("$.최고가").isArray())
                .andExpect(jsonPath("$.최고가.length()").value(1))
                .andExpect(jsonPath("$.최고가[0].brand").value("B"))
                .andExpect(jsonPath("$.최고가[0].price").value("25,000"));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 조회 시 404 반환 테스트")
    void nonExistentCategoryIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/implement3/lowest_highest")
                .param("category", "미존재카테고리")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 카테고리 입니다."));
    }

    @Test
    @DisplayName("카테고리는 존재하지만 상품이 없을 때 404 반환 테스트")
    void emptyCategoryIntegrationTest() throws Exception {
        itemRepository.deleteAll();

        Item shirtA = Item.builder()
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        itemRepository.save(shirtA);
        itemRepository.deleteAll();

        mockMvc.perform(get("/api/implement3/lowest_highest")
                .param("category", "상의")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("카테고리 파라미터 누락 시 400 반환 테스트")
    void missingCategoryParameterIntegrationTest() throws Exception {
        mockMvc.perform(get("/api/implement3/lowest_highest")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}