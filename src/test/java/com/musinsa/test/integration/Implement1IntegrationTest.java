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
class Implement1IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        Item shirt1 = Item.builder()
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shirt2 = Item.builder()
                .brand("B")
                .category("상의")
                .price(15000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pants1 = Item.builder()
                .brand("A")
                .category("하의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item pants2 = Item.builder()
                .brand("B")
                .category("하의")
                .price(18000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoes1 = Item.builder()
                .brand("A")
                .category("신발")
                .price(30000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Item shoes2 = Item.builder()
                .brand("B")
                .category("신발")
                .price(28000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirt1);
        itemRepository.save(shirt2);
        itemRepository.save(pants1);
        itemRepository.save(pants2);
        itemRepository.save(shoes1);
        itemRepository.save(shoes2);
    }

    @Test
    @DisplayName("카테고리별 최저가 상품 조회 API 통합 테스트")
    void categoryLowestPriceIntegrationTest() throws Exception {
        // 상품은 가나다 순으로 나열
        mockMvc.perform(get("/api/implement1/category_lowest_price")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lowestPriceItems").isArray())
                .andExpect(jsonPath("$.lowestPriceItems.length()").value(3))
                .andExpect(jsonPath("$.lowestPriceItems[0].category").value("상의"))
                .andExpect(jsonPath("$.lowestPriceItems[0].brand").value("A"))
                .andExpect(jsonPath("$.lowestPriceItems[0].price").value("10,000"))
                .andExpect(jsonPath("$.lowestPriceItems[1].category").value("신발"))
                .andExpect(jsonPath("$.lowestPriceItems[1].brand").value("B"))
                .andExpect(jsonPath("$.lowestPriceItems[1].price").value("28,000"))
                .andExpect(jsonPath("$.lowestPriceItems[2].category").value("하의"))
                .andExpect(jsonPath("$.lowestPriceItems[2].brand").value("B"))
                .andExpect(jsonPath("$.lowestPriceItems[2].price").value("18,000"))
                .andExpect(jsonPath("$.totalPrice").value("56,000"));
    }

    @Test
    @DisplayName("데이터가 없을 때 빈 결과와 0원 반환 통합 테스트")
    void emptyDataIntegrationTest() throws Exception {
        itemRepository.deleteAll();

        mockMvc.perform(get("/api/implement1/category_lowest_price")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lowestPriceItems").isArray())
                .andExpect(jsonPath("$.lowestPriceItems.length()").value(0))
                .andExpect(jsonPath("$.totalPrice").value("0"));
    }
}