package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.ItemBrandPriceDto;
import com.musinsa.test.dto.LowestHighestResponseDto;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.repository.ItemRepository;
import com.musinsa.test.util.PriceFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LowestHighestService {

    private final ItemRepository itemRepository;

    /**
     * 주어진 카테고리가 존재하는 카테고리인 확인
     *
     * @param category 카테고리
     */
    public Boolean isValidCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }

        try {
            List<String> categories = itemRepository.findCategories();
            return categories != null && categories.contains(category);
        } catch ( Exception e ) {
            return false;
        }
    }

    /**
     * 주어진 카테코리로 최저가 최고가 아이템을 찾기
     *
     * @param category 카테고리
     */
    public LowestHighestResponseDto findLowestHighestPrice(String category) {
        List<ItemBrandPriceDto> lowestPrices = new ArrayList<>();
        List<ItemBrandPriceDto> highestPrices = new ArrayList<>();

        Optional<Item> lowestPriceItem = itemRepository.findFirstByCategoryOrderByPriceAscUpdatedAtDesc(category);
        Optional<Item> highestPriceItem = itemRepository.findFirstByCategoryOrderByPriceDescUpdatedAtAsc(category);

        if (lowestPriceItem.isEmpty() && highestPriceItem.isEmpty()) {
           throw new RecordNotFoundException("상품이 존재하지 않습니다.");
        }

        // 최저가
        lowestPriceItem.ifPresent(item -> {
            ItemBrandPriceDto currentDto = new ItemBrandPriceDto(item.getBrand(), PriceFormatter.KorFormat(item.getPrice()));
            lowestPrices.add(currentDto);
        });

        // 최고가
        highestPriceItem.ifPresent(item -> {
            ItemBrandPriceDto currentDto = new ItemBrandPriceDto(item.getBrand(), PriceFormatter.KorFormat(item.getPrice()));
            highestPrices.add(currentDto);
        });

        return new LowestHighestResponseDto(category, lowestPrices, highestPrices);
    }
}
