package com.musinsa.test.service;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.ItemAdminDto;
import com.musinsa.test.dto.ItemRequestDto;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.repository.ItemRepository;
import com.musinsa.test.util.PriceFormatter;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * ID를 이용해 상품을 찾음
     *
     * @param itemId Item ID
     * @return Item 리턴
     */
    @Transactional(readOnly = true)
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RecordNotFoundException("해당하는 상품을 찾을 수 없습니다."));
    }

    /**
     * ItemRequestDto 를 이용해 상품을 생성
     *
     * @param requestDto requestDto
     * @return Item 리턴
     */
    @Transactional
    public Item createItem(ItemRequestDto requestDto) {
        validateUpdateDto(requestDto);

        Item newItem = Item.builder()
                .brand(requestDto.getBrand())
                .category(requestDto.getCategory())
                .price(requestDto.getPrice())
                .build();

        return itemRepository.save(newItem);
    }

    /**
     * ItemRequestDto 를 이용해 상품을 갱신
     *
     * @param requestDto requestDto
     * @param itemId Item ID
     * @return Item 리턴
     */
    @Transactional
    public Item updateItem(ItemRequestDto requestDto, Long itemId) {
        validateUpdateDto(requestDto);

        Item item = getItemById(itemId);
        item.setBrand(requestDto.getBrand());
        item.setCategory(requestDto.getCategory());
        item.setPrice(requestDto.getPrice());

        return itemRepository.save(item);
    }

    /**
     * 상품 아이디를 이용해 상품을 찾고 삭제
     *
     * @param itemId Item ID
     */
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = getItemById(itemId);

        itemRepository.delete(item);
    }

    /**
     * 페이지네이션 처리된 ItemAdminDto 를 리턴
     *
     * @param pageable Object
     * @return Page<ItemAdminDto>
     */
    @Transactional(readOnly = true)
    public Page<ItemAdminDto> getList(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return itemPage.map(this::convertToDto);
    }

    /**
     * Item 을 ItemAdminDto 로 전환
     *
     * @param item Object
     * @return ItemAdminDto
     */
    private ItemAdminDto convertToDto(Item item) {
        return ItemAdminDto.builder()
                .id(item.getId())
                .brand(item.getBrand())
                .category(item.getCategory())
                .price(PriceFormatter.KorFormat(item.getPrice()))
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    /**
     * item 저장/갱신전에 간단한 유호성 검사
     *
     * @param requestDto ItemRequestDto
     */
    private void validateUpdateDto(ItemRequestDto requestDto) {
        if (requestDto.getPrice() == null) {
            throw new IllegalArgumentException("가격은 필수입니다.");
        }

        if (requestDto.getBrand() == null || requestDto.getBrand().isEmpty()) {
            throw new IllegalArgumentException("브랜드는 필수입니다.");
        }

        if (requestDto.getCategory() == null || requestDto.getCategory().isEmpty()) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }

        if (requestDto.getPrice() <= 0) {
            throw new IllegalArgumentException("가격은 0원보다 커야 합니다.");
        }
    }
}
