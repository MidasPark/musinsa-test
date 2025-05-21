package com.musinsa.test.repository;

import com.musinsa.test.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("카테고리 목록 조회 테스트")
    void findCategoriesTest() throws InterruptedException {
        itemRepository.deleteAll();

        Item shirt = Item.builder()
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // 시간차 발생 용도
        TimeUnit.SECONDS.sleep(1);

        Item pants = Item.builder()
                .brand("B")
                .category("하의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // 시간차 발생 용도
        TimeUnit.SECONDS.sleep(1);

        Item shoes = Item.builder()
                .brand("C")
                .category("신발")
                .price(30000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirt);
        itemRepository.save(pants);
        itemRepository.save(shoes);

        List<String> categories = itemRepository.findCategories();
        assertThat(categories).hasSize(3);
        assertThat(categories).containsExactlyInAnyOrder("상의", "하의", "신발");
    }

    @Test
    @DisplayName("카테고리별 최저가 상품 조회 테스트")
    void findLowestPriceItemTest() throws InterruptedException {
        itemRepository.deleteAll();

        Item shirt1 = Item.builder()
                .brand("A")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // 시간차 발생 용도
        TimeUnit.SECONDS.sleep(1);

        Item shirt2 = Item.builder()
                .brand("B")
                .category("상의")
                .price(10000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirt1);
        itemRepository.save(shirt2);

        Optional<Item> lowestPriceItem = itemRepository.findFirstByCategoryOrderByPriceAscUpdatedAtDesc("상의");
        assertThat(lowestPriceItem).isPresent();
        assertThat(lowestPriceItem.get().getBrand()).isEqualTo("B");
    }

    @Test
    @DisplayName("카테고리별 최고가 상품 조회 테스트")
    void findHighestPriceItemTest() throws InterruptedException {
        itemRepository.deleteAll();

        Item shirt1 = Item.builder()
                .brand("A")
                .category("상의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // 시간차 발생 용도
        TimeUnit.SECONDS.sleep(1);

        Item shirt2 = Item.builder()
                .brand("B")
                .category("상의")
                .price(20000)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        itemRepository.save(shirt1);
        itemRepository.save(shirt2);

        Optional<Item> highestPriceItem = itemRepository.findFirstByCategoryOrderByPriceDescUpdatedAtAsc("상의");
        assertThat(highestPriceItem).isPresent();
        assertThat(highestPriceItem.get().getBrand()).isEqualTo("A");
    }

    @Test
    @DisplayName("카테고리가 없을 때 빈 결과 반환 테스트")
    void findCategoriesEmptyTest() {
        itemRepository.deleteAll();

        List<String> categories = itemRepository.findCategories();
        assertThat(categories).isEmpty();
    }
}