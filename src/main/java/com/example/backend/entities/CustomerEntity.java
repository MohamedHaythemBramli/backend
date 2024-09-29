package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Integer customerID;

    @Column(name = "customer_name", length = 64)
    private String customerName;

    @Column(name = "customer_address", length = 255)
    private String customerAddress;

    @Column(name = "customer_code", length = 32)
    private String customerCode;

    @Column(name = "customer_phone", length = 32)
    private String customerPhone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_order_date")
    private LocalDate lastOrderDate;

    @Column(name = "pic", length = 255)
    private String pic;
}
