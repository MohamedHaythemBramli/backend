package com.example.backend.dto.order;

import com.example.backend.entities.CustomerEntity;
import com.example.backend.entities.ItemEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OrderDTO {
    private Integer orderId;
    private String orderCode;
    private LocalDate orderDate;
    private Integer totalPrice;
    private Integer quantity;
    private CustomerEntity customer;
    private ItemEntity item;
}
