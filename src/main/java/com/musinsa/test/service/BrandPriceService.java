package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.BrandLowestPriceDto;
import com.musinsa.test.dto.BrandLowestPriceResponseDto;
import com.musinsa.test.dto.CategoryPriceDto;
import com.musinsa.test.repository.ItemRepository;
import com.musinsa.test.util.PriceFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandPriceService {

    private final ItemRepository itemRepository;

    /**
     * 현재는 요청 시마다 전체 데이터를 기반으로 계산
     * 대규모 트래픽이 요규되는 서비스에서는 성능 최적화를 위해 캐싱 처리나 쿼리 최적화 등아 고려해야 됨
     *
     * @return 최소 총액 브랜드 정보 또는 조건 미충족 시 빈 응답
     */
    public BrandLowestPriceResponseDto getLowestPriceBrand() {
        List<Item> allItems = itemRepository.findAll();
        if (allItems.isEmpty()) {
            return new BrandLowestPriceResponseDto();
        }

        // 카테고리 목록
        List<String> allCategories = itemRepository.findCategories();
        if (allCategories.isEmpty()) {
            return new BrandLowestPriceResponseDto();
        }

        // 브랜드별 상품 그룹화
        Map<String, List<Item>> itemsByBrand = allItems.stream().collect(Collectors.groupingBy(Item::getBrand));

        BrandLowestPriceDto bestLowestBrands = null;

        // 비교를 위해 최대값으로 세팅
        int minTotalPrice = Integer.MAX_VALUE;

        // 최소값을 찾기 위해 모든 브랜드 반복
        for (Map.Entry<String, List<Item>> brandEntry : itemsByBrand.entrySet()) {
            String currentBrandName = brandEntry.getKey();
            List<Item> currentBrandItems = brandEntry.getValue();

            Map<String, Item> currentBrandLowestItems = new HashMap<>();

            // 람다 사용을 위해 Atomic 타입 사용
            AtomicInteger currentBrandTotalPrice = new AtomicInteger();
            AtomicBoolean isHasCategories = new AtomicBoolean(true);

            for (String category : allCategories) {
                Optional<Item> minPriceItem = currentBrandItems.stream()
                        .filter(item -> item.getCategory().equals(category))
                        .min(Comparator.comparingInt(Item::getPrice));

                minPriceItem.ifPresentOrElse(
                    item -> {
                        currentBrandLowestItems.put(category, item);
                        currentBrandTotalPrice.addAndGet(item.getPrice());
                    },
                    () -> {
                        isHasCategories.set(false);
                    }
                );

                if (!isHasCategories.get()) {
                    break;
                }
            }

            // 모든 카테고리를 가진 브랜드인지 확인 후 현재 최소 총액보다 작으면 업데이트
            if (isHasCategories.get() && currentBrandTotalPrice.get() < minTotalPrice) {
                minTotalPrice = currentBrandTotalPrice.get();
                List<CategoryPriceDto> categoryPrices = new ArrayList<>();

                for (String category : allCategories) {
                    Item item = currentBrandLowestItems.get(category);
                    categoryPrices.add(new CategoryPriceDto(category, PriceFormatter.KorFormat(item.getPrice())));
                }

                bestLowestBrands = new BrandLowestPriceDto(
                        currentBrandName,
                        categoryPrices,
                        PriceFormatter.KorFormat(minTotalPrice)
                );
            }
        }

        if (bestLowestBrands == null) {
            return new BrandLowestPriceResponseDto();
        }

        return new BrandLowestPriceResponseDto(bestLowestBrands);
    }
}
