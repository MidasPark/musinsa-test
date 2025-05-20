package com.musinsa.test.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemAdminDto {
    private long id;
    private String brand;
    private String category;
    private String price;
    private Date createdAt;
    private Date updatedAt;
}
