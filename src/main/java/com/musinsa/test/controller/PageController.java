package com.musinsa.test.controller;

import com.musinsa.test.domain.Item;
import com.musinsa.test.dto.*;
import com.musinsa.test.exception.RecordNotFoundException;
import com.musinsa.test.service.BrandPriceService;
import com.musinsa.test.service.ItemService;
import com.musinsa.test.service.LowestHighestService;
import com.musinsa.test.service.LowestPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final LowestPriceService lowestPriceService;
    private final BrandPriceService brandPriceService;
    private final LowestHighestService lowestHighestService;
    private final ItemService itemService;

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, Musinsa!");
        return "welcome";
    }

    @GetMapping("/implement1")
    public String implement1(Model model) {
        LowestPriceResponseDto lowestPrice = lowestPriceService.getLowestPrice();
        model.addAttribute("lowestPrice", lowestPrice);

        return "implement1";
    }

    @GetMapping("/implement2")
    public String implement2(Model model) {
        BrandLowestPriceResponseDto lowestPriceBrand = brandPriceService.getLowestPriceBrand();
        model.addAttribute("lowestPriceBrand", lowestPriceBrand);

        return "implement2";
    }

    @GetMapping("/implement3")
    public String implement3(
            @RequestParam(name = "categoryName", required = false, defaultValue = "상의") String categoryName,
            Model model)
    {
        model.addAttribute("requestedCategory", categoryName);

        if (!lowestHighestService.isValidCategory(categoryName)) {
            model.addAttribute("error", "존재하지 않는 카테고리 입니다: " + categoryName);
            return "implement3";
        }

        try {
            LowestHighestResponseDto lowestHighest = lowestHighestService.findLowestHighestPrice(categoryName);
            model.addAttribute("lowestHighest", lowestHighest);
        } catch (RecordNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "데이터 조회 중 오류가 발생했습니다");
        }

        return "implement3";
    }

    @GetMapping("/implement4")
    public String implement4List(
            Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ItemAdminDto> itemPage = itemService.getList(pageable);
        model.addAttribute("itemPage", itemPage);
        return "implement4";
    }

    @GetMapping("/implement4-insert")
    public String implement4Insert() {
        return "implement4-insert";
    }

    @GetMapping("/implement4-update/{itemId}")
    public String implement4Update(Model model, @PathVariable long itemId) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return "implement4-update";
    }
}
