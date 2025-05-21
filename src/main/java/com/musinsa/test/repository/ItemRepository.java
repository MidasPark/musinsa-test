package com.musinsa.test.repository;

import com.musinsa.test.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 중복된 값을 제거한 모든 카테고리 조회
    @Query("SELECT DISTINCT i.category FROM Item i ORDER BY i.category ASC")
    List<String> findCategories();

    // 카테고리에서 가격이 가장 낮은 상품 하나를 조회
    // 동일한 가격이 존재한다면 최근에 생성된 상품을 리턴
    Optional<Item> findFirstByCategoryOrderByPriceAscUpdatedAtDesc(String category);

    // 카테고리에서 가격이 가장 낮은 상품 하나를 조회
    // 동일한 가격이 존재한다면 최근에 생성된 상품을 리턴
    Optional<Item> findFirstByCategoryOrderByPriceDescUpdatedAtAsc(String category);
}
