package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.LowestPriceByCategoryDto;
import com.musinsa.test.dto.LowestPriceResponseDto;
import com.musinsa.test.repository.ItemRepository;
import com.musinsa.test.util.PriceFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LowestPriceService {

    private final ItemRepository itemRepository;

    public LowestPriceResponseDto getLowestPrice() {
        // 람다 사용을 위해 Atomic 타입 사용
        AtomicInteger totalPrice = new AtomicInteger();

        // 카테고리 목록
        List<String> categories = itemRepository.findCategories();
        if (categories.isEmpty()) {
            return new LowestPriceResponseDto(new ArrayList<>(), "0");
        }

        List<LowestPriceByCategoryDto> lowestPriceItems = new ArrayList<>();
        for (String category : categories) {
            Optional<Item> currentLowestPrice = itemRepository.findFirstByCategoryOrderByPriceAscUpdatedAtDesc(category);
            currentLowestPrice.ifPresent(item -> {
                lowestPriceItems.add(
                        new LowestPriceByCategoryDto(item.getCategory(), item.getBrand(), PriceFormatter.KorFormat(item.getPrice()))
                );
                totalPrice.addAndGet(item.getPrice());
            });
        }

        return new LowestPriceResponseDto(lowestPriceItems, PriceFormatter.KorFormat(totalPrice.get()));
    }
}
