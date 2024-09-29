package com.example.backend.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CreateCustomerRequest {
    @NotBlank(message = "{customer.name.required}")
    private String name;

    @NotBlank(message = "{customer.address.required}")
    private String address;

    @NotBlank(message = "{customer.code.required}")
    private String code;

    @NotBlank(message = "{customer.phone.required}")
    private String phone;

    @NotNull(message = "{customer.isActive.required}")
    private Boolean isActive;

    private MultipartFile pic;
}