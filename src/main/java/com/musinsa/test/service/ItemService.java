package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.ItemRequestDto;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RecordNotFoundException("해당하는 아이템을 찾을 수 없습니다."));
    }

    @Transactional
    public Item createItem(ItemRequestDto requestDto) {
        Item newItem = Item.builder()
                .brand(requestDto.getBrand())
                .category(requestDto.getCategory())
                .price(requestDto.getPrice())
                .build();

        return itemRepository.save(newItem);
    }

    @Transactional
    public Item updateItem(ItemRequestDto requestDto, Long itemId) {
        Item item = getItemById(itemId);

        item.setBrand(requestDto.getBrand());
        item.setCategory(requestDto.getCategory());
        item.setPrice(requestDto.getPrice());

        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = getItemById(itemId);

        itemRepository.delete(item);
    }
}
