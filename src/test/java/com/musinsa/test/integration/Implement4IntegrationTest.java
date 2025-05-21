package com.musinsa.test.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.ItemRequestDto;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Implement4IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Item testItem;

    @BeforeEach
    void setUp() {
        testItem = Item.builder()
                .brand("TestBrand")
                .category("TestCategory")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        testItem = itemRepository.save(testItem);
    }

    @Test
    @DisplayName("새로운 상품 추가 API 통합 테스트")
    void createItemIntegrationTest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setBrand("NewBrand");
        requestDto.setCategory("NewCategory");
        requestDto.setPrice(15000);

        mockMvc.perform(post("/api/implement4/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

        Optional<Item> createdItem = itemRepository.findAll().stream()
                .filter(item -> item.getBrand().equals("NewBrand") && item.getCategory().equals("NewCategory"))
                .findFirst();

        assertThat(createdItem).isPresent();
        assertThat(createdItem.get().getPrice()).isEqualTo(15000);
    }

    @Test
    @DisplayName("유효하지 않은 상품 추가 시 400 반환 테스트 - 가격 누락")
    void createItemInvalidPriceIntegrationTest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setBrand("NewBrand");
        requestDto.setCategory("NewCategory");

        mockMvc.perform(post("/api/implement4/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error"));
    }

    @Test
    @DisplayName("유효하지 않은 상품 추가 시 400 반환 테스트 - 브랜드 누락")
    void createItemInvalidBrandIntegrationTest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setCategory("NewCategory");
        requestDto.setPrice(15000);

        mockMvc.perform(post("/api/implement4/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error"));
    }

    @Test
    @DisplayName("유효하지 않은 상품 추가 시 400 반환 테스트 - 카테고리 누락")
    void createItemInvalidCategoryIntegrationTest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setBrand("NewBrand");
        requestDto.setPrice(15000);

        mockMvc.perform(post("/api/implement4/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error"));
    }

    @Test
    @DisplayName("기존 상품 업데이트 API 통합 테스트")
    void updateItemIntegrationTest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setBrand("UpdatedBrand");
        requestDto.setCategory("UpdatedCategory");
        requestDto.setPrice(20000);

        mockMvc.perform(put("/api/implement4/item/" + testItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("정상적으로 갱신되었습니다."));

        Optional<Item> updatedItem = itemRepository.findById(testItem.getId());
        assertThat(updatedItem).isPresent();
        assertThat(updatedItem.get().getBrand()).isEqualTo("UpdatedBrand");
        assertThat(updatedItem.get().getCategory()).isEqualTo("UpdatedCategory");
        assertThat(updatedItem.get().getPrice()).isEqualTo(20000);
    }

    @Test
    @DisplayName("존재하지 않는 상품 업데이트 시 404 반환 테스트")
    void updateNonExistentItemIntegrationTest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setBrand("UpdatedBrand");
        requestDto.setCategory("UpdatedCategory");
        requestDto.setPrice(20000);

        mockMvc.perform(put("/api/implement4/item/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("상품 삭제 API 통합 테스트")
    void deleteItemIntegrationTest() throws Exception {
        mockMvc.perform(delete("/api/implement4/item/" + testItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("정상적으로 삭제되었습니다."));

        Optional<Item> deletedItem = itemRepository.findById(testItem.getId());
        assertThat(deletedItem).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시 404 반환 테스트")
    void deleteNonExistentItemIntegrationTest() throws Exception {
        mockMvc.perform(delete("/api/implement4/item/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}