package com.example.backend.dto.customer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CustomerDTO {
    private Integer customerID;
    private String name;
    private String address;
    private String code;
    private String phone;
    private Boolean isActive;
    private String pic;
    private LocalDate lastOrderDate;
}