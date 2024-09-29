package com.example.backend.dto.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ItemDTO {
    private Integer itemsID;
    private String itemsName;
    private String itemsCode;
    private Integer stock;
    private Integer price;
    private Boolean isAvailable;
    private LocalDate lastReStock;
}
